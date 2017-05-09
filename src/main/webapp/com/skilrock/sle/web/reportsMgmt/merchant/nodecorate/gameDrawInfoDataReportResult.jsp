<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
		<title>Result Search</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	 <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/dataTables.jqueryui.css" type="text/css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/jquery.dataTables.css" type="text/css">
	</head>
	<body>
		<div class="container">
			<div class="col-md-12">
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
  						<div class="col-md-11">
     						<h3 class="box-title"><i class="fa fa-gear"></i>Draw Info</h3>
  						</div>
  						<div class="col-md-1" style="text-align:right">
     						<img src="<%=request.getContextPath()%>/images/backOffice/reports/excel-icon.png" width="27" id="expExcelDrawData" height="27" alt="EXCEL Download" align="right" />
							<s:form id="excelForm" action="export_to_excel" method="post" enctype="multipart/form-data" namespace="/com/skilrock/sle/web/common/Action">
								<s:hidden id="tableData" name="exportData"></s:hidden>
							</s:form>
  						</div>
 					</div>
					<div class="box-body">
						<div class="col-md-3"></div>
						<s:if test="gameDataReportList.size>0">
							<div class="panel panel-default col-md-12 box-panel-style"
								style="max-height: 700px; overflow: auto;">
								<table id="example2" class="display" cellspacing="0" width="100%">
									<thead>
										<tr>
											<th>Merchant User Name</th>
											<th>Ticket Number</th>
											<th>Purchase Time</th>
											<th>Ticket Status</th>
											<th>Sale Amount</th>
											<th>Winning Amount</th>
										</tr>
									</thead>
									<tbody>
										<s:iterator value="gameDataReportList">
											<tr>
												<td><s:property value="%{merchantOrgName}" /></td>
												<td><s:property value="%{ticketNumber}" /></td>
												<td><s:property value="%{purchaseTime}" /></td>
												<td><s:property value="%{ticketStatus}" /></td>
												<td><s:property value="getText('{0,number,#0.00}',{saleAmount})"/></td>
												<td><s:property value="getText('{0,number,#0.00}',{winningAmount})"/></td>
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

		<script type="text/javascript">
		$(document).ready(function() {
			$("#other_data").html(null);

			$('#example2').dataTable( {
				"bJQueryUI" : false,
				"sPaginationType" : "full_numbers",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ]
			});

			$('#expExcelDrawData').click(function(){
				exportToExcel('example2');
			});
		});
</script>
	</body>
</html>