package com.project.android.photo_journal_android;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EntriesViewHolder {

    ImageView imageView;
    TextView titleView, dateView;
    EntriesViewHolder(View v) {
        imageView = v.findViewById(R.id.rowImageView);
        titleView = v.findViewById(R.id.rowTitle);
        dateView = v.findViewById(R.id.rowDate);
    }
}
