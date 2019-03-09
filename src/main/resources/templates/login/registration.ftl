<#import "../parts/common.ftl" as c>
<#include "../parts/security.ftl">
<@c.page>
<div class="container">
    <div class="row">
        <form action="/registration" method="post" class="col s12">
            <div class="row">
                <div class="input-field col s6">
                    <i class="material-icons prefix">account_circle</i>
                    <input id="icon_prefix" type="text" class="validate" name="username">
                    <label for="icon_prefix">Nickname</label>
                </div>
                <div class="input-field col s6">
                    <i class="material-icons prefix">email</i>
                    <input id="icon_telephone" type="email" class="validate" name="email">
                    <label for="icon_telephone">Email</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s12">
                    <i class="material-icons prefix">create</i>
                    <input id="password" type="password" class="validate" name="password">
                    <label for="password">Password</label>
                </div>
            </div>
            <button type="submit" class="waves-effect waves-light btn-large"><i class="material-icons right">add_circle</i>Sign in</button>
        </form>
    </div>
</div>
</@c.page>