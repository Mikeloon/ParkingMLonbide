package com.lksnext.parkingmlonbide.Adapters;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingmlonbide.DataClasses.Reserva;
import com.lksnext.parkingmlonbide.DataClasses.TipoEstacionamiento;
import com.lksnext.parkingmlonbide.R;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int PERMISSION_REQUEST_NOTIFICATION = 1;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        // Obtener el ID de la reserva desde el intent
        int reservaId = intent.getIntExtra("reserva_id", 0);
        String user_id = intent.getStringExtra("id_user");
        // Obtener la reserva correspondiente al ID desde la lista de reservas
        getReservaById(reservaId, user_id, new OnReservaLoadedListener() {
            @Override
            public void onReservaLoaded(Reserva reserva) {
                if (reserva != null) {
                    createNotification(context, reserva);
                } else {
                    Log.e(TAG, "Error al obtener el documento del usuario");
                }
            }
        });
    }

    private void getReservaById(long reservaId, String idUser, final OnReservaLoadedListener listener) {
        db.collection("users")
                .document(idUser)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<Map<String, Object>> reservasList = (List<Map<String, Object>>) documentSnapshot.get("reservas");
                            if (reservasList != null) {
                                for (Map<String, Object> reservaData : reservasList) {
                                    Long rid = (Long) reservaData.get("id");
                                    if (rid != null && rid == reservaId) {
                                        // Obtener los valores de los campos de la reserva
                                        com.google.firebase.Timestamp timestamp = (com.google.firebase.Timestamp) reservaData.get("fechaReserva");
                                        Date fechaReserva = timestamp.toDate();
                                        String horaInicio = (String) reservaData.get("horaInicio");
                                        String horaFin = (String) reservaData.get("horaFin");
                                        TipoEstacionamiento tipoPlaza = TipoEstacionamiento.valueOf((String) reservaData.get("tipoPlaza"));
                                        String plazaId = (String) reservaData.get("plazaId");

                                        // Crear objeto Reserva con los valores obtenidos
                                        Reserva reserva = new Reserva(plazaId, rid, fechaReserva, horaInicio, horaFin, tipoPlaza);

                                        // Llamar al callback con la reserva obtenida
                                        listener.onReservaLoaded(reserva);
                                        return; // Terminar el bucle ya que se encontró la reserva
                                    }
                                }
                            }
                        }
                        // Si no se encontró la reserva, llamar al callback con valor nulo
                        listener.onReservaLoaded(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ocurrió un error al obtener el documento
                        Log.e(TAG, "Error al obtener el documento del usuario", e);
                        // Llamar al callback con valor nulo
                        listener.onReservaLoaded(null);
                    }
                });
    }

    // Interfaz para el callback de carga de reserva
    interface OnReservaLoadedListener {
        void onReservaLoaded(Reserva reserva);
    }

    private void createNotification(Context context, Reserva reserva) {
        // Crea y configura la notificación según tus necesidades
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.baseline_car_crash_24)
                .setContentTitle("¡Tu reserva está por finalizar!")
                .setContentText("La reserva de " + reserva.getPlazaId() + " está a punto de finalizar en 15 minutos.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Agrega cualquier configuración adicional que desees para la notificación

        // Muestra la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.VIBRATE}, PERMISSION_REQUEST_NOTIFICATION);
            return;
        }else {
            // Mostrar la notificación
            notificationManager.notify((int) reserva.getId(), builder.build());
        }
    }

}

