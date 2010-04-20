javascript:

var useridField = document.getElementsByName('%useridField%');
if(useridField == null) useridField = document.getElementsById('%useridField%');
if(useridField != null) useridField = useridField[0];

var passwdField = document.getElementsByName('%passwdField%');
if(passwdField == null) passwdField = document.getElementsById('%passwdField%');
if(passwdField != null) passwdField = passwdField[0];

if(useridField != null && passwdField != null) {
	useridField.onclick = function() {
		if(useridField.value == '' || passwdField.value == '')
			window.JSCALLBACK.showPSLogin('%domain%'); 
	};
}