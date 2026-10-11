package com.example.mediahider.asynctask;

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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MoveFileAsyncTaskForR extends AsyncTask<Void,Integer, Void> implements ProgressUpdateListener {

    private final List<Uri> videoFileInfoArrayList;
    private final Activity mContext;
    private final boolean fromFolder;

    boolean commingFromPrivate;

    private boolean showDbFailedMessage = false;
    private DeleteProgressDialog deleteProgressDialog;
    private long currentBytesWritten = 0;
    private long lenOfFile = 0;

    public MoveFileAsyncTaskForR(Activity context , List<Uri> uriList, boolean commingFromPrivate, boolean fromFolder){
        this.videoFileInfoArrayList = uriList;
        this.mContext = context;
        this.commingFromPrivate = commingFromPrivate;
        deleteProgressDialog=new DeleteProgressDialog(mContext);
        deleteProgressDialog.startDeleting(context.getString(R.string.moving));
        this.fromFolder = fromFolder;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

  /*  @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
      //  Log.d("@ASHISH", "COPIED _"+values[0] );

    }*/

    @Override
    protected Void doInBackground(Void... voids) {

        File newFile =  StorageUtils.getPrivateVideoStorageDirR();
        File externalStoragePublic = StorageUtils.getPublicVideoStorageDirR();

        int size_Of_list = videoFileInfoArrayList.size();

        if(size_Of_list > 0){
            for(int i=0;i<videoFileInfoArrayList.size();i++){
                if (commingFromPrivate) {
                    DocumentFile file = DocumentFile.fromSingleUri(mContext, videoFileInfoArrayList.get(i));
                    if(file != null){
                        lenOfFile += file.length();
                    }
                }else {
                    lenOfFile += new File(StorageHelper.getFilePathFromUri(mContext, videoFileInfoArrayList.get(i))).length();
                }
            }
        }

//        if(fromFolder){
            for(int x= 0; x< size_Of_list; x++){
                try {
                    hideFile(x,newFile,externalStoragePublic);
                }catch (Exception e){
                    Log.d("@ASHISH INDEX ISSUE", e.toString());
                }
            }
//        }
//        else{
//            for(int x= 0; x< videoFileInfoArrayList.size(); x++){
//                try {
//                    int index = posList.get(x);
//                    if (index < size_Of_list) {
//                        hideFile(index,newFile,externalStoragePublic);
//                    }
//                }catch (Exception e){
//                    Log.d("@ASHISH INDEX ISSUE", e.toString());
//                }
//            }
//        }

        return null;
    }

    private void hideFile(int index, File newFile, File externalStoragePublic) {
        Uri fileUri = videoFileInfoArrayList.get(index);
        if (commingFromPrivate) {
            try {
                String tempPath = StorageHelper.getFilePathFromUri(mContext, videoFileInfoArrayList.get(index));
                String newExternalPath = externalStoragePublic + "/" + StorageUtils.getFileNameFromPath(tempPath);
                File abc = moveHiddenVideoHider(mContext, fileUri, newExternalPath, this);
                if (abc != null) {
                    DocumentFile hiddenFile = DocumentFile.fromSingleUri(mContext, fileUri);
                    if(hiddenFile != null){
                        hiddenFile.delete();
                    }
                    StorageUtils.scanMediaFile(mContext, abc.getAbsolutePath());
                }
                showDbFailedMessage = true;

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            String oldFilePath = StorageHelper.getFilePathFromUri(mContext, videoFileInfoArrayList.get(index));
            String filename = StorageUtils.getFileNameFromPath(oldFilePath);
            String newFilename = StorageUtils.newEncode(filename);
            String newPath = newFile.getPath() + "/" + newFilename;
            try {
                File file = new File(oldFilePath);
                boolean moved = StorageUtils.move(oldFilePath, newPath);
                if (!moved) {
                    File abc = moveHiddenVideoHider(mContext, Uri.fromFile(file), newPath, this);
                    if (abc != null) {
                        boolean isDeleted = file.delete();
                        if(!isDeleted) {
                            Uri uri = Utils.getVideoContentUri(mContext, file);
                            mContext.getContentResolver().delete(uri, null, null);
                        }
                        StorageUtils.scanMediaFile(mContext, file.getPath());
                    }
                }
                if (moved) {
                    StorageUtils.scanMediaFile(mContext, newPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        if (mIMovedFilelistener!=null) {
//            mIMovedFilelistener.onMovedFileSuccessfully(posList);
//        }
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
                if(progressUpdateListener!=null && lenOfFile > 0) {
                    progressUpdateListener.onProgress((int) ((currentBytesWritten * 100) / lenOfFile), 100);
                }
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

