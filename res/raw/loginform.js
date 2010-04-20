javascript:

var ps_useridField = document.getElementsByName('%useridField%');
if(ps_useridField == null) ps_useridField = document.getElementsById('%useridField%');
if(ps_useridField != null) ps_useridField = ps_useridField[0];

var ps_passwdField = document.getElementsByName('%passwdField%');
if(ps_passwdField == null) ps_passwdField = document.getElementsById('%passwdField%');
if(ps_passwdField != null) ps_passwdField = ps_passwdField[0];

if(ps_useridField != null && ps_passwdField != null) {
	ps_useridField.onclick = function() {
		if(ps_useridField.value == '' || ps_passwdField.value == '')
			window.PSJSCALLBACK.showPSLogin('%domain%'); 
	};
}