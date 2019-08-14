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
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.provider.Settings;


import com.mojodigi.ninjafox.Activity.DownLoadSettingActivity;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Task.ExportWhitelistTask;
import com.mojodigi.ninjafox.View.jmmToast;

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {




    private ListPreference downLoadPrefs;
    private String[] downLoadEntries;


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
        final SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        sp.registerOnSharedPreferenceChangeListener(this);





        downLoadEntries = getResources().getStringArray(R.array.setting_entries_download);
        downLoadPrefs = (ListPreference) findPreference(getString(R.string.sp_download));

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
        downLoadPrefs.setSummary(dSummary);





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


        final CheckBoxPreference sp_passwords = (CheckBoxPreference) findPreference(getString(R.string.sp_passwords));

        sp_passwords.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                if (sp_passwords.isChecked()) {
                    jmmToast.show(getActivity(), "password saved disabled");

                    sp_passwords.setChecked(false);
                } else {
                    jmmToast.show(getActivity(), "password saved enabled");
                    sp_passwords.setChecked(true);
                }
                return false;
            }
        });


        // set  the download path  in perefrence screen;
        String dPath=  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        Preference dPathPrefs=findPreference(getString(R.string.download_path_0));
        if(dPath!=null  &&  dPathPrefs!=null) {
            dPathPrefs.setSummary(dPath);
        }



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
            downLoadPrefs.setSummary(summary);
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
