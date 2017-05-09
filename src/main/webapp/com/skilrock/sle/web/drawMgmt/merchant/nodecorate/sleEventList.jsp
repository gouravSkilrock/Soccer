<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.sle.common.Util"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/jquery/typeahead.bundle.min.css"
	type="text/css">
<div style="display: none;">
	<input type="hidden" id="serverDate" value="<%=Util.getCurrentTimeString()%>">
	<select name="mappedEventId" id="mappedEventId">
		<s:iterator value="mappedEventMasterList">
			<option value='<s:property value="eventId"/>'>
				<s:property value="eventId" />
			</option>
		</s:iterator>
	</select>
	<select name="mappedLeagueId" id="mappedLeagueId">
		<s:iterator value="mappedEventMasterList">
			<option value='<s:property value="leagueId"/>'>
				<s:property value="leagueId" />
			</option>
		</s:iterator>
	</select>
	<select name="mappedVenueId" id="mappedVenueId">
		<s:iterator value="mappedEventMasterList">
			<option value='<s:property value="venueId"/>'>
				<s:property value="venueId" />
			</option>
		</s:iterator>
	</select>
	<select name="mappedHomeTeamId" id="mappedHomeTeamId">
		<s:iterator value="mappedEventMasterList">
			<option value='<s:property value="homeTeamId"/>'>
				<s:property value="homeTeamId" />
			</option>
		</s:iterator>
	</select>
	<select name="mappedAwayTeamId" id="mappedAwayTeamId">
		<s:iterator value="mappedEventMasterList">
			<option value='<s:property value="awayTeamId"/>'>
				<s:property value="awayTeamId" />
			</option>
		</s:iterator>
	</select>
	<select name="mappedHomeTeamOdds" id="mappedHomeTeamOdds">
		<s:iterator value="mappedEventMasterList">
			<option value='<s:property value="homeTeamOdds"/>'>
				<s:property value="homeTeamOdds" />
			</option>
		</s:iterator>
	</select>
	<select name="mappedAwayTeamOdds" id="mappedAwayTeamOdds">
		<s:iterator value="mappedEventMasterList">
			<option value='<s:property value="awayTeamOdds"/>'>
				<s:property value="awayTeamOdds" />
			</option>
		</s:iterator>
	</select>
	<select name="mappedDrawOdds" id="mappedDrawOdds">
		<s:iterator value="mappedEventMasterList">
			<option value='<s:property value="drawOdds"/>'>
				<s:property value="drawOdds" />
			</option>
		</s:iterator>
	</select>
	<select name="startTime" id="startTime">
		<s:iterator value="mappedEventMasterList">
			<option value='<s:property value="startTime"/>'>
				<s:property value="startTime" />
			</option>
		</s:iterator>
	</select>
	<select name="endTime" id="endTime">
		<s:iterator value="mappedEventMasterList">
			<option value='<s:property value="endTime"/>'>
				<s:property value="endTime" />
			</option>
		</s:iterator>
	</select>
	
</div>
<div class="panel panel-default col-md-12 box-panel-style"
	align="center" style="display: none" id="eventDiv">
	<s:form cssClass="form-horizontal" method="post" action="#"
		id="eventSearchForm">
		<div class="col-md-12">
			<table style="text-align: center" id="mappedEventsTable"
				class="table table-bordered table-striped panel-table-border-style">
				<tbody>
					<tr>
						<th class="table-header-style"></th>
						<th class="table-header-style">
							Event Description
						</th>
						<th class="table-header-style">
							League Name
						</th>
						<th class="table-header-style">
							Home Team
						</th>
						<s:if test="%{countryDeployed=='ZIMBABWE'}">
							<th class="table-header-style">
								Home Team Odds
							</th>
						</s:if>
						<th class="table-header-style">
							Away Team
						</th>
						<s:if test="%{countryDeployed=='ZIMBABWE'}">
							<th class="table-header-style">
								Away Team Odds
							</th>
						</s:if>
						<th class="table-header-style">
							Start Time
						</th>
						<th class="table-header-style">
							End Time
						</th>
						<th class="table-header-style">
							Venue
						</th>
						<s:if test="%{countryDeployed=='ZIMBABWE'}">
							<th class="table-header-style">
								Draw Odds
							</th>
						</s:if>
						<th class="table-header-style">
							Message
						</th>
					</tr>
					<s:iterator value="mappedEventMasterList">
						<tr>
							<td>
								<input type="radio" name="updateEventInfo"
									value='<s:property value="eventId"/>' />
							</td>
							<td>
								<s:property value="eventDescription" />
							</td>
							<td>
								<s:property value="leagueName" />
							</td>
							<td>
								<s:property value="homeTeamName" />
							</td>
							<s:if test="%{countryDeployed=='ZIMBABWE'}">
								<td>
									<s:property value="homeTeamOdds" />
								</td>
							</s:if>
							<td>
								<s:property value="awayTeamName" />
							</td>
							<s:if test="%{countryDeployed=='ZIMBABWE'}">
								<td>
									<s:property value="awayTeamOdds" />
								</td>
							</s:if>
							<td>
								<s:property value="startTime" />
							</td>
							<td>
								<s:property value="endTime" />
							</td>
							<td>
								<s:property value="venueName" />
							</td>
							<s:if test="%{countryDeployed=='ZIMBABWE'}">
								<td>
									<s:property value="drawOdds" />
								</td>
							</s:if>
							<td>

							</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
		<div class="col-md-12" style="display:none;" id="drawWithSameEvents"></div>
	</s:form>
