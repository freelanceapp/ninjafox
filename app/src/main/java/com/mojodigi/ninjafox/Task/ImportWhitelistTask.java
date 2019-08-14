package com.mojodigi.ninjafox.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.mojodigi.ninjafox.Fragment.SettingFragment;
import com.mojodigi.ninjafox.R;

import com.mojodigi.ninjafox.Unit.BrowserUtility;
import com.mojodigi.ninjafox.View.jmmToast;

import java.io.File;


public class ImportWhitelistTask extends AsyncTask<Void, Void, Boolean> {
    private SettingFragment fragment;
    private Context context;
    private ProgressDialog dialog;
    private File file;
    private int count;

    public ImportWhitelistTask(SettingFragment fragment, File file) {
        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.dialog = null;
        this.file = file;
        this.count = 0;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage(context.getString(R.string.toast_wait_a_minute));
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        count = BrowserUtility.importWhitelist(context, file);

        if (isCancelled()) {
            return false;
        }
        return count >= 0;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        dialog.hide();
        dialog.dismiss();

        if (result) {
            fragment.setDBChange(true);
            jmmToast.show(context, context.getString(R.string.toast_import_whitelist_successful) + count);
        } else {
            jmmToast.show(context, R.string.toast_import_whitelist_failed);
        }
    }
}
