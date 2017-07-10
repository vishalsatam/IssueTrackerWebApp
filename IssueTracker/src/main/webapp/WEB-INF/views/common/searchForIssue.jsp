<!DOCTYPE HTML>
<html>
<head>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
<link href="${pageContext.request.contextPath}/resources/css/workarea.css" rel="stylesheet" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
</head>
</head>

<body>
		<c:if test="${authorization == 'analyst'}">
	   <%@ include file = "../analyst/analyst_controls.jsp" %>
	   </c:if>
	   <c:if test="${authorization == 'customer'}">
	   <%@ include file = "../customer/customer_controls.jsp" %>
	   </c:if>
	   <c:if test="${authorization == 'manager'}">
	   <%@ include file = "../manager/manager_controls.jsp" %>
	   </c:if>
	   
	   <h1>Work Area</h1>
	   <div id="workarea">
	   		<div id="navTab">
	   		
	   		<nav class="navbar navbar-tabs">
			    	<ul class="nav nav-tabs">
			    		
			    		<%@ include file = "./tabHeadings.jsp" %>
			    		
			    	</ul>
			</nav>
	   		</div>
	   		
	   		<div>
	   			<form action="searchForIssues.htm" method="post">
		   			Search By <input required type="number" name="searchString" value="${searchString}" id="searchString"/> <input onClick="return validate();" type="submit"/><br/>
					
					<c:if test="${searchBy == 'ID' || searchBy == '' || empty searchBy}">
		   				Issue Id <input checked = "checked" id = "searchById" type="radio" name="searchBy" value="searchById">
		   				Title <input  id = "searchByTitle" type="radio" name="searchBy" value="searchByTitle">
		   				Team ID <input  id = "searchByTeamId" type="radio" name="searchBy" value="searchByTeamId">
	   				</c:if>
	   				<c:if test="${searchBy == 'Title'}">
		   				Issue Id <input  id = "searchById" type="radio" name="searchBy" value="searchById">
		   				Title <input checked = "checked" id = "searchByTitle" type="radio" name="searchBy" value="searchByTitle">
		   				Team ID <input  id = "searchByTeamId" type="radio" name="searchBy" value="searchByTeamId">
	   				</c:if>
	   				<c:if test="${searchBy == 'TeamId'}">
		   				Issue Id <input  id = "searchById" type="radio" name="searchBy" value="searchById">
		   				Title <input  id = "searchByTitle" type="radio" name="searchBy" value="searchByTitle">
		   				Team ID <input checked = "checked" id = "searchByTeamId" type="radio" name="searchBy" value="searchByTeamId">
	   				</c:if>		   				
	   				
	   			</form>
	   		</div>
	   		
	   		<div>
	   			<c:if test="${not empty searchString}">
	   				Your search by ${searchBy} - "${searchString}" returned ${numResults} results
	   			</c:if>
	   		</div>
	   		
	   		<div>
	   			
		   		<table id="issuesTable" class="table table-striped hoverable">
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
	   		
	   </div>

<script>

$('#issuesTable > tbody > tr').click(function(e) {
    //var target = $(event.target);
	var delCheck = this.firstChild.nextSibling.nextSibling.nextSibling.innerHTML;
   	//alert(delCheck);
   	//alert(this);
    if(!$(e.target).hasClass('actionIcons')){
		
		window.location.href = "viewIssue.htm?issueToDisplay="+delCheck;
    }

});

$("#searchById").click(function(){
	$("#searchString").attr("type","number");
});
$("#searchByTitle").click(function(){
	$("#searchString").attr("type","text");
});
$("#searchByTeamId").click(function(){
	$("#searchString").attr("type","number");
});

function validate(){
	//alert("func");
	//alert(document.getElementById("searchString").value);
	if(!document.getElementById("message").value.match(/^[a-zA-Z0-9][a-zA-Z0-9\s.,'":?\-]*$/)){
		alert("Search String is required and must be contain valid characters. Must start with a number or a character.");
		return false;
		
	}
	else{
		return true;
	}
}

</script>

</body>
</html>