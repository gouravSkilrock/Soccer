<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.sle.common.Util"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<label class="draw-with-same-events-color"><span class="glyphicon glyphicon-exclamation-sign"></span>&nbsp;This event is mapped for following draws - </label>
<div style="display:none;">
	<select name="drawWithSameEventSaleTime" id="drawWithSameEventSaleTime">
		<s:iterator value="getDrawDetailsForParticularEvent">
			<option value='<s:property value="saleStartTime"/>'>
				<s:property value="saleStartTime" />
			</option>
		</s:iterator>
	</select>
	<select name="drawWithSameEventFreezeTime" id="drawWithSameEventFreezeTime">
		<s:iterator value="getDrawDetailsForParticularEvent">
			<option value='<s:property value="drawFreezeTime"/>'>
				<s:property value="drawFreezeTime" />
			</option>
		</s:iterator>
	</select>
	<select name="drawWithSameEventDateTime" id="drawWithSameEventDateTime">
		<s:iterator value="getDrawDetailsForParticularEvent">
			<option value='<s:property value="drawDateTime"/>'>
				<s:property value="drawDateTime" />
			</option>
		</s:iterator>
	</select>
</div>
<table style="text-align: center" id="drawWithSameEvent"
	class="table table-bordered table-striped panel-table-border-style draw-with-same-events-color">
	<tbody>
		<tr>
			<th class="table-header-style">Draw Id</th>
			<th class="table-header-style">Game Type</th>
			<th class="table-header-style">Sale Start Time</th>
			<th class="table-header-style">Draw Freeze Time</th>
			<th class="table-header-style">Draw Date Time</th>
			<th class="table-header-style">Draw Status</th>
		</tr>
		<s:iterator value="getDrawDetailsForParticularEvent">
			<tr>
				<td><s:property value="drawId" /></td>
				<td><s:property value="gameTypeName" /></td>
				<td><s:property value="saleStartTime" /></td>
				<td><s:property value="drawFreezeTime" /></td>
				<td><s:property value="drawDateTime" /></td>
				<td><s:property value="drawStatus" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
<script type="text/javascript">
	var drawWithSameEventFreezeTime = [];
	var drawWithSameEventDateTime = [];
	var drawWithSameEventSaleTime = [];
	$('#drawWithSameEventFreezeTime option').each(function() {
		drawWithSameEventFreezeTime.push(new Date(($(this).attr('value')).replace(/\-/g, "/")));
	});
	$('#drawWithSameEventDateTime option').each(function() {
		drawWithSameEventDateTime.push(new Date(($(this).attr('value')).replace(/\-/g, "/")));
	});
	$('#drawWithSameEventSaleTime option').each(function() {
		drawWithSameEventSaleTime.push(new Date(($(this).attr('value')).replace(/\-/g, "/")));
	});
</script>