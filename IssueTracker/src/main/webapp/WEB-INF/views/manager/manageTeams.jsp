<!DOCTYPE HTML>
<html>
<head>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
<link href="${pageContext.request.contextPath}/resources/css/workarea.css" rel="stylesheet" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>


</head>

<body>
	   <%@ include file = "manager_controls.jsp" %>
	   <div id="workarea">
	   		<div id="newTeamArea">
		   		<div>
		   			New Team Name <input required type="text" id="newTeamName" name="newTeamName" />
		   		</div>
		   		<div id="setSeverity">
		   			Sev 1 Hours :<input required type="number" name="sev1" id="sev1"/>
		   			Sev 2 Hours: <input required type="number" name="sev2" id="sev2"/>
		   			Sev 3 Hours: <input required type="number" name="sev3" id="sev3"/>
		   		</div>
		   		<div>
		   			<a onClick ="return createNewTeam();" href="#" class="btn btn-primary">Create Team</a>
		   		</div>
		   	</div>
	   		<div>
	   			Select a Team : 
	   			<select onChange = "return populateUsersForTeam(this.value);" id="teamSelector">
	   				<option value="">-- Select a Team --</option>
	   				<c:forEach var="teamInList" items="${teamList}">
	   					<option value="${teamInList.teamId}">${teamInList.teamName}</option>
	   				</c:forEach>
	   			</select>
	   			<a onClick ="return deleteTeam();" href="#" class="btn btn-danger">Delete Team</a>
	   			
	   		</div>
	   		
	   		<div>
	   			Select Users To Be added to this team
	   			<select id="userToBeAdded">
	   				<option value="">-- Select a User --</option>
	   				<c:forEach var="userInList" items="${userWithoutTeam}">
	   					<option value="${userInList.userId}">${userInList.firstName} ${userInList.lastName}</option>
	   				</c:forEach>
	   			</select>
	   			<a onClick ="return addToTeam();" href="#" class="btn btn-primary">Add to Team</a>
	   			
	   		</div>
	   		
	   		<div>
		   		<table id="usertable" class="table table-striped">
		   			<thead>
		   			<tr>
		   				<th id = "teamNameDisplay" colspan = "6"></th>
		   				
		   				
		   			</tr>
		   			<tr>
		   				
		   				<th>User Id</th>
		   				<th>First Name</th>
		   				<th>Last Name</th>
		   				<th>Email ID</th>
		   				<th>Phone Number</th>
		   				<th></th>
		   				
		   				
		   			</tr>
		   			</thead>
		   			<tbody>
	 					<c:forEach var="userInList" items="${usersInTeamList}">
	                        <tr>
	                        	
	                        	<td>${userInList.userId}</td>
	                            <td>${userInList.firstName}</td>
	                            <td>${userInList.lastName}</td>
	                            <td>${userInList.emailId}</td>
	                            <td>${userInList.phoneNumber}</td>
	                            <td></td>
	                            
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

function removeFromTeam(elem){
	//alert("remove");
	//alert(elem);
	var delUser = elem.parentNode.parentNode.firstChild;
	//alert(delUser);
	var xmlHttp = getNewXMLHttpRequest();
	xmlHttp.open("GET", "removeFromTeam.htm?teamId="+document.getElementById("teamSelector").value+"&analystId="+delUser.innerHTML, true);
	
	xmlHttp.onreadystatechange=function()
	{
		if(xmlHttp.readyState == 4 && xmlHttp.status == 200){
			
			
		  	populateUsersForTeam(document.getElementById("teamSelector").value);
		  	var json = JSON.parse(xmlHttp.responseText);
		  	alert(json.msg);
		    var opt = document.createElement("option");
		    opt.innerHTML = json.firstName + " " + json.lastName;
		    opt.setAttribute("value",json.userId);
		  	$("#userToBeAdded").append(opt);

		}
		else if(xmlHttp.readyState == 4 && xmlHttp.status != 200){
			var json = JSON.parse(xmlHttp.responseText);
			alert(json.msg);
		}
		
	};
	
	xmlHttp.send();
}

function deleteTeam(){
	if($("#usertable > tbody").children().length >= 1){
		alert("Team not empty");
		return false;
	}
	else if($("#teamSelector option:selected").val() == ""){
		alert("Please Select Team first");
		return false;
	}
	else{
		var xmlHttp = getNewXMLHttpRequest();
		xmlHttp.open("GET", "deleteTeam.htm?teamId="+document.getElementById("teamSelector").value, true);
		
		xmlHttp.onreadystatechange=function()
		{
			if(xmlHttp.readyState == 4 && xmlHttp.status == 200){
				if(xmlHttp.responseText.match(/Success/gi)){
					$("#teamSelector option:selected").remove();
					$('#teamSelector option:eq(0)').attr("selected","selected");
					alert("Team Deleted");
				}
			}
		};
		
		xmlHttp.send();	
		return true;
	
	}
	
}

