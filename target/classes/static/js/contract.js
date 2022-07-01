const stepNumContract = 5;

function validateStepContract(stepId) {
    if (stepId===1 && !(validateContractNumber())){
       return false;
    }
    for (const el of document.getElementById('stepContract'+stepId).querySelectorAll('[required]')) {
        if (!el.reportValidity()) {
          return false;
        }
    }
    return true;
}

function validateAddFile() {
    for (const el of document.getElementById('add-file-form').querySelectorAll('[required]')) {
        if (!el.reportValidity()) {
          return el.reportValidity();
        }
    }
    return true;
}

function validateAddFDM() {
  for (const el of document.getElementById('add-fdm-form').querySelectorAll('[required]')) {
        if (!el.reportValidity()) {
          return el.reportValidity();
        }
    }
    return true;
}

function goToStepContract(stepId) {
    $('#tabListContract'+' a[href="#stepContract'+stepId+'"]').tab('show');
    let $progress_bar = $('.progress-bar')
    $progress_bar.css({width: stepId/stepNumContract*100+'%'});
    $progress_bar.text(stepId+'/'+stepNumContract);
}

function stepContract1Function(e) {
    e.stopImmediatePropagation();
    e.preventDefault();
    goToStepContract(1);
}

function stepContract2Function(e) {
    e.stopImmediatePropagation();
    if (validateStepContract(1)) {
        goToStepContract(2);
    }
}

function stepContract3Function(e) {
    e.stopImmediatePropagation();
    if (validateStepContract(1) && validateStepContract(2) ) {
        goToStepContract(3);
    }
}

function stepContract4Function(e) {
    e.stopImmediatePropagation();
    if (validateStepContract(1) && validateStepContract(2) && validateStepContract(3) ) {
        goToStepContract(4);
    }
}

function stepContract5Function(e) {
    e.stopImmediatePropagation();
    if (validateStepContract(1) && validateStepContract(2) && validateStepContract(3) && validateStepContract(4) ) {
        goToStepContract(5);
    }
}

function stepContract1NextFunction() {
    if (validateStepContract(1)) {
        goToStepContract(2);
    }
}

function stepContract2NextFunction() {
    if (validateStepContract(1) && validateStepContract(2)) {
        goToStepContract(3);
    }
}

function stepContract3NextFunction() {
    if (validateStepContract(1) && validateStepContract(2) && validateStepContract(3)) {
        goToStepContract(4);
    }
}

function stepContract4NextFunction() {
    if (validateStepContract(1) && validateStepContract(2) && validateStepContract(3) && validateStepContract(4)) {
        goToStepContract(5);
    }
}

function stepContract2PrewFunction() {
    goToStepContract(1);
}

function stepContract3PrewFunction() {
    goToStepContract(2);
}

function stepContract4PrewFunction() {
    goToStepContract(3);
}

function stepContract5PrewFunction() {
    goToStepContract(4);
}

function resetModalContract() {
    goToStepContract(1);
}

function limitDateToContract(e) {
    let id = e.target.id.substring(18);
    let end = document.getElementById('contract-date-to' + id);
    if (e.target.value) {
        end.min = e.target.value;
    }
}

function limitDateFromContract(e) {
    let id = e.target.id.substring(16);
    let start = document.getElementById('contract-date-from' + id);
    if (e.target.value) {
        start.max = e.target.value;
    }
}

function removeRow(btn) {
    let tabRow = btn.parentNode.parentNode;
    tabRow.parentNode.removeChild(tabRow);
}

function checkFDM(contractForm) {
    let id = '';
    if(typeof(contractForm) != 'undefined' && contractForm != null){
        id=contractForm.querySelectorAll("[id^=id]")[0].value;
    }
    let rows = document.getElementById('fdms' + id).rows.length;
    if(rows>1){
        return true;
    }
    return false;
}

