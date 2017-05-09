<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.opensymphony.xwork2.inject.Context"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en-US">
	<head>
		<meta charset="UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="author" content="ThemeStarz">
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/assets/css/jquery.datetimepicker.css"
			type="text/css">
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/assets/css/ionicons.min.css"
			type="text/css">
		<title>Sports Lottery | Update Jackpot Amount/Message</title>
	</head>
	<body class="page-account" id="page-top">

		<!-- Wrapper -->
		<div class="wrapper">
			<!-- Page Content -->
			<div id="page-content">

				<div class="container">
					<div class="col-md-1"></div>
					<div class="col-md-11">
						<div class="box box-info box-style">
							<div class="box-header box-header-style">
								<i class="fa fa-gear"></i>
								<h3 class="box-title">
									Update Jackpot Amount/Message
								</h3>
							</div>
							<div class="box-body">
								<div class="panel panel-default col-md-12 box-panel-style"
									id="gameDiv">
									<s:form cssClass="form-horizontal" method="post" theme="simple"
										id="f0" action="updateJackpotAmountMessageUpdate">
										<div class="form-group">
											<div class="col-md-10 left-button-div-style"></div>
											<div>
											<div class="col-md-5" align="left">
												<label class="control-label" for="inputSuccess">
													Last Draw Status
												</label>

												
											</div>
											<div class="col-md-3" align="left">
											<input class="form-control" value="<s:property value="status" />" type="text" disabled>
												
																									
											</div>
												<div class="col-md-4"></div>
											<div class="col-md-5"></div>
										</div><div class="col-md-10 left-button-div-style"></div>
											<div>
											<div class="col-md-5" align="left">
												<label class="control-label" for="inputSuccess">
													Carry Forward
												</label>

												<font size="2">(From Last Draw)</font>
											</div>
											<div class="col-md-3" align="left">
											 <input class="form-control" name="delet" id="disabledInput" value="<s:property value="carryForward" />" type="text" disabled>
											</div>
												<div class="col-md-4"></div>
											
										</div>
										<div class="col-md-10 left-button-div-style"></div>
											<div>
											<div class="col-md-5" align="left">
												<label class="control-label" for="inputSuccess">
													Enter Jackpot Amount
												</label>

												<font size="2">(Rolled Over From Last Draw)</font>
											</div>
											<div class="col-md-3" align="right">
											<div class="input-group">
												<s:textfield id="amount" name="amount"
													cssClass="form-control"
													value="%{new com.skilrock.sle.common.Util().getPropertyValue('HARDCODED_JACKPOT_AMOUNT')}"
													maxlength="10" onkeypress="validateNumValues(event);" />
													<span class="input-group-addon">Ghs</span>
											
												</div></div>
											<div class="col-md-5"></div>
											<div class="col-md-3">
												<small  class="col-md-12 small-tag-style-for-error"></small>
												<small class="col-md-12 small-tag-style-for-success"></small>
											</div>
											
											</div>
											
											<div class="col-md-10 left-button-div-style"></div>
											<div>
											<div class="col-md-5" align="left">
												<label class="control-label" for="inputSuccess">
													Current Sale
												</label>

												<font size="2">(Sale till now of Current Draw)</font>
											</div>
											<div class="col-md-3" align="left">
											<input class="form-control" value="<s:property value="saleAmt" />" type="text" disabled>
												
																									
											</div>
												<div class="col-md-4"></div>
											<div class="col-md-5"></div>
										</div>
										<div class="col-md-10 left-button-div-style"></div>
												<div>
											<div class="col-md-5" align="left">
												<label class="control-label" for="inputSuccess">
													Jackpot Amount 
												</label>

												<font size="2">(As per the above sale(12 Match))</font>
											</div>
											<div class="col-md-3" align="left">
											<input class="form-control" value="<s:property value="jackpotAmt" />" type="text" disabled>
												
																									
											</div>
												<div class="col-md-4"></div>
											<div class="col-md-5"></div>
										</div>
										<div class="col-md-10 left-button-div-style"></div>
											<div>
											<div class="col-md-5" align="left">
												<label class="control-label" for="inputSuccess">
													Enter Display Amount
												</label>

												<font size="2">(To be Displayed on Ticket for Current Draw)</font>
											</div>
											
											<div class=" col-md-3" align="right">
											<div class="input-group">
												<s:textfield id="messageAmount" name="messageAmount"
													cssClass="form-control"
													value="%{new com.skilrock.sle.common.Util().getPropertyValue('JACKPOT_DISPLAY_AMOUNT')}"
													maxlength="10" onkeypress="validateValues(event);" />
											 <span class="input-group-addon">Ghs</span>
													</div></div><div class="col-md-4"></div>
										<div class="col-md-5"></div>
											<div class="col-md-3">
												<small  class="col-md-12 small-tag-style-for-error"></small>
												<small  class="col-md-12 small-tag-style-for-success"></small>
											</div>
											</div>
											<div class="col-md-4"></div>
											
											<div class="col-md-12">
												<hr class="panel-hr-style">
											</div>

											<div class="col-md-5 left-button-div-style"></div>
											<div class="col-md-7 right-button-div-style" align="left">
												<s:submit type="submit" cssClass="btn btn-primary"
													value="Update" onclick="return validateAmt();" />
											</div>
										</div>
									</s:form>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-12" id="resultDiv"></div>
			</div>
		</div>


		<script type="text/javascript"
			src="<%=request.getContextPath()%>/assets/js/jquery.datetimepicker.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/assets/js/app.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/sleEventInsertion.js"></script>

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/assets/js/app.js"></script>
		<script type="text/javascript">


