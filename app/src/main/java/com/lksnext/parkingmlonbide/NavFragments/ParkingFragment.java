package com.lksnext.parkingmlonbide.NavFragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lksnext.parkingmlonbide.Adapters.SimpleAdapter;
import com.lksnext.parkingmlonbide.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.Map;



public class ParkingFragment extends Fragment {
    private FirebaseFirestore db;
    private TextView fechaTextView;
    private Calendar calendar;
    private RecyclerView recyclerViewCoche;
    private RecyclerView recyclerViewElectrico;
    private RecyclerView recyclerViewMinusv;
    private RecyclerView recyclerViewMoto;
    private ProgressBar progressBar;

    public static final String MSG_NORESERVAS = "No hay reservas de este tipo de plaza este día";

    public ParkingFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parking, container, false);

        db = FirebaseFirestore.getInstance();
        fechaTextView = view.findViewById(R.id.fechaTextViewP);
        calendar = Calendar.getInstance();

        recyclerViewCoche = view.findViewById(R.id.recyclerViewReservasN);
        recyclerViewElectrico = view.findViewById(R.id.recyclerViewReservasE);
        recyclerViewMinusv = view.findViewById(R.id.recyclerViewReservasMinusv);
        recyclerViewMoto = view.findViewById(R.id.recyclerViewReservasM);
        progressBar = view.findViewById(R.id.progressBarP);
        actualizarFechaActual();
        LinearLayoutManager layoutManagerCoche = new LinearLayoutManager(getContext());
        recyclerViewCoche.setLayoutManager(layoutManagerCoche);

        LinearLayoutManager layoutManagerElectrico = new LinearLayoutManager(getContext());
        recyclerViewElectrico.setLayoutManager(layoutManagerElectrico);

        LinearLayoutManager layoutManagerMinusv = new LinearLayoutManager(getContext());
        recyclerViewMinusv.setLayoutManager(layoutManagerMinusv);

        LinearLayoutManager layoutManagerMoto = new LinearLayoutManager(getContext());
        recyclerViewMoto.setLayoutManager(layoutManagerMoto);


        ImageView btnAnterior = view.findViewById(R.id.btnAnteriorP);
        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrocederDia(view);
            }
        });

        ImageView btnSiguiente = view.findViewById(R.id.btnSiguienteP);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avanzarDia(view);
            }
        });

        return view;
    }
    private void actualizarFechaActual() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MMM/yyyy");
        Date fechaActual = calendar.getTime();
        fechaTextView.setText(formato.format(fechaActual));
        fechaTextView.setGravity(Gravity.CENTER);
        mostrarReservas(formato.format(fechaActual));
    }
    private void retrocederDia(View view) {
        Calendar today = Calendar.getInstance();
        if (calendar.after(today)) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            actualizarFechaActual();
        }
    }
    private void avanzarDia(View view) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_YEAR, 6); // Agregar 6 para incluir el día actual y los próximos 6 días
        if (calendar.before(today)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            actualizarFechaActual();
        }
    }

    private void mostrarReservas(String fecha) {
        ArrayList<String> reservasCoche = new ArrayList<>();
        ArrayList<String> reservasElectrico = new ArrayList<>();
        ArrayList<String> reservasMinusv = new ArrayList<>();
        ArrayList<String> reservasMoto = new ArrayList<>();

        fechaTextView.setVisibility(View.INVISIBLE);
        recyclerViewCoche.setVisibility(View.INVISIBLE);
        recyclerViewElectrico.setVisibility(View.INVISIBLE);
        recyclerViewMinusv.setVisibility(View.INVISIBLE);
        recyclerViewMoto.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Query fechaReserva = db.collection("Parking").whereEqualTo("reserva.FechaReserva", fecha);

        fechaReserva.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (task.getResult() != null){
                    Log.d(TAG, task.getResult().toString());
                    procesarReservas(task.getResult(), reservasCoche, reservasElectrico, reservasMinusv, reservasMoto);
                }
            }
        });
    }

    private void procesarReservas(QuerySnapshot snapshot, ArrayList<String> reservasCoche, ArrayList<String> reservasElectrico, ArrayList<String> reservasMinusv, ArrayList<String> reservasMoto) {
        for (QueryDocumentSnapshot ReservaDocument : snapshot) {
            Map<String, Object> reserva = (Map<String, Object>) ReservaDocument.getData().get("reserva");
            Log.d(TAG, reserva.toString());
            String tipoPlaza = (String) reserva.get("tipoPlaza");
            String reservaString = String.format("%s: %s", reserva.get("plazaId"), reserva.get("intervaloHoras"));

            switch (tipoPlaza) {
                case "Coche":
                    agregarReserva(reservasCoche, reservaString);
                    break;
                case "Electrico":
                    agregarReserva(reservasElectrico, reservaString);
                    break;
                case "Minusvalido":
                    agregarReserva(reservasMinusv, reservaString);
                    break;
                case "Moto":
                    agregarReserva(reservasMoto, reservaString);
                    break;
                default:
                    break;
            }
        }

        configurarRecyclerView(reservasCoche, recyclerViewCoche);
        configurarRecyclerView(reservasElectrico, recyclerViewElectrico);
        configurarRecyclerView(reservasMinusv, recyclerViewMinusv);
        configurarRecyclerView(reservasMoto, recyclerViewMoto);

        progressBar.setVisibility(View.INVISIBLE);
        fechaTextView.setVisibility(View.VISIBLE);
        recyclerViewCoche.setVisibility(View.VISIBLE);
        recyclerViewElectrico.setVisibility(View.VISIBLE);
        recyclerViewMinusv.setVisibility(View.VISIBLE);
        recyclerViewMoto.setVisibility(View.VISIBLE);
    }

    private void agregarReserva(ArrayList<String> listaReservas, String reservaString) {
        listaReservas.add(reservaString);
        if (listaReservas.size() == 1 && listaReservas.get(0).equals(MSG_NORESERVAS)) {
            listaReservas.remove(0);
        }
    }

    private void configurarRecyclerView(ArrayList<String> listaReservas, RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = new SimpleAdapter(listaReservas);
        recyclerView.setAdapter(adapter);
        if (listaReservas.isEmpty()) {
            listaReservas.add(MSG_NORESERVAS);
        }
    }

}
