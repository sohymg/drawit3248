//this class is for javascript to call back to drawit class

package com.drawIt;

public class JSCallback {
	
	drawIt drawit;
	
	public JSCallback(drawIt drawit) {
		this.drawit = drawit;
	}
	
    @SuppressWarnings("unused")
    public void showPSLogin(String domain) {
        drawit.showPSLogin(domain);
    }
    
    public void showPSSave(String domain, String formName, String useridField, String userid, String passwdField, String passwd) {
    	drawit.showPSSave(domain, formName, useridField, userid, passwdField, passwd);
    }
}
