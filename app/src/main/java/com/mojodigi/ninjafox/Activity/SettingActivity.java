package com.mojodigi.ninjafox.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Fragment.SettingFragment;
import com.mojodigi.ninjafox.Task.ImportBookmarksTask;
import com.mojodigi.ninjafox.Task.ImportWhitelistTask;
import com.mojodigi.ninjafox.Unit.IntentUtility;
import com.mojodigi.ninjafox.View.jmmToast;

import java.io.File;



public class SettingActivity extends Activity {
    private SettingFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.clear_all_back_color)));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        fragment = new SettingFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                IntentUtility.setDBChange(fragment.isDBChange());
                IntentUtility.setSPChange(fragment.isSPChange());
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            IntentUtility.setDBChange(fragment.isDBChange());
            IntentUtility.setSPChange(fragment.isSPChange());
            finish();
            return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentUtility.REQUEST_BOOKMARKS) {
            if (resultCode != Activity.RESULT_OK || data == null || data.getData() == null) {
                jmmToast.show(this, R.string.toast_import_bookmarks_failed);
            } else {
                File file = new File(data.getData().getPath());
                new ImportBookmarksTask(fragment, file).execute();
            }
        } else if (requestCode == IntentUtility.REQUEST_WHITELIST) {
            if (resultCode != Activity.RESULT_OK || data == null || data.getData() == null) {
                jmmToast.show(this, R.string.toast_import_whitelist_failed);
            } else {
                File file = new File(data.getData().getPath());
                new ImportWhitelistTask(fragment, file).execute();
            }
        } else if (requestCode == IntentUtility.REQUEST_CLEAR) {
            if (resultCode == Activity.RESULT_OK && data != null && data.hasExtra(ClearActivity.DB_CHANGE)) {
                fragment.setDBChange(data.getBooleanExtra(ClearActivity.DB_CHANGE, false));
            }
        }
    }
}
