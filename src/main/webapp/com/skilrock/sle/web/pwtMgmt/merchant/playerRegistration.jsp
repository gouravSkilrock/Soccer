<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>

<html lang="en-US">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="ThemeStarz">
    	<!-- <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/formValidation.css" type="text/css"> -->	
    
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ionicons.min.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jquery.datetimepicker.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/dataTables.jqueryui.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jtable/jquery.dataTables.css" type="text/css">

	
	<title>Sports Lottery | Result Approval</title>
	
	<SCRIPT>
	$(document).ready(function(){
			var countryData=$('#countryData').val();
			//alert(countryData);
			var obj = jQuery.parseJSON(countryData);
   			$.each(obj,function(index,value){
     		$('#country').append(
                    $('<option></option>').val(value.countryCode).html(value.countryName));
   			});
 			
 			$('#country').change(function(){
				$('#state').find('option').remove().end().append($('<option></option>').val(-1).html('--Please Select--'));
    			$('#city').find('option').remove().end().append($('<option></option>').val(-1).html('--Please Select--'));
 				
 				var countryCode=$('#country').val();
 				$.each(obj,function(index,value){
   					if(countryCode == value.countryCode){
   						if(value.stateBeanList != undefined){
    						$.each(value.stateBeanList,function(index1,value1){
  								$('#state').append($('<option></option>').val(value1.stateCode).html(value1.stateName));
   							});
   						}
   					}    
   				});
 			});
 
  			$('#state').change(function(){
				$('#city').find('option').remove().end().append($('<option></option>').val(-1).html('--Please Select--'));
 				var stateCode=$('#state').val();
 				$.each(obj,function(index,value){
   					if($('#country').val() == value.countryCode){
   						if(value.stateBeanList != undefined){
    						$.each(value.stateBeanList,function(index1,value1){
     							if(stateCode == value1.stateCode){
   									if(value1.cityBeanList != undefined){
    									$.each(value1.cityBeanList,function(index2,value2){
  											$('#city').append($('<option></option>').val(value2.cityCode).html(value2.cityName));
   										});
   									}
   								}
							});
   						}
   					}
    			});
 			});

			$('#country option:nth-child(2)').attr('selected', 'selected');
			$('#country option:nth-child(2)').trigger('change');

			$('#state option:nth-child(2)').trigger('change');
			
		});
	</SCRIPT>
</head>

