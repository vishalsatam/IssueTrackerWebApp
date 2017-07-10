<!DOCTYPE HTML>
<html>
<head>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
<link href="${pageContext.request.contextPath}/resources/css/workarea.css" rel="stylesheet" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
</head>

<body>
	   <%@ include file = "manager_controls.jsp" %>
	   <div id="workarea">
	   		<form action="assignIssue.htm" method="post">
	   		
	   		<div id="selectionsForAssignIssue">
		   		<div>
		   			<select id="teamSelector" onchange="return loadTeam(this);" name="teamSelector" class="form-control">
		   				<option value="">-- Select a team --</option>
		   				<c:forEach var="teamInList" items="${teamList}">
		   					<c:if test="${not empty teamId}">
		   						<c:if test="${teamId==teamInList.teamId}">
		   							<option selected="selected" value="${teamInList.teamId}">${teamInList.teamName}</option>
		   						</c:if>
		   						<c:if test="${teamId!=teamInList.teamId}">
		   							<option value="${teamInList.teamId}">${teamInList.teamName}</option>
		   						</c:if>
		   					</c:if>
		   					<c:if test="${empty teamId}">
		   						<option value="${teamInList.teamId}">${teamInList.teamName}</option>
		   					</c:if>
		   					
		   				</c:forEach>
		   			</select>
		   					
		   		</div>
		   		<div>
		   			<select id="analystSelector" name="analystSelector" class="form-control">
		   				<option value="">-- Select an Analyst --</option>
		   				<c:forEach var="userInList" items="${analystList}">
		   					<option value="${userInList.userId}">${userInList.userId} - ${userInList.firstName} ${userInList.lastName}</option>
		   				</c:forEach>
		   			</select>
		   		</div>
		   		
		   		<div>
		   			<select id="issueSelector" name="issueSelector" class="form-control">
		   				<option value="">-- Select an Issue --</option>
		   				<c:forEach var="issueInList" items="${issueList}">
		   					<c:if test="${issueInList.status=='New'}">
		   						<option value="${issueInList.issueId}">${issueInList.issueId} - ${issueInList.title}</option>
		   					</c:if>
		   				</c:forEach>
		   			</select>
		   		</div>
		   		<div>
		   			<input type="submit" onClick = "return assignValidate();" value="Assign" class="btn btn-warning"/>
		   		</div>
	   			</form>
	   		</div>
	   		<div>
	   			
		   		<table id="issuesTable" class="table table-striped">
		   			<thead>
		   			
		   			<tr>
		   				<th></th>
		   				<th>Issue Id</th>
		   				<th>Creation Date</th>
		   				<th>Title</th>
		   				<th>Description</th>
		   				<th>Severity</th>
		   				<th>Customer Name</th>
		   				<th>Assignee</th>
		   				<th>Status</th>
		   				
		   			</tr>
		   			</thead>
		   			<tbody>
	 					<c:forEach var="issueInList" items="${issueList}">
	                        <tr>
	                        	<!-- <td><input class="actionIcons" type="checkbox" name="rowsToBeDeleted" value="${issueInList.issueId}"/><a href="#" onCLick = "return deleteIndivUser(this);" class="delLink"><span class="actionIcons glyphicon glyphicon-trash"></span><a> </td>-->
	                        	<td></td>
	                        	<td>${issueInList.issueId}</td>
	                            <td>${issueInList.creationDate}</td>
	                            <td>${issueInList.title}</td>
	                            <td>${issueInList.description}</td>
	                            <td>${issueInList.severity}</td>
	                            <td>${issueInList.issueRaisedBy.firstName} ${issueInList.issueRaisedBy.lastName}</td>
	                            <td>${issueInList.primaryAssigneeId.userId} - ${issueInList.primaryAssigneeId.firstName} ${issueInList.primaryAssigneeId.lastName}</td>
	                            <td>${issueInList.status}</td>
	                        </tr>
	                        
	                    </c:forEach>
	                </tbody>
	                <!--
	                <tfoot>
	                	<tr>
	                		<td colspan="7"><input class="btn btn-danger" value="Delete Users" type ="submit" /></td>
	                	</tr>
	                </tfoot>
	                -->
		   		</table>
		   	</div>
	   		
	   		<div>
	   			
		   		<table id="usertable" class="table table-striped">
		   			<thead>
		   			<tr>
		   				<th></th>
		   				<th>User Id</th>
		   				<th>Username</th>
		   				<th>First Name</th>
		   				<th>Last Name</th>
		   				<th>Email ID</th>
		   				<th>Assigned Issues</th>
		   				<th>Pending With Analyst</th>
		   				
		   			</tr>
		   			</thead>
		   			<tbody>
	 					<c:forEach var="userInList" items="${analystList}">
	                        <tr>
	                        	<!-- <td><input class="actionIcons" type="checkbox" name="rowsToBeDeleted" value="${userInList.userId}"/><a href="#" onCLick = "return deleteIndivUser(this);" class="delLink"><span class="actionIcons glyphicon glyphicon-trash"></span><a> </td>-->
	                        	<td></td>
	                        	<td>${userInList.userId}</td>
	                        	<td>${userInList.account.username}</td>
	                            <td>${userInList.firstName}</td>
	                            <td>${userInList.lastName}</td>
	                            <td>${userInList.emailId}</td>
	                            <td>${fn:length(userInList.workList)}</td>
	                            <td>${fn:length(userInList.pendingIssuesList)}</td>
	                            
	                        </tr>
	                        
	                    </c:forEach>
	                </tbody>
	                <!-- 
	                <tfoot>
	                	<tr>
	                		<td colspan="7"><input class="btn btn-danger" value="Delete Users" type ="submit" /></td>
	                	</tr>
	                </tfoot>
	                -->
		   		</table>
		   	</div>
	   		
	   		
	   </div>

<script>
function assignValidate(){
	//alert("func");
	//alert(document.getElementById("message").value);
	if(document.getElementById("teamSelector").value == ""){
		alert("Please Select Team");
		return false;
	}
	else if(document.getElementById("analystSelector").value == ""){
		alert("Please Select Analyst");
		return false;
	}
	else if(document.getElementById("issueSelector").value == ""){
		alert("Please Select Issue");
		return false;
	}
	else{
		return true;
	}
}


function loadTeam(obj){
	if(obj.value == ""){
		return false;
	}
	else{
		location.href = "assignIssue.htm?teamId="+obj.value;
		return true;
	}
}


function getNewXMLHttpRequest(){
	var xmlHttp;
   
	try     // Firefox, Opera 8.0+, Safari
	{
		xmlHttp=new XMLHttpRequest();
	}
	catch (e)
	{
		try  // Internet Explorer
		{
			xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
		}
		catch (e)
		{
			try
			{
				xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			catch (e)
			{
				alert("Your browser does not support AJAX!");
				return null;
			}
		}
	}
	finally{
		return xmlHttp; 
	}
}

</script>


</body>
</html>