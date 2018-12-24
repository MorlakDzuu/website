<#import "../parts/common.ftl" as c>
<#include "../parts/security.ftl">
<@c.page>
<div style="margin-top: 30px;">
    <#list tasks?sort_by("date")?reverse as task>
    <div class="row">
        <div class="col s10">
            <a href="/task/${task.id}">
                <div class="card-panel teal">
                    <span class="white-text">${task.name?if_exists}</span>
                    <#if task.finish_date??><span class="yellow-text right">To do before: ${task.finish_date?if_exists}</span></#if>
                </div>
            </a>
        </div>
    </div>
    <#else>
    No tasks, you are retarded
    </#list>
</div>
</@c.page>