package com.jason.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.jason.hao.R;

/**
 * Created by shenghao on 2015/6/24.
 */
public class ConfirmDialog {

    public static AlertDialog alertDialog;

    public static void Show(Context context, String title, String message, CharSequence ok, CharSequence cancel, DialogInterface.OnClickListener yes, DialogInterface.OnClickListener no) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(ok, yes)
                .setNegativeButton(cancel, no);


        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static void Hide() {
        alertDialog.dismiss();
    }

    public static void ShowConfirm(Context context, String title, String message, CharSequence ok, DialogInterface.OnClickListener yes) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(ok, yes)
                .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Hide();
                    }
                });

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
