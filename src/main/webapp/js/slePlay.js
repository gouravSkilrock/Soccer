var drawData = "";
var eventArr=[];
var currentBetMul = 1;
var data = '';
var result = '';
var slePlay = {};
var gameIndex = 0;
var unitprice = 0;
var winRespData = '';
var cancelRespData ='';
var payPwtData ="";
var drawIndex = 0;
var reprintCountChild = 0;
var curTrx = "";
var isPayAction = true;
var allowBuy = true;
var interval;
var cDate;
var verifyCode = null;
$(document).ready(function(){
	updateMidPanel();
});
function getMidPanelHtml(gameIndex){

	if(result.sleData.gameData[0].gameTypeData[gameIndex].drawData.length > 0){
		var targetDate=result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].ftg;
		var sptdate = String(targetDate).split(" ");
		var date=String(sptdate[0]).split("-");
		 day = date[0];
		 month = date[1];
		 year = date[2];
		 var combineDatestr = month + "/" + day + "/" + year;
		    targetDate=combineDatestr+" "+sptdate[1];
		   
		    clearInterval(interval);
			$('#example').countdown({
				date: targetDate,
				currentDate:cDate,
				offset: -8,
				day: 'Day',
				days: 'Days'
			});	
		 }
	
	
	var midPannelHtml = '';
	currentBetMul = 1;
	unitprice = result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeUnitPrice;
	$('#noOfLines').html("0");
	$("#tktPrice").html("0");
	$(".myBtn").removeClass('selected');
	$(".myBtn-new").removeClass('selected');
	$("#gameid").val(result.sleData.gameData[0].gameId);
	$("#tktMaxAmt").val(result.sleData.gameData[0].tktMaxAmt);
	$("#gameTypeId").val(result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeId);
	$("#eventSelectionType").val(result.sleData.gameData[0].gameTypeData[gameIndex].eventSelectionType);
	$("#gameTypeMaxBetAmtMultiple").val(result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeMaxBetAmtMultiple);
	$("#gameTypeUnitPrice").val(result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeUnitPrice);
	if(result.sleData.gameData[0].gameTypeData[gameIndex].drawData.length > 0) {
		$("#drawId").val(result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].drawId);
		/* Preparing Middle Panel for requested GameIndex */				
		midPannelHtml += '<div class="panel4 panel-default" id="'+result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeDevName+'_midpanel">';
		
			/* Preparing Panel Header */
			midPannelHtml += '<div class="panel-heading">';
			midPannelHtml += '<h4><i class="fa fa-futbol-o fa-fw"></i> '+result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeDisplayName+'</h4>';
			midPannelHtml += '<ul id="example" class="timer_div"><li><span class="days" id="days">00</span><p class="days_text">Days</p></li><li class="seperator">:</li><li><span class="hours" id="hours">00</span><p class="hours_text">Hours</p></li><li class="seperator">:</li><li><span class="minutes" id="minutes">00</span><p class="minutes_text">Minutes</p></li><li class="seperator">:</li><li><span class="seconds" id="seconds">00</span><p class="seconds_text">Seconds</p></li></ul>';
			midPannelHtml += '<div class="select_dr"><strong class="draw-fnn"> Select Draw:</strong><select id="draw_select" onchange="getAdvanceDrawData(this)" class="btn dropdown-toggle selectpicker btn-default option" style="float: left;width: 290px;"></div>';
			midPannelHtml += '<div>';
			for(var dIndex=0; dIndex<result.sleData.gameData[0].gameTypeData[gameIndex].drawData.length;dIndex++){
				if(dIndex == 0){
					midPannelHtml += '<option id="d-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[dIndex].drawId+'" value="'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[dIndex].drawId+'" selected="selected">';
				} else{
					midPannelHtml += '<option id="d-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[dIndex].drawId+'" value="'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[dIndex].drawId+'">';
				}
				
				midPannelHtml += result.sleData.gameData[0].gameTypeData[gameIndex].drawData[dIndex].drawDisplayString+' - '+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[dIndex].drawDateTime ;
				midPannelHtml += '</option>';
			}

			midPannelHtml += '</select></div></div>';
			
			/* Preparing Panel Body */
			midPannelHtml += '<div class="panel-body-new">';
			
				/* Preparing Draw Info Panel */
				midPannelHtml += '<div class="panel3 panel-default col-md-12" style="margin-top: 0px;background-color:#f5f5f5;padding-left: 0px;color: black;">';
				midPannelHtml += '<div class="panel-body">';
				midPannelHtml += '<div class="col-lg-2 col-md-2 col-sm-3"> <strong class="draw-fn">Draw Name: </strong></div>';
				midPannelHtml += '<div class="col-lg-4 col-md-4 col-sm-3"> <span class="draw-dt">'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].drawDisplayString+'</span></div>';
				midPannelHtml += '<div class="col-lg-2 col-md-2 col-sm-3"><strong class="draw-fn"> Draw Time: </strong></div>';
				midPannelHtml += '<div class="col-lg-4 col-md-4 col-sm-3"><span class="draw-dt"> '+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].drawDateTime+'</span></div>';			
				midPannelHtml += '</div>';
				midPannelHtml += '</div>';	
				
				/* Preparing Events Panel */
				midPannelHtml += '<div class="panel3 panel-default col-md-12 pdb">';
				midPannelHtml += '<div class="row">';
					var eventArrayLen = parseInt(result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData.length);
					var selectedCss = '';
					var bottomElemLeft = 0;
					var bottomElemRight = 0;
					
					if(eventArrayLen % 2 == 0){
						bottomElemRight = parseInt(eventArrayLen) - 1;
						bottomElemLeft = parseInt(eventArrayLen) - 2;
					}else{
						bottomElemLeft = parseInt(eventArrayLen) - 1;
					}
					eventArr=[];
					for(var m=0; m<eventArrayLen; m++){
					
						eventArr[m]={
								"eventId" : result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventId,
								"eventSelected" : ""
						};
						if(m==0){
							selectedCss = "box-top-left";
						}else if(m==1){
							selectedCss = "box-top-right";
						}else if(m == parseInt(bottomElemLeft)){
							selectedCss = "box-bottom-left";
						}else if(m == parseInt(bottomElemRight)){
							selectedCss = "box-bottom-right";
						}else{
							if(m%2 == 0){
								selectedCss = "box-mid-left";
							}else{
								selectedCss = "box-mid-right";
							}
						}							
						//console.log(result.sleData.gameData[0].gameTypeData[gameIndex]);
						midPannelHtml += '<div  class="span6">';
						midPannelHtml += '<div class="name_hd"><div class="data_time_dv"><span class="pull-left league-name">'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventDate+'</span><span class="pull-right league-name">'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventLeague+','+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventVenue+'</span></div></div>';
						midPannelHtml += '<div class="new_div">';
						midPannelHtml += '<div class="scc_tm">';
						midPannelHtml += '<div class="col-lg-5 col-md-4 col-sm-4"><h6 class="hm_nm">'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventCodeHome+'</h6><span class="name_of_team">'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventDisplayHome+'</span></div>';
						midPannelHtml += '<div class="col-lg-2 col-md-4 col-sm-4 text-center-new"><span class="vs-d">vs</span></div>';
						midPannelHtml += '<div class="col-lg-5 col-md-4 col-sm-4"><h6 class="hm_nm">'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventCodeAway+'</h6><span class="name_of_team">'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventDisplayAway+'</span></div>';
						midPannelHtml += '</div>';
					/*	midPannelHtml += '<div class="row new_dv">';
						midPannelHtml += '<div class="col-lg-6 col-md-6 col-sm-6 team-name"><h6>'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventCodeHome+'</h6></div>';
						midPannelHtml += '<div class="col-lg-6 col-md-6 col-sm-6 team-name text-center-new"><h6>'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventCodeAway+'</h6></div>';
						midPannelHtml += '</div>';
						midPannelHtml += '<div class="row new_dv">';
						midPannelHtml += '<div class="col-lg-5 col-md-4 col-sm-4 team-name"><h6>'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventDisplayHome+'</h6></div>';
						midPannelHtml += '<div class="col-lg-2 col-md-4 col-sm-4 text-center-new"><span class="vs">vs</span></div>';
						midPannelHtml += '<div class="col-lg-5 col-md-4 col-sm-4 team-name text-center-new"><h6>'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventDisplayAway+'</h6></div>';
						midPannelHtml += '</div>';*/
							
							
							
						var eventTypeLen = parseInt(result.sleData.gameData[0].gameTypeData[gameIndex].eventType.split(',').length);
						if(eventTypeLen == 3){
							midPannelHtml += '<div class="row">';
							midPannelHtml += '<div class="col-lg-4 col-md-4 col-sm-4 text-align-right"><input type="button" id='+result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].drawId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventId+'-H value="HOME" class="myBtn"></div>';
							midPannelHtml += '<div class="col-lg-4 col-md-4 col-sm-4 text-align-center"><input type="button" id='+result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].drawId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventId+'-D value="Draw" class="myBtn"></div>';
							midPannelHtml += '<div class="col-lg-4 col-md-4 col-sm-4 text-align-left"><input type="button" id='+result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].drawId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventId+'-A value="Away" class="myBtn"></div>';
						} else if(eventTypeLen == 5){
							midPannelHtml += '<div class="row" style="margin-left:10px;">';
							midPannelHtml += '<div class="span-1 text-align-right"><input type="button" id='+result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].drawId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventId+'-H2 value="H+" class="myBtn-new"></div>';
							midPannelHtml += '<div class="span-1 text-align-center"><input type="button" id='+result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].drawId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventId+'-H value="H" class="myBtn-new"></div>';
							midPannelHtml += '<div class="span-2 text-align-left"><input type="button" id='+result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].drawId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventId+'-D value="D" class="myBtn-new"></div>';
							midPannelHtml += '<div class="span-1 text-align-left"><input type="button" id='+result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].drawId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventId+'-A value="A" class="myBtn-new"></div>';
							midPannelHtml += '<div class="span-1 text-align-left"><input type="button" id='+result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].drawId+'-'+result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventId+'-A2 value="A+" class="myBtn-new"></div>';
						}
						midPannelHtml += '</div>';							
						midPannelHtml += '</div>';
						midPannelHtml += '</div>';
					}
				midPannelHtml += '</div>';
				midPannelHtml += '</div>';
			
				/* Preparing Ticket Info Panel */
				midPannelHtml += '<div class="panel3 panel-default col-md-12">';
				midPannelHtml += '<div class="panel-body">';
					midPannelHtml += '<table class="table table-bordered table-striped" style="text-align:center">';
					midPannelHtml += '<tr>';
						midPannelHtml += '<th style="text-align:center">Ticket Cost</th>';
						midPannelHtml += '<th style="text-align:center">Bet Amount Multiple</th>';
						midPannelHtml += '<th style="text-align:center">Number Of Lines</th>';
					midPannelHtml += '</tr>';
					midPannelHtml += '<tr>';
						midPannelHtml += '<td><div class="valNums"><span class="currencySymbol">$</span><span id="tktPrice">0</span></div></td>';
						midPannelHtml += '<td><div class="numberActionBox"><input type="button" value="-" class="minus plusminus"><span class="displayTextBox" id="unitPrice">'+unitprice+'</span><input type="button" value="+" class="plus plusminus"></div></td>';
						midPannelHtml += '<td> <span class="valNums" id="noOfLines">0</span> </td>';
					midPannelHtml += '</tr>';	
					midPannelHtml += '</table>';					
	            midPannelHtml += '</div>';
				midPannelHtml += '</div>';
				
			midPannelHtml += '</div>';
		
			/* Preparing Panel Footer */
			midPannelHtml += '<div class="panel-footer">';
			midPannelHtml += '<div class="row">';
			midPannelHtml += '<div class="col-lg-6 col-md-6 col-sm-6" style="text-align:right"><input type="button" class="btn btn-primary" disabled="true" id="buy" value="Purchase Ticket" onclick="doBuyConfirm();"></div>';
			midPannelHtml += '<div class="col-lg-6 col-md-6 col-sm-6" style="text-align:left"><input type="button" class="btn btn-primary" onclick="resetGame('+gameIndex+');" value="Reset"></div>';
	        midPannelHtml += '</div>';
	        midPannelHtml += '</div>';
		
		midPannelHtml += '</div>';
	}else{
		midPannelHtml += '<div class="col-md-12 alert alert-danger" style="text-align:center">No Draw Available !! Next Draw will be available on '+result.sleData.gameData[0].gameTypeData[gameIndex].upcomingDrawStartTime+'</div>';
		}
	 	
	 	return midPannelHtml;
}
	
