var a = document.getElementById("Form");

if(a) {
    a.addEventListener('submit', function () {
        if (confirm("Do you really want delete this task ?")) {
            document.form.submit();
        }
    });
}

$(document).ready(function(){
    $('.sidenav').sidenav();
    $('.datepicker').datepicker();
});

var addTaskForm = document.getElementById("addTaskForm");

if(addTaskForm) {
    addTaskForm.addEventListener('submit', function () {
        var chips = document.querySelectorAll('.chip');
        var all = [];
        chips.forEach(function (chip) {
            var str = chip.textContent;
            str = str.replace("close", '');
            all.push(str);
            console.log(str);
        });
        document.getElementById('tags').value = all;
        addTaskForm.submit();
    });
}

$('.chips-placeholder').chips({
    placeholder: 'Enter a tag',
    secondaryPlaceholder: '+Tag',
});

var dataTags = {};
var dataNames = {};

$.getJSON("/getAllTags", function (data) {
    data.forEach(function (dat) {
        dataTags[dat] = null;
    });
})

$('.chips-autocomplete').chips({
    autocompleteOptions: {
        data: dataTags,
        limit: 100,
        minLength: 1
    }
});

$.getJSON("/getAllNames", function (data) {
    data.forEach(function (dat) {
        dataNames[dat] = null;
    });
})

$('.autocomplete').autocomplete({
    data: dataNames
});

var tags = document.querySelectorAll('.deleteTag');
tags.forEach(function (tag) { 
    tag.addEventListener('click', function () {
        var taskId = document.getElementById("taskId").textContent;
        var tagId = tag.textContent.replace("close", '');
        console.log(taskId + ' ' + tagId);
        $.ajax({
            type: 'GET',
            url: '/deleteTag',
            data: 'taskId=' + taskId + '&tagId=' + tagId,
            success: function(data){
            }
        });

    })
})