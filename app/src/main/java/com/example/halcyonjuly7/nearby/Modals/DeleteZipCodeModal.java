package com.example.halcyonjuly7.nearby.Modals;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import com.example.halcyonjuly7.nearby.Interfaces.IZipCodeDelete;

/**
 * Created by halcyonjuly7 on 4/16/17.
 */

public class DeleteZipCodeModal extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity context = getActivity();
        final DialogFragment dialog_context = this;
        final Bundle args = getArguments();
        return new AlertDialog.Builder(context).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((IZipCodeDelete)context).delete_zip(args.getInt("zip_index"));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog_context.dismiss();
            }
        }).create();
    }
}
