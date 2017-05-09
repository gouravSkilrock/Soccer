$('#startDateTimePicker').datetimepicker({
	mask:'9999/19/39 29:59',
	step:5,
	//format:'d/m/Y HH:mm',
	//formatDate:'Y/m/d HH:mm'
});
$('#endDateTimePicker').datetimepicker({
	mask:'9999/19/39 29:59',
	step:5,
	//format:'d/m/Y HH:mm',
	//formatDate:'Y/m/d HH:mm'
});
$('#endDateTimePickerDiv').click(function(){
	$('#endDateTimePicker').datetimepicker('show');
});
$('#startDateTimePickerDiv').click(function(){
	$('#startDateTimePicker').datetimepicker('show');
});

		/*$('#venue').change(function(){
			if($(this).val()!=-1){
				$('#venue').parent().parent().find(".small-tag-style-for-error").html('');
	      		$('#venue').parent().parent().find(".small-tag-style-for-error").css("display","none");
	      		$('#venue').removeClass("box-error-style");
				$('#venue').parent().parent().find(".small-tag-style-for-success").html("venue selected !!");
	      		$('#venue').parent().parent().find(".small-tag-style-for-success").css("display","block");
	      		$('#venue').addClass("box-success-style");
			}else{
				$('#venue').parent().parent().find(".small-tag-style-for-success").html('');
	      		$('#venue').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      		$('#venue').removeClass("box-success-style");
	      		
				$('#venue').parent().parent().find(".small-tag-style-for-error").html("Please select venue !!");
	      		$('#venue').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      		$('#venue').addClass("box-error-style");
	    	}
		});
		
		$('#homeTeam').change(function(){
			if($(this).val()!=-1){
				$('#homeTeam').parent().parent().find(".small-tag-style-for-error").html('');
	      		$('#homeTeam').parent().parent().find(".small-tag-style-for-error").css("display","none");
	      		$('#homeTeam').removeClass("box-error-style");
	      		$('#homeTeam').parent().parent().find(".small-tag-style-for-success").html("Home Team selected !!");
	      		$('#homeTeam').parent().parent().find(".small-tag-style-for-success").css("display","block");
	      		$('#homeTeam').addClass("box-success-style");
			}else{
			    $('#homeTeam').parent().parent().find(".small-tag-style-for-success").html('');
	      		$('#homeTeam').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      		$('#homeTeam').removeClass("box-success-style");
				$('#homeTeam').parent().parent().find(".small-tag-style-for-error").html("Please select venue !!");
	      		$('#homeTeam').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      		$('#homeTeam').addClass("box-error-style");
	    	}
		});
		
		$('#awayTeam').change(function(){
			if($(this).val()!=-1){
				$('#awayTeam').parent().parent().find(".small-tag-style-for-error").html('');
	      		$('#awayTeam').parent().parent().find(".small-tag-style-for-error").css("display","none");
	      		$('#awayTeam').removeClass("box-error-style");
	      		$('#awayTeam').parent().parent().find(".small-tag-style-for-success").html("Away Team selected !!");
	      		$('#awayTeam').parent().parent().find(".small-tag-style-for-success").css("display","block");
	      		$('#awayTeam').addClass("box-success-style");
			}else{
			    $('#awayTeam').parent().parent().find(".small-tag-style-for-success").html('');
	      		$('#awayTeam').parent().parent().find(".small-tag-style-for-success").css("display","none");
	      		$('#awayTeam').removeClass("box-success-style");
				$('#awayTeam').parent().parent().find(".small-tag-style-for-error").html("Please select venue !!");
	      		$('#awayTeam').parent().parent().find(".small-tag-style-for-error").css("display","block");
	      		$('#awayTeam').addClass("box-error-style");
	    	}
		});
		
		$('#startDateTimePicker').blur(function(){
			if($(this).val()!=-1){
				$('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").html('');
	      		$('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","none");
	      		$('#startDateTimePicker').removeClass("box-error-style");
	      		$('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").html("Start Date Selected !!");
	      		$('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","block");
	      		$('#startDateTimePicker').addClass("box-success-style");
			}else{
			$('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").html('');
	      		$('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      		$('#startDateTimePicker').removeClass("box-success-style");
				$('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").html("Please select venue !!");
	      		$('#startDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      		$('#startDateTimePicker').addClass("box-error-style");
	    	}
		});
		
		$('#endDateTimePicker').change(function(){sleEventInsertion
			if($(this).val()!=-1){
				$('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").html('');
	      		$('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","none");
	      		$('#endDateTimePicker').removeClass("box-error-style");
	      		$('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").html("End Date Selected !!");
	      		$('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","block");
	      		$('#endDateTimePicker').addClass("box-success-style");
			}else{
			$('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").html('');
	      		$('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-success").css("display","none");
	      		$('#endDateTimePicker').removeClass("box-success-style");
				$('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").html("Please select venue !!");
	      		$('#endDateTimePicker').parent().parent().parent().find(".small-tag-style-for-error").css("display","block");
	      		$('#endDateTimePicker').addClass("box-error-style");
	    	}
		});*/