var drawCountIndex=0;
var data='';
var gameIndex = 0;
var sideMenuHtml='';
var gameTypeIndex = 0;
var gameTypeId='';
var eventSelectionType ='';
var drawData = '';
var respData ='';
var gameIndex = 0;
var drawIndex= '';
var gameTypeIndex = 0;
var eventSelectionType ='';
var gameTypeId = 0;
var gameId = 0;
var tktThresholdAmt = 0;
var unitPrice = 0;
var gameTypeMaxBetAmtMultiple;
var maxTktAmount;
var eventArr =[];
var tktPrice = 0;
var drawDateTime='';
var betAmount=0;
var currentBetMul='';
var currentBetAmt='';
var curTrx='';
var allFreezeTimeArray = [];
var minTimeToFetchData;
var interval;
var timerArr = [];
var delayTime;
var serUpdatedCurDate='';
var freezeTimerIntervalId;
var setIntervalId1;
var setIntervalId2;
var setIntervalId3;
var setIntervalId4;
var setIntervalId5;
var setIntervalId6;
var finalBetAmount=1;
var isForTimer = false;
var onstartcheckdraw=false;
var currencySymbol='';
var priceSchemeArray={"0":{"13":"10000","12":"200","11":"20","10":"10"},"1":{"10":"1000","9":"20","8":"2"},"2":{"6":"100","5":"2","4":"1"},"3":{"4":"10","3":"1"}};
var betAmountSchemeArray={"0":{"0":"1","1":"2","2":"3","3":"4","4":"5"},"1":{"0":"1","1":"2","2":"3","3":"4","4":"5"},"2":{"0":"0.5","1":"1.0","2":"1.5","3":"2.0","4":"2.5"},"3":{"0":"0.5","1":"1.0","2":"1.5","3":"2.0","4":"2.5"}};
var cardNo='';
var keyupFiredCount=0;
var keyupFiredCount1=0;
var printUsingApplet;
var nVer = navigator.appVersion;
var nAgt = navigator.userAgent;
var browserName  = navigator.appName;
var fullVersion  = ''+parseFloat(navigator.appVersion); 
var majorVersion = parseInt(navigator.appVersion,10);
var nameOffset,verOffset,ix;

window.onload = function () {
	var maxHeight=0;
	var resultmaxHeight=0;
	$('.sportsLottWrapInner > li').each(function(){
		if($(this).height()>maxHeight)
			maxHeight=$(this).height();
	});
	$('.resultPopUp .sportsLottWrapInner > li').each(function(){
		if($(this).height()>resultmaxHeight)
			resultmaxHeight=$(this).height();
	});
	$('.sportsLottWrapInner > li').css('height',maxHeight);
	$('.resultPopUp .sportsLottWrapInner > li').css('height',resultmaxHeight);
};
/*var acc = document.getElementsByClassName("accordion");
var i;
for (i = 0; i < acc.length; i++) {
    acc[i].onclick = function(){
		for (j = 0; j < acc.length; j++) {
			acc[j].classList.remove("active");
			acc[j].nextElementSibling.classList.remove("show");
		}
		this.classList.toggle("active");
        this.nextElementSibling.classList.toggle("show");
  };
}*/


$(document).ready(function(){
	currServerTime();
	 if ((verOffset=nAgt.indexOf("MSIE"))!=-1) {
	    	printUsingApplet = "YES";
	    }else if ((verOffset=nAgt.indexOf("Chrome"))!=-1) {
	    	printUsingApplet = "NO";
	    }
	updateMidPanel(false);
	$('.beforePageLoad').delay(100).fadeOut('slow');
	$('.soccer-gm-sl').on('click', function(){
		$(this).toggleClass('select-dr');
	});
	$('.gm-innre-div').on('click', function(){
		$(this).toggleClass('select-dr');
	});
	
		$("#soccoerDiv").click(function(){
		$("#soDiv").css("display", "block");
		$("#soDivTen").css("display", "none");
		$("#soDivSix").css("display", "none");
		$("#soDivFour").css("display", "none");
		
		});
		
		$("#soccoerTen").click(function(){
		$("#soDiv").css("display", "none");
		$("#soDivTen").css("display", "block");
		$("#soDivSix").css("display", "none");
		$("#soDivFour").css("display", "none");

		});
		
		$("#soccoerSix").click(function(){
		$("#soDiv").css("display", "none");
		$("#soDivTen").css("display", "none");
		$("#soDivSix").css("display", "block");
		$("#soDivFour").css("display", "none");

		});
		
		$("#soccoerFour").click(function(){
		$("#soDiv").css("display", "none");
		$("#soDivTen").css("display", "none");
		$("#soDivSix").css("display", "none");
		$("#soDivFour").css("display", "block");

		});
		 /**
	     * Barcode scanner and MSR code starts
	     */
	    function DelayExecution(f, delay) {
	        var timer = null; 
	        return function () {
	            var context = this, args = arguments;
	            clearTimeout(timer);
	            timer = window.setTimeout(function () {
	                f.apply(context, args);
	            },
	            delay || 100);
	        };
	    }
	    
	    $.fn.ConvertToBarcodeTextbox = function () {
	         $(this).keyup(DelayExecution(function (event) {
	         if((event.keyCode==13 || $("#pwtTicket").val().length==18 || $("#pwtTicket").val().length==20) && keyupFiredCount1<=1){
	        	 if(event.keyCode!=13){
	        		 $('#pwtOk').click();
	        	 }
	        	 
	         }
	             var keycode = (event.keyCode ? event.keyCode : event.which);  
	                 keyupFiredCount1 = keyupFiredCount1 + 1;  
	         }));
	     };
	   
		
	    function DelayExecution1(f, delay) {
	         var timer = null; 
	         return function () {
	             var context = this, args = arguments;
	             clearTimeout(timer);
	             timer = window.setTimeout(function () {
	                 f.apply(context, args);
	             },
	             delay || 10);
	         };
	     }
	     $.fn.ConverToCardScanTextBox = function () {
	    	 $(this).keyup(DelayExecution1(function (event) {
	    		 if(event.keyCode!=27){
	    			    if($(this).val().length>=10 && keyupFiredCount<=1){
	   		        	 $(this).val($(this).val().replace(';','').replace('?',''));
	   		        	 $(this).attr("readonly",true);
	   		         }
	   	             var keycode = (event.keyCode ? event.keyCode : event.which);  
	   		 			if($(this).val().length<10){
	   		         		alert("Please enter value by swipe reader only!!");
	   		         		$(this).val('');
	   		         	}   
	    		 }
	         }));
	     };
	     try {
	         $("#cardNo").ConverToCardScanTextBox();
	         $("#pwtTicket").ConvertToBarcodeTextbox();

	     } catch (e) { 
	    	 
	     }
});

/**
 * 
 * mouse hover work to validate the sale to continue
 */
$(document).on("mouseover",".buy-now",function(){
	if($(".sports-no-dr").html()==null || $(".sports-no-dr").html()=="" ){
		$("#buyNowMessage").css('display','block');	
		$("#buyMessage").html("No draw availble for this game type");
		return false;
	}
	if($("#noOfLines").html()== 0){
		if(gameTypeId == 3 || gameTypeId == 4 ){
			$("#buyNowMessage").css('display','block');	
			$("#buyMessage").html("Please select atleast 1 option for all events (H+,H,D,A,A+)");
			return false;	
		}
		else{
			$("#buyNowMessage").css('display','block');	
			$("#buyMessage").html("Please select atleast 1 option all events (Home,Draw,Away)");
			return false;
		}
	}
	
});
$(".buy-now").mouseout(function(e){
	$("#buyNowMessage").css('display','none');	
	$("#buyMessage").html("");
});


function updateMidPanel(isForResetTimer){
	var resetTimer=true;
	var sideMenuHtml = '';
	var i = 0;
	var requestParam=path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/fetchSLEDrawData.action';
	drawData = _ajaxCallJson(requestParam,'','');
	data = JSON.stringify(drawData);
	respData = drawData;
	gameId = respData.sleData.gameData[0].gameId;
	tktThresholdAmt = respData.sleData.gameData[0].tktThresholdAmt;
	maxTktAmount = respData.sleData.gameData[0].tktMaxAmt;
	if(isForResetTimer){
		onstartcheckdraw = false;
	}
	//preparing side menu
	if(respData.responseCode == 0){
		$(respData.sleData.gameData[gameIndex].gameTypeData).each(function(){
			if(this.drawData != null && this.drawData != ""){
				if(!onstartcheckdraw){
					sideMenuHtml +='<button menu-id ="'+i+'~'+this.gameTypeId+'" evt-sel="'+this.eventSelectionType+'" class="select-game side-menu sideMenuList"><span class="logo-game"><img src="'+path+'/sleGamePlayAssets/sleContent/sports-img/sports_lottery'+(4-i)+'.png" width="90" alt=""></span><span class="name-game">'+this.gameTypeDisplayName+'</span><span class="next-draw-time"><span class="next-dr-name">Next Draw</span><span class="dr-time-nxt  countdate" id="countDown'+i+'" tdate ="'+this.upcomingDrawStartTime+'"></span><span class="next-dr-tm"></span></span></button>';	
					onstartcheckdraw=true;
					 i++;
					return;
 				}
			}
			sideMenuHtml +='<button menu-id ="'+i+'~'+this.gameTypeId+'" evt-sel="'+this.eventSelectionType+'" class="side-menu sideMenuList"><span class="logo-game"><img src="'+path+'/sleGamePlayAssets/sleContent/sports-img/sports_lottery'+(4-i)+'.png" width="90" alt=""></span><span class="name-game">'+this.gameTypeDisplayName+'</span><span class="next-draw-time"><span class="next-dr-name">Next Draw</span><span class="dr-time-nxt  countdate" id="countDown'+i+'" tdate ="'+this.upcomingDrawStartTime+'"></span><span class="next-dr-tm"></span></span></button>';
			       i++;
		 });
		$(".ui-left-menu").html(sideMenuHtml);
		
	}else {
		// TODO if response in not success.
	}
	$(".countdate").each(function(){
		timerArr.push(getCompataibleDate($(this).attr("tdate")));
	});
	if(resetTimer){
		 var k = 0;
		 $(respData.sleData.gameData[0].gameTypeData).each(function(k){ 
			 if(this.gameTypeId == 1)
				 setTimer1("countDown"+k,timerArr[k]);
			 else if(this.gameTypeId == 2)
				 setTimer2("countDown"+k,timerArr[k]);
			 else if(this.gameTypeId == 3)
				 setTimer3("countDown"+k,timerArr[k]);
			 else if(this.gameTypeId == 4)
				 setTimer4("countDown"+k,timerArr[k]);
			 k++;
		 });
	}
 	allFreezeTimeArray = timerArr.sort();
	minTimeToFetchData = allFreezeTimeArray[0];
	var dateArr = allFreezeTimeArray[0].split(" ")[0].split("/");
	minTimeToFetchData = dateArr[2]+"/"+dateArr[0]+"/"+dateArr[1] +" "+allFreezeTimeArray[0].split(" ")[1];
    for(gameTypeIndex; gameTypeIndex<respData.sleData.gameData[gameIndex].gameTypeData.length; gameTypeIndex++){
		if(respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData.length > 0){
			eventSelectionType = respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].eventSelectionType;
			createMidPanel(gameIndex,gameTypeIndex,resetTimer);
			return false;
            break;
		}
	}
	$(".side-menu").first().addClass("select-game");
	createMidPanel(0,0,resetTimer);
}


/**
 * Call draw date timer when draw freezes
 */
function callDrawDateTimer(gameId){
	var gIndex = 0;
	var i = 0;
	$(respData.games).each(function(){
		if(this.gameId == gameId){
			gIndex = i;
			return false;
		}
		i++;
	});
	var lastDrawFreezeTime='';
	var lastDrawDateTime='';
	var timerFreezeTime='';
	var timerDateTime='';
	
	if(respData.games[gIndex].lastDrawFreezeTime != undefined && respData.games[gIndex].lastDrawFreezeTime != ''){
		lastDrawFreezeTime=new Date(getCompataibleDate(respData.games[gIndex].lastDrawFreezeTime)).getTime();
	}
	//last Draw Date Time
	if(respData.games[gIndex].lastDrawDateTime!= undefined && respData.games[gIndex].lastDrawDateTime!=''){
		lastDrawDateTime=new Date(getCompataibleDate(respData.games[gIndex].lastDrawDateTime)).getTime();
	}

	if (lastDrawFreezeTime == '' || lastDrawDateTime == '' || serUpdatedCurDate > lastDrawDateTime) {
		//current draw freeze Time
		currDrawFreezeTime=new Date(getCompataibleDate(respData.games[gIndex].draws[0].drawFreezeTime)).getTime();
		timerFreezeTime=currDrawFreezeTime;
		
		//current draw Date Time
		currDrawDateTime=new Date(getCompataibleDate(respData.games[gIndex].draws[0].drawDateTime)).getTime();
		timerDateTime=currDrawDateTime;
	}else {
		timerFreezeTime=lastDrawFreezeTime;
		timerDateTime=lastDrawDateTime;
	}	
	checkCurrentDrawFreezeScreenActive(timerFreezeTime,timerDateTime,"timer-"+respData.games[gIndex].gameCode);
}

//if date comes in dd-MM-yyyy hh:mm:ss format
function getCompataibleDate(date){
	var arr = date.split(" ")[0].split("-");
	var finalDate = arr[1]+"/"+arr[0]+"/"+arr[2]+" "+date.split(" ")[1];
	return finalDate;
}


function setTimer1(elem_id, date) {
	setIntervalId1 = setInterval(function () {
		commonSetTimer(elem_id,date,1);
	}, 1000);
}
function setTimer2(elem_id, date) {
	setIntervalId2 = setInterval(function () {
		commonSetTimer(elem_id,date,2);
	}, 1000);
}
function setTimer3(elem_id, date) {
	setIntervalId3 = setInterval(function () {
		commonSetTimer(elem_id,date,3);
	}, 1000);
}
function setTimer4(elem_id, date) {
	setIntervalId4 = setInterval(function () {
		commonSetTimer(elem_id,date,4);
	}, 1000);
}