function addFile(contractId) {
    let id = '';
    if(typeof(contractId) != 'undefined' && contractId != null){
        id=contractId;
    }
    if (validateAddFile()) {
        let table = document.getElementById('files'+id);
        let filePath = document.getElementById('file-path');
        let fileDescription = document.getElementById('file-description');

        let rowCount = table.rows.length;
        let row = table.insertRow(rowCount);
        let objectIndex=rowCount-1;
		 id=id+'_';

        for (let i = 0; i < rowCount+1; i++) {
            let element = document.getElementById("file-path_"+id+i);

             if(typeof(element) == 'undefined' || element == null){
                objectIndex=i;
                break;
             }
        }

        let cell1 = row.insertCell(0);
        let element1 = document.createElement('input');
        element1.type = "file";
		element1.id="file-path_"+id+objectIndex;
        element1.required = true;
        element1.className ="form-control-file";
        element1.name='contractAttachmentDTOS['+objectIndex+'].file';

        let files = filePath.files;
        let dt = new DataTransfer();

        for(let i=0; i<files.length; i++) {
        let f = files[i];
        dt.items.add(
          new File(
            [f.slice(0, f.size, f.type)],
            f.name
        ));
        }

        element1.files = dt.files;
        cell1.appendChild(element1);

        filePath.value='';

        let cell2 = row.insertCell(1);

        let element2 = document.createElement('input');
        element2.type = "text";
        element2.className ="form-control";
        element2.required = true;
        element2.minLength=1;
        element2.maxLength=256;
        element2.name='contractAttachmentDTOS['+objectIndex+'].description';
        element2.value=fileDescription.value;
        cell2.appendChild(element2);
        fileDescription.value='';

        let cell3 = row.insertCell(2);
        let element3 = document.createElement('button');
        element3.name = "delete-file"+ objectIndex;
        element3.innerHTML='<span class="btn-label"><i class="fas fa-trash-alt"></i></span>';
        element3.className ="btn btn-danger btn-sm";
        element3.onclick = function () {removeRow(this);};
        cell3.appendChild(element3);
        $('#add-file-modal').modal('hide');
    }
}

function getCategoryAndProjectList() {
    let year = document.getElementById("fdm-year").value;
    let category = document.getElementById("fdm-category");
    let project = document.getElementById("fdm-project");

    for (let i=category.length-1; i>0; i--) {
        category.options.remove(i);
    }

    for (let i=project.length-1; i>0; i--) {
        project.options.remove(i);
    }

    if(year!=='') {
        $.ajax({
            type: 'GET',
            url: '/getFDMCategoryList/' + year,
            success: function(result) {
                let res = JSON.parse(result);
                let $fdm_category = $("#fdm-category");
                for(let i = 0; i < res[1].length; i++) {
                    $fdm_category.append($('<option>', {value: res[0][i],text: res[1][i]}));
                }
                $fdm_category.selectpicker('refresh');
            }
        });

        $.ajax({
            type: 'GET',
            url: '/getFDMProjectList/' + year,
            success: function(result) {
                let res2 = JSON.parse(result);
                let $fdm_project=$("#fdm-project");
                for(let i = 0; i < res2[1].length; i++) {
                    $fdm_project.append($('<option>', {value: res2[0][i],text: res2[1][i]}));
                }
                $fdm_project.selectpicker('refresh');
            }
        });
    }
}

