package com.lksnext.parkingmlonbide.Adapters;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lksnext.parkingmlonbide.R;

import java.util.ArrayList;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder>{

    private ArrayList<String> reservasList;

    public SimpleAdapter(ArrayList<String> reservasList) {
        this.reservasList = reservasList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item_reserva, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String reserva = reservasList.get(position);
        Log.d(TAG, "Adapter:" + reserva);
        holder.bind(reserva);
    }

    @Override
    public int getItemCount() {
        return reservasList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewReserva;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewReserva = itemView.findViewById(R.id.textViewReserva);
        }

        public void bind(String reserva) {
            textViewReserva.setText(reserva);
        }
    }
}
