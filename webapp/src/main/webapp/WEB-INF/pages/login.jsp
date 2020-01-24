<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Login</title>
</head>
<body>
<h2>Login page</h2>
<h2>Please, fill fields</h2>

<form action="/outlook/login/action" method="post">
  Login: <input type="text" name="login"><br>
  Password: <input type="password" name="pass"><br>
  <input type="submit" value="Login">
</form>

<div class="register-form">
  <form class="form-horizontal" action="/outlook/login/action" method="post" style="margin: 0px;" id="loginFormId">

    <input type="hidden" id="initialURI" name="initialURI" value="/portal">

    <div class="control-group">
      <input id="username" placeholder="User name" name="username" value="" type="text">
    </div>
    <div class="control-group">
      <input id="password" placeholder="Password" class="Password" name="password" value="" type="password">
      <span id="togglepwdbtn" style="margin: 25px -40px; display: none;"><img class="toggleimg" style="-khtml-user-select: none;-o-user-select: none;-moz-user-select: none;-webkit-user-select: none;user-select: none;height:18px; !imprtant" src="/community-extension/images/show_pwd.png" title="Show password" toggle_title="Hide password" toggle_src="/community-extension/images/hide_pwd.png"></span>
    </div>
    <div class="control-group">
      <label class="uiCheckbox">
        <input class="checkbox" type="checkbox" name="rememberme" id="rememberme" value="true">
        <span> Stay connected<span>
                        </span></span></label>
    </div>
    <div class="control-group">
      <button class="btn btn-primary" onclick="login();" type="submit"> Sign In</button>
      <br><br>
      <center> Not a member yet? <a href="/portal/intranet/register"> Register now </a></center>
      <div class="forgotAccout center">
        <a class="forgotLink" href="/portal/intranet/forgotpassword"> Can't access your account?</a>
      </div>
    </div>
  </form>
</div>

<a href="/outlook/app/registration" target="_blank">
  <span>Registration</span>
</a>
</body>
</html>
