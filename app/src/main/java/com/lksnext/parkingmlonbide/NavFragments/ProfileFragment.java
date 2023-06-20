package com.lksnext.parkingmlonbide.NavFragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.lksnext.parkingmlonbide.DataClasses.Reserva;
import com.lksnext.parkingmlonbide.Adapters.ReservaAdapter;
import com.lksnext.parkingmlonbide.DataClasses.TipoEstacionamiento;
import com.lksnext.parkingmlonbide.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private FirebaseFirestore db;
    private ReservaAdapter reservaAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView userTxt = (TextView) v.findViewById(R.id.txtWelcome);
        TextView mailTxt = (TextView) v.findViewById(R.id.Correotxt);
        TextView reservasTxt = (TextView) v.findViewById(R.id.reservastxt);
        RecyclerView recyclerViewReservas = v.findViewById(R.id.recyclerViewReservas);
        recyclerViewReservas.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // El documento existe, puedes acceder a los campos
                            String username = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            userTxt.setText(username);
                            mailTxt.setText(email);

                            List<Reserva> reservas = new ArrayList<>();
                            List<Map<String, Object>> reservasList = (List<Map<String, Object>>) documentSnapshot.get("reservas");
                            if (reservasList != null) {
                                for (Map<String, Object> reservaData : reservasList) {
                                    // Obtener los valores de los campos de la reserva
                                    com.google.firebase.Timestamp timestamp = (com.google.firebase.Timestamp) reservaData.get("fechaReserva");
                                    Date fechaReserva = timestamp.toDate();
                                    Long horasReservaLong = (Long) reservaData.get("horasReserva");
                                    int horasReserva = horasReservaLong.intValue();
                                    TipoEstacionamiento tipoPlaza = TipoEstacionamiento.valueOf((String) reservaData.get("tipoPlaza"));

                                    // Crear objeto Reserva y agregarlo a la lista
                                    Reserva reserva = new Reserva();
                                    reserva.setFechaReserva(fechaReserva);
                                    reserva.setHorasReserva(horasReserva);
                                    reserva.setTipoPlaza(tipoPlaza);

                                    reservas.add(reserva);
                                }
                            }
                            if (reservas != null && !reservas.isEmpty()) {
                                reservasTxt.setVisibility(View.INVISIBLE);
                                SimpleDateFormat formato = new SimpleDateFormat("dd/MMM/yyyy");
                                if (reservaAdapter == null) {
                                    reservaAdapter = new ReservaAdapter(reservas, formato);
                                    recyclerViewReservas.setAdapter(reservaAdapter);
                                } else {
                                    // Si el adaptador ya existe, actualiza los datos y llama a notifyDataSetChanged()
                                    reservaAdapter.setReservas(reservas);
                                    reservaAdapter.notifyDataSetChanged();

                            } }else {
                                recyclerViewReservas.setVisibility(View.GONE);
                                reservasTxt.setText("No tienes reservas");
                            }
                            // Utiliza los campos como desees
                            Log.d(TAG, "Nombre: " + username);
                            Log.d(TAG, "Correo electrónico: " + email);
                        } else {
                            // El documento no existe o aún no se ha creado
                            Log.d(TAG, "El documento no existe");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ocurrió un error al obtener el documento
                        Log.e(TAG, "Error al obtener el documento del usuario", e);
                    }
                });

        return v;
    }
}