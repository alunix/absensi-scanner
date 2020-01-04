package com.hiskia.absensi.ui;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

/**
 * Created by crocodic on 3/5/18.
 */

public class BaseActivity extends AppCompatActivity {

    private Toast toast;
    private ProgressBar progressBar;
    public Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void toast(@StringRes int message) {
        toast(getString(message));
    }

    public void toast(String toastMessage) {
        if (toastMessage != null && !toastMessage.isEmpty()) {
            if (toast != null) toast.cancel();
            toast = Toast.makeText(this.getApplicationContext(), toastMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}