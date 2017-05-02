package com.example.david.chess16;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.example.david.chess16.control.Engine;

/**
 * Created by David on 5/1/2017.
 */

public class TextDialogFragment extends DialogFragment {
    public static final String MESSAGE_KEY = "message_key";

    @Override
    public Dialog onCreateDialog(Bundle savidInstanceState){
        // Use the Builder class for convenient dialog construction
        Bundle bundle = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setTitle("If you would like to Save, Enter a title and hit save.");
        builder.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        ((PlayChess) getActivity()).match.setTitle(input.getText().toString());
                        Engine.saveMatch();
                        Toast.makeText(getContext(), "Match Saved as " + input.getText().toString(), Toast.LENGTH_SHORT).show();
                        try {
                            Engine.saveState(getContext());
                        } catch (Exception e){
//                            Toast.makeText(getContext(), "Seesion Not Saved", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        ((PlayChess) getActivity()).finish();
                    }
                });
        builder.setNegativeButton("Don't Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        Toast.makeText(getContext(), "Match Not Saved", Toast.LENGTH_SHORT).show();
                        ((PlayChess) getActivity()).finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();


    }
}