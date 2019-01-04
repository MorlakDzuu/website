<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
<div class="container">
<#if !auth>
<a href="/login" class="btn waves-effect waves-teal" style="margin-top: 50px;">LOGIN</a>
</#if>
<#if auth>
<a href="/logout" class="btn waves-effect waves-teal" style="margin-top: 50px;">LOGOUT</a>
</#if>
</div>
</@c.page>