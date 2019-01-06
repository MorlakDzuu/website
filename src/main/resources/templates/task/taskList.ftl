<#import "../parts/common.ftl" as c>
<#include "../parts/security.ftl">
<@c.page>
<div class="container">
    <div style="margin-top: 30px;">
        <#list data?if_exists as elem>
            <div class="row">
                <div class="col s10">
                    <a href="/task/${elem.task.id}<#if currentPage??>?currentPage=${currentPage}</#if>">
                        <div class="card-panel teal z-depth-5">
                            <span class="white-text">${elem.task.name?if_exists}</span>
                            <#if elem.task.finish_date??><span class="yellow-text right">To do before: ${elem.task.finish_date?if_exists}</span></#if>
                        </div>
                    </a>
                    <#list elem.tag as tag>
                      <div class="chip green m-t-4"><span class="white-text">${tag.tag}</span></div>
                    </#list>
                </div>
                <div class="col s2">
                    <div id="deleteTask${elem.task.id}" style="cursor: pointer;">
                        <div class="card-panel red z-depth-5">
                            <span class="white-text">Delete</span>
                        </div>
                    </div>
                </div>
            </div>
            <script type="text/javascript">
                var deleteTask = document.getElementById("deleteTask${elem.task.id}");
                deleteTask.addEventListener('click', function () {
                    if(confirm("Do you want to delete task ${elem.task.name}")) {
                    document.location.href = '/task/${elem.task.id}/delete';
                }})
            </script>
        <#else>
            <div class="red-text">Sorry, but you don't have tasks :(</div>
        </#list>
    </div>
    <#if tasksNumber??>
        <#if !(tasksNumber < 11)>
            <ul class="pagination">
                <#list 1 .. (tasksNumber / 10 + 1) as number>
                    <li class="waves-effect <#if currentPage??><#if currentPage == number>active</#if><#else><#if number == 1>active</#if></#if>"><a href="/taskList?pageNumber=${number - 1}">${number}</a></li>
                </#list>
            </ul>
        </#if>
    </#if>
</div>
</@c.page>