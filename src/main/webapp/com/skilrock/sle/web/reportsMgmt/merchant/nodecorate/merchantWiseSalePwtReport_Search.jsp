<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.sle.common.UtilityFunctions"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Result Submission Data Search</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ionicons.min.css" type="text/css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/dataTables.jqueryui.css" type="text/css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/jquery.dataTables.css" type="text/css" />
		<script type="text/javascript"  src="<%=request.getContextPath() %>/js/Export_Excel.js"></script>
	</head>
	<body>
		<div class="container">
			<div class="col-md-12">
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
						<div class="col-md-11">
							<h3 class="box-title">
								<i class="fa fa-gear"></i>
	     						<s:text name="REPORT_FROM_TO_DATE">
									<s:param value="startDate"></s:param>
									<s:param value="endDate"></s:param>
								</s:text>
							</h3>
  						</div>
  						<div class="col-md-1" style="text-align:right">
     						<img src="<%=request.getContextPath()%>/images/backOffice/reports/excel-icon.png" width="27" id="expExcel" height="27" alt="EXCEL Download" align="right" />
							<s:form id="excelForm" action="export_to_excel" method="post" enctype="multipart/form-data" namespace="/com/skilrock/sle/web/common/Action">
								<s:hidden id="tableData" name="exportData"></s:hidden>
							</s:form>
  						</div>
					</div>
					<div class="box-body">
						<div class="col-md-3"></div>
						<s:if test="reportMap.size>0">
							<div class="panel panel-default col-md-12 box-panel-style"
								style="max-height: 700px; overflow: auto;">
								<table id="example" class="display" cellspacing="0" width="100%">
									<thead>
										<tr>
											<th rowspan="2"><center>Merchant Name</center></th>
											<s:iterator value="gameList">
												<th colspan="2"><center>(<s:property value="gameName" />) <s:property value="gameTypeName" /></center></th>
											</s:iterator>
											<th colspan="2"><center>Total</center></th>
										</tr>
										<tr>
											<s:iterator value="gameList">
												<th><center>Sale Amount</center></th>
												<th><center>PWT Amount</center></th>
											</s:iterator>
											<th><center>Sale Amount</center></th>
											<th><center>PWT Amount</center></th>
										</tr>
									</thead>
									<tbody>

										<s:set name="totalSale" value="0.0" />
										<s:set name="totalPwt" value="0.0" />

										<s:iterator value="reportMap">

											<s:set name="hSale" value="0.0" />
											<s:set name="hPwt" value="0.0" />

											<tr>
												<td><s:property value="%{key}" /></td>
												<s:iterator value="%{value}">
													<td>
														<s:set name="hSale" value="%{#hSale+saleAmount}" />
														<s:property value="getText('{0,number,#,##0.00}', {saleAmount})" />
													</td>
													<td>
														<s:set name="hPwt" value="%{#hPwt+winningAmount}" />
														<s:property value="getText('{0,number,#,##0.00}', {winningAmount})" />
														
													</td>
												</s:iterator>
												<td><%=UtilityFunctions.getAmountFormatForMobile(pageContext.getAttribute("hSale"))%></td>
												<td><%=UtilityFunctions.getAmountFormatForMobile(pageContext.getAttribute("hPwt"))%></td>
											</tr>
										</s:iterator>
									</tbody>
								</table>
							</div>
						</s:if>
						<s:else>
							<div class="alert alert-warning col-md-6"
								style="margin-bottom: 13px">
								<center>
									<b>No Record Exist !!</b>
								</center>
							</div>
						</s:else>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/app.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				$('#expExcel').click(function(){
					exportToExcel('example');
				});

				$('#example').dataTable({
					"bJQueryUI" : false,
					"bSort" : false,
					"bInfo" : false,
					"bFilter" : false,
					"bPaginate": false
				});
			});
		</script>
	</body>
</html>