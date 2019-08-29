package com.mojodigi.ninjafox.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;


import com.mojodigi.ninjafox.Activity.DownLoadSettingActivity;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Task.ExportWhitelistTask;
import com.mojodigi.ninjafox.Unit.BrowserUtility;
import com.mojodigi.ninjafox.View.jmmToast;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener  {




    private ListPreference mDownLoadPrefs;
    private MultiSelectListPreference mClearDataPrefs; 
    private String[] downLoadEntries;
    SharedPreferences settingsPrefs;

    private boolean spChange = false;

    public boolean isSPChange() {
        return spChange;
    }

    private boolean dbChange = false;



    public boolean isDBChange() {
        return dbChange;
    }

    public void setDBChange(boolean dbChange) {
        this.dbChange = dbChange;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);
    }

    @Override
    public void onResume() {
        super.onResume();
        settingsPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        sp.registerOnSharedPreferenceChangeListener(this);
          /*download prefs*/
        downLoadEntries = getResources().getStringArray(R.array.setting_entries_download);
        mDownLoadPrefs = (ListPreference) findPreference(getString(R.string.sp_download));



        String  dFlag = sp.getString(getString(R.string.sp_download), "0");
        String dSummary="";

        if(dFlag.equalsIgnoreCase("0"))
        {
            dSummary=getString(R.string.download_setting_both);
        }
        else if(dFlag.equalsIgnoreCase("1"))
        {
            dSummary=getString(R.string.download_setting_wifi);
        }
        else if(dFlag.equalsIgnoreCase("2"))
        {
            dSummary=getString(R.string.download_setting_mobile);
        }
        else
        {
            dSummary=getString(R.string.download_setting_both);
        }
        mDownLoadPrefs.setSummary(dSummary);

        /*download prefs*/




        /*clear  data  prefs*/

        mClearDataPrefs=(MultiSelectListPreference)findPreference(getString(R.string.sp_clear_data));
        mClearDataPrefs.setDefaultValue(getResources().getStringArray(R.array.values_clear_data_default));    /*  <!--initilize with  blank  array to  keep all unselected-->*/

        mClearDataPrefs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                 int a =23_12;

                Set<String> selected   = (Set) o;
                System.out.print(""+selected);
                Iterator<String> itr = selected.iterator();

                while(itr.hasNext()){
                    String selectedOption = itr.next();

                    /**/
                    switch (selectedOption)
                    { case ("0"):  // form  data
                        {
                            BrowserUtility.clearFormData(getActivity());
                            break;
                        }
                        case ("1"):
                        {
                            BrowserUtility.clearHistory(getActivity());
                            break;
                        }

                        case ("2"):
                        {
                            BrowserUtility.clearBookmarks(getActivity());
                            break;
                        }
                        case ("3"):
                        {
                            BrowserUtility.clearCookie(getActivity());
                            break;
                        }
                        case ("4"):
                        {
                            BrowserUtility.clearCache(getActivity());
                            break;
                        }
                    }
                    /**/


                }
                jmmToast.show(getActivity(), R.string.toast_clear_successful);

                return false;
            }
        });

        /*clear  data  prefs*/



        Preference verPref = findPreference(getString(R.string.setting_title_version));
        if (verPref != null) {
            String version = "";
            try {
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                version = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            verPref.setSummary(version);
        }

        final CheckBoxPreference sp_location = (CheckBoxPreference) findPreference(getString(R.string.sp_location));


        boolean st = isGpsEnabled();

        if (checkIfLocationOpened()) {
            sp_location.setChecked(true);
        }
        else
        {
            sp_location.setChecked(false);
        }


        sp_location.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {


                if (sp_location.isChecked()) {
                    disableLocation();

                } else {
                    statusCheck();
                }
                return false;
            }
        });

 /* <!-- for the time being not required-->*/
       /* final CheckBoxPreference sp_passwords = (CheckBoxPreference) findPreference(getString(R.string.sp_passwords));
        sp_passwords.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                if (sp_passwords.isChecked()) {
                    setPassWordPrefs(false);
                    sp_passwords.setChecked(false);
                } else {
                    setPassWordPrefs(true);
                    sp_passwords.setChecked(true);
                }
                return false;
            }
        });*/
        /* <!-- for the time being not required-->*/

        // set  the download path  in perefrence screen;
        String dPath=  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        Preference dPathPrefs=findPreference(getString(R.string.download_path_0));
        if(dPath!=null  &&  dPathPrefs!=null) {
            dPathPrefs.setSummary(dPath);
        }
    }
    private void setPassWordPrefs(boolean b) {

        /*the  value  is  being used  in jmmWebView class for storing from data  of a website*/
        SharedPreferences.Editor editor = settingsPrefs.edit();
        editor.putBoolean(getString(R.string.sp_passwords),b);
        editor.commit();
    }


    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        switch (preference.getTitleRes()) {
            case R.string.setting_title_version:
                jmmToast.show(getActivity(), "version");
                break;
            case R.string.download_path_0:
                jmmToast.show(getActivity(), "download path");
            default:
                break;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        spChange = true;
        if (key.equals(getString(R.string.sp_download))) {
            String summary = downLoadEntries[Integer.valueOf(sp.getString(key, "0"))];
            mDownLoadPrefs.setSummary(summary);
        }




    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private boolean isGpsEnabled() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            return false;
        }

    }
    private void disableLocation()
    {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    private boolean checkIfLocationOpened() {
        String provider = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps") || provider.contains("network"))
            return true;
        else
            // otherwise return false
            return false;
    }



    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
