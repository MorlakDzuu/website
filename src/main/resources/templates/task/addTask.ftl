<#import "../parts/common.ftl" as c>
<#include "../parts/security.ftl">
<@c.page>
<form id="addTaskForm" class="col s8" style="margin-top: 30px;" method="POST" action="/addTask" onsubmit="return false" enctype="multipart/form-data">
    <div class="input-field">
        <input class="val" id="input_text" type="text" value="${task?if_exists.name?if_exists}" name="nameOfTask" required>
        <label for="input_text">Write name of task</label>
    </div>
    <input type="text" class="datepicker" value="${task?if_exists.finish_date?if_exists}" name="finishDate" placeholder="Date of deadline" />
    <div class="input-field">
        <textarea id="textarea2" class="materialize-textarea" name="descriptionOfTask">${task?if_exists.description?if_exists}</textarea>
        <label for="textarea2">Write description of your task</label>
    </div>
    <div class="file-field input-field">
        <div class="btn">
            <span>File</span>
            <input type="file" name="files" multiple>
        </div>
        <div class="file-path-wrapper">
            <input class="file-path validate" type="text" placeholder="Upload one or more files">
        </div>
    </div>
    <input type="hidden" name="taskId" value="${task?if_exists.id?if_exists}">
    <button class="btn waves-effect waves-light" type="submit" name="action">Submit
        <i class="material-icons right">send</i>
    </button>
</form>
<#if task??>
    <ul class="collection with-header">
        <li class="collection-header"><h4>Files</h4></li>
        <#list files?sort_by("filename") as file>
          <li class="collection-item">
              <div>${file.filename}</div>
          </li>
        </#list>
    </ul>
</#if>
<script type="text/javascript">
    var taskForm = document.getElementById("addTaskForm");
    var validateElements = document.querySelectorAll('.val');

    if(taskForm) {
        taskForm.addEventListener('submit', function () {
            if (validateElements[0].value.length > 200) {
                alert("Too many symbols in name");
            } else {
                taskForm.submit();
            }
        });
    }
</script>
</@c.page>