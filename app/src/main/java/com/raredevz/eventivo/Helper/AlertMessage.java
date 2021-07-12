package com.raredevz.eventivo.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertMessage {
    public static void showMessage(Context context,String Message){
        new AlertDialog.Builder(context)
                .setMessage(Message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


}
