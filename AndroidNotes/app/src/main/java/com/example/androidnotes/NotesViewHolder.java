package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder
{
    TextView notesDate, notesTitle, notesContent;
    public NotesViewHolder(@NonNull View itemView)
    {
        super(itemView);
        notesDate = itemView.findViewById(R.id.notesDate);
        notesTitle = itemView.findViewById(R.id.notesTitle);
        notesContent = itemView.findViewById(R.id.notesContent);
    }
}