function updateMidPanel(){
	var requestParam=path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/fetchSLEDrawData.action';
	drawData = _ajaxCallJson(requestParam,'','');
	data = JSON.stringify(drawData);
	result = $.parseJSON(data);		
	var curDate = $('#serverDate').val().split(" ");
	var date=String(curDate[0]).split("-");
	var year = date[0];
	var month = date[1];
	var day = date[2];
	var combineDatestr = month + "/" + day + "/" + year;
	cDate=combineDatestr+" "+curDate[1];
	//$("#side-menu").append('<li class="my-m"><span>Game List</span></li>');
	/* Preparing Sidebar */
	for(var k=0; k<result.sleData.gameData[0].gameTypeData.length; k++){
		//if(result.sleData.gameData[0].gameTypeData[k].drawData.length > 0) {
			$("#side-menu").append('<li><a href="#" class="myHrefClass" id="'+result.sleData.gameData[0].gameTypeData[k].gameTypeDevName+'#'+k+'"><i class="fa fa-gear fa-fw"></i> '+result.sleData.gameData[0].gameTypeData[k].gameTypeDisplayName+'</a></li>');
			$("#side-menu-match-list").append('<li><a href="#" class="sleMatchList" id="'+result.sleData.gameData[0].gameTypeData[k].gameTypeDevName+'#'+k+'"><i class="fa fa-gear fa-fw"></i> '+result.sleData.gameData[0].gameTypeData[k].gameTypeDisplayName+'</a></li>');
			$("#side-menu-result-report").append('<li><a href="#" class="sleDrawResult" id="'+result.sleData.gameData[0].gameTypeData[k].gameTypeDevName+'#'+k+'"><i class="fa fa-gear fa-fw"></i> '+result.sleData.gameData[0].gameTypeData[k].gameTypeDisplayName+'</a></li>');
			
//		}
	}						
	//to check for which game draw data available on midPanel Load	
	for(gameIndex;gameIndex<result.sleData.gameData[0].gameTypeData.length;gameIndex++){
		if(result.sleData.gameData[0].gameTypeData[gameIndex].drawData.length > 0){
            break;
		}
	}
	if(gameIndex>=result.sleData.gameData[0].gameTypeData.length){
		gameIndex=0;
	}
	$('#midPannel').append(getMidPanelHtml(gameIndex));
	$(".myHrefClass").on("click", function(e){
		e.preventDefault();
		var elemId = $(this).attr('id');
		drawIndex=0;
		gameIndex = elemId.split('#')[1];
		$('#midPannel').empty();
		$('#midPannel').append(getMidPanelHtml(gameIndex));
		slePlay.init($);
	});
	
	$(".sleMatchList").on("click", function(e){
		e.preventDefault();
		$("#sleParentApplet").css("visibility", "hidden");
		$("#regDiv").innerHTML = "";
		$("#regButton").innerHTML = "";
		var elemId = $(this).attr('id');
		gameIndex = elemId.split('#')[1];
		$('#midPannel').empty();
		 var matchreqData = JSON.stringify({
			  "merchantCode": $('#merCode').val(),
			  "userName"    : $('#userName').val(),
	     	  "sessionId"   : $('#sessionId').val(),
	     	  "gameId"		: result.sleData.gameData[0].gameId,
	     	  "gameTypeId"  : result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeId

		});
		var requestParam = path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/fetchMatchlistData.action';
		_ajaxCallDiv(requestParam,"requestData="+matchreqData,'midPannel');
		updateBaseMatchList();
		slePlay.init($);
	});
	
	$(".sleDrawResult").on("click", function(e){
		e.preventDefault();
		$("#sleParentApplet").css("visibility", "hidden");
		$("#regDiv").innerHTML = "";
		$("#regButton").innerHTML = "";
		var elemId = $(this).attr('id');
		gameIndex = elemId.split('#')[1];
		$('#midPannel').empty();
		 var matchreqData = JSON.stringify({
			  "merchantCode": $('#merCode').val(),
			  "userName"    : $('#userName').val(),
	     	  "sessionId"   : $('#sessionId').val(),
	     	  "gameId"		: result.sleData.gameData[0].gameId,
	     	  "gameTypeId"  : result.sleData.gameData[0].gameTypeData[gameIndex].gameTypeId

		});
		var requestParam = path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/fetchSLEDrawResultData.action';
		_ajaxCallDiv(requestParam,"requestData="+matchreqData,'midPannel');
		updateBaseSleDrawResult();
		slePlay.init($);
	});
	
	$("#cancel").click(function(){
		$('#verify-d').hide();
		$('#winMsg').empty();
		var chkCancel = confirm('Cancel Last Ticket?');
		if (!chkCancel) {
			return;
		}
		var requestParam = path+'/com/skilrock/sle/web/merchantUser/playMgmt/action/cancelTicket.action';
		var cancelTicketData =_ajaxCallJson(requestParam,"autoCancel=false&cancelType=CANCEL_MANUAL",'');
		//parent.frames[0].updateBalance(projectName+"/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
		updateBalance();
		if(cancelTicketData.isSuccess){
			curTrx = "CANCEL";
	    	$('#winMsg').html("");
	    	setAppData(JSON.stringify(cancelTicketData));	
	    }else{
	    	$("#sleParentApplet").css("visibility", "hidden");
	    	$('#winMsg').html("<center><b>"+cancelTicketData.errorMsg+"</b></center>");
	    }
		
	});
	
	$("#pwt").click(function(){
		$('#verify-d').show();
		$('#ticketNumber').val('');
		$('#winMsg').empty();
		$('#winMsg').html("");
	});
	$("#reprint").click(function(){
		$('#verify-d').hide();
		$('#winMsg').empty();
		$('#winMsg').html("");
		var chkReprint = confirm('Reprint Last Ticket?');
		if (!chkReprint) {
			return;
		}
		curTrx = "RPT";
		var requestParam = path+'/com/skilrock/sle/web/merchantUser/playMgmt/action/reprintTicket.action';
		var reprintTicketData =_ajaxCallJson(requestParam,'','');
		if(reprintTicketData.isSuccess){
	    	$('#winMsg').html("");
	    	setAppData(JSON.stringify(reprintTicketData));	
	    }else{
	    	$("#sleParentApplet").css("visibility", "hidden");
	    	$('#winMsg').html("<center><b>"+reprintTicketData.errorMsg+"</b></center>");
	    }
	});
	slePlay.init($);
	
	$("#sleParentApplet").css("visibility", "hidden");
}

