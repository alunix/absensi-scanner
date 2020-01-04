package com.hiskia.absensi.helper;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by yzzzd on 4/13/16.
 */
public class Loading {
    private ProgressDialog pDialog;
    private Context context;

    public Loading(Context context) {
        this.context = context;
    }

    public void showLoading(String pesan, boolean cancelable) {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(pesan);
        pDialog.setCancelable(cancelable);
        pDialog.show();
    }

    public void dismissDialog() {
        pDialog.dismiss();
    }
}
