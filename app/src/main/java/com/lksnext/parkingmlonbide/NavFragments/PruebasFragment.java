package com.lksnext.parkingmlonbide.NavFragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private TextView fechaCorrespondienteTextView;
    private LinearLayout linearLayout;
    private ImageView arrowImageView;
    private boolean isArrowRotated = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pruebas, container, false);

        fechaTextView = view.findViewById(R.id.fechaTextView);
        fechaCorrespondienteTextView = view.findViewById(R.id.fechaCorrespondienteTextView);
        linearLayout = view.findViewById(R.id.LinearLayoutFechaReserva);
        arrowImageView = view.findViewById(R.id.arrowFecha);

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

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateArrowImage();
            }
        });

        calendar = Calendar.getInstance();
        actualizarFechaActual();
        fechaCorrespondienteTextView.setText("Reservar plaza parking");

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

    private void rotateArrowImage() {
        if (isArrowRotated) {
            // Si la imagen ya está girada, la restauramos a su posición original
            arrowImageView.animate().rotation(0).start();
            isArrowRotated = false;
        } else {
            // Giramos la imagen 90 grados hacia la derecha
            arrowImageView.animate().rotation(-90).start();
            isArrowRotated = true;
        }
    }
}