function getAdvanceDrawData(drawId){
	drawId = drawId.value;
	var i=0;
	for(i;i<result.sleData.gameData[0].gameTypeData[gameIndex].drawData.length;i++)
	{
		if(result.sleData.gameData[0].gameTypeData[gameIndex].drawData[i].drawId == drawId ){
		      break;
		}
			
	}
	drawIndex = i;
	$('#midPannel').empty();
	$('#midPannel').append(getMidPanelHtml(gameIndex));
	slePlay.init($);
	$('#d-'+drawId).attr("selected", "selected");
}
   

function resetGame(gameIndex)
{   
	$('#winMsg').empty();
	$("#buy").attr("disabled",true);
	eventArr=[];
	currentBetMul=1;
	$('#noOfLines').html("0");
	$("#tktPrice").html("0");
	$(".myBtn").removeClass('selected');
	$(".myBtn-new").removeClass('selected');
	$('#unitPrice').html(unitprice);
	
	var eventArrayLen = parseInt(result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData.length);
	for(var m=0; m<eventArrayLen; m++){
		eventArr[m]={
				"eventId" : result.sleData.gameData[0].gameTypeData[gameIndex].drawData[drawIndex].eventData[m].eventId,
				"eventSelected" : ""
		};
	}
	
//	$("#sleParentApplet").css("visibility", "hidden");
}