function addFDM(contractId) {
    let id='';
    if(typeof(contractId) != 'undefined' && contractId != null){
        id=contractId;
    }
    if (validateAddFDM()) {
        let table = document.getElementById('fdms'+id);
        let fdmYear = document.getElementById('fdm-year');
        let fdmMonth = document.getElementById('fdm-month');
        let fdmCategory = document.getElementById('fdm-category');
        let fdmProject = document.getElementById('fdm-project');
        let fdmAmount = document.getElementById('fdm-amount');
        let fdmAmountVAT = document.getElementById('fdm-amount-vat');

        let rowCount = table.rows.length;
        let row = table.insertRow(rowCount);
        let objectIndex=rowCount-1;
        for (let i = 0; i < rowCount+1; i++) {
            let element = document.getElementById("fdm-month"+id+"_"+i);

             if(typeof(element) == 'undefined' || element == null){
                objectIndex=i;
                break;
             }
        }

        let cell1 = row.insertCell(0);
        cell1.classList.add("col-md-1");
        let element1 = document.createElement('input');
        element1.className ="form-control";
        element1.value = fdmYear.value;
        element1.required = true;
        element1.setAttribute('readonly', true);
        element1.name='fdmDTOS['+objectIndex+'].year';
        cell1.appendChild(element1);

        let cell2 = row.insertCell(1);
        cell2.classList.add("col-md-1");
        let element2 = document.createElement('select');
        element2.innerHTML=fdmMonth.innerHTML;
        element2.required = true;
        element2.id="fdm-month"+id+"_"+objectIndex;
        element2.name='fdmDTOS['+objectIndex+'].month';
        cell2.appendChild(element2);
        $("#fdm-month"+id+"_"+objectIndex).val(fdmMonth.value).selectpicker('refresh');

        let cell3 = row.insertCell(2);
        cell3.classList.add("col-md-3");
        let element3 = document.createElement('select');
        element3.innerHTML=fdmCategory.innerHTML;
        element3.required = true;
        element3.id="fdm-category"+objectIndex;
        element3.name='fdmDTOS['+objectIndex+'].categoryId';
        cell3.appendChild(element3);
        $("#fdm-category"+objectIndex).val(fdmCategory.value).selectpicker('refresh');

        let cell4 = row.insertCell(3);
        cell4.classList.add("col-md-3");
        let element4 = document.createElement('select');
        element4.innerHTML=fdmProject.innerHTML;
        element4.required = true;
        element4.id="fdm-project"+objectIndex;
        element4.name='fdmDTOS['+objectIndex+'].projectId';
        cell4.appendChild(element4);
        $("#fdm-project"+objectIndex).val(fdmProject.value).selectpicker('refresh');

        let cell5 = row.insertCell(4);
        cell5.classList.add("col-md-2");
        let element5 = document.createElement('input');
        element5.innerHTML=fdmAmount.innerHTML;
        element5.name='fdmDTOS['+objectIndex+'].amount';
        element5.required = true;
        element5.value=fdmAmount.value;
        element5.className ="form-control";
        cell5.appendChild(element5);

        let cell6 = row.insertCell(5);
        cell6.classList.add("col-md-2");
        let element6 = document.createElement('input');
        element6.innerHTML=fdmAmountVAT.innerHTML;
        element6.name='fdmDTOS['+objectIndex+'].amountVAT';
        element6.required = true;
        element6.value=fdmAmountVAT.value;
        element6.className ="form-control";
        cell6.appendChild(element6);

        let cell7 = row.insertCell(6);
        let element7 = document.createElement('button');
        element7.name = "delete-fdm"+ objectIndex;
        element7.innerHTML='<span class="btn-label"><i class="fas fa-trash-alt"></i></span>';
        element7.className ="btn btn-danger btn-sm";
        element7.onclick = function () {removeRow(this);};
        cell7.appendChild(element7);
        $('#add-fdm-modal').modal('hide');
    }
}

function prepareModalContract(contractId) {
    $("#modal-edit-contract"+contractId).modal();
}

function prepareModalAddFile(contractId) {
    $("#add-file-modal").modal();
    let id='';
    if(typeof(contractId) != 'undefined' && contractId != null){
        id=contractId;
    }
    let element = document.getElementById("af-contract-id");
    element.value=id;
}

function prepareModalAddFDM(contractId) {
    $("#add-fdm-modal").modal();
    let id='';
    if(typeof(contractId) != 'undefined' && contractId != null){
        id=contractId;
    }
    let element = document.getElementById("fdm-contract-id");
    element.value=id;
}

function changePageSize(){
    document.getElementById("contracts_page_size").submit();
}

function validateContractNumber() {
    let input=document.getElementById('contract-number');
    let contractNumber=input.value;
    let result;
    let json;

    let httpRequest = new XMLHttpRequest();
    httpRequest.open('GET', '/getContractNumberValidationResult/' + contractNumber.replace("/", "_"), false);
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

function currencyChanged(contractId){
    let id='';
    if(typeof(contractId) != 'undefined' && contractId != null){
        id=contractId;
    }
    let currencySelect=document.getElementById("contract-currency"+id);
    let exchangeRateInput=document.getElementById("contract-exchange-rate"+id);

    if((currencySelect.selectedIndex===1 && id==='')||(currencySelect.selectedIndex===0 && id!=='') ){
        exchangeRateInput.value=1;
        exchangeRateInput.readOnly = true;
    }
    else{
        exchangeRateInput.readOnly = false;
        let httpRequest = new XMLHttpRequest();

        httpRequest.onreadystatechange = function() {
            if (this.readyState === 4 && this.status === 200) {
                let result = JSON.parse(httpRequest.responseText);
                exchangeRateInput.value=Math.round((result['rates'][0]['mid'] + Number.EPSILON) * 100) / 100;
            }
        };

        let url = 'http://api.nbp.pl/api/exchangerates/rates/A/'+currencySelect.value+'/last/';
        httpRequest.open('GET', url, true );
        httpRequest.send();
    }
}