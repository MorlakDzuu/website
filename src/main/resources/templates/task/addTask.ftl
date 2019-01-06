<#import "../parts/common.ftl" as c>
<#include "../parts/security.ftl">
<@c.page>
<div class="container">
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
    <div class="input-field  m-t-0">
        <i class="material-icons prefix">visibility</i>
        <div class="chips chips-placeholder chips-autocomplete m-t-0" id="inputTags"></div>
    </div>
    <input type="hidden" id="tags" name="tags">
    <input type="hidden" name="taskId" value="${task?if_exists.id?if_exists}">
    <button class="btn waves-effect waves-light" type="submit" name="action">Submit
        <i class="material-icons right">send</i>
    </button>
</form>
    <!-- Default with no input (automatically generated)  -->

    <!-- Customizable input  -->

<#if files??>
    <ul class="collection with-header">
        <li class="collection-header"><h4>Files</h4></li>
        <#list files?if_exists?sort_by("filename") as file>
          <li class="collection-item">
              <div>${file.filename}</div>
          </li>
        </#list>
    </ul>
</#if>
</div>
</@c.page>