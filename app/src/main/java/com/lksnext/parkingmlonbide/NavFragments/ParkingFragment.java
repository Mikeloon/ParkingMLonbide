package com.lksnext.parkingmlonbide.NavFragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingmlonbide.Adapters.CustomListAdapter;
import com.lksnext.parkingmlonbide.DataClasses.Parking;
import com.lksnext.parkingmlonbide.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ParkingFragment extends Fragment {
    private FirebaseFirestore db;
    private TextView fechaTextView;
    private Calendar calendar;
    private TextView fechaCorrespondienteTextView;
    private LinearLayout linearLayout;
    private ListView listViewCoche;
    private ListView listViewElectrico;
    private ListView listViewMinusv;
    private ListView listViewMoto;


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
        fechaCorrespondienteTextView = view.findViewById(R.id.fechaCorrespondienteTextViewP);
        linearLayout = view.findViewById(R.id.LinearLayoutFechaReservaP);
        listViewCoche = view.findViewById(R.id.listViewCocheP);
        listViewElectrico = view.findViewById(R.id.listViewElectricoP);
        listViewMinusv = view.findViewById(R.id.listViewMinusvP);
        listViewMoto = view.findViewById(R.id.listViewMotoP);

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

        calendar = Calendar.getInstance();
        actualizarFechaActual(view);

        return view;
    }

    private void actualizarFechaActual(View view) {
        SimpleDateFormat formato = new SimpleDateFormat("dd-MMM-yyyy");
        Date fechaActual = calendar.getTime();
        fechaTextView.setText(formato.format(fechaActual));
        fechaTextView.setGravity(Gravity.CENTER);
        mostrarReservas(formato.format(fechaActual), view);
    }

    private void retrocederDia(View view) {
        Calendar today = Calendar.getInstance();
        if (calendar.after(today)) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            actualizarFechaActual(view);
        }
    }

    private void avanzarDia(View view) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_YEAR, 6); // Agregar 6 para incluir el día actual y los próximos 6 días
        if (calendar.before(today)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            actualizarFechaActual(view);
        }
    }

    private void mostrarReservas(String fecha,View view) {
        SimpleDateFormat formato = new SimpleDateFormat("dd-MMM-yyyy");
        Log.d(TAG,fecha);
        // Obtener las reservas del día actual para cada tipo de plaza
        db.collection("Parking")
                .document(fecha)
                .collection("Coche")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<String> reservasCoche = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String plazaId = document.getId();
                        Log.d(TAG,plazaId);
                        List<Map<String, Object>> reservas = (List<Map<String, Object>>) document.get("reservas");
                        if (reservas != null) {
                            for (Map<String, Object> reserva : reservas) {
                                String intervaloHoras = (String) reserva.get("intervaloHoras");
                                String usuario = (String) reserva.get("usuario");
                                String reservaString = String.format("%s \n %s: %s", plazaId, usuario, intervaloHoras);
                                reservasCoche.add(reservaString);
                            }
                        }
                    }
                    ArrayAdapter<String> adapterCoche = new CustomListAdapter(requireContext(), reservasCoche);
                    listViewCoche.setAdapter(adapterCoche);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG,"Error en coger las reservas de coches");
                });

        db.collection("Parking")
                .document(fecha)
                .collection("Electrico")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<String> reservasElectrico = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String plazaId = document.getId();
                        List<Map<String, Object>> reservas = (List<Map<String, Object>>) document.get("reservas");
                        if (reservas != null) {
                            for (Map<String, Object> reserva : reservas) {
                                String intervaloHoras = (String) reserva.get("intervaloHoras");
                                String usuario = (String) reserva.get("usuario");
                                String reservaString = String.format("%s \n %s: %s", plazaId, usuario, intervaloHoras);
                                reservasElectrico.add(reservaString);
                            }
                        }
                    }
                    ArrayAdapter<String> adapterElectrico = new CustomListAdapter(requireContext(), reservasElectrico);
                    listViewElectrico.setAdapter(adapterElectrico);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG,"Error en coger las reservas electricas");
                });

        db.collection("Parking")
                .document(fecha)
                .collection("Minusvalido")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<String> reservasMinusv = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String plazaId = document.getId();
                        List<Map<String, Object>> reservas = (List<Map<String, Object>>) document.get("reservas");
                        if (reservas != null) {
                            for (Map<String, Object> reserva : reservas) {
                                String intervaloHoras = (String) reserva.get("intervaloHoras");
                                String usuario = (String) reserva.get("usuario");
                                String reservaString = String.format("%s \n %s: %s", plazaId, usuario, intervaloHoras);
                                reservasMinusv.add(reservaString);
                            }
                        }
                    }
                    ArrayAdapter<String> adapterMinusv = new CustomListAdapter(requireContext(), reservasMinusv);
                    listViewMinusv.setAdapter(adapterMinusv);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG,"Error en coger las reservas minusvalido");
                });

        db.collection("Parking")
                .document(fecha)
                .collection("Moto")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<String> reservasMoto = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String plazaId = document.getId();
                        List<Map<String, Object>> reservas = (List<Map<String, Object>>) document.get("reservas");
                        if (reservas != null) {
                            for (Map<String, Object> reserva : reservas) {
                                String intervaloHoras = (String) reserva.get("intervaloHoras");
                                String usuario = (String) reserva.get("usuario");
                                String reservaString = String.format("%s \n %s: %s", plazaId, usuario, intervaloHoras);
                                reservasMoto.add(reservaString);
                            }
                        }
                    }
                    ArrayAdapter<String> adapterMoto = new CustomListAdapter(requireContext(), reservasMoto);
                    listViewMoto.setAdapter(adapterMoto);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG,"Error en coger las reservas moto");
                });
    }
}