</div>
<s:select id="venueName" headerKey="-1" name="venueName"
			list="%{venueMasterList}" listKey="venueDispName"
			listValue="venueDispName" cssStyle="display:none;"
			cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6"></s:select>
<s:select id="venueIds" headerKey="-1" name="venueIds"
			list="%{venueMasterList}" listKey="venueId" listValue="venueDispName"
			cssStyle="display:none;"
			cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6"></s:select>
<div>
	<div class="panel panel-default col-md-12 box-panel-style padding-15"
		align="center" style="display: none" id="updateEventDiv">
		<div class="col-md-12 " style="text-align: left;">
			<div style="display: none;" class="alert alert-danger"
				id="sameTeamError">
				Home team and away team cannot be same !!
			</div>
			<div style="display: none;" class="alert alert-danger"
				id="dateTimeError">
				Start time must be less than end time !!
			</div>
			<div class="alert alert-danger" id="dateError"></div>
			<div class="alert alert-danger" id="atleastOneChangeError"></div>
			<div class="alert alert-danger" style="display: none;" id="venue_error"></div>
					
		</div>
		<s:form cssClass="form-horizontal" method="post" action="#"
			name="updateEventForm" id="updateEventForm" theme="simple">
			<div class="form-group col-md-12 margin-div-style">
				<div class="col-md-5" align="right">
					<label class="control-label" for="inputSuccess">
						League Name
					</label>
				</div>
				<div class="col-md-7" align="left">
					<s:select id="leagueId" name="leagueId" list="%{leagueMap}"
						listKey="key" listValue="%{value.leagueDispName}"
						cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6"></s:select>
				</div>
			</div>
			<div class="form-group col-md-12 margin-div-style"></div>
			<div class="form-group col-md-12 margin-div-style">
				<div class="form-group col-md-6 margin-div-style">
					<div class="col-md-5" align="right">
						<label class="control-label" for="inputSuccess">
							Home Team
						</label>
					</div>
					<div class="col-md-7" align="left">
						<s:select id="homeTeamId" name="homeTeamId"
							list="%{teamMasterList}" listKey="teamId" listValue="teamName"
							cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6 form-control"></s:select>
					</div>
					<div class="col-md-5" align="left"></div>
					<div class="col-md-7">
						<small id="homeTeamError" class="col-md-12 small-tag-style-for-error"></small>
					</div>
				</div>
					
				<div class="form-group col-md-6 margin-div-style">
					<div class="col-md-5" align="right">
						<label class="control-label" for="inputSuccess">
							Away Team
						</label>
					</div>
					<div class="col-md-7" align="left">
						<s:select id="awayTeamId" name="awayTeamId"
							list="%{teamMasterList}" listKey="teamId" listValue="teamName"
							cssClass="btn dropdown-toggle selectpicker btn-default option col-md-6 form-control"></s:select>
					</div>
					<div class="col-md-5" align="left"></div>
					<div class="col-md-7">
						<small id="awayTeamError" class="col-md-12 small-tag-style-for-error"></small>
					</div>
				</div>
			</div>

			<div class="form-group col-md-12 margin-div-style">
				<div class="form-group col-md-6 margin-div-style">
					<div class="col-md-5" align="right">
						<label class="control-label" for="inputSuccess">
							Start Date
						</label>
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
				</div>
				<div class="form-group col-md-6 margin-div-style">
					<div class="col-md-5" align="right">
						<label class="control-label" for="inputSuccess">
							End Date
						</label>
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
				</div>
			</div>
			<div class="form-group col-md-12 margin-div-style">
				<div class="col-md-5" align="right">
					<label class="control-label" for="inputSuccess">
						Venue Name
					</label>
				</div>
				<div class="col-md-7" align="left">
					<div id="venue">
						<input type="text" id="venue_Name_Entered_TextBox" name="venue_Name_Entered_TextBox"
							class="typeahead form-control" style="width: 300px;"
							placeholder="Enter Venue Name" maxlength="20">
					</div>
					<s:hidden id="old_venue"></s:hidden>
					<s:hidden id="venueId" name="venueId"></s:hidden>
					<s:hidden id="venue_Name_Entered" name="venue_Name_Entered"></s:hidden>
					<div id="sameAsHomeTeamChkBox">
						<input type="checkbox" id="venueSameAsHomeTeamCheck" name="venueSameAsHomeTeamCheck" />
						<label class="control-label">Same as home team</label>
					</div>
				</div>
			</div>
			<s:if test="%{countryDeployed=='ZIMBABWE'}">
				<div class="form-group col-md-12 margin-div-style">
					<div class="form-group col-md-4 margin-div-style">
						<div class="col-md-6" align="right">
							<label class="control-label" for="inputSuccess">
								Home Team Odds
							</label>
						</div>
						<div class="col-md-6" align="left">
							<s:textfield id="homeTeamOdds" name="homeTeamOdds" maxlength="6"
								cssClass="form-control" />
						</div>
						<div class="col-md-6"></div>
						<div class="col-md-6">
							<small class="col-md-12 small-tag-style-for-error"></small>
							<small class="col-md-12 small-tag-style-for-success"></small>
						</div>
					</div>
					<div class="form-group col-md-4 margin-div-style">
						<div class="col-md-6" align="right">
							<label class="control-label" for="inputSuccess">
								Draw Odds
							</label>
						</div>
						<div class="col-md-6" align="left">
							<s:textfield id="drawOdds" name="drawOdds" maxlength="6"
								cssClass="form-control" />
						</div>
						<div class="col-md-6"></div>
						<div class="col-md-6">
							<small class="col-md-12 small-tag-style-for-error"></small>
							<small class="col-md-12 small-tag-style-for-success"></small>
						</div>
					</div>
					<div class="form-group col-md-4 margin-div-style">
						<div class="col-md-6" align="right">
							<label class="control-label" for="inputSuccess">
								Away Team Odds
							</label>
						</div>
						<div class="col-md-6" align="left">
							<s:textfield id="awayTeamOdds" name="awayTeamOdds" maxlength="6"
								cssClass="form-control" />
						</div>
						<div class="col-md-6"></div>
						<div class="col-md-6">
							<small class="col-md-12 small-tag-style-for-error"></small>
							<small class="col-md-12 small-tag-style-for-success"></small>
						</div>
					</div>
				</div>
			</s:if>
			<input type="button" value="Update" id="updateEventBtn"
				class="btn btn-primary margin_right_two">
		</s:form>
	</div>
</div>
<div id="updateSuccess" style="display: none;"></div>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/sleEventInsertion.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/typeahead.bundle.min.js"></script>
<script type="text/javascript">
	var mappedEventId = [];
	var mappedLeagueId = [];
	var mappedVenueId = [];
	var mappedHomeTeamId = [];
	var mappedAwayTeamId = [];
	var mappedHomeTeamOdds = [];
	var mappedAwayTeamOdds = [];
	var mappedDrawOdds = [];
	var startTime = [];
	var endTime = [];
	var venueName = [];
	var venueIds = [];
	$('#venueName option').each(function() {
		venueName.push($(this).attr('value'));
	});
	$('#venueIds option').each(function() {
		venueIds.push($(this).attr('value'));
	});			
	$('#venueSameAsHomeTeamCheck').prop('checked',false);
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
			
				$('#venue_Name_Entered_TextBox').typeahead({
				highlight : true,
				minLength : 1,
				source : venueName
			});
</script>
