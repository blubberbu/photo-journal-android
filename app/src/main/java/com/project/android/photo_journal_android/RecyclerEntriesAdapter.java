package com.project.android.photo_journal_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android.photo_journal_android.models.Entry;

import java.util.ArrayList;

public class RecyclerEntriesAdapter extends RecyclerView.Adapter<RecyclerEntriesAdapter.ItemViewHolder> {
    private LayoutInflater inflater;
    private Context context;

    private ArrayList<Entry> entries;

    public RecyclerEntriesAdapter(Context context, ArrayList<Entry> entriesList) {
        this.context = context;
        this.entries = entriesList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.recyclerview_entry, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Entry entry = entries.get(position);

        holder.imageEntry.setImageBitmap(entry.getImage());
        holder.textDate.setText(entry.getDate());
        holder.textTitle.setText(entry.getTitle());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageFilterView imageEntry;
        TextView textDate, textTitle;

        public ItemViewHolder(View itemView) {
            super(itemView);  

            imageEntry = (ImageFilterView) itemView.findViewById(R.id.imageEntry);
            textDate = (TextView) itemView.findViewById(R.id.textDate);
            textTitle = (TextView) itemView.findViewById(R.id.textTitle);
        }
    }
}
