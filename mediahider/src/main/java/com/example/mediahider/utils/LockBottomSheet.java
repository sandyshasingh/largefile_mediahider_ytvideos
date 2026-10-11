package com.example.mediahider.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediahider.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;

public class LockBottomSheet {
    public static void showBSForPublicHiderFolderInfo(final Activity activity) {


        try {
            View dialogView = activity.getLayoutInflater().inflate(R.layout.bs_public_folder_info, null);
            final BottomSheetDialog dialog = new BottomSheetDialog(activity,R.style.CustomBottomSheetDialogTheme);
            dialog.setContentView(dialogView);
            dialog.show();


            dialog.setCanceledOnTouchOutside(true);

            Button gotItButton = (Button) dialog.findViewById(R.id.positive_button);
            TextView folderPath = (TextView) dialog.findViewById(R.id.path);

            if (StorageHelper.isR()) {
                folderPath.setText(StorageUtils.getPublicVideoStorageDirR().getAbsolutePath());
            } else {
                folderPath.setText(StorageUtils.getPublicVideoStorageDir().getAbsolutePath());
            }

            SpannableString message_1 =new SpannableString("to RocksPlayer folder");
            message_1.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.orng1)), 3, 15, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            ((TextView) dialog.findViewById(R.id.text)).setText(message_1);

            gotItButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog!=null && dialog.isShowing() ) {
                        dialog.dismiss();
                    }
                }
            });


            Button showVideo = (Button) dialog.findViewById(R.id.showVideoButton);

            showVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (dialog!=null && dialog.isShowing() ) {
                            dialog.dismiss();
                        }

                        File dir;

                        if (StorageHelper.isR()) {
                            dir = StorageUtils.getPublicVideoStorageDirR();
                        }else {
                            dir = StorageUtils.getPublicVideoStorageDir();
                        }

//                        if (dir!=null && dir.exists()) {
//                            if (dir.listFiles()!=null && dir.listFiles().length>0) {
//                                Intent intent = null;
//                                try {
//                                    intent = new Intent(activity, Class.forName("com.VideoActivity"));
//                                    intent.putExtra("Path", dir.getPath());
//                                    intent.putExtra("Title", "AVXPlayer");
//                                    intent.putExtra("bucket_id", "");
//                                    if (Utils.getActivityIsAlive(activity)) {
//                                        activity.startActivityForResult(intent, Utils.MOVE_IN_PRIVATE_REQ_CODE);
////                                        activity.overridePendingTransition(R.anim.scale_to_center, R.anim.push_down_out);
//                                    }
//                                } catch (ClassNotFoundException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }else{
//                                Toast.makeText(activity, "File is scanning. Please try later.",Toast.LENGTH_LONG).show();
//                            }
//                        }

                    }catch (ActivityNotFoundException a){
//                        logException(new Throwable("Issue in opening Video Activity",a));
                    }
                }
            });




        }catch (WindowManager.BadTokenException bt){
//            logException(new Throwable("Bad token exception in BT"));
        }

    }

}
