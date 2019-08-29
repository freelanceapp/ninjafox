package com.mojodigi.ninjafox.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Database.Record;
import com.mojodigi.ninjafox.Service.HolderService;
import com.mojodigi.ninjafox.Unit.BrowserUtility;
import com.mojodigi.ninjafox.Unit.IntentUtility;
import com.mojodigi.ninjafox.Unit.RecordUnit;
import com.mojodigi.ninjafox.View.DialogAdapter;
import com.mojodigi.ninjafox.View.jmmContextWrapper;
import com.mojodigi.ninjafox.View.jmmToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class HolderActivity extends Activity {


    private Record first = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getData() == null) {
            finish();
            return;
        }

        first = new Record();
        first.setTitle(getString(R.string.album_untitled));
        first.setURL(getIntent().getData().toString());
        first.setTime(System.currentTimeMillis());

        Intent toActivity = new Intent(HolderActivity.this, MainActivity.class);
        toActivity.putExtra(IntentUtility.OPEN, first.getURL());
        startActivity(toActivity);
        finish();

    }


    @Override
    public void onDestroy() {

        first = null;
        super.onDestroy(); }
}
