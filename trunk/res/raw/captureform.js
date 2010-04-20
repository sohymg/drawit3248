javascript:var ps_formList = document.getElementsByTagName('form');

for(var ps_j=0; ps_j<ps_formList.length; ps_j++) {

	var inputList = ps_formList[ps_j].getElementsByTagName('input');
	
	for(var ps_k=0; ps_k<inputList.length; ps_k++) {
		var input = inputList[ps_k];
		var type = input.attributes.getNamedItem('type'); 

		if(type != null && type.value == 'password') {
			input.onblur = function()  {
				var formObj = this.form;
				var formName = null, useridField = null, useridValue, passwdField = null, passwdValue;

				var inputList = formObj.getElementsByTagName('input');

				for(var i=inputList.length - 1; i>=0; i--) {
					var input = inputList[i];
					var type = input.attributes.getNamedItem('type'); 

					if(type != null) {
						if(type.value == 'password') {
							passwdValue = input.value;

							if(input.id != '') passwdField = input.id;
							else passwdField = input.name;
						} 
						else if(passwdField != null && (type.value == 'text' 
							|| (type.value != 'button' && type.value != 'checkbox' && type.value != 'file' 
								&& type.value != 'hidden' && type.value != 'image' && type.value != 'radio'
								&& type.value != 'reset' && type.value != 'submit'))) {

							useridValue = input.value;

							if(input.id != '') useridField = input.id; 
							else useridField = input.name;
						} 
					}
					else if(passwdField != null && input.value != null) {
						useridValue = input.value;

						if(input.id != '') useridField = input.id; 
						else useridField = input.name;
					}
				} 

				if(formObj.id != '') formName = formObj.id;
				else formName = formObj.name;

				if(formName != null && useridField != null && passwdField != null && useridValue != '' && passwdValue != '') {
					
					window.PSJSCALLBACK.showPSSave('%domain%', formName, useridField, useridValue, passwdField, passwdValue);
				}
			}
		}

	}
}