<body class=" page-account" id="page-top" >
<!-- Wrapper -->
<div class="wrapper">

    <!-- Page Content -->
    <div id="page-content">
        <!-- Breadcrumb -->
        	<div class="container" style="margin-top:8px"></div>
        <!-- end Breadcrumb -->
		
		
	<div class="container">
			<div class="col-md-12">
				<div class="box box-info box-style">
					<div class="box-header box-header-style">
						<i class="fa fa-gear"></i><h3 class="box-title">Player Info</h3>
					</div>
					<div class="box-body" >
						<div style="margin-left: 0px; text-align: left; display: none; margin-bottom:5px; margin-top:-4px" id="jsErrorDiv" class="col-md-12 alert alert-danger"></div>						
						<div class="col-md-3"></div>
							<div class="panel panel-default col-md-12 box-panel-style">
								<s:form theme="simple" cssClass="form-horizontal" method="post" action="boHighPrizeORMasApproval" id="verifyPwtForm" onsubmit="return validatePlayerInfoForm()">
									<s:hidden name="pwtStatus" value="%{pwtStatus}" />
									<s:hidden name="ticketNumber" value="%{ticketNumber}" />
									<div class="form-group">
										<div class="col-md-12 margin-div-style"></div>
										<s:hidden id="countryData" name="countryData"></s:hidden>
										
										<div class="form-group col-md-12 margin-div-style">
                                            <div class="col-md-3" align="right"><label class="control-label" for="inputSuccess"> First Name<font color="red">*</font></label></div>
                                            <div class="col-md-9" align="left" >
                                            	<div class="col-md-7"><input  placeholder="First Name" id="firstName" name="firstName" class="form-control"></div>
											</div>
											<div class="col-md-3"></div><div class="col-md-9">
												<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
												</div>
											</div>
                                        </div>
                                        
                                        <div class="form-group col-md-12 margin-div-style">
                                            <div class="col-md-3" align="right"><label class="control-label" for="inputSuccess"> Last Name<font color="red">*</font></label></div>
                                            <div class="col-md-9" align="left" >
                                            	<div class="col-md-7"><input  placeholder="Last Name" id="lastName" name="lastName" class="form-control"></div>
											</div>
											<div class="col-md-3"></div><div class="col-md-9">
												<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
												</div>
											</div>
                                        </div>
                                        
                                        <div class="form-group col-md-12 margin-div-style">
                                            <div class="col-md-3" align="right"><label class="control-label" for="inputSuccess"> Email Id<font color="red">*</font></label></div>
                                            <div class="col-md-9" align="left" >
                                            	<div class="col-md-7"><input  placeholder="Email Id" id="emailId" name="emailId" class="form-control"></div>
											</div>
											<div class="col-md-3"></div><div class="col-md-9">
												<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
												</div>
											</div>
                                        </div>
                                        
                                        <div class="form-group col-md-12 margin-div-style">
                                            <div class="col-md-3" align="right"><label class="control-label" for="inputSuccess"> Phone Number<font color="red">*</font></label></div>
                                            <div class="col-md-9" align="left" >
                                            	<div class="col-md-7"><input  placeholder="Phone Number" id="phoneNumber" name="phoneNumber" class="form-control"></div>
											</div>
											<div class="col-md-3"></div><div class="col-md-9">
												<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
												</div>
											</div>
                                        </div>
                                        
                                        <div class="form-group col-md-12 margin-div-style">
                                            <div class="col-md-3" align="right"><label class="control-label" for="inputSuccess"> Address 1<font color="red">*</font></label></div>
                                            <div class="col-md-9" align="left" >
                                            	<div class="col-md-7"><input  placeholder="Address 1" id="address1" name="address1" class="form-control"></div>
											</div>
											<div class="col-md-3"></div><div class="col-md-9">
												<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
												</div>
											</div>
                                        </div>
                                        
                                        <div class="form-group col-md-12 margin-div-style">
                                            <div class="col-md-3" align="right"><label class="control-label" for="inputSuccess"> Address 2</label></div>
                                            <div class="col-md-9" align="left" >
                                            	<div class="col-md-7"><input  placeholder="Address 2" id="address2" name="address2" class="form-control"></div>
											</div>
											<div class="col-md-3"></div><div class="col-md-9">
												<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
												</div>
											</div>
                                        </div>
                                        
                                      
                                        
                                         
                                        <div class="form-group col-md-12 margin-div-style">
                                            <div class="col-md-3" align="right"><label class="control-label" for="inputSuccess"> Country</label></div>
                                            <div class="col-md-9" align="left" >
                                            	<div class="col-md-7">
                                            	<s:select id="country" headerKey="-1" headerValue="--Please Select--" name="country" list="{}"  cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
                                            	</div>
											</div>
											<div class="col-md-3"></div><div class="col-md-9">
												<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
												</div>
											</div>
                                        </div>
                                        
                                        <div class="form-group col-md-12 margin-div-style">
                                            <div class="col-md-3" align="right"><label class="control-label" for="inputSuccess"> State<font color="red">*</font></label></div>
                                            <div class="col-md-9" align="left" >
                                            	<div class="col-md-7">
                                            	<s:select id="state" headerKey="-1" headerValue="--Please Select--" name="state" list="{}"  cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
                                                </div>
											</div>
											<div class="col-md-3"></div><div class="col-md-9">
												<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
												</div>
											</div>
                                        </div>
                                        
                                          <div class="form-group col-md-12 margin-div-style">
                                            <div class="col-md-3" align="right"><label class="control-label" for="inputSuccess"> City<font color="red">*</font></label></div>
                                            <div class="col-md-9" align="left" >
                                            	<div class="col-md-7">
                                            	<s:select id="city" headerKey="-1" headerValue="--Please Select--" name="city" list="{}"  cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
												</div>
											</div>
											<div class="col-md-3"></div><div class="col-md-9">
												<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
												</div>
											</div>
                                        </div>
                                       
                                        
                                         <div class="form-group col-md-12 margin-div-style">
                                            <div class="col-md-3" align="right"><label class="control-label" for="inputSuccess"> ID Type<font color="red">*</font></label></div>
                                            <div class="col-md-9" align="left" >
                                            	<div class="col-md-7">
                                            	<s:select id="idType" headerKey="-1" headerValue="--Please Select--" name="idType" list="{'Passport','Driving Licence','NID','Pan Card'}"  cssClass="btn dropdown-toggle selectpicker btn-default option col-md-12"></s:select>
                                            	</div>
											</div>
											<div class="col-md-3"></div><div class="col-md-9">
												<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
												</div>
											</div>
                                        </div>
                                        
                                        <div class="form-group col-md-12 margin-div-style">
                                            <div class="col-md-3" align="right"><label class="control-label" for="inputSuccess"> ID Number<font color="red">*</font></label></div>
                                            <div class="col-md-9" align="left" >
                                            	<div class="col-md-7"><input  placeholder="ID Number" id="idNumber" name="idNumber" class="form-control" maxlength="15"></div>
											</div>
											<div class="col-md-3"></div><div class="col-md-9">
												<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
												</div>
											</div>
                                        </div>
                                        
                                        <div class="form-group col-md-12 margin-div-style">
                                            <div class="col-md-3" align="right"><label class="control-label" for="inputSuccess"> Remark<font color="red">*</font></label></div>
                                            <div class="col-md-9" align="left" >
                                            	<div class="col-md-7"><input  placeholder="Remark" id="remark" name="remarks" class="form-control"></div>
											</div>
											<div class="col-md-3"></div><div class="col-md-9">
												<div class="col-md-7">
												<small class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
												</div>
											</div>
                                        </div>
                                        
                                        
   										<input type='hidden' name="verificationCode" id="verificationCode" value='<s:property value="%{verificationCode}"/>'/>
   										<input type='hidden' name="saleMerCode" id="saleMerCode" value='<s:property value="%{saleMerCode}"/>'/>
                                        
										<div class="col-md-12"><hr class="panel-hr-style"></div>
										<div class="col-md-5 left-button-div-style"></div>
										<div class="col-md-7 right-button-div-style" align="left" ><button type="submit" class="btn btn-primary">Submit</button></div>
									</div>
									</s:form>	
							</div>
						</div>
					</div>
				</div>
		</div>
		
		<div id="resultDiv"></div>

		
	
	</div>
    <!-- end Page Content -->

</div>


<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery.datetimepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/app.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jtable/dataTables.jqueryui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jtable/jquery.dataTables.js"></script>






<!--[if gt IE 8]>
<script type="text/javascript" src="assets/js/ie.js"></script>
<![endif]-->
</body>
</html>