function pmsRoundOff(val,place){
	return getFormattedAmount(val);
	if (arguments.length == 1)
	    return Math.round(val);

	var multiplier = Math.pow(10, place);
	return (Math.round(val * multiplier) / multiplier).toFixed(2);
}

function getFormattedAmount(amount) {
	amount = amount+"";
	var tmp = amount.split(".");
	if(tmp.length==2)
	{
		if(tmp[1].length>2)
		{
			if(tmp[1].substr(0,2)!="00")
				return parseFloat(amount).toFixed(2);
		}
		else if(tmp[1].length==2)
		{
			if(tmp[1]!="00")
				return parseFloat(amount).toFixed(2);	
		}
		else if(tmp[1].length==1)
		{
			if(tmp[1]!="0")
				return parseFloat(amount).toFixed(2);	
		}
		else
		{
			return tmp[0];
		}
	}
	return tmp[0];
}

function pmsParseInt(number){
	return parseInt(number, 10);
}

function doBuyConfirm(){
	$("#buy").attr("disabled",true);
	getBalance();
}

function updateBalance(){
	parent.frames[0].postMessage('updateBalance', document.referrer);
}
function getBalance(){
	parent.frames[0].postMessage('getBalance', document.referrer);
}
if (window.addEventListener) {
    window.addEventListener ("message", buyConfirm, false);        
}
else {
    if (window.attachEvent) {
        window.attachEvent("onmessage",buyConfirm, false);
    }
}

