//this class is for javascript to call back to drawit class

package com.drawIt;

public class PSJSCallback {
	
	drawIt drawit;
	
	public PSJSCallback(drawIt drawit) {
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
