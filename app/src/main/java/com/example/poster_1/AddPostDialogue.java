package com.example.poster_1;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddPostDialogue  extends AppCompatDialogFragment {

    TextView postText ;
    AddPostTextListener listener ;
    Button cancelPostBtn , addPostBtn ;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context;
        Builder builder = new Builder(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.add_post_dialogue_layout,null);
        postText = v.findViewById(R.id.postText_apLayout);
        addPostBtn  = v.findViewById(R.id.postAddBtn_apLayout);
        cancelPostBtn = v.findViewById(R.id.postCancelBtn_apLayout);

        addPostBtn.setOnClickListener(v12 -> {
            String text = postText.getText().toString();
            if(text.length() != 0){
                listener.addPostTextListener(text);
                this.dismiss();
            }
        });
        cancelPostBtn.setOnClickListener(v1 -> {
            this.dismiss();
        });

        builder.setView(v);
                /*.setPositiveButton("ADD", (dialog, which) -> {

                    String text = postText.getText().toString();
                    listener.addPostTextListener(text);
                })
                .setNegativeButton("CANCEL", (dialog, which) -> {

                });*/


        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {


        try {
            listener = (AddPostTextListener) context ;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement AddPostTextListener");
        }

        super.onAttach(context);
    }

    public interface AddPostTextListener {
        void addPostTextListener(String postText);
    }

}