function buyConfirm(event){
	var balLimitCredit = event.data;
	
    $('#winMsg').empty();
	var unitPrice = parseFloat($('#gameTypeUnitPrice').val()); 
	var tktPrice=parseFloat($("#tktPrice").html()).toFixed(2);
	var noOfLines=parseFloat($("#noOfLines").html());
	var betAmount=parseFloat($("#unitPrice").html());

	if(noOfLines<=0){
		alert("System Error! Inconvenience Regreted");
		return false;
	}
	if(betAmount<=0){
		alert("Bet Amount Can Not Be Zero.");
		return false;
	}
	if(tktPrice<=0){
		alert("System Error! Inconvenience Regreted");
		return false;
	}
	
	if(tktPrice>parseFloat(balLimitCredit)){
		alert("Insufficient Balance! Inconvenience Regreted");
		return false;
	}
	var drawData=[];
	drawData.push({
		"drawId": $('#drawId').val(),
		"betAmtMul": currentBetMul,
		"eventData": eventArr
	});
	var temp = JSON.stringify({
		  "merchantCode": $('#merCode').val(),
		  "userName": $('#userName').val(),
     	  "sessionId": $('#sessionId').val(),
		  "noOfBoard": 1,
		  "gameId": $('#gameid').val(),
		  "drawInfo": drawData,
		  "gameTypeId":$('#gameTypeId').val(),
		  "totalPurchaseAmt" : tktPrice.toString()
		   
	});

	var requestParam=path+'/com/skilrock/sle/web/merchantUser/playMgmt/action/sportsLotteryPurchaseTicket.action';
	drawData = _ajaxCallJson(requestParam,"requestData="+temp,'');
	data = JSON.stringify(drawData);
	var purchaseResult = $.parseJSON(data);	
	var responseCode = purchaseResult.responseCode;
	var responseMessage = purchaseResult.responseMsg;
	if(responseCode != 0){
		if(responseCode== 10003){
			alert("Draw Freezed or Not Available!");
		} else if (responseCode== 118 || responseCode ==10012){
			window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
		} else {
			alert(responseMessage);
		}
		$("#side-menu").empty();
		$("#side-menu-match-list").empty();
		$("#side-menu-result-report").empty();
		
		$('#midPannel').empty();
		//parent.frames[0].updateBalance(projectName+"/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
		updateBalance();
		updateMidPanel();
	} else{
		//call Applet
		curTrx = "BUY";
		//parent.frames[0].updateBalance(projectName+"/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
		updateBalance();
		setAppData(data);
	}
	
}

function updateBaseMatchList(){
	var drawTypeData = $('#drawMatchDetailBeanList').val();
	if(drawTypeData!=undefined){
    var drawTypeList = jQuery.parseJSON(drawTypeData);

		$('#tableData').empty();
		var drawId = $('#drawMatchId').prop("selectedIndex");
		var eventList = drawTypeList[drawId].eventMasterList;
		var evtDisplayLength=eventList.length;
		if (eventList!=undefined && eventList.length>0 ) {
			$('#tableData').append('');
			
			var tdHtml = '<div class="col-lg-12">';
			$.each(eventList, function(key, value) {
				var homeTeamName = value.homeTeamName;
				
			var tdHtml = '';
			
			tdHtml += '<div class="col-lg-6 col-md-6">';
			tdHtml += '<div class="row-team">';
			tdHtml += '<div class="name_hd"><div class="data_time_dv"><span class="pull-left league-name">'+value.startTime+'</span><span class="pull-right league-name">'+value.leagueName+','+value.venueName+'</span></div></div>';
			tdHtml += '<div class="scc_tm">';
			tdHtml += '<div class="col-lg-5 col-md-4 col-sm-4"><h6 class="hm_nm">'+value.homeTeamCode+'</h6><span class="name_of_team">'+value.homeTeamName+'</span></div>';
			tdHtml += '<div class="col-lg-2 col-md-4 col-sm-4 text-center-new"><span class="vs-d">vs</span></div>';
			tdHtml += '<div class="col-lg-5 col-md-4 col-sm-4"><h6 class="hm_nm">'+value.awayTeamCode+'</h6><span class="name_of_team">'+value.awayTeamName+'</span></div>';
			tdHtml += '</div>';
			tdHtml += '<div class="team_row">';
			tdHtml += '<div class="btn_rw_div">';
			if(evtDisplayLength==4 || evtDisplayLength== 6){
				tdHtml += '<div class="myBtn-new-div" id="'+value.eventId+'-H2">H+</div>';
				tdHtml += '<div class="myBtn-new-div" id="'+value.eventId+'-H">H</div>';
				tdHtml += '<div class="myBtn-new-div" id="'+value.eventId+'-D">D</div>';
				tdHtml += '<div class="myBtn-new-div" id="'+value.eventId+'-A">A</div>';
				tdHtml += '<div class="myBtn-new-div" id="'+value.eventId+'-A2">A+</div>';							
			}else{
				tdHtml += '<div class="myBtn-new-box" id="'+value.eventId+'-H">Home</div>';
				tdHtml += '<div class="myBtn-new-box" id="'+value.eventId+'-D">Draw</div>';
				tdHtml += '<div class="myBtn-new-box" id="'+value.eventId+'-A">Away</div>';
			}
			
			tdHtml += '</div>';
			tdHtml += '</div>';
			tdHtml += '</div>';
			tdHtml += '</div>';
			
			$('#tableData').append(tdHtml);	
		/*	if(value.winninOptionCode=="C"){
				$('.myBtn-new-div').addClass('myBtn-active');
				$('.myBtn-new-box').addClass('myBtn-new-box-active');
			}else{
				var evid=value.eventId+"-"+value.winninOptionCode.replace("+","2");
				$("#"+evid).addClass('myBtn-active');
			}*/
			
			});
			$('#tableData').append('</div>');
			$('#tableDiv').css("display", "block");							
		}
}else {
			$('#tableData').empty();
			$('#tableDiv').css("display", "none");
		}
}

