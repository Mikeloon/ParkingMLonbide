package com.lksnext.parkingmlonbide.NavFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingmlonbide.DataClasses.Parking;
import com.lksnext.parkingmlonbide.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ParkingFragment extends Fragment {
    private FirebaseFirestore db;
    private TextView fechaTextView;
    private Calendar calendar;
    private TextView fechaCorrespondienteTextView;
    private LinearLayout linearLayout;

    public ParkingFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parking, container, false);

        fechaTextView = view.findViewById(R.id.fechaTextViewP);
        fechaCorrespondienteTextView = view.findViewById(R.id.fechaCorrespondienteTextViewP);
        linearLayout = view.findViewById(R.id.LinearLayoutFechaReservaP);

        ImageView btnAnterior = view.findViewById(R.id.btnAnteriorP);
        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrocederDia();
            }
        });

        ImageView btnSiguiente = view.findViewById(R.id.btnSiguienteP);
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
        SimpleDateFormat formato = new SimpleDateFormat("dd-MMM-yyyy");
        Date fechaActual = calendar.getTime();
        fechaTextView.setText(formato.format(fechaActual));
        fechaTextView.setGravity(Gravity.CENTER);
        mostrarReservas(formato.format(fechaActual));
    }
    private void retrocederDia() {
        Calendar today = Calendar.getInstance();
        if (calendar.after(today)) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            actualizarFechaActual();
        }
    }

    private void avanzarDia() {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_YEAR, 6); // Agregar 6 para incluir el día actual y los próximos 6 días
        if (calendar.before(today)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            actualizarFechaActual();
        }
    }

    private void mostrarReservas(String fecha){

    }

}