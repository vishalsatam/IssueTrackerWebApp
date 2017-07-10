
<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
<link href="${pageContext.request.contextPath}/resources/css/interface.css" rel="stylesheet" >
</head>

<body>
	<div id ="wrapper" class="container-fluid">

		
	   <div id="loginWrapper">
	   	  <h6 class="displayError">${loginError}</h6>
	      <form id="loginForm" method='POST' action='login.htm'>
	      <input type="text" required class="form-control inputText" placeholder="User Name" name="username"/>
	      <input type="password"  required placeholder = "Password" class="form-control inputText" name="password" />
	      <input type="submit" onclick="return validateForm();" class="form-control btn-primary" value="Login"/>
	      </form>
	   </div>

	</div>
</body>
</html>