function commonSetTimer(elem_id, date, id){
	var target_date = new Date(date).getTime();
	var days, hours, minutes, seconds;
	var current_date = serUpdatedCurDate;
	var seconds_left = (target_date - current_date) / 1000;
	days = parseInt(seconds_left / 86400);
	seconds_left = seconds_left % 86400;
	hours = parseInt(seconds_left / 3600);
	seconds_left = seconds_left % 3600;
	minutes = parseInt(seconds_left / 60);
	seconds = parseInt(seconds_left % 60);
	if(days == 00 && hours == 00 && minutes == 00 && seconds == 00){
		$("#"+elem_id).text(seconds);
		$("#"+elem_id).siblings(".next-dr-tm").text(seconds>1?"secs":"sec");
		updateMidPanel(true);
	}else if(days == 00 && hours == 00 && minutes == 00){
		if(seconds >= 0 ){
			$("#"+elem_id).text(seconds);
			$("#"+elem_id).siblings(".next-dr-tm").text(seconds>1?"secs":"sec");
		}
	}else if (days == 00 && hours == 00) {
		if(minutes >= 0 ){
			$("#"+elem_id).text(minutes);
			$("#"+elem_id).siblings(".next-dr-tm").text(minutes>1?"mins":"min");
		}
	}else if (days == 00) {
		if(hours >= 0 ){
			$("#"+elem_id).text(hours);
			$("#"+elem_id).siblings(".next-dr-tm").text(hours>1?"hrs":"hr");
		}
	}else {
		if(days >= 0 ){
			$("#"+elem_id).text(days);
			$("#"+elem_id).siblings(".next-dr-tm").text(days>1?"days":"day");
		}
	}

}
function currServerTime(){
	var dArr =[]; 
	dArr = $("#currentServerTime").val().split(" ")[0].split("-");
	var fDate = dArr[1]+"/"+dArr[2]+"/"+dArr[0]+" "+$("#currentServerTime").val().split(" ")[1];
	serUpdatedCurDate= new Date(fDate).getTime();
	var id = setInterval(function(){
		serUpdatedCurDate = serUpdatedCurDate + 1000;
	},1000);
}

function checkCurrentDrawFreezeScreenActive(timerDrawFreezeTime,timerDrawDateTime,gameDiv) {
	$("."+gameDiv).html("");
	var gameSel = "";
	gameInFocus = gameDiv.split("-")[1];
	$(".sideMenuList").each(function(){
		if($(this).hasClass("select-game")){
			gameSel = $(this).children().eq(1).text();
		} 
	});
	freezeTimerIntervalId = setInterval(function () {
		if(gameInFocus == gameSel){
			if('Soccer 13' == gameInFocus){
				resetAllGames();
			}else if('Soccer 10' == gameInFocus){
				resetAllGames();
			}else if('Soccer 6' == gameInFocus){
				resetAllGames();
			}else if('Soccer 4' == gameInFocus){
				resetAllGames();
			}
		}
		startDrawScreen(timerDrawFreezeTime,timerDrawDateTime,gameDiv);
	},1000);
}
function startDrawScreen(timerDrawFreezeTime,timerDrawDateTime,gameDiv){
	//console.log("serUpdatedCurDate "+new Date(serUpdatedCurDate) +" timerDrawFreezeTime "+new Date(timerDrawFreezeTime)+" timerDrawDateTime "+new Date(timerDrawDateTime));
	if(serUpdatedCurDate >= timerDrawFreezeTime && serUpdatedCurDate <= timerDrawDateTime){
		var delayTime = ((timerDrawDateTime - serUpdatedCurDate)/1000) +1;
		if(delayTime > 0){
			clearInterval(freezeTimerIntervalId);
			$("."+gameDiv).circularCountDown({
	              delayToFadeIn: 500,
				  size: 182,
				  fontColor: '#fff',
				  colorCircle: 'white',
				  background: '#2ECC71',
	              reverseLoading: false,
	              duration: {
	                  seconds: parseInt(delayTime)
	              },
	              beforeStart: function() {
	                  //$('.launcher').hide();
	              },
	              end: function(countdown) {
	                  countdown.destroy();
	                  $("."+gameDiv).css("display","none");
                      clearInterval(setIntervalId1);
	      			  clearInterval(setIntervalId2);
	      			  clearInterval(setIntervalId3);
	      			  clearInterval(setIntervalId4);
	                  isSideMenuBuild = false;
	                  isForTimer = true;
	                  var resetTimer = true;
	                  var menuId = "";
	                  $(".sideMenuList").each(function(){
	                	 if($(this).hasClass("select-game")){
	                		 menuId = $(this).attr('id');
	                	 } 
	                  });
	                  var gameId = menuId.split('~')[1];
	                  updateMidPanel(gameId,true);
	                  /*createMidPanel(menuId, gameId,resetTimer);*/
	              }
	          });
		}
	}
}
/**
 * creating Mid panel of SLE game
 * @param gameIndex
 * @param gameTypeIndex
 * @param resetTimer
 */

function createMidPanel(gameIndex,gameTypeIndex,resetTimer){
	prepareMidPanelDrawData(gameIndex,gameTypeIndex,0);
} 

function prepareMidPanelDrawData(gameIndex,gameTypeIndex,drawIndex,resetTimer){
	$(".sports-no-dr").html("");
	$(".select-draw").html("");
	$(".showAdvDraw").html("");
	$("#cancelAdvDrawWindow").show();
	$("#submitAdvDrawWindow").show();
	eventArr=[];
	$("#buy-btn").attr("disabled",true);
	$("#tktPrice").html(tktPrice);
	$("#sportsLottWrapInnerSoccerOther").html('');
	$("#sportsLottWrapInnerSoccer4").html('');
	$("#sportsLottWrapOuter").html('');
	//price scheme ui design
	var scheme=priceSchemeArray[gameTypeIndex];
	var gamePrize='';
	gamePrize+='<table class="table-prize" cellpadding="0" cellspacing="0"><thead><tr><th>Match</th><th>Prize/Jackpot (USD)</th></tr></thead><tbody>';
	for(var i in scheme){
		 gamePrize+='<tr><td>'+i+'</td><td>'+scheme[i]+'</td></tr>';
	}
	gamePrize+='</tbody></table>';
	$(".tktViewName").html("Prize Scheme");
	$(".tktView").html(gamePrize);
	//Betamount designing on ui
	var betAmountDesign=betAmountSchemeArray[gameTypeIndex];
	var betAmountScheme='';
	for(var j in betAmountDesign){
		betAmountScheme+='<button class="unitPrice"><i class="fa fa-usd" aria-hidden="true"></i>'+betAmountDesign[j]+'</button>';
	}
	betAmountScheme+='<button  id="otherBtn">Other</button><input type="text" class="other-amt" style="display: none;" id="otherAmt">';
	$(".sl-select-amt").html(betAmountScheme);
	$(".unitPrice").first().addClass("amt-select");
	unitPrice =  respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].gameTypeUnitPrice;
    gameTypeMaxBetAmtMultiple = respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].gameTypeMaxBetAmtMultiple;
    gameTypeId = respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].gameTypeId;
    if(gameTypeIndex<=1){
    	$("#betAmount").val(1);	
    }else{
    	$("#betAmount").val(0.5);
    }
    
	betAmount=unitPrice;
    var k = 0;
	var evtOpts = [];
	var eventId =0;
	var AdvdrawPanel='';
	eventOpts = respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].eventType.replace("[","").replace("]","").split(",");
    var midPanelHtml = '';
    var midPanelHtmlNoDraw = '';
    if(respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData.length > 0){
		var drawId = respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData[drawIndex].drawId;
		$("#drawId").val(drawId);
		var drawCount = 0;
		//console.log(respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData[drawIndex].drawDateTime);
		for(var dIndex=0; dIndex<respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData.length;dIndex++,drawCount++){
			if(dIndex==0){
				$(".sports-no-dr").html("<span>"+respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData[dIndex].drawDateTime+"</span>");
				$(".select-draw").html(respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData[dIndex].drawDateTime);
			}if(respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData[dIndex].drawDateTime!=null){
				if(dIndex<1){
					AdvdrawPanel+='<div class="col-span-4">';
					AdvdrawPanel+='<div class="ad-draw-check-box radio-btn select-checkbox">';
					AdvdrawPanel+='<input id="radio'+(dIndex+1)+'" type="radio" name="advDraw" class="advDrawSelect" index="'+drawCount+'" checked="true">';
					AdvdrawPanel+='<label for="radio'+(dIndex+1)+'">'+respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData[dIndex].drawDisplayString+' '+respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData[dIndex].drawDateTime+'</label>';
					AdvdrawPanel+='</div></div>';
				}
				else{
					AdvdrawPanel+='<div class="col-span-4">';
					AdvdrawPanel+='<div class="ad-draw-check-box radio-btn">';
					AdvdrawPanel+='<input id="radio'+(dIndex+1)+'" type="radio" name="advDraw" class="advDrawSelect" index="'+drawCount+'">';
					AdvdrawPanel+='<label for="radio'+(dIndex+1)+'">'+respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData[dIndex].drawDisplayString+' '+respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData[dIndex].drawDateTime+'</label>';
					AdvdrawPanel+='</div></div>';
				}
				
            }
		}
		
		var isEvtArrPrepared = false;
			$(respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData[drawIndex].eventData).each(function(){
				eventId = this.eventId;
				k=0;
				midPanelHtml+='<li><div class="gameDiv sportsLottWrap"><div class="gameDivInner">';
				midPanelHtml+= '<div class="titleAndDateWrap"><div class="dateWrap">'+this.eventDate.split(" ")[0]+' '+" "+'<strong>'+this.eventDate.split(" ")[1]+'</strong></div>';
				midPanelHtml+= '<div class="titleNameWrap">'+this.eventLeague+','+this.eventVenue+'</div><div class="clearBoth"></div></div>';
				midPanelHtml+= '<div class="row teamTitle"><div class="col-xs-5 text-center"><div class="abbriName">'+this.eventDisplayHome+'</div></div>';
				midPanelHtml+= '<div class="col-xs-2 text-center"><span class="vsWrap">VS</span></div>';
				midPanelHtml+= '<div class="col-xs-5 text-center"><div class="abbriName">'+this.eventDisplayAway+'</div></div>';
				midPanelHtml+= '</div>';
				midPanelHtml+= '<div class="row selectArea">';
				if(gameTypeIndex == 2 || gameTypeIndex == 3){
					$(eventOpts).each(function(){
						midPanelHtml+= '<div class="col-xs-2"><span class="numWrap" id="'+gameTypeId+'-'+drawId+'-'+eventId+'-'+eventOpts[k].replace(/(^\s+|\s+$)/g, "").replace('+','2')+'">'+eventOpts[k]+'</span></div>';
						k++;
					});
				}else{
					$(eventOpts).each(function(){
						 if(eventOpts[k].replace(/(^\s+|\s+$)/g, "") == "H"){
					     	midPanelHtml+= '<div class="col-xs-4"><span class="numWrap" id="'+gameTypeId+'-'+drawId+'-'+eventId+'-'+eventOpts[k].replace(/(^\s+|\s+$)/g, "").replace('+','2')+'">HOME</span></div>';
					     }
						else if(eventOpts[k].replace(/(^\s+|\s+$)/g, "") == "D"){
						     	midPanelHtml+= '<div class="col-xs-4"><span class="numWrap" id="'+gameTypeId+'-'+drawId+'-'+eventId+'-'+eventOpts[k].replace(/(^\s+|\s+$)/g, "").replace('+','2')+'">DRAW</span></div>';
						   }
						else if(eventOpts[k].replace(/(^\s+|\s+$)/g, "") == "A"){
					     	midPanelHtml+= '<div class="col-xs-4"><span class="numWrap" id="'+gameTypeId+'-'+drawId+'-'+eventId+'-'+eventOpts[k].replace(/(^\s+|\s+$)/g, "").replace('+','2')+'">AWAY</span></div>';
					    }
						 k++;
					});
				}
				var tmpEventData = respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData[drawIndex].eventData;
				if(!isEvtArrPrepared){
					for(var l=0;l<tmpEventData.length;l++){
						eventArr.push({
										"eventId" : tmpEventData[l].eventId,
										"eventSelected" : ""
									});	
					}
				}
			isEvtArrPrepared = true;
			
			 });
			midPanelHtml+= '</div></div></div></li>';
			 if(gameTypeIndex == 3){
			    	$("#sportsLottWrapInnerSoccer4").append(midPanelHtml);
			    }else{
			    	$("#sportsLottWrapInnerSoccerOther").append(midPanelHtml);
			    }
    }else{
    	if(respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].areEventsMappedForUpcomingDraw){
    		midPanelHtmlNoDraw +='<div class="match-not">Match not available. Next match is on '+respData.sleData.gameData[0].gameTypeData[gameTypeIndex].upcomingDrawStartTime+'. For more details please check for <input type=button id="showMatchList" name="Match List" value="Match List" style="background-color: #FFDB4B;" /> </div></div>';
    	}
    	else{
    		midPanelHtmlNoDraw +='<div class="match-not">Match not available. Next match is on '+respData.sleData.gameData[0].gameTypeData[gameTypeIndex].upcomingDrawStartTime+'.</div></div>';
    	}
    	$("#sportsLottWrapOuter").html(midPanelHtmlNoDraw);
    }
    
	if(AdvdrawPanel=="" || AdvdrawPanel == null){
		AdvdrawPanel="No Draw Available !!!";
		$(".select-draw").html("No draw");
		$("#cancelAdvDrawWindow").hide();
		$("#submitAdvDrawWindow").hide();
	}
	$(".showAdvDraw").html(AdvdrawPanel);
}
/**
 * click on advance draw to play
 */
$(document).on('click','.advDrawSelect',function(){
	$(".ad-draw-check-box").removeClass("select-checkbox");
	drawCountIndex=$(this).attr('index');
	$(this).parent().addClass("select-checkbox");
});
//click on side menu of sports lottery
$(document).on('click','.side-menu',function(){
	$("#sleParentApplet").css("display","none");
	$(".tktView").css("display","block");
	$(".sideMenuList").each(function(){
		$(this).removeClass("select-game");
	});
	$(".unitPrice").removeClass("amt-select");
	$(".unitPrice").first().addClass("amt-select");
	$(this).addClass("select-game");
	resetBetAmountMenu();
	$("#selection").html("");
	 $("#noOfLines").html("0");
	 $("#tktPrice").html("0");
	 $('#open-list-btn').hide('show');
	 gameTypeIndex = $(this).attr('menu-id').split("~")[0];
	 gameTypeId = $(this).attr('menu-id').split("~")[1];
	 eventSelectionType = $(this).attr('evt-sel');   
	 resetTimer=false;
	 createMidPanel(gameIndex,gameTypeIndex,resetTimer);
});


