package com.example.david.chess16;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by David on 5/2/2017.
 */

public class MessageDialogFragment extends DialogFragment {
    public static final String MESSAGE_KEY = "message_key";

    @Override
    public Dialog onCreateDialog(Bundle savidInstanceState){
        // Use the Builder class for convenient dialog construction
        Bundle bundle = getArguments();
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        builder.setMessage(bundle.getString(MESSAGE_KEY))
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                TextDialogFragment saveMatch = new TextDialogFragment();
                                saveMatch.setCancelable(false);
                                saveMatch.show(getFragmentManager(), "badfields");
                            }
                        });
        // Create the AlertDialog object and return it
        return builder.create();


    }
}
