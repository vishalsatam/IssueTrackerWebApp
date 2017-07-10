<!DOCTYPE HTML>
<html>
<head>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
<link href="${pageContext.request.contextPath}/resources/css/workarea.css" rel="stylesheet" >
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

</head>

<body>

	   <%@ include file="admin_controls.jsp" %>
	   <div id="workarea">
	   		<form action = "manageUsers.htm" method="POST">
	   		<!-- 
	   		<div>
	   			Search <input type="text" name="searchText"/>
	   			Search by 
	   			Name <input type ="radio" name="searchBy" value="name"/>
	   			Email <input type ="radio" name="searchBy" value="email"/>
	   			Username <input type ="radio" name="searchBy" value="username"/>
	   			
	   			<br/>
	   			
	   			
	   		</div>
	   		-->
	   		<div>
	   			<a href="viewupdateuser.htm" class="btn btn-warning">Create User</a>
	   		</div>
	   		<div>
		   		<table id="usertable" class="table table-striped hoverable">
		   			<thead>
		   			<tr>
		   				<th></th>
		   				<th>User Id</th>
		   				<th>First Name</th>
		   				<th>Last Name</th>
		   				<th>Email ID</th>
		   				<th>Phone Number</th>
		   				<th>Role</th>
		   				<th>Username</th>
		   				
		   			</tr>
		   			</thead>
		   			<tbody>
	 					<c:forEach var="userInList" items="${usersList}">
	                        <c:if test="${userInList.userId != sessionScope.sessionUserId}">
		                        <tr>
		                        	<td><input class="actionIcons" type="checkbox" name="rowsToBeDeleted" value="${userInList.userId}"/><a href="#" onClick = "return deleteIndivUser(this);" class="delLink"><span class="actionIcons glyphicon glyphicon-trash"></span></a> </td>
		                        	<td>${userInList.userId}</td>
		                            <td>${userInList.firstName}</td>
		                            <td>${userInList.lastName}</td>
		                            <td>${userInList.emailId}</td>
		                            <td>${userInList.phoneNumber}</td>
		                            <td>${userInList.role}</td>
		                            <td>${userInList.account.username}</td>
		                        </tr>
	                        </c:if>
	                    </c:forEach>
	                </tbody>
	                <c:if test="${fn:length(usersList) gt 1}">
		                <tfoot>
		                	<tr>
		                		<td colspan="7"><input class="btn btn-danger" value="Delete Users" onClick="return validate();" type ="submit" /></td>
		                	</tr>
		                </tfoot>
		   			</c:if>
		   		</table>
		   	</div>
		   	</form>
	   </div>

</body>

<script>

$('#usertable > tbody > tr').click(function(e) {
    //var target = $(event.target);
	var delCheck = this.firstChild;
   	//alert(delCheck.nextSibling.firstChild.value);
   	//alert(this);
    if(!$(e.target).hasClass('actionIcons')){
		
		window.location.href = "viewupdateuser.htm?useridtodisplay="+delCheck.nextSibling.firstChild.value;
    }

});

function validate(){
	
	if($("input[name='rowsToBeDeleted']:checked").size()<=0){
		alert("Please select a row first");
		return false;
	}
	return true;
}

function deleteIndivUser(obj){
	
	var delCheck = obj.previousSibling;
   var delCheckVal = obj.previousSibling.value;
   //alert(delCheckVal);
	//var numRowsText = document.getElementById("numRows").value;
	//var numRows = document.querySelectorAll("span.badge")[0].innerHTML;
	var xmlHttp = getNewXMLHttpRequest();
			xmlHttp.open("GET", "manageUsers.htm?action=deleteIndiv&deleteParam=" + delCheckVal, true);
			
			xmlHttp.onreadystatechange=function()
			{
				if(xmlHttp.readyState == 4){
					var resp = "" + xmlHttp.responseText;
					var regex = /Messages were deleted successfully/;
				  if (xmlHttp.status == 200) {
					   alert("User deleted successfully");
					   
	
					   $(delCheck.parentNode.parentNode).remove();
					   //numRows--;
						
					   //numRowsText.innerHTML = "You have "+ numRows + " messages";
					   //alert(document.querySelectorAll("span.badge").innerHTML);
					   //document.querySelectorAll("span.badge")[0].innerHTML = numRows;
					   //document.getElementById("numRows").value = numRows;
					   return true;
					}
					//else{
					//	alert("Error Occurred : \n Details:\n"+resp);
					//	return false;
					//}
					
				}
				
				
			};
			
		xmlHttp.send();	


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

</html>