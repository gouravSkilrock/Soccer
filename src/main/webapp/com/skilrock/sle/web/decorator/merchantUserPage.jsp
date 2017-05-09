<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@page import="com.skilrock.sle.common.Util"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Expires", "-1");
	String path = request.getContextPath();
	/*String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";*/
	
%>

<!DOCTYPE html>

<html lang="en-US">
<head>
	<s:set name="contextPath"
			value="#request.get('javax.servlet.include.context_path')"
			scope="session" />
		<title><decorator:title /> Welcome to Sports Lottery
		</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
    <meta charset="UTF-8"/>
    
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="ThemeStarz">
    <!-- <link href='http://fonts.googleapis.com/css?family=Roboto:300,400,700' rel='stylesheet' type='text/css'>-->
     <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/style.css" type="text/css">
    <link href="<%=request.getContextPath()%>/assets/fonts/font-awesome.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/bootstrap/css/bootstrap.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/mycss.css" type="text/css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/bootstrap-select.min.css" type="text/css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/jquery.slider.min.css" type="text/css">   
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/AdminLTE.css" type="text/css">
	
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery-2.1.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/smoothscroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/bootstrap-select.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/custom.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery/ajaxCall.js"></script>
<Script ype="text/javascript" src="<%=request.getContextPath()%>/js/validation.js?version=3.0"></script>
<script type="text/javascript" src="https://code.jquery.com/ui/1.11.4/jquery-ui.js"></script> 

	<decorator:head />
</head>



<body class="page-sub-page navigation-fixed-top page-invoice" id="page-top">

<div id="loadingDiv" style="display:none"><img src="<%=request.getContextPath()%>/images/backOffice/ui/ajax-loader_blue.gif" /></div>

<!-- Wrapper -->
<div class="wrapper">
    <!-- Navigation -->
    <div class="navigation">	 
		
		<!-- START TABS CONTENT -->
		<div class="tab-content">
		


		<!-- SCRATCH -->
		<div id="se" class="tab-pane fade in active">
			 <div class="menu-div-style">
        			<div class="container">
           				 <header class="navbar navbar-style" id="top" role="banner">
						<nav class="collapse navbar-collapse bs-navbar-collapse navbar-right" role="navigation">
                    		<ul class="nav navbar-nav" id="navHeader">
                   			</ul>
                		</nav><!-- /.navbar collapse-->
					</header>
				</div>
    			</div>
		</div><!-- END SE TAB -->		

	</div><!-- tab content -->
		
				   	   
		
		
		
		
	<!-- <div class="col-md-1"></div><div class="col-md-10">
        <div class="box box-info">
            <div class="box-header" style="cursor: move;">
                <i class="fa fa-gear"></i>
                    <h3 class="box-title">{pageTitle}</h3>
            </div>
            <div class="box-body">
                <form method="post" action="#">
				
					
				</form>
            </div>
            <div class="box-footer clearfix">
                
            </div>
        </div>
    </div>-->
</div>
<decorator:body></decorator:body>
    <!-- end Page Content -->

</div>




<script>

	$(document).ready(function(){					
		var scrollPos = $('.menu-div-style').offset().top;
		//alert(scrollPos);
		$(window).scrollTop(25);
		$(document).bind('contextmenu', function (e) {
  			e.preventDefault();
 		 	//alert('Right Click is not allowed');
		});
		createMerchantUserMenu();
	});




  function createMerchantUserMenu(){
		var data=$.parseJSON('<s:text name="%{#session.MENU_DATA}"/>');
		var mainli;
		var subUl;
		var menuItemsli;
		//console.log(data);
        	$.each(data, function(index, value){
  				mainli = $('<li class="active"><a style="cursor:pointer">'+value.menuName+'</a>');
  				subUl =  $('<ul class="child-navigation">');
   				$.each(value.menuItems, function(index1, value1){
     					$.each(value1.actionItems, function(index2, value2){
     					<%if(Util.getPropertyValue("COUNTRY_DEPLOYED").equals("GHANA")){%>
     						if(value1.menuItemId == 4 && value1.menuItemName =="Result Submission"){
     						    menuItemsli = $('<li ><a href=<%=request.getContextPath()%>'+value2.actionName+' target ="_blank">'+value1.menuItemName+'</a><li>');
     						}
     						else{
   						        menuItemsli = $('<li ><a href="<%=request.getContextPath()%>'+value2.actionName+'">'+value1.menuItemName+'</a><li>');
   						    }
   						    subUl.append(menuItemsli);
   						<%}else{%>
   							 menuItemsli = $('<li ><a href="<%=request.getContextPath()%>'+value2.actionName+'">'+value1.menuItemName+'</a><li>');
   						 	 subUl.append(menuItemsli);
   						<%}%>
   								
	 							});     						
   				});
   				subUl.append('</ul>');
   				mainli.append(subUl);
   				mainli.append('</li>');
   				$('#navHeader').append(mainli);
			});
		mainli.append('<li></li>');    
  }
</script>

<!--[if gt IE 8]>
<script type="text/javascript" src="assets/js/ie.js"></script>
<![endif]-->
</body>
</html>

