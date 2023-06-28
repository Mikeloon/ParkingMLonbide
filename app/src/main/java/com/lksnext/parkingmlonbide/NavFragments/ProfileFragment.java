package com.lksnext.parkingmlonbide.NavFragments;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import com.lksnext.parkingmlonbide.Adapters.AlarmReceiver;
import com.lksnext.parkingmlonbide.Adapters.ReservaAdapter;
import com.lksnext.parkingmlonbide.DataClasses.Reserva;
import com.lksnext.parkingmlonbide.DataClasses.TipoEstacionamiento;
import com.lksnext.parkingmlonbide.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
                                    String horaInicio = (String) reservaData.get("horaInicio");
                                    String horaFin = (String) reservaData.get("horaFin");
                                    TipoEstacionamiento tipoPlaza = TipoEstacionamiento.valueOf((String) reservaData.get("tipoPlaza"));
                                    Long rid = (long) reservaData.get("id");
                                    String plazaId = (String) reservaData.get("plazaId");
                                    // Crear objeto Reserva y agregarlo a la lista
                                    Reserva reserva = new Reserva(plazaId,rid,fechaReserva, horaInicio, horaFin, tipoPlaza);
                                    reservas.add(reserva);
                                }
                            }
                            if (reservas != null && !reservas.isEmpty()) {
                                reservasTxt.setVisibility(View.INVISIBLE);
                                SimpleDateFormat formato = new SimpleDateFormat("dd/MMM/yyyy");
                                if (reservaAdapter == null) {
                                    reservaAdapter = new ReservaAdapter(reservas, formato, v, getParentFragmentManager());
                                    recyclerViewReservas.setAdapter(reservaAdapter);
                                } else {
                                    // Si el adaptador ya existe, actualiza los datos y llama a notifyDataSetChanged()
                                    reservaAdapter.setReservas(reservas);
                                    reservaAdapter.notifyDataSetChanged();
                                }
                                setupAlarms(reservas);
                            }else {
                                recyclerViewReservas.setVisibility(View.GONE);
                                reservasTxt.setText("No tienes reservas");
                            }
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

    private void setupAlarms(List<Reserva> reservas) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        for (Reserva reserva : reservas) {
            Date fechaReserva = reserva.getFechaReserva();
            String horaFinReserva = reserva.getHoraFin();

            // Obtener la hora y minutos de la cadena "horaFin"
            String[] horaMinutos = horaFinReserva.split(":");
            int hora = Integer.parseInt(horaMinutos[0]);
            int minutos = Integer.parseInt(horaMinutos[1]);

            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaReserva);
            cal.set(Calendar.HOUR_OF_DAY, hora);
            cal.set(Calendar.MINUTE, minutos);
            cal.add(Calendar.MINUTE, -15);

            // Configurar la alarma para la fecha y hora deseada
            Intent intent = new Intent(requireContext(), AlarmReceiver.class);
            intent.putExtra("reserva_id", reserva.getId()); // Puedes pasar información adicional si es necesario
            intent.putExtra("id_user", FirebaseAuth.getInstance().getCurrentUser().getUid());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), (int) reserva.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            }
        }
    }
}