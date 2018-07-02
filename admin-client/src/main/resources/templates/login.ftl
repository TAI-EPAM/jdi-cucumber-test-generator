<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/css/loginStyle.css"/>
</head>

<body>
<form class="form-signin" action="/login" method="post" shrink-to-fit=no>

<#if  error??><div class="alert alert-danger" role="alert" ><#if SPRING_SECURITY_LAST_EXCEPTION??>${SPRING_SECURITY_LAST_EXCEPTION.message}</#if></div></#if>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
    <label for="inputLogin" class="sr-only">Login</label>
    <input type="text" name="username" id="inputLogin" class="form-control" placeholder="Login"
           required  autofocus>
    <label for="inputPassword" class="sr-only">Password</label>
    <input type="password" id="inputPassword" name="password" class="form-control"
           placeholder="Password" required>

    <div class="row">

        <div class="col-xs-6 col-sm-6 col-md-6">
            <input value = "Sing in" class="btn btn-lg btn-primary btn-block" type="submit"/>
        </div>
    </div>
</form>

</body>
</html>