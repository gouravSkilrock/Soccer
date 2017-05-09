var commonApp = {};
/**
 * Parameters used for form validations.
 * @since 06-Oct-2015
 */
var oldLeague;
var oldHomeTeam;
var oldHomeTeamOdd;
var oldAwayTeam;
var oldAwayTeamOdd;
var oldStartTime;
var oldEndTime;
var oldVenue;
var oldDrawOdd;			
var isCheckedInEventInsertion;
/**
 * Parameters used in functions for validations
 * @since 29-Sep-2015
 */
var validateTeam;
var validateCode;
var teamCodeToBeUpdated;
commonApp.ui = {
	/**
	 * Fetches options upon change in game selected in league team management.
	 * @since 28-Sep-2015
	 */
	GameIdChange : {
		element : "#gameId",
		init : function(){
				$("#gameId").change(function() {
					var game = $("#gameId").val();
					if (game == "-1") {
						$("#options").hide();
						$("#addLeagueDiv").hide();
						$("#addTeamDiv").hide();
						$("#mapLeagueTeamDiv").hide();
						$("#result").hide();
						
					} else {
						$("#result").hide();
						$("#options").show();
					}
				});
		}
	},
	/**
	 * Shows add league form when Add League button is clicked in League Team Management.
	 * @since 28-Sep-2015
	 */
	AddLeagueButton : {
		element : "#addLeague",
		init : function(){
				$("#addLeague").click(function() {
					$("#addLeagueDiv").show();
					$("#addTeamDiv").hide();
					$("#mapLeagueTeamDiv").hide();
					reset();
				});
		}
	},
	/**
	 * Shows add team form when Team Management button is clicked in League Team Management.
	 * @since 28-Sep-2015
	 */
	TeamManagementButton : {
		element : "#teamMgmt",
		init : function(){
			$("#teamMgmt").click(function() {
				$("#addLeagueDiv").hide();
				$("#addTeamDiv").show();
				$("#updateTeamInfo").hide();
				$("#updateTeamData").hide();
				$("#mapLeagueTeamDiv").hide();
				reset();
			});
		}
	},
	/**
	 * Shows league team mapping form when Map League-Team button is clicked in League Team Management.
	 * @since 28-Sep-2015
	 */
	MapLeagueTeamButton : {
		element : "#mapLeagueTeam",
		init : function(){
				$("#mapLeagueTeam").unbind().click(function(e) {
					e.preventDefault();
					$("#addLeagueDiv").hide();
					$("#addTeamDiv").hide();
					var requestParam = path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/getLeagues.action';
					var result = _ajaxCallText(requestParam,'');
					$("#mapLeagueTeamDiv").html(result);
					$("#mapLeagueTeamDiv").show();
					reset();
				});
		}
	},
	/**
	 * Checks whether league name already exists or not.
	 * If league name exists it shows error else sends an AJAX call to the server to insert new league.
	 * @since 28-Sep-2015
	 */
	AddNewLeagueButton : {
		element : "#addLeagueBtn",
		init : function(){
				$('#addLeagueBtn').unbind().click(function(e) {
					e.preventDefault();
					var l_name = $("#leagueName").val();
					var leagueExists = false;
					var containSpecialSymbol = $.trim(l_name).match(/^[^&,:@$%]+$/g) !== null ? true:false;
					if ($.trim(l_name).length == 0) {
						$("#l_error1").text("Please Enter League Name");
						$("#league_exists_error").text("");
						return false;
					}
					else if(containSpecialSymbol == false){
						$("#l_error1").text("Please Enter Correct League Name");
						$("#league_exists_error").text("");
						return false;
					}
					for (i = 0; i < league.length; i++) {
						if (league[i].toUpperCase() == $.trim(l_name).toUpperCase()) {
							leagueExists = true;
						}
					}
					if (leagueExists == true) {
						$("#league_exists_error").text("League Already Exists");
						$("#l_error1").text("");
						return false;
					} 
					else {
						var requestParam = path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/sleAddLeague.action?gameId='+ $("#gameId").val() +'&leagueName='+ encodeURIComponent($("#leagueName").val());
						var result = _ajaxCallText(requestParam,'');
						if (result != undefined	&& result.length > 0) {
							league.push(l_name);
							$("#addLeagueDiv").hide();
							$("#addTeamDiv").hide();
							$("#mapLeagueTeamDiv").hide();
							$("#result").show();
							$("#result").html(result);
						}
					}
				});
		}
	},
	/**
	 * Checks whether team name and team code already exists or not by calling the validateTeamForm().
	 * If any match found it shows error otherwise sends an AJAX call to the server to insert team name, team code
	 * and venue name.
	 * @since 28-Sep-2015
	 */
	AddNewTeamButton : {
		element : "#addTeamBtn",
		init : function(){
				$('#addTeamBtn').unbind().click(function(e) {
					e.preventDefault();
					var t_name = $("#teamName").val();
					var t_code = $("#teamCode").val();
					var formno=1;
					validateTeamForm(t_name,t_code,team,teamCode,formno);
					if(validateTeam !=0 || validateCode !=0 ){
						return false;
					}
					else{
						var requestParam = path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/sleAddTeam.action?gameId='+ $("#gameId").val()+ '&teamName='+ encodeURIComponent($("#teamName").val())+ '&teamCode='+ encodeURIComponent($("#teamCode").val());
						var result = _ajaxCallText(requestParam,'');
						if (result != undefined && result.length > 0) {
							team.push(t_name);
							teamCode.push(t_code);
							resetUpdateTeamError();
							$("#addLeagueDiv").hide();
							$("#addTeamDiv").hide();
							$("#mapLeagueTeamDiv").hide();
							$("#result").show();
							$("#result").html(result);
							$("#teamInsert").show();
							$("#teamUpdate").hide();
						}
					}
				});
		}
	},
	/**
	 * When add team option is selected the add new team form is displayed
	 * @since 03-Oct-2015
	 */
	AddNewTeamRadioSelected : {
		element : "#addNewTeamBtn",
		init : function(){
				$("#addNewTeamBtn").change(function(){
					$("#updateTeamBtn").prop('checked', false);
					$("#addNewTeam").show();
					$("#updateTeamInfo").hide();
					$("#updateTeamData").hide();
					reset();
				});
		}	
	},
	/**
	 * When update team option is selected in team management it fetches all the teams that are active.
	 * @since 03-Oct-2015
	 */
	UpdateTeamRadioSelected : {
		element : "#updateTeamBtn",
		init : function(){
				$("#updateTeamBtn").unbind().change(function(e){
					e.preventDefault();
					$("#addNewTeamBtn").prop('checked', false);
					var requestParam = path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/getTeams.action';
					var result = _ajaxCallText(requestParam,'');
					$("#updateTeamInfo").html(result);
					$("#updateTeamInfo").show();
					$("#addNewTeam").hide();
				});
		}
	},
	/**
	 * When a particular team is selected for updation it brings its corresponding data in the 
	 * update team form.
	 * @since 03-Oct-2015
	 */
	SelectTeamForUpdation : {
		element : "#teamInfo",
		init : function(){
				$("#teamInfo").change(function(){
					if($("#teamInfo").val() == -1){
						$("#updateTeamData").hide();
						$("#addNewTeam").hide();
						reset();
					}
					else{
						var teamIndex = team.indexOf($("#teamInfo option:selected").text());
						teamCodeToBeUpdated = teamCode[teamIndex];
						$("#updateTeamData").show();
						$("#addNewTeam").hide();
						$("#updateTeamName").val($("#teamInfo option:selected").text());
						$("#updateTeamCode").val(teamCodeToBeUpdated);
						resetUpdateTeamError();
					}
				});
		}	
	},
	/**
	 * When update button is clicked it validates the form and sends the data to server through 
	 * an AJAX call.
	 * @since 28-Oct-2015
	 */
	UpdateTeamInformation : {
		element : "#updateTeamInfoBtn",
		init : function(){
				$("#updateTeamInfoBtn").unbind().on("click",function(e){
					e.preventDefault();
					var t_name = $("#updateTeamName").val();
					var t_code = $("#updateTeamCode").val();
					var teamSelected = $("#teamInfo option:selected").text();
					var formno=2;
					validateTeamForm(t_name,t_code,team,teamCode,formno);
					if(validateTeam !=0 || validateCode !=0 ){
						return false;
					}
					else{
						var requestParam = path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/updateTeams.action?teamId='+ $("#teamInfo").val() +'&teamName='+ encodeURIComponent(t_name) +'&teamCode=' +encodeURIComponent(t_code);
						var result = _ajaxCallText(requestParam,'');
						resetAddTeamError();
						teamIndex = team.indexOf($("#teamInfo option:selected").text());
						team[teamIndex] = t_name;
						teamIndex = teamCode.indexOf(teamCodeToBeUpdated);
						teamCode[teamIndex] = t_code;
						$("#addLeagueDiv").hide();
						$("#addTeamDiv").hide();
						$("#mapLeagueTeamDiv").hide();
						$("#result").show();
						$("#result").html(result);
						$("#teamUpdate").show();
						$("#teamInsert").hide();
					}
				});
		}
	},
	/**
	 * When league is selected in map league team form an AJAX call goes to server which fetches all the mapped 
	 * and unmapped teams for that particular league base on leaguId.
	 * @since 28-Oct-2015
	 */
	LeagueSelectInMapLeagueTeam : {
		element : "#leagueId",
		init : function(){
				$('#leagueId').change(function() {
					if ($(this).val() != -1) {
						var selectedGameId = $('#gameId').val();
						$('#gameId').val(selectedGameId);
						formSubmit('mapLeagueTeamForm','mapping');
						var mappedLT = [];
					} else {
						$('#mapping').empty();
					}
				});
		}
	},
	/**
	 * When add button clicked in map league team form it copies teams from unmapped teams drop down to mapped teams drop down
	 * and deletes the teams from unmapped teams drop down.
	 * @since 28-Oct-2015
	 */
	AddFromUnmappedTeams : {
		element : "#add",
		init : function(){
			$('#add').click(function(e) {
				var selectedOpts = $('#allTeam option:selected');
				if (selectedOpts.length == 0) {
					alert("Please select atleast one team to map!");
					e.preventDefault();
				}
				if (selectedOpts.length != 0) {
					$('#mappedTeam').append($(selectedOpts).clone());
					$(selectedOpts).remove();
					defaultSelect();
					e.preventDefault();
				}
			});
		}
	},
	/**
	 * When remove button clicked in map league team form it copies teams from mapped teams drop down to unmapped teams drop down
	 * and deletes the teams from mapped teams drop down.
	 * @since 28-Oct-2015
	 */
	AddFromMappedTeams : {
		element : "#remove",
		init : function(){
			$('#remove').click(function(e) {
				e.preventDefault();
				var selectedOpts = $('#mappedTeam option:selected');
				if (selectedOpts.length == 0) {
					alert("Please select atleast one team to unmap!");
					e.preventDefault();
				}
			$('#allTeam').append($(selectedOpts).clone());
				$(selectedOpts).remove();
				defaultSelect();
			});
		}
	},
	/**
	 * When submit in Map League-Team is clicked it validates the map league team form and sends an AJAX call to the server
	 * to update the league team mappind information in the database.
	 * @since 29-Sep 2015
	 */
   MapLeagueTeamSubmitButton : {
		element : "#mapLeagueTeamBtn",
		init : function(){
				$('#mapLeagueTeamBtn').on("click",function(e) {
					e.preventDefault();
					var gameId = $("#gameId").val();
					var mappedLT = [];
					var flag = 0;
					$('#mappedTeam option').each(function() {
						mappedLT.push($(this).attr('value'));
					});
					var alreadyMapped = [];
					$('#alreadyMapped option').each(function() {
						alreadyMapped.push($(this).attr('value'));
					});
					var noOfAlreadyMapped = alreadyMapped.length;
					var noOfTeams = mappedLT.length;
					var is_same = mappedLT.length == alreadyMapped.length && mappedLT.every(function(element, index) {
					    return element === alreadyMapped[index]; 
						});
					if(noOfTeams !=0 && is_same == true){
						alert("Please add or remove atleast one team!");
						return false;
					}
					if (noOfTeams == 0 && alreadyMapped == 0) {
						alert("Please select atleast one team!");
						return false;
					} 
					else {
						var requestParam = path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/mapLeagueTeam.action?gameId='+ gameId+ '&leagueId='+ $("#leagueId").val()+ '&mappedTeam='+ mappedLT;
						var eventData = _ajaxCallText(requestParam,'');
						if (eventData != undefined && eventData.length > 0) {
							$("#addLeagueDiv").hide();
							$("#addTeamDiv").hide();
							$("#result").show();
							$("#mapLeagueTeamDiv").hide();
							$("#result").html(eventData);
						}
					}
				});
		}
	},
	/**
	 * When any game is selected it resets the options in game type & all the errors and also hide all the divs
	 * @since 06-Oct-2015
	 */
	GameSelectInEventManagement : {
		element : "#event_gameId",
		init : function(){
			$("#event_gameId").change(function(){
				$("#gameTypeId").val(-1);
				$("#game_error").hide();
				$("#game_type_error").hide();
				$("#drawInfo").hide();
				$("#errorDiv").hide();
				$("#eventList").hide();
				resetUpdateError();
			});
		}
	},
	/**
	 * When any game type is selected all the divs and error s are hidden are reset
	 * @since 06-Oct-2015
	 */
	GameTypeSelect : {
		element : "#gameTypeId",
		init : function(){
				$("#gameTypeId").change(function(){
					$("#game_error").hide();
					$("#game_type_error").hide();
					$("#drawInfo").hide();
					$("#errorDiv").hide();
					$("#eventList").hide();
					resetUpdateError();
				});
		}
	},
	/**
	 * When after selecting game and game type search button is clicked then first form is validated by calling validateEventMgmtForm()
	 * and storing its return value in isError.If isError is true, error is shown otherwise an AJAX call goes to server which fetches the 
	 * draw list.
	 * @since 06-Oct-2015
	 */
	SearchGameTypeDraws : {
		element : "#searchDrawBtn",
		init : function(){
				$("#searchDrawBtn").unbind().click(function(e){
					e.preventDefault();
					resetUpdateError();
					var isError = validateEventMgmtForm();		
					if(isError == true){
						return false;
					}
					else{
						var requestParam = path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/getGameTypeDrawInformation.action?gameId='+$("#event_gameId").val()+'&gameTypeId='+$("#gameTypeId").val();
						var eventData = _ajaxCallText(requestParam,'');
						if (eventData != undefined && eventData.length > 0) {
							$("#drawInfo").html(eventData);
							$("#drawInfo").show();
							$("#errorDiv").hide();
							
						}
						else{
							$("#drawInfo").hide();
							$("#updateEventDiv").hide();
							$("#errorDiv").show();
						}
					}
				});
		}
	},
	/**
	 * When a draw is selected from the draw list this function sends an AJAX call to the server to fetch the events mapped for
	 * that draw if selected key value is not -1.If no events are mapped it shows error otherwise it fetches all the required
	 *  information regarding events and draw sale start time.
	 *  @since 06-Oct-2015
	 */
	DrawListSelect : {
		element : "#draw_Id",
		init : function(){
				$("#draw_Id").unbind().change(function(e) {
					e.preventDefault();
					resetUpdateError();
					if($("#draw_Id").val() == -1){
						$("#eventMsgDiv").hide();
						$("#eventDiv").hide();	
						$("#updateEventDiv").hide();
						$("#drawWithSameEvents").hide();
						return false;
					}
					else{
						var requestParam = path+ '/com/skilrock/sle/web/merchantUser/drawMgmt/Action/getMappedEventInformation.action?drawId='+ $("#draw_Id").val()+"&gameId="+ $("#event_gameId").val();
						var eventData = _ajaxCallText(requestParam, '');
						if (eventData != undefined && eventData.length > 0) {
							$("#eventList").html(eventData);
							getInfo();
							if(mappedEventId.length == 0){
								$("#eventList").hide();
								$("#eventDiv").hide();
								$("#eventMsgDiv").show();
							}
							else{
								$("#eventDiv").show();
								$("#eventList").show();
								$("#eventMsgDiv").hide();
							}
						}
					}
				});
		}
	},
	/**
	 * When any event is selected then based on its eventId data is set and displayed in update event form
	 * @since 06-Oct-2015
	 */
	EventSelection : {
		element : "input:radio[name=updateEventInfo]",
		init : function(){
				$("input:radio[name=updateEventInfo]").on('change',(function(){
					resetUpdateError();
					$('#venueSameAsHomeTeamCheck').prop('checked',false);
					var requestParam = path+ '/com/skilrock/sle/web/merchantUser/drawMgmt/Action/fetchDrawInfoForParticularEvent.action?gameId='+$("#event_gameId").val()+'&eventId='+ $("input[name='updateEventInfo']:checked").val();
					var eventData = _ajaxCallText(requestParam, '');
					if(eventData != undefined && eventData.length>0){
						$("#drawWithSameEvents").html(eventData);
						$("#drawWithSameEvents").show();
					}
					$('#mappedEventsTable tr').each(function(){$(this).find('td:last').text("");});
					$("#updateSuccess").hide();
					$("#updateEventDiv").show();
					var saleTime = new Date(Math.min.apply(null,drawWithSameEventSaleTime));
					var cTime = $("#serverDate").val().replace(/\-/g, "/");
					var currentTime = new Date(cTime);
					if(saleTime < currentTime){
						$('#leagueId').attr("disabled", true);
						$('#venue_Name_Entered_TextBox').attr("disabled", true);
						$('#sameAsHomeTeamChkBox').hide();
						$('#homeTeamId').attr("disabled", true);
						$('#awayTeamId').attr("disabled", true);
					}
					else{
						$('#sameAsHomeTeamChkBox').show();
					}
					getInfo();
					var event = $("input[name='updateEventInfo']:checked").val();
					var eventIndex = mappedEventId.indexOf(event);
					setInfo(eventIndex);
				}));
		}
	},
	GetMappedTeams:{
		element : "#leagueId",
		init : function(){
			$('#leagueId').change(function(){
				if($(this).val()!=-1){
					$("#event_gameId").val();
					var requestParam = path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/fetchLeagueTeamData.action?gameId='+$("#event_gameId").val()+'&leagueId='+ $("#leagueId").val();
					var result = _ajaxCallJson(requestParam,'');		
					
				    var homeTeam = $('#homeTeamId');
				    var awayTeam=$('#awayTeamId');
				    homeTeam.find('option').remove();
				    awayTeam.find('option').remove();
                    $('<option>').val(-1).text("-- Please Select --").appendTo(homeTeam);
                    $('<option>').val(-1).text("-- Please Select --").appendTo(awayTeam);
                    for(var i=0;i<result.length;i++){
                    	$('<option>').val(result[i].teamId).text(result[i].teamName).appendTo(homeTeam);
                    	$('<option>').val(result[i].teamId).text(result[i].teamName).appendTo(awayTeam);
                     }
				}else{
					
				}
			});
		}
	},
	
	/**
	 * Updates event changes by validating whether any entries has been changed or not or event updation form
	 * satisfies all validation criteria.If any of validations are found to be not satisfied, error is displayed, otherwise
	 * an AJAX call goes to server to update the changes regarding event based on event id.
	 * @since 06-Oct-2015
	 */
	UpdateEventManagementInformation : {
		element : "#updateEventBtn",
		init : function(){
				$("#updateEventBtn").on('click',function(){
					/*if(!($("#venueSameAsHomeTeamCheck").prop('checked'))){
						$("#venue_Name_Entered").val($("#venue_Name_Entered_TextBox").val());
					}*/
					$("#venue_Name_Entered").val($("#venue_Name_Entered_TextBox").val());
					venueNameIndex = venueName.indexOf($("#venue_Name_Entered").val());
					if (venueNameIndex == -1) {
						$("#venueId").val(venueNameIndex);
					} else {
						$("#venueId").val(venueIds[venueNameIndex]);
					}
					var eventId = $("input[name='updateEventInfo']:checked").val();
					var updateAt = mappedEventId.indexOf(eventId) + 1;
					var message;
					if(drawWithSameEventFreezeTime.length == 1){
						message = "";
					}
					else{
						message="This event is also mapped for other draws.";
					}
					var isChange = check(updateAt,$("#leagueId").val(),$("#homeTeamId").val(),$("#awayTeamId").val(),$("#venue_Name_Entered").val(),new Date($("#startDateTimePicker").val()),new Date($("#endDateTimePicker").val()),$("#homeTeamOdds").val(),$("#awayTeamOdds").val(),$("#drawOdds").val());
					var isValid = validateUpdateEvent($("#startDateTimePicker").val(),$("#endDateTimePicker").val(),$("#homeTeamId").val(),$("#awayTeamId").val(),$("#leagueId").val(),$("#venue_Name_Entered").val(),$("#homeTeamOdds").val(),$("#awayTeamOdds").val(),$("#drawOdds").val(),$("#draw_Id").val());
					if(isChange == false){
						$("#dateTimeError").hide();
						$("#sameTeamError").hide();
						$("#dateError").hide();
						$("#homeTeamError").hide();
						$("#awayTeamError").hide();
						return false;
					}
					else if(isValid == false){
							$("#atleastOneChangeError").hide();
						return false;
					}
					else{
						$("#atleastOneChangeError").hide();
						$.confirm({
							icon: 'fa fa-warning',
							animationSpeed: 800,
							animation: 'zoom',
							animationBounce: 2.0,
							theme : 'supervan',
							confirmButton: 'Yes',
						    cancelButton: 'No',
						    confirmButtonClass: 'btn-primary',
						    cancelButtonClass: 'btn-primary',
						    title: message,
						    content: 'Do you want to proceed ?',
						    confirm: function(){
						    	var requestParam = path+ '/com/skilrock/sle/web/merchantUser/drawMgmt/Action/updateEventInformation.action?gameId='+$("#event_gameId").val()+'&drawId='+$("#draw_Id").val()+'&eventId='+ eventId +"&leagueId="+ $("#leagueId").val()+"&venueId="+ $("#venueId").val()+"&homeTeamId="+ $("#homeTeamId").val()+"&awayTeamId="+ $("#awayTeamId").val()+"&homeTeamOdds="+ ($("#homeTeamOdds").val()==undefined ?"":$("#homeTeamOdds").val()) +"&awayTeamOdds="+ ($("#awayTeamOdds").val()==undefined ?"":$("#awayTeamOdds").val()) +"&drawOdds=" +($("#drawOdds").val()==undefined ?"":$("#drawOdds").val()) +"&startTime="+ $("#startDateTimePicker").val()+"&endTime="+ $("#endDateTimePicker").val()+"&venue_Name_Entered="+ $("#venue_Name_Entered").val();
								var eventData = _ajaxCallText(requestParam, '');
								if (eventData != undefined && eventData.length > 0) {
									$("input:radio[name='updateEventInfo']").prop('checked',false);
									if(isChange == true){
										$("#atleastOneChangeError").hide();
									}
									$("#eventList").empty();
									$("#eventList").html(eventData);
									$("#eventDiv").show();
									$("#eventList").show();
									$("#dateTimeError").hide();
									$("#dateError").hide();
									$("#sameTeamError").hide();
									$('#mappedEventsTable tr:eq('+(updateAt)+') td:last').text("Updated Successfully !!").css("color","red");
								}
						    },
						    cancel: function(){
						    }
						});
					}
				});
		}
	},
	
	/**
	 * This is for EventManegement...............Create one more function for checkbox change for eventManagement as for Event Insertion
		 */
	
	HomeTeamSelectedInEventManagement : {
		element : "#homeTeamId",
		init : function(){
				$("#homeTeamId").change(function(){
					var homeTeamNameForVenue = $("#homeTeamId option:selected").text();
					if($("#homeTeamId").val() == -1 && $("#venueSameAsHomeTeamCheck").prop('checked')==true){
						$("#venue_Name_Entered_TextBox").val("");
						$("#venue_Name_Entered_TextBox").attr("disabled",true);
					}
					else if($("#homeTeamId").val() != -1 && $("#venueSameAsHomeTeamCheck").prop('checked')==true){
						$("#venue_Name_Entered_TextBox").val(homeTeamNameForVenue);
						$("#venue_Name_Entered_TextBox").attr("disabled",true);
					}
				});
		}
	},
	
	VenueChangeAtEventManagement :{
		element : "#venueSameAsHomeTeamCheck",
		init : function(){
				$('#venueSameAsHomeTeamCheck').click(function(){
					if($("#venueSameAsHomeTeamCheck").prop('checked')==true){
						if($("#homeTeamId").val()==-1){
							alert("Please select home team !!");
							$("#venue_Name_Entered_TextBox").val("");
							$("#venue_Name_Entered_TextBox").attr("disabled",true);
						}
						else{
							$("#venue_Name_Entered_TextBox").val("");
							$("#venue_Name_Entered_TextBox").val($("#homeTeamId option:selected").text());
							$("#venue_Name_Entered").val($("#homeTeamId option:selected").text());
							$("#venue_Name_Entered_TextBox").attr("disabled",true);
						}
					}
					else{
						$("#venue_Name_Entered_TextBox").val($("#old_venue").val());
						$("#venue_Name_Entered_TextBox").attr("disabled",false);
					}
				});
		}	
	},
	
	/**
	 * This fetches data when change sale time button is clicked in Draw Management.
	 * @author Nikhil
	 */
	SaleTimeButton : {
		element : "#saleTime",
		init : function(){
				$('#saleTime').click(function(){
					var isValid=validateDrawListForm();
					if(isValid){
						$('#drawDetailsForm').attr('action', path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/fetchSaleTimeChangeDrawDetail.action');
					}
				});
		}
	},
	
	/**
	 * This method calls in result submission
	 * @author Mayank
	 */
	DrawIdChange : {
		element : "#drawId",
		init : function(){
		 $('#backBtn').show();
				$('#drawId').change(function(){
					$('#errorDiv').html("");
					$('#errorDiv').css("display", "none");
				    $('#resultDivEventData').html("");
				    $('#cnfSubmit').css("display", "none");
					if($(this).val()!=-1) {
					$('#drawDiv').hide();
					$('#tableData').empty();
					var drawId = $(this).prop("selectedIndex");
					var drawGameId=$('select#drawId option:selected').val();
					var eventList = drawTypeList[drawId-1].eventMasterList;
					var eventOptMap = drawTypeList[drawId-1].eventOptionMap;
						
					if (eventList!=undefined && eventList.length>0 ) {
						    $('#body').removeClass();
							$('#body').addClass("bg3");
							$('#divtwo').css("display","none");
							$('#divtwo').html('');
							$('.loadLogo').html('');
							$('#oldDiv').html('');
							$('#oldDiv').css('display','none');
							$('#evtListSize').val(eventList.length);
							$('#resSubForm').css('display','block');
							
							
							var evtDisplayLength=eventList.length;
							var tdHtml = '';
							tdHtml+= '<div class="container container-fluid pageWrap">';
							tdHtml+='<input type="hidden" id="evtListSize"/>';
							tdHtml+='<input type="hidden" id="drawId"/>';
							tdHtml+= '<div class="pageTitleWrap">';
							tdHtml+= '<div class="pageLogo"><img src="'+projectName+'/images/backOffice/merchant/resultSubmission/soccerCashLogo.png" alt="soccer cash"></div>';
							tdHtml+= '<div class="mainTitle">Sports Game Result <span>submission</span></div>';
							tdHtml+= '<div class="secondTitle">'+drawTypeList[drawId-1].drawName+' , '+drawTypeList[drawId-1].drawDateTime+'</div>';
							tdHtml+= '</div>';
							tdHtml+= '<div class="contentOuterWrap">';
							tdHtml+= '<div class="contentInnerWrap soccerPlay">';
							
							var i = 0;
							$.each(eventList, function(key, eventBean) {
								if(i%2 == 0){
								tdHtml+= '<div class="row">';
							 }
								i++;					
								tdHtml += '<div class="col-sm-6">';
					            tdHtml += '<div class="gameDiv sportsLottWrap">';
					            tdHtml += '<div class="gameDivInner">';
					            
					            tdHtml += '<div class="titleAndDateWrap">';
					            tdHtml += '<div class="dateOuterWrap"><div class="dateWrap">'+eventBean.startTime+'</div></div>';
					            tdHtml += '<div class="titleNameWrap">'+eventBean.leagueName+'</div>';
					            tdHtml += '<div class="clearBoth"></div>';
					            
					            tdHtml+= '<div class="row">';
					            
					            tdHtml+= '<div class="col-xs-5 text-center">';
								tdHtml+= '<div class="abbri">'+eventBean.homeTeamCode+'</div>';
					            tdHtml+= '<div class="abbriName">'+eventBean.homeTeamName+'</div>';
					            tdHtml+= '</div>';
					            
					            tdHtml+= '<div class="col-xs-2 text-center" style="margin-top: 15px;"><span class="vsWrap">vs</span></div>';
					  
					            tdHtml+= '<div class="col-xs-5 text-center">';
					            tdHtml+= '<div class="abbri">'+eventBean.awayTeamCode+'</div>';
					            tdHtml+= '<div class="abbriName">'+eventBean.awayTeamName+'</div>';
					            tdHtml+= '</div>';
					            
					            tdHtml+= '</div></div>';
				                
					           var eventOptionsList = eventBean.eventOptionsList;
					            
					           var cancelHtml='';
								if (eventOptionsList!=undefined && eventOptionsList.length>0 ) {
								tdHtml+= '<div class="row selectArea" name="'+eventBean.eventId+'#'+eventBean.homeTeamName+' vs '+eventBean.awayTeamName+'" id ="evtDescDiv">';
								$.each(eventOptMap, function(key, value) {
									if(value =='CANCEL'){
										cancelHtml+='<div class="redBlocker" id="'+eventBean.eventId+'_C" name="'+eventBean.eventId+'_'+value+'" style="display: none;">'
										cancelHtml+='<div class="row">'
										cancelHtml+='<div class="col-xs-3">Match<br>Cancelled</div>'
										cancelHtml+='<div class="col-xs-9">This Bet will Considered As Winning</div>'
										cancelHtml+='</div>'
										cancelHtml+='</div>';
									}else{
										key=key.replace('+','2');
										tdHtml+= '<div class="col-xs-4"><span class="textWrap" id="'+eventBean.eventId+'_'+key+'" name="'+eventBean.eventId+'_'+value+'"onclick="optSelect(this.id)">'+value+'</span>';
					               		tdHtml+= '</div>';
									}
					            
					           
								});
								tdHtml+=cancelHtml;
								 tdHtml+= '</div><div class="cancelBTN" id="'+eventBean.eventId+'" onclick="cancelEventResultSubmit(this.id);">x</div>';
								 
								tdHtml+= '</div></div></div>'	;			
								}
								if(i%2==0){
									tdHtml+= '</div>';
								}
								
							});
								$('#resultDivEventData').append(tdHtml);
								$('#evtListSize').val(parseFloat(eventList.length));	
								$('#drawId').val(drawGameId);
								
							
										
										
						}	
						else {
								$('#tableData').empty();
								$('#drawDiv').show();
								$('#jsErrorDiv').css("display", "none");
								$('#errorDiv').html("Sorry !! No events are mapped with this draw - Please try again later.");
								$('#errorDiv').css("display", "block");
							}	
					}
				});
		}
	},
	/**
	 * For Event Insertion : When home team is selected checkbox appears and should be checked,combobox is filled with home team name
	 * and becomes readonly
	 */
	HomeTeamSelectedInEventInsertion : {
		element : "#homeTeam",
		init : function(){
				$("#homeTeam").change(function(){
					var homeTeamNameForVenue = $("#homeTeam option:selected").text();
					if($("#homeTeam").val() == -1 && $("#venueSameAsHomeTeam").prop('checked')==true){
						$("#venue_Name_textBox").val("");
						$("#venue_Name_textBox").attr("disabled",true);
					}
					else if($("#homeTeam").val() != -1 && $("#venueSameAsHomeTeam").prop('checked')==true){
						$("#venue_Name_textBox").val(homeTeamNameForVenue);
						$("#venue_Name").val(homeTeamNameForVenue);
						$("#venue_Name_textBox").attr("disabled",true);
					}
				});
		}
	},
	
	EventInsertionSubmit :{
		element : "#submitBtn",
		init : function(){
				$("#submitBtn").click(function() {
					venueNameIndex = venueName.indexOf($("#venue_Name").val());
					if(isCheckedInEventInsertion == 1){
						$("#venue_Name").val($("#venue_Name_textBox").val());
					}
					if (venueNameIndex == -1) {
						$("#venue").val(venueNameIndex);
					} else {
						$("#venue").val(venueIds[venueNameIndex]);
					}
				});
		}
	},
	
	VenueChangeAtEventInsertion :{
		element : "#venueSameAsHomeTeam",
		init : function(){
				isCheckedInEventInsertion = 1;
				$('input').on('ifChecked', function(event){
					isCheckedInEventInsertion = 0;
					if($("#homeTeam").val()==-1){
						alert("Please select home team !!");
						$("#venue_Name_textBox").val("");
						$("#venue_Name_textBox").attr("disabled",true);
					}
					else{
						$("#venue_Name_textBox").val($("#homeTeam option:selected").text());
						$("#venue_Name_textBox").attr("disabled",true);
						$("#venue_Name").val($("#homeTeam option:selected").text());
					}
				});
				$('input').on('ifUnchecked', function(event){
					isCheckedInEventInsertion = 1;
					$("#venue_Name_textBox").attr("disabled",false);
					$("#venue_Name_textBox").val("");
					
				});
		}
	},

	/**
	 * Common Utility
	 * @since 28-Sep-2015
	 */
	utility : {
		element : "body",
		init : function() {
				/**
				 * Functions and parameters for League Team Insertion
				 */
			
				/**
				 * This function resets all the errors in league team management.
				 * @since 29-Sep-2015
				 */
				reset = function(){
					$("#l_error1").text("");
					resetAddTeamError();
					resetUpdateTeamError();
					$("#leagueName").val("");
					$("#teamName").val("");
					$("#teamCode").val("");
					$("#updateTeamName").val("");
					$("#updateTeamCode").val("");
					$("#result").hide();
					$('#mapping').empty();
					$("#success").text("");
					$("#league_exists_error").text("");
					$("#leagueId").val(-1); 
					$("#addNewTeamBtn").prop('checked', true);
					$("#updateTeamBtn").prop('checked', false);
					$("#addNewTeam").show();
					$("#updateTeamInfo").hide();
				};
				/**
				 * This function resets all	 the errors in add new team form.
				 * @since 28-Sep-2015
				 */
				resetAddTeamError = function(){
					$("#t_error1").text("");
					$("#t_error2").text("");
					$("#team_exists_error").text("");
					$("#teamCode_exists_error").text("");
				};
				/**
				 * This function resets all the errors in update team form.
				 * @since 28-Sep-2015
				 */
				resetUpdateTeamError = function() {
					$("#update_t_error1").text("");
					$("#update_t_error2").text("");
					$("#update_team_exists_error").text("");
					$("#update_teamCode_exists_error").text("");
				};
				/**
				 * This function validates the add team and update team form.
				 * @param t_name
				 * @param t_code
				 * @param team
				 * @param teamCode
				 * @param formNumber
				 * @since 29-Sep-2015
				 */
				validateTeamForm = function(t_name,t_code,team,teamCode,formNumber) {
					var teamExists = false;
					var teamCodeExists = false;
					var teamSelected = $("#teamInfo option:selected").text();
					var teamNameContainSpecialSymbol = $.trim(t_name).match(/^[^&,:@$%]+$/g) !== null ? true:false;
					var teamCodeContainSpecialSymbol = $.trim(t_code).match(/^[^&,:@$%]+$/g) !== null ? true:false;
					validateTeam = 0;
					validateCode = 0;
					if(formNumber == 1){
						for (i = 0; i < team.length; i++) {
							if (team[i].toUpperCase() == t_name.toUpperCase()) {
								teamExists = true;
							}
						}
						for (i = 0; i < teamCode.length; i++) {
							if (teamCode[i].toUpperCase() == t_code.toUpperCase()) {
								teamCodeExists = true;
							}
						}
					}
					else{
						for (i = 0; i < team.length; i++) {
							if (team[i].toUpperCase()!=teamSelected.toUpperCase() && team[i].toUpperCase() == t_name.toUpperCase() ) {
								teamExists = true;
							}
						}
						for (i = 0; i < teamCode.length; i++) {
							if (teamCode[i].toUpperCase()!=teamCodeToBeUpdated.toUpperCase() && teamCode[i].toUpperCase() == t_code.toUpperCase()) {
								teamCodeExists = true;
							}
						}
					}
					if ($.trim(t_name).length == 0 ){
						$("#t_error1").text("Please Enter Team Name");
						$("#update_t_error1").text("Please Enter Team Name");
						$("#team_exists_error").text("");
						$("#update_team_exists_error").text("");
						validateTeam++;
					} 
					else if(teamNameContainSpecialSymbol == false){
						$("#t_error1").text("Please Enter Correct Team Name");
						$("#update_t_error1").text("Please Enter Correct Team Name");
						$("#team_exists_error").text("");
						$("#update_team_exists_error").text("");
						validateTeam++;
					}
					else if(teamExists == true){
						$("#team_exists_error").text("Team Name Already Exists");
						$("#update_team_exists_error").text("Team Name Already Exists");
						$("#t_error1").text("");
						$("#update_t_error1").text("");
						validateTeam++;
					}
					else{
						$("#team_exists_error").text("");
						$("#update_team_exists_error").text("");
						$("#t_error1").text("");
						$("#update_t_error1").text("");
					}
					if( $.trim(t_code).length == 0) {
						$("#t_error2").text("Please Enter Team Code");
						$("#update_t_error2").text("Please Enter Team Code");
						$("#teamCode_exists_error").text("");
						$("#update_teamCode_exists_error").text("");
						validateCode++;
					}
					else if(teamCodeContainSpecialSymbol == false){
						$("#t_error2").text("Please Enter Correct Team Code");
						$("#update_t_error2").text("Please Enter Correct Team Code");
						$("#teamCode_exists_error").text("");
						$("#update_teamCode_exists_error").text("");
						validateCode++;
					}
					else if(teamCodeExists == true){
						$("#teamCode_exists_error").text("Team Code Already Exists");
						$("#update_teamCode_exists_error").text("Team Code Already Exists");
						$("#t_error2").text("");
						$("#update_t_error2").text("");
						validateCode++;
					}
					else{
						$("#teamCode_exists_error").text("");
						$("#update_teamCode_exists_error").text("");
						$("#t_error2").text("");
						$("#update_t_error2").text("");
					}
				};
				
				defaultSelect = function() {
					$("#mappedTeam option").each(function() {
						$(this).prop("selected", false);
					});
				};
	
				/**
				 * Functions and parameters for validations in Event Management
				 */
				
				/**
				 * This function gets all th	e information regarding mapped events in the particular draw in a js array.
				 * @since 06-Oct-2015
				 */			
				getInfo = function(){	
					$('#mappedEventId option').each(function() {
						mappedEventId.push($(this).attr('value'));
					});
					$('#mappedLeagueId option').each(function() {
						mappedLeagueId.push($(this).attr('value'));
					});
					$('#mappedVenueId option').each(function() {
						mappedVenueId.push($(this).attr('value'));
					});
					$('#mappedHomeTeamId option').each(function() {
						mappedHomeTeamId.push($(this).attr('value'));
					});
					$('#mappedHomeTeamOdds option').each(function() {
						mappedHomeTeamOdds.push($(this).attr('value'));
					});
					$('#mappedAwayTeamId option').each(function() {
						mappedAwayTeamId.push($(this).attr('value'));
					});
					$('#mappedAwayTeamOdds option').each(function() {
						mappedAwayTeamOdds.push($(this).attr('value'));
					});
					$('#mappedDrawOdds option').each(function() {
						mappedDrawOdds.push($(this).attr('value'));
					});
					$('#startTime option').each(function() {
						startTime.push($(this).attr('value'));
					});
					$('#endTime option').each(function() {
						endTime.push($(this).attr('value'));
					});
					
				};
				/**
				 * This function sets the information in "updateEventForm" upon selecting events to change there information.
				 * @since 06-Oct-2015
				 */
				
				setInfo = function(i) {
					var league = mappedLeagueId[i];
					var venue = mappedVenueId[i];
					var homeTeam = mappedHomeTeamId[i];
					var awayTeam = mappedAwayTeamId[i];
					var homeTeamOdds = mappedHomeTeamOdds[i];
					var awayTeamOdds = mappedAwayTeamOdds[i];
					var drawOdds = mappedDrawOdds[i];
					var startDate = startTime[i];
					var endDate = endTime[i];
					$("#leagueId").val(league);
					$("#venue_Name_Entered_TextBox").val(venueName[venueIds.indexOf(venue)]);
					$("#old_venue").val(venueName[venueIds.indexOf(venue)]);
					$("#homeTeamId").val(homeTeam);
					$("#awayTeamId").val(awayTeam);
					$("#homeTeamOdds").val(homeTeamOdds);
					$("#awayTeamOdds").val(awayTeamOdds);
					$("#drawOdds").val(drawOdds);
					$("#startDateTimePicker").val(startDate.replace(/\-/g, "/"));
					$("#endDateTimePicker").val(endDate.replace(/\-/g, "/"));
					oldLeague =$("#leagueId").val();
					oldHomeTeam = $("#homeTeamId").val();
					oldHomeTeamOdd = $("#homeTeamOdds").val();
					oldAwayTeam = $("#awayTeamId").val();
					oldAwayTeamOdd = $("#awayTeamOdds").val();
					oldStartTime = new Date($("#startDateTimePicker").val());
					oldEndTime = new Date($("#endDateTimePicker").val());
					oldVenue = $("#old_venue").val();
					oldDrawOdd = $("#drawOdds").val();
					if($("#homeTeamId option:selected").text() == oldVenue){
						$("#venueSameAsHomeTeamCheck").prop('checked',true);
						$("#venue_Name_Entered_TextBox").prop('disabled',true);
					}
				};
				/**
				 *  This function validates the "updateEventForm".
				 *  @since 06-Oct-2015
				 */			
				validateUpdateEvent = function(eventStartTime,eventEndTime,homeTeam,awayTeam,league,venue,homeOdd,awayOdd,drawOdd,drawId) {
					var startTime = new Date(eventStartTime);
					var endTime = new Date(eventEndTime);
					var drawTime = new Date(Math.min.apply(null,drawWithSameEventDateTime));
					var freezeTime = new Date(Math.max.apply(null,drawWithSameEventFreezeTime));
					var isValid = true;
					$("#homeTeamError").html('');
					$("#homeTeamError").css("display","none");
					$("#awayTeamError").html('');
					$("#awayTeamError").css("display","none");
					if($.trim(venue)== "")
					{
						$("#venue_error").show();
						$("#venue_error").text("Please enter/select venue name !!");
						isValid = false;
					}
					else if($.trim(venue)!= "" && $.trim(venue)!= undefined){
						if(!($.trim(venue).match(/^[^&,:@$%]+$/g))){
							$("#venue_error").show();
							$("#venue_error").text("Please enter correct venue name !!");
							isValid = false;
						}/*else if($.isNumeric($.trim(venue))){
							$("#venue_error").show();
							$("#venue_error").text("Please enter atleast one alphabet in venue name !!");
							isValid = false;
						}
						else if($.trim(venue).match(/^[^a-zA-Z]+$/)){
							$("#venue_error").show();
							$("#venue_error").text("Please enter atleast one alphabet in venue name !!");
							isValid = false;
						}*/
						else{
							$("#venue_error").hide();
							$("#venue_error").text("");
						}
					}
					if(startTime > endTime){
						$("#dateTimeError").show();
						isValid = false;
					}else{
						$("#dateTimeError").hide();
					}
					
					if(homeTeam==-1){
						 $("#homeTeamError").append("Please enter home team !!");
						 $("#homeTeamError").css("display","block");
						isValid = false;
					}else{
						$("#homeTeamError").hide();
					}
					if(awayTeam==-1){
						 $("#awayTeamError").append("Please enter away team !!");
						 $("#awayTeamError").css("display","block");
						isValid = false;
					}else{
						$("#awayTeamError").hide();
					}
					if(homeTeam!=-1 && awayTeam!=-1 && homeTeam == awayTeam){
						$("#sameTeamError").show();
						isValid = false;
					}
					else{
						$("#sameTeamError").hide();
					}
					if(((startTime <= freezeTime) || (startTime >= drawTime)) || ((endTime <= freezeTime) || (endTime >= drawTime))){
						$("#dateError").show();
						$("#dateError").text("Start time and end time must be in between draw freeze time ("+freezeTime.getFullYear()+"-"+(freezeTime.getMonth()+1)+"-"+freezeTime.getDate()+" "+freezeTime.getHours()+":"+((freezeTime.getMinutes()<10?'0':'') + freezeTime.getMinutes())+":"+((freezeTime.getSeconds()<10?'0':'') + freezeTime.getSeconds())+") and draw date time ("+drawTime.getFullYear()+"-"+(drawTime.getMonth()+1)+"-"+drawTime.getDate()+" "+drawTime.getHours()+":"+((drawTime.getMinutes()<10?'0':'') + drawTime.getMinutes())+":"+((drawTime.getSeconds()<10?'0':'') + drawTime.getSeconds())+") !!");
						isValid = false;
					}
					else{
						$("#dateError").hide();
						$("#dateError").text("");
					}
					
					var pattern = new RegExp(/^\$?\d+(\.\d{1,2})?$/);
					if(homeOdd!= "" && homeOdd!=undefined){
						if(pattern.test(homeOdd)==false){
							  $('#homeTeamOdds').parent().parent().find(".small-tag-style-for-success").css("display","none");
						      $('#homeTeamOdds').parent().parent().find(".small-tag-style-for-error").html("Please enter correct odds");
						      $('#homeTeamOdds').parent().parent().find(".small-tag-style-for-error").css("display","block");
						      $('#homeTeamOdds').addClass("box-error-style");
						      isValid=false;
						}
						else{
							$('#homeTeamOdds').parent().parent().find(".small-tag-style-for-error").html("");
						    $('#homeTeamOdds').parent().parent().find(".small-tag-style-for-error").css("display","none");
						}
					}
					
					if(awayOdd!= "" && awayOdd!=undefined){
						if(pattern.test(awayOdd)==false){
							 $('#awayTeamOdds').parent().parent().find(".small-tag-style-for-success").css("display","none");
						     $('#awayTeamOdds').parent().parent().find(".small-tag-style-for-error").html("Please enter correct odds");
						     $('#awayTeamOdds').parent().parent().find(".small-tag-style-for-error").css("display","block");
						     $('#awayTeamOdds').addClass("box-error-style");
						     isValid=false;
						}
						else{
							$('#awayTeamOdds').parent().parent().find(".small-tag-style-for-error").html("");
						    $('#awayTeamOdds').parent().parent().find(".small-tag-style-for-error").css("display","none");
						}
					}
					
					if(drawOdd!= "" && drawOdd!=undefined){
						if(pattern.test(drawOdd)==false){
							  $('#drawOdds').parent().parent().find(".small-tag-style-for-success").css("display","none");
						      $('#drawOdds').parent().parent().find(".small-tag-style-for-error").html("Please enter correct odds");
						      $('#drawOdds').parent().parent().find(".small-tag-style-for-error").css("display","block");
						      $('#drawOdds').addClass("box-error-style");
						      isValid=false;
						}
						else{
							$('#drawOdds').parent().parent().find(".small-tag-style-for-error").html("");
						    $('#drawOdds').parent().parent().find(".small-tag-style-for-error").css("display","none");
						}
					}
					
					return isValid;
				};
				/**
				 * This function validates whether the user has selected any game & game type or not.
				 * @since 06-Oct-2015
				 */			
				validateEventMgmtForm = function() {
					var error = false;
					if($("#event_gameId").val()==-1){
						$("#game_error").show();
						error = true;
					}
					if($("#gameTypeId").val()==-1){
						$("#game_type_error").show();
						error = true;
					}
					return error;
				};		
				/**
				 * This function checks whether any updates have been made in the event or not.
				 * @since 06-Oct-2015
				 */			
				check = function(updateAt,leagueName,homeTeamName,awayTeamName,venueName,startTime,endTime,newHomeOdd,newAwayodd,newDrawOdd) {
					var isChange = true;
					if((oldLeague == leagueName) && (oldHomeTeam == homeTeamName) && (oldAwayTeam == awayTeamName) && (oldVenue == venueName) && (oldStartTime-startTime == 0) && (oldEndTime-endTime == 0) && (oldHomeTeamOdd == newHomeOdd) && (oldAwayTeamOdd == newAwayodd) && (oldDrawOdd == newDrawOdd)){
						$("#atleastOneChangeError").text("Please change atleast one information !!");
						$("#atleastOneChangeError").show();
						isChange = false;
					}
					return isChange;
				};			 
				/**
				 * This function resets display of all the errors to default value.
				 * @since 06-Oct-2015
				 */			
				resetUpdateError = function() {
					$("#sameTeamError").hide();
					$("#homeTeamError").hide();
					$("#awayTeamError").hide();
					$("#dateError").hide();
					$("#dateTimeError").hide();
					$("#atleastOneChangeError").hide();
					$("#homeOdd_error").hide();
					$("#noHomeOdd_error").hide();
					$("#homeOddPattern_error").hide();
					$("#awayOdd_error").hide();
					$("#noAwayOdd_error").hide();
					$("#awayOddPattern_error").hide();
					$("#drawOdd_error").hide();
					$("#noDrawOdd_error").hide();
					$("#drawOddPattern_error").hide();
				};
				
				/**
				 * Result Submission for Ghana
				 * @since 04-Nov-2015
				 */		
				
				cancelEventResultSubmit = function(id) {
					$("#"+id).parent().find('.selectArea').find('span').removeClass("textWrap_click").addClass("textWrap");
					if($("#"+id+"_C").css('display') == 'block')
					{
						$("#"+id+"_C").toggle(1 % 2 == 0 );
					} else{
						$("#"+id+"_C").toggle(0 % 2 == 0 );
					}
					
				};
				optSelect = function(id) {
					$("#"+id).parent().parent().find('span').removeClass("textWrap_click").addClass("textWrap");
					$("#"+id).removeClass().addClass("textWrap_click");
				};
				submitResultSubmissionForm = function() {

				    var i =0;
					var inputData=[];
					var eventDescData=[];
					var resultVal = '';
					var content  = '';
					var evtId =0;
					var optName ='';
					$(".textWrap_click").each(function(){
				    	inputData[i] = $(this).attr("name");
						i++;
					});
					$(".redBlocker").each(function(){
						if($('#'+$(this).attr("id")).is(':visible')){
				    		inputData[i] = $(this).attr("name");
							i++;
						}
					});
					var isValid = validateResultSubmitForm(inputData);
					if(isValid){
							for(var m=0; m<inputData.length; m++) {
							   resultVal += inputData[m]+',';
							}
							eventDescData = fetchFinalData();
							//console.log(eventDescData);
						    for(var i=0;i<inputData.length;i++){
							      var eventId=inputData[i].split("_")[0];
							      var evtOptName=inputData[i].split("_")[1];
						      	  evtDataMap[eventId]=evtOptName;
						    }
						    //console.log(evtDataMap);
							$('#form3_gameId').val($('#gameId').val());
						    $('#form3_gameTypeId').val($('#gameTypeId').val());
							$('#form3_drawId').val($('#drawId').val());
							$('#eventResult').val(resultVal);
							content+='<div class="container-fluid pageWrap"><center><table class="custom_table_margin_10"><tr><th class="result_th"><b>Event Name</b></th><th></th><th class="result_th">Option Selected</th></tr><tr><td colspan="2"></td></tr><br/>';
							for(var k =0; k<eventDescData.length;k++){
							    evtId = eventDescData[k].split("#")[0];
							    optName = getResultSubmitEvtDataMap(evtId);
							    content +='<tr><td style="text-align:left;padding-bottom:12px;">'+eventDescData[k].split("#")[1]+'</td><td></td><td style="text-align:center;">'+optName+'</td></tr><tr><td></td><td></td></tr>';
							}
							content+='</table></center></div>';
							$.confirm({
									animationSpeed: 800,
									animation: 'zoom',
									animationBounce: 2.0,
									theme : 'supervan',
									columnClass: 'confirm_custom_width',
									confirmButton: 'Confirm',
								    cancelButton: 'Cancel',
								    confirmButtonClass: 'btn-primary',
								    cancelButtonClass: 'btn-primary',
								    title: "Confirm your selection!",
								    content:content,
								    dialogClass: "modal-dialog modal-lg",
								    confirm:function (){
						               $('#form3').submit();
						              }
							});
					}
					else {
						 $.alert({
							    icon: 'fa fa-warning',
								animationSpeed: 800,
								animation: 'zoom',
								animationBounce: 2.0,
							    title: 'Alert!',
							    content: 'Result must be selected for each event!',
							    theme : 'supervan',
							    confirm: function(){
							        
							    }
							});
					}
				};
				fetchFinalData = function() {
					var i =0;
					var finalData=[];
					$(".selectArea").each(function(){
					finalData[i] = $(this).attr("name");
					i++;
					});
					return finalData;
				};
				getResultSubmitEvtDataMap = function(k) {
					return evtDataMap[k];
				};
			
		}
	            		  
	}
};

commonApp.init = function($) {
	$.each(commonApp.ui, function(n, v) {
		if ($(v.element).length > 0 && typeof v.init === "function") {
			commonApp.ui[n].element = $(v.element);
			v.init();
		}
	});
};
