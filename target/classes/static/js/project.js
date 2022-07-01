const stepNumProject = 3;

function validateStepProject(stepId) {
    if (stepId===2 && !(validateProjectSymbol())){
       return false;
    }
  for (const el of document.getElementById('stepProject'+stepId).querySelectorAll('[required]')) {
        if (!el.reportValidity()) {
          return el.reportValidity();
        }
    }
    return true;
}

function goToStepProject(stepId) {
  $('#tabListProject a[href="#stepProject'+stepId+'"]').tab('show');
    let $progress_bar=$('.progress-bar')
    $progress_bar.css({width: stepId/stepNumProject*100+'%'});
    $progress_bar.text(stepId+'/'+stepNumProject);
}

function stepProject1Function(e) {
    e.stopImmediatePropagation()
    e.preventDefault();
    goToStepProject(1);
}

function stepProject2Function(e) {
e.stopImmediatePropagation()
 if (validateStepProject(1)) {
    goToStepProject(2);
 }
}

function stepProject3Function(e) {
e.stopImmediatePropagation()
 if (validateStepProject(1) && validateStepProject(2) ) {
    goToStepProject(3);
 }
}

function stepProject1NextFunction() {
 if (validateStepProject(1)) {
    goToStepProject(2);
 }
}

function stepProject2NextFunction() {
 if (validateStepProject(1) && validateStepProject(2) ) {
    goToStepProject(3);
 }
}

function stepProject2PrewFunction() {
    goToStepProject(1);
}

function stepProject3PrewFunction() {
    goToStepProject(2);
}

function resetModalProject() {
goToStepProject(1);
}

function prepareModalProject(projectId) {
    let selectObject = document.getElementById("project-parent"+projectId);
    $("#project-year"+projectId).selectpicker('refresh');
    $("#project-type"+projectId).selectpicker('refresh');
    $("#modal-edit-project"+projectId).modal();
    getParentProjectList(projectId, selectObject.value);
}

function limitDateToProject(e) {
    let id = e.target.id.substring(17);
    let end = document.getElementById('project-date-to'+id);
    if (e.target.value) {
        end.min = e.target.value;
    }
}

function limitDateFromProject(e) {
    let id = e.target.id.substring(15);
    let start = document.getElementById('project-date-from'+id);
    if (e.target.value) {
        start.max = e.target.value;
    }
}

function changePageSize(){
    document.getElementById("projects_page_size").submit();
}

function getParentProjectList(projectId){
    let id='';
    let selectedValue='';

    if(typeof(projectId) != 'undefined' && projectId != null){
        id=projectId;
    }

    if(id !== ''){
        let selectedVal=document.getElementById("parent-id"+id).value;

        if(typeof(selectedVal) != 'undefined' && selectedVal != null){
            selectedValue=selectedVal;
        }
    }

    let year = document.getElementById("project-year"+id).value;
    let type = document.getElementById("project-type"+id).value;
    let selectObject = document.getElementById("project-parent"+id);

    for (let i=selectObject.length-1; i>0; i--) {
        selectObject.options.remove(i);
    }

    if(year!=='' && type!=='') {
        $.ajax({
            type: 'GET',
            url: '/getParentProjectList/' + year +'/'+type+'/'+id,
            success: function(result) {
                let rs = JSON.parse(result);
                let $project_parent= $("#project-parent"+id)
                for (let key in rs) {
                    if (rs.hasOwnProperty(key)) {
                        $project_parent.append($('<option>', {value: key, text: rs[key]}));
                    }
                }
                $project_parent.val(selectedValue).selectpicker('refresh');
            }
        });
    }
}

function validateProjectSymbol() {
    let input=document.getElementById('project-symbol');
    let symbol=input.value;
    let select = document.getElementById('project-year');
    let year=select.value;
    let result;
    let json;

    let httpRequest = new XMLHttpRequest();
    httpRequest.open('GET', '/getProjectSymbolValidationResult/' + year +'/' + symbol, false);
    httpRequest.send();

    if (httpRequest.status === 200) {
        json = JSON.parse(httpRequest.responseText);
        result = (json['result']==='true');
    }

    if (result===false){
        input.value="";
        alert(json['message']);
    }

    return result;
}