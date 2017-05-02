package com.example.david.chess16;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by David on 5/1/2017.
 */

public class ChessDialogFragment extends DialogFragment {
    public static final String MESSAGE_KEY = "message_key";

    @Override
    public Dialog onCreateDialog(Bundle savidInstanceState){
        // Use the Builder class for convenient dialog construction
        Bundle bundle = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Accept Draw?");
        builder.setPositiveButton("Accept",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,
                                    int id) {
                    Toast.makeText(getContext(), "Draw Accepted", Toast.LENGTH_SHORT).show();
                }
            });
        builder.setNegativeButton("Decline",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        Toast.makeText(getContext(), "Draw Declined", Toast.LENGTH_SHORT).show();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();


    }
}
