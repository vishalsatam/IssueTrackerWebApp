<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
<link href="${pageContext.request.contextPath}/resources/css/workarea.css" rel="stylesheet" >
</head>

<body>
	<div id ="wrapper" class="container-fluid">
	


		<%@ include file="admin_controls.jsp" %>
	   <div id="workarea">
	   	<form action="viewupdateuser.htm" commandName="userToBeSaved" method="POST">
	   		<input type="hidden" name="userIdOfUser" value="${userToBeDisplayed.userId}"/>
	   		<c:set value="${userToBeDisplayed}" var="userToBeSaved" scope="request" ></c:set>
	   		<div>
	   			<h4>User Information</h4>
	   			First Name<input type="text" name="firstName" value="${userToBeDisplayed.firstName}" }/><br/>
	   			Last Name <input type="text" name="lastName" value="${userToBeDisplayed.lastName}"/><br/>
	   			Email<input type="text" name="emailId" value="${userToBeDisplayed.emailId}"/><br/>
	   			Phone Number <input type="text" name="phoneNumber" value="${userToBeDisplayed.phoneNumber}"/><br/>
	   		</div>
	   		<div>
	   			<h4>Account Information</h4>
	   			UserName <input type="text" name="username" value="${userToBeDisplayed.account.username}"/><br/>
	   			Password <input type="password" name="password" value="${userToBeDisplayed.account.password}"/><br/>
	   			Status <input type = "text" name="status" value="${userToBeDisplayed.account.status}"/><br/>
	   			Role
	   			<c:if test="${not empty userToBeDisplayed}">
	   				<select disabled = "disabled" name="role">
	   					<option value="${userToBeDisplayed.role}">${fn:toUpperCase(userToBeDisplayed.role)}</option>
	   				</select>
	   			</c:if>
	   			
	   			<c:if test="${empty userToBeDisplayed}">
	   				
	   				<select name="role">
	   					<option value="analyst">Analyst</option>
	   					<option value="manager">Manager</option>
	   					<option value="customer">Customer</option>
	   					<option value="admin">Admin</option>
	   				</select>	   			
	   			</c:if>
	   			

	  		</div>
	  		<div>
	  			<c:set value="${userToBeDisplayed}" var="userToBeDisplayed" scope="request" ></c:set>
	  			<input type="submit" value="Save" class="btn btn-primary"/> ${updateStatus}
	  		</div>
		</form>
	   </div>

	</div>
</body>
</html>