//click on bettype of game type id 
$(document).on('click','.numWrap',function(){
	$('#open-list-btn').hide('show');
	var id = this.id;
	var tmpInfoArr	= id.split("-");
	var gameTypeId	= tmpInfoArr[0];
	var drawId		= tmpInfoArr[1];
	var eventId		= tmpInfoArr[2];
	var selection	= tmpInfoArr[3];
	var tmpId		= gameTypeId+"-"+drawId+"-"+eventId;
	
	if(eventSelectionType!="MULTIPLE"){
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
				var tmpIndInfoArr = $(this).attr('id').split("-");
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
		//callbackClick=true;
		$(this).trigger("click");
	}
	updateTicketPrice((unitPrice+"").length);

});

/**
 * check all betType are select or not  
 */

function checkAllBetTypeSelect(eventArr,noOfLines){
   $('#noOfLines').html("0");
   var flag=0;
   var checkEvt=eventArr;
   var checkEvtSelect='';
   for(l=0;l<checkEvt.length;l++){
	   checkEvtSelect=checkEvt[l].eventSelected;
	   if(!checkEvtSelect == ""){
		   flag=1;
	   }
	   else{
		   flag=0;
		   return false;
	   }
   }
   if(flag == 1)
   $('#noOfLines').html(noOfLines);
   currentBetAmt=$("#betAmount").val();
   currentBetMul= Math.round(currentBetAmt/unitPrice);
}

/**
 * updating betType in purchase details
 */
function updateBetType(eventArr){
	var betTypeSelection='';
	var evtSel ="";
	var evt=eventArr;
	//var evtSel = evt.eventSelected.split(",");
		for(l=0;l<evt.length;l++){
			evtSel=evt[l].eventSelected.split(",");
			for(k=0;k<evtSel.length;k++){
				if(evtSel[k] == "H%2B"){
					betTypeSelection =betTypeSelection + "H+"+",";
				}
				else if(evtSel[k] == "A%2B"){
					betTypeSelection =betTypeSelection + "A+"+",";
				}else{
					if(evtSel[k]!=""){
						betTypeSelection =betTypeSelection + evtSel[k]+"," ;	
					}
				}
				$("#selection").html(betTypeSelection);
				if($("#selection").val() == ","){
					$("#selection").html("");	
				}
			}
	
		}
				
}

/**
 * updating event data
 * @param eventId
 * @param selection
 */
function updateEventData(eventId,selection){
	for(var l=0;l<eventArr.length;l++){
		if(eventId==eventArr[l].eventId){
			eventArr[l]={
						"eventId" : eventId,
						"eventSelected" : selection
						};
			updateBetType(eventArr);
			break;
		}
	}
}
/**
 * 
 * update ticket price
 */
function updateTicketPrice(places){
	$("#tktPrice").html(0);
	var noOfLines=parseFloat($("#noOfLines").html());
	var betAmount=parseFloat($("#betAmount").val());
	if(!isNaN(noOfLines)){
		var tktPrice=pmsRoundOff(noOfLines*betAmount,places);
		$("#tktPrice").html(tktPrice);	
	}
	
}


function updateNoOfLine(){
	var maxNoofLine = pmsParseInt(maxTktAmount/unitPrice);
	var noOfLines=1;
	var buyFlag=true;
	for(var l=0;l<eventArr.length;l++){
		var tmpArr=eventArr[l].eventSelected.split(",");
		noOfLines = noOfLines * tmpArr.length;
		if(tmpArr.length==0 || (tmpArr.length==1 && tmpArr[0]=="")){
			buyFlag=false;
		}
	}
	if(noOfLines > maxNoofLine){
		$("#error-popup").show(400);
		$("#error").html("You Can't Play For Maximum "+maxNoofLine+" Lines.");
		$("#error-popup").delay(2000).hide(400);
		//alert("You Can't Play For Maximum "+maxNoofLine+" Lines.");
		return false;
	}
	if(buyFlag)
		$("#buy-btn").attr("disabled",false);
	else
		$("#buy-btn").attr("disabled",true);
	
	checkAllBetTypeSelect(eventArr,noOfLines);
	//$('#noOfLines').html(noOfLines);
	return true;
}

function pmsRoundOff(val,place){
	return getFormattedAmount(val);
	if (arguments.length == 1)
	    return Math.round(val);

	var multiplier = Math.pow(10, place);
	return (Math.round(val * multiplier) / multiplier).toFixed(2);
}