function validateValues(e) {
		var specialKeys = new Array();
        specialKeys.push(8); //Backspace
        specialKeys.push(9); //Tab
        specialKeys.push(46); //Delete
        specialKeys.push(36); //Home
        specialKeys.push(35); //End
        specialKeys.push(37); //Left
        specialKeys.push(39); //Right
    
     
     
            var keyCode = e.keyCode == 0 ? e.charCode : e.keyCode;
           // alert(e.charCode);
            if(!((keyCode >= 48 && keyCode <= 57)   || (specialKeys.indexOf(e.keyCode) != -1 && e.charCode != e.keyCode) || (keyCode == 88) || (keyCode == 120) || (keyCode == 46) || (keyCode == 44))){
          e.preventDefault();
            return false ;
        }
			}
	
	function validateNumValues(e) {
		var specialKeys = new Array();
        specialKeys.push(8); //Backspace
        specialKeys.push(9); //Tab
        specialKeys.push(46); //Delete
        specialKeys.push(36); //Home
        specialKeys.push(35); //End
        specialKeys.push(37); //Left
        specialKeys.push(39); //Right
    
     
     
            var keyCode = e.keyCode == 0 ? e.charCode : e.keyCode;
           // alert(e.charCode);
            if(!((keyCode >= 48 && keyCode <= 57)   || (specialKeys.indexOf(e.keyCode) != -1 && e.charCode != e.keyCode) || (keyCode == 46) )){
          e.preventDefault();
            return false ;
        }
			}
			

function validateAmt(){
			var a=0;
			var amtVal = $.trim($('#amount').val());
			var msgVal = $.trim($('#messageAmount').val());
			var isValidate=false;
			var Validate=false;
	
	$(".small-tag-style-for-error").html('');
	$(".small-tag-style-for-error").css("display","none");
	$(".small-tag-style-for-success").html('');
	$(".small-tag-style-for-success").css("display","none");
	$("#jsErrorDiv").html('');
	$("#jsErrorDiv").css("display","none");
	$('amount').removeClass('box-error-style');
	$('messageAmount').removeClass('box-error-style');
	$('amount').removeClass('box-success-style');
	$('messageAmount').removeClass('box-success-style');
	
if(amtVal.length==0){

			$('#amount').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#amount').parent().parent().parent().find(".small-tag-style-for-error").html("Please enter the amount !!");
	      $('#amount').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#amount').addClass("box-error-style");
	     
		
}else if(amtVal.split('.').length == 2){ 
	
				var amountArr = amtVal.split('.');
				if (amountArr[1].length > 2 ) {
		$('#amount').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#amount').parent().parent().parent().find(".small-tag-style-for-error").html("Please enter valid amount !!");
	      $('#amount').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#amount').addClass("box-error-style");
	    }else{
				 var validate= true;
				}		
}else{
				 var validate= true;
}
				
if(msgVal.length==0){

			$('#messageAmount').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#messageAmount').parent().parent().parent().find(".small-tag-style-for-error").html("Please enter the amount !!");
	      $('#messageAmount').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#messageAmount').addClass("box-error-style");
	     
		
}else if(msgVal.split('.').length == 2){	 
			
				var amtArr = msgVal.split('.');
				if (amtArr[1].length > 2) {
		$('#messageAmount').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#messageAmount').parent().parent().parent().find(".small-tag-style-for-error").html("Please enter valid amount !!");
	      $('#messageAmount').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#messageAmount').addClass("box-error-style");
	      }else{
				var isValidate= true;
			}	
}else{
				var isValidate= true;
}
		if(amtVal.split('.').length > 2)
    {
        $('#amount').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#amount').parent().parent().parent().find(".small-tag-style-for-error").html("Please enter valid amount !!");
	      $('#amount').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#amount').addClass("box-error-style");
	      isValidate=false;
    }

if(isValidate && validate){
return true;
}else{
return false;
}
}
						
			
			</script>
	</body>
</html>