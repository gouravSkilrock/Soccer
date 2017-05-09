function validateEventInsertionForm(){
	
	//console.log("Lorem Ipsum");
	//console.log("Test LoremIpsum");
	//console.log("Test caching");
	var isValidate=true;	
	$(".small-tag-style-for-error").html('');
	$(".small-tag-style-for-error").css("display","none");
	$("#jsErrorDiv").html('');
	$("#jsErrorDiv").css("display","none");
	$('input').removeClass('box-success-style');
	$('select').removeClass('box-success-style');
	$('input').removeClass('box-error-style');
	$('select').removeClass('box-error-style');
		 
	if($('#venue_Name').val()== "")
		
	{     $('#v_success').css("display","none");
		  $('#v_error').css("display","block");
	      $('#v_error').html("Please enter/select venue name !!");
	      $('#venue_Name').addClass("box-error-style");
	      isValidate=false;
	}
	else if($.trim($('#venue_Name').val())!= "" && $.trim($('#venue_Name').val())!= undefined){
		if(!($.trim($('#venue_Name').val()).match(/^[^&,:@$%]+$/g))){
			  $('#v_success').css("display","none");
			  $('#v_error').css("display","block");
		      $('#v_error').html("Please enter correct venue name!!");
		      $('#venue_Name').addClass("box-error-style");
		      isValidate=false;
		}/*else if($.isNumeric($.trim($('#venue_Name').val()))){
			  $('#v_success').css("display","none");
			  $('#v_error').css("display","block");
		      $('#v_error').html("Please enter atleast one alphabet !!");
		      $('#venue_Name').addClass("box-error-style");
		      isValidate=false;
		}
		else if($.trim($('#venue_Name').val()).match(/^[^a-zA-Z]+$/)){
			  $('#v_success').css("display","none");
			  $('#v_error').css("display","block");
		      $('#v_error').html("Please enter atleast one alphabet !!");
		      $('#venue_Name').addClass("box-error-style");
		      isValidate=false;
		}*/
		else{
			  $('#v_success').css("display","block");
			  $('#v_error').css("display","none");
		      $('#v_error').html("");
		}
	}
	if($('#homeTeam').val()== -1 || $('#homeTeam').val()== "")
	{    	
		  $('#homeTeam').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#homeTeam').parent().parent().find(".small-tag-style-for-error").html("Please select any team !!");
	      $('#homeTeam').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#homeTeam').addClass("box-error-style");
	      isValidate=false;
	}	
	
	if($('#homeTeamOdds').val()!= "" && $('#homeTeamOdds').val()!=undefined){
		if(pattern.test($('#homeTeamOdds').val())==false){
			  $('#homeTeamOdds').parent().parent().find(".small-tag-style-for-success").css("display","none");
		      $('#homeTeamOdds').parent().parent().find(".small-tag-style-for-error").html("Please enter correct odds");
		      $('#homeTeamOdds').parent().parent().find(".small-tag-style-for-error").css("display","block");
		      $('#homeTeamOdds').addClass("box-error-style");
		      isValidate=false;
		}
	}
	
	if($('#awayTeamOdds').val()!= "" && $('#awayTeamOdds').val()!=undefined){
	     if(pattern.test($('#awayTeamOdds').val())==false){
			 $('#awayTeamOdds').parent().parent().find(".small-tag-style-for-success").css("display","none");
		     $('#awayTeamOdds').parent().parent().find(".small-tag-style-for-error").html("Please enter correct odds");
		     $('#awayTeamOdds').parent().parent().find(".small-tag-style-for-error").css("display","block");
		     $('#awayTeamOdds').addClass("box-error-style");
		     isValidate=false;
		}
	}
	
	if($('#drawOdds').val()!= "" && $('#drawOdds').val()!=undefined){
		if(pattern.test($('#drawOdds').val())==false){
			  $('#drawOdds').parent().parent().find(".small-tag-style-for-success").css("display","none");
		      $('#drawOdds').parent().parent().find(".small-tag-style-for-error").html("Please enter correct odds");
		      $('#drawOdds').parent().parent().find(".small-tag-style-for-error").css("display","block");
		      $('#drawOdds').addClass("box-error-style");
		      isValidate=false;
		}
	}
	
	if($('#awayTeam').val()== -1 || $('#awayTeam').val()== "")
	{  
		 $('#awayTeam').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#awayTeam').parent().parent().find(".small-tag-style-for-error").html("Please select any team !!");
	      $('#awayTeam').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#awayTeam').addClass("box-error-style");
	      isValidate=false;
	}
	if($('#startDateTimePicker').val()== "____/__/__ __:__")
	{     
		 $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").html("Please select start date !!");
	      $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#startDateTimePicker').addClass("box-error-style");
	      isValidate=false;
	}
	if($('#endDateTimePicker').val()== "____/__/__ __:__")
	{  
		  $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").html("Please select end date !!");
	      $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#endDateTimePicker').addClass("box-error-style");
	      isValidate=false;
	}else{
		 $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","block");
	}
	/*var flag = 0;
	$("#cb").find("checkbox").each(function(){
	    if ($(this).prop('checked')==true){ 
	        flag+=1;
	    }
	});
	
	if(flag == 0){
		$('#cbError').html("Please select aleast one event !!");
		$('#cbError').css("display", "block");
	}*/
	
	if($('#homeTeam').val() == $('#awayTeam').val() && $('#homeTeam').val() != -1 && $('#awayTeam').val() != -1){
		$("#jsErrorDiv").append("<li>Home Team and Away Team can't be same. Please select different one !!</li>");
		$("#jsErrorDiv").css("display","block");
	    isValidate=false;
	    
	}
	
	if($('#startDateTimePicker').val() == $('#endDateTimePicker').val() && $('#startDateTimePicker').val()!="____/__/__ __:__" && $('#endDateTimePicker').val()!="____/__/__ __:__"){
		$("#jsErrorDiv").append("<li>Start Date and End Date can't be same. Please select different one !!</li>");
		$("#jsErrorDiv").css("display","block");	
	    isValidate=false;
	}
	
	var sDate = new Date($('#startDateTimePicker').val());
	var eDate = new Date($('#endDateTimePicker').val());
	if(sDate > eDate){
		$("#jsErrorDiv").append("<li>End Date must be greater than Start Date. Please select different one !!</li>");
		$("#jsErrorDiv").css("display","block");	
	    isValidate=false;
	}
	
	return isValidate;
}

