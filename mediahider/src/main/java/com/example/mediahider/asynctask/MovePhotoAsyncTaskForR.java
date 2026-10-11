package com.example.mediahider.asynctask;

import static com.example.mediahider.utils.StorageUtils.getFileNameFromPath;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.example.mediahider.R;
import com.example.mediahider.utils.DeleteProgressDialog;
import com.example.mediahider.utils.LockBottomSheet;
import com.example.mediahider.utils.StorageHelper;
import com.example.mediahider.utils.StorageUtils;
import com.example.mediahider.utils.Utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MovePhotoAsyncTaskForR extends AsyncTask<Void,Integer, Void> implements ProgressUpdateListener {

    private final List<Uri> photoArrayList;
    private final Activity mContext;
//    ArrayList<Integer> posList;
    boolean commingFromPrivate;
//    IMovedFilelistener mIMovedFilelistener;
    private boolean showDbFailedMessage = false;
    private DeleteProgressDialog deleteProgressDialog;
    private long currentBytesWritten = 0;
//    private long lenOfFile = 0;

    public MovePhotoAsyncTaskForR(Activity context , List<Uri> uriList,  boolean commingFromPrivate){
        this.photoArrayList = uriList;
        this.mContext = context;
//        this.mIMovedFilelistener = iMovedFilelistener;
        this.commingFromPrivate = commingFromPrivate;

        deleteProgressDialog=new DeleteProgressDialog(mContext);
        deleteProgressDialog.startDeleting(mContext.getString(R.string.moving));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        //  Storage storage = MyApplication.getStorage();
        File newFile =  StorageUtils.getPrivatePhotoStorageDirR();
        File externalStoragePublic = StorageUtils.getPublicPhotoStorageDirR();

        int size_Of_list = photoArrayList.size();

//        for(MediaStoreData mediaStoreData : photoArrayList){
//            if (commingFromPrivate) {
//                lenOfFile = DocumentFile.fromSingleUri(mContext, Uri.parse(mediaStoreData.uri)).length();
//            }else {
//                lenOfFile = new File(mediaStoreData.uri).length();
//            }
//        }


        //TODO testing required on deletion
        for(int x= 0; x< size_Of_list; x++){
            try {
                if (x < size_Of_list) {

                    if (commingFromPrivate){
                        Log.d("sandy", "80 async task");
                        try {
                            Uri fileUri = photoArrayList.get(x);
                            String tempPath =  StorageHelper.getFilePathFromUri(mContext, photoArrayList.get(x));
                            Log.d("sandy", "84 "+tempPath + "uri: "+ fileUri);
                            String newExternalPath = externalStoragePublic + "/" + getFileNameFromPath(tempPath);
                            Log.d("sandy", "86 "+newExternalPath);
                            File abc = moveHiddenVideoHider(mContext,fileUri,newExternalPath,this);
                            if(abc!=null) {
                                Log.d("sandy", "89 ");
                                DocumentFile.fromSingleUri(mContext, fileUri).delete();
                                StorageUtils.scanMediaFile(mContext,abc.getAbsolutePath());
                            }
//                            showDbFailedMessage = true;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else {
                        Log.d("sandy", "97 async task");
                        String oldFilePath =  StorageHelper.getFilePathFromUri(mContext, photoArrayList.get(x));
                        Log.d("sandy", "102 async task"+oldFilePath + "uri: "+photoArrayList.get(x));
                        String filename = getFileNameFromPath(oldFilePath);
                        String newFilename =  StorageUtils.newEncode(filename);
                        String newPath = newFile.getPath() + "/" + newFilename;

                        try {
                            Log.d("sandy", "104 async task");
                            File file = new File(oldFilePath);
                            boolean moved = StorageUtils.move(oldFilePath, newPath);
                            if(!moved){
                                File abc = moveHiddenVideoHider(mContext, Uri.fromFile(file),newPath,this);
                                if(abc!=null){
                                    Uri uri = Utils.getImageContentUri(mContext,file);
                                    mContext.getContentResolver().delete(uri,null,null);
                                    StorageUtils.scanMediaFile(mContext,uri.getPath());
                                }
                            }
                            if (moved) {
                                Log.d("sandy", "116 async task");
                                StorageUtils.scanMediaFile(mContext,newPath);
                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("sandy", e.toString());
                        }

                    }
                }
            }catch (Exception e){
                Log.d("@sandy INDEX ISSUE", e.toString());
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (deleteProgressDialog!=null){
            deleteProgressDialog.dismiss();
        }
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


    private File moveHiddenVideoHider(Context context, Uri uri, String targetPath, ProgressUpdateListener progressUpdateListener) {
        if (uri == null) return null;
        FileInputStream input = null;
        FileOutputStream output = null;

        try {
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
            if (pfd == null) return null;
            FileDescriptor fd = pfd.getFileDescriptor();
            input = new FileInputStream(fd);
            output = new FileOutputStream(targetPath);
            int read;

            byte[] bytes = new byte[4096];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
                currentBytesWritten += read;
//                if(progressUpdateListener!=null) {
//                    progressUpdateListener.onProgress((int) ((currentBytesWritten * 100) / lenOfFile), 100);
//                }
            }

            return new File(targetPath);
        } catch (Exception ignored) {
        } finally {
//            closeQuietly(input);
//            closeQuietly(output);
        }
        return null;
    }


    @Override
    public void onProgress(int pos, int size) {
        if (deleteProgressDialog!=null) {
            deleteProgressDialog.setValues(pos,size);
        }
    }
}
