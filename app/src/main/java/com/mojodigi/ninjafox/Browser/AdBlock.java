package com.mojodigi.ninjafox.Browser;

import android.content.Context;
import android.content.res.AssetManager;

import com.mojodigi.ninjafox.Database.RecordAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;




/*
*
*
* // tks
*
* https://www.hidroh.com/2016/05/19/hacking-up-ad-blocker-android/
* //  comlpete guide to  adblock  implementation;
*
*
*
* */

public class AdBlock {
    private static final String FILE = "add_providers.txt";
    private static final Set<String> addHosts = new HashSet<>();
    private static final List<String> whitelist = new ArrayList<>();
    private static final Locale locale = Locale.getDefault();

    private static void loadHosts(final Context context) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                AssetManager manager = context.getAssets();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(manager.open(FILE)));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        addHosts.add(line.toLowerCase(locale));
                    }
                } catch (IOException i) {}
            }
        });
        thread.start();
    }

    private synchronized static void loadDomains(Context context) {
        RecordAction action = new RecordAction(context);
        action.open(false);
        whitelist.clear();
        for (String domain : action.listDomains()) {
            whitelist.add(domain);
        }
        action.close();
    }

    private static String getDomain(String url) throws URISyntaxException {
        url = url.toLowerCase(locale);

        int index = url.indexOf('/', 8); // -> http://(7) and https://(8)
        if (index != -1) {
            url = url.substring(0, index);
        }

        URI uri = new URI(url);
        String domain = uri.getHost();
        if (domain == null) {
            return url;
        }
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    private Context context;

    public AdBlock(Context context) {
        this.context = context;

        if (addHosts.isEmpty()) {
            loadHosts(context);
        }
        loadDomains(context);
    }

    public boolean isWhite(String url) {
        for (String domain : whitelist) {
            if (url.contains(domain)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAd(String url) {
        String domain;
        try {
            domain = getDomain(url);
        } catch (URISyntaxException u) {
            return false;
        }
        return addHosts.contains(domain.toLowerCase(locale));
    }

    public synchronized void addDomain(String domain) {
        RecordAction action = new RecordAction(context);
        action.open(true);
        action.addDomain(domain);
        action.close();
        whitelist.add(domain);
    }

    public synchronized void removeDomain(String domain) {
        RecordAction action = new RecordAction(context);
        action.open(true);
        action.deleteDomain(domain);
        action.close();
        whitelist.remove(domain);
    }

    public synchronized void clearDomains() {
        RecordAction action = new RecordAction(context);
        action.open(true);
        action.clearDomains();
        action.close();
        whitelist.clear();
    }
}
