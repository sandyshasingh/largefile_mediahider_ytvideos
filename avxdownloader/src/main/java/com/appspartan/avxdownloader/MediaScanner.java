package com.appspartan.avxdownloader;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.util.LinkedList;
import java.util.List;

public class MediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection mMediaScanConn;
    private ScannerListener mScannerListener;

    private LinkedList<String[]> mLinkedList = new LinkedList<>();
    private String[] mCurrentScanPaths;
    private int mScanCount = 0;

    /**
     * Create scanner.
     *
     * @param context context.
     */
    public MediaScanner(Context context) {
        this.mMediaScanConn = new MediaScannerConnection(context.getApplicationContext(), this);
    }


    /**
     * Create scanner.
     *
     * @param context         context.
     * @param scannerListener {@link ScannerListener}.
     * @deprecated use {@link #MediaScanner(Context)} instead.
     */
    @Deprecated
    public MediaScanner(Context context, ScannerListener scannerListener) {
        this(context);
        this.mScannerListener = scannerListener;
    }

    /**
     * Scanner is running.
     *
     * @return true, other wise false.
     */
    public boolean isRunning() {
        return mMediaScanConn.isConnected();
    }

    /**
     * Scan file.
     *
     * @param filePath file absolute path.
     */
    public void scan(String filePath) {
        scan(new String[]{filePath});
    }

    /**
     * Scan file list.
     *
     * @param filePaths file absolute path list.
     */
    public void scan(List<String> filePaths) {
        scan(filePaths.toArray(new String[filePaths.size()]));
    }

    /**
     * Scan file array.
     *
     * @param filePaths file absolute path array.
     */
    public void scan(String[] filePaths) {
        if (mLinkedList!=null && filePaths != null && filePaths.length > 0) {
            this.mLinkedList.add(filePaths);
            executeOnce();
        }
    }

    /**
     * Execute scanner.
     */
    private void executeOnce() {
        if (mMediaScanConn!=null && !isRunning() && mLinkedList!=null &&  mLinkedList.size() > 0) {
            try {
                this.mCurrentScanPaths = mLinkedList.remove(0);
                this.mMediaScanConn.connect();
            }catch (Exception e){}
        }
    }

    @Override
    public void onMediaScannerConnected() {
        if (mCurrentScanPaths == null){
            return;
        }
        for (String filePath : mCurrentScanPaths) {
            if (!TextUtils.isEmpty(filePath)) {
                String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                mMediaScanConn.scanFile(filePath, mimeType);
            }
        }
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        if (mScannerListener != null) mScannerListener.oneComplete(path, uri);
        mScanCount += 1;
        if ( mCurrentScanPaths!=null && mScanCount == mCurrentScanPaths.length) {
            if (mMediaScanConn!=null) {
                mMediaScanConn.disconnect();
            }
            if (mScannerListener != null) mScannerListener.allComplete(mCurrentScanPaths);
            mScanCount = 0;
            mCurrentScanPaths = null;
            executeOnce();
        }
    }
}