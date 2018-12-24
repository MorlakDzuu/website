<#import "../parts/common.ftl" as c>
<#include "../parts/security.ftl">
<@c.page>
<div style="margin-top: 30px;">
    <div class="row">
        <div class="col s4 m4">
            <div class="card">
                <div class="card-image">
                    <img src="${picture}">
                </div>
                <div class="card-content">
                    <p class="card-text">${name}</p>
                    <p class="card-text">${email}</p>
                    <p class="card-text">Number of tasks: ${tasksNumber}</p>
                    <p class="card-text">Uploads left: ${downloadsLeft}</p>
                </div>
            </div>
        </div>
        <div class="col s1"></div>
        <div class="col s7">
        <form method="POST" action="/user" enctype="multipart/form-data">
            <div class="input-field">
                <input id="input_text" type="text" name="name" placeholder="${name}">
                <label for="input_text">Write your new nickname</label>
            </div>
            <div class="input-field">
                <input id="email" type="email" class="validate" name="email" placeholder="${email}">
                <label for="email">Write your new email</label>
            </div>
            <div class="file-field input-field">
                <div class="btn">
                    <span>File</span>
                    <input type="file" name="file">
                </div>
                <div class="file-path-wrapper">
                    <input class="file-path validate" type="text">
                </div>
            </div>
            <button class="btn waves-effect waves-light" type="submit" name="action">Submit
                <i class="material-icons right">send</i>
            </button>
        </form>
        </div>
    </div>
</div>
</@c.page>