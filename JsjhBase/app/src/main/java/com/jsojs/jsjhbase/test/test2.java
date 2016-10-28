package com.jsojs.jsjhbase.test;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jsojs.baselibrary.util.UpdateManager;
import com.jsojs.jsjhbase.R;

/**
 * Created by root on 16-10-28.
 */

public class test2 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        UpdateManager.getInstance().Update(this);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return UpdateManager.getInstance().getPdialog();

    }
}
