<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
<link href="${pageContext.request.contextPath}/resources/css/workarea.css" rel="stylesheet" >
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

</head>

<body>
	
	  <%@ include file="customer_controls.jsp" %>
	   
	   <div id="workarea">
	   
	   	<div id="newissueCreation">
	   		<h4>New Issue Information</h4>
	   		<form action="raiseNewIssue.htm" method="POST">
		   		<div>
		   			<label for="title" class="col-lg-2 form-control-label">Title</label>
		   			<div class="col-lg-10">
			   			<input class="form-control" type="text" id = "title" name="title" value="${title}"/>
			   			<p class="formErrors">${titleerr}</p>
		   			</div>
		   			
		   			<label for="description" class="col-lg-2 form-control-label">Description</label>
		   			<div class="col-lg-10">

			   			<textarea rows="5" id="description" class="col-lg-2 form-control" name="description" >${description}</textarea>
			   			<p class="formErrors">${descriptionerr}</p>
		   			</div><br/><br/>
		   			
		   			<label for="sev" class="col-lg-2 form-control-label">Severity</label>
		   			<div class="col-lg-10">
			   				<select id="sev" class="form-control" name="severity">
			   				<option value="1" selected="selected">Sev-1</option>
			   				<option value="2">Sev-2</option>
			   				<option value="3">Sev-3</option>
			   			</select>
			   			<p class="formErrors">${severr}</p>
					</div>
					
					
		   			<label for="teamSelector" class="col-lg-2 form-control-label">Team</label>
		   			<div class="col-lg-10">
			   			<select id="teamSelector" class="form-control" name="teamSelector">
			   				<option value="">-- Select a Team --</option>
		   						<c:forEach var="teamInList" items="${teamList}">
		   							<option value="${teamInList.teamId}">${teamInList.teamName}</option>
		   						</c:forEach>
			   			</select>
			   			<p class="formErrors">${teamerr}</p>
			   		</div>	
		   			
		   			<input class="btn btn-success form-control" onClick="return validate();" type="submit" value="Create"/>
		   			<p class="formErrors">${success}</p>
		   		</div>
		   		
		   		
		   	</form>
		  </div>
	   </div>

<script>
	function validate(){
		if($("#teamSelector option:selected").val() == ""){
			alert("Please Select Team first");
			return false;
		}
		else{
			return true;
		}
	}
</script>

</body>
</html>