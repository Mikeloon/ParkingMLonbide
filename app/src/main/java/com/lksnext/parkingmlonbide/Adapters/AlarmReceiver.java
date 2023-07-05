package com.lksnext.parkingmlonbide.Adapters;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.lksnext.parkingmlonbide.view.HomePage;
import com.lksnext.parkingmlonbide.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, HomePage.class);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_MUTABLE);
        Log.d(TAG, "Intent creado hecho");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "reservaNotification")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Tu reserva está a punto de finalizar!")
                .setContentText("Quedan 15 minutos para que termine tu reserva.")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        Log.d(TAG, "builder hecho");
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // El permiso no está otorgado, inicia la actividad de solicitud de permisos
                Intent permissionIntent = new Intent(context, PermissionRequestActivity.class);
                permissionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(permissionIntent);
            } else {
                // El permiso ya está otorgado, crea la notificación
                notificationManagerCompat.notify(123, builder.build());
                Log.d(TAG, "notificacion hecha");
            }
        } else {
            // El dispositivo no está ejecutando Android Tiramisú, crea la notificación sin solicitar permisos
            notificationManagerCompat.notify(123, builder.build());
            Log.d(TAG, "notificacion hecha");
        }
    }
}