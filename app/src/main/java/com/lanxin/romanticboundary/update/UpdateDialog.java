package com.lanxin.romanticboundary.update;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.text.Html;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;

import com.lanxin.romanticboundary.utils.PreUtils;

class UpdateDialog {


    static void show(final Context context, FragmentManager fragmentManager, String content, final String downloadUrl) {
        if (isContextValid(context)) {
            PreUtils.putString(context,Constants.UPDATE_URL,downloadUrl);
            SimpleDialogFragment.createBuilder(context, fragmentManager)
                    .setTitle(com.loveplusplus.update.R.string.android_auto_update_dialog_title)
                    .setRequestCode(0)
                    .setMessage(Html.fromHtml(content))
                    .setPositiveButtonText(com.loveplusplus.update.R.string.android_auto_update_dialog_btn_download)
                    .setNegativeButtonText(com.loveplusplus.update.R.string.android_auto_update_dialog_btn_cancel)

                    .show();

//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle(R.string.android_auto_update_dialog_title);
//            builder.setMessage(Html.fromHtml(content))
//                    .setPositiveButton(R.string.android_auto_update_dialog_btn_download, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                           goToDownload(context, downloadUrl);
//                        }
//                    })
//                    .setNegativeButton(R.string.android_auto_update_dialog_btn_cancel, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                        }
//                    });
//
//            AlertDialog dialog = builder.create();
//            //点击对话框外面,对话框不消失
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
        }
    }

    private static boolean isContextValid(Context context) {
        return context instanceof Activity && !((Activity) context).isFinishing();
    }


     static void goToDownload(Context context, String downloadUrl) {
        Intent intent = new Intent(context.getApplicationContext(), DownloadService.class);
        intent.putExtra(Constants.APK_DOWNLOAD_URL, downloadUrl);
        context.startService(intent);
    }
}
