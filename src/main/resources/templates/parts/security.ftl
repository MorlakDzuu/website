<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
    current_user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = current_user.getName()
    picture = current_user.getPicture()
    email = current_user.getEmail()
    auth = true
    >
<#else>
    <#assign
    auth = false
    >
</#if>