function validateGameDataForm(){
	var isValidate=true;
	$(".small-tag-style-for-error").html('');
	$(".small-tag-style-for-error").css("display","none");
	$('select').removeClass('box-error-style');
	
	if($('#gameId').val()== -1 || $('#gameId').val()== "")
	{
	      $('#gameId').parent().parent().find(".small-tag-style-for-error").html("Please select Any Game !!");
	      $('#gameId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#gameId').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#gameTypeId').val()== -1 || $('#gameTypeId').val()== "")
	{
	      $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").html("Please select Any Game Type !!");
	      $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#gameTypeId').addClass("box-error-style");
	      isValidate=false;
	}
	
	return isValidate;
}

function validateSimnetGameDataForm(){
	var isValidate=true;
	$(".small-tag-style-for-error").html('');
	$('#errorforwinners').html('');
	$(".small-tag-style-for-error").css("display","none");
	$('select').removeClass('box-error-style');
	
	if($('#gameId').val()== -1 || $('#gameId').val()== "")
	{
	      $('#gameId').parent().parent().find(".small-tag-style-for-error").html("Please select Any Game !!");
	      $('#gameId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#gameId').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#gameTypeId').val()== -1 || $('#gameTypeId').val()== "")
	{
	      $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").html("Please select Any Game Type !!");
	      $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#gameTypeId').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#saleOnSimnet').val()== -1 || $('#saleOnSimnet').val()== ""|| !($('#saleOnSimnet').val().match(/^\d+$/)))
	{
	      $('#saleOnSimnet').parent().parent().find(".small-tag-style-for-error").html("Please Insert a Valid Winning Amount !!");
	      $('#saleOnSimnet').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#saleOnSimnet').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#noOfWinnersfor12').val()=="" || $('#noOfWinnersfor11').val()== "" || $('#noOfWinnersfor10').val()=="" || !($('#noOfWinnersfor12').val().match(/^\d+$/))|| !($('#noOfWinnersfor11').val().match(/^\d+$/))|| !($('#noOfWinnersfor11').val().match(/^\d+$/)))
	{
	      $('#errorforwinners').html("Please Insert Valid Number Of Winnners For All Match !!");
	      $('#errorforwinners').css("display","block");
	      $('#errorforwinners').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#simnetDrawsId').val()==-1)
	{
	      $('#simnetDrawsId').parent().parent().find(".small-tag-style-for-error").html("Please select a draw ");
	      $('#simnetDrawsId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#simnetDrawsId').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#simnetDrawsId').val()==-2)
	{
	      $('#simnetDrawsId').parent().parent().find(".small-tag-style-for-error").html("No Claim hold draws Exist for this Game Type");
	      $('#simnetDrawsId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#simnetDrawsId').addClass("box-error-style");
	      isValidate=false;
	}
	
	if (isValidate) {
		var success = confirm("Confirm ! want to submit SIMNET details");
		if (success) {
			isValidate=true;
		}
		else{
			isValidate=false;
		}
	}
	return isValidate;
}




function validateEventDataForm(){
	var isValidate=true;
	$(".small-tag-style-for-error").html('');
	$(".small-tag-style-for-error").css("display","none");
	$('select').removeClass('box-error-style');
	
	if($('#drawId').val()== -1 || $('#gameId').val()== "")
	{
	      $('#drawId').parent().parent().find(".small-tag-style-for-error").html("Please select Any Draw !!");
	      $('#drawId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#drawId').addClass("box-error-style");
	      isValidate=false;
	}	
	return isValidate;
}

function validateResultSubmissionForm(inputList){
	var isValidate=true;	
		$("input:radio").each(function(){
		  var name = $(this).attr("name");
		  if($("input:radio[name="+name+"]:checked").length == 0){
			  isValidate = false;
			  return isValidate;
		  }
		});
	return isValidate;
}

function validateResultSubmitForm(inputData) {
	var isValidate=true;
	var inputLength = parseFloat(inputData.length);
	if($('#evtListSize').val() > inputLength){
		isValidate = false;
	}else if($('#evtListSize').val() < inputLength){
		isValidate = false;
	}
	 return isValidate;
} 

function validateFetchReportRequestForm(){
	var isValidate=true;	
	$(".small-tag-style-for-error").html('');
	$(".small-tag-style-for-error").css("display","none");
	$(".small-tag-style-for-success").html('');
	$(".small-tag-style-for-success").css("display","none");
	$("#jsErrorDiv").html('');
	$("#jsErrorDiv").css("display","none");
	$('input').removeClass('box-error-style');
	$('select').removeClass('box-error-style');
	$('input').removeClass('box-success-style');
	$('select').removeClass('box-success-style');
	$("#resultDiv").empty();
		 
	if($('#gameId').val()== -1 || $('#gameId').val()== "")
	{
		  $('#gameId').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#gameId').parent().parent().find(".small-tag-style-for-error").html("Please select Game !!");
	      $('#gameId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#gameId').addClass("box-error-style");
	      isValidate=false;
	}else{
		$('#gameId').parent().parent().find(".small-tag-style-for-error").css("display","none");
		$('#gameId').parent().parent().find(".small-tag-style-for-success").html("Game selected !!");
	    $('#gameId').parent().parent().find(".small-tag-style-for-success").css("display","block");
	    $('#gameId').addClass("box-success-style");
	}
	
	if($('#gameTypeId').val()== -1 || $('#gameTypeId').val()== "")
	{
		  $('#gameTypeId').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").html("Please select Game Type!!");
	      $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#gameTypeId').addClass("box-error-style");
	      isValidate=false;
	}else{
		$('#gameTypeId').parent().parent().find(".small-tag-style-for-error").css("display","none");
		$('#gameTypeId').parent().parent().find(".small-tag-style-for-success").html("Type of Game selected !!");
	    $('#gameTypeId').parent().parent().find(".small-tag-style-for-success").css("display","block");
	    $('#gameTypeId').addClass("box-success-style");
	}
	
	if($('#startDateTimePicker').val()== "____/__/__")
	{
		  $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").html("Please select start date !!");
	      $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#startDateTimePicker').addClass("box-error-style");
	      isValidate=false;
	}else{
	      $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","none");
		  $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").html("Start date selected !!");
	      $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","block");
	      $('#startDateTimePicker').addClass("box-success-style");
	}
	
	if($('#endDateTimePicker').val()== "____/__/__")
	{
	  	  $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").html("Please select end date !!");
	      $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#endDateTimePicker').addClass("box-error-style");
	      isValidate=false;
	}else{
	      $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","none");
		  $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").html("end Date selected !!");
	      $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","block");
	      $('#endDateTimePicker').addClass("box-success-style");		
	}
	
	/*if($('#startDateTimePicker').val() == $('#endDateTimePicker').val() && $('#startDateTimePicker').val()!="____/__/__" && $('#endDateTimePicker').val()!="____/__/__"){
		$("#jsErrorDiv").append("<li>Start Date and End Date can't be same. Please select different one !!</li>");
		$("#jsErrorDiv").css("display","block");	
	    isValidate=false;
	}*/
	
	var sDate = new Date($('#startDateTimePicker').val());
	var eDate = new Date($('#endDateTimePicker').val());
	if(sDate > eDate){
		$("#jsErrorDiv").append("<li>End Date must be greater than Start Date. Please select different one !!</li>");
		$("#jsErrorDiv").css("display","block");	
	    isValidate=false;
	}
	
	if(isValidate == true){
		$('input').removeClass('box-error-style');
		$('select').removeClass('box-error-style');
		$('input').removeClass('box-success-style');
		$('select').removeClass('box-success-style');
		$(".small-tag-style-for-error").html('');
		$(".small-tag-style-for-error").css("display","none");
		$(".small-tag-style-for-success").html('');
		$(".small-tag-style-for-success").css("display","none");
	}
	return isValidate;
}

function validateResultApprovalForm(){
	var isValidate=true;	
	$(".small-tag-style-for-error").html('');
	$(".small-tag-style-for-error").css("display","none");
	$(".small-tag-style-for-success").html('');
	$(".small-tag-style-for-success").css("display","none");
	$("#jsErrorDiv").html('');
	$("#jsErrorDiv").css("display","none");
	$('input').removeClass('box-error-style');
	$('select').removeClass('box-error-style');
	$('input').removeClass('box-success-style');
	$('select').removeClass('box-success-style');
	$("#resultDiv").empty();
		 
	if($('#gameId').val()== -1 || $('#gameId').val()== "")
	{
		  $('#gameId').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#gameId').parent().parent().find(".small-tag-style-for-error").html("Please select Game !!");
	      $('#gameId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#gameId').addClass("box-error-style");
	      isValidate=false;
	}else{
		$('#gameId').parent().parent().find(".small-tag-style-for-error").css("display","none");
		$('#gameId').parent().parent().find(".small-tag-style-for-success").html("Game selected !!");
	    $('#gameId').parent().parent().find(".small-tag-style-for-success").css("display","block");
	    $('#gameId').addClass("box-success-style");
	}
	
	if($('#gameTypeId').val()== -1 || $('#gameTypeId').val()== "")
	{
		  $('#gameTypeId').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").html("Please select Game Type!!");
	      $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#gameTypeId').addClass("box-error-style");
	      isValidate=false;
	}else{
		$('#gameTypeId').parent().parent().find(".small-tag-style-for-error").css("display","none");
		$('#gameTypeId').parent().parent().find(".small-tag-style-for-success").html("Type of Game selected !!");
	    $('#gameTypeId').parent().parent().find(".small-tag-style-for-success").css("display","block");
	    $('#gameTypeId').addClass("box-success-style");
	}
	
	if($('#drawId').val()== "") {
		$('#drawId').val("");
	}
	if($('#startDateTimePicker').val()== "____/__/__") {
		$('#startDateTimePicker').val("");
	}
	if($('#endDateTimePicker').val()== "____/__/__") {
		$('#endDateTimePicker').val("");
	}
	
	if($('#startDateTimePicker').val()== "")
	{
		  $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").html("Please select start date !!");
	      $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#startDateTimePicker').addClass("box-error-style");
	      isValidate=false;
	}else{
	      $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","none");
		  $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").html("Start date selected !!");
	      $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","block");
	      $('#startDateTimePicker').addClass("box-success-style");
	}
	
	if($('#endDateTimePicker').val()== "")
	{
	  	  $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").html("Please select end date !!");
	      $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#endDateTimePicker').addClass("box-error-style");
	      isValidate=false;
	}else{
	      $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","none");
		  $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").html("end Date selected !!");
	      $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","block");
	      $('#endDateTimePicker').addClass("box-success-style");		
	}
/*	
	if($('#startDateTimePicker').val() == $('#endDateTimePicker').val() && $('#startDateTimePicker').val()!="____/__/__ __:__" && $('#endDateTimePicker').val()!="____/__/__ __:__"){
		$("#jsErrorDiv").append("<li>Start Date and End Date can't be same. Please select different one !!</li>");
		$("#jsErrorDiv").css("display","block");	
	    isValidate=false;
	    return isValidate;
	}*/
	
	var sDate = new Date($('#startDateTimePicker').val());
	var eDate = new Date($('#endDateTimePicker').val());
	if(sDate > eDate){
		$("#jsErrorDiv").append("<li>End Date must be greater than Start Date. Please select different one !!</li>");
		$("#jsErrorDiv").css("display","block");	
	    isValidate=false;
	}
	
	if(isValidate == true){
		$('input').removeClass('box-error-style');
		$('select').removeClass('box-error-style');
		$('input').removeClass('box-success-style');
		$('select').removeClass('box-success-style');
		$(".small-tag-style-for-error").html('');
		$(".small-tag-style-for-error").css("display","none");
		$(".small-tag-style-for-success").html('');
		$(".small-tag-style-for-success").css("display","none");
	}
	return isValidate;
}


function validateDrawMgmtForm(){
	var isValidate=true;	
	$(".small-tag-style-for-error").html('');
	$(".small-tag-style-for-error").css("display","none");
	$(".small-tag-style-for-success").html('');
	$(".small-tag-style-for-success").css("display","none");
	$("#jsErrorDiv").html('');
	$("#jsErrorDiv").css("display","none");
	$('input').removeClass('box-error-style');
	$('select').removeClass('box-error-style');
	$('input').removeClass('box-success-style');
	$('select').removeClass('box-success-style');
	$("#resultDiv").empty();
		 
	if($('#gameId').val()== -1 || $('#gameId').val()== "")
	{
		  $('#gameId').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#gameId').parent().parent().find(".small-tag-style-for-error").html("Please select Game !!");
	      $('#gameId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#gameId').addClass("box-error-style");
	      isValidate=false;
	}else{
		$('#gameId').parent().parent().find(".small-tag-style-for-error").css("display","none");
		$('#gameId').parent().parent().find(".small-tag-style-for-success").html("Game selected !!");
	    $('#gameId').parent().parent().find(".small-tag-style-for-success").css("display","block");
	    $('#gameId').addClass("box-success-style");
	}
	
	if($('#gameTypeId').val()== -1 || $('#gameTypeId').val()== "")
	{
		  $('#gameTypeId').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").html("Please select Game Type!!");
	      $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#gameTypeId').addClass("box-error-style");
	      isValidate=false;
	}else{
		$('#gameTypeId').parent().parent().find(".small-tag-style-for-error").css("display","none");
		$('#gameTypeId').parent().parent().find(".small-tag-style-for-success").html("Type of Game selected !!");
	    $('#gameTypeId').parent().parent().find(".small-tag-style-for-success").css("display","block");
	    $('#gameTypeId').addClass("box-success-style");
	}
	
	
	if($('#startDateTimePicker').val()== "____/__/__")
	{
		  $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").html("Please select start date !!");
	      $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#startDateTimePicker').addClass("box-error-style");
	      isValidate=false;
	}else{
	      $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","none");
		  $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").html("Start date selected !!");
	      $('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","block");
	      $('#startDateTimePicker').addClass("box-success-style");
	}
	
	if($('#endDateTimePicker').val()== "____/__/__")
	{
	  	  $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").html("Please select end date !!");
	      $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#endDateTimePicker').addClass("box-error-style");
	      isValidate=false;
	}else{
	      $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","none");
		  $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").html("end Date selected !!");
	      $('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","block");
	      $('#endDateTimePicker').addClass("box-success-style");		
	}
	
	var sDate = new Date($('#startDateTimePicker').val());
	var eDate = new Date($('#endDateTimePicker').val());
	if(sDate > eDate){
		$("#jsErrorDiv").append("<li>End Date must be greater than Start Date. Please select different one !!</li>");
		$("#jsErrorDiv").css("display","block");	
	    isValidate=false;
	}

	
	if(isValidate == true){
		$('input').removeClass('box-error-style');
		$('select').removeClass('box-error-style');
		$('input').removeClass('box-success-style');
		$('select').removeClass('box-success-style');
		$(".small-tag-style-for-error").html('');
		$(".small-tag-style-for-error").css("display","none");
		$(".small-tag-style-for-success").html('');
		$(".small-tag-style-for-success").css("display","none");
	}
	return isValidate;
}




function validateDrawListForm(){
	var isValidate=true;
	$("#jsErrorDiv2").html('');
	$("#jsErrorDiv2").css("display","none");
		  if($("input:radio[name=drawId]:checked").length == 0){
			  $("#jsErrorDiv2").append("<li>Select atleast one draw !!</li>");
			  $("#jsErrorDiv2").css("display","block");
			  var scrollPos = $('#drawTableDiv').offset().top;
			  $(window).scrollTop(scrollPos);
			  isValidate = false;
		  }
	return isValidate;
}

function validateApprovedResultSubmissionForm(){
	
	var isValidate=true;
	$("#jsErrorDiv").html('');
	$("#jsErrorDiv").css("display","none");	
	if($("input:radio[name=userResult]:checked").length == 0){
		  $("#jsErrorDiv").append("<li>Select atleast one result (Of First User / Second User / Yours) !!</li>");
		  $("#jsErrorDiv").css("display","block");
		  var scrollPos = $('#approvedResultDiv').offset().top;
		  $(window).scrollTop(scrollPos);
		  isValidate = false;
		  return isValidate;
	 }
	
	if($('input[name=userResult]:checked', '#approvalForm').val() == 3){
		$("input:radio").each(function(){
			  var name = $(this).attr("name");
			  if($("input:radio[name="+name+"]:checked").length == 0){
				  $("#jsErrorDiv").append("<li>please select all events Result!!</li>");
				  $("#jsErrorDiv").css("display","block");
				  var scrollPos = $('#approvedResultDiv').offset().top;
				  $(window).scrollTop(scrollPos);
				  isValidate = false;
				  return isValidate;
			  }
			});
	}

	var resultVal="";
	var inputList = document.getElementsByTagName('input');
	for(var i=0; i<inputList.length; i++) {
		if (inputList[i].type == 'radio' && inputList[i].name.match('opt')) {
			var elem = inputList[i];
			if(elem.checked == true) {
				resultVal += elem.name.substring(3)+"_"+elem.value+",";
			}
		}
	}
	$('#eventResult').val(resultVal);

	return isValidate;
}


function validatePlayerInfoForm(){
	var isValidate=true;
	$(".small-tag-style-for-error").html('');
	$(".small-tag-style-for-error").css("display","none");
	$("#jsErrorDiv").html('');
	$("#jsErrorDiv").css("display","none");
	$('input').removeClass('box-success-style');
	$('select').removeClass('box-success-style');
	$('input').removeClass('box-error-style');
	$('select').removeClass('box-error-style');
	if($('#firstName').val() == "")
	{
		  $('#firstName').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#firstName').parent().parent().parent().find(".small-tag-style-for-error").html("Please Enter First Name !!");
	      $('#firstName').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#firstName').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#lastName').val() == "")
	{
		  $('#lastName').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#lastName').parent().parent().parent().find(".small-tag-style-for-error").html("Please Enter Last Name !!");
	      $('#lastName').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#lastName').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#emailId').val() == "" || !isValidEmailAddress($('#emailId').val()))
	{
		  $('#emailId').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#emailId').parent().parent().parent().find(".small-tag-style-for-error").html("Please Enter valid Email Id !!");
	      $('#emailId').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#emailId').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#phoneNumber').val() == "")
	{
		  $('#phoneNumber').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#phoneNumber').parent().parent().parent().find(".small-tag-style-for-error").html("Please Enter Phone Number !!");
	      $('#phoneNumber').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#phoneNumber').addClass("box-error-style");
	      isValidate=false;
	}

	if(!isValidMobileNumber($('#phoneNumber').val()))
	{
		  $('#phoneNumber').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#phoneNumber').parent().parent().parent().find(".small-tag-style-for-error").html("Enter Valid Phone Number !!");
	      $('#phoneNumber').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#phoneNumber').addClass("box-error-style");
	      isValidate=false;
	}

	if($('#address1').val() == "")
	{
		  $('#address1').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#address1').parent().parent().parent().find(".small-tag-style-for-error").html("Please Enter Address !!");
	      $('#address1').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#address1').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#idNumber').val() == "")
	{
		  $('#idNumber').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#idNumber').parent().parent().parent().find(".small-tag-style-for-error").html("Please Enter ID Number !!");
	      $('#idNumber').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#idNumber').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#remark').val() == "")
	{
		  $('#remark').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#remark').parent().parent().parent().find(".small-tag-style-for-error").html("Please Enter Remark !!");
	      $('#remark').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#remark').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#city').val() == "" || $('#city').val() == -1)
	{
		  $('#city').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#city').parent().parent().parent().find(".small-tag-style-for-error").html("Please Select City !!");
	      $('#city').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#city').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#state').val() == "" || $('#state').val() == -1)
	{
		  $('#state').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#state').parent().parent().parent().find(".small-tag-style-for-error").html("Please Select State !!");
	      $('#state').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#state').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#country').val() == "" || $('#country').val() == -1)
	{
		  $('#country').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#country').parent().parent().parent().find(".small-tag-style-for-error").html("Please Select Country !!");
	      $('#country').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#country').addClass("box-error-style");
	      isValidate=false;
	}
	
	if($('#idType').val() == "" || $('#idType').val() == -1)
	{
		  $('#idType').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#idType').parent().parent().parent().find(".small-tag-style-for-error").html("Please Select ID Type !!");
	      $('#idType').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#idType').addClass("box-error-style");
	      isValidate=false;
	}
	
	return isValidate;
}


function validateTicketNumber() {
	
	var isValidate=true;
	$(".small-tag-style-for-error").html('');
	$("#resultDiv").html('');
	$(".small-tag-style-for-error").css("display","none");
	
	var ticketNumber=$('#ticketNumber').val();
	$("#ticketNumberError").html('');
	$("#ticketNumberError").css("display","none");
	if(ticketNumber=="" || ticketNumber==undefined){
	 $("#ticketNumberError").append("Please Enter Ticket Number !!");
	  $("#ticketNumberError").css("display","block");
	  return false;
	}
	var pattern = new RegExp(/^[0-9]{16,20}$/);
    if(pattern.test(ticketNumber)==false){
    	$("#ticketNumberError").append("Please Enter Correct Ticket Number !!");
  	    $("#ticketNumberError").css("display","block");
  	    $('#ticketNumber').val('');   
  	    return false;
    }
    return true;
}

function validateTicketNumberWithReason() {
	
	var isValidate=true;
	$(".small-tag-style-for-error").html('');
	$("#resultDiv").html('');
	$(".small-tag-style-for-error").css("display","none");
	var ticketNumber=$('#ticketNumber').val();
	var reasonForCancel=$('#reasonForCancel').val();
	$("#ticketNumberError").html('');
	$("#ticketNumberError").css("display","none");
	$("#reasonError").html('');
	$("#reasonError").css("display","none");
	if(ticketNumber=="" && reasonForCancel==-1){
		 $("#ticketNumberError").append("Please Enter Ticket Number !!");
		 $("#reasonError").append("Please Enter Reason !!");
    	 $("#reasonError").css("display","block");
		  $("#ticketNumberError").css("display","block");
		  return false;
	}
	if(ticketNumber=="" || ticketNumber==undefined){
	 $("#ticketNumberError").append("Please Enter Ticket Number !!");
	  $("#ticketNumberError").css("display","block");
	  return false;
	}
	 if(reasonForCancel==-1){
    	 $("#reasonError").append("Please Enter Reason !!");
    	 $("#reasonError").css("display","block");
    	 return false;
    }
	var pattern = new RegExp(/^[0-9]{16,20}$/);
    if(pattern.test(ticketNumber)==false){
    	$("#ticketNumberError").append("Please Enter Correct Ticket Number !!");
  	    $("#ticketNumberError").css("display","block");
  	    $('#ticketNumber').val('');   
  	    return false;
    }
    return true;
}

function validatedResultSubmissionUserAssignForm(){

	var isValidate=true;
	$("#jsErrorDiv").html('');
	$("#jsErrorDiv").css("display","none");	

	if($('#gameId').val()== -1 || $('#gameId').val()== "")
	{
		  $('#gameId').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#gameId').parent().parent().find(".small-tag-style-for-error").html("Please select Game !!");
	      $('#gameId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#gameId').addClass("box-error-style");
	      isValidate=false;
	}else{
		$('#gameId').parent().parent().find(".small-tag-style-for-error").css("display","none");
		$('#gameId').parent().parent().find(".small-tag-style-for-success").html("Game selected !!");
	    $('#gameId').parent().parent().find(".small-tag-style-for-success").css("display","block");
	    $('#gameId').addClass("box-success-style");
	}
	
	if($('#gameTypeId').val()== -1 || $('#gameTypeId').val()== "")
	{
		  $('#gameTypeId').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").html("Please select Game Type!!");
	      $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#gameTypeId').addClass("box-error-style");
	      isValidate=false;
	}else{
		$('#gameTypeId').parent().parent().find(".small-tag-style-for-error").css("display","none");
		$('#gameTypeId').parent().parent().find(".small-tag-style-for-success").html("Type of Game selected !!");
	    $('#gameTypeId').parent().parent().find(".small-tag-style-for-success").css("display","block");
	    $('#gameTypeId').addClass("box-success-style");
	}

	var userListString = "";

	var count = 0;
	var arr = new Array();
	for(var i=0; i<5; i++){
		var obj = $("#user"+(i+1));
		if(obj != null){
			var val = obj.val();
			arr[i] = val;
			userListString += val+",";
		}
	}
	userListString = userListString.substring(0, userListString.length-1);
	$("#userListString").val(userListString);
	
	for(var i=0; i<(arr.length); i++){
		if(arr[i] != -1){
			count = count + 1;
		}
	}
	
	if(count <=1){
		 $("#jsErrorDiv").append("<li>please select atleast two users!!</li>");
		 $("#jsErrorDiv").css("display","block");
		return false;
	}
	
	for(var i=0; i<(arr.length); i++){
		if(arr[i] != -1){
			for(var j=(i+1); j<arr.length; j++){
				if(arr[i] == arr[j]){
					$("#jsErrorDiv").append("<li>please select different users!!</li>");
					 $("#jsErrorDiv").css("display","block");
					return false;
				}
			}
		}
	}	
	return isValidate;
}

function validateFixtureWiseWinningAnalysisReportRequestForm() {
	var isValidate=true;	
	$(".small-tag-style-for-error").html('');
	$(".small-tag-style-for-error").css("display","none");
	$(".small-tag-style-for-success").html('');
	$(".small-tag-style-for-success").css("display","none");
	$("#jsErrorDiv").html('');
	$("#jsErrorDiv").css("display","none");
	$('input').removeClass('box-error-style');
	$('select').removeClass('box-error-style');
	$('input').removeClass('box-success-style');
	$('select').removeClass('box-success-style');
		 
	if($('#gameId').val()== -1 || $('#gameId').val()== ""){
		  $('#gameId').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#gameId').parent().parent().find(".small-tag-style-for-error").html("Please select Game !!");
	      $('#gameId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#gameId').addClass("box-error-style");
	      isValidate=false;
	}else{
		$('#gameId').parent().parent().find(".small-tag-style-for-error").css("display","none");
		$('#gameId').parent().parent().find(".small-tag-style-for-success").html("Game selected !!");
	    $('#gameId').parent().parent().find(".small-tag-style-for-success").css("display","block");
	    $('#gameId').addClass("box-success-style");
	}
	
	if($('#gameTypeId').val()== -1 || $('#gameTypeId').val()== "")
	{
		  $('#gameTypeId').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").html("Please select Game Type!!");
	      $('#gameTypeId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#gameTypeId').addClass("box-error-style");
	      isValidate=false;
	}else{
		$('#gameTypeId').parent().parent().find(".small-tag-style-for-error").css("display","none");
		$('#gameTypeId').parent().parent().find(".small-tag-style-for-success").html("Type of Game selected !!");
	    $('#gameTypeId').parent().parent().find(".small-tag-style-for-success").css("display","block");
	    $('#gameTypeId').addClass("box-success-style");
	}

	if($('#drawId').val()== -1 || $('#drawId').val()== "")
	{
		  $('#drawId').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#drawId').parent().parent().find(".small-tag-style-for-error").html("Please select Draw!!");
	      $('#drawId').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#drawId').addClass("box-error-style");
	      isValidate=false;
	}else{
		$('#drawId').parent().parent().find(".small-tag-style-for-error").css("display","none");
		$('#drawId').parent().parent().find(".small-tag-style-for-success").html("Draw selected !!");
	    $('#drawId').parent().parent().find(".small-tag-style-for-success").css("display","block");
	    $('#drawId').addClass("box-success-style");
	}

	if($('#selectedDateTimePicker').val()== "____/__/__")
	{
		  $('#selectedDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      $('#selectedDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").html("Please select date !!");
	      $('#selectedDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      $('#selectedDateTimePicker').addClass("box-error-style");
	      isValidate=false;
	}else{
	      $('#selectedDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","none");
		  $('#selectedDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").html("Date selected !!");
	      $('#selectedDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","block");
	      $('#selectedDateTimePicker').addClass("box-success-style");
	}

	if(isValidate == true){
		$('input').removeClass('box-error-style');
		$('select').removeClass('box-error-style');
		$('input').removeClass('box-success-style');
		$('select').removeClass('box-success-style');
		$(".small-tag-style-for-error").html('');
		$(".small-tag-style-for-error").css("display","none");
		$(".small-tag-style-for-success").html('');
		$(".small-tag-style-for-success").css("display","none");
	}
	return isValidate;
}

function validateUpdateFreeze() {
	var sec=$('#seconds').val();


	if ($('#logoutAllRet').is(":checked")) 
	{
		var pattern=new RegExp(/^-{0,1}\d+$/);
		if(pattern.test(sec) && sec!=0){
			$("#jsErrorDiv2").css("display","none");
			return true;
		}else{
			 $("#jsErrorDiv2").css("display","block");
			$('#jsErrorDiv2').html('Please enter correct time!!');
			return false;
		}
	}
	else{
		 $("#jsErrorDiv2").css("display","block");
		$('#jsErrorDiv2').html('Please select retailer logout confirmation!!');
		return false;
	}
}


/* General Validations */

function isValidEmailAddress(emailAddress) {
    var pattern = new RegExp(/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\.[a-zA-Z]{2,4}[.a-zA-z]{0,4}$/i);
    return pattern.test(emailAddress);
};

function isValidMobileNumber(mobileNum) {
	pattern = new RegExp(/^[1-9]{1}[0-9]{9}$/);
    return pattern.test(mobileNum);
};

function isVerificationCodeValid(){
	if($("#verificationCode").val().trim().length != 7 || isNaN($("#verificationCode").val())){
		$("#verficationCodeError").html("Please enter valid verification code!!");
		$("#verficationCodeError").css("display","block");
		return false;
	} else{
		$("#verficationCodeError").html("");
		$("#verficationCodeError").css("display","none");
		return true;
	}
}






