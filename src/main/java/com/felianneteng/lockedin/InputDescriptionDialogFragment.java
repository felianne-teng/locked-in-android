package com.felianneteng.lockedin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class InputDescriptionDialogFragment extends DialogFragment {

    EditText descriptionEditText;
    NoticeDialogListener nListener;
    private String desc;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(com.felianneteng.lockedin.R.layout.task_input_dialog, null);
        builder.setView(view);
        descriptionEditText = view.findViewById(com.felianneteng.lockedin.R.id.descriptionEditText);
        builder.setTitle("Enter a task.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                desc = descriptionEditText.getText().toString();
                nListener.onDialogPositiveClick(InputDescriptionDialogFragment.this);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                nListener.onDialogNegativeClick(InputDescriptionDialogFragment.this);
            }
        });
        return builder.create();
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            nListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public String getDescription()
    {
        return desc;
    }
}
