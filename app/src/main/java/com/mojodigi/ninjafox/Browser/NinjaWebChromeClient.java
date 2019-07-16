package com.mojodigi.ninjafox.Browser;

import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.mojodigi.ninjafox.View.jmmWebView;



public class NinjaWebChromeClient extends WebChromeClient {
    private jmmWebView customWebView;

    public NinjaWebChromeClient(jmmWebView customWebView) {
        super();
        this.customWebView = customWebView;
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        customWebView.getBrowserController().onCreateView(view, resultMsg);
        return isUserGesture;
    }

    @Override
    public void onCloseWindow(WebView view) {
        super.onCloseWindow(view);
    }

    @Override
    public void onProgressChanged(WebView view, int progress) {
        super.onProgressChanged(view, progress);
        customWebView.update(progress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        customWebView.update(title, view.getUrl());
    }

    @Deprecated
    @Override
    public void onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback callback) {
        customWebView.getBrowserController().onShowCustomView(view, requestedOrientation, callback);
        super.onShowCustomView(view, requestedOrientation, callback);
    }

    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        customWebView.getBrowserController().onShowCustomView(view, callback);
        super.onShowCustomView(view, callback);
    }

    @Override
    public void onHideCustomView() {
        customWebView.getBrowserController().onHideCustomView();
        super.onHideCustomView();
    }

    /* For 4.1 to 4.4 */
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        customWebView.getBrowserController().openFileChooser(uploadMsg);
    }

    /* For 4.1 to 4.4 */
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        customWebView.getBrowserController().openFileChooser(uploadMsg);
    }

    /* For 4.1 to 4.4 */
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        customWebView.getBrowserController().openFileChooser(uploadMsg);
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        customWebView.getBrowserController().showFileChooser(filePathCallback, fileChooserParams);
        return true;
    }

    /**
     * TODO: ?support this method
     * @link http://developer.android.com/reference/android/webkit/WebChromeClient.html#onGeolocationPermissionsShowPrompt%28java.lang.String,%20android.webkit.GeolocationPermissions.Callback%29
     * @param origin
     * @param callback
     */
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }
}
