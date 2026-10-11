package com.example.mediahider.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.example.mediahider.utils.AppProgressDialog;
import com.example.mediahider.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//public class MoveMusicAsyntask extends AsyncTask<Void,Integer, Void> {
//
//    private final List<MusicModel> photoArrayList;
//    private final Activity mContext;
//    ArrayList<Integer> posList;
//    boolean commingFromPrivate;
//    IMovedFilelistener mIMovedFilelistener;
//    private  MediaScanner mediaScanner;
//    private AppProgressDialog mProgressDialog;
//    private boolean showDbFailedMessage = false;
//    private boolean movedSuccessful = true;
//
//    public MoveMusicAsyntask(Activity context, List<MusicModel> stringArrayList, boolean commingFromPrivate){
//        this.photoArrayList = stringArrayList;
//        this.mContext = context;
//        this.mIMovedFilelistener = iMovedFilelistener;
//        this.commingFromPrivate = commingFromPrivate;
////        this.posList = posList;
//        mediaScanner = new MediaScanner(mContext);
//    }
//
//    @Override
//    protected void onPreExecute() {
////        if (posList!=null && posList.size()>0) {
////
////        }
//        try {
//            showDialog();
//        }catch (Exception e){
//            Log.d("Progress Issue", e.toString());
//        }
//        super.onPreExecute();
//    }
//
//  /*  @Override
//    protected void onProgressUpdate(Integer... values) {
//        super.onProgressUpdate(values);
//      //  Log.d("@ASHISH", "COPIED _"+values[0] );
//
//    }*/
//
//    @Override
//    protected Void doInBackground(Void... voids) {
//
//        //  Storage storage = MyApplication.getStorage();
//        File newFile =  StorageUtils.getPrivateMusicStorageDir(mContext);
//        File externalStoragePublic = StorageUtils.getPublicVideoStorageDir();
//
//        int size_Of_list = photoArrayList.size();
//        //TODO testing required on deletion
//        for(int x= 0; x< size_Of_list; x++){
//            try {
//                if (x < size_Of_list) {
//                    String oldFilePath =  photoArrayList.get(x).getFilePath();
//                    long rowId = photoArrayList.get(x).getId();
//                    if (rowId <1){
//                        rowId = System.currentTimeMillis();
//                    }
//                    if (commingFromPrivate){
//                        FilepathDatabase filepathDatabase = null;
////                        FilepathDatabase filepathDatabase = VideoPrivateDbUtility.getVideoFilePathFromDB(oldFilePath);// getVideoFilePathFromDB(oldFilePath);
//                        if (filepathDatabase!=null ) {
//                            String newFilePath = filepathDatabase.getOldFilepath();
//                            boolean moved = false;
//                            try {
//                                moved = StorageUtils.move(oldFilePath, newFilePath);
//                                if (moved) {
//                                    mediaScanner.scan(newFilePath);
//                                    deleteFromDB(filepathDatabase);
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                // Log.d("@ASHISH", e.toString());
//                            }
//
//                        }else{
//                            try {
//                                String filename = oldFilePath.substring(oldFilePath.lastIndexOf("/") + 1);
//                                String newExternalPath = externalStoragePublic.getPath() + "/" + StorageUtils.decode(filename, StorageUtils.offset);
//                                boolean moved = StorageUtils.move(oldFilePath, newExternalPath);
//                                if (moved) {
//                                    // Log.d("Moved",""+moved);
//                                    mediaScanner.scan(newExternalPath);
//                                    mediaScanner.scan(oldFilePath);
//                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                                    intent.setData(Uri.fromFile(new File(newExternalPath)));
//                                    mContext.sendBroadcast(intent);
//                                }else {
//                                    movedSuccessful = false;
//                                }
////                                showDbFailedMessage = true;
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }else {
//                        String filename = StorageUtils.getFileNameFromPath(oldFilePath);
//                        String newFilename =  StorageUtils.encode(filename, StorageUtils.offset);
//                        String newPath = newFile.getPath() + "/" + newFilename;
//                        boolean moved = false;
//                        try {
//                            moved = StorageUtils.move(oldFilePath, newPath);
//                            if (moved) {
//                                mediaScanner.scan(newPath);
//                                mediaScanner.scan(oldFilePath);
//                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                                intent.setData(Uri.fromFile(new File(newPath)));
//                                mContext.sendBroadcast(intent);
//                                saveinDB(rowId, oldFilePath, newPath);
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//            }catch (IndexOutOfBoundsException e){
//            }
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//        dismissProgressDailog();
//        if (mIMovedFilelistener!=null) {
//            mIMovedFilelistener.onMovedFileSuccessfully(posList);
//        }
//        if (showDbFailedMessage){
//
//            if (ThemeUtils.getActivityIsAlive(mContext)) {
//
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        LockBottomSheet.showBSForPublicHiderFolderInfo(mContext);
//                    }
//                }, 300);
//
//            }
//        }
//
//    }
//
//    private void deleteFromDB(FilepathDatabase filepathDatabase) {
//        try {
////            FilepathDatabaseDao dbDao = MyApplication.getDaoSession().getFilepathDatabaseDao();
////            if (filepathDatabase != null) {
////                Log.d("PATH DELETE", filepathDatabase.getOldFilepath());
////            }
////            dbDao.delete(filepathDatabase);
//        }catch (Exception e){
//
//        }
//    }
//
//    private void saveinDB(long rowId, String oldFilePath, String newFilePath) {
////        FilepathDatabaseDao dbDao = MyApplication.getDaoSession().getFilepathDatabaseDao();
////        FilepathDatabase filepathDatabase = new FilepathDatabase(rowId, oldFilePath, newFilePath);
////        dbDao.insertOrReplace(filepathDatabase);
//    }
//
//    private void showDialog() {
//        if (Utils.getActivityIsAlive(mContext)) {
//            mProgressDialog = new AppProgressDialog(mContext);
//            mProgressDialog.setCancelable(true);
//            mProgressDialog.setCanceledOnTouchOutside(true);
//            mProgressDialog.show();
//        }
//    }
//
//    private void dismissProgressDailog() {
//        try {
//            if (Utils.getActivityIsAlive(mContext) && mProgressDialog != null && mProgressDialog.isShowing()) {
//                mProgressDialog.dismiss();
//            }
//        }catch (Exception e){
////            log(e.toString());
//        }
//    }
//
//
//}
//
