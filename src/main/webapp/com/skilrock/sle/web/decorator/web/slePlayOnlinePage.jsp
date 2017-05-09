<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.opensymphony.xwork2.inject.Context"%>
<%@page import="com.skilrock.sle.common.Util"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.sle.common.javaBeans.UserInfoBean;"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Draw UI</title>
<link href="https://fonts.googleapis.com/css?family=Roboto:400,300,500,700" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Open+Sans:400,300	,600,700" rel="stylesheet" type="text/css">
<link href="https://netdna.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.css" rel="stylesheet">
<link rel="stylesheet" href="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/css/draw-ui-css.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/css/sports-lottery.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/css/ticket.css">
<script src="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/js/jquery-1.11.0.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.countdown.js"></script>
<!--[if lt IE 9]>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/css/posie8.css" media="screen">
<script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.min.js"></script>
<script src="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/js/posie8.js"></script>
<![endif]-->
</head>
<body>
	<input type="hidden" id="gameid" value="">
	<input type="hidden" id="currentServerTime" value="<%=Util.getCurrentTimeString() %>">
	<input type="hidden" id="serverDate" value="<%=Util.getCurrentTimeString()%>">
	<input type="hidden" id="tktMaxAmt" value="">
	<input type="hidden" id="gameTypeId" value="">
	<input type="hidden" id="drawId" value="">
	<input type="hidden" id="eventSelectionType" value="">
	<input type="hidden" id="gameTypeMaxBetAmtMultiple" value="">
	<input type="hidden" id="gameTypeUnitPrice" value="">
	<input type="hidden" id="merCode" value="<%=session.getAttribute("MER_CODE")%>">
	<input type="hidden" id="saleMerCode" value="">
	<input type="hidden" id="sessionId" value="<%=((UserInfoBean)session.getAttribute("USER_INFO")).getUserSessionId()%>">
	<input type="hidden" id="userName" value="<%=((UserInfoBean)session.getAttribute("USER_INFO")).getUserName()%>">
	<input type="hidden" id="parentOrgName" value="<%=((UserInfoBean)session.getAttribute("USER_INFO")).getParentOrgName()%>">

<div class="beforePageLoad"></div>
<!--left side-->
<div class="left-col left-fix">
          <div class="left-draw-ui">
                    <h5>Game Play</h5>
                    <div class="ui-left-menu">
                       
                    </div>
                 <!--Banner-->
                    <div class="gm-banner">
                      <img src="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/sports-UI/game-banner.jpg" width="155" alt="">
                    </div>
                <!--Banner-->
            </div>
        </div>
<!--left side-->
<!--middle side-->
<div class="middle-col center-fix">
	<div class="draw-no-top">
    	<div class="draw">
        <div class="draw-name"><h5>Draw</h5></div>
        <div class="draw-right">
        	<div class="text-center">
            	<span class="draw-nm-sl-sl">Select Draw</span>
            	 <button class="select-draw"> Jul 22, 2016 11:50:00</button>
            </div> 
         </div>
        </div>
        <div class="results">
        	<div class="result-name"><h5>Results</h5></div>
            <div class="results-number" style="display:none">
            <div class="win-nmd">
                <span class="month-span">Jul 22</span>
                <span class="time-span">09:20</span>
             </div>
             <div class="right-result-sl">
             	<table class="table-sl">
                	<tbody>
                    	<tr>
                        	<td><span>1</span>Home</td>
                            <td><span>2</span>Draw</td>
                            <td><span>3</span>Home</td>
                            <td><span>4</span>Away</td>
                            <td><span>5</span>Away</td>
                            <td><span>6</span>Home</td>
                             <td><span>7</span>Draw</td>
                        </tr>
                        <tr>
                        	<td><span>8</span>Away</td>
                            <td><span>9</span>Home</td>
                            <td><span>10</span>Draw</td>
                            <td><span>11</span>Home</td>
                            <td><span>12</span>Draw</td>
                            <td><span>13</span>Away</td>
                             <td></td>
                        </tr>
                    </tbody>
                 </table>
             </div>
            	
          </div>
        </div>
    </div>
    <!--sport game soccer 4 html-->
    <div class="sl-div-s">
      <!-- soccer 13 -->
      <div class="sportsLottWrapOuter" >
                <ul class="sportsLottWrapInner col-count-3" id="sportsLottWrapInnerSoccerOther">
                </ul>
                <ul class="sportsLottWrapInner col-count-2" id="sportsLottWrapInnerSoccer4">
                </ul>
                <div id="sportsLottWrapOuter"></div>
      </div>
      <!-- soccer 4 -->
    </div>
    <!--sport game html-->
    
    
    
