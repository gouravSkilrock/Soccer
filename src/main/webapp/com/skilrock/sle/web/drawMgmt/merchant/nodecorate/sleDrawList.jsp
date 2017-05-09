<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<div style="display:none;">
</div>
<div class="box-body">
<div class="panel panel-default col-md-12 box-panel-style"
	align="center" id="drawDiv">
	<s:form cssClass="form-horizontal" method="post" action="#" id="eventSearchForm" theme="simple">
		<div class="col-lg-12 col-md-12 padding-15">
			<div class="col-md-5" align="right">
				<label class="control-label" for="inputSuccess">
					Select Draw
				</label>
			</div>
			<div class="col-md-7" align="right">
				<s:select id="draw_Id" headerKey="-1" headerValue="--Please Select--"
					name="draw_Id" list="%{drawMasterList}" listKey="drawId"
					listValue="drawName + ' ' + drawFreezeTime"
					cssClass="btn dropdown-toggle selectpicker btn-default col-md-6"></s:select>
			</div>
		</div>
	</s:form>
</div>
</div>
<div id="eventList" style="display:none;" class="box-body"></div>
<div class="col-md-2"></div>
<div class="col-md-8 alert alert-danger" style="display:none;text-align:center" id="eventMsgDiv">
		Events are not scheduled for this draw !!
</div>
<script type="text/javascript">
	var draw_Id = [];
	$('#draw_Id option').each(function() {
		draw_Id.push($(this).attr('value'));
	});
	commonApp.init($);
</script>