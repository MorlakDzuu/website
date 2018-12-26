<#macro page>
<#include "security.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <title>Website</title>
</head>
<body>
    <nav>
        <div class="nav-wrapper">
            <a href="/" class="brand-logo" style="margin-left: 10px;">Website</a>
            <a href="/" data-target="mobile-demo" class="sidenav-trigger"><i class="material-icons">menu</i></a>
            <ul id="nav-mobile" class="right hide-on-med-and-down">
                <li><a href="/addTask">Add task</a></li>
                <li><a href="/taskList">See tasks</a></li>
		<#if auth>
              <li><a href="/user">My personal Area</a></li>
        </#if>
            </ul>
        </div>
    </nav>
    <div class="container">
<#nested>
</div>
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script
        src="https://code.jquery.com/jquery-3.3.1.js"
        integrity="sha256-2Kok7MbOyxpgUVvAk/HJ2jigOSYS2auK4Pfzbm7uH60="
        crossorigin="anonymous"></script>
    <script src="/js/materialize.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        $('.sidenav').sidenav();
        $('.datepicker').datepicker();
    });
</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <#list notes?if_exists as note>
        <script type="text/javascript">
            M.toast({html: '${note}'});
        </script>
    </#list>
    <script src="/js/main.js"></script>
</body>
</html>
</#macro>