</div>
<!--middle side-->
<!--right side-->
<div class="right-col right-fix">
	<div class="win-set-button">
                    <div class="winning-button" id="pwt">
                        <button>
                        <div class="figure"><img src="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/sports-UI/winning-claim.png" width="35"></div>
                        <span>Winning Claim</span>
                        </button>
                    </div>
                    <div class="setting-button">
                        <button id="setting-btn">
                        <div class="figure"><img src="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/sports-UI/setting-icon.png" width="35"></div>
                        <span>Setting</span>
                        </button>
                    </div>
                    <div class="down-menu" id="open-list-btn" style="display: none;">
                            <button id="reprint">
                            <span><img src="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/sports-UI/reprint-icon.png" class="img-responsive">Reprint</span>
                            </button>
                            <button id="result">
                            <span><img src="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/sports-UI/result-icon.png" class="img-responsive">Results</span>
                            </button >
                            <button id="cancel">
                            <span><img src="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/sports-UI/cancel-icon.png" class="img-responsive">Cancel</span>
                            </button >
                            <button id="matchList">
                            <span><img src="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/sports-UI/reports-icon.png" class="img-responsive">Match List</span>
                            </button>
                             <button id="testPrint">
                            <span><img src="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/sports-UI/test-print-icon.png" class="img-responsive">Test Print</span>
                            </button>
                        </div>
             </div>
	<div class="prize ">
    	<h5 class="tktViewName">Prize Scheme</h5>
        <div class="list-prize tktView">
        	
        </div>
         <div class="panel-body">
			<div class="list-prize panel-body ticket-format"  id="sleParentApplet" style=" display:none; overflow-y: scroll; height: 120px; z-index:1; background-color: white;">
		        <div id="sleRegDiv"></div>
			    <applet style="z-index: 1" width="220" height="600" mayscript="" archive="applets/gson-2.2.2.jar" name="TicketApp" jnlp_href="applets/App.jnlp" codebase="<%=request.getContextPath()%>" code="SLEAppletTicketEngine"></applet>
			    <div id="sleRegButton"></div>
		   </div>
		   </div>
    </div>
    <div class="purchase-detail">
    	<h5>Purchase Details</h5>
        <div class="draw-dt">
        	<div class="draw-list">
            	<div class="sport-dr-letf">Draw :</div>
                <div class="sports-no-dr">
              
                </div>
            </div>
            <div class="sl-no-ol">
            	<div class="sport-dr-letf">Number of line :</div>
                <div class="sports-nof">
                	<span id="noOfLines">0</span>
                </div>
            </div>
            <div class="sports-bet-amt">
              <span>Bet Amount </span>
               <div class="sl-select-amt">
               </div>
            </div>
		</div>
        <input type="hidden"  id="betAmount" >
        <div class="buy-now">
             <div class="toottip-div" id="buyNowMessage" style="block">
        		<div id="buyMessage"></div>
        		<span></span>
        	 </div>
        	 <button id="buy-btn">
            	<span class="rupees-icon"><i class="fa fa-usd" aria-hidden="true"></i><span  id="tktPrice"></span></span>
            	<span class="buy-span">
                	<span >BUY</span>NOW
                </span>
             </button>
       </div>
    </div>
</div>