function pmsParseInt(number){
	return parseInt(number, 10);
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


$('.plusminus').click(function(){
	//alert("jhhff");
	if($(this).hasClass("minus-icon")){
		updateBetMul('-');
	} else if($(this).hasClass("plus-icon")){
		updateBetMul('+');
	}
});




/**
 * for sale
 */


$(document).on('click','#buy-btn',function(){
	$("#sleParentApplet").css("display", "none");
	$(".tktView").css("display","block");
	if(cardInfoReq=='TRUE'){
		$("#card-no").css("display","block");
		$("#cardNo").attr("readonly",false);
		$("#cardNo").val("");
		$("#cardNo").focus();
		$("#error-message1").html("");
	}else{
		doSale();
	}
});


$(document).on('click','#cardOk',function(){
	if($("#cardNo").val()==''){
		$("#error-message1").html("Please enter card no to submit card detail");
		return false;
	}
	$("#card-no").css("display","none");
	cardNo=$("#cardNo").val();
	doSale();
});


$(document).on('click','#cardClose,#cardCancel',function(){
	$("#cardNo").val("");
	$("#card-no").css("display","none");
	doSale();
});

function doSale(){
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
	var tktPrice=parseFloat($("#tktPrice").html());
	var noOfLines=parseFloat($("#noOfLines").html());
	var betAmount=parseFloat($("#betAmount").val());
	finalBetAmount=$("#betAmount").val();
	if(noOfLines<=0){
		$("#error-popup").show(400);
		$("#error").html("System Error! Inconvenience Regreted");
		$("#error-popup").delay(2000).hide(400);
		//alert("System Error! Inconvenience Regreted");
		return false;
	}
	if(betAmount<=0){
		$("#error-popup").show(400);
		$("#error").html("Bet Amount Can Not Be Zero.");
		$("#error-popup").delay(2000).hide(400);
		//alert("Bet Amount Can Not Be Zero.");
		return false;
	}
	if(tktPrice<=0){
		$("#error-popup").show(400);
		$("#error").html("System Error! Inconvenience Regreted");
		$("#error-popup").delay(2000).hide(400);
		//alert("System Error! Inconvenience Regreted");
		return false;
	}

	if(tktPrice>parseFloat(balLimitCredit)){
		alert("Insufficient Balance! Inconvenience Regreted");
		return false;
	}

	var buyConfirm= confirm("Do You want to purchase");
	if(buyConfirm){
		var drawData=[];
		drawData.push({
			"drawId": $('#drawId').val(),
			"betAmtMul": currentBetMul.toString(),
			"eventData": eventArr
		});
		var temp = JSON.stringify({
			"merchantCode": $('#merCode').val(),
			"userName": $('#userName').val(),
			"sessionId": $('#sessionId').val(),
			"noOfBoard": "1",
			"gameId":gameId.toString(),
			"drawInfo": drawData,
			"gameTypeId":gameTypeId.toString(),
			"totalPurchaseAmt" : tktPrice.toString()

		});
		//  console.log(temp);
		var requestParam=path+'/com/skilrock/sle/web/merchantUser/playMgmt/action/sportsLotteryPurchaseTicket.action';
		drawData = _ajaxCallJson(requestParam,"requestData="+temp,'');
		data = JSON.stringify(drawData);
		var purchaseResult = $.parseJSON(data);	
		var responseCode = purchaseResult.responseCode;
		var responseMessage = purchaseResult.responseMsg;
		resetAllGames();
		if(responseCode != 0){
			if(responseCode== 10003){
				$("#error-popup").show(400);
				$("#error").html("Draw Freezed or Not Available!");
				$("#error-popup").delay(2000).hide(400);
				//alert("Draw Freezed or Not Available!");
			} else if (responseCode== 118 || responseCode ==10012){
				window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
			} else {
				$("#error-popup").show(400);
				$("#error").html(responseMessage);
				$("#error-popup").delay(2000).hide(400);
				//alert(responseMessage);
			}
			//parent.frames[0].updateBalance(projectName+"/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			updateBalance();
			resetAllGames();
		} else{
			curTrx = "BUY";

			//setAppData(data);
			var finalSaleRespData = {};
			finalSaleRespData={
					"serviceName" : "SLE",
					"mode" : curTrx,
					"finalBetAmount":finalBetAmount,
					"gameDevName" : purchaseResult.tktData.gameTypeName,
					"orgName" : purchaseResult.tktData.orgName,
					"advMsg" : purchaseResult.advMessage, 
					"retailerName" : purchaseResult.tktData.retailerName,
					"brCd" :  purchaseResult.tktData.brCd, 
					"ticketNumber" :purchaseResult.tktData.ticketNo,
					"gameName" : purchaseResult.tktData.gameName,
					"purchaseDate" :dateFormat(new Date(getCompataibleDate2(purchaseResult.tktData.purchaseDate)),dateFormat.masks.sikkimDate),
					"purchaseTime" : getCompataibaleTime(purchaseResult.tktData.purchaseTime),
					"purchaseAmt" : purchaseResult.tktData.ticketAmt,
					"boardData" :  purchaseResult.tktData.boardData,
					"currencySymbol" : purchaseResult.tktData.currSymbol, 
					"eventType":purchaseResult.tktData.eventType,
					"jackpotMsg":purchaseResult.tktData.jackpotMsg,
					"cardNo":cardNo,
					"parentOrgName":$('#parentOrgName').val()
			};
			currencySymbol=purchaseResult.tktData.currSymbol;

		}
		printData = JSON.stringify(finalSaleRespData);
		//console.log(printData);
		// prepareSaleTicket(JSON.parse(printData));
			//prepareSaleTicket(JSON.parse(printData));
		$(".tktViewName").html("Ticket Preview");
			if("YES" == printUsingApplet){
				$(".tktView").css("display","none");
				setAppletData(printData);
			}else{
				setAppDataSLE(printData);
			}
			cardNo='';
			//updateBalance();

		}

	}

 /**
  * For Reprint
  */
 
 $(document).on('click','#reprint',function(){
		curTrx = "Reprint";
		var requestParam = path+'/com/skilrock/sle/web/merchantUser/playMgmt/action/reprintTicket.action';
		var reprintTicketData =_ajaxCallJson(requestParam,'','');
		//console.log(JSON.stringify(reprintTicketData));
		if(reprintTicketData.isSuccess){
			
			 var reprintData = {
								 "serviceName" : "SLE",
								 "mode" : curTrx,
								 "finalBetAmount": finalBetAmount,
			 					 "gameDevName" : reprintTicketData.tktData.gameTypeName,
								 "orgName" : reprintTicketData.tktData.orgName,
								 "advMsg" : reprintTicketData.advMessage, 
								 "retailerName" : reprintTicketData.tktData.retailerName,
								 "brCd" :  reprintTicketData.tktData.brCd, 
								 "ticketNumber" :reprintTicketData.tktData.ticketNo,
								 "gameName" : reprintTicketData.tktData.gameName,
								 "purchaseDate" :dateFormat(new Date(getCompataibleDate2( reprintTicketData.tktData.purchaseDate)),dateFormat.masks.sikkimDate),
								 "purchaseTime" : getCompataibaleTime(reprintTicketData.tktData.purchaseTime),
								 "purchaseAmt" : reprintTicketData.tktData.ticketAmt,
								 "boardData" :  reprintTicketData.tktData.boardData,
								 "currencySymbol" : reprintTicketData.tktData.currSymbol, 
								 "eventType":reprintTicketData.tktData.eventType,
								 "jackpotMsg":reprintTicketData.tktData.jackpotMsg,
								 "cardNo":cardNo,
								 "parentOrgName":$('#parentOrgName').val()
			 };
			 currencySymbol=reprintTicketData.tktData.currSymbol;
			
	    	//setAppData(JSON.stringify(reprintTicketData));	
	    }else{
	    	$("#error-popup").show(400);
			$("#error").html(reprintTicketData.errorMsg);
			$("#error-popup").delay(2000).hide(400);
	       //alert(reprintTicketData.errorMsg);
	    }
		 printData = JSON.stringify(reprintData);
		// console.log(printData);
		 //prepareReprintTicket(JSON.parse(printData));
		if("YES" == printUsingApplet){
			$(".tktView").css("display","none");
			setAppletData(printData);
    	}else{
    		setAppDataSLE(printData);
    	}
 });

 
 /**
  * for cancel Ticket
  */
 
 $(document).on('click','#cancel',function(){
		var chkCancel = confirm('Cancel Last Ticket?');
		if (!chkCancel) {
			return;
		}
		var requestParam = path+'/com/skilrock/sle/web/merchantUser/playMgmt/action/cancelTicket.action';
		var cancelTicketData =_ajaxCallJson(requestParam,"autoCancel=false&cancelType=CANCEL_MANUAL",'');
		//parent.frames[0].updateBalance(projectName+"/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
		//console.log(JSON.stringify(cancelTicketData));
		//updateBalance();
		if(cancelTicketData.isSuccess){
			curTrx = "Cancel";
			var cancelData = {
					"serviceName" : "SLE",
					'mode': curTrx,
					'ticketNumber':cancelTicketData.ticketNumber,
					'orgName':cancelTicketData.orgName,
					'gameName': cancelTicketData.gameName,
					'gameDispName': cancelTicketData.gameTypeName,
					'refundAmount':cancelTicketData.refundAmount,
					'cancelTime':cancelTicketData.cancelDateTime,
					'advMsg':cancelTicketData.advMessage,
					'currencySymbol':cancelTicketData.currSymbol,
					'parentOrgName':$('#parentOrgName').val()
				  };
         }else{
	    	$("#error-popup").show(400);
			$("#error").html(cancelTicketData.errorMsg);
			$("#error-popup").delay(2000).hide(400);
	    	//alert(cancelTicketData.errorMsg);
	    }
		printData = JSON.stringify(cancelData);
    	//console.log(printData);
    	//prepareCancelTicket(JSON.parse(printData));
		if("YES" == printUsingApplet){
			$(".tktView").css("display","none");
			setAppletData(printData);
    	}else{
    		setAppDataSLE(printData);
    	}
		
	});
	
 
 /**
  * pwt 
  * prize winning ticket
  */
 
 $(document).on('click','#pwt',function(){
	 $("#sleParentApplet").css("display","none");
	 $(".tktView").css("display","block");
	 keyupFiredCount1=0;
	 $("#pwtTicket").val("");
	 $("#error-message").html("");
	 $('#pwt-win').css('display','block');
	 $("#pwtTicket").focus();
 });
 $("#pwtTicket").keypress(function(){
	 $("#error-message").html("");
	});
 
 $(document).on('click','#pwtClose',function(){
	 $('#pwt-win').css('display','none');
 });

 $(document).on('click','#pwtCancel',function(){
	 $('#pwt-win').css('display','none');
 });
 
 $(document).on('click','#pwtOk',function(){
	 $("#error-message").html("");
	 doVerifyTicket();
 });
 
 /**
  * verification of Ticket
  */
 
 function  doVerifyTicket(){
	 keyupFiredCount1=0;
	    if($('#pwtTicket').val().replace(/(^\s+|\s+$)/g, "").length ==0){
	    	$("#error-message").html("Please Enter Ticket Number");
	    	$('#pwtTicket').val("");
	       //alert("Please Enter Ticket Number");
	       return;
	    }
	    if($('#pwtTicket').val().replace(/(^\s+|\s+$)/g, "").length !=18 && $('#pwtTicket').val().replace(/(^\s+|\s+$)/g, "").length !=20){
		  //alert("Invalid Ticket Number");
		  $("#error-message").html("Invalid Ticket Number");
		   return;
	     }
	  
	    var verifyPwtData="";
	    var winData = JSON.stringify({
			  "merchantCode": $('#merCode').val(),
			  "userName"    : $('#userName').val(),
	     	  "sessionId"   : $('#sessionId').val(),
	     	  "ticketNumber": $('#pwtTicket').val()
		});

	    var requestParam=path+'/com/skilrock/sle/web/merchantUser/pwtMgmt/Action/verifyWebTicket.action';
	    verifyPwtData = _ajaxCallJson(requestParam,"requestData="+winData,'');
	    winRespData = JSON.stringify(verifyPwtData);
	   //winRespData='{"currDate":"2016-07-25|currTime:17:08:12","responseCode":0,"drawTime":"2016-09-17 06:00:00","saleMerCode":"RMS","drawName":"THURSDAY-S13","prizeAmt":"10000.00","claimStatus":true,"gameName":"Soccer","boardCount":1,"totalPay":"10000.00 USD","message":"Draw Expired","ticketNo":"666662259104590020","totalClmPend":0,"gameTypeName":"Soccer 13","responseMsg":"SUCCESS"}';
	    //console.log(JSON.stringify(verifyPwtData));
	    var verifyResult = $.parseJSON(winRespData);
	    
	    //alert(verifyResult.ticketNo +" "+ verifyResult.message +" "+verifyResult.prizeAmt); 
		if(verifyResult.responseCode==0){
			if(verifyResult.claimStatus){
				var merCode = $("#merCode").val();
				$("#saleMerCode").val(verifyResult.saleMerCode);
				if("Asoft" == verifyResult.saleMerCode){
					verifyCode = prompt("Ticket Number :" +verifyResult.ticketNo +" has winning "+verifyResult.totalPay+"\n"+"Please enter verification code to claim!");
					if(verifyCode.length == 7 && !isNaN(verifyCode)){
						 $('#pwt-win').css('display','none');
						doPayPwt();
					} else {
						$('#pwt-win').css('display','none');
						$("#error-popup").show(400);
						$("#error").html("Invalid verification code!");
						$("#error-popup").delay(2000).hide(400);
						//alert("Invalid verification code!");
						 $('#pwtTicket').val('');
					}
				}
				else{
					var flag = confirm("Ticket Number :" +verifyResult.ticketNo +" has winning "+verifyResult.totalPay+"\n"+"Do you want to claim ?");
			         if(flag){
			        	 $('#pwt-win').css('display','none');
						doPayPwt();
						}
			         else{
			        	 $('#pwtTicket').val('');
			         }
				}
				
			}else{
				if(verifyResult.responseCode!=0){
					//alert(verifyResult.responseMsg);
					$('#pwt-win').css('display','none');
					$("#error-popup").show(400);
					$("#error").html(verifyResult.responseMsg);
					$("#error-popup").delay(2000).hide(400);
				}
				else{
					//alert(verifyResult.message);
					$('#pwt-win').css('display','none');
					$("#error-popup").show(400);
					$("#error").html(verifyResult.message);
					$("#error-popup").delay(2000).hide(400);
					
				}
			}
		}else{
			if(verifyResult.responseCode!=0){
				$('#pwt-win').css('display','none');
				$("#error-popup").show(400);
				$("#error").html(verifyResult.responseMsg);
				$("#error-popup").delay(2000).hide(400);
				//alert(verifyResult.responseMsg);
			}
			else{
				$('#pwt-win').css('display','none');
				$("#error-popup").show(400);
				$("#error").html(verifyResult.message);
				$("#error-popup").delay(2000).hide(400);
				//alert(verifyResult.message);
				
			}
		}
 }
 
 /**
  * After verification
  * pay pwt ticket
  */
 
 function doPayPwt(){
		
	 var pwtReqData = JSON.stringify({
		  "merchantCode": $('#merCode').val(),
		  "userName"    : $('#userName').val(),
		  "sessionId"   : $('#sessionId').val(),
		  "ticketNumber": $('#pwtTicket').val(),
		  "saleMerCode" : $('#saleMerCode').val(),
		  "verificationCode" : ($('#saleMerCode').val() == 'Asoft'? verifyCode:"")
		  });
	 var requestParam = path+'/com/skilrock/sle/web/merchantUser/pwtMgmt/Action/payPwtWebTicket.action';
     verifyCode='';
     payPwtData = _ajaxCallJson(requestParam,"requestData="+pwtReqData,'');
    // payPwtData='{"currDate":"2016-07-25|currTime:17:08:12","responseCode":0,"drawTime":"2016-09-17 06:00:00","saleMerCode":"RMS","drawName":"THURSDAY-S13","prizeAmt":"10000.00","claimStatus":true,"gameName":"Soccer","boardCount":1,"totalPay":"10000.00 USD","message":"Draw Expired","ticketNo":"666662259104590020","totalClmPend":0,"gameTypeName":"Soccer 13","responseMsg":"SUCCESS"}';
     winRespdata = JSON.stringify(payPwtData);
     //winRespdata='{"currDate":"2016-07-25|currTime:17:08:12","responseCode":0,"drawTime":"2016-09-17 06:00:00","saleMerCode":"RMS","drawName":"THURSDAY-S13","prizeAmt":"10000.00","claimStatus":true,"gameName":"Soccer","boardCount":1,"totalPay":"10000.00 USD","message":"Draw Expired","ticketNo":"666662259104590020","totalClmPend":0,"gameTypeName":"Soccer 13","responseMsg":"SUCCESS"}';
     //console.log(JSON.stringify(payPwtData));
     var payPwtRespData = $.parseJSON(winRespdata);
     var responseCode = payPwtRespData.responseCode;
     var pwtFinalRespData='';
     updateBalance();
     if(responseCode != 0){
    	    $('#pwt-win').css('display','none');
    	    $("#error-popup").show(400);
			$("#error").html(payPwtRespData.message);
			$("#error-popup").delay(2000).hide(400);
    	    //alert(payPwtRespData.message);
	 } else {
		 curTrx = "PWT";
		 //setAppData(winRespdata);
		 pwtFinalRespData={
				 "serviceName" : "SLE",
				 "mode" : curTrx,		 
				 "gameDevName" : payPwtRespData.gameTypeName,
				 "orgName" : payPwtRespData.orgName,
				 "advMsg" : payPwtRespData.advMessage, 
				 "retailerName" : payPwtRespData.retailerName,
				 "ticketNumber" :payPwtRespData.ticketNo,
				 "gameName" : payPwtRespData.gameName,
				 "currencySymbol" : payPwtRespData.currSymbol,
				 "drawDateTime"  : payPwtRespData.drawTime,
				 "drawName"      :payPwtRespData.drawName,
				 "reprintCountPWT":payPwtRespData.reprintCountPWT,
				 "pwtDateTime"  :payPwtRespData.pwtDateTime,
				 "boardCount"  :payPwtRespData.boardCount,
				 "totalPay" : payPwtRespData.totalPay,
				 "message"  : payPwtRespData.message,
				 "balance" :  payPwtRespData.balance,
				 "totalWinAmt":payPwtRespData.totalWinAmt,
                 "responseMsg" : payPwtRespData.responseMsg,
                 "claimPndAmt" : payPwtRespData.claimPndAmt,
                 "parentOrgName":$('#parentOrgName').val()
		};
		 currencySymbol= payPwtRespData.currSymbol;
		 printData = JSON.stringify(pwtFinalRespData);
		// console.log(JSON.stringify(printData));
		// preparePwtTicket(JSON.parse(printData));
		if("YES" == printUsingApplet){
			$(".tktView").css("display","none");
			setAppletData(printData);
    	}else{
    		setAppDataSLE(printData);
    	}
	}
 }
 
 /**
  * match list data fetch
  */
 
 //common method for making game menu for match list
 
 function makeGameMenuForPopup(){
	 $("#matchListWindow").css("display","block");
	 var matchListMenuHtml='';
	 i=0;
	 if(respData.responseCode == 0){
			$(respData.sleData.gameData[gameIndex].gameTypeData).each(function(){
				matchListMenuHtml +='<button menu-id ="'+i+'~'+this.gameTypeId+'" class="match-list-menu activeClass'+(this.gameTypeId-1)+'"><span class="logo-game"><img src="'+path+'/sleGamePlayAssets/sleContent/sports-img/sports_lottery'+(4-i)+'.png" width="90" alt=""></span></button>';
					i++;
			 });
			$(".game-name-tab").html(matchListMenuHtml);
			
		}else {
			$(".game-name-tab").html("No Match List Available !!!");
		}
 } 
 
 
 $(document).on('click', '#showMatchList', function(){
	 
	 var gameTypeIdSML='';	 
	 $(".sideMenuList").each(function(){
		 if($(this).hasClass("select-game")){
			 gameTypeIdSML = $(this).attr("menu-id").split("~")[0];
		 }
	 });
	 $("#popupName").html("Match List");
	 $("#resultListData").css("display","none");
	 makeGameMenuForPopup();//making game menu for metch list
	 $(".activeClass"+gameTypeIdSML).addClass("active-button-result");
	 $("#matchListWindow").css("display","block");
	 updateBaseMatchListFirst(gameTypeIdSML);
 });
 
 $(document).on('change', '#drawResultId', function(){
		updateBaseSleDrawResult();
	});	
 
 $(document).on('change', '#drawMatchId', function(){
		updateBaseMatchList();
	});
 $(document).on('click','#closeMatchList',function(){
	 $("#matchListWindow").css("display","none");
 });
 
 $(document).on('click', '#printMatchList', function(){
	    $("#matchListWindow").css("display","none");
		var drawTypeData = $('#drawMatchDetailBeanListForApplet').val();
	    var drawTypeList = jQuery.parseJSON(drawTypeData).drawData;
	    
		var drawId = $('#drawMatchId').prop("selectedIndex");
		var matchListResponse = drawTypeList[drawId];
		//console.log(matchListResponse);
		curTrx = "MATCH_LIST";
		//matchListResponse=JSON.stringify(drawMatchListData);
		
		var finalMatchListResp={};
		finalMatchListResp={
				 "serviceName" : "SLE",
				 "mode" : curTrx,	
				  "drawDateTime":matchListResponse.drawDateTime,
				  "drawDisplayString":matchListResponse.drawDisplayString,
				  "drawId":matchListResponse.drawId,
			      "drawName":matchListResponse.drawName,
			      "eventData":matchListResponse.eventData,
				  "gameDispName":matchListResponse.gameDispName,
				  "gameTypeDispName":matchListResponse.gameTypeDispName,
				  "orgName":matchListResponse.orgName,
				  "parentOrgName":$('#parentOrgName').val()
		};
		 printData = JSON.stringify(finalMatchListResp);
		 //console.log(printData);
		if("YES" == printUsingApplet){
			$(".tktView").css("display","none");
			setAppletData(printData);
    	}else{
    		setAppDataSLE(printData);
    	}
	});
 
 $(document).on('click','#matchList',function(){
	 $("#matchListPanelData").html("");
	 $("#popupName").html("Match List");
	 $("#resultListData").css("display","none");
	 var matchListMenuHtml='';
	 var i=0;
	 makeGameMenuForPopup();//making game menu for metch list
	 var gameTypeIdIndex='';
     for(gameTypeIdIndex=0;gameTypeIdIndex<4;gameTypeIdIndex++){
    	 var matchreqData = JSON.stringify({
			  "merchantCode": $('#merCode').val(),
			  "userName"    : $('#userName').val(),
	     	  "sessionId"   : $('#sessionId').val(),
	     	  "gameId"		: respData.sleData.gameData[0].gameId,
	     	  "gameTypeId"  : respData.sleData.gameData[0].gameTypeData[gameTypeIdIndex].gameTypeId

		});
		var requestParam = path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/fetchMatchlistData.action';
		_ajaxCallDiv(requestParam,"requestData="+matchreqData,'hiddenMatchListPanelData');
		var drawTypeDataCheck = $('#drawMatchDetailBeanList').val();
		 if(drawTypeDataCheck!=undefined){
			 $(".activeClass"+gameTypeIdIndex).addClass("active-button-result");
			 updateBaseMatchListFirst(gameTypeIdIndex); 
			 return false;
		 }
		 else{
			 $("#matchListPanelData").append("Match not available");
		 }
	 }
});
 
 
 $(document).on('click','.match-list-menu',function(){
	 $("#hiddenMatchListPanelData").html("");
	 $(".match-list-menu").removeClass("active-button-result");
	 $(this).addClass("active-button-result");
	 var gameTypeIdValue = $(this).attr('menu-id').split("~")[0];
	 updateBaseMatchListFirst(gameTypeIdValue);
});
 
 function updateBaseMatchListFirst(gameTypeIdFirst){
	 $("#matchListWindowData").css("display","block");
	 $("#matchListPanelDatawise").html("");
	 var matchreqData = JSON.stringify({
			  "merchantCode": $('#merCode').val(),
			  "userName"    : $('#userName').val(),
	     	  "sessionId"   : $('#sessionId').val(),
	     	  "gameId"		: respData.sleData.gameData[0].gameId,
	     	  "gameTypeId"  : respData.sleData.gameData[0].gameTypeData[gameTypeIdFirst].gameTypeId

		});
		var requestParam = path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/fetchMatchlistData.action';
		_ajaxCallDiv(requestParam,"requestData="+matchreqData,'matchListPanelData');
		updateBaseMatchList();
 }
 
 function updateBaseMatchList(){
	 var drawTypeData = $('#drawMatchDetailBeanList').val();
	 if(drawTypeData!=undefined){
		 var drawTypeList = jQuery.parseJSON(drawTypeData);
		 var drawId = $('#drawMatchId').prop("selectedIndex");
		 var eventList = drawTypeList[drawId].eventMasterList;
		 var evtDisplayLength=eventList.length;
		 var matchListDataHtml='';
		 if(eventList!=undefined && eventList.length>0 ){
			 $.each(eventList, function(key, value) {
			 matchListDataHtml+='<li> <div class="scgameDiv"> <div class="scgameDivN">';
	         matchListDataHtml+='<div class="sctitleAnd"><div class="dateWrap">'+value.startTime.split(" ")[0]+' <strong>'+value.startTime.split(" ")[1]+'</strong></div>';
			 matchListDataHtml+='<div class="sctitleName">'+value.leagueName+','+value.venueName+'</div></div>';
			 matchListDataHtml+='<div class="scteamTitle">';
			 matchListDataHtml+='<div class="col-xs-5 text-center"><div class="scabbriName">'+value.homeTeamName+'</div></div>';
			 matchListDataHtml+='<div class="col-xs-2 text-center"><span class="scvsWrap">VS</span></div>';
			 matchListDataHtml+='<div class="col-xs-5 text-center"><div class="scabbriName">'+value.awayTeamName+'</div></div></div></div></div></li>';
	      });
		 }
		 else{
			matchListDataHtml+='Match not available'; 
		 }
		 $("#matchListPanelDatawise").html(matchListDataHtml);
	 }
	 else{
		 $("#matchListPanelData").html("");
		 $("#matchListPanelDatawise").html("Match not available");
	 }
 }
/**
 * on click on result button 
 */
$(document).on('click','#result',function(){
	 $("#popupName").html("Results");
	 $(".game-name-tab").html("");
	 $("#matchListWindow").css("display","block");
	 $("#matchListWindowData").css("display","none");
	 var matchListMenuHtml='';
	 var i=0;
	 if(respData.responseCode == 0){
			$(respData.sleData.gameData[gameIndex].gameTypeData).each(function(){
				matchListMenuHtml +='<button menu-id ="'+i+'~'+this.gameTypeId+'" class="result-menu activeClass'+(this.gameTypeId-1)+'"><span class="logo-game"><img src="'+path+'/sleGamePlayAssets/sleContent/sports-img/sports_lottery'+(4-i)+'.png" width="90" alt=""></span></button>';
					i++;
			 });
			$(".game-name-tab").html(matchListMenuHtml);
			
		}else {
			$(".game-name-tab").html("No Result Available !!!");
		}
	 var gameTypeIdIndex='';
     for(gameTypeIdIndex=0;gameTypeIdIndex<4;gameTypeIdIndex++){
    	 var matchreqData = JSON.stringify({
			  "merchantCode": $('#merCode').val(),
			  "userName"    : $('#userName').val(),
	     	  "sessionId"   : $('#sessionId').val(),
	     	  "gameId"		: respData.sleData.gameData[0].gameId,
	     	  "gameTypeId"  : respData.sleData.gameData[0].gameTypeData[gameTypeIdIndex].gameTypeId

		});
		var requestParam = path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/fetchSLEDrawResultData.action';
		_ajaxCallDiv(requestParam,"requestData="+matchreqData,'hiddenMatchListPanelData');
		var drawTypeDataCheck = $('#drawResultBeanList').val();
		 if(drawTypeDataCheck!=undefined){
			 $(".activeClass"+gameTypeIdIndex).addClass("active-button-result");
			 updateBaseSleDrawResultFirst(gameTypeIdIndex); 
			 return false;
		 }
		 else{
			 $("#resultListDataPanel").html("No Result available");
		 }
	 }
 });
 
$(document).on('click','.result-menu',function(){
	$("#hiddenMatchListPanelData").html("");
	 $(".result-menu").removeClass("active-button-result");
	 $(this).addClass("active-button-result");
	 var gameTypeIdValue = $(this).attr('menu-id').split("~")[0];
	 updateBaseSleDrawResultFirst(gameTypeIdValue);
 });

function updateBaseSleDrawResultFirst(gameTypeId){
	$("#resultListDataPanelWise").html("");
	 var resultData='';
	var resultData = JSON.stringify({
		  "merchantCode": $('#merCode').val(),
		  "userName"    : $('#userName').val(),
   	      "sessionId"   : $('#sessionId').val(),
   	      "gameId"		: respData.sleData.gameData[0].gameId,
    	  "gameTypeId"  : respData.sleData.gameData[0].gameTypeData[gameTypeId].gameTypeId

	});
	 var requestParam = path+'/com/skilrock/sle/web/merchantUser/drawMgmt/Action/fetchSLEDrawResultData.action';
	 _ajaxCallDiv(requestParam,"requestData="+resultData,'resultListDataPanel');
	 $("#resultListData").css("display","block");
	 updateBaseSleDrawResult();
}

function updateBaseSleDrawResult(){
	$("#hiddenMatchListPanelData").html("");
	 $("#resultListDataPanelWise").html("");
	 var drawTypeData = $('#drawResultBeanList').val();
	 if(drawTypeData!=undefined){
		 var drawTypeList = jQuery.parseJSON(drawTypeData);
		 //console.log(JSON.stringify(drawTypeList));
		 var drawId = $('#drawResultId').prop("selectedIndex");
		 var eventList = drawTypeList[drawId].eventMasterList;
		 var evtDisplayLength=eventList.length;
		 var resultListDataHtml='';
		 if(eventList!=undefined && eventList.length>0 ){
			 $.each(eventList, function(key, value) {
				 resultListDataHtml='';
				 resultListDataHtml+=' <li><div class="gameDiv sportsLottWrap"><div class="gameDivInner">';
				
				 resultListDataHtml+='<div class="titleAndDateWrap"><div class="dateWrap">'+value.startTime.split(" ")[0]+'<strong>'+value.startTime.split(" ")[1]+'</strong></div><div class="titleNameWrap">African Premier League, Madagascar</div></div>';
				 resultListDataHtml+='<div class="row teamTitle">';
				 resultListDataHtml+='<div class="col-xs-5 text-center"><div class="abbriName">'+value.homeTeamName+'</div></div>';
				 resultListDataHtml+='<div class="col-xs-2 text-center"><span class="vsWrap">VS</span></div>';
				 resultListDataHtml+='<div class="col-xs-5 text-center"><div class="abbriName">'+value.awayTeamName+'</div></div></div>';
                         
				 resultListDataHtml+='<div class="row selectArea">';
				 if(evtDisplayLength==4 || evtDisplayLength== 6){
					 resultListDataHtml+='<div class="col-xs-2"><span class="textWrap" id="'+value.eventId+'-H2">H+</span></div>';
					 resultListDataHtml+='<div class="col-xs-2"><span class="textWrap" id="'+value.eventId+'-H">H</span></div>';
					 resultListDataHtml+='<div class="col-xs-2"><span class="textWrap" id="'+value.eventId+'-D">D</span></div>';
					 resultListDataHtml+='<div class="col-xs-2"><span class="textWrap" id="'+value.eventId+'-A">A</span></div>';
					 resultListDataHtml+='<div class="col-xs-2"><span class="textWrap" id="'+value.eventId+'-A2">A+</span></div>';
				 }
				 else{
					 resultListDataHtml+='<div class="col-xs-4"><span class="textWrap" id="'+value.eventId+'-H">Home</span></div>';
					 resultListDataHtml+='<div class="col-xs-4"><span class="textWrap" id="'+value.eventId+'-D">Draw</span></div>';
					 resultListDataHtml+='<div class="col-xs-4"><span class="textWrap" id="'+value.eventId+'-A">Away</span></div>';
				 }
                             
                 resultListDataHtml+='</div> </div></div></li>';
                 $("#resultListDataPanelWise").append(resultListDataHtml);
                 if(value.winninOptionCode=="C"){
                	 if(evtDisplayLength==4 || evtDisplayLength== 6){
                		 var evid=value.eventId+"-H2"; 
                	 }
                	 else{
                		 var evid=value.eventId+"-H"; 
                	 }
     				$("#"+evid).parent().parent().append('<span class="redBlocker"><div class="row"><div class="col-xs-3">Match<br>Cancelled</div> <div class="col-xs-9">This Bet will Considered As Winning</div></div></span>');
                	// $("#"+evid).parent().children().addClass('selected');
     				//$("#"+evId).parent().children().addClass('selected');
     			}else{
     				var evid=value.eventId+"-"+value.winninOptionCode.replace("+","2");
     				$("#"+evid).addClass("selected");
     			}
          });
		 }
		 else{
			matchListDataHtml+='No Result available !!!';
			$("#resultListDataPanelWise").html(resultListDataHtml);
		 }
		 
	 }
	 else{
		// $("#resultListDataPanelWise").html("No Result available !!!");
	 }
}
/**
 *close popup 
 */
  $('#closePopUp').on('click', function() {
	  $("#error-popup").hide(400);
  }); 

 /**
  * onclick on setting button
  */
  $('#setting-btn').on('click', function(event) { 
	  $("#sleParentApplet").css("display", "none");
	  $(".tktView").css("display","block");
	  if($("#open-list-btn").css("display") == "block"){
			$("#open-list-btn").css("display","none");
			return false;
		} else if($("#open-list-btn").css("display") == "none"){
			$("#open-list-btn").css("display","block");
			return false;
		}
  });
 
  //sportsLottWrapInner
  $(document).click(function(event) { 
	    if(!$(event.target).closest('#settings-btn').length &&
	       !$(event.target).is('#settings-btn')) {
	        if($("#open-list-btn").css("display") == "block") {
	            $("#open-list-btn").css("display","none");
	           
	        }
	    }        
	});;

 
 /**
  * click to show advance draw 
  */
 $(document).on('click','.select-draw',function(){
	 $("#advDrawWindow").css('display','block');
 });
 $(document).on('click','#closeAdvDrawWindow',function(){
	 $("#advDrawWindow").css('display','none');
 });
 $(document).on('click','#cancelAdvDrawWindow',function(){
	 $("#advDrawWindow").css('display','none');
 });
 
 /**
  * to select advance draw 
  */
 $(document).on('click','#submitAdvDrawWindow',function(){
	 var advDrawPanelHtml='';
	 var gameTypeIdAdv=''; 
	 $("#advDrawWindow").css('display','none'); 
	 $(".sideMenuList").each(function(){
		 if($(this).hasClass("select-game")){
			 gameTypeIdAdv = $(this).attr("menu-id").split("~")[0];
		 }
	 });
	if($('.advDrawSelect').is(':checked')) {
		 eventArr=[];
		 resetAllGames();
		 $("#sportsLottWrapInnerSoccerOther").html("");
		 $("#sportsLottWrapInnerSoccer4").html("");
		 
		 var isEvtArrPrepareAdv = false;
		 if(respData.sleData.gameData[0].gameTypeData[gameTypeIdAdv].drawData[drawCountIndex].eventData!= undefined){
			 var drawId = respData.sleData.gameData[0].gameTypeData[gameTypeIdAdv].drawData[drawCountIndex].drawId;
			 $("#drawId").val(drawId);
			 $(".select-draw").html(respData.sleData.gameData[gameIndex].gameTypeData[gameTypeIndex].drawData[drawCountIndex].drawDateTime);
			 $(".sports-no-dr").html("<span>"+respData.sleData.gameData[0].gameTypeData[gameTypeIdAdv].drawData[drawCountIndex].drawDateTime+"</span>");
		     $(respData.sleData.gameData[0].gameTypeData[gameTypeIdAdv].drawData[drawCountIndex].eventData).each(function(){
				eventId = this.eventId;
				k=0;
				advDrawPanelHtml+='<li><div class="gameDiv sportsLottWrap"><div class="gameDivInner">';
				advDrawPanelHtml+= '<div class="titleAndDateWrap"><div class="dateWrap">'+this.eventDate.split(" ")[0]+' '+" "+'<strong>'+this.eventDate.split(" ")[1]+'</strong></div>';
				advDrawPanelHtml+= '<div class="titleNameWrap">'+this.eventLeague+','+this.eventVenue+'</div><div class="clearBoth"></div></div>';
				advDrawPanelHtml+= '<div class="row teamTitle"><div class="col-xs-5 text-center"><div class="abbriName">'+this.eventDisplayHome+'</div></div>';
				advDrawPanelHtml+= '<div class="col-xs-2 text-center"><span class="vsWrap">V/S</span></div>';
				advDrawPanelHtml+= '<div class="col-xs-5 text-center"><div class="abbriName">'+this.eventDisplayAway+'</div></div>';
				advDrawPanelHtml+= '</div>';
				advDrawPanelHtml+= '<div class="row selectArea">';
				if(gameTypeIdAdv == 2 || gameTypeIdAdv == 3){
					$(eventOpts).each(function(){
						advDrawPanelHtml+= '<div class="col-xs-2"><span class="numWrap" id="'+gameTypeIdAdv+'-'+drawId+'-'+eventId+'-'+eventOpts[k].replace(/(^\s+|\s+$)/g, "").replace('+','2')+'">'+eventOpts[k]+'</span></div>';
						k++;
					});
				}else{
					$(eventOpts).each(function(){
						 if(eventOpts[k].replace(/(^\s+|\s+$)/g, "") == "H"){
							 advDrawPanelHtml+= '<div class="col-xs-4"><span class="numWrap" id="'+gameTypeIdAdv+'-'+drawId+'-'+eventId+'-'+eventOpts[k].replace(/(^\s+|\s+$)/g, "").replace('+','2')+'">HOME</span></div>';
					     }
						else if(eventOpts[k].replace(/(^\s+|\s+$)/g, "") == "D"){
							advDrawPanelHtml+= '<div class="col-xs-4"><span class="numWrap" id="'+gameTypeIdAdv+'-'+drawId+'-'+eventId+'-'+eventOpts[k].replace(/(^\s+|\s+$)/g, "").replace('+','2')+'">DRAW</span></div>';
						   }
						else if(eventOpts[k].replace(/(^\s+|\s+$)/g, "") == "A"){
							advDrawPanelHtml+= '<div class="col-xs-4"><span class="numWrap" id="'+gameTypeIdAdv+'-'+drawId+'-'+eventId+'-'+eventOpts[k].replace(/(^\s+|\s+$)/g, "").replace('+','2')+'">AWAY</span></div>';
					    }
						 k++;
					});
				}
				var tmpEventDataAdv = respData.sleData.gameData[0].gameTypeData[gameTypeIdAdv].drawData[drawCountIndex].eventData;
				if(!isEvtArrPrepareAdv){
					for(var l=0;l<tmpEventDataAdv.length;l++){
						eventArr.push({
										"eventId" : tmpEventDataAdv[l].eventId,
										"eventSelected" : ""
									});	
					}
				}
		isEvtArrPrepareAdv = true;
			
			 });
			
	 }    if(gameTypeIdAdv == 3){
		 $("#sportsLottWrapInnerSoccer4").append(advDrawPanelHtml);
	 }else{
		 $("#sportsLottWrapInnerSoccerOther").append(advDrawPanelHtml);
	 }
		  
	} 
 });
 
 /**
  * update balance of Retailer
  */
 function updateBalance(){
		parent.frames[0].postMessage('updateBalance', document.referrer);
	}
 
 /**
  * to select manually betamount on click
  */
 $(document).on('click','.unitPrice',function(){
	 $("#otherBtn").show();	
	 $("#otherAmt").css('display','none');
	 $(".unitPrice").removeClass("amt-select");
	 $(this).addClass("amt-select");
	 $("#betAmount").val($(this).text());
	 finalBetAmount=$("#betAmount").val();
	 var maxBetAmtMul= parseFloat(gameTypeMaxBetAmtMultiple);
	 var counterObj	= $("#betAmount");
	 currentBetAmt =parseFloat(counterObj.val());
	 currentBetMul= Math.round(currentBetAmt/unitPrice);
     counterObj.val(pmsRoundOff(currentBetAmt,(unitPrice+"").length));
	 updateTicketPrice((unitPrice+"").length);
	 
}); 
 
 /**
  * to select manually betamount by type
  */
  $(document).on('click','#otherBtn',function(){
	  $("#otherBtn").hide();
	  $("#otherAmt").css('display','block');
	  $("#otherAmt").focus();
	  $("#tktPrice").html("0");
	  $("#buy-btn").attr("disabled",true);
	  $(".unitPrice").removeClass("amt-select");
	  
  });
  $(document).on("keyup","#otherAmt",function(e){
		if(!isNaN($(this).val().replace(/(^\s+|\s+$)/g, ""))){
			 var tempBetAmount=$(this).val();
			  if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105) || e.keyCode == 8 ){
				    if(parseFloat($(this).val().replace(/(^\s+|\s+$)/g, "")) < parseFloat(unitPrice) && parseFloat($(this).val().replace(/(^\s+|\s+$)/g, ""))!=0 ){
				    	 $(this).focus();
						 $(this).val("");
						 $("#tktPrice").html("0");
						 $("#error-popup").show(300);
						 $("#error").html("Price must be greater than "+unitPrice);
						 $("#error-popup").delay(1000).hide(300);
						 $("#buy-btn").attr("disabled",true);
					}
				    else if(parseFloat($(this).val().replace(/(^\s+|\s+$)/g, "")) > parseFloat(unitPrice * gameTypeMaxBetAmtMultiple) || parseFloat(($(this).val().replace(/(^\s+|\s+$)/g, "")) % parseFloat(unitPrice)) != 0){
				    	 $(this).focus();
						 $(this).val("");
						 $("#tktPrice").html("0"); 
				    	 $("#error-popup").show(300);
				    	 $("#error").html("Price must less than "+(unitPrice * gameTypeMaxBetAmtMultiple)+" and a multiple of "+unitPrice);
						 $("#error-popup").delay(1000).hide(300);
						 $("#buy-btn").attr("disabled",true);
				    }
				    else if($(this).val().replace(/(^\s+|\s+$)/g, "")== "" ||$(this).val().replace(/(^\s+|\s+$)/g, "") == null){
				    	$("#tktPrice").html("0"); 
				    	 if(gameTypeIndex<=1){
				    		 $("#betAmount").val(1);	 
				    	 }
				    	 else{
				    		 $("#betAmount").val(0.50);
				    	 }
				    	 $("#buy-btn").attr("disabled",true);
				    	
				    }
				    else{ 
				    	  $("#betAmount").val($(this).val().replace(/(^\s+|\s+$)/g, ""));
				    	  finalBetAmount=$("#betAmount").val();
						 var maxBetAmtMul= parseFloat(gameTypeMaxBetAmtMultiple);
						 var counterObj	= $("#betAmount");
				    	 currentBetAmt =parseFloat(counterObj.val());
						 currentBetMul= Math.round(currentBetAmt/unitPrice);
					     counterObj.val(pmsRoundOff(currentBetAmt,(unitPrice+"").length));
						 updateTicketPrice((unitPrice+"").length);
						 if($("#tktPrice").html() != 0){
							 $("#buy-btn").attr("disabled",false);
						 }
						 else{
							 $("#tktPrice").html("0");
						 }
						
				    }
			  }
	    }
		else{
			$(this).focus();
			$(this).val("");
			$("#tktPrice").html("0");
			$("#error-popup").show(300);
			$("#error").html("Price must be multiple of "+unitPrice);
			$("#error-popup").delay(1000).hide(300);
		}
	 
  });

  
  /**
   * closing popup window on esc
   */
  $(document).on('keyup',function(e) {
	  if(e.which == 27){
		  if($("#advDrawWindow").css("display") == "block"){
			  $("#advDrawWindow").css("display","none"); 
		  }
		  if($("#pwt-win").css("display") == "block"){
			  $("#pwt-win").css("display","none"); 
		  }
		  if($("#matchListWindow").css("display") == "block"){
			  $("#matchListWindow").css("display","none"); 
		  }
		  if($("#card-no").css("display") == "block"){
			  $('#cardClose').click();
		  }
	  }
	  if(e.which == 13){
			  if($("#pwt").css("display") == "block"){
				  $("#pwtOk").click();
			  }  
		  }
  });

 /**
  * Reset All Games Of Sports Lottery
  */
 function resetAllGames(){
	 resetBetAmountMenu();	
	 $("#buy-btn").attr("disabled",true);
	 $("#selection").html("");
	 $("#noOfLines").html("0");
	 $("#tktPrice").html(0);
	 if(gameTypeIndex<=1){
		 $("#betAmount").val(1);	 
	 }
	 else{
		 $("#betAmount").val(0.50);
	 }
	 $(".numWrap").removeClass("selected");
	 $(".unitPrice").removeClass("amt-select");
	 $(".unitPrice").first().addClass("amt-select");
	 finalSelection="";
	 selection='';
//	 noOfLines=0;
	 for(var l=0;l<eventArr.length;l++){
		 eventArr[l].eventSelected='';
		}
}
function resetBetAmountMenu(){
	$("#otherAmt").val("");
	$("#otherAmt").css('display','none');
	 $("#otherBtn").show();	
}
/**
 * sale ticket on UI
 * @param obj sale respnse
 */
