package com.example.mediahider.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.Calendar;
import java.util.UUID;


/**
 * Created by ashish123 on 8/9/15.
 */
public class AppThemePrefrences {

    public static final String PUBLIC_DIRECTORY_HIDERBACKUP = "AVXPlayer";

    public static final String IS_OLD_USER = "IS_OLD_USER";
    public static final String DP_DATA_API_TIME = "DP_DATA_API_TIME";
    public static final String SAVE_DIALOG_DATA = "save_dialog_data";

    public static final String LAST_TOP_BUTTON = "LAST_TOP_BUTTON";
    public static final String IS_REELS_HELP_SHOW = "IS_REELS_HELP_SHOW";


    public static final String ON_BOARDING_TYPE_VALUE ="ON_BOARDING_TYPE";

    public static final String FIRST_TIME = "FIRST_TIME";
    public static final String RESUME_PLAY = "RESUME_PLAY";
    public static final String RESUME_STATUS = "RESUME_STATUS";
    public static final String PINCH_TO_ZOOM = "PINCH_TO_ZOOM";
    public static final String DEFAULT_SUBTITLE = "DEFAULT_SUBTITLE";
    public static final String BATTERY_TIME = "BATTERY_TIME";
    public static final String REMEMBER_BRIGHTNESS = "REMEMBER_BRIGHTNESS";
    public static final String ORIENTATION_PLAYER_STATUS = "rotate";
    public static final String SHOW_HIDDEN_FILES = "show_hidden_files";
    public static final String SOFTWARE_DECODER = "SOFTWARE_DECODER";
    public static final String CLOSE_ALL_TAB_FROM_NOTIFICATION = "CLOSE_ALL_TAB_FROM_NOTIFICATION";
    public static final String MUSIC_SORT_BY = "MUSIC_SORT_BY";

    public static final String HIDER_URI = "HIDER_URI";
    public static final String CLEAR_CACHE = "CLEAR_CACHE";

    public static final String PYO_USER = "YOYO_DONE";
    public static final String PYO_JSON_DATA = "PYO_JSON_DATA";
    public static final String PYO_ORD = "PYO_ORD";
    public static final String PYO_TOKN = "PYO_TOKN";
    public static final String PYO_STATE = "PYO_STATE";
    public static final String PYO_SKU = "PYO_SKU";
    public static final String PYO_TIME_STAMP = "PYO_TIME_STAMP";


    public static int NUMBER_OF_MINUTES_FROM_MILLIS = 1000 * 60;
    public static  final String EQUALIZER_SAVED_FILE_NAME ="rocks_eqz.ser";
    public static final String NIGHT_MODE = "NIGHT_MODE";
    public static final String AUTO_PLAY = "AUTO_PLAY";
    public static final String LOOP_FLAG = "LOOP_FLAG";
    public static final String LIST_COLUMN_COUNT = "LIST_COLUMN_COUNT";
    public static final String PHOTO_LIST_COLUMN_COUNT = "PHOTO_LIST_COLUMN_COUNT";
    public static final String SCREEN_BRIGHTNESS = "SCREEN_BRIGHTNESS" ;
    public static final String APP_LANGAUGE = "APP_LANGAUGE";
    public static final long   DOUBLETAP_DURATION = 300;
    public static final String VIDEO_SORT_BY = "VIDEO_SORT_BY";
    public static final String PHOTO_SORT_BY = "PHOTO_SORT_BY";
    public static final String VIDEO_FOLDER_SORT_BY = "VIDEO_FOLDER_SORT_BY";
    public static final String PHOTO_ALBUM_SORT_BY = "VIDEO_FOLDER_SORT_BY";
    public static final String TEMP_VALE = "YOU_KNOW_THE";
    public static final String PIN_RECOVERY_EMAILID = "PIN_RECOVERY_EMAILID";
    public static final String USER_UUID = "USER_UUID";
    public static final String SPLASH_OPENED_COUNT = "SPLASH_OPENED_COUNT";
    public static final String LASTOPENTIME = "LASTOPENTIME";

    public static final String FIRST_TIME_OPEN_APP = "FIRST_TIME_OPEN_APP";
    public static final String DELETE_PHOTO_DIALOG_NOT_SHOW = "DELETE_PHOTO_DIALOG_NOT_SHOW";
    public static final String NETWORK_STREAM = "NETWORK_STREAM";
    public static final String SLEEP_TIME = "SLEEP_TIME";
    public static final String LOGOUT_ONLINE = "LOGOUT_ONLINE_DIALOG_NOT_SHOW";
    public static final String PIN_VALUE = "PIN_VALUE";
    public static final String PIN_RECOVERY_EMAILID_NEW = "PIN_RECOVERY_EMAILID_NEW";
    public static final String EXTENSION_MODE = "EXTENSION_MODE";
    public static final String DELETE_DIALOG_NOT_SHOW_PHOTO = "PHOTO_DELETE_DIALOG";
    public static final short DEFAULT_BASSBBOST = 10;
    public static final int EQ_STATE_ENABLED = 0 ;
    public static final int EQ_STATE_DISABLED = 1 ;
    public static final String INTERSTITIAL_AD_OPENED_TIME = "INTS_AD_OPEN_TIME";
    public static final String GALLERY_AD_OPENED_TIME = "GALLERY_AD_OPEN_TIME";
    public static final String SPLASH_OPENED_TIME_AT_FIRST_LAUNCH = "SPLASH_OPENED_TIME_AT_FIRST_LAUNCH";
    public static final String SHOW_HISTORY_ON_HOME = "HISTORY_ON_HOME";
    public static String MUSIC_VR_LAST_VALUE = "music_vr_last_value";
    public static String MUSIC_BB_LAST_VALUE = "music_bb_value";