<%-- <table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td>
			<div id="sleParentApplet" style="display: block; overflow: auto; height: 300px; background-color: white;">
					<%
					String barcodeType = (String)application.getAttribute("BARCODE_TYPE");
						if (barcodeType.equals("applet")) {
					%>
					<script>
						var codebase = "";
						function stopRender(){
								for (var j=0;j<document.applets.length;j++){
									if(typeof document.applets[j].isActive!="undefined"){
										codebase = (document.applets[j].codeBase).substring('<%=request.getContextPath()%>'.length);
										//document.execCommand('Stop') ;
										//alert(codebase);
										gamesData();fillGame(activeGames[0],'manualCall');							
									 winAjaxReq("jreVersion.action?jreVersion="+ codebase);
									}
								}
						}	
                  </script>
					<%
						StringBuffer codebaseBuffer = new StringBuffer();
						codebaseBuffer.append(!request.isSecure() ? "http://" : "https://");
						codebaseBuffer.append(request.getServerName());
						if (request.getServerPort() != (!request.isSecure() ? 80 : 443)) {
							codebaseBuffer.append(':');
							codebaseBuffer.append(request.getServerPort());
						}
						codebaseBuffer.append(request.getContextPath());
						codebaseBuffer.append('/');
					%>
					<div id="RegDiv"></div>
					
					<%	if("SIGNED".equalsIgnoreCase((String)application.getAttribute("APPLET_SIGNED"))){ %>
							<applet code="TicketApplet" codebase="<%=codebaseBuffer.toString()%>" jnlp_href="applets/App.jnlp" width="200" height="200"	name="TicketApp" mayscript>
								<param name="data" value="108172000002746000" />
									<div style="font-size:12px; height:300px; line-height:center;">
										<table>
											<tr>
												<td height="300px;" align="center">
													No Java Runtime Environment v 1.5.2 found!!<br/>
									        		<a style="color:red" href="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/jre-1_5_0_12-windows-i586-p.exe">Click to install</a>
									        	</td>
									        </tr>
								        </table>
								       </div>
							</applet>
					<%	}else if("UNSIGNED".equalsIgnoreCase((String)application.getAttribute("APPLET_SIGNED"))){%>
						<applet codebase="<%=request.getContextPath()%>/java1.5/" code="SLEAppletTicketEngine.class"  width="200" height="500" name="SLEAppletTicketEngine" mayscript>
							<param name="data" value="108172000002746000" />
						</applet>
					<% }%>	
		
					
					<div id="sleRegButton"></div>
					<%
						} else {
					%>

							<script>gamesData();fillGame(activeGames[1],'manualCall');</script>
					<%
						}
					%>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<div id="pwtResult"></div>
		</td>
	</tr>
</table> --%>

<%-- 
<div class="panel-body">
	<div class="panel-body"  id="sleParentApplet" style="display: block; overflow: auto; height: 550px; background-color: white;">
        <div id="sleRegDiv"></div>
	    <applet width="220" height="450" mayscript="" archive="applets/gson-2.2.2.jar" name="TicketApp" jnlp_href="applets/App.jnlp" codebase="<%=request.getContextPath()%>" code="SLEAppletTicketEngine"></applet>
	    <div id="sleRegButton"></div>
