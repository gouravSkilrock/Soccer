var _id = {o:function (el_id) {
	return document.getElementById(el_id);
}, n:function (el_id) {
	return document.getElementsByName(el_id);
}, tn:function (el_id) {
	return document.getElementsByTagName(el_id);
}, i:function (el_id, msg, e) {
	if (e) {
		msg = "<font color = 'red'>" + msg + "</font>";
	}
	this.o(el_id).innerHTML = msg;
}, v:function (el_id, r) {
	if (r) {
		return this.o(el_id).value == "";
	}
	return this.o(el_id).value;
}, iniFocus:function (el_id) {
	this.o(el_id).focus();
}, blk:function (el_id) {
	this.o(el_id).style.display = "block";
}, non:function (el_id) {
	this.o(el_id).style.display = "none";
}, hid:function (el_id) {
	this.o(el_id).style.visibility = "hidden";
}, vis:function (el_id) {
	this.o(el_id).style.visibility = "visible";
}, fo:function (f, ele) {
	return document.forms[f].elements[ele];
}, fi:function (f, ele, msg, e) {
	if (e) {
		msg = "<font color = 'red'>" + msg + "</font>";
	}
	this.fo(f, ele).innerHTML = msg;
}, fv:function (f, ele, r) {
	if (r) {
		return this.fo(f, ele).value == "";
	}
	return this.fo(f, ele).value;
}, finiFocus:function (f, ele) {
	this.fo(f, ele).focus();
}};

function _ajaxCallJson(actionName, param,divId) {
	var _resp;
	$.ajax( {
		type : "POST",
		url : actionName,
		data : param,
		datatype: "application/json",
		async : false,
		beforeSend: function() {
			//$("#resultDiv").html('<img src="images/backOffice/ui/ajax-loader_blue.gif" />');
			$("#submit").hide();
		},
        complete: function() {
			$("#submit").show();
		},
		success : function(result, arguments, text) {
			if (arguments == 'success') {
				var data = text.responseText;
				 _resp = JSON.parse(data);
				
			} else {
				_resp = 'error';
			}
		},
	    error: function(xhr, status, text) {
	    	var responseStatus=xhr.status;
	    	//alert(responseStatus);
	    	if(responseStatus == 440){
	    		window.open(projectName+"/com/skilrock/pms/backOffice/commonMethod/Action/applicationExceptionAction.action?errorMessage="+$(result).find("#errorMessage").text(), target="_top");
	    	}
	    	
	    	//window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
	        //var response = $.parseJSON(xhr.responseText);

	        console.log('Failure!');

	        //if (response) {
	          //  console.log(response.error);
	       // } else {
	            // This would mean an invalid response from the server - maybe the site went down or whatever...
	        //}
	        _resp = false;
	    }
	});
	 
	return _resp;
}

function _ajaxCallText(actionName, param) {
	var _resp;
	$.ajax( {
		type : "POST",
		url : actionName,
		data : param,
		async : false,
		success : function(result, arguments, text) {
			if (arguments == 'success') {
				_resp = text.responseText;
			} else {
				_resp = 'error';
			}

		}
	});
	return _resp;
}

function _ajaxCallDiv(actionName,param,divId) {
	$.ajax( {
		type : "POST",
		url : actionName,
		data : param,
		async : false,
		success : function(result) {
		//alert(result);
		if(result.match("ajaxExceptionOccurHandled.action")){
			//alert($(result).filter("#errorMessage").text());
		window.open(projectName+"/com/skilrock/pms/backOffice/commonMethod/Action/applicationExceptionAction.action?errorMessage="+$(result).find("#errorMessage").text(), target="_top");
		}else
			$("#"+divId).html(result);
		
		}
	});
}

function formSubmit(formId,divId,event){
	//alert("ddd"+formId);
	$("#mainDiv").hide();
	var form=$('#'+formId);
	//alert(form.attr('action'));
	$.ajax( {
		type : "POST",
		url : form.attr('action'),
		data : form.serialize(),
		//async : false,
		beforeSend: function() {
			//$("#resultDiv").html('<img src="'+projectName+'/images/backOffice/ui/ajax-loader_blue.gif?val="'+new Date()+' />');
			//$("#submit").hide();
			$("#resultDiv").html($("#loadingDiv").html());
			$("#resultDiv").css("text-align","center");
			//$("#loadingDiv").show();
		},
        complete: function() {
			//$("#submit").show();
			$("#loadingDiv").hide();
		},
		success : function(result) {
		//alert(result);
		//alert($(result).find('#errorMessage').text());
		//alert($(result).filter("#errorMessage").text());
		//alert($(result).find(".result-heading-new").text());
		if(result.match("ajaxExceptionOccurHandled.action")){
			//alert($(result).filter("#errorMessage").html());
			//alert($(result).filter("#errorMessage").text());
		window.open(projectName+"/com/skilrock/pms/backOffice/commonMethod/Action/applicationExceptionAction.action?errorMessage="+$(result).find("#errorMessage").text(), target="_top");
		}else
			$("#"+divId).html(result);
		}
	});
	return false;
}

function _ajaxCallForReports(url,targetDiv,currElemId){
	 var childElem = document.getElementById(currElemId);
	 var parentElem = childElem.parentNode;
    var noOfChildNode = parentElem.getElementsByTagName("tr").length;
	var _respData = _ajaxCallText(url, "");
   if(_respData == "success") {
   	parentElem.removeChild(childElem);

   }
}