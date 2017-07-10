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
	   		
	   		<div class="issueArea">
	   			<div class="issueDesc jumbotron">
		   			<h5>Issue Id : ${issueToBeDisplayed.issueId}</h5>
		   			<h5>Title : ${issueToBeDisplayed.title}</h5>
		   			<h5>Description : ${issueToBeDisplayed.description}</h5>
		   			<h5>Assigned To : ${issueToBeDisplayed.primaryAssigneeId.userId} - ${issueToBeDisplayed.primaryAssigneeId.firstName} ${issueToBeDisplayed.primaryAssigneeId.lastName}</h5>
		   			<h5>Team Id : ${issueToBeDisplayed.teamId.teamId} Name : ${issueToBeDisplayed.teamId.teamName}</h5>
		   			<h5>Created On : ${issueToBeDisplayed.creationDate}</h5>
		   			<h5>Status : ${issueToBeDisplayed.status}</h5>
		   			<h5>Severity : ${issueToBeDisplayed.severity}</h5>
		   			<h6><a href="downloadPdf.pdf?issueId=${issueToBeDisplayed.issueId}" target="_blank">Download PDF report</a></h6>
		   			
		   			<c:choose>
					    <c:when test="${issueToBeDisplayed.status == 'Closed By Team'}">
					    	<h4 class="underdue">Closed</h4>
					    </c:when>
					    <c:when test="${issueToBeDisplayed.status == 'Closed By Requestor'}">
							<h4 class="underdue">Closed</h4>
					    </c:when>
					    <c:otherwise>
						    <c:if test="${passedDeadLine == 'true'}">
				   				<h4 class="underdue">Pending Time : ${deadline}</h4>
				   			</c:if>
				   			<c:if test="${passedDeadLine == 'false'}">
				   				<h4 class="overdue">Overdue : ${deadline}</h4>
				   			</c:if>
					    </c:otherwise>
					</c:choose>

	   			
	   			</div>
	   			
	   			<div class="messages">
	   				<c:forEach var="messageInList" items="${messages}">	
	   					<div class="message">
	   						<div>
	   							
	   							<h4>${messageInList.description}</h4>
	   						</div>
	   						<div>
	   							<h4>${messageInList.actionPerformed} by ${messageInList.senderName}</h4>
	   							<h4>${messageInList.sentDate}</h4>
	   						</div>
	   						<div>
	   							<c:if test = "${ not empty messageInList.attachment}">
	   								Attachment : <a href="downloadFile.htm?fileName=${messageInList.attachment}">${messageInList.attachment}</a>
	   							</c:if>
	   						</div>
	   					</div>
	   				</c:forEach>
	   			</div>
	   			
	   			<div id="pagination">
	   				<nav>
					  <ul class="pagination">
					    <li class="disabled"><a href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
						    <c:forEach var="pnum" begin="1" end="${totalPages}">
						    	<li><a href="viewIssue.htm?issueToDisplay=${issueToBeDisplayed.issueId}&pageNum=${pnum}">${pnum}<span class="sr-only">(current)</span></a></li>
						    </c:forEach>
					    <li class="disabled"><a href="#" aria-label="Previous"><span aria-hidden="true">&raquo;</span></a></li>
					  </ul>
					</nav>
	   			</div>
	   			
	   			<div id="actionsToPerform" class="container-fluid">
	   			
	   				<div class="row">
		   				<form action="performAction.htm" method="POST" enctype="multipart/form-data">
			   				<input type="hidden" name="issueId" value="${issueToBeDisplayed.issueId}" />
			   				<div class="displayActionBlocks col-lg-8" id = "dataToBeSet">
				   				<label for="message" class="col-lg-2 form-control-label">Message</label>
				   				<div class="col-lg-10">
				   					<input required type = "text" name="message" id="message" class="form-control" />
								</div>
								<label for="attachment" class="col-lg-2 form-control-label">Attach File</label>
				   				<div class="col-lg-10">
				   					<input type="file" name="attachment" value="Upload" id="attachment"/>
								</div>
			   				</div>
			   				
			   				<!-- Transfer -->
			   				<c:if test="${(roleofassigneduser == 'manager' || roleofassigneduser == 'analyst') && (issueToBeDisplayed.status=='Open With Assignee' || issueToBeDisplayed.status=='Pending Closure Approval' || issueToBeDisplayed.status=='Pending Clarification Approval') && belongsToTeam == 'yes'}">
				   				<div class="displayActionBlocks col-lg-4" id ="transferActionsToBePerformed">
					   				
					   				<label for="newAssignee" class="col-lg-2 form-control-label">Team Member</label>
					   				<div class="col-lg-10">
						   				<select name="newAssignee" class="form-control" id="newAssignee">
						   					<c:forEach var="teamMember" items="${teamList}">
						   						<option value="${teamMember.userId}">${teamMember.userId} - ${teamMember.firstName} ${teamMember.lastName}</option>
						   					</c:forEach>
						   				</select>
						   			</div>
					   				<input type="submit" name="action" onClick="return transferValidate();" value="Transfer" class="btn btn-warning form-control"/>
								</div>
							</c:if>
							<c:if test="${roleofassigneduser == 'analyst' && issueToBeDisplayed.issueCurrentlyWithId.userId == sessionScope.sessionUserId && issueToBeDisplayed.status=='Open With Assignee'}">
				   				<div class="displayActionBlocks col-lg-4" id ="transferActionsToBePerformed">
					   				
					   				<label for="newAssignee" class="col-lg-12 form-control-label">Team Member</label>
					   				<div class="col-lg-12">
						   				<select name="newAssignee" class="form-control" id="newAssignee">
						   					<c:forEach var="teamMember" items="${teamList}">
						   						<option value="${teamMember.userId}">${teamMember.userId} - ${teamMember.firstName} ${teamMember.lastName}</option>
						   					</c:forEach>
						   				</select>
						   			</div>
					   				<input type="submit" name="action" onClick="return transferValidate();" value="Transfer" class="btn btn-warning form-control"/>
								</div>
							</c:if>
			   				<!-- End of Transfer -->
		   				
 					</div>
 					<!-- End of Data -->
 					<!-- Begin action controls -->
 					
	 					<div class="row">
			   				<c:if test="${roleofassigneduser == 'analyst' && issueToBeDisplayed.issueCurrentlyWithId.userId == sessionScope.sessionUserId}">
				   				<div class="displayActionBlocks" id ="analystActionsToBePerformed">
						   			<c:if test="${issueToBeDisplayed.status == 'Open With Assignee'}">	
						   				<input type="submit" name="action" value="Send For Clarification" class="btn btn-primary"/>
						   				<input type="submit" name="action" value="Close Issue" class="btn btn-danger"/>
						   			</c:if>
				   				</div>
			   				</c:if>
	
			   				<c:if test="${roleofassigneduser == 'manager' && issueToBeDisplayed.issueCurrentlyWithId.userId == sessionScope.sessionUserId}">
				   				<div class="displayActionBlocks" id ="managerActionsToBePerformed">
					   				<c:if test="${issueToBeDisplayed.status == 'Pending Clarification Approval' || issueToBeDisplayed.status == 'Pending Closure Approval'}">	
					   					<input type="submit" name="action" value="Approve" class="btn btn-success"/>
					   					<input type="submit" name="action" value="Reject" class="btn btn-danger"/>
					   				</c:if>
				   				</div>
			   				</c:if>
	
			   				<c:if test="${(roleofassigneduser == 'customer' && issueToBeDisplayed.issueCurrentlyWithId.userId == sessionScope.sessionUserId) || (issueToBeDisplayed.issueRaisedBy.userId == sessionScope.sessionUserId && sessionScope.authorization == 'customer')}">
				   				<div class="displayActionBlocks" id ="customerActionsToBePerformed">
				   					<c:if test="${issueToBeDisplayed.status == 'Pending Clarification'}">
						   				<input type="submit" name="action" value="Provide Clarification" class="btn btn-primary"/>
						   			</c:if>
						   			<c:if test="${issueToBeDisplayed.status == 'Closed By Team'}">
						   				<input type="submit" name="action" value="Confirm Closure" class="btn btn-success"/>
						   			</c:if>
						   			<c:if test="${issueToBeDisplayed.status == 'Closed By Requestor' || issueToBeDisplayed.status == 'Closed By Team'}">
						   				<input type="submit" name="action" value="Re-Open" class="btn btn-warning"/>
						   			</c:if>
				   					
				   				</div>
							</c:if>
							</div>
							
							<div class="row">
								<div class="displayActionBlocks" id ="commonActionsToBePerformed">
									<input type="submit" name="action" onClick = "return validate();" value="Post Comment" class="btn btn-default"/>
				   				</div>
			   				</div>
		   				</form>
	   					
	   				
	   			</div>
	   			
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

function validate(){
	//alert("func");
	//alert(document.getElementById("message").value);
	if(!document.getElementById("message").value.match(/^[a-zA-Z0-9][a-zA-Z0-9\s.,'":?\-]*$/)){
		alert("Message is required and must be contain valid characters. Must start with a number or a character.");
		return false;
		
	}
	else{
		return true;
	}
}

function transferValidate(){
	//alert("func");
	//alert(document.getElementById("message").value);
	if(!document.getElementById("newAssignee").value.match(/^[a-zA-Z0-9][a-zA-Z0-9\s.,'":?\-]*$/)){
		alert("Transferee should be selected");
		return false;
		
	}
	else{
		return true;
	}
}

</script>

</body>
</html>