</div> --%>
<!--right side-->

		<div class="full-screen" id="advDrawWindow" style="display:none">
         <div class="popup">
            <h5>Select Draw <button id="closeAdvDrawWindow"></button></h5>
            <div class="gm-tab-result-list ">
               <div class="col-span-12 showAdvDraw">
               </div>
               <div class="button-tab">
                  <div class="col-span-12 text-center">
                     <div class="button-cancel">
                        <button id="cancelAdvDrawWindow">Cancel</button>
                     </div>
                     <div class="button-submit">
                        <button id="submitAdvDrawWindow">Submit</button>
                     </div>
                  </div>
               </div>
            </div>
         </div>
        </div>
		<div id="hiddenMatchListPanelData" style="display:none"></div>
		<div class="full-screen" id="matchListWindow" style="display:none">
		    <div class="popup">
		    	<h5><span id="popupName"></span><button id="closeMatchList"></button></h5>
		        <div class="gm-result">
		        	<div class="game-name-tab">
		            
		            <!-- game menu -->	
		                
		            </div>
		             <div class="result-soccer-gm" style="display:none" id="resultListData">
		            	 <ul class="sportsLottWrapInnerResult col-count-3 soccer10"  id="resultListDataPanel">
		                   <!-- result data -->
		                </ul>
		                <div class="sportsLottWrapInnerResult col-count-3 soccer10" id="resultListDataPanelWise"></div>
		            </div>
		          <div class="match-list-area" id="matchListWindowData" style="display:none" >
		            	<div class="soccer13-game">
		                	<ul class="soccer13-col" id="matchListPanelData">
		                       <!-- match list data drop down  -->
		                    </ul>
		                    <ul class="soccer13-col" id="matchListPanelDatawise" >
		                       <!-- match list data  -->
		                    </ul>
		                  
		                </div>
		            </div>
		
		        </div>
		    </div>
		</div>
 		<!-- Modal Starts -->
	  <div class="full-screen" id="pwt-win" style="display: none">
		    <div class="popup">
		    <h5>Please enter ticket number <button id="pwtClose"></button></h5>
					<div class="modal-body">
						<div class="tkt-no-ct">
							<input type="text" id="pwtTicket" maxlength="20">
						</div>
						<div class="error-msg" id="error-message"></div>
					</div>
					<div class="col-span-12 text-center">
                     <div class="button-cancel">
                        <button id="pwtCancel">Cancel</button>
                     </div>
                     <div class="button-submit">
                        <button id="pwtOk">Ok</button>
                     </div>
                  </div>
					
		    </div>
		</div>
		
	  <div class="full-screen" id="card-no" style="display: none">
		    <div class="popup">
		    <h5>Please enter card number <button id="cardClose"></button></h5>
					<div class="modal-body">
						<div class="tkt-no-ct">
							<input type="text" id="cardNo" maxlength="20">
						</div>
						<div class="error-msg" id="error-message1"></div>
					</div>
					<div class="col-span-12 text-center">
                     <div class="button-cancel">
                        <button id="cardCancel">Cancel</button>
                     </div>
                     <div class="button-submit">
                        <button id="cardOk">Ok</button>
                     </div>
                  </div>
					
		    </div>
		</div>
		<!-- Modal Ends -->

		  <div class="full-screen" style="display:none">
			    <div class="popup">
			    	<h5>Game Results<button></button></h5>
			        <div class="gm-result">
			        	 <div class="game-name-tab">
			             </div>
			        </div>
			    </div>
		 </div>
<!-- error pop up start -->

		  <div class="popup systemMSG error" id="error-popup" style="display: none;">
			    	<h5>ERROR <button id="closePopUp"></button></h5>
			          <div class="modal-bodyWrap">
			             <div class="modal-body">
			                <div class="row">
			                   <div class="col-xs-9 msgBox" id="error"></div>
			                </div>                  
			             </div>
			          </div>
		  </div>
	      <div class="popup1 systemMSG1 success" id="success-popup" style="display: none;">
	    	<h5>Success<button id="err-popup-button"></button></h5>
	          <div class="modal-bodyWrap">
	             <div class="modal-body">
	                <div class="row">
	                   <div class="col-xs-9 msgBox" id="success"></div>
	                </div>                  
	             </div>
	          </div>
	    </div>
	    <!-- error popup Ends -->
<script>var path="<%=request.getContextPath()%>";</script>
<script>var projectName = "/<%=Util.merchantInfoMap.get("RMS").getProjectName()%>";</script>
<script>var cardInfoReq = "<%=Util.getPropertyValue("WEAVER_CARD_DURING_SALE")%>";</script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery/ajaxCall.js"></script>
<script src="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/js/slePos.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/sleGamePlayAssets/sleContent/js/dateformat.js"></script>
</body>
</html>