function updateBaseSleDrawResult(){

	var drawTypeData = $('#drawResultBeanList').val();
    var drawTypeList = jQuery.parseJSON(drawTypeData);

		$('#tableData').empty();
		var drawId = $('#drawResultId').prop("selectedIndex");
		var eventList = drawTypeList[drawId].eventMasterList;
		var evtDisplayLength=eventList.length;
		if (eventList!=undefined && eventList.length>0 ) {
			$('#tableData').append('');
			
			var tdHtml = '<div class="col-lg-12">';
			$.each(eventList, function(key, value) {
				var homeTeamName = value.homeTeamName;
				
			var tdHtml = '';
			
			tdHtml += '<div class="col-lg-6 col-md-6 outer_div">';
			tdHtml += '<div class="row-team">';
			tdHtml += '<div class="name_hd"><div class="data_time_dv"><span class="pull-left league-name">'+value.startTime+'</span><span class="pull-right league-name">'+value.leagueName+','+value.venueName+'</span></div></div>';
			tdHtml += '<div class="scc_tm">';
			tdHtml += '<div class="col-lg-5 col-md-4 col-sm-4"><h6 class="hm_nm">'+value.homeTeamCode+'</h6><span class="name_of_team">'+value.homeTeamName+'</span></div>';
			tdHtml += '<div class="col-lg-2 col-md-4 col-sm-4 text-center-new"><span class="vs-d">vs</span></div>';
			tdHtml += '<div class="col-lg-5 col-md-4 col-sm-4"><h6 class="hm_nm">'+value.awayTeamCode+'</h6><span class="name_of_team">'+value.awayTeamName+'</span></div>';
			tdHtml += '</div>';
			tdHtml += '<div class="team_row">';
			tdHtml += '<div class="btn_rw_div">';
			if(evtDisplayLength==4 || evtDisplayLength== 6){
				tdHtml += '<div class="myBtn-new-div" id="'+value.eventId+'-H2">H+</div>';
				tdHtml += '<div class="myBtn-new-div" id="'+value.eventId+'-H">H</div>';
				tdHtml += '<div class="myBtn-new-div" id="'+value.eventId+'-D">D</div>';
				tdHtml += '<div class="myBtn-new-div" id="'+value.eventId+'-A">A</div>';
				tdHtml += '<div class="myBtn-new-div" id="'+value.eventId+'-A2">A+</div>';							
			}else{
				tdHtml += '<div class="myBtn-new-box" id="'+value.eventId+'-H">Home</div>';
				tdHtml += '<div class="myBtn-new-box" id="'+value.eventId+'-D">Draw</div>';
				tdHtml += '<div class="myBtn-new-box" id="'+value.eventId+'-A">Away</div>';
			}
			
			tdHtml += '</div>';
			tdHtml += '</div>';
			tdHtml += '</div>';
			tdHtml += '</div>';
			
			$('#tableData').append(tdHtml);	
			if(value.winninOptionCode=="C"){
				var evid=value.eventId+"-H2";
				var evId=value.eventId+"-H";
				$("#"+evid).parent().children().addClass('myBtn-active');
				$("#"+evId).parent().children().addClass('myBtn-new-box-active');
			}else{
				var evid=value.eventId+"-"+value.winninOptionCode.replace("+","2");
				$("#"+evid).addClass('myBtn-active');
			}
			
			});
			$('#tableData').append('</div>');
			$('#tableDiv').css("display", "block");							
		} else {
			$('#tableData').empty();
			$('#tableDiv').css("display", "none");
		}

}

function doVerifyTicket(){
    $('#winMsg').empty();
    if($('#ticketNumber').val().length ==0){
       $('#winMsg').append("<html><center><b>"+ "Please Enter Ticket Number" +"</b></center></html>" );
       return;
    }
    if($('#ticketNumber').val().length !=18 && $('#ticketNumber').val().length !=16){
	   $('#winMsg').append("<html><center><b>"+ "Invalid Ticket Number" +"</b></center></html>" );
	   return;
     }
  
    var verifyPwtData="";
    var winData = JSON.stringify({
		  "merchantCode": $('#merCode').val(),
		  "userName"    : $('#userName').val(),
     	  "sessionId"   : $('#sessionId').val(),
     	  "ticketNumber": $('#ticketNumber').val()
	});

    var requestParam=path+'/com/skilrock/sle/web/merchantUser/pwtMgmt/Action/verifyWebTicket.action';
    verifyPwtData = _ajaxCallJson(requestParam,"requestData="+winData,'');
    winRespData = JSON.stringify(verifyPwtData);
    var verifyResult = $.parseJSON(winRespData);
    
    //alert(verifyResult.ticketNo +" "+ verifyResult.message +" "+verifyResult.prizeAmt); 
	if(verifyResult.responseCode==0){
		if(verifyResult.claimStatus){
			var merCode = $("#merCode").val();
			$("#saleMerCode").val(verifyResult.saleMerCode);
			if("Asoft" == verifyResult.saleMerCode){
				verifyCode = prompt("Ticket Number :" +verifyResult.ticketNo +" has winning "+verifyResult.totalPay+"\n"+"Please enter verification code to claim!");
				if(verifyCode.length == 7 && !isNaN(verifyCode)){
					doPayPwt();
				} else {
					alert("Invalid verification code!");
					 $('#ticketNumber').val('');
				}
			}
			else{
				var flag = confirm("Ticket Number :" +verifyResult.ticketNo +" has winning "+verifyResult.totalPay+"\n"+"Do you want to claim ?");
		         if(flag){
					doPayPwt();
					}
		         else{
		        	 $('#ticketNumber').val('');
		         }
			}
			
		}else{
			$('#winMsg').append("<html><center><b>" +verifyResult.message+ "</b></center></html>" );
		}
	}else{
		$('#winMsg').append("<html><center><b>" +verifyResult.responseMsg+ "</b></center></html>" );
	}
}
function doPayPwt(){
 	
 var pwtReqData = JSON.stringify({
	  "merchantCode": $('#merCode').val(),
	  "userName"    : $('#userName').val(),
	  "sessionId"   : $('#sessionId').val(),
	  "ticketNumber": $('#ticketNumber').val(),
	  "saleMerCode" : $('#saleMerCode').val(),
	  "verificationCode" : ($('#saleMerCode').val() == 'Asoft'? verifyCode:"")
	  });

     var requestParam = path+'/com/skilrock/sle/web/merchantUser/pwtMgmt/Action/payPwtWebTicket.action';
     verifyCode='';
     payPwtData = _ajaxCallJson(requestParam,"requestData="+pwtReqData,'');
     winRespdata = JSON.stringify(payPwtData);
     var payPwtRespData = $.parseJSON(winRespdata);
     var responseCode = payPwtRespData.responseCode;
    // parent.frames[0].updateBalance(projectName+"/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
     updateBalance();
     if(responseCode != 0){
    	 $('#winMsg').append("<html><center><b>" +payPwtRespData.responseMsg+ "</b></center></html>" );
	 } else {
		 curTrx = "PWT";
		 setAppData(winRespdata);
	}
}

