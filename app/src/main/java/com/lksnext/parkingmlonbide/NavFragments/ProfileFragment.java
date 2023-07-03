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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lksnext.parkingmlonbide.Adapters.ReservaAdapter;
import com.lksnext.parkingmlonbide.DataClasses.Reserva;
import com.lksnext.parkingmlonbide.DataClasses.TipoEstacionamiento;
import com.lksnext.parkingmlonbide.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private FirebaseFirestore db;
    private ReservaAdapter reservaAdapter;

    private View v;
    private ProgressBar progressBar;

    private TextView userTxt;
    private TextView mailTxt;
    private TextView reservasTxt;
    private  RecyclerView recyclerViewReservas;
    private String uid;

    public static final String DAY_FORMAT = "dd/MMM/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(DAY_FORMAT);

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        userTxt = (TextView) v.findViewById(R.id.txtWelcome);
        mailTxt = (TextView) v.findViewById(R.id.Correotxt);
        reservasTxt = (TextView) v.findViewById(R.id.reservastxt);
        recyclerViewReservas = v.findViewById(R.id.recyclerViewReservas);
        progressBar = v.findViewById(R.id.progressBar);

        recyclerViewReservas.setLayoutManager(new LinearLayoutManager(getContext()));
        db = FirebaseFirestore.getInstance();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        buscarReservasUsuario(uid, userTxt, mailTxt, reservasTxt, recyclerViewReservas, v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        buscarReservasUsuario(uid, userTxt, mailTxt, reservasTxt, recyclerViewReservas, v);
    }

    private void getUser(String uid){
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
                            userTxt.setVisibility(View.VISIBLE);
                            mailTxt.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private Date parseBookingDate(String fecha){
        Date date = null;
        try {
            date = sdf.parse(fecha);
        } catch (ParseException e) {
            System.out.println("Error");
        }
        return date;
    }

    private void setUpUserBooking(List<Reserva> reservaList){
        if (!reservaList.isEmpty()) {
            reservasTxt.setVisibility(View.INVISIBLE);
            recyclerViewReservas.setVisibility(View.VISIBLE);
            SimpleDateFormat formato = new SimpleDateFormat(DAY_FORMAT);
            reservaAdapter = new ReservaAdapter(reservaList, formato, v, getParentFragmentManager());
            recyclerViewReservas.setAdapter(reservaAdapter);
        } else {
            recyclerViewReservas.setVisibility(View.GONE);
            reservasTxt.setVisibility(View.VISIBLE);
            SimpleDateFormat formato = new SimpleDateFormat(DAY_FORMAT);
            reservasTxt.setText("No tienes reservas");
            reservaAdapter = new ReservaAdapter(new ArrayList<>(), formato, v, getParentFragmentManager());
            recyclerViewReservas.setAdapter(reservaAdapter);
        }
        progressBar.setVisibility(View.GONE);
    }

    private void buscarReservasUsuario(String uid, TextView userTxt, TextView mailTxt, TextView reservasTxt, RecyclerView recyclerViewReservas, View v){

        progressBar.setVisibility(View.VISIBLE);
        // Ocultar otros elementos relevantes
        userTxt.setVisibility(View.INVISIBLE);
        mailTxt.setVisibility(View.INVISIBLE);
        reservasTxt.setVisibility(View.INVISIBLE);
        recyclerViewReservas.setVisibility(View.INVISIBLE);

        getUser(uid);

        Query UsuarioReservas = db.collection("Parking").whereEqualTo("reserva.usuarioId", uid);
        List<Reserva> reservaList = new ArrayList<>();
        Date currentDate = new Date(); // Obtener la fecha actual
        Log.d(TAG,"CurrentDate: " + currentDate);
        UsuarioReservas.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        for (QueryDocumentSnapshot ReservaU: task.getResult()){
                            Map<String, Object> reserva = (Map<String, Object>) ReservaU.getData().get("reserva");
                            Log.d(TAG,reserva.toString());
                            String plazaId = (String) reserva.get("plazaId");
                            Long id = Long.valueOf(ReservaU.getId());
                            String fecha = (String) reserva.get("FechaReserva");
                            Date date = parseBookingDate(fecha);

                            if (date != null && !date.before(currentDate)) {
                                String intervaloHoras = (String) reserva.get("intervaloHoras");
                                TipoEstacionamiento tipoPlaza = TipoEstacionamiento.valueOf((String) reserva.get("tipoPlaza"));
                                Reserva reservaElem = new Reserva(plazaId, id, date, intervaloHoras.split("-")[0], intervaloHoras.split("-")[1], tipoPlaza);
                                reservaList.add(reservaElem);
                            }
                        }
                    }
                    setUpUserBooking(reservaList);
                }
            }
        });
    }


}