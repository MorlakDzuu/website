<#import "../parts/common.ftl" as c>
<#include "../parts/security.ftl">
<@c.page>
<a href="/taskList" class="btn-floating btn-large waves-effect waves-light" style="position: fixed;"><i class="material-icons">arrow_back</i></a>
<div class="container">
<div class="row">
    <div id="taskId" hidden="true">${task.id}</div>
    <#assign big="s12">
    <#if task.finish_date??><#assign big="s8"></#if>
    <div class="col ${big}">
        <div class="media">
            <div class="media-body">
                <h5 class="mt-3 mb-1">${task.name?if_exists}</h5>
                <div class="mt-2">
                    <p>
                        ${task.description?if_exists}
                    </p>
                </div>
            </div>
        </div>
    </div>
    <#if task.finish_date??>
    <div class="col s4">
        <h1 class="display-4">${days?if_exists}</h1>
    </div>
    </#if>
</div>
    <#list tags?if_exists as tag>
    <div class="chip green m-t-4">
        <span class="white-text">${tag.tag}</span>
        <i class="close material-icons deleteTag">close<div hidden="true">${tag.id}</div></i>
    </div>
    </#list>
<#if files??>
    <ul class="collection with-header">
        <li class="collection-header"><h4>Files</h4></li>
        <#list files?sort_by("filename") as file>
          <li class="collection-item">
              <div>${file.filename}
                  <a class="close secondary-content" href="/task/file/${file.id}">
                      <i class="material-icons">file_download</i>
                  </a>
                  <a class="secondary-content" href="/task/file/${file.id}/delete" style="margin-right: 20px; color: red;">
                      <i class="material-icons">delete</i>
                  </a>
              </div>
          </li>
        </#list>
    </ul>
</#if>
<div class="row">
    <form class="col s2" name="editTask" method="get" action="/task/${task.id}/edit" id="editTaskForm">
        <button type="submit" class="btn waves-effect waves-light">Edit</button>
    </form>
</div>
<div class="row">
    <form class="col s2" name="form" method="get" action="/task/${task.id}/delete" onsubmit="return false" id="Form">
        <button type="submit" class="btn waves-effect waves-light red">Delete</button>
    </form>
</div>
</div>
</@c.page>