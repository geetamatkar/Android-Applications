package com.example.app_civiladvocacy;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialViewHolder extends RecyclerView.ViewHolder {

    ImageView imgView;
    TextView name, title, party ;



    public OfficialViewHolder(@NonNull View view) {
        super(view);
        name = view.findViewById(R.id.textViewName);
        title = view.findViewById(R.id.textViewTitle);
        party = view.findViewById(R.id.textViewParty);
        imgView = view.findViewById(R.id.profileImgView);
    }
}
