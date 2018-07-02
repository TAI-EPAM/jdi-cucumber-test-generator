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
                <li class="active"><a href="/getUsers"><label value="User List">Users
                    List</label></a></li>
                <li><a href="/getJira"><label value="Jira Settings">Jira Settings</label></a></li>
                <li><a href="/getProjects"><label value="Project">Projects List</label></a></li>
            </ul>
        </div>
    </nav>
</div>
<#if  error??>
<div class="alert alert-danger" role="alert">${error}</div></#if>

<div id="content">
    <fieldset>
        <legend><h2>SetRole</h2></legend>
        <form name="userDTO" action="/setRole" method="POST">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <table>
                <tr>
                    <td>Email:</td>
                    <td><input type="text" name="email" required/></td>
                </tr>
                <tr>
                    <td>Role:</td>
                    <td><input type="text" name="role" required/></td>
                </tr>
            </table>
            <input type="submit" value="Save"/>
        </form>
    </fieldset>
    <br/>
</div>

<div id="content">
    <fieldset>
        <legend><h2>Block User</h2></legend>
        <form name="id" action="/blockUser" method="POST">

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <table>
                <tr>
                    <td>Id:</td>
                    <td><input type="text" name="id" required/></td>
                </tr>
            </table>
            <input type="submit" value="Save"/>
        </form>
    </fieldset>
    <br/>
</div>

<div id="content">
    <fieldset>
        <legend><h2>Unblock User</h2></legend>
        <form name="id" action="/unblockUser" method="POST">

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <table>
                <tr>
                    <td>Id:</td>
                    <td><input type="text" name="id" required/></td>
                </tr>
            </table>
            <input type="submit" value="Save"/>
        </form>
    </fieldset>
    <br/>
</div>


<table class="datatable">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Surname</th>
        <th>Email</th>
        <th>Role</th>
        <th>Attempts</th>
        <th>Locked</th>
        <th>Locked By Admin</th>
    </tr>
<#list userList as user>
    <tr>
        <td>${user.id}</td>
        <td>${user.name}</td>
        <td>${user.surname}</td>
        <td>${user.email}</td>
        <td>${user.role}</td>
        <td>${user.attempts}</td>
        <td>${user.locked?string('locked','unlocked')}</td>
        <td>${user.blockedByAdmin?string('blocked','unblocked')}</td>
    </tr>
</#list>
</table>

</body>
</html>