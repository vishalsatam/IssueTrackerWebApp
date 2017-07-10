<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
<link href="${pageContext.request.contextPath}/resources/css/workarea.css" rel="stylesheet" >
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
</head>

<body>
	<div id ="wrapper" class="container-fluid">
	
		<c:if test="${authorization == 'admin'}">
	   <%@ include file = "../admin/admin_controls.jsp" %>
	   </c:if>
	   <c:if test="${authorization == 'analyst'}">
	   <%@ include file = "../analyst/analyst_controls.jsp" %>
	   </c:if>
	   <c:if test="${authorization == 'customer'}">
	   <%@ include file = "../customer/customer_controls.jsp" %>
	   </c:if>
	   <c:if test="${authorization == 'manager'}">
	   <%@ include file = "../manager/manager_controls.jsp" %>
	   </c:if>

	   <div id="workarea">
	   	<form:form action="profile.htm" commandName="userToBeSaved" method="POST">
	   		<input type="hidden" name="useridtodisplay" value="${useridtodisplay}"/>
	   		
	   		<div class="userdetailsdisplay col-lg-12">
	   			
	   			<h4 class="col-lg-12">User Information</h4>
	   			<label for="firstName" class="col-lg-2 form-control-label">First Name</label>
	   			<div class="col-lg-10">
	   				<form:input path= "firstName" type="text" name="firstName" value="${userToBeSaved.firstName}" class="form-control"/><form:errors class="formErrors" path="firstName"/>
	   			</div>
	   			<label for="lastName" class="col-lg-2 form-control-label">Last Name</label>
	   			<div class="col-lg-10">
	   				<form:input path= "lastName" type="text" name="lastName" value="${userToBeSaved.lastName}" class="form-control"/><form:errors path="lastName" class="formErrors"/>
	   			</div>
	   			<label for="emailId" class="col-lg-2 form-control-label">Email</label>
	   			<div class="col-lg-10">
	   				<form:input  path= "emailId" type="email" name="emailId" value="${userToBeSaved.emailId}" class="form-control"/><form:errors class="formErrors" path="emailId"/>
	   			</div>
	   			<label for="phoneNumber" class="col-lg-2 form-control-label">Phone Number</label>
	   			<div class="col-lg-10">
	   				<form:input  path= "phoneNumber" type="number" min="1000000000" max="9999999999" name="phoneNumber" value="${userToBeSaved.phoneNumber}" class="form-control"/><form:errors class="formErrors" path="phoneNumber"/>
	   			</div>
	   		</div>
	   		<div class="userdetailsdisplay col-lg-12">
	   			<h4 class="col-lg-12">Account Information</h4>
	   			<label for="username" class="col-lg-2 form-control-label">Username</label>
	   				<div class="col-lg-10">
	   					<form:input path = "account.username" type="text" name="username" id="username" value="${userToBeSaved.account.username}" class="form-control"/><form:errors  class="formErrors" path="account.username" /><p class="formErrors" id="usernameErr">${usernameErr}</p>
	   				</div>
	   			<label for="password" class="col-lg-2 form-control-label">Password</label>
	   			<div class="col-lg-10">
	   				 <form:input path = "account.password" type="password" name="password" id="password" value="${userToBeSaved.account.password}" class="form-control"/><form:errors class="formErrors" path="account.password"/>
	   			</div>
	   			<label for="status" class="col-lg-2 form-control-label">Status</label>
	   				<div class="col-lg-10">
	   					<c:if test="${not empty useridtodisplay}">
	   						<form:input readonly="true"  path = "account.status" type = "text" name="status" value="${userToBeSaved.account.status}" class="form-control"/><form:errors class="formErrors" path="account.status"/>
	   					</c:if>
	   					<c:if test="${empty useridtodisplay}">
	   						<form:select path="account.status" name="status" class="form-control">
			   					<form:option value="active" label="Active"/>
			   					<form:option value="suspended" label="Suspended"/>
	   						</form:select>
	   						<form:errors class="formErrors" path="account.status"/>
	   					</c:if>
	   					
	   				</div>
	   			<label for="role" class="col-lg-2 form-control-label">Role</label>
	   				<div class="col-lg-10">
						<c:if test="${not empty useridtodisplay}">
	   						<form:input path = "role" readonly="true"  type = "text" name="role" value="${userToBeSaved.role}" class="form-control"/><form:errors class="formErrors" path="role"/>
						</c:if>
	   					<c:if test="${empty useridtodisplay}">
							<form:select path="role" name="role" class="form-control">
			   					<form:option value="analyst" label="Analyst"/>
			   					<form:option value="manager" label="Manager"/>
			   					<form:option value="customer" label="Customer"/>
			   					<form:option value="admin" label="Admin"/>
							</form:select>
							<form:errors class="formErrors" path="role"/>
						</c:if>
	   				</div>
	  		</div>
	  		<div class="col-lg-12 buttondisplay">
	  			<c:if test="${not empty useridtodisplay}">
	  				<input type="submit" value="Update" class="btn btn-warning form-control"/>
	  			</c:if>
	  			<c:if test="${empty useridtodisplay}">
	  				<input type="submit" value="Create" class="btn btn-success form-control"/>
	  			</c:if>
	  			<label class="formSuccess">${updateStatus}</label>
	  		</div>
		</form:form>
	   </div>

	</div>
	
<script>


</script>	
	
</body>
</html>