function setAppData(buyData) {
	$("#sleParentApplet").css("visibility", "hidden");
	$("#regDiv").innerHTML = "";
	$("#regButton").innerHTML = "";
	isPrintFail = false;
	document.applets[0].showSLEStatus(buyData);
	$("#sleParentApplet").css("visibility", "visible");
	document.applets[0].repaint();
}


function setMatchListAppData(buyData) {
	$("#sleParentApplet").css("visibility", "hidden");
	$("#regDiv").innerHTML = "";
	$("#regButton").innerHTML = "";
	isPrintFail = false;
	document.applets[0].showSLEStatus(buyData);
	//$("#sleParentApplet").css("visibility", "visible");
	//document.applets[0].repaint();
}
function matchListPrintResponse(printStatus) {
	if (printStatus == "SUCCESS") {
		//$("#sleParentApplet").css("visibility", "visible");
		return true;
	} else {
		$("#sleParentApplet").css("visibility", "hidden");
		var curTrxLoc = curTrx;
		if (curTrxLoc == "MATCH_LIST") {
			alert('Please check your printer');
			}
		}
	}



var isPrintFail = false;
function cancelTktAutoSLE(tktNum, totAmt, printStatus) {
	if (printStatus == "SUCCESS") {
//		$("#buy").attr("disabled",false);
		allowBuy = true;
		$("#sleParentApplet").css("visibility", "visible");
		resetGame(gameIndex);
		return true;
	} else {
		$("#sleParentApplet").css("visibility", "hidden");
		var repCount = reprintCountChild;
		var curTrxLoc = curTrx;
		var status = "";
		if (curTrxLoc == "BUY") {
			if (repCount < 2) {
				status = confirm("\t PRINTER NOT CONNECTED \n Check Your Printer and then Press OK to Reprint or Cancel to Cancel Ticket");
				if (!status) {
					var requestParam = path+'/com/skilrock/sle/web/merchantUser/playMgmt/action/cancelTicket.action';
					var cancelTicketData =_ajaxCallJson(requestParam,"autoCancel=true&cancelType=CANCEL_PRINTER",'');
					if(cancelTicketData.isSuccess){
//						$("#buy").attr("disabled",false);
						allowBuy = true;
						alert('Last Ticket Can not be Purchased');
						//parent.frames[0].updateBalance(projectName+"/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
						updateBalance();
						return false;
					}
					isPrintFail = true;
					$("#sleParentApplet").css("visibility", "hidden");
					return true;
				} else {
					reprintCountChild++;
					//document.getElementById("parentApplet").style.visibility = 'visible';
					return true;
				}
			} else {
				var requestParam = path+'/com/skilrock/sle/web/merchantUser/playMgmt/action/cancelTicket.action';
				var cancelTicketData =_ajaxCallJson(requestParam,"autoCancel=true&cancelType=CANCEL_PRINTER",'');
				if(cancelTicketData.isSuccess){
//					_id.o("buy").disabled = false;
					allowBuy = true;
					alert('Last Ticket Can not be Purchased');
					//parent.frames[0].updateBalance(projectName+"/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
					updateBalance();
				}
				isPrintFail = true;
				
				$("#sleParentApplet").css("visibility", "hidden");
				return false;
			}
		} else {
			if (curTrxLoc == "PWT") {
				if (repCount < 2) {
					if (totAmt > 0) {
						reprintCountChild++;
						if (repCount == 0) {
							alert("\t PRINTER NOT CONNECTED. \n Check Your Printer and then Press OK to Reprint Ticket");
						} else {
							if (repCount == 1) {
								alert("\t PRINTER NOT CONNECTED \n Your Winning Amount is:" + totAmt + "\n Your Ticket No is:" + tktNum);
							}
						}
						$("#sleParentApplet").css("visibility", "visible");
						return true;
					}
				} else {
					isPrintFail = true;
					$("#sleParentApplet").css("visibility", "visible");
				}
			} else {
				if (curTrxLoc == "RPT") {
					if (repCount < 2) {
						reprintCountChild++;
						alert("\t PRINTER NOT CONNECTED \n Check Your Printer and then Press OK to Reprint Ticket");
						$("#sleParentApplet").css("visibility", "visible");
						return true;
					} else {
						isPrintFail = true;
						$("#sleParentApplet").css("visibility", "hidden");
					}
				} else {
					if (curTrxLoc == "CANCEL") {
						alert('PRINTER NOT CONNECTED \n\n Ticket Has Been Cancelled.');
					}
				}
			}
		}
	}
}

