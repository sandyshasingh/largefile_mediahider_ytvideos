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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MoveFileAsyntask extends AsyncTask<Void,Integer, Void> {

    private final List<Uri> videoFileInfoArrayList;
    private final Activity mContext;
    ArrayList<Integer> posList;
    boolean commingFromPrivate;

    private MediaScanner mediaScanner;
    private AppProgressDialog mProgressDialog;
    private boolean showDbFailedMessage = false;
    private boolean movedSuccessful = true;

    public MoveFileAsyntask(Activity context, List<Uri> uriList, boolean commingFromPrivate){
        this.videoFileInfoArrayList = uriList;
        this.mContext = context;
        this.commingFromPrivate = commingFromPrivate;
        this.posList = posList;
        mediaScanner = new MediaScanner(mContext);
    }

    @Override
    protected void onPreExecute() {
        if (videoFileInfoArrayList!=null && !videoFileInfoArrayList.isEmpty()) {
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
        File newFile =  StorageUtils.getPrivateVideoStorageDir(mContext);
        File externalStoragePublic = StorageUtils.getPublicVideoStorageDir();

//        int posSize = posList.size();
        int itemsCount = videoFileInfoArrayList.size();

        for(int pos= 0; pos< itemsCount; pos++){
            try {
//                int x = (int) posList.get(pos);
//                if (x < itemsCount) {
                    String oldFilePath =  StorageHelper.getFilePathFromUri(mContext,videoFileInfoArrayList.get(pos));
//                    long rowId = videoFileInfoArrayList.get(x).row_ID;
//                    if (rowId <1){
//                        rowId = System.currentTimeMillis();
//                    }
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
                                showDbFailedMessage = true;

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
//                                saveinDB(rowId, oldFilePath, newPath);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("@ASHISH", e.toString());
                        }

                    }
//                }
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
//        if (mIMovedFilelistener!=null) {
//            mIMovedFilelistener.onMovedFileSuccessfully(posList);
//        }
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
//            log(e.toString());
        }
    }


}

