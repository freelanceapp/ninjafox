package com.mojodigi.ninjafox.Service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Browser.AlbumController;
import com.mojodigi.ninjafox.Browser.BrowserContainer;
import com.mojodigi.ninjafox.Browser.BrowserController;
import com.mojodigi.ninjafox.Unit.BrowserUnit;
import com.mojodigi.ninjafox.Unit.IntentUnit;
import com.mojodigi.ninjafox.Unit.NotificationUnit;
import com.mojodigi.ninjafox.Unit.RecordUnit;
import com.mojodigi.ninjafox.Unit.ViewUnit;
import com.mojodigi.ninjafox.View.jmmContextWrapper;
import com.mojodigi.ninjafox.View.jmmWebView;


public class HolderService extends Service implements BrowserController {
    @Override
    public void updateAutoComplete() {}

    @Override
    public void updateBookmarks() {}

    @Override
    public void updateInputBox(String query) {}

    @Override
    public void updateProgress(int progress) {}

    @Override
    public void showAlbum(AlbumController albumController, boolean anim, boolean expand, boolean capture) {}

    @Override
    public void removeAlbum(AlbumController albumController) {}

    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {}

    @Override
    public void showFileChooser(ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {}

    @Override
    public void onCreateView(WebView view, Message resultMsg) {}

    @Override
    public boolean onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback callback) {
        return true;
    }

    @Override
    public boolean onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        return true;
    }

    @Override
    public boolean onHideCustomView() {
        return true;
    }

    @Override
    public void onLongPress(String url) {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        jmmWebView webView = new jmmWebView(new jmmContextWrapper(this));
        webView.setBrowserController(this);
        webView.setFlag(BrowserUnit.FLAG_NINJA);
        webView.setAlbumCover(null);
        webView.setAlbumTitle(getString(R.string.album_untitled));
        ViewUnit.bound(this, webView);

        webView.loadUrl(RecordUnit.getHolder().getURL());
        webView.deactivate();

        BrowserContainer.add(webView);
        updateNotification();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (IntentUnit.isClear()) {
            BrowserContainer.clear();
        }
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateNotification() {
        Notification notification = NotificationUnit.getHBuilder(this).build();
        startForeground(NotificationUnit.HOLDER_ID, notification);
    }
}
