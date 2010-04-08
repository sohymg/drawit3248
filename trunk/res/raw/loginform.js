javascript:

var useridField = document.getElementsByName('%useridField%')[0];
if(useridField == null) useridField = document.getElementsById('%useridField%')[0];

var passwdField = document.getElementsByName('%passwdField%')[0];
if(passwdField == null) passwdField = document.getElementsById('%passwdField%')[0];

if(useridField != null && passwdField != null) {
	useridField.onclick = function() {
		if(useridField.value == '' || passwdField.value == '')
			window.JSCALLBACK.showPSLogin('%domain%'); 
	};
}