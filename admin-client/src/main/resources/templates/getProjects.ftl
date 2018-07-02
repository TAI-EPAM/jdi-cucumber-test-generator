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
<nav class="navbar navbar-default" style="margin-top">
    <div class="container-fluid">
<ul class="nav navbar-nav">
    <li><a href="/getUsers"><label value="User List" >Users List</label></a></li>
    <li><a href="/getJira"><label value="Jira Settings" >Jira Settings</label></a></li>
    <li class="active"><a href="/getProjects"><label value="Project" >Projects List</label></a></li>
</ul>
</nav>
<#if  error??><div class="alert alert-danger" role="alert" >${error}</div></#if>
<div>
    <fieldset>
        <legend><h2>Delete Project</h2></legend>
        <form name="projectDTO" action="/deleteProject" method="POST">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <table>
                <tr><td>ID:</td> <td><input type="text" name="id" required/></td></tr>
            </table>
            <input type="submit" value="Save" />
        </form>
    </fieldset>
</div>

<div>
    <fieldset>
        <legend><h2>Create Project</h2></legend>
        <form name="ProjectCreateDTO" action="/createProject" method="POST">

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <table>
            <tr><td>Description:</td> <td><input type="text" name="description" required/></td></tr>
            <tr><td>Name:</td> <td><input type="text" name="name" required/></td></tr>
            </table>
            <input type="submit" value="Save" />
        </form>
    </fieldset>
    <br/>
</div>
<div>
<table class="datatable">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
    </tr>
<#list projectList as project>
    <tr>
        <td>${project.id}</td>
        <td>${project.name}</td>
        <td>${project.description}</td>
    </tr>
</#list>
</div>
</body>
</html>