slePlay.ui = {
	/**
	 * Common Utility
	 */
	utility : {
			element : 'body',
			init : function() {
				$('.plusminus').click(function(){
					if($(this).hasClass("minus")){
						updateBetMul('-');
					} else if($(this).hasClass("plus")){
						updateBetMul('+');
					}
				});
				
				$('#drawResultId').on("change", function(e) {
					updateBaseSleDrawResult();
				});			
				$('#drawMatchId').on("change", function(e) {
					updateBaseMatchList();
				});
				$("#printMatchList").click(function(){
					var drawTypeData = $('#drawMatchDetailBeanListForApplet').val();
				    var drawTypeList = jQuery.parseJSON(drawTypeData).drawData;
				    
					var drawId = $('#drawMatchId').prop("selectedIndex");
					var drawMatchListData = drawTypeList[drawId];
					curTrx = "MATCH_LIST";
					setMatchListAppData(JSON.stringify(drawMatchListData));	
				});
				$('.myBtn, .myBtn-new').click(function(){
						var id = this.id;
						var tmpInfoArr	= id.split("-");
						var gameTypeId	= tmpInfoArr[0];
						var drawId		= tmpInfoArr[1];
						var eventId		= tmpInfoArr[2];
						var selection	= tmpInfoArr[3];
						var tmpId		= gameTypeId+"-"+drawId+"-"+eventId;
						if($('#eventSelectionType').val()!="MULTIPLE")
						{
							$("[id^="+tmpId+"]").removeClass('selected');
							if($("#"+id).hasClass("selected")){
								$("#"+id).removeClass("selected");
							}else{
								$("#"+id).addClass("selected");
							}
							$("[id^="+tmpId+"]").each(function() {
								if($(this).hasClass("selected")){
									if(selection=="H2")
										selection="H%2B";
									if(selection=="A2")
										selection="A%2B";
								}
							});
							updateEventData(eventId,selection);
						}
						else
						{
							if($("#"+id).hasClass("selected")){
								$("#"+id).removeClass("selected");
							}else{
								$("#"+id).addClass("selected");
							}
							var finalSelection="";
							$("[id^="+tmpId+"]").each(function() {
								if($(this).hasClass("selected")){
									var tmpIndInfoArr	= $(this).attr('id').split("-");
									var selectionInd	= tmpIndInfoArr[3];
									if(selectionInd=="H2")
										selectionInd="H%2B";
									if(selectionInd=="A2")
										selectionInd="A%2B";
									finalSelection=finalSelection+selectionInd+",";
								}
							});
							finalSelection=finalSelection.substr(0,(finalSelection.length-1));
							updateEventData(eventId,finalSelection);
						}
						if(!updateNoOfLine())
						{
							$("#"+id).trigger("click");
						}
						updateTicketPrice((unitPrice+"").length);
				});
				
				function updateEventData(eventId,selection)
				{
					for(var l=0;l<eventArr.length;l++){
						if(eventId==eventArr[l].eventId){
							eventArr[l]={
											"eventId" : eventId,
											"eventSelected" : selection
										};
							break;
						}
					}
				}
				
				function updateNoOfLine()
				{
					var maxNoofLine=pmsParseInt($('#tktMaxAmt').val()/$('#gameTypeUnitPrice').val());
					var noOfLines=1;
					var buyFlag=true;
					for(var l=0;l<eventArr.length;l++){
						var tmpArr=eventArr[l].eventSelected.split(",");
						noOfLines = noOfLines * tmpArr.length;
						if(tmpArr.length==0 || (tmpArr.length==1 && tmpArr[0]==""))
						{
							buyFlag=false;
						}
					}
					if(noOfLines > maxNoofLine)
					{
						alert("You Can Play For Maximum "+maxNoofLine+" Lines.");
						return false;
					}
					if(buyFlag)
						$("#buy").attr("disabled",false);
					else
						$("#buy").attr("disabled",true);
					
					$('#noOfLines').html(noOfLines);
					return true;
				}
				
				function updateTicketPrice(places){
					var noOfLines=parseFloat($("#noOfLines").html());
					var betAmount=parseFloat($("#unitPrice").html());
					var tktPrice=pmsRoundOff(noOfLines*betAmount,places);
					$("#tktPrice").html(tktPrice);
				}
				
				$(function() {
					$("#accordion").accordion({
						autoHeight: false,
						navigation: true
					});
				});
				
				function updateBetMul(sign){
						var unitPrice = parseFloat($('#gameTypeUnitPrice').val());
						var maxBetAmtMul= parseFloat($('#gameTypeMaxBetAmtMultiple').val());
						
						var counterObj	= $("#unitPrice");
						var currentBetAmt =parseFloat(counterObj.html());
						if(sign=="-"){
							if(currentBetAmt===unitPrice){
								currentBetAmt=(maxBetAmtMul);
							}else{
								if(currentBetAmt==0){
									currentBetAmt=unitPrice;
								}else{
									currentBetAmt = currentBetAmt-unitPrice;
								}
							}
						}else{
							if(currentBetAmt===(maxBetAmtMul)){
								currentBetAmt=unitPrice;
							}else{
								currentBetAmt = currentBetAmt + unitPrice;
							}
						}
						
						currentBetMul= Math.round(currentBetAmt/unitPrice);

						counterObj.html(pmsRoundOff(currentBetAmt,(unitPrice+"").length));
						updateTicketPrice((unitPrice+"").length);
				}
			}
		}

};
slePlay.init = function($) {
		$.each(slePlay.ui, function(n, v) {
			if ($(v.element).length > 0 && typeof v.init === "function") {
				slePlay.ui[n].element = $(v.element);
				v.init();
			}
		});
};