    public final  static String EQUILIZER_SELECTED_REVERB="equilizer_selected_reverb";

    //10 for custom and 1 to 5 for band
    public final  static String EQUILIZER_SELECTED_BAND="eqz_select_band";

    //0 for enable and 1 for disable
    public static final String EQ_ENABLED_SETTINGS = "EQ_ENABLED";

    public static final String DO_NOT_ASK_DENY = "do_not_ask";
    public static boolean swDecoderMode = false;
    public static final String REPEAT_MODE = "REPEAT_MODE";

    public static SharedPreferences pref;

    public static void init(Context context) {
        if (pref == null) {
            pref = context.getSharedPreferences("com.music.rockes", Context.MODE_PRIVATE);
        }
    }

    public static void SetSharedPreference(Context ctx, String Key, String Value) {
        init(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Key, Value);
        editor.apply();
    }

    public static String GetSharedPreference(Context ctx, String Key) {
        if (ctx!=null) {
            init(ctx);
            if (pref.contains(Key)) {
                return pref.getString(Key, "");
            } else
                return null;
        }else {
            return null;
        }
    }

    public static void SetBooleanSharedPreference(Context ctx, String Key, boolean Value) {
        init(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Key, Value);
        editor.apply();
    }

    public static void SetBooleanSharedPreferenceMultiProcess(Context ctx, String Key, boolean Value) {
        init(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Key, Value);
        editor.apply();
    }



    public static boolean GetBooleanSharedPreference(Context ctx, String Key) {
        init(ctx);
        if (pref.contains(Key)) {
            return pref.getBoolean(Key, false);
        } else
            return false;
    }


    public static boolean GetBooleanSharedPreferenceMultiProcess(Context ctx, String Key) {
        init(ctx);
        if (pref.contains(Key)) {
            return pref.getBoolean(Key, false);
        } else
            return false;
    }

    public static boolean GetBooleanSharedPreference(Context ctx, String Key, boolean defaultvalue) {
        init(ctx);
        if (pref.contains(Key)) {
            return pref.getBoolean(Key, defaultvalue);
        } else
            return defaultvalue;
    }


    public static void SetLongSharedPreference(Context ctx, String Key, Long Value) {
        init(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(Key, Value);
        editor.apply();
    }
    public static long GetLongSharedPreference(Context ctx, String Key, long defaultValue) {
        init(ctx);
        if (pref.contains(Key)) {
            return pref.getLong(Key, defaultValue);
        } else
            return defaultValue;
    }


    public static long GetLongSharedPreference(Context ctx, String Key) {
        init(ctx);
        if (pref.contains(Key)) {
            return pref.getLong(Key, 0);
        } else
            return 0;
    }





    public static void SetIntSharedPreference(Context ctx, String Key, int Value) {
        init(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Key, Value);
        editor.apply();
    }

    public static void SetFloatSharedPreference(Context ctx, String Key, float Value) {
        init(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(Key, Value);
        editor.apply();
    }

    public static int GetIntSharedPreference(Context ctx, String Key, int defaultValue) {
        init(ctx);
        if (pref.contains(Key)) {

            return pref.getInt(Key, defaultValue);
        } else
            return defaultValue;
    }


    public static int GetIntSharedPreference(Context ctx, String Key) {

        if (ctx==null){
            return 0;
        }

        init(ctx);

        if (pref.contains(Key)) {

            return pref.getInt(Key, 0);
        } else
            return 0;
    }


    public static float GetFloatSharedPreference(Context ctx, String Key) {
        init(ctx);
        if (pref.contains(Key)) {
            return pref.getFloat(Key, 0.0f);
        } else
            return 0.0f;
    }

    public static float GetFloatSharedPreference(Context ctx, String Key, float defaultValue) {
        init(ctx);
        if (pref.contains(Key)) {
            return pref.getFloat(Key, defaultValue);
        } else
            return defaultValue;
    }


    public static long getCurrentTimeInMilliSeconds() {
        return Calendar.getInstance().getTimeInMillis();
    }


    public static String getUserId(Context context){
        String uuid =   AppThemePrefrences.GetSharedPreference(context,AppThemePrefrences.USER_UUID);
        if (TextUtils.isEmpty(uuid)){
            String uuidFirestTime  = UUID.randomUUID().toString();
            if (TextUtils.isEmpty(uuidFirestTime)){
                uuidFirestTime =  ""+System.currentTimeMillis();
            }
            AppThemePrefrences.SetSharedPreference(context,AppThemePrefrences.USER_UUID,uuidFirestTime);
            return uuidFirestTime;
        }
       // count = count+1;
        return uuid;
    }


    public static int  getAppVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
           return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String getAppVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static int getSplashLaunchCount(Context context){
        if (context !=null) {
            int openedCount = AppThemePrefrences.GetIntSharedPreference(context, AppThemePrefrences.SPLASH_OPENED_COUNT);
            return openedCount;
        }
        return 0;
    }

    public static void SetRotateSharedPreference(Context ctx, String Key, int Value) {
        init(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Key, Value);
        editor.apply();
    }

    public static int GetRotateSharedPreference(Context ctx, String Key) {
        init(ctx);
        if (pref.contains(Key)) {

            return pref.getInt(Key, 0);
        } else
            return 0;
    }
    
    public static void setSoftwareDecoderMode(Context context){
        swDecoderMode = GetBooleanSharedPreference(context, AppThemePrefrences.SOFTWARE_DECODER, false);
    }
}