function addToTeam(){
	//alert("Add analyst to team");
	var userToBeAdded = document.getElementById("userToBeAdded");
	var userId = userToBeAdded.value;
	//alert(userId);
	if(userId != ""){
		var xmlHttp = getNewXMLHttpRequest();
		xmlHttp.open("GET", "addToTeam.htm?teamId="+document.getElementById("teamSelector").value+"&analystId="+document.getElementById("userToBeAdded").value, true);
		
		xmlHttp.onreadystatechange=function()
		{
			if(xmlHttp.readyState == 4 && xmlHttp.status == 200){
				var json = JSON.parse(xmlHttp.responseText);
		
				/*
				for(var count = 0; count < json.users.length; count++){
					var tableRow = document.createElement("tr");
					var tableElement = document.createElement("td");
					var textNode = document.createTextNode("");
					tableElement.appendChild(textNode);
					tableRow.appendChild(tableElement);
					
					
					textNode = document.createTextNode(json.userId);
					tableElement = document.createElement("td");
					tableRow = document.createElement("tr");
					tableElement.appendChild(textNode);
					tableRow.appendChild(tableElement);
					
					textNode = document.createTextNode(json.firstName);
					tableElement = document.createElement("td");
					tableRow = document.createElement("tr");
					tableElement.appendChild(textNode);
					tableRow.appendChild(tableElement);
					
					$("#usertable > tbody").append(tableRow);
				}
				*/
				
				
				populateUsersForTeam(document.getElementById("teamSelector").value);
				var elt = document.getElementById("userToBeAdded");
				
			        

			    $("#userToBeAdded option:selected").remove();
			    $('#userToBeAdded option:eq(0)').attr("selected","selected");
			}
			
			
		};
		
	xmlHttp.send();	
	return true;
	
	}
	else{
		alert("No user selected!");
		return false;
	}
}


function populateUsersForTeam(teamId){
	//alert("Populate Analysts");
	$("#usertable > tbody").empty();
	if(teamId != ""){
		
		var xmlHttp = getNewXMLHttpRequest();
		xmlHttp.open("GET", "populateUsers.htm?teamId="+teamId, true);
		
		xmlHttp.onreadystatechange=function()
		{
			if(xmlHttp.readyState == 4 && xmlHttp.status == 200){
				var json = JSON.parse(xmlHttp.responseText);
				//alert(xmlHttp.responseText);

				//alert(json[0]["firstName"]);
				//alert(json.length);
				
				for(var count = 0; count < json.length; count++){
					
					var tableRow = document.createElement("tr");
					var tableElement = document.createElement("td");
					var textNode = document.createTextNode("");
					var anchor = document.createElement("a");
					
					textNode = document.createTextNode(json[count]["userId"]);
					tableElement = document.createElement("td");
					tableElement.appendChild(textNode);
					tableRow.appendChild(tableElement);
					
					textNode = document.createTextNode(json[count]["firstName"]);
					tableElement = document.createElement("td");
					tableElement.appendChild(textNode);
					tableRow.appendChild(tableElement);
					
					textNode = document.createTextNode(json[count]["lastName"]);
					tableElement = document.createElement("td");
					tableElement.appendChild(textNode);
					tableRow.appendChild(tableElement);
					
					textNode = document.createTextNode(json[count]["emailId"]);
					tableElement = document.createElement("td");
					tableElement.appendChild(textNode);
					tableRow.appendChild(tableElement);
					
					textNode = document.createTextNode(json[count]["phoneNumber"]);
					tableElement = document.createElement("td");
					tableElement.appendChild(textNode);
					tableRow.appendChild(tableElement);
					
					//textNode = document.createTextNode(json[count]["userId"]);
					tableElement = document.createElement("td");
					anchor.setAttribute("onClick","return removeFromTeam(this);");
					anchor.setAttribute("href","#");
					anchor.innerHTML = "Delete";
					tableElement.appendChild(anchor);
					tableRow.appendChild(tableElement);

					
					$("#usertable > tbody").append(tableRow);
					
				}
				
			  	
			}
			
			
		};
		
	xmlHttp.send();
	$("#teamNameDisplay").html($("#teamSelector option:selected").html());
	return true;
	
	}
	else{
		$("#teamNameDisplay").html("");
		if($("#teamSelector option:eq(0):selected")){
			
		}
		else{
			alert("No value selected");
		}
		return false;
	}
	

}

function createNewTeam(){
	//alert("CREATE");
	
	if(!document.getElementById("newTeamName").value.match(/^[a-zA-Z0-9][a-zA-Z0-9\s]*$/)){
		alert("Name is required and must be alphanumeric");
		return false;
		
	}
	if(!document.getElementById("sev1").value.match(/^[0-9][0-9]*$/)){
		alert("Sev1 is required and must be only have digits");
		return false;
		
	}
	if(!document.getElementById("sev2").value.match(/^[0-9][0-9]*$/)){
		alert("Sev2 is required and must be only have digits");
		return false;
		
	}
	if(!document.getElementById("sev3").value.match(/^[0-9][0-9]*$/)){
		alert("Sev3 is required and must be only have digits");
		return false;
		
	}
	
	var xmlHttp = getNewXMLHttpRequest();
			xmlHttp.open("GET", "createTeam.htm?teamName="+document.getElementById("newTeamName").value+"&sev1="+document.getElementById("sev1").value+"&sev2="+document.getElementById("sev2").value+"&sev3="+document.getElementById("sev3").value, true);
			
			xmlHttp.onreadystatechange=function()
			{
				if(xmlHttp.readyState == 4 ){
					if(xmlHttp.status == 200){
						var json = JSON.parse(xmlHttp.responseText);
						var opt = document.createElement("option");
					  	opt.innerHTML = json.teamName;
					  	opt.setAttribute("value",json.teamId);
					  	opt.setAttribute("onCLick","return populateUsersForTeam(json.teamId);");
					  	var sel = document.getElementById("teamSelector");
					  	sel.appendChild(opt);
					  	alert("Team added");
					}
				  	else{
						alert("Some Error occurred while adding");
					}
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

</body>
</html>