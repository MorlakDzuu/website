var a = document.getElementById("Form");

if(a) {
    a.addEventListener('submit', function () {
        if (confirm("Do you really want delete this task ?")) {
            document.form.submit();
        }
    });
}