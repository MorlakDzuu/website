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

function contains(arr, elem) {
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] === elem) {
            return true;
        }
    }
    return false;
}

var addTaskForm = document.getElementById("addTaskForm");

if(addTaskForm) {
    addTaskForm.addEventListener('submit', function () {
        var chips = document.querySelectorAll('.chip');
        var oldChips = document.querySelectorAll('.old');
        var all = [];
        chips.forEach(function (chip) {
            var str = chip.textContent;
            str = str.replace("close", '');
            if (!contains(oldChips, chip)) {
                all.push(str);
            }
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

var searchForm = document.getElementById("searchForm");
var inputTags = document.getElementById("inputTags");

if (inputTags) {
    inputTags.addEventListener('click', function () {
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
    });
}
if (searchForm) {
    searchForm.addEventListener('click', function () {
        $.getJSON("/getAllNames", function (data) {
            data.forEach(function (dat) {
                dataNames[dat] = null;
            });
        });
        $('.autocomplete').autocomplete({
            data: dataNames
        });
    });
}

var tags = document.querySelectorAll('.deleteTag');
tags.forEach(function (tag) { 
    tag.addEventListener('click', function () {
        var taskId = document.getElementById("taskId").textContent;
        var tagId = tag.textContent.replace("close", '');
        $.ajax({
            type: 'GET',
            url: '/deleteTag',
            data: 'taskId=' + taskId + '&tagId=' + tagId
        });

    })
})