package com.lksnext.parkingmlonbide.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lksnext.parkingmlonbide.R;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    public CustomListAdapter(Context context, ArrayList<String> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView itemTextView = convertView.findViewById(R.id.itemTextView);
        String item = getItem(position);
        itemTextView.setText(item);

        return convertView;
    }
}

