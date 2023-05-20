package com.example.androidnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder> {
    private final MainActivity mainActivity;
    private final List<Notes> listNotes;
    public NotesListAdapter(List<Notes> list, MainActivity mainact) {
        listNotes = list;
        mainActivity = mainact;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_notes, parent, false);
        view.setOnLongClickListener(mainActivity);
        view.setOnClickListener(mainActivity);

        return new NotesViewHolder(view);
    }

    @Override
    public int getItemCount()
    {
        return listNotes.size();
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder NvHolder, int position) {
        Notes notes = listNotes.get(position);
        NvHolder.notesTitle.setText(notes.getTitle());

        // check 80 character limit for content
        int len = notes.getContent().length();
        if(len < 80)
            NvHolder.notesContent.setText(notes.getContent());
        else
        {
            String strDisplay = notes.getContent().substring(0,80) + "...";
            NvHolder.notesContent.setText(strDisplay );
        }

        SimpleDateFormat dateForm = new SimpleDateFormat("EEE MMM dd, hh:mm a"); //format to get date and time
        NvHolder.notesDate.setText(dateForm.format(new Date(notes.getUpdate())));
    }

}
