package com.mojodigi.ninjafox.Browser;

import android.os.Handler;
import android.os.Message;

import com.mojodigi.ninjafox.View.jmmWebView;



public class NinjaClickHandler extends Handler {
    private jmmWebView webView;

    public NinjaClickHandler(jmmWebView webView) {
        super();
        this.webView = webView;
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
        webView.getBrowserController().onLongPress(message.getData().getString("url"));
    }
}
