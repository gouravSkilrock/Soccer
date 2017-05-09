<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.text.DecimalFormat"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String nameSpacePath = path+"/com/skilrock/sle/web/merchantUser/drawMgmt/Action/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Result Search</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/dataTables.jqueryui.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/jquery.dataTables.css" type="text/css">
	<script type="text/javascript"  src="<%=request.getContextPath() %>/js/Export_Excel.js"></script>
	
	<style type="text/css">
		
		td {
			text-align:center;
		}
		
		th {
			text-align:center;
		}
	
	</style>
	
  </head>
  
  <body>
    <div class="container">
			<div class="col-md-12">
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
  						<div class="col-md-11">
     						<h3 class="box-title" ><i class="fa fa-gear"></i>
     							<span id="reportTitle">Soccer Sale Trend Report</span>
     						</h3>
  						</div>
  						<div class="col-md-1" style="text-align:right">
     						<img src="<%=request.getContextPath()%>/images/backOffice/reports/excel-icon.png" width="27" id="expExcel" height="27" alt="EXCEL Download" align="right"/>
							<s:form id="excelForm" action="exportToExcel" method="post" enctype="multipart/form-data" namespace="/com/skilrock/sle/web/merchantUser/reportsMgmt/Action">
								<s:hidden id="tableData" name="reportData"></s:hidden>
							</s:form>
  						</div>
 					</div>
					<div class="box-body" >
						<div class="col-md-3"></div>
							<s:if test="saleTrendDataList.size>0">
							<div class="panel panel-default col-md-12 box-panel-style" style="overflow: auto;" id="tableDataDiv">									
								<table id="example" class="table table-bordered table-striped panel-table-border-style" cellspacing="0" width="100%">
									<thead>
										<tr>
											<th rowspan=3>Event Id</th>
											<th colspan=2>Period</th>
											<th colspan=3>Sale Performance</th>
											<th colspan=8>Winning Performance</th>										
										</tr>
										<tr>
											<th rowspan=2>Day</th>
											<th rowspan=2 style="width:90px">Date</th>
											<th rowspan=2>Sale</th>
											<th rowspan=2 >Ticket Count</th>
											<th rowspan=2>Avg. Sale / Ticket</th>
											<th colspan=2>10 Out of 12</th>
											<th colspan=2>11 Out of 12</th>
											<th colspan=2>12 Out of 12</th>
											<th colspan=2>Total</th>
										</tr>
										<tr>
											<th>Win Amount per Cedi</th>
											<th>No of Players</th>
											<th>Win Amount per Cedi</th>
											<th>No of Players</th>
											<th>Win Amount per Cedi</th>
											<th>No of Players</th>
											<th>Total Win Amount per Cedi</th>
											<th>Total Players</th>
										</tr>
									</thead>
									<tbody>
										
										<!--<s:set name="totalNoOfSale" value="0.0" />
										<s:set name="totalSaleAmount" value="0.0" />
										<s:set name="totalNoOfClmTkts" value="0.0" />
										<s:set name="totalClmAmount" value="0.0" />
										<s:set name="totalNoOfUnClmTkts" value="0.0" />
										<s:set name="totalUnClmAmount" value="0.0" />
										<s:set name="totalNoOfWinning" value="0.0" />
										<s:set name="totalWinningAmount" value="0.0" />-->
										
										<s:iterator id="beanCart" value="saleTrendDataList">
											
											<tr>
												<td rowspan="<s:property value="%{saleBeansDayWise.size + 1}"/>"><s:property value="drawInfoBean.drawId"/><br><b>(Sale Start Time : <s:property value="drawInfoBean.saleStartTime"/>)</b></td>
												
												<s:if test="saleBeansDayWise.size>0">
												
													<td><s:property value="%{saleBeansDayWise[0].day}"/></td>
													<td><s:property value="%{saleBeansDayWise[0].date}"/></td>
													<td><s:property value="getText('{0,number,#,##0.00}', {saleBeansDayWise[0].saleAmount})"/></td>
													<td><s:property value="%{saleBeansDayWise[0].ticketCount}"/></td>
													<td><s:property value="getText('{0,number,#,##0.00}', {saleBeansDayWise[0].avgSalePerTkt})"/></td>
												
													<s:if test='%{drawInfoBean.drawStatus == "CLAIM ALLOW"}'>
														<td rowspan="<s:property value="%{saleBeansDayWise.size + 1}"/>"><s:property value="getText('{0,number,#,##0.00}', {winningBean.rankThreeWinningAmt})"/></td>
														<td rowspan="<s:property value="%{saleBeansDayWise.size + 1}"/>"><s:property value="winningBean.rankThreeTotalPlayers"/></td>
														<td rowspan="<s:property value="%{saleBeansDayWise.size + 1}"/>"><s:property value="getText('{0,number,#,##0.00}', {winningBean.rankTwoWinningAmt})"/></td>
														<td rowspan="<s:property value="%{saleBeansDayWise.size + 1}"/>"><s:property value="winningBean.rankTwoTotalPlayers"/></td>
														<td rowspan="<s:property value="%{saleBeansDayWise.size + 1}"/>"><s:property value="getText('{0,number,#,##0.00}', {winningBean.rankOneWinningAmt})"/></td>
														<td rowspan="<s:property value="%{saleBeansDayWise.size + 1}"/>"><s:property value="winningBean.rankOneTotalPlayers"/></td>
														<td rowspan="<s:property value="%{saleBeansDayWise.size + 1}"/>"><s:property value="getText('{0,number,#,##0.00}', {winningBean.totalWinningAmt})"/></td>
														<td rowspan="<s:property value="%{saleBeansDayWise.size + 1}"/>"><s:property value="winningBean.totalPlayers"/></td>
													</s:if>
													<s:else>
														<td  rowspan="<s:property value="%{saleBeansDayWise.size + 1}"/>" colspan=8><s:property value="drawInfoBean.drawStatus"/></td>
													</s:else>
												</s:if>
												<s:else>
													<td colspan=13>NO DATA AVAILABLE</td>												
												</s:else>
											</tr>
											
										<s:if test="saleBeansDayWise.size>0">
											<s:set name="tSale" value="0.0" />
											<s:set name="tTickets" value="0" />
											<s:set name="tAvgSale" value="0.0" />
											<s:set name="tSale" value="%{#tSale +  saleBeansDayWise[0].saleAmount}" />
											<s:set name="tTickets" value="%{#tTickets +  saleBeansDayWise[0].ticketCount}" />
											<s:set name="tAvgSale" value="%{#tAvgSale +  saleBeansDayWise[0].avgSalePerTkt}" />														
											<s:iterator id="saleDataCart2" value="saleBeansDayWise" status="taskIndex">
												<s:if test='%{#taskIndex.index+1 < saleBeansDayWise.size}'>
																									
													<tr>
														<td><s:property value="%{saleBeansDayWise[#taskIndex.index+1].day}"/></td>
														<td><s:property value="%{saleBeansDayWise[#taskIndex.index+1].date}"/></td>
														<s:set name="tSale" value="%{#tSale + saleBeansDayWise[#taskIndex.index+1].saleAmount}" />
														<td><s:property value="getText('{0,number,#,##0.00}', {saleBeansDayWise[#taskIndex.index+1].saleAmount})"/></td>
														<s:set name="tTickets" value="%{#tTickets + saleBeansDayWise[#taskIndex.index+1].ticketCount}" />
														<td><s:property value="%{saleBeansDayWise[#taskIndex.index+1].ticketCount}"/></td>														
														<td><s:property value="getText('{0,number,#,##0.00}', {saleBeansDayWise[#taskIndex.index+1].avgSalePerTkt})"/></td>
													</tr>
												</s:if>
											</s:iterator>
											
											<tr>
												<th colspan=2>Total</th>
												<th><%=pageContext.getAttribute("tSale")%></th>
												<th><%=pageContext.getAttribute("tTickets")%></th>
												<%
													if(Double.parseDouble(String.valueOf(pageContext.getAttribute("tTickets"))) <= 0) {
												%>
												<th>0.00</th>
												<%
													} else {
												%>
												<th><%= new DecimalFormat("#,##0.00").format(Double.parseDouble(String.valueOf(pageContext.getAttribute("tSale")))/Double.parseDouble(String.valueOf(pageContext.getAttribute("tTickets")))) %></th>
												<%
													}
												%>
											</tr>
										</s:if>											
										</s:iterator>
										
									</tbody>
								</table>
								</div>
							</s:if>
							<s:else>
								<div class="alert alert-warning col-md-6" style="margin-bottom:13px"><center><b>No Record Exist !!</b></center></div>
							</s:else>									
						</div>
					</div>
				</div>
		</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/app.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#expExcel').click(function(){
			var reportTitle = document.getElementById("reportTitle").innerHTML;
			var tblData = "<span><b>Report Name : </b></span>"+ reportTitle + "</br>";
				 tblData += "<span><b>Game Name : </b></span>"+ $("#gameId option:selected").text() + "</br>";
				 tblData += "<span><b>Game Type : </b></span>"+ $("#gameTypeId option:selected").text() + "</br>";
				 tblData += "<span><b>Start Date : </b></span>"+$("#startDateTimePicker").val()+"</br>";
				 tblData += "<span><b>End Date : </b></span>"+$("#endDateTimePicker").val()+"</br>";	
				 tblData += document.getElementById("tableDataDiv").innerHTML;
			document.getElementById('tableData').value = tblData;
			$("#excelForm").submit();
			return false;			
		});
	});
</script>

</body>
</html>
