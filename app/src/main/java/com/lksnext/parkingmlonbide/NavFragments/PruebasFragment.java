package com.lksnext.parkingmlonbide.NavFragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lksnext.parkingmlonbide.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PruebasFragment extends Fragment {

    private TextView fechaTextView;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pruebas, container, false);

        fechaTextView = view.findViewById(R.id.fechaTextView);

        ImageView btnAnterior = view.findViewById(R.id.btnAnterior);
        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrocederDia();
            }
        });

        ImageView btnSiguiente = view.findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avanzarDia();
            }
        });

        calendar = Calendar.getInstance();
        actualizarFechaActual();

        return view;
    }

    private void actualizarFechaActual() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MMM/yyyy");
        Date fechaActual = calendar.getTime();
        fechaTextView.setText(formato.format(fechaActual));
        fechaTextView.setGravity(Gravity.CENTER);
    }

    private void avanzarDia() {
        if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 6) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            actualizarFechaActual();
        }
    }

    private void retrocederDia() {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_YEAR, 1); // Agregar 1 para incluir el día actual
        if (calendar.after(today)) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            actualizarFechaActual();
        }
    }

}
