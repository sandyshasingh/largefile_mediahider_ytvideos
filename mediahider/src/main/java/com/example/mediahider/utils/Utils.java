package com.example.mediahider.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.ArrayList;

public class Utils {

    public static final int RENAME_MULTIPLE_REQ_CODE_IMG_IN_R = 20118;
    public static final int RENAME_MULTIPLE_REQ_CODE_VIDEO_IN_R = 20119;
    public static final int RENAME_MULTIPLE_REQ_CODE_AUDIO_IN_R = 20120;
    public static final int DELETE_MULTIPLE_REQ_CODE_IN_R = 20108;
    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void renameMultipleVideoFromUriR(Activity context, ArrayList<Uri> uriList) {


        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {

                    PendingIntent pi = MediaStore.createWriteRequest(context.getContentResolver(),  uriList);
                    ( (Activity) context).startIntentSenderForResult(pi.getIntentSender(), RENAME_MULTIPLE_REQ_CODE_VIDEO_IN_R, null, 0, 0, 0);
                    context.getContentResolver().notifyChange(Uri.parse("content://media"), null);

                } catch (Exception e) {
                    return null;
                }
                return null;
            }

        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static boolean getActivityIsAlive(Activity activity) {
        if (activity != null && !activity.isDestroyed()) {
            return true;
        }
        return false;
    }

    public static Uri getVideoContentUri(Activity context,File file){
        try {
            String filePath = file.getAbsolutePath();
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Video.Media._ID},
                    MediaStore.Video.Media.DATA + "=? ",
                    new String[]{filePath},
                    null
            );
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                cursor.close();
                return Uri.withAppendedPath(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        "" + id
                );
            } else {
                if (file.exists()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Video.Media.DATA, filePath);
                    return context.getContentResolver().insert(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values
                    );
                } else {
                    context.runOnUiThread(() -> {
                        Toast.makeText(context,"File can't delete.",Toast.LENGTH_SHORT).show();
                    });
                    return null;
                }
            }
        }catch (Exception e){
            context.runOnUiThread(() -> {
                Toast.makeText(context,"File can't delete.",Toast.LENGTH_SHORT).show();
            });
            return null;
        }
    }

    public static void deleteItemUsingCOntentResolver(Context context, ArrayList filePathID) {
        ContentResolver contentResolver = context.getContentResolver();
        if (StorageHelper.isR()) {
            ArrayList<Uri> list = new ArrayList<>();
            for (Object id : filePathID) {
                Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, (Long) id);
                list.add(deleteUri);
            }
            deleteMultipleItemsFromUriR(context, list);
        } else {
            for (Object id : filePathID) {
                Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, (Long) id);
                contentResolver.delete(deleteUri, null, null);
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void deleteMultipleItemsFromUriR(Context context, ArrayList<Uri> mList) {
        try {
            PendingIntent pi = MediaStore.createDeleteRequest(context.getContentResolver(), mList);
            ( (Activity) context).startIntentSenderForResult(pi.getIntentSender(), DELETE_MULTIPLE_REQ_CODE_IN_R, null, 0, 0, 0,null);
            context.getContentResolver().notifyChange(Uri.parse("content://media"), null);
        }
        catch (Exception e ) {
            // Log.d("exception",e.getMessage());
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void deleteMultipleImagesFromPathR(Context context, ArrayList<Uri> list) {
//        ArrayList<Uri> list = new ArrayList<>();
        AsyncTask<Void,Void, Void> asyncTask=  new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
//                    for (String path : file) {
//                        Uri uri = getImageContentUri(context, new File(path));
//                        list.add(uri);
//                    }
                    PendingIntent pi = MediaStore.createDeleteRequest(context.getContentResolver(), list);
                    ((Activity) context).startIntentSenderForResult(pi.getIntentSender(), DELETE_MULTIPLE_REQ_CODE_IN_R, null, 0, 0, 0, null);
                    context.getContentResolver().notifyChange(Uri.parse("content://media"), null);
                } catch (Exception e) {
//                    Log.d("exception", e.getMessage());
                }
                return null;
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static Uri getImageContentUri(Context context,File file){
        try {
            String filePath = file.getAbsolutePath();
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID},
                    MediaStore.Images.Media.DATA + "=? ",
                    new String[]{filePath},
                    null
            );
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                cursor.close();
                return Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "" + id
                );
            } else {
                if (file.exists()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, filePath);
                    return context.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                    );
                } else {
                    Toast.makeText(context,"File can't delete.",Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        }catch (Exception e){
            Toast.makeText(context,"File can't delete.",Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void renameMultiplePhotosFromUriR(Context context, ArrayList<Uri> uriList) {
//        ArrayList<Uri> list = new ArrayList<>();

        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
//                    for (String path : pathList) {
//                        Uri uri = getImageContentUri(context, new File(path));
//                        list.add(uri);
//                    }

                    Log.d("dikshamerijaan","57 utils.java");

                    PendingIntent pi = MediaStore.createWriteRequest(context.getContentResolver(),  uriList);
                    ( (Activity) context).startIntentSenderForResult(pi.getIntentSender(), RENAME_MULTIPLE_REQ_CODE_IMG_IN_R, null, 0, 0, 0);
                    context.getContentResolver().notifyChange(Uri.parse("content://media"), null);

                } catch (Exception e) {
                    return null;
                }
                return null;
            }

        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void renameMultipleAudioFromIdR(Context context, ArrayList<Uri> uriList) {
//        ArrayList<Uri> mList = new ArrayList<>();
//        for (int i = 0; i < list.length; i++) {
//            Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + list[i]);
//            mList.add(uri);
//        }

        try {
            PendingIntent pi = MediaStore.createWriteRequest(context.getContentResolver(), uriList);
            ((Activity) context).startIntentSenderForResult(pi.getIntentSender(), RENAME_MULTIPLE_REQ_CODE_AUDIO_IN_R, null, 0, 0, 0, null);
            context.getContentResolver().notifyChange(Uri.parse("content://media"), null);
        } catch (Exception e) {
            Log.d("exception", e.getMessage());
        }

    }
}