function prepareSaleTicket(obj){
	//alert(obj.eventType.replace('[','').replace(']','').split(","));
	var i=0;
	/*$(obj.boardData[i]).each(function(){
		alert(obj.boardData.drawName);
	});*/
	$(".tktView").html("");
	var dispTkt='';
	
	dispTkt+='<div class="ticket-div"><h5>Africa Lotto</h5><div class="ticket-view"><div class="ticket-format">';
	
	//adv TOP message
	if(obj.advMsg!=null && obj.advMsg.TOP!=null){	
		var topAdvMsg=obj.advMsg.TOP[0];
		
		while(obj.advMsg.TOP[i]!=null){
			dispTkt+="<div class='dg-dg'>"+obj.advMsg.TOP[i++]+"</div>";
		}
	}
	
	//organisation logo
	
	if(obj.parentOrgName.includes("Africa Bet") > 0){
		dispTkt+='<div class="tkt-logo border-top"><div class="logo-rel"><img src="'+path+'/sleGamePlayAssets/sleContent/sports-img/africa_bet.png" alt="" width="110"></div>';
	}else{
		dispTkt+='<div class="tkt-logo border-top"><div class="logo-rel"><img src="'+path+'/sleGamePlayAssets/sleContent/sports-img/africa_loto.png" alt="" width="110"></div>';			
	}
	
	//game name
	if(obj.gameDevName!=null){
		dispTkt+='<span class="tkt-gm_nm">'+obj.gameDevName+'</span></div>';	
	}
	
	
	//purchase date and time 
	if(obj.purchaseDate!=null && obj.purchaseTime!=null){
	dispTkt+=' <div class="bg-gary-tkt"><span class="dt-gary-tkt">'+obj.purchaseDate+' '+obj.purchaseTime+'</span></div>';
	}
	//Ticket Number
	dispTkt+='<div class="tkt-dt-m">';
	if(obj.ticketNumber!=null){
		dispTkt+='<div class="tkt-rw"><span class="no-o-tkt">Ticket No. :</span><span class="no-of-tkt">'+obj.ticketNumber+'</span></div>';	
	}
	if(obj.cardNo!=''){
		dispTkt+='<div class="tkt-rw"><span class="no-o-tkt">Card No. :</span><span class="no-of-tkt">'+obj.cardNo+'</span></div>';	
	}
	
	
	//drawDate and DrawTime
	dispTkt+='<div class="tkt-rw"><span class="no-o-tkt">Draw Date</span><span class="no-o-tkt right">Draw Time</span></div>';
	if(obj.boardData!=null){
		dispTkt+='<div class="tkt-rw"><span class="no-of-tkt left">'+dateFormat(new Date(getCompataibleDate2(obj.boardData[0].drawDate)),dateFormat.masks.sikkimDate)+'</span><span class="no-of-tkt">'+getCompataibaleTime(obj.boardData[0].drawTime)+'</span></div></div>';
		
		//draw name
		dispTkt+='<div class="gmname-tkt">'+obj.boardData[0].drawName+'</div><div class="tkt-rw border-top">';
		
		//betType Header
		dispTkt+='<div class="hda-div"><div class="hda-heading"> ';
		//alert(obj.eventType.split(",").length);
		if(obj.eventType!=null){
			var eventTypeSel = obj.eventType.replace('[','').replace(']','');
			var eventTypeSelArray=eventTypeSel.split(","); 
			$(eventTypeSelArray).each(function(){
		        dispTkt+='<span>'+this+'</span>';
			});
		}
		dispTkt+='</div>';
	
		//Teams and BetType Selection
		if(obj.eventType.split(",").length == "5"){
			$(obj.boardData[0].eventData).each(function(){
				dispTkt+='<div class="hda-row"><div class="team-nm"><span>'+this.eventCodeHome+'</span><span>vs</span><span>'+this.eventCodeAway+'</span></div>';	
				var eventTypeDataString = commonEventTypeDataMade(this.selection,obj.eventType);
				dispTkt+=eventTypeDataString;
				dispTkt+='</div>';
			});
		}
		else{
			$(obj.boardData[0].eventData).each(function(){
				dispTkt+='<div class="hda-row-3"><div class="team-nm"><span>'+this.eventCodeHome+'</span><span>vs</span><span>'+this.eventCodeAway+'</span></div>';	
				var eventTypeDataString = commonEventTypeDataMade(this.selection,obj.eventType);
				dispTkt+=eventTypeDataString;
				dispTkt+='</div>';
			});
		}
		
		dispTkt+='</div></div>';	
		
		//border message
		/*dispTkt+=' <div class="text-p border-top"><p>Find Below pattern to win prize amount per game</p> </div>';*/
		
		//unitprice
		dispTkt+='<div class="tkt-dt-m "><div class="tkt-rw"><span class="no-o-tkt text-upar">Unit Price ('+currencySymbol+')</span><span class="no-of-tkt">'+obj.boardData[0].unitPrice+'</span></div>';
		
		//no of lines
		dispTkt+=' <div class="tkt-rw"><span class="no-o-tkt text-upar">No of Lines</span><span class="no-of-tkt">'+obj.boardData[0].noOfLines+'</span></div>';
		
		//betamount
		dispTkt+='<div class="tkt-rw"><span class="no-o-tkt text-upar">Bet Amount</span><span class="no-of-tkt">'+finalBetAmount+'</span></div>';
	}
	//Total amount
	if(obj.purchaseAmt!=null){
	dispTkt+='<div class="tkt-rw tlam"><span class="no-o-tkt">Total Amount ('+currencySymbol+')</span><span class="no-of-tkt">'+obj.purchaseAmt+'</span></div></div>';
	}
    //barcode image
	dispTkt+='<div class="code-img"><img src="'+path+'/sleGamePlayAssets/sleContent/sports-img/barcode.png" class="code_img"><span class="code-no">'+obj.brCd+'</span></div>';
     
	//retailer name
	if(obj.retailerName!=null){
	dispTkt+='<div class="bottom-username border-top"><span>'+obj.retailerName+'</span></div>';
	}
	
	//adv BOTTOM message
	var i=0;
	if(obj.advMsg!=null && obj.advMsg.BOTTOM!=null){	
		var topAdvMsg=obj.advMsg.BOTTOM[0];
		
		while(obj.advMsg.BOTTOM[i]!=null){
			dispTkt+="<div class='dg-dg'>"+obj.advMsg.BOTTOM[i++]+"</div>";
		}
	}
	
	dispTkt+='</div></div></div></div>';
	$(".tktView").html(dispTkt);
}

