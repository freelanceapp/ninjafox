package com.mojodigi.ninjafox.Browser;

import android.graphics.Bitmap;
import android.view.View;

public interface AlbumController {
    int getFlag();

    void setFlag(int flag);

    View getAlbumView();

    void setAlbumCover(Bitmap bitmap);

    String getAlbumTitle();

    void setAlbumTitle(String title);

    void activate();

    void deactivate();

    void setIsInCogTab(boolean flag);
    boolean getIsInCogTab();
}
