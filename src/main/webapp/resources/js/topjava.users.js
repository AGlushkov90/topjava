const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
    // $(".checkbox").onclick = function (e)
    // {
    //     checkboxUser($(this).closest('tr').attr("id"), $(this).find(':checkbox')[0].checked);
    // }
    //
    // $(".checkbox").toArray().forEach(function(item, i, arr) {
    //     alert( i + ": " + item + " (массив:" + arr + ")" );
    // });

    $(".checkbox").change(function () {
        // checkboxUser($(this).closest('tr').attr("id"), $(this).find(':checkbox')[0].checked);
        checkboxUser($(this));
    });
});

function checkboxUser(cell) {
    const checked = cell.find(':checkbox')[0].checked;
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl + "change-active",
        data: {
            id: cell.closest('tr').attr("id"),
            active: checked
        }
    })
    if (checked) {
        cell.closest('tr').css('color', 'black');
    } else {
        cell.closest('tr').css('color', 'red');
    }
}
