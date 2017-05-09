function exportToExcel1(){
	var headerArr=[];
	  var totalColumnsLength=$('#example1 tbody:first tr:first').find('td').length;
	  
	 //alert($('#example1 tbody tr:first').find('td').length);
	 // alert($('#example1 thead').find('tr').length);
	  
	  
	  var noOfrow=$('#example1 thead:first').find('tr').length;
	var noOfcolumn=$('#example1 tbody tr:first').find('td').length;



						var noOfFootRow=$('#example1 > tfoot >tr').length;
						//alert('noOfFootRow=='+noOfFootRow);
						var footCellArr;
						var footRowArr = new Array();

						for(var i=0;i<noOfFootRow;i++){
							footCellArr=new Array();
							for(var j=0;j<noOfcolumn;j++){
								cellObj={};
								cellObj['data']="";
								cellObj['isDataSet']="NO";
								cellObj['isMergedCell']="NO";
								cellObj['mergeRange']="0,0,0,0";
								footCellArr.push(cellObj);
								}
							footRowArr.push(footCellArr);
						}
						$('#example1 > tfoot:first >tr').each(function(index){
							var row = $(this);
							var currentCol=-1;
							//alert('before--'+row.text().trim()+'--after--'+$(this).html());
							row.find('th').each(function(colno){
							
							currentCol++;
							if(footRowArr[index][currentCol].isDataSet == 'YES')
							currentCol++;
							
							var rowSpan = (typeof $(this).attr('rowspan') == 'undefined' ) ? 1 : $(this).attr('rowspan'); 
							var colSpan =  (typeof $(this).attr('colspan') == 'undefined' ) ? 1 : $(this).attr('colspan');
							var col = $(this);
							//alert('before--'+col.text().trim()+'--after--'+col.attr('aria-label').substr(0, col.attr('aria-label').indexOf(':')).trim());
							//alert("rowSpan---="+rowSpan +"=---colSpan="+ colSpan+"=---currentCol="+currentCol);
							
							var colText="";
							if(typeof col.attr('aria-label') == 'undefined' )
								colText=col.text().trim();
							else
								colText=col.attr('aria-label').substr(0, col.attr('aria-label').indexOf(':')).trim();
							
							
							if(rowSpan == 1 && colSpan ==1){
								footRowArr[index][currentCol].isDataSet ='YES';
								footRowArr[index][currentCol].data =colText;
							}//end of equal rowspan and colspan
							
							var isRowIncrement=true;
							if(rowSpan>1){
							for(var i=index;i<rowSpan;i++){
							if(isRowIncrement){
								footRowArr[i][currentCol].isDataSet ='YES';
								footRowArr[i][currentCol].isMergedCell ='YES';
							var rowLast=index+rowSpan-1;
							footRowArr[i][currentCol].mergeRange=index+","+rowLast+","+currentCol+","+currentCol;
							footRowArr[i][currentCol].data =colText;
							}else{
								footRowArr[i][currentCol].isDataSet ='YES';
							
							}
							isRowIncrement = false;
							
							}
							}//end of rowSpan loop
							var isColIncrement=true;
							
							if(colSpan>1){
							for(var j=0;j<colSpan;j++){
							if(isColIncrement){
								footRowArr[index][currentCol].isMergedCell ='YES';
							var colLast=currentCol+parseInt(colSpan)-1;
							footRowArr[index][currentCol].mergeRange=index+","+index+","+currentCol+","+colLast;
							footRowArr[index][currentCol].isDataSet ='YES';
							footRowArr[index][currentCol].data =colText;
							}else{
							currentCol++;
							footRowArr[index][currentCol].isDataSet ='YES';
							}
							isColIncrement=false;
							
							}
							}//end of colspan loop
							
							
							});
							
							//alert("--"+index+"index row--="+JSON.stringify(rowArr[index]));
					});
						
						
						var cellObj;
						var rowArr = new Array();

						for(var i=0;i<noOfrow;i++){

						var cellArr=new Array();
						for(var j=0;j<noOfcolumn;j++){
						cellObj={};
						cellObj['data']="";
						cellObj['isDataSet']="NO";
						cellObj['isMergedCell']="NO";
						cellObj['mergeRange']="0,0,0,0";
						cellArr.push(cellObj);
						}
						rowArr.push(cellArr);

						}
						//alert("dummy data---="+JSON.stringify(rowArr));




					$('#example1 > thead:first >tr').each(function(index){
							var row = $(this);
							var currentCol=-1;
							//alert('before--'+row.text().trim()+'--after--'+$(this).html());
							row.find('th').each(function(colno){
							
							currentCol++;
							if(rowArr[index][currentCol].isDataSet == 'YES')
							currentCol++;
							
							var rowSpan = (typeof $(this).attr('rowspan') == 'undefined' ) ? 1 : $(this).attr('rowspan'); 
							var colSpan =  (typeof $(this).attr('colspan') == 'undefined' ) ? 1 : $(this).attr('colspan');
							var col = $(this);
							//alert('before--'+col.text().trim()+'--after--'+col.attr('aria-label').substr(0, col.attr('aria-label').indexOf(':')).trim());
							//alert("rowSpan---="+rowSpan +"=---colSpan="+ colSpan+"=---currentCol="+currentCol);
							
							var colText="";
							if(typeof col.attr('aria-label') == 'undefined' )
								colText=col.text().trim();
							else
								colText=col.attr('aria-label').substr(0, col.attr('aria-label').indexOf(':')).trim();
							
							
							if(rowSpan == 1 && colSpan ==1){
							rowArr[index][currentCol].isDataSet ='YES';
							rowArr[index][currentCol].data =colText;
							}//end of equal rowspan and colspan
							
							var isRowIncrement=true;
							if(rowSpan>1){
							for(var i=index;i<rowSpan;i++){
							if(isRowIncrement){
							rowArr[i][currentCol].isDataSet ='YES';
							rowArr[i][currentCol].isMergedCell ='YES';
							var rowLast=index+rowSpan-1;
							rowArr[i][currentCol].mergeRange=index+","+rowLast+","+currentCol+","+currentCol;
							rowArr[i][currentCol].data =colText;
							}else{
							rowArr[i][currentCol].isDataSet ='YES';
							
							}
							isRowIncrement = false;
							
							}
							}//end of rowSpan loop
							var isColIncrement=true;
							
							if(colSpan>1){
							for(var j=0;j<colSpan;j++){
							if(isColIncrement){
							rowArr[index][currentCol].isMergedCell ='YES';
							var colLast=currentCol+parseInt(colSpan)-1;
							rowArr[index][currentCol].mergeRange=index+","+index+","+currentCol+","+colLast;
							rowArr[index][currentCol].isDataSet ='YES';
							rowArr[index][currentCol].data =colText;
							}else{
							currentCol++;
							rowArr[index][currentCol].isDataSet ='YES';
							}
							isColIncrement=false;
							
							}
							}//end of colspan loop
							
							
							});
							
							//alert("--"+index+"index row--="+JSON.stringify(rowArr[index]));
					});
//var json  = JSON.stringify(rowArr);
//alert(json);
	  
	  
	  var excelData={};
	  excelData['headerData']=rowArr;
	  excelData['noOfFootRow']=noOfFootRow;
	  if(noOfFootRow>0){
		  excelData['footerData']=footRowArr;
		 }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  /*
	var t = $('#example1 thead tr').each(function() {
			// $(this) is used more than once; cache it for performance.
				var $row = $(this);
				//alert($row.html());
				//alert($row.find('th').length);
				var headerLength=$row.find('th').length;
				//alert("---"+$row.find(
							//	'th:nth-child(1)').text().trim());
				totalColumnsLength=headerLength;
				var headerData={};
				for(var g=1;g<headerLength+1;g++){
				alert($row.find(
								'th:nth-child('+g+')').attr('colspan'));
				headerData['col'+g]=$row.find(
								'th:nth-child('+g+')').text().trim();
				}
				// For each row that's "mapped", return an object that
				//  describes the first and second __tag_92$37_ in the row.
				// alert($row.find('.check_one').prop('checked'));
				headerArr.push(headerData);
			});
			*/
			
	var rowArr=[];
	var t = $('#example1 tbody:first tr').each(function() {
			// $(this) is used more than once; cache it for performance.
		//alert('before--'+$(this).text().trim()+'--after--'+$(this).html());
				var $row = $(this);
				//alert($row.html());
				//alert($row.find('td').length);
				var clumnLength=$row.find('td').length;
				
				
				

					var cellArr=new Array();
					for(var j=1;j<clumnLength+1;j++){
						 cellObj={};
					/*alert('before--'+$row.find(
							':nth-child('+j+')').find('a').text().trim()+'--after--'+$row.find(
									':nth-child('+j+')').html());*/
							if($row.find(
									':nth-child('+j+')').find('a').length >0){
					cellObj['data']=$row.find(
								':nth-child('+j+')').find('a').text().trim();
							}else{
								cellObj['data']=$row.find(
										':nth-child('+j+')').text().trim();
							}
							
					
								cellObj['dataType']=$row.find(
								':nth-child('+j+')').attr('class');
							//alert($row.find(':nth-child('+j+')').attr('class'));
						cellArr.push(cellObj);
					}
					rowArr.push(cellArr);

				
				
				
				
				
				
				
				
				
				/*
				
				var rowData={};
				for(var g=1;g<clumnLength+1;g++){
				rowData['col'+g]=$row.find(
								':nth-child('+g+')').text().trim();
				}
				// For each row that's "mapped", return an object that
				//  describes the first and second __tag_92$37_ in the row.
				// alert($row.find('.check_one').prop('checked'));
				rowArr.push(rowData);*/
			});
			
			excelData['rowData']=rowArr;
			excelData['noOfColumns']=totalColumnsLength;
			excelData['playerName']=$('#playerName option:selected').text();
			excelData['fromDate']=$('#startDate').val();
			excelData['endDate']=$('#endDate').val();
			excelData['mainHeader']=$('.form-section-heading').text();
			//alert($('#combobox option:selected').text());
			//alert($('#startDate').val());
			//alert($('#endDate').val());
			//var wdrData = new Array();
			//wdrData[key]=  new PlayerWithdrawReportBean();
			
	//alert(json);
	//alert(JSON.stringify(rowArr));
	//alert(JSON.stringify(excelData));

//_ajaxCallText("<%=request.getContextPath() %>/com/skilrock/pms/web/backOffice/reportsMgmt/Action/export_to_excel.action",'exportData='+JSON.stringify(excelData));


$('#tableData').val(JSON.stringify(excelData));
$('#excelForm').submit() ;
//return true;
}