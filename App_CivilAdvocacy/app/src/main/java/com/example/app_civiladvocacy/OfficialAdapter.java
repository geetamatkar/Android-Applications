package com.example.app_civiladvocacy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {
    private final List<OfficialActivity> officialList;
    private static final String TAG = "OfficialAdapter";
    private final MainActivity OMainActivity;
    public OfficialAdapter(MainActivity OMainActivity, List<OfficialActivity> officialList) {
        this.OMainActivity = OMainActivity;
        this.officialList = officialList;
    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }

    @NonNull
    @Override
    public OfficialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.official_entry, parent, false);
        view.setOnClickListener(OMainActivity);
        return new OfficialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialViewHolder offViewHolder, int position) {
        OfficialActivity official;
        official = officialList.get(position);
        Log.d(TAG, "onBindViewHolder: Filling recycler. Name: " + official.getName());
        offViewHolder.title.setText(official.getTitle());
        offViewHolder.name.setText(official.getName());
        offViewHolder.party.setText(String.format("(%s)", official.getParty()));

        if(official.getPhotoURL() == null){
            offViewHolder.imgView.setImageResource(R.drawable.missing);
        }

        if (official.getPhotoURL() != null) {
            Log.d(TAG, "onBindViewHolder: getting - " + official.getPhotoURL());
            Picasso.get()
                    .load(official.getPhotoURL())
                    .error(R.drawable.brokenimage)
                    .into(offViewHolder.imgView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "onSuccess: for " + official.getName());
                        }
                        @Override
                        public void onError(Exception e) {

                            Log.d(TAG, "onError: " + e);
                        }
                    });
        }
    }

}
