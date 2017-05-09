<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
  <head>
  <base href="<%=basePath%>">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Free coming soon template with jQuery countdown">
    <meta name="author" content="http://bootstraptaste.com">
    <link rel="shortcut icon" href="assets/img/favicon.png">

    <title>Sports Lottery | Comming Soon</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap/bootstrap-theme.css">
    
	

    <!-- siimple style -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/commingsoonpage/style.css">
    
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

	<div id="wrapper">
		<div class="container">
			<div class="row">
				<div class="col-sm-12 col-md-12 col-lg-12">
					<h1>Hello User...</h1>
					<h2 class="subtitle"><b>You have landed on the wrong page !!</b></h2>
				</div>
				
			</div>
			
			<div class="row ">
				<div class="col-md-4  ">
				</div>
				<!--  <div class="col-md-2  alert alert-info">
					<a href="<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() +"/LMSLinuxNew"%>"><b>Go to LMS</b></a>
				</div>
				
				<div class=" col-md-2   alert alert-info">
					<a href="<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() +"/PMS"%>"><b>Go to PMS</b></a>
				</div> -->
				
			</div>
			<div class="row">
				<div class="col-lg-6 col-lg-offset-3">
						<p class="copyright">Copyright &copy; 2015</p>
				</div>
			</div>		
		</div>
	</div>
    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/bootstrap/bootstrap.min.js"></script>
	
  </body>
</html>

