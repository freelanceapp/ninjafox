package com.mojodigi.ninjafox.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.mojodigi.ninjafox.Browser.AlbumController;
import com.mojodigi.ninjafox.Browser.BrowserController;
import com.mojodigi.ninjafox.R;


public class jmmRelativeLayout extends RelativeLayout implements AlbumController {
    private Context context;
    private Album album;
    private int flag = 0;
    private  boolean IsinCog;
    private BrowserController controller;
    public void setBrowserController(BrowserController controller) {
        this.controller = controller;
        this.album.setBrowserController(controller);
    }
    //new code

    public void setBrowserController(BrowserController controller,boolean isInCog) {
        this.controller = controller;
        this.album.setBrowserController(controller);
        //this.album.setIncogTab(isInCog);
    }

    //new code

    public jmmRelativeLayout(Context context) {
        this(context, null);
    }

    public jmmRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public jmmRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.album = new Album(context, this, this.controller);
        initUI();
    }

    private void initUI() {
        album.setAlbumCover(null);
        album.setAlbumTitle(context.getString(R.string.album_untitled));
        album.setBrowserController(controller);
    }

    @Override
    public int getFlag() {
        return flag;
    }

    @Override
    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public View getAlbumView() {
        return album.getAlbumView();
    }

    @Override
    public void setAlbumCover(Bitmap bitmap) {
        album.setAlbumCover(bitmap);
    }

    @Override
    public String getAlbumTitle() {
        return album.getAlbumTitle();
    }

    @Override
    public void setAlbumTitle(String title) {
        album.setAlbumTitle(title);
    }
    @Override
    public void setIsInCogTab(boolean flag) {
       // album.setIncogTab(flag);
        this.IsinCog=flag;
    }

    @Override
    public boolean getIsInCogTab() {
        return  IsinCog;
        //return  album.getIsIncogTab();
    }

    @Override
    public void activate() {
        album.activate();

    }

    @Override
    public void deactivate() {
        album.deactivate();
    }


}
