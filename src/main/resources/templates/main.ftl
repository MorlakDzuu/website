<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
<#if !auth>
<a href="/login" class="btn waves-effect waves-teal">LOGIN</a>
</#if>
<#if auth>
<a href="/logout" class="btn waves-effect waves-teal">LOGOUT</a>
</#if>

</@c.page>