function commonEventTypeDataMade(selectionTemp,eventTypeTemp){
	selectionArray = selectionTemp.split(",");
	var eventTypeSel = eventTypeTemp.replace('[','').replace(']','').split(",");
	selectionArrayHtml='';
	var flag='';
	selectionArrayHtml+='<div class="team-res">';
	for(i=0;i<eventTypeTemp.split(",").length;i++){
		flag=true;
		for(j=0;j<selectionArray.length;j++){
			if(eventTypeSel[i].replace(/(^\s+|\s+$)/g, "")==selectionArray[j].replace(/(^\s+|\s+$)/g, "")){
				selectionArrayHtml+='<span>X</span>';
					flag=false;
			}
		}
		if(flag){
			selectionArrayHtml+='<span></span>';
		}
	}
	selectionArrayHtml+='</div>';
  return selectionArrayHtml;
}
//Reprint ticket on UI
 function  prepareReprintTicket(obj){
		
		var i=0;
		$(".tktView").html("");
		var dispTkt='';
		
		dispTkt+='<div class="ticket-div"><h5>Africa Lotto</h5><div class="ticket-view"><div class="ticket-format">';
		
		//adv TOP message
		if(obj.advMsg!=null && obj.advMsg.TOP!=null){	
			var topAdvMsg=obj.advMsg.TOP[0];
			
			while(obj.advMsg.TOP[i]!=null){
				dispTkt+="<div class='dg-dg'>"+obj.advMsg.TOP[i++]+"</div>";
			}
		}
		
		//organisation logo
		if(obj.parentOrgName.includes("Africa Bet") > 0){
			dispTkt+='<div class="tkt-logo border-top"><div class="logo-rel"><img src="'+path+'/sleGamePlayAssets/sleContent/sports-img/africa_bet.png" alt="" width="110"></div>';
		}else{
			dispTkt+='<div class="tkt-logo border-top"><div class="logo-rel"><img src="'+path+'/sleGamePlayAssets/sleContent/sports-img/africa_loto.png" alt="" width="110"></div>';			
		}
		
		
		//game name
		if(obj.gameDevName!=null){
			dispTkt+='<span class="tkt-gm_nm">'+obj.gameDevName+'</span></div>';	
		}
		
		if(obj.purchaseDate!=null && obj.purchaseTime!=null){
			dispTkt+=' <div class="bg-gary-tkt"><span class="dt-gary-tkt">'+obj.purchaseDate+' '+obj.purchaseTime+'</span></div>';
			}
		
		//Ticket Number
		dispTkt+='<div class="tkt-dt-m">';
		if(obj.ticketNumber!=null){
			dispTkt+='<div class="tkt-rw"><span class="no-o-tkt">Ticket No. :</span><span class="no-of-tkt">'+obj.ticketNumber+'</span></div>';	
		}
		
		
		//drawDate and DrawTime
		if(obj.boardData!=null){
				dispTkt+='<div class="tkt-rw"><span class="no-o-tkt">Draw Date</span><span class="no-o-tkt right">Draw Time</span></div>';
				dispTkt+='<div class="tkt-rw"><span class="no-of-tkt left">'+dateFormat(new Date(getCompataibleDate2(obj.boardData[0].drawDate)),dateFormat.masks.sikkimDate)+'</span><span class="no-of-tkt">'+getCompataibaleTime(obj.boardData[0].drawTime)+'</span></div></div>';
				
				//draw name
				dispTkt+='<div class="gmname-tkt">'+obj.boardData[0].drawName+'</div><div class="tkt-rw border-top">';
				
				//betType Header
				dispTkt+='<div class="hda-div"><div class="hda-heading"> ';
				//alert(obj.eventType.split(",").length);
				if(obj.eventType!=null){
					var eventTypeSel = obj.eventType.replace('[','').replace(']','');
					var eventTypeSelArray=eventTypeSel.split(","); 
					$(eventTypeSelArray).each(function(){
				        dispTkt+='<span>'+this+'</span>';
					});
				}
				dispTkt+='</div>';
		
				//Teams and BetType Selection
				if(obj.eventType.split(",").length == "5"){
					$(obj.boardData[0].eventData).each(function(){
						dispTkt+='<div class="hda-row"><div class="team-nm"><span>'+this.eventCodeHome+'</span><span>vs</span><span>'+this.eventCodeAway+'</span></div>';	
						var eventTypeDataString = commonEventTypeDataMade(this.selection,obj.eventType);
						dispTkt+=eventTypeDataString;
						dispTkt+='</div>';
					});
				}
				else{
					$(obj.boardData[0].eventData).each(function(){
						dispTkt+='<div class="hda-row-3"><div class="team-nm"><span>'+this.eventCodeHome+'</span><span>vs</span><span>'+this.eventCodeAway+'</span></div>';	
						var eventTypeDataString = commonEventTypeDataMade(this.selection,obj.eventType);
						dispTkt+=eventTypeDataString;
						dispTkt+='</div>';
					});
				}
				
				dispTkt+='</div></div>';	
				
				
				//border message
				/*dispTkt+=' <div class="text-p border-top"><p>Find Below pattern to win prize amount per game</p> </div>';*/
				
				//unitprice
				dispTkt+='<div class="tkt-dt-m "><div class="tkt-rw"><span class="no-o-tkt text-upar">Unit Price ('+currencySymbol+')</span><span class="no-of-tkt">'+obj.boardData[0].unitPrice+'</span></div>';
				
				//no of lines
				dispTkt+=' <div class="tkt-rw"><span class="no-o-tkt text-upar">No of Lines</span><span class="no-of-tkt">'+obj.boardData[0].noOfLines+'</span></div>';
				}
		//betamount
		if(obj.boardData[0].unitPrice!=null){
		dispTkt+='<div class="tkt-rw"><span class="no-o-tkt text-upar">Bet Amount</span><span class="no-of-tkt">'+finalBetAmount+'</span></div>';
		}
		//Total amount
		if(obj.purchaseAmt!=null){
		dispTkt+='<div class="tkt-rw tlam"><span class="no-o-tkt">Total Amount ('+currencySymbol+')</span><span class="no-of-tkt">'+obj.purchaseAmt+'</span></div></div>';
		}
	    //barcode image
		dispTkt+='<div class="code-img"><img src="'+path+'/sleGamePlayAssets/sleContent/sports-img/barcode.png" class="code_img"><span class="code-no">'+obj.brCd+'</span></div>';
	     
		//retailer name
		if(obj.retailerName!=null){
		dispTkt+='<div class="bottom-username border-top"><span>'+obj.retailerName+'</span></div>';
		}
		
		//adv BOTTOM message
		var i=0;
		if(obj.advMsg!=null && obj.advMsg.BOTTOM!=null){	
			var topAdvMsg=obj.advMsg.BOTTOM[0];
			
			while(obj.advMsg.BOTTOM[i]!=null){
				dispTkt+="<div class='dg-dg'>"+obj.advMsg.BOTTOM[i++]+"</div>";
			}
		}
		
		dispTkt+='</div></div></div></div>';
		$(".tktView").html(dispTkt);
 }
 
 function  prepareCancelTicket(obj){
		var i=0;
		/*$(obj.boardData[i]).each(function(){
			alert(obj.boardData.drawName);
		});*/
		$(".tktView").html("");
		var dispTkt='';
		
		dispTkt+='<div class="ticket-div"><h5>Africa Lotto</h5><div class="ticket-view"><div class="ticket-format">';
		
		//adv TOP message
		if(obj.advMsg!=null && obj.advMsg.TOP!=null){	
			var topAdvMsg=obj.advMsg.TOP[0];
			
			while(obj.advMsg.TOP[i]!=null){
				dispTkt+="<div class='dg-dg'>"+obj.advMsg.TOP[i++]+"</div>";
			}
		}
		
		//organisation logo
		if(obj.parentOrgName.includes("Africa Bet") > 0){
			dispTkt+='<div class="tkt-logo border-top"><div class="logo-rel"><img src="'+path+'/sleGamePlayAssets/sleContent/sports-img/africa_bet.png" alt="" width="110"></div>';
		}else{
			dispTkt+='<div class="tkt-logo border-top"><div class="logo-rel"><img src="'+path+'/sleGamePlayAssets/sleContent/sports-img/africa_loto.png" alt="" width="110"></div>';			
		}
		
		//game name
		if(obj.gameDispName!=null){
		dispTkt+='<span class="tkt-gm_nm">'+obj.gameDispName+'</span></div>';
		}
		
		//purchase date and time 
		if(obj.cancelTime!=null){
		dispTkt+=' <div class="bg-gary-tkt"><span class="dt-gary-tkt">'+dateFormat(new Date(getCompataibleDate2(obj.cancelTime.split(" ")[0])),dateFormat.masks.sikkimDate)+' '+getCompataibaleTime(obj.cancelTime.split(" ")[1])+'</span></div>';
		}
		//Ticket Number
		dispTkt+='<div class="tkt-dt-m">';
		if(obj.ticketNumber!=null){
		dispTkt+='<div class="tkt-rw"><span class="no-o-tkt">Ticket No. :</span><span class="no-of-tkt">'+obj.ticketNumber+'</span></div></div>';
		}
		//cancelled message
		dispTkt+=' <div class="text-p border-top"><p><font color="red">Ticket has been Cancelled !!!</font></p> </div>';
		
		//refunded amount
		if(obj.refundAmount!=null){
		dispTkt+='<div class="tkt-rw tlam"><span class="no-o-tkt">Total Amount ('+currencySymbol+')</span><span class="no-of-tkt">'+obj.refundAmount+'</span></div></div>';
		}	
		
		//adv BOTTOM message
		var i=0;
		if(obj.advMsg!=null && obj.advMsg.BOTTOM!=null){	
			var topAdvMsg=obj.advMsg.BOTTOM[0];
			
			while(obj.advMsg.BOTTOM[i]!=null){
				dispTkt+="<div class='dg-dg'>"+obj.advMsg.BOTTOM[i++]+"</div>";
			}
		}
		dispTkt+='</div></div></div></div>';
		$(".tktView").html(dispTkt);
 }

 /**
  * print winning ticket 
  */
 function preparePwtTicket(obj){
	 var i=0;
		$(".tktView").html("");
		var dispTkt='';
		
		dispTkt+='<div class="ticket-div"><h5>Africa Lotto</h5><div class="ticket-format">';
		
		//adv TOP message
		if(obj.advMsg!=null && obj.advMsg.TOP!=null){	
			var topAdvMsg=obj.advMsg.TOP[0];
			
			while(obj.advMsg.TOP[i]!=null){
				dispTkt+="<div class='dg-dg'>"+obj.advMsg.TOP[i++]+"</div>";
			}
		}
		
		//organisation logo
		if(obj.parentOrgName.includes("Africa Bet") > 0){
			dispTkt+='<div class="tkt-logo border-top"><div class="logo-rel"><img src="'+path+'/sleGamePlayAssets/sleContent/sports-img/africa_bet.png" alt="" width="110"></div>';
		}else{
			dispTkt+='<div class="tkt-logo border-top"><div class="logo-rel"><img src="'+path+'/sleGamePlayAssets/sleContent/sports-img/africa_loto.png" alt="" width="110"></div>';			
		}
		
		//game name
		if(obj.gameDevName!=null){
		dispTkt+='<span class="tkt-gm_nm">'+obj.gameDevName+'</span></div>';
		}
		//purchase date and time
		if(obj.pwtDateTime!=null){
		dispTkt+=' <div class="bg-gary-tkt"><span class="dt-gary-tkt">'+dateFormat(new Date(getCompataibleDate2(obj.pwtDateTime.split(" ")[0])),dateFormat.masks.sikkimDate)+' '+getCompataibaleTime(obj.pwtDateTime.split(" ")[1])+'</span></div>';
		}
		//Ticket Number
		dispTkt+='<div class="tkt-dt-m">';
		if(obj.ticketNumber!=null){
		dispTkt+='<div class="tkt-rw"><span class="no-o-tkt">Ticket No. :</span><span class="no-of-tkt">'+obj.ticketNumber+'</span></div>';
		}
		//drawDate and DrawTime
		dispTkt+='<div class="tkt-rw"><span class="no-o-tkt">Draw Date</span><span class="no-o-tkt right">Draw Time</span></div>';
		if(obj.drawDateTime!=null){
		dispTkt+='<div class="tkt-rw"><span class="no-of-tkt left">'+dateFormat(new Date(getCompataibleDate2(obj.drawDateTime.split(" ")[0])),dateFormat.masks.sikkimDate)+'</span><span class="no-of-tkt">'+getCompataibaleTime(obj.drawDateTime.split(" ")[1])+'</span></div></div>';
		}
		//draw name
		if(obj.drawName!=null){
		dispTkt+='<div class="gmname-tkt">'+obj.drawName+'</div><div class="tkt-rw border-top">'; 
		}
		//winning amount
		if(obj.totalWinAmt!=null){
		dispTkt+='<div class="tkt-rw tlam"><span class="no-o-tkt">Winning Amt ('+currencySymbol+')</span><span class="no-of-tkt">'+obj.totalWinAmt+'</span></div></div>';
		}
		//total pay amount
		if(obj.totalPay!=null){
		dispTkt+='<div class="tkt-rw tlam"><span class="no-o-tkt">Total Pay ('+currencySymbol+')</span><span class="no-of-tkt">'+obj.totalPay+'</span></div></div>';
		}
		
		//adv BOTTOM message
		var i=0;
		if(obj.advMsg!=null && obj.advMsg.BOTTOM!=null){	
			var topAdvMsg=obj.advMsg.BOTTOM[0];
			
			while(obj.advMsg.BOTTOM[i]!=null){
				dispTkt+="<div class='dg-dg'>"+obj.advMsg.BOTTOM[i++]+"</div>";
			}
		}
		
        dispTkt+='</div></div></div>';
		$(".tktView").html(dispTkt);
 }
 
 function setAppDataSLE(data) {
		isValid = false;
		resetAllGames();
		var connSuccess = false;
		var connection = new WebSocket('ws://localhost:8082');
		connection.onopen = function () {
	   		connection.send(data);
	   		if(connection.readyState == 1){
	   			connSuccess = true;
	   		}
		};
		connection.onerror = function(){
			if("BUY" == curTrx){
				var requestParam = path+'/com/skilrock/sle/web/merchantUser/playMgmt/action/cancelTicket.action';
				var cancelTicketData =_ajaxCallJson(requestParam,"autoCancel=true&cancelType=CANCEL_PRINTER",'');
				//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				updateBalance();
				alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not." +
					"\n\nLast ticket has been cancelled!!");
				$("#buy-btn").prop('disabled',true);
			} else if("Reprint" == curTrx){
				alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not.");
			} else if("Cancel" == curTrx){
				//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				updateBalance();
				alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not."+
					"\n\nLast ticket has been cancelled!");
			} else if("PWT" == curTrx){
				//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				updateBalance();
				alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not." +
						"\n\nTicket has been claimed!!");
				//preparePwtTicket(JSON.parse(printData));
			} else if("TestPrint" == curTrx){
				alert("Cannot communicate to printing application.\nPlease check your NodeJS server is running and try again.\nTo start NodeJs server either restart your system or\n follow the steps below:\n" +
						"1.) Open My Computer. \n2.) Goto C:/Program Files (x86)/skilrock/dgpcpos (For 64-bit system) and\n" +
						"     C:/Program Files (x86)/skilrock/dgpcpos (For 32-bit system).\n3.) Double click on runNodeInvisible to start NodeJs server.");
			}
			else if("MATCH_LIST" == curTrx){
				alert("Cannot communicate to printing application.\nPlease check your NodeJS server is running and try again.\nTo start NodeJs server either restart your system or\n follow the steps below:\n" +
						"1.) Open My Computer. \n2.) Goto C:/Program Files (x86)/skilrock/dgpcpos (For 64-bit system) and\n" +
						"     C:/Program Files (x86)/skilrock/dgpcpos (For 32-bit system).\n3.) Double click on runNodeInvisible to start NodeJs server.");
			}
		};
		connection.onmessage = function (evt) {
			//console.log(evt.data);
			var printStatus = "";
	    	var dataReceived = $.parseJSON(evt.data);
	    	if("APP_NOT_FOUND" == dataReceived){
	    		printStatus = dataReceived;
	    	}else{
	    		printStatus = dataReceived.utf8Data;
	    	}
	        connection.close();
		    if(connSuccess){
		    	checkAndShowTicketSLE(printStatus);
			}
	    };
	}
 
 function checkAndShowTicketSLE(printStatus){
		if("SUCCESS" == printStatus){
			$("#buy-btn").prop('disabled',true);
			//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			updateBalance();
			reprintCountChild = 0;
			if("BUY" == curTrx){
				prepareSaleTicket(JSON.parse(printData));
			}else if("Reprint" == curTrx){
				prepareReprintTicket(JSON.parse(printData),'Reprint');
			}else if("Cancel" == curTrx){
				prepareCancelTicket(JSON.parse(printData));
			}else if("PWT" == curTrx){
				preparePwtTicket(JSON.parse(printData));
			}else if("TestPrint" == curTrx){
				$("#success").html("Printing application is working fine!");
				$("#success-popup").show(300);		
				$('#success-popup').delay(1500).hide(300);
			}
			else if("MATCH_LIST" == curTrx){
				$("#success").html("Match list is start printing");
				$("#success-popup").show(300);		
				$('#success-popup').delay(1500).hide(300);
			}
			return true;
		} else if("FAIL" == printStatus){
			if("BUY" == curTrx){
				if(reprintCountChild < 2){
					var status = confirm("Printer not connected!! \nCheck your printer and press OK to reprint or press Cancel to cancel the ticket!!");
					if(!status){
						var requestParam = path+'/com/skilrock/sle/web/merchantUser/playMgmt/action/cancelTicket.action';
						var cancelTicketData =_ajaxCallJson(requestParam,"autoCancel=true&cancelType=CANCEL_PRINTER",'');
						//var _resp = winAjaxReq("cancelTicketAuto.action?json="+data);
						//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
						updateBalance();
						$("#buy-btn").prop('disabled',true);
						reprintCountChild = 0;
						return false;
					}else{
						setAppletData(printData);
						reprintCountChild++;
					}
				} else{
					var requestParam = path+'/com/skilrock/sle/web/merchantUser/playMgmt/action/cancelTicket.action';
					var cancelTicketData =_ajaxCallJson(requestParam,"autoCancel=true&cancelType=CANCEL_PRINTER",'');
				//	parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
					updateBalance();
					$("#buy-btn").prop('disabled',true);
					reprintCountChild = 0;
					alert("Last ticket has been cancelled!!");
					return false;
				}
			}else if ("Reprint" == curTrx) {
				if (reprintCount < 2) {
					alert("Printer not connected. \nCheck your printer and try again!!");
					return true;
				} else {
					return false;
				}
			}else if("Cancel" == curTrx){
				updateBalance();
				alert("Printer not connected.\nLast ticket has been cancelled!!");
				//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			}else if("TestPrint" == curTrx){
				alert("Printer not connected!!");
			}
			else if("MATCH_LIST" == curTrx){
				alert("Printer not connected!!");
			}
		} else if("APP_NOT_FOUND" == printStatus){
			if("BUY" == curTrx){
				var requestParam = path+'/com/skilrock/sle/web/merchantUser/playMgmt/action/cancelTicket.action';
				var cancelTicketData =_ajaxCallJson(requestParam,"autoCancel=true&cancelType=CANCEL_PRINTER",'');
				//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				updateBalance();
				alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not." +
					"\n\nLast ticket has been cancelled!!");
				$("#buy-btn").prop('disabled',true);
			} else if("Reprint" == curTrx){
				alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not.");
			} else if("Cancel" == curTrx){
				//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				updateBalance();
				alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not."+
					"\n\nLast ticket has been cancelled!");
			} else if("PWT" == curTrx){
				//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				updateBalance();
				alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not." +
					"\n\nTicket has been claimed!!");
				preparePwtTicket(JSON.parse(printData));
			}else if("TestPrint" == curTrx){
				alert("Printing Application not running!!");
			}
			else if("MATCH_LIST" == curTrx){
				alert("Printing Application not running!!");
			}
		} else if("INVALIDINPUT" == printStatus){
			if("BUY" == curTrx){
				var requestParam = path+'/com/skilrock/sle/web/merchantUser/playMgmt/action/cancelTicket.action';
				var cancelTicketData =_ajaxCallJson(requestParam,"autoCancel=true&cancelType=CANCEL_PRINTER",'');
				//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				updateBalance();
				alert("Ticket cannot be printed due to \nInternal System Error!!\nLast ticket has been cancelled!!");
				$("#buy-btn").prop('disabled',true);
			} else if("Reprint" == curTrx){
				alert("Ticket cannot be printed due to \nInternal System Error!!");
			} else if("Cancel" == curTrx){
				//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				updateBalance();
				alert("Ticket cannot be printed due to \nInternal System Error!!");
			} else if("PWT" == curTrx){
				//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				updateBalance();
				alert("Ticket cannot be printed due to \nInternal System Error!!\nTicket has been claimed!!");
				preparePwtTicket(JSON.parse(printData));
			}
			else if("TestPrint" == curTrx){
				//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				alert("Ticket cannot be printed due to \nInternal System Error!!\nTicket has been claimed!!");
				preparePwtTicket(JSON.parse(printData));
			}
			else if("MATCH_LIST" == curTrx){
				//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				alert("Ticket cannot be printed due to \nInternal System Error!!\nTicket has been claimed!!");
				preparePwtTicket(JSON.parse(printData));
			}
		}
	}
 
 /**
  * test printer for check is ready or not
  */
 $("#testPrint").on("click",function(){
		curTrx = "TestPrint";
		var testPrint = {
			 "serviceName":"SLE",
             "mode": curTrx
		};
		printData = JSON.stringify(testPrint);
		if("YES" == printUsingApplet){
			setAppletData(printData);
    	}else{
    		setAppDataSLE(printData);
    	}
	});
 
