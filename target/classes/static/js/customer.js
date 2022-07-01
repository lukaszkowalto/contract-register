const stepNumCustomer = 3;

function validateStepCustomer(stepId, type) {
    if (stepId===1 && !(validateCustomerSurogateKey(type) )){
       return false;
    }

  for (const el of document.getElementById('stepCustomer'+stepId+type).querySelectorAll('[required]')) {
        if (!el.reportValidity()) {
          return el.reportValidity();
        }
    }
    return true;
}

function goToStepCustomer(stepId, type) {
  $('#tabListCustomer'+type+' a[href="#stepCustomer'+stepId+type+'"]').tab('show');
  let $progress_bar =  $('.progress-bar');
    $progress_bar.css({width: stepId/stepNumCustomer*100+'%'});
    $progress_bar.text(stepId+'/'+stepNumCustomer);
}

function stepCustomer1Function(e, type) {
    e.stopImmediatePropagation()
    e.preventDefault();
    goToStepCustomer(1,type);
}

function stepCustomer2Function(e, type) {
e.stopImmediatePropagation()
 if (validateStepCustomer(1, type)) {
    goToStepCustomer(2, type);
 }
}

function stepCustomer3Function(e, type) {
e.stopImmediatePropagation()
 if (validateStepCustomer(1, type) && validateStepCustomer(2, type) ) {
    goToStepCustomer(3, type);
 }
}

function stepCustomer1NextFunction(type) {
 if (validateStepCustomer(1, type)) {
    goToStepCustomer(2, type);
 }
}

function stepCustomer2NextFunction(type) {
 if (validateStepCustomer(1, type) && validateStepCustomer(2, type) ) {
    goToStepCustomer(3, type);
 }
}

function stepCustomer2PrewFunction(type) {
goToStepCustomer(1, type);
}

function stepCustomer3PrewFunction(type) {
goToStepCustomer(2, type);
}

function resetModalCustomer(type) {
goToStepCustomer(1, type);
}

function prepareModalCustomer(customerId) {
    $("#customer-type"+customerId).selectpicker('refresh');
    $("#customer-gender"+customerId).selectpicker('refresh');
    $("#customer-country"+customerId).selectpicker('refresh');
    $("#modal-edit-customer"+customerId).modal();
    let type = document.getElementById("customer-type"+customerId).value;
    let person = document.getElementById("person-part"+customerId);
    let business = document.getElementById("business-part"+customerId);
    let name = document.getElementById("customer-name"+customerId);
    let nip = document.getElementById("customer-nip"+customerId);
    let firstName = document.getElementById("customer-firstName"+customerId);
    let lastName = document.getElementById("customer-lastName"+customerId);
    let gender = document.getElementById("customer-gender"+customerId);

    if(type==="PERSON") {
        business.style.display = "none";
        person.style.display = "block";
        name.required = false;
        nip.required = false;
    }
    else {
        business.style.display = "block";
        person.style.display = "none";
        firstName.required = false;
        lastName.required = false;
        gender.required = false;
    }
}

function changePageSize(){
    document.getElementById("customers_page_size").submit();
}

function prepareFormCustomer(customerId) {
    let type = document.getElementById("customer-type"+customerId).value;
    let person = document.getElementById("person-part"+customerId);
    let business = document.getElementById("business-part"+customerId);

    let firstName = document.getElementById("customer-firstName"+customerId);
    let lastName = document.getElementById("customer-lastName"+customerId);
    let gender = document.getElementById("customer-gender"+customerId);
    let pesel = document.getElementById("customer-pesel"+customerId);
    let employeeFlag = document.getElementById("customer-employee-flag"+customerId);

    let name = document.getElementById("customer-name"+customerId);
    let nip = document.getElementById("customer-nip"+customerId);
    let regon = document.getElementById("customer-regon"+customerId);
    let krs = document.getElementById("customer-krs"+customerId);
    if(type==="PERSON") {
        business.style.display = "none";
        firstName.required = true;
        lastName.required = true;
        gender.required = true;

        person.style.display = "block";
        name.value='';
        name.required = false;
        nip.value='';
        nip.required = false;
        regon.value='';
        krs.value='';
    }
    else {
        business.style.display = "block";
        name.required = true;
        nip.required = true;
        person.style.display = "none";
        firstName.value='';
        firstName.required = false;
        lastName.value='';
        lastName.required = false;
        gender.value='';
        gender.required = false;
        pesel.value='';
        employeeFlag.checked = false;
    }
}

function validateCustomerSurogateKey(type) {
    let input;
    let servicePath;
    let result;
    let json;

    if(type.toUpperCase()==="PERSON") {
        input=document.getElementById('customer-pesel');
        servicePath='/getCustomerPeselValidationResult/';
    }
    else {
        input=document.getElementById('customer-nip');
        servicePath='/getCustomerNipValidationResult/';
    }

    let surogateKey=input.value;

    let httpRequest = new XMLHttpRequest();
    httpRequest.open('GET', servicePath + surogateKey , false);
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