package com.mojodigi.ninjafox.SharedPrefs;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mojodigi.ninjafox.Model.NewsDataModel;
import com.mojodigi.ninjafox.Model.SpeedDialModel;
import com.mojodigi.ninjafox.Unit.CommonUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Takendra
 */

public class SharedPreferenceUtil {
    private static SharedPreferenceUtil mSharedPreferenceUtils;
    protected Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    public SharedPreferenceUtil(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    /**
     * Creates single instance of SharedPreferenceUtils
     *
     * @param context context of Activity or Service
     * @return Returns instance of SharedPreferenceUtils
     */

    public static synchronized SharedPreferenceUtil getInstance(Context context) {

        if (mSharedPreferenceUtils == null) {
            mSharedPreferenceUtils = new SharedPreferenceUtil(context.getApplicationContext());
        }
        return mSharedPreferenceUtils;
    }

    /**
     * Stores String value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */

    public void setValue(String key, String value) {
        mSharedPreferencesEditor.putString(key, value);
        mSharedPreferencesEditor.commit();
    }

    /**
     * Stores int value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    public void setValue(String key, int value) {
        mSharedPreferencesEditor.putInt(key, value);
        mSharedPreferencesEditor.commit();
    }
    public void setIntValue(String key, int value) {
        mSharedPreferencesEditor.putInt(key, value);
        mSharedPreferencesEditor.commit();
    }
    /**
     * Stores Double value in String format in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    public void setValue(String key, double value) {
        setValue(key, Double.toString(value));
    }

    /**
     * Stores long value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    public void setValue(String key, long value) {
        mSharedPreferencesEditor.putLong(key, value);
        mSharedPreferencesEditor.commit();
    }

    /**
     * Stores boolean value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    public void setValue(String key, boolean value) {
        mSharedPreferencesEditor.putBoolean(key, value);
        mSharedPreferencesEditor.commit();
    }

    /**
     * Retrieves String value from preference
     *
     * @param key key of preference
     * @param defaultValue default value if no key found
     */
    public String getStringValue(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    /**
     * Retrieves int value from preference
     *
     * @param key  key of preference
     * @param defaultValue default value if no key found
     */
    public int getIntValue(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    /**
     * Retrieves long value from preference
     *
     * @param key  key of preference
     * @param defaultValue default value if no key found
     */
    public long getLongValue(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    /**
     * Retrieves boolean value from preference
     *
     * @param keyFlag      key of preference
     * @param defaultValue default value if no key found
     */
    public boolean getBoolanValue(String keyFlag, boolean defaultValue) {
        return mSharedPreferences.getBoolean(keyFlag, defaultValue);
    }

    /**
     * Removes key from preference
     *
     * @param key key of preference that is to be deleted
     */
    public void removeKey(String key) {
        if (mSharedPreferencesEditor != null) {
            mSharedPreferencesEditor.remove(key);
            mSharedPreferencesEditor.commit();
        }
    }

    public void saveArrayList(ArrayList<NewsDataModel> list, String key) {

        if (list == null)
            return;
        if (key == null)
            return;
        Gson gson = new Gson();
        String json = gson.toJson(list);
        mSharedPreferencesEditor.putString(key, json);
        mSharedPreferencesEditor.commit();
    }

    public void saveArrayList_Social(ArrayList<SpeedDialModel> list, String key) {

        if (list == null)
            return;
        if (key == null)
            return;
        Gson gson = new Gson();
        String json = gson.toJson(list);
        mSharedPreferencesEditor.putString(key, json);
        mSharedPreferencesEditor.commit();



      /*  Set<SpeedDialModel> set = new HashSet<SpeedDialModel>();
        set.addAll(list);
        mSharedPreferencesEditor.putStringSet(key, set.toString());

        mSharedPreferencesEditor.commit();*/


    }

    public ArrayList<SpeedDialModel> getArrayList_Social(Context context) {
        if (context == null)
            return null;

        List<SpeedDialModel> arrayList;
        if (mSharedPreferences.contains(CommonUtility.PREF_SOCIAL_LIST)) {
            String jsonArrayList = mSharedPreferences.getString(CommonUtility.PREF_SOCIAL_LIST, null);
            Gson gson = new Gson();
            SpeedDialModel[] itemsArrayList = gson.fromJson(jsonArrayList, SpeedDialModel[].class);

            arrayList = Arrays.asList(itemsArrayList);
            arrayList = new ArrayList<SpeedDialModel>(arrayList);
        } else
            return null;

        return (ArrayList<SpeedDialModel>) arrayList;
    }



    public ArrayList<NewsDataModel> getArrayList(Context context) {
        if (context == null)
            return null;

        List<NewsDataModel> arrayList;
        if (mSharedPreferences.contains(CommonUtility.PREF_NEWS_LIST)) {
            String jsonArrayList = mSharedPreferences.getString(CommonUtility.PREF_NEWS_LIST, null);
            Gson gson = new Gson();
            NewsDataModel[] itemsArrayList = gson.fromJson(jsonArrayList, NewsDataModel[].class);

            arrayList = Arrays.asList(itemsArrayList);
            arrayList = new ArrayList<NewsDataModel>(arrayList);
        } else
            return null;

        return (ArrayList<NewsDataModel>) arrayList;
    }

    /**
     * Clears all the preferences stored
     */
    public void clear() {
        mSharedPreferencesEditor.clear().commit();
    }
}