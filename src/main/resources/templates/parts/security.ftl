<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
    current_user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    auth = true
    >
<#else>
    <#assign
    auth = false
    >
</#if>