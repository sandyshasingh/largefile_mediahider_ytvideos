package com.example.mediahider.asynctask;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.example.mediahider.utils.AppProgressDialog;
import com.example.mediahider.utils.LockBottomSheet;
import com.example.mediahider.utils.MediaScanner;
import com.example.mediahider.utils.StorageHelper;
import com.example.mediahider.utils.StorageUtils;
import com.example.mediahider.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MovePhotoAsyntask extends AsyncTask<Void,Integer, Void> {

    private final List<Uri> photoArrayList;
    private final Activity mContext;

    boolean commingFromPrivate;
    private MediaScanner mediaScanner;
    private AppProgressDialog mProgressDialog;
    private boolean showDbFailedMessage = false;
    private boolean movedSuccessful = true;

    public MovePhotoAsyntask(Activity context , List<Uri> uriList, boolean commingFromPrivate){
        this.photoArrayList = uriList;
        this.mContext = context;
        this.commingFromPrivate = commingFromPrivate;

    mediaScanner = new MediaScanner(mContext);
}

@Override
protected void onPreExecute() {
    if (photoArrayList!=null && !photoArrayList.isEmpty()) {
        try {
            showDialog();
        }catch (Exception e){
            Log.d("Progress Issue", e.toString());
        }
    }
    super.onPreExecute();
}


@Override
protected Void doInBackground(Void... voids) {

    //  Storage storage = MyApplication.getStorage();
    File newFile =  StorageUtils.getPrivatePhotoStorageDir(mContext);
    File externalStoragePublic = StorageUtils.getPublicVideoStorageDir();

    int size_Of_list = photoArrayList.size();
    //TODO testing required on deletion
    for(int x= 0; x< size_Of_list; x++){
        try {
            String oldFilePath = StorageHelper.getFilePathFromUri(mContext, photoArrayList.get(x));
//                long rowId = photoArrayList.get(x).rowId;
//                if (rowId <1){
//                    rowId = System.currentTimeMillis();
//                }
            if (commingFromPrivate){
                    try {
                        String filename = oldFilePath.substring(oldFilePath.lastIndexOf("/") + 1);
                        String newExternalPath = externalStoragePublic.getPath() + "/" + StorageUtils.decode(filename, StorageUtils.offset);
                        boolean moved = StorageUtils.move(oldFilePath, newExternalPath);
                        if (moved) {
                            // Log.d("Moved",""+moved);
                            mediaScanner.scan(newExternalPath);
                        }else {
                            movedSuccessful = false;
                        }
//                                showDbFailedMessage = true;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


            }else {
                String filename = StorageUtils.getFileNameFromPath(oldFilePath);
                String newFilename =  StorageUtils.encode(filename, StorageUtils.offset);
                String newPath = newFile.getPath() + "/" + newFilename;
                boolean moved = false;
                try {
                    moved = StorageUtils.move(oldFilePath, newPath);
                    if (moved) {
                        mediaScanner.scan(newPath);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("@ASHISH", e.toString());
                }

            }
        }catch (IndexOutOfBoundsException e){
            Log.d("@ASHISH INDEX ISSUE", e.toString());
        }
    }
    return null;
}

@Override
protected void onPostExecute(Void aVoid) {
    super.onPostExecute(aVoid);
    dismissProgressDailog();

    if (showDbFailedMessage){

        if (Utils.getActivityIsAlive(mContext)) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LockBottomSheet.showBSForPublicHiderFolderInfo(mContext);
                }
            }, 300);

        }
    }

}

private void showDialog() {
    if (Utils.getActivityIsAlive(mContext)) {
        mProgressDialog = new AppProgressDialog(mContext);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.show();
    }
}

private void dismissProgressDailog() {
    try {
        if (Utils.getActivityIsAlive(mContext) && mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }catch (Exception e){
//        log(e.toString());
    }
}


}
