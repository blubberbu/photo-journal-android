package com.project.android.photo_journal_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android.photo_journal_android.models.Entry;

import java.util.ArrayList;

public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.EntryViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Entry> entries;

    public EntriesAdapter(Context context, ArrayList<Entry> entriesList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.entries = entriesList;
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_entry, parent, false);

        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        Entry entry = entries.get(position);

        holder.imageEntry.setImageBitmap(entry.getImage());
        holder.textDate.setText(entry.getDate());
        holder.textTitle.setText(entry.getTitle());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class EntryViewHolder extends RecyclerView.ViewHolder {
        ImageFilterView imageEntry;
        TextView textDate, textTitle;

        public EntryViewHolder(View itemView) {
            super(itemView);

            imageEntry = (ImageFilterView) itemView.findViewById(R.id.imageEntry);
            textDate = (TextView) itemView.findViewById(R.id.textDate);
            textTitle = (TextView) itemView.findViewById(R.id.textTitle);
        }
    }
}