//if date comes in yyyy-MM-dd format
 function getCompataibleDate2(date){
 	var dArr = date.split("-");
 	var finalDate = dArr[1]+"/"+dArr[2]+"/"+dArr[0];
 	return finalDate;
 }
 
//if time comes in HH:MM:SS format
 function getCompataibaleTime(time){
 	var tArr=time.split(":");
 	var finalTime= tArr[0]+":"+tArr[1];
 	return finalTime;
 }
 
 
 function setAppletData(buyData) {
//	var data = "{'mode':'Buy','gameDevName':'MiniRoulette','orgName':'sikkim', 'brCd':195634067469470030, 'advMsg':{'TOP':['This is adv','This is adv2'],'BOTTOM':['Bottom adv1','Bottom adv2']},'retailerName':'ret','brCd':'416736243110680010','ticketNumber':'416736243110680010','gameName':'Mini Roulette','purchaseDate':'Aug 30, 2016','purchaseTime':'16:48','purchaseAmt':600,'betTypeData':[{'isQp':false,'betName':'redNumbers','pickedNumbers':'01, 03, 05, 07, 09, 11','numberPicked':'6','unitPrice':20,'noOfLines':6,'betAmtMul':1,'panelPrice':120},{'isQp':false,'betName':'blackNumbers','pickedNumbers':'02, 04, 06, 08, 10, 12','numberPicked':'6','unitPrice':20,'noOfLines':6,'betAmtMul':1,'panelPrice':120},{'isQp':false,'betName':'firstRow','pickedNumbers':'03, 06, 09, 12','numberPicked':'4','unitPrice':20,'noOfLines':4,'betAmtMul':1,'panelPrice':120},{'isQp':false,'betName':'secondRow','pickedNumbers':'02, 05, 08, 11','numberPicked':'4','unitPrice':20,'noOfLines':4,'betAmtMul':1,'panelPrice':120},{'isQp':false,'betName':'thirdRow','pickedNumbers':'01, 04, 07, 10','numberPicked':'4','unitPrice':20,'noOfLines':4,'betAmtMul':1,'panelPrice':120}],'currencySymbol':'INR','drawData':[{'drawId':'2213','drawDate':'30-08-2016','drawTime':'16:55:00'},{'drawId':'2214','drawDate':'30-08-2016','drawTime':'17:15:00'},{'drawId':'2215','drawDate':'30-08-2016','drawTime':'17:35:00'},{'drawId':'2216','drawDate':'30-08-2016','drawTime':'17:55:00'},{'drawId':'2217','drawDate':'30-08-2016','drawTime':'18:15:00'},{'drawId':'2218','drawDate':'30-08-2016','drawTime':'18:35:00'}]}";
	$("#sleParentApplet").css("display", "none");
	$("#sleRegDiv").innerHTML = "";
	$("#sleRegButton").innerHTML = "";
	isPrintFail = false;
	/*var data = JSON.parse(buyData);*/
	//data = JSON.stringify(data);
	$("#sleParentApplet").css("display", "block");
	document.applets[0].showStatus(buyData);
	//alert("Applet Calling");
	document.applets[0].repaint();
}

 

