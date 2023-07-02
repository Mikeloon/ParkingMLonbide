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
                    Log.d(TAG,task.getResult().toString());
                    for (QueryDocumentSnapshot ReservaDocument : task.getResult()) {
                        Map<String, Object> reserva = (Map<String, Object>) ReservaDocument.getData().get("reserva");
                        Log.d(TAG,reserva.toString());
                        switch ((String) reserva.get("tipoPlaza")) {
                            case "Coche":
                                String CocheString = String.format("%s: %s", reserva.get("plazaId"), reserva.get("intervaloHoras"));
                                reservasCoche.add(CocheString);
                                break;
                            case "Electrico":
                                String ElectricoString = String.format("%s: %s", reserva.get("plazaId"), reserva.get("intervaloHoras"));
                                reservasElectrico.add(ElectricoString);
                                break;
                            case "Minusvalido":
                                String MinusvString = String.format("%s: %s", reserva.get("plazaId"), reserva.get("intervaloHoras"));
                                reservasMinusv.add(MinusvString);
                                break;
                            case "Moto":
                                String MotoString = String.format("%s: %s", reserva.get("plazaId"), reserva.get("intervaloHoras"));
                                reservasMoto.add(MotoString);
                                break;
                            default:
                                break;
                        }
                    }
                    if (reservasCoche != null && !reservasCoche.isEmpty()) {
                        Log.d(TAG,"Array final coches:" + reservasCoche.toString());
                        RecyclerView.Adapter adapterCoche = new SimpleAdapter(reservasCoche);
                        recyclerViewCoche.setAdapter(adapterCoche);

                    } else{
                        reservasCoche.add("No hay reservas de este tipo de plaza este dia");
                        RecyclerView.Adapter adapterCoche = new SimpleAdapter(reservasCoche);
                        recyclerViewCoche.setAdapter(adapterCoche);
                        Log.d(TAG, String.valueOf(adapterCoche.getItemCount()));
                    }

                    if (reservasElectrico != null && !reservasElectrico.isEmpty()) {
                        RecyclerView.Adapter adapterElectrico = new SimpleAdapter(reservasElectrico);
                        recyclerViewElectrico.setAdapter(adapterElectrico);

                    } else{
                        reservasElectrico.add("No hay reservas de este tipo de plaza este dia");
                        RecyclerView.Adapter adapterElectrico = new SimpleAdapter(reservasElectrico);
                        recyclerViewElectrico.setAdapter(adapterElectrico);
                    }

                    if (reservasMinusv != null && !reservasMinusv.isEmpty()) {
                        RecyclerView.Adapter adapterMinusv = new SimpleAdapter(reservasMinusv);
                        recyclerViewMinusv.setAdapter(adapterMinusv);

                    } else{
                        reservasMinusv.add("No hay reservas de este tipo de plaza este dia");
                        RecyclerView.Adapter adapterMinusv = new SimpleAdapter(reservasMinusv);
                        recyclerViewMinusv.setAdapter(adapterMinusv);
                    }
                    if (reservasMoto != null && !reservasMoto.isEmpty()) {
                        RecyclerView.Adapter adapterMoto = new SimpleAdapter(reservasMoto);
                        recyclerViewMoto.setAdapter(adapterMoto);

                    } else{
                        reservasMoto.add("No hay reservas de este tipo de plaza este dia");
                        RecyclerView.Adapter adapterMoto = new SimpleAdapter(reservasMoto);
                        recyclerViewMoto.setAdapter(adapterMoto);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    fechaTextView.setVisibility(View.VISIBLE);
                    recyclerViewCoche.setVisibility(View.VISIBLE);
                    recyclerViewElectrico.setVisibility(View.VISIBLE);
                    recyclerViewMinusv.setVisibility(View.VISIBLE);
                    recyclerViewMoto.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
