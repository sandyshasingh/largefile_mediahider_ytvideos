package com.example.mediahider.utils;

import static com.example.mediahider.utils.AppThemePrefrences.PUBLIC_DIRECTORY_HIDERBACKUP;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class StorageUtils {

    public static final int offset =17;
    public static final String PRIVATE_DIR = "AVX_HIDER";
    public static final String PUBLIC_DIRECTORY_ROCKS_HIDERBACKUP = "AVXPlayer";

    public static File getPrivateVideoStorageDirR() {

        File xhiderDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()
                + "/AVX Video Folder");
        if(!xhiderDirectory.exists()) {
            xhiderDirectory.mkdirs();
        }
        return xhiderDirectory;

    }

    public static File getPublicMusicStorageDirR() {
        // Get the directory for the app's private pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + File.separator + PUBLIC_DIRECTORY_ROCKS_HIDERBACKUP);
        if (!file.mkdirs()) {
            Log.e("@STORAGE", "Directory not created");
        }
        return file;
    }
    public static File getPublicVideoStorageDirR() {
        // Get the directory for the app's private pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + File.separator + PUBLIC_DIRECTORY_ROCKS_HIDERBACKUP);
        if (!file.mkdirs()) {
            Log.e("@STORAGE", "Directory not created");
        }
        return file;
    }

    public static File getPublicVideoStorageDir() {
        // Get the directory for the app's private pictures directory.
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + PUBLIC_DIRECTORY_ROCKS_HIDERBACKUP);
        if (!file.mkdirs()) {
            Log.e("@STORAGE", "Directory not created");
        }
        return file;
    }

    public static String decode(String enc, int offset) {
        return encode(enc, 26-offset);
    }
    public static String encode(String enc, int offset) {
        offset = offset % 26 + 26;
        StringBuilder encoded = new StringBuilder();
        for (char i : enc.toCharArray()) {
            if (Character.isLetter(i)) {
                if (Character.isUpperCase(i)) {
                    encoded.append((char) ('A' + (i - 'A' + offset) % 26 ));
                } else {
                    encoded.append((char) ('a' + (i - 'a' + offset) % 26 ));
                }
            } else {
                encoded.append(i);
            }
        }
        return encoded.toString();
    }
    public static File getPublicPhotoStorageDirR() {
        // Get the directory for the app's private pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + PUBLIC_DIRECTORY_HIDERBACKUP);
        if (!file.mkdirs()) {
            Log.e("@STORAGE", "Directory not created");
        }
        return file;
    }



    public static File getPrivateVideoStorageDir(Context context) {
        File xhiderDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + PRIVATE_DIR + "/.AvxPrivateData");
        if(!xhiderDirectory.exists()) {
            xhiderDirectory.mkdirs();
        }

        File noMediaFile = new File(xhiderDirectory, ".nomedia");
        if(!noMediaFile.exists()) {
            try {
                noMediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
            }
        }
        return xhiderDirectory;
    }

    public static File getPrivatePhotoStorageDirR() {

        File xhiderDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()
                + "/AVX Photo Folder");
        if(!xhiderDirectory.exists()) {
            xhiderDirectory.mkdirs();
        }
        return xhiderDirectory;

    }

    public static File getPrivatePhotoStorageDir(Context context) {
        File xhiderDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + PRIVATE_DIR + "/AvxPhoto" + "/.PrivateData");
        if(!xhiderDirectory.exists()) {
            xhiderDirectory.mkdirs();
        }

        File noMediaFile = new File(xhiderDirectory, ".nomedia");
        if(!noMediaFile.exists()) {
            try {
                noMediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
            }
        }
        return xhiderDirectory;
    }

    public static File getPrivateMusicStorageDirR() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            File xhiderDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()
                    + "/AVX Music Folder");
            if(!xhiderDirectory.exists()) {
                xhiderDirectory.mkdirs();
            }
            return xhiderDirectory;
        }
        return null;

    }

    public static File getPrivateMusicStorageDir(Context context) {
        File xhiderDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + PRIVATE_DIR + "/AvxMusic" + "/.PrivateData");
        if(!xhiderDirectory.exists()) {
            xhiderDirectory.mkdirs();
        }

        File noMediaFile = new File(xhiderDirectory, ".nomedia");
        if(!noMediaFile.exists()) {
            try {
                noMediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
            }
        }
        return xhiderDirectory;
    }

    public static File getFile(String path) {
        return new File(path);
    }

    public static boolean rename(String fromPath, String toPath) {
        try {
            File file = getFile(fromPath);
            File newFile = new File(toPath);
            return file.renameTo(newFile);
        }catch (Exception e){
            return false;
        }
    }

    public static String newEncode(String enc) {
        return enc + ".avxplayer";
    }

    public static String newDecode(String enc) {
        return enc.replace(".avxplayer","");
    }

    public static void scanMediaFile(Context context , String path){
        try {
            MediaScanner mediaScanner = new MediaScanner(context);
            mediaScanner.scan(path);
        }catch (Exception e){

        }
    }

    public static boolean move(String fromPath, String toPath) {
        if (rename(fromPath, toPath)){
            return true;
        }
       /* if (copy(fromPath, toPath)) {
            return getFile(fromPath).delete();
        }*/
        return false;
    }

    public static String getFileNameFromPath(String path) {
        try {
            String filename = path.substring(path.lastIndexOf("/") + 1);
            return filename;
        }catch (Exception e){
            return "video_file"+System.currentTimeMillis();
        }
    }
}
