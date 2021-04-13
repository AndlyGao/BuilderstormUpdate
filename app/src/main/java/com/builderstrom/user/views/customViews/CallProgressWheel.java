package com.builderstrom.user.views.customViews;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.builderstrom.user.R;


/**
 * Created by anil_singhania.
 */
public class CallProgressWheel {

    private static ProgressDialog progressDialog;

    /**
     * Displays custom loading dialog
     */
    public static void showLoadingDialog(Context context, String message) {
        try {
            if (isDialogShowing()) {
                dismissLoadingDialog();
            }

            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (activity.isFinishing()) {
                    return;
                }
            }

            progressDialog = new ProgressDialog(context, android.R.style.Theme_Translucent_NoTitleBar);
            progressDialog.show();
            WindowManager.LayoutParams layoutParams = progressDialog.getWindow().getAttributes();
            layoutParams.dimAmount = 0.5f;
            progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND |
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.progresswheel);
            RelativeLayout frameLayout = progressDialog.findViewById(R.id.dlgProgress);


            ((ProgressWheel) progressDialog.findViewById(R.id.progress_wheel)).spin();
            TextView messageText = progressDialog.findViewById(R.id.tvProgress);
            //  messageText.setTypeface(DataLogin.getFont(context));
            messageText.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void dismissLoadingDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            Log.e("e", "=" + e);
        }
    }

    public static boolean isDialogShowing() {
        try {
            if (progressDialog == null) {
                return false;
            } else {
                return progressDialog.isShowing();
            }
        } catch (Exception e) {
            return false;
        }
    }


}
