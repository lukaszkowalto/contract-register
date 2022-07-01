const stepNumCategory = 3;

function validateStepCategory(stepId) {
    if (stepId===2 && !(validateCategorySymbol())){
       return false;
    }
  for (const el of document.getElementById('stepCategory'+stepId).querySelectorAll('[required]')) {
        if (!el.reportValidity()) {
          return el.reportValidity();
        }
    }
    return true;
}

function goToStepCategory(stepId) {
  $('#tabListCategory a[href="#stepCategory'+stepId+'"]').tab('show');
    let $progress_bar = $('.progress-bar');
    $progress_bar.css({width: stepId/stepNumCategory*100+'%'});
    $progress_bar.text(stepId+'/'+stepNumCategory);
}

function stepCategory1Function(e) {
    e.stopImmediatePropagation()
    e.preventDefault();
    goToStepCategory(1);
}

function stepCategory2Function(e) {
e.stopImmediatePropagation()
 if (validateStepCategory(1)) {
    goToStepCategory(2);
 }
}

function stepCategory3Function(e) {
e.stopImmediatePropagation()
 if (validateStepCategory(1) && validateStepCategory(2) ) {
    goToStepCategory(3);
 }
}

function stepCategory1NextFunction() {
 if (validateStepCategory(1)) {
    goToStepCategory(2);
 }
}

function stepCategory2NextFunction() {
 if (validateStepCategory(1) && validateStepCategory(2) ) {
    goToStepCategory(3);
 }
}

function stepCategory2PrewFunction() {
goToStepCategory(1);
}

function stepCategory3PrewFunction() {
goToStepCategory(2);
}

function resetModalCategory() {
goToStepCategory(1);
}

function changePageSize(){
    document.getElementById("categories_page_size").submit();
}

function prepareModalCategory(categoryId) {
    let selectObject = document.getElementById("category-parent"+categoryId);
    $("#category-year"+categoryId).selectpicker('refresh');
    $("#category-type"+categoryId).selectpicker('refresh');
    $("#modal-edit-category"+categoryId).modal();

    getParentCategoryList(categoryId, selectObject.value);
}

function getParentCategoryList(categoryId){
    let id='';
    let selectedValue='';

    if(typeof(categoryId) != 'undefined' && categoryId != null){
        id=categoryId;
    }

    if(id !== ''){
        let selectedVal=document.getElementById("parent-id"+id).value;

        if(typeof(selectedVal) != 'undefined' && selectedVal != null){
            selectedValue=selectedVal;
        }
    }

    let year = document.getElementById("category-year"+id).value;
    let type = document.getElementById("category-type"+id).value;
    let selectObject = document.getElementById("category-parent"+id);

    for (let i=selectObject.length-1; i>0; i--) {
        selectObject.options.remove(i);
    }

    if(year!=='' && type!=='') {
        $.ajax({
            type: 'GET',
            url: '/getParentCategoryList/' + year +'/'+type+'/'+id,
            success: function(result) {
                let res = JSON.parse(result);
                let $category_parent=$("#category-parent"+id);
                for (let key in res) {
                            if (res.hasOwnProperty(key)) {
                                $category_parent.append($('<option>', {value: key, text: res[key]}));
                            }
                }
                $category_parent.val(selectedValue).selectpicker('refresh');
            }
        });
    }
}

function validateCategorySymbol() {
    let input=document.getElementById('category-symbol');
    let symbol=input.value;
    let select=document.getElementById('category-year');
    let year=select.value;
    let result;
    let json;

    let httpRequest = new XMLHttpRequest();
    httpRequest.open('GET', '/getCategorySymbolValidationResult/' + year +'/' + symbol, false);
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