package com.drawIt;

import android.app.AlertDialog;
import android.content.Context;

public class Util {
	public static void showMsg(Context context, String msg) {
		new AlertDialog.Builder(context)
        .setMessage(msg)
        .setPositiveButton(android.R.string.ok, null)
        .setCancelable(false)
        .create()
        .show();
	}
	
	public static void pl(String msg) {
		System.out.println(msg);
	}
}
