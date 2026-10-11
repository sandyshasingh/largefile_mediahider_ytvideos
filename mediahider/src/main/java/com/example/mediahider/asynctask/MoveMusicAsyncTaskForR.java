package com.example.mediahider.asynctask;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.example.mediahider.R;
import com.example.mediahider.utils.DeleteProgressDialog;
import com.example.mediahider.utils.LockBottomSheet;
import com.example.mediahider.utils.StorageUtils;
import com.example.mediahider.utils.Utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

//public class MoveMusicAsyncTaskForR extends AsyncTask<Void,Integer, Void> implements ProgressUpdateListener {

//    private final List<MusicModel> photoArrayList;
//    private final Activity mContext;
//    ArrayList<Integer> posList;
//    boolean commingFromPrivate;
//
//    private boolean showDbFailedMessage = false;
//    private DeleteProgressDialog deleteProgressDialog;
//    private long currentBytesWritten = 0;
//    private long lenOfFile = 0;
//
//    public MoveMusicAsyncTaskForR(Activity context, List<MusicModel> stringArrayList, boolean commingFromPrivate){
//        this.photoArrayList = stringArrayList;
//        this.mContext = context;
//
//        this.commingFromPrivate = commingFromPrivate;
////        this.posList = posList;
//        deleteProgressDialog=new DeleteProgressDialog(mContext);
//        deleteProgressDialog.startDeleting(mContext.getString(R.string.moving));
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//    @Override
//    protected Void doInBackground(Void... voids) {
//
//        //  Storage storage = MyApplication.getStorage();
//        File newFile =  StorageUtils.getPrivateMusicStorageDirR();
//        File externalStoragePublic = StorageUtils.getPublicMusicStorageDirR();
//
//        int size_Of_list = photoArrayList.size();
//
//        for(MusicModel mediaStoreData : photoArrayList){
//            if (commingFromPrivate) {
//                lenOfFile = DocumentFile.fromSingleUri(mContext,mediaStoreData.getUri()).length();
//            }else {
//                lenOfFile = new File(mediaStoreData.getFilePath()).length();
//            }
//        }
//
//
//        //TODO testing required on deletion
//        for(int x= 0; x< size_Of_list; x++){
//            try {
//                if (x < size_Of_list) {
//
//                    if (commingFromPrivate){
//                        try {
//                            Uri fileUri = photoArrayList.get(x).getUri();
//                            String newExternalPath = externalStoragePublic + "/" + photoArrayList.get(x).getFilename();
//                            File abc = moveHiddenVideoHider(mContext,fileUri,newExternalPath,this);
//                            if(abc!=null) {
//                                DocumentFile.fromSingleUri(mContext, fileUri).delete();
//                                StorageUtils.scanMediaFile(mContext,abc.getAbsolutePath());
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }else {
//                        String oldFilePath =  photoArrayList.get(x).getFilePath();
//                        String filename = StorageUtils.getFileNameFromPath(oldFilePath);
//                        String newFilename =  StorageUtils.newEncode(filename);
//                        String newPath = newFile.getPath() + "/" + newFilename;
//
//                        try {
//                            File file = new File(oldFilePath);
//                            boolean moved = StorageUtils.move(oldFilePath, newPath);
//                            if(!moved){
//                                File abc = moveHiddenVideoHider(mContext, Uri.fromFile(file),newPath,this);
//                                if(abc!=null){
//                                    Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + photoArrayList.get(x).getId());
//                                    mContext.getContentResolver().delete(uri,null,null);
//                                    StorageUtils.scanMediaFile(mContext,uri.getPath());
//                                }
//                            }
//                            if (moved) {
//                                StorageUtils.scanMediaFile(mContext,newPath);
//                            }
//
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//            }catch (Exception e){
//            }
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//
//        if (deleteProgressDialog!=null){
//            deleteProgressDialog.dismiss();
//        }
//        if (showDbFailedMessage){
//
//            if (Utils.getActivityIsAlive(mContext)) {
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


//    private File moveHiddenVideoHider(Context context, Uri uri, String targetPath, ProgressUpdateListener progressUpdateListener) {
//        if (uri == null) return null;
//        FileInputStream input = null;
//        FileOutputStream output = null;
//
//        try {
//            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
//            if (pfd == null) return null;
//            FileDescriptor fd = pfd.getFileDescriptor();
//            input = new FileInputStream(fd);
//            output = new FileOutputStream(targetPath);
//            int read;
//
//            byte[] bytes = new byte[4096];
//            while ((read = input.read(bytes)) != -1) {
//                output.write(bytes, 0, read);
//                currentBytesWritten += read;
//                if(progressUpdateListener!=null) {
//                    progressUpdateListener.onProgress((int) ((currentBytesWritten * 100) / lenOfFile), 100);
//                }
//            }
//
//            return new File(targetPath);
//        } catch (Exception ignored) {
//            Log.d("#VIBHOR    ",ignored.toString());
//        } finally {
////            closeQuietly(input);
////            closeQuietly(output);
//        }
//        return null;
//    }
//
//
//    @Override
//    public void onProgress(int pos, int size) {
//        if (deleteProgressDialog!=null) {
//            deleteProgressDialog.setValues(pos,size);
//        }
//    }
//}