var isPrintFail = false;
function checkAndShowTicketForApplet(tktNum, totAmt, printStatus) {
	//alert("Status:"+printStatus+"|Ticket Number :"+tktNum);
	if("SUCCESS" == printStatus){
		$("#buy").prop('disabled',true);
//		parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
		reprintCountChild = 0;
		if("TestPrint" == curTrx){
			$("#success").html("Printing application is working fine!");
			$("#success-popup").css('display','block');		
			$('#success-popup').delay(1500).fadeOut('slow');
		}
		
	} else if("FAIL" == printStatus){
		if("Buy" == curTrx){
			if(reprintCountChild < 2){
				var status = confirm("Printer not connected!! \nCheck your printer and press OK to reprint or press Cancel to cancel the ticket!!");
				if(!status){
					var data = {"ticketNumber":tktNum, "autoCancel":true};
					data = JSON.stringify(data);
					var _resp = winAjaxReq("cancelTicketAuto.action?json="+data);
				//	parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
					$("#buy").prop('disabled',true);
					$("#sleParentApplet").css("display", "none");
					$(".tktView").css("display","block");
					reprintCountChild = 0;
					return false;
				}else{
					setAppletData(printData);
					reprintCountChild++;
				}
			} else{
				var data = {"ticketNumber":tktNum, "autoCancel":true};
				data = JSON.stringify(data);
				var _resp = winAjaxReq("cancelTicketAuto.action?json="+data);
				//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				$("#buy").prop('disabled',true);
				$("#sleParentApplet").css("display", "none");
				$(".tktView").css("display","block");
				reprintCountChild = 0;
				alert("Last ticket has been cancelled!!");
			}
		}else if ("Reprint" == curTrx) {
			if (reprintCount < 2) {
				$("#sleParentApplet").css("display", "none");
				$(".tktView").css("display","block");
				alert("Printer not connected. \nCheck your printer and try again!!");
			} else {
			}
		}else if("Cancel" == curTrx){
			$("#sleParentApplet").css("display", "none");
			$(".tktView").css("display","block");
			alert("Printer not connected.\nLast ticket has been cancelled!!");
			//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
		}else if("TestPrint" == curTrx){
			$("#sleParentApplet").css("display", "none");
			$(".tktView").css("display","block");
			alert("Printer not connected!!");
		}
	} else if("APP_NOT_FOUND" == printStatus){
		if("Buy" == curTrx){
			var data = {"ticketNumber":tktNum, "autoCancel":true};
			data = JSON.stringify(data);
			var _resp = winAjaxReq("cancelTicketAuto.action?json="+data);
			//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not." +
				"\n\nLast ticket has been cancelled!!");
			$("#buy").prop('disabled',true);
			$("#sleParentApplet").css("display", "none");
			$(".tktView").css("display","block");
		} else if("Reprint" == curTrx){
			$("#sleParentApplet").css("display", "none");
			$(".tktView").css("display","block");
			alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not.");
		} else if("Cancel" == curTrx){
			$("#sleParentApplet").css("display", "none");
			$(".tktView").css("display","block");
			//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not."+
				"\n\nLast ticket has been cancelled!");
		} else if("PWT" == curTrx){
			$("#sleParentApplet").css("display", "none");
			$(".tktView").css("display","block");
			//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not." +
				"\n\nTicket has been claimed!!");
		}else if("TestPrint" == curTrx){
			$("#sleParentApplet").css("display", "none");
			$(".tktView").css("display","block");
			alert("Printing Application not running!!");
		}
	} else if("INVALIDINPUT" == printStatus){
		if("Buy" == curTrx){
			var data = {"ticketNumber":tktNum, "autoCancel":true};
			data = JSON.stringify(data);
			var _resp = winAjaxReq("cancelTicketAuto.action?json="+data);
			//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Ticket cannot be printed due to \nInternal System Error!!\nLast ticket has been cancelled!!");
			$("#buy").prop('disabled',true);
			$("#sleParentApplet").css("display", "none");
			$(".tktView").css("display","block");
		} else if("Reprint" == curTrx){
			$("#sleParentApplet").css("display", "none");
			$(".tktView").css("display","block");
			alert("Ticket cannot be printed due to \nInternal System Error!!");
		} else if("Cancel" == curTrx){
			$("#sleParentApplet").css("display", "none");
			$(".tktView").css("display","block");
			//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Ticket cannot be printed due to \nInternal System Error!!");
		} else if("PWT" == curTrx){
			$("#sleParentApplet").css("display", "none");
			$(".tktView").css("display","block");
			//parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Ticket cannot be printed due to \nInternal System Error!!\nTicket has been claimed!!");
		}
	}
}


