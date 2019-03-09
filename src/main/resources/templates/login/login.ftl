<#import "../parts/common.ftl" as c>
<#include "../parts/security.ftl">
<@c.page>
<div class="container">
    <div class="row">
        <form action="/login" method="post" class="col s12">
            <div class="row">
                <div class="input-field col s12">
                    <i class="material-icons prefix">account_circle</i>
                    <input id="icon_prefix" type="text" class="validate" name="username">
                    <label for="icon_prefix">Nickname</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s12">
                    <i class="material-icons prefix">create</i>
                    <input id="password" type="password" class="validate" name="password">
                    <label for="password">Password</label>
                </div>
            </div>
            <button type="submit" class="waves-effect waves-light btn-large"><i class="material-icons right">assignment_turned_in</i>Log in</button>
        </form>
        <div class="row">
            <a class="btn waves-effect waves-light col s2" href="/registration" style="margin-top: 30px; margin-left: 10px;">Add new user
                <i class="material-icons left">add</i>
            </a>
        </div>
    </div>
</div>
</@c.page>