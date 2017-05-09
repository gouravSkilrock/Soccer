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
  </head>
  
  <body>
    <div class="container">
			<div class="col-md-12">
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
  						<div class="col-md-11">
     						<h3 class="box-title"><i class="fa fa-gear"></i>
     							<span id="other_data"></span>
     						</h3>
  						</div>
  						<div class="col-md-1" style="text-align:right">
     						<img src="<%=request.getContextPath()%>/images/backOffice/reports/excel-icon.png" width="27" id="expExcel" height="27" alt="EXCEL Download" align="right" />
							<s:form id="excelForm" action="export_to_excel" method="post" enctype="multipart/form-data" namespace="/com/skilrock/sle/web/common/Action">
								<s:hidden id="tableData" name="exportData"></s:hidden>
							</s:form>
  						</div>
 					</div>
					<div class="box-body" >
						<div class="col-md-3"></div>
							<s:if test="gameDataReportList.size>0">
							<div class="panel panel-default col-md-12 box-panel-style" style="overflow: auto;">									
								<table id="example" class="display" cellspacing="0" width="100%">
									<thead>
										<tr>
											<th>Game Name</th>
											<th>Game Type Name</th>
											<th>Draw Name</th>
											<th>Result Time</th>
											<th>Freeze Time</th>
											<th>Status</th>
											<th>Total Sold Tickets</th>
											<th>Total Sale Amount</th>
											<th>Avg Sale Per User</th>
											<th>Total Claimed Tickets</th>
											<th>Total Claimed Amount</th>
											<th>Total Unclaimed Tickets</th>
											<th>Total Unclaimed Amount</th>
											<th>Total Winning Tickets</th>
											<th>Total Winning Amount</th>
											
											
											
										</tr>
									</thead>
									<tbody>
										
										<s:set name="totalNoOfSale" value="0.0" />
										<s:set name="totalSaleAmount" value="0.0" />
										<s:set name="totalNoOfClmTkts" value="0.0" />
										<s:set name="totalClmAmount" value="0.0" />
										<s:set name="totalNoOfUnClmTkts" value="0.0" />
										<s:set name="totalUnClmAmount" value="0.0" />
										<s:set name="totalNoOfWinning" value="0.0" />
										<s:set name="totalWinningAmount" value="0.0" />
										
										<s:iterator id="beanCart" value="gameDataReportList">
											<tr>
												<td><s:property value="%{gameName}" /></td>
												<td><s:property value="%{gameTypeName}" /></td>
												<td>
													<s:if test="%{drawStatus eq 'CLAIM ALLOW' && isArchData != 'YES'}">
														<s:a cssClass="aa" theme="simple" href="gameId=%{gameId}&gameTypeId=%{gameTypeId}&drawId=%{drawId}" ><s:property value="%{drawName}" /></s:a>
													</s:if>
													<s:else>
														<s:property value="%{drawName}" />
													</s:else>
												</td>
												<td><s:property value="%{drawTime}" /></td>
												<td><s:property value="%{drawFreezeTime}" /></td>
												<td><s:property value="%{drawStatus}" /></td>
												<td><s:set name="noOfSale" value="%{noOfSale}" /><s:property value="noOfSale" /></td>
												<td><s:set name="saleAmount" value="%{saleAmount}" /><s:property value="%{saleAmount}" /></td>
												<s:if test="%{avgSalePerSeller==0.0}">
													<td>N.A</td>
												</s:if>
												<s:else>
													<td><s:set name="avgSale" value="%{avgSalePerSeller}" /><s:property value="getText('{0,number,#0.00}', {avgSalePerSeller})" /></td>
												</s:else>
												<td><s:set name="totClaimedTkts" value="%{totalClaimedTkts}" /><s:property value="totalClaimedTkts" /></td>
												<td><s:set name="totClaimedAmt" value="%{totalClaimedAmt}" /><s:property value="getText('{0,number,#0.00}', {totalClaimedAmt})" /></td>
												<td><s:set name="totUnClaimedTkts" value="%{totalUnclaimedTkts}" /><s:property value="totalUnclaimedTkts" /></td>
												<td><s:set name="totUnClaimedAmt" value="%{totalUnclaimedAmt}" /><s:property value="getText('{0,number,#0.00}', {totalUnclaimedAmt})" /></td>
												<td><s:set name="noOfWinning" value="%{noOfWinning}" /><s:property value="noOfWinning" /></td>
												<td><s:set name="winningAmount" value="%{winningAmount}" /><s:property value="%{diplayWinningAmt}" /></td>
											</tr>
										
										<s:set name="totalNoOfSale" value="%{#totalNoOfSale+#noOfSale}" />
										<s:set name="totalSaleAmount" value="%{#totalSaleAmount+#saleAmount}" />
										<s:set name="totalNoOfClmTkts" value="%{#totalNoOfClmTkts+#totClaimedTkts}" />
										<s:set name="totalClmAmount" value="%{#totalClmAmount+#totClaimedAmt}" />
										<s:set name="totalNoOfUnClmTkts" value="%{#totalNoOfUnClmTkts+#totUnClaimedTkts}" />
										<s:set name="totalUnClmAmount" value="%{#totalUnClmAmount+#totUnClaimedAmt}" />
										<s:set name="totalNoOfWinning" value="%{#totalNoOfWinning+#noOfWinning}" />
										<s:set name="totalWinningAmount" value="%{#totalWinningAmount+#winningAmount}" />
										
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
		var otherData = $("#gameId option:selected").text()+" - "+$("#gameTypeId option:selected").text();
		$("#other_data").html(otherData);

		$('#example').dataTable({
			"bJQueryUI" : false,
			"sPaginationType" : "full_numbers",
			"aLengthMenu" : [ [ 10, 25, 50, -1 ],
					[ 10, 25, 50, "All" ] ]
		});

		$('#expExcel').click(function(){
			exportToExcel('example');
		});
	});
</script>

</body>
</html>
