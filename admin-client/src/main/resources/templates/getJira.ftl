<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/css/tablestyle.css"/>
</head>
<body>

<div style="margin-top">
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <ul class="nav navbar-nav">
                <li><a href="/getUsers"><label value="User List">Users List</label></a></li>
                <li class="active"><a href="/getJira"><label value="Jira Settings">Jira Settings</label></a></li>
                <li><a href="/getProjects"><label value="Project">Projects List</label></a></li>
            </ul>
        </div>
    </nav>
</div>
<#if  error??><div class="alert alert-danger" role="alert" >${error}</div></#if>
    <fieldset>
        <legend><h2>Set Jira Settings</h2></legend>
        <form name="jiraDTO" action="/setJiraSettings" method="POST">
        <table>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <tr><td>uri:</td> <td><input type="text" name="uri" required/></td></tr>
            <tr><td>login:</td> <td><input type="text" name="login" required/></td></tr>
            <tr><td>password:</td> <td><input type="password" name="password" /></td></tr>
        </table>
            <input type="submit" value="Save" />
        </form>
    </fieldset>

<table class="datatable">
    <tr>
        <th>ID</th>
        <th>uri</th>
        <th>login </th>
    </tr>
<#list jiraSettings as sett>
<tr>
    <td>${sett.id}</td>
    <td>${sett.uri}</td>
    <td>${sett.login}</td>
</tr>
</#list>
</table>
</body>
</html>