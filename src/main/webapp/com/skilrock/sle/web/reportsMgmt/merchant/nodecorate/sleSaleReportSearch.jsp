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
	<STYLE type="text/css">
		th {
			text-align: center;
		 }
	</STYLE>
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
 					<s:hidden id="colCount" value="5"></s:hidden>
 					<s:hidden id="rowCount" value="1"></s:hidden>
					<s:hidden id="leaveCount" value="0"></s:hidden>
					<div class="box-body" >
						<div class="col-md-3"></div>
							<s:if test="saleReportDataList.size>0">
							<div class="panel panel-default col-md-12 box-panel-style" style="overflow: auto;">									
								<table id="example" class="display" cellspacing="0" width="100%">
									<thead>
										<tr>
											<th>Game Name</th>
											<th>Game Type Name</th>
											<th>Draw Name</th>
											<th>Result Time</th>
											<th>Freeze Time</th>
											<th>Sale Amount</th>
										</tr>
									</thead>
									<tbody>
										
										<s:set name="netAmount" value="0.0" />
										
										<s:iterator id="beanCart" value="saleReportDataList">
											<tr>
												<td><s:property value="%{gameName}" /></td>
												<td><s:property value="%{gameTypeName}" /></td>
												<td>
													<s:property value="%{drawName}" />
												</td>
												<td><s:property value="%{drawTime}" /></td>
												<td><s:property value="%{drawFreezeTime}" /></td>
												<td><s:set name="netAmount" value="%{netAmount}" /><s:property value="getText('{0,number,#,##0.00}', {netAmount})" /></td>
											</tr>
										<s:set name="totalSaleAmount" value="%{#totalSaleAmount+#netAmount}" />
										</s:iterator>
									</tbody>
									<tfoot>
										<tr>
											    <th colspan="5"><center>Total</center></th>
												<th></th>
											</tr>
									</tfoot>
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
		var colCount = parseInt($("#colCount").val());
		var rowCount = parseInt($("#rowCount").val());
		var leaveCount = parseInt($("#leaveCount").val());
		$('#example').dataTable({
			"bJQueryUI" : false,
			"sPaginationType" : "full_numbers",
			"aLengthMenu" : [ [ 10, 25, 50, -1 ],
					[ 10, 25, 50, "All" ] ],
			"fnFooterCallback" : function(nRow, aaData, iStart, iEnd, aiDisplay) {
			//alert("nRow"+nRow+"  aaData "+aaData +"   iStart"+iStart+"   iEnd"+iEnd+"  aiDisplay "+aiDisplay );
			//alert(isNaN(colCount));
				if(!isNaN(colCount)) {
						var queryArr = [];
						//alert("Row Count : "+rowCount+"colCount : "+colCount);
						var rowTot= rowCount+colCount+leaveCount;
						for ( var k = colCount; k < rowTot; k++) {
							var iPageMarket = 0;
							for ( var i = iStart; i < iEnd; i++) {
								var id1 = aaData[aiDisplay[i]][k];
								var isN = parseFloat(id1.replace(/,/g,''));
								if (!isNaN(isN)) {
									iPageMarket += isN;
								}
							}
							queryArr.push(iPageMarket);
						}
						var nCells = nRow.getElementsByTagName('th');
						for ( var k = 1; k <= rowCount; k++) {
							nCells[k].innerHTML = queryArr[k-1].toFixed(2);;
						}
					}
				}
		});

		$('#expExcel').click(function(){
			exportToExcel('example');
		});
	});
</script>

</body>
</html>
