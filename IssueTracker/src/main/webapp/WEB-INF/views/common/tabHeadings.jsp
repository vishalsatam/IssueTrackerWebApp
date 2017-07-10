		<li id="Pending" role="presentation"><a href="pendingIssues.htm">Pending</a></li>
  		<li id="MyIssues" role="presentation" ><a href="myIssues.htm">My Open Issues</a></li>
  		<li id="Closed" role="presentation" ><a href="closedIssues.htm">Closed</a></li>
  		<li id="SearchForIssues" role="presentation" ><a href="searchForIssues.htm">Search Issues</a></li>
  		<li id="Issue" role="presentation" ><a href="#" disabled="disabled">Issue</a></li>
  		<c:if test="${tabToBeShown == 'Pending'}">
  			<script>$('#Pending').addClass("active")</script>
  		</c:if>
  		<c:if test="${tabToBeShown == 'MyIssues'}">
  			<script>$('#MyIssues').addClass("active")</script>
  		</c:if>
  		<c:if test="${tabToBeShown == 'Closed'}">
  			<script>$('#Closed').addClass("active")</script>
  		</c:if>
  		<c:if test="${tabToBeShown == 'SearchForIssue'}">
  			<script>$('#SearchForIssues').addClass("active")</script>
  		</c:if>
  		<c:if test="${empty tabToBeShown}">
  			<script>$('#Issue').addClass("active")</script>
  		</c:if>
