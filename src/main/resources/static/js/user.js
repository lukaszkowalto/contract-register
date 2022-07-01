function prepareModalUser(userId) {
    $("#user-role"+userId).selectpicker('refresh');
    $("#modal-edit-user"+userId).modal();
}

function changePageSize(){
    document.getElementById("users_page_size").submit();
}