<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String nameSpacePath = path
			+ "/com/skilrock/sle/web/merchantUser/drawMgmt/Action/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/assets/css/jquery.datetimepicker.css"
	type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/jquery/jquery-ui-1.9.2.custom.css"
	type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/jquery/typeahead.bundle.min.css"
	type="text/css">
<!-- <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ionicons.min.css" type="text/css"> -->
</head>

<body>
	<div class="panel panel-default col-md-12 box-panel-style"
		id="eventInsertDiv">
		<s:select id="venueName" headerKey="-1" name="venueName"
			list="%{venueMasterList}" listKey="venueDispName"
			listValue="venueDispName" cssStyle="display:none;"
			cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6"></s:select>
		<s:select id="venueIds" headerKey="-1" name="venueIds"
			list="%{venueMasterList}" listKey="venueId" listValue="venueDispName"
			cssStyle="display:none;"
			cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6"></s:select>

		<s:form cssClass="form-horizontal" method="post" name="abc"
			action="eventInsertSubmit" theme="simple" id="f1"
			onsubmit="return (validateEventInsertionForm()&&formSubmit(this.id,'resultDiv'));">
			<s:token name="token" />
			<div class="col-md-12 alert alert-danger" id="jsErrorDiv"
				style="display:none; margin-left:0px; text-align:left"></div>
			<div class="form-group col-md-12 margin-div-style">
				<div class="form-group col-md-6 margin-div-style">
					<div class="col-md-5" align="right">
						<label class="control-label" for="inputSuccess"> Home Team</label>
					</div>
					<div class="col-md-7" align="left">
						<s:select id="homeTeam" headerKey="-1"
							headerValue="--Please Select--" name="homeTeam"
							list="%{teamMasterList}" listKey="teamCodeId"
							listValue="teamName"
							cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
					</div>
					<div class="col-md-5"></div>
					<div class="col-md-7">
						<small class="col-md-12 small-tag-style-for-error"></small> <small
							class="col-md-12 small-tag-style-for-success"></small>
					</div>

				</div>
				<div class="form-group col-md-6 margin-div-style">
					<div class="col-md-5" align="right">
						<label class="control-label" for="inputSuccess"> Away Team</label>
					</div>
					<div class="col-md-7" align="left">
						<s:select id="awayTeam" headerKey="-1"
							headerValue="--Please Select--" name="awayTeam"
							list="%{teamMasterList}" listKey="teamCodeId"
							listValue="teamName"
							cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
					</div>
					<div class="col-md-5"></div>
					<div class="col-md-7">
						<small class="col-md-12 small-tag-style-for-error"></small> <small
							class="col-md-12 small-tag-style-for-success"></small>
					</div>
				</div>
			</div>

			   <div class="form-group col-md-12 margin-div-style">

				<div class="form-group col-md-6 margin-div-style">
					<div class="col-md-5" align="right">
						<label class="control-label" for="inputSuccess"> Start
							Date</label>
					</div>
					<div class="col-md-7" align="left">
						<div class="input-group">
							<input type="text" id="startDateTimePicker"
								name="eventStartDateTime" class="form-control" readonly />
							<div class="input-group-addon" id="startDateTimePickerDiv">
								<i class="fa fa-calendar"></i>
							</div>
						</div>
					</div>
					<div class="col-md-5"></div>
					<div class="col-md-7">
						<small class="col-md-12 small-tag-style-for-error"></small> <small
							class="col-md-12 small-tag-style-for-success"></small>
					</div>
				</div>
				<div class="form-group col-md-6 margin-div-style">
					<div class="col-md-5" align="right">
						<label class="control-label" for="inputSuccess"> End Date</label>
					</div>
					<div class="col-md-7" align="left">
						<div class="input-group">
							<input type="text" id="endDateTimePicker" class="form-control"
								name="eventEndDateTime" readonly />
							<div class="input-group-addon" id="endDateTimePickerDiv">
								<i class="fa fa-calendar"></i>
							</div>
						</div>
					</div>
					
					<div class="col-md-5"></div>
					<div class="col-md-7">
						<small class="col-md-12 small-tag-style-for-error"></small> <small
							class="col-md-12 small-tag-style-for-success"></small>
					</div>
				</div>
				
			</div>
			<div class="form-group has-feedback col-md-12 margin-div-style">
				<div class="col-md-5" align="right" style="margin-left: 12px;">
					<label class="control-label" for="inputSuccess"> Venue </label>
				</div>
				<div class="col-md-6" align="left">
					<div id="venueId">
						<input type="text" id="venue_Name_textBox" name="venue_Name_textBox"
							class="form-control" style="width: 300px;"
							placeholder="Enter Venue Name" maxlength="20" data-provide="typeahead">
							<i class="glyphicon glyphicon-search form-control-feedback" style="left:280px;"></i>
					</div>
					<div>
						<small class="col-md-12 small-tag-style-for-error" id="v_error" style="width:300px; margin-top:-5px;"></small> 
						<small class="col-md-12 small-tag-style-for-success" id="v_success"></small>
					</div>
					<div id="sameAsChkBox" class="col-md-6" style="margin-left:-15px;">
						<input type="checkbox" id="venueSameAsHomeTeam" name="venueSameAsHomeTeam"/>
						<label class="control-label">Same as home team</label>
					</div>
				</div>
				<s:hidden id="venue" name="venue"></s:hidden>
				<s:hidden id="venue_Name" name="venue_Name"></s:hidden>
			</div>
             
			<s:if test="%{countryDeployed=='ZIMBABWE'}">
				<div class="form-group col-md-12 margin-div-style">
					<div class="form-group col-md-4 margin-div-style">
						<div class="col-md-6" align="right">
							<label class="control-label" for="inputSuccess"> Home
								Team Odds</label>
						</div>
						<div class="col-md-6" align="left">
							<s:textfield id="homeTeamOdds" name="homeTeamOdds" maxlength="6"
								cssClass="form-control" />
						</div>
						<div class="col-md-6"></div>
						<div class="col-md-6">
							<small class="col-md-12 small-tag-style-for-error"></small> <small
								class="col-md-12 small-tag-style-for-success"></small>
						</div>
					</div>
					<div class="form-group col-md-4 margin-div-style">
						<div class="col-md-6" align="right">
							<label class="control-label" for="inputSuccess">Draw Odds</label>
						</div>
						<div class="col-md-6" align="left">
							<s:textfield id="drawOdds" name="drawOdds" maxlength="6"
								cssClass="form-control" />
						</div>
						<div class="col-md-6"></div>
						<div class="col-md-6">
							<small class="col-md-12 small-tag-style-for-error"></small> <small
								class="col-md-12 small-tag-style-for-success"></small>
						</div>
					</div>
					<div class="form-group col-md-4 margin-div-style">
						<div class="col-md-6" align="right">
							<label class="control-label" for="inputSuccess"> Away
								Team Odds</label>
						</div>
						<div class="col-md-6" align="left">
							<s:textfield id="awayTeamOdds" name="awayTeamOdds" maxlength="6"
								cssClass="form-control" />
						</div>
						<div class="col-md-6"></div>
						<div class="col-md-6">
							<small class="col-md-12 small-tag-style-for-error"></small> <small
								class="col-md-12 small-tag-style-for-success"></small>
						</div>
					</div>
				</div>
			</s:if>
			<%-- <div class="form-group">
                                          <div class="col-md-3" align="right" style="width:190px"><label class="control-label" for="inputSuccess"> Selected Events</label></div>
											<div class="col-md-9" align="left" id="cb">
												<div class="checkbox col-md-2">
													<label><input name="optionSet" type="checkbox" class="form-control" checked value="HOME_H" disabled/> Home</label>                                                
												</div>
												<div class="checkbox col-md-2">
													<label><input name="optionSet" type="checkbox" class="form-control" checked value="DRAW_D" disabled/> Draw</label>                                                
												</div>
												 <div class="checkbox col-md-2">
													<label><input name="optionSet" type="checkbox" class="form-control" checked value="AWAY_A" disabled/> Away</label>                                                
												</div>
												 <!-- <div class="checkbox col-md-2">
													<label><input name="optionSet" type="checkbox" class="form-control" checked value="CANCEL_C"/> Cancel</label>                                                
												</div> -->
												 <div class="checkbox col-md-4">
													<small class="col-md-12 small-tag-style-for-error" style="border:1px solid rgb(235,204,209)" id="cbError"></small>                                                
												</div>
											</div>
                                        </div> --%>
			<s:hidden id="gameId" name="gameId" value="%{gameIds}"></s:hidden>
			<s:hidden id="leagueId" name="leagueId" value="%{leagueId}"></s:hidden>
			<hr class="panel-hr-style">
			<div class="form-group col-md-12 margin-div-style">
				<div class="col-md-6 left-button-div-style"></div>
				<div class="col-md-6 right-button-div-style" align="left">
					<button type="submit" class="btn btn-primary" id="submitBtn">Submit</button>
				</div>
			</div>
		</s:form>
	</div>


	<script type="text/javascript"
		src="<%=request.getContextPath()%>/assets/js/jquery.datetimepicker.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/assets/js/app.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/sleEventInsertion.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/typeahead.bundle.min.js"></script>
	<script>
		var venueName = [];
		var venueIds = [];
		$(document).ready(function() {
			$('#venueName option').each(function() {
				venueName.push($(this).attr('value'));
			});
			$('#venueIds option').each(function() {
				venueIds.push($(this).attr('value'));
			});
			
			commonApp.init($);
			/* substringMatcher = function(strs) {
				return function findMatches(q, cb) {
					var matches, substringRegex;
					matches = [];
					substrRegex = new RegExp(q, 'i');
					$.each(strs, function(i, str) {
						if (substrRegex.test(str)) {
							matches.push(str);
						}
					});
					cb(matches);
				};
			}; */
			
			$('#venue_Name_textBox').typeahead({
				highlight : true,
				minLength : 1,
				source : venueName
			});
			
		
			
		});
		

	</script>
	
</body>
</html>


