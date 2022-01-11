package com.project.android.photo_journal_android;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.project.android.photo_journal_android.models.Entry;

import java.util.ArrayList;
import java.util.List;

public class EntriesAdapter extends ArrayAdapter {
    Context context;
    DatabaseHelper db;
    List<Entry> entries;

    public EntriesAdapter(Context context, List entries) {
        super(context, R.layout.entry_list_row, R.id.rowTitle, entries);
        this.context = context;
        this.entries = entries;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        EntriesViewHolder entriesViewHolder = null;
        final Entry entry = entries.get(position);

        //first time creating a new item convertView = null
        //only inflate when creating a new item as inflating is expensive
        if (rowView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = layoutInflater.inflate(R.layout.entry_list_row, parent, false);
            entriesViewHolder = new EntriesViewHolder(rowView);
            entriesViewHolder.imageView = (ImageView) rowView.findViewById(R.id.rowImageView);
            entriesViewHolder.titleView = (TextView) rowView.findViewById(R.id.rowTitle);
            entriesViewHolder.dateView = (TextView) rowView.findViewById(R.id.rowDate);
            rowView.setTag(entriesViewHolder);
        }else {
            entriesViewHolder = (EntriesViewHolder) rowView.getTag();
        }

        Uri imgUri = Uri.parse(entry.getImage());

        entriesViewHolder.imageView.setImageURI(imgUri);
        entriesViewHolder.titleView.setText(entry.getTitle());
        entriesViewHolder.dateView.setText(entry.getDate());


//        return super.getView(position, convertView, parent);
        return rowView;
    }
}
