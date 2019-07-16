package com.mojodigi.ninjafox.View;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;

import com.mojodigi.ninjafox.R;



public class jmmContextWrapper extends ContextWrapper {
    private Context mContext;

    public jmmContextWrapper(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.mContext.setTheme(R.style.BrowserActivityTheme);
    }

    @Override
    public Resources.Theme getTheme() {
        return mContext.getTheme();
    }
}
