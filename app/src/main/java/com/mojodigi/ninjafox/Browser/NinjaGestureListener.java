package com.mojodigi.ninjafox.Browser;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.mojodigi.ninjafox.View.jmmWebView;



public class NinjaGestureListener extends GestureDetector.SimpleOnGestureListener {
    private jmmWebView webView;
    private boolean longPress = true;

    public NinjaGestureListener(jmmWebView webView) {
        super();
        this.webView = webView;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (longPress) {
            webView.onLongPress();
        }
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        longPress = false;
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        longPress = true;
    }
}
