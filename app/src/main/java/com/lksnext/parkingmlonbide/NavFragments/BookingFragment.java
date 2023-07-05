package com.lksnext.parkingmlonbide.NavFragments;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lksnext.parkingmlonbide.Adapters.AlarmReceiver;
import com.lksnext.parkingmlonbide.DataClasses.Parking;
import com.lksnext.parkingmlonbide.DataClasses.Reserva;
import com.lksnext.parkingmlonbide.DataClasses.TipoEstacionamiento;
import com.lksnext.parkingmlonbide.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookingFragment extends Fragment {

    private FirebaseFirestore db;
    private TextView fechaTextView;
    private Calendar calendar;

    public static final String DAY_FORMAT = "dd/MM/yyyy";

    String[] horasInicio = {"08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00"};
    String[] horasFin = {"08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        createNotificationChannel();
        db = FirebaseFirestore.getInstance();
        fechaTextView = view.findViewById(R.id.fechaTextView);
        LinearLayout linearLayoutReservaCoche = view.findViewById(R.id.LinearLayoutRealizarReservaCoche);
        LinearLayout linearLayoutReservaElectrico = view.findViewById(R.id.LinearLayoutRealizarReservaElec);
        LinearLayout linearLayoutReservaMinusv = view.findViewById(R.id.LinearLayoutRealizarReservaMinusv);
        LinearLayout linearLayoutReservaMoto = view.findViewById(R.id.LinearLayoutRealizarReservaMoto);

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

        linearLayoutReservaCoche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupReserva("Reserva de plaza para coche convencional");
            }
        });

        linearLayoutReservaElectrico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupReserva("Reserva de plaza para coche eléctrico");
            }
        });

        linearLayoutReservaMinusv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupReserva("Reserva de plaza para minusválidos");
            }
        });

        linearLayoutReservaMoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupReserva("Reserva de plaza para moto");
            }
        });

        calendar = Calendar.getInstance();
        actualizarFechaActual();
        return view;
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReservaReminderChannel";
            String description = "Channel for booking reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("reservaNotification",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void actualizarFechaActual() {
        SimpleDateFormat formato = new SimpleDateFormat(DAY_FORMAT);
        Date fechaActual = calendar.getTime();
        fechaTextView.setText(formato.format(fechaActual));
        fechaTextView.setGravity(Gravity.CENTER);
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

    private void mostrarPopupReserva(String tipoReserva) {

        PopupWindow popupWindow = new PopupWindow(requireContext());

        View popupView = getLayoutInflater().inflate(R.layout.popup_reserva, null);

        TextView textoCalculoHoras = popupView.findViewById(R.id.textoCalculoHoras);
        TextView tituloPopup = popupView.findViewById(R.id.tituloPopup);
        Spinner comboBoxHoraInicio = popupView.findViewById(R.id.comboBoxHoraInicio);
        Spinner comboBoxHoraFin = popupView.findViewById(R.id.comboBoxHoraFin);

        tituloPopup.setText(tipoReserva);

        ArrayAdapter<String> horaInicioAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, horasInicio);
        comboBoxHoraInicio.setAdapter(horaInicioAdapter);

        ArrayAdapter<String> horaFinAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, horasFin);
        comboBoxHoraFin.setAdapter(horaFinAdapter);
        Button btnAceptar = popupView.findViewById(R.id.btnAceptar);

        String tipoPlaza = getTipoPlaza(tipoReserva).toString();
        String fechaReserva = fechaTextView.getText().toString();

        Spinner comboBoxPlaza = popupView.findViewById(R.id.comboBoxPlaza);

        ArrayList<String> plazasIds = obtenerIdsPlazas(tipoPlaza);
        ArrayAdapter<String> plazaAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, plazasIds);
        comboBoxPlaza.setAdapter(plazaAdapter);

        comboBoxHoraInicio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String horaInicio = comboBoxHoraInicio.getSelectedItem().toString();
                String horaFin = comboBoxHoraFin.getSelectedItem().toString();

                // Calcula la duración en horas
                int duracionHoras = calcularDuracionHoras(horaInicio, horaFin);
                String fechaReserva = fechaTextView.getText().toString();
                // Verifica si la reserva es válida
                boolean reservaValida = esReservaValida(fechaReserva,horaInicio, horaFin);

                // Actualiza el texto del cálculo de horas
                if (reservaValida) {
                    String texto = "Tipo de plaza: "+ tipoPlaza + "\n Fecha de reserva: " + fechaReserva + "\n" + horaInicio + " - " + horaFin + " (" + duracionHoras + " horas)";
                    textoCalculoHoras.setText(texto);
                } else {
                    textoCalculoHoras.setText("Reserva inválida");
                }
                btnAceptar.setEnabled(reservaValida);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó nada
            }
        });
        comboBoxHoraFin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String horaInicio = comboBoxHoraInicio.getSelectedItem().toString();
                String horaFin = comboBoxHoraFin.getSelectedItem().toString();

                // Calcula la duración en horas
                int duracionHoras = calcularDuracionHoras(horaInicio, horaFin);
                String fechaReserva = fechaTextView.getText().toString();
                // Verifica si la reserva es válida
                boolean reservaValida = esReservaValida(fechaReserva,horaInicio, horaFin);

                // Actualiza el texto del cálculo de horas
                if (reservaValida) {
                    String texto = "Tipo de plaza: "+ tipoPlaza + "\n Fecha de reserva: " + fechaReserva + "\n" + horaInicio + " - " + horaFin + " (" + duracionHoras + " horas)";
                    textoCalculoHoras.setText(texto);
                } else {
                    textoCalculoHoras.setText("Reserva inválida");
                }
                btnAceptar.setEnabled(reservaValida);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó nada
            }
        });

        // Configurar el tamaño y la interactividad del popup
        popupWindow.setContentView(popupView);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        // Mostrar el popup en la posición deseada
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);

        ImageView btnCerrar = popupView.findViewById(R.id.btnCerrar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String horaInicio = comboBoxHoraInicio.getSelectedItem().toString();
                String horaFin = comboBoxHoraFin.getSelectedItem().toString();
                TipoEstacionamiento plaza = getTipoPlaza(tipoReserva);
                comprobarDisponibilidad(horaInicio, horaFin, fechaReserva, plaza, comboBoxPlaza.getSelectedItem().toString(), new DisponibilidadCallback() {
                    @Override
                    public void onDisponibilidadChecked(boolean disponible) {
                        if (disponible) {
                            String fechaReserva = fechaTextView.getText().toString();
                            Date fechaDate = null;
                            SimpleDateFormat dateFormat = new SimpleDateFormat(DAY_FORMAT);
                            try {
                                fechaDate = dateFormat.parse(fechaReserva);
                            } catch (ParseException e) {
                            }

                            Reserva reservation = new Reserva(comboBoxPlaza.getSelectedItem().toString(),fechaDate, horaInicio, horaFin, plaza);
                            Long rid = reservation.getId();

                            // Add the reservation to the appropriate document in the "parking" collection
                            agregarReserva(comboBoxPlaza,rid,tipoPlaza,fechaReserva,horaInicio,horaFin, reservation);

                        } else {
                            // La plaza no está disponible
                            Toast.makeText(getActivity(), "La plaza seleccionada ya está reservada en ese horario", Toast.LENGTH_SHORT).show();
                        }
                        popupWindow.dismiss();
                    }
                });
            }
        });
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public int calcularDuracionHoras(String horaInicio, String horaFin) {
        int inicioHoras = obtenerHoras(horaInicio);
        int inicioMinutos = obtenerMinutos(horaInicio);

        int finHoras = obtenerHoras(horaFin);
        int finMinutos = obtenerMinutos(horaFin);

        int duracionHoras;
        if (finHoras < inicioHoras || (finHoras == inicioHoras && finMinutos < inicioMinutos)) {
            // Hora fin es anterior a la hora de inicio
            duracionHoras = 0;
        } else {
            duracionHoras = (finHoras - inicioHoras);
            if (finMinutos > inicioMinutos) {
                // Agregar 1 a la duración si los minutos de fin son mayores a los minutos de inicio
                duracionHoras += 1;
            }
        }

        return duracionHoras;
    }

    private boolean esReservaValida(String fechaReserva,String horaInicio, String horaFin) {

        int duracionHoras = calcularDuracionHoras(horaInicio, horaFin);
        LocalTime horaActual = null;
        int inicioHoras = obtenerHoras(horaInicio);
        int inicioMinutos = obtenerMinutos(horaInicio);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate fechaActual = LocalDate.now();
            horaActual = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DAY_FORMAT);
            LocalDate fechaIngresada = LocalDate.parse(fechaReserva, formatter);

            if (fechaActual.equals(fechaIngresada) && horaActual.toSecondOfDay() > inicioHoras * 3600 + inicioMinutos * 60) {
                return false;
            }
        }
        if (duracionHoras <= 0 || duracionHoras > 8) {
            return false;
        }

        return true;
    }

    public int obtenerHoras(String hora) {
        String[] partes = hora.split(":");
        return Integer.parseInt(partes[0]);
    }

    public int obtenerMinutos(String hora) {
        String[] partes = hora.split(":");
        return Integer.parseInt(partes[1]);
    }

    private int obtenerHora(String hora) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = dateFormat.parse(hora);
        } catch (ParseException e) {
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Obtener los minutos de la hora
        int minutos = calendar.get(Calendar.MINUTE);

        // Redondear los minutos a la media hora más cercana
        int minutosRedondeados = Math.round(minutos / 30f) * 30;

        // Establecer los minutos redondeados en el calendario
        calendar.set(Calendar.MINUTE, minutosRedondeados);

        // Obtener la hora del calendario
        int horaExacta = calendar.get(Calendar.HOUR_OF_DAY);

        return horaExacta;
    }

    private TipoEstacionamiento getTipoPlaza(String plaza){
        TipoEstacionamiento tipoReserva = null;
        if (plaza.contains("convencional")) {
            tipoReserva = TipoEstacionamiento.Coche;
        } else if (plaza.contains("eléctrico")) {
            tipoReserva = TipoEstacionamiento.Electrico;
        } else if (plaza.contains("minusválidos")) {
            tipoReserva = TipoEstacionamiento.Minusvalido;
        } else if (plaza.contains("moto")) {
            tipoReserva = TipoEstacionamiento.Moto;
        }
        if (tipoReserva == null) {
            throw new IllegalArgumentException("Tipo de plaza no válido: " + plaza);
        }
        return tipoReserva;
    }

    private ArrayList<String> obtenerIdsPlazas(String tipoPlaza) {
        ArrayList<String> idsPlazas = new ArrayList<>();
        String plazaId = "";
        switch (tipoPlaza) {
            case "Coche":
                for (int i = 0; i < Parking.PlazaCoches; i++) {
                    plazaId = "Coche" + i;
                    idsPlazas.add(plazaId);
                }
                break;
            case "Electrico":
                for (int i = 0; i < Parking.PlazasElectricos; i++) {
                    plazaId = "Electrico" + i;
                    idsPlazas.add(plazaId);
                }
                break;
            case "Minusvalido":
                for (int i = 0; i < Parking.PlazasMinusvalidos; i++) {
                    plazaId = "Minusvalido" + i;
                    idsPlazas.add(plazaId);
                }
                break;
            case "Moto":
                for (int i = 0; i < Parking.Motos; i++) {
                    plazaId = "Moto" + i;
                    idsPlazas.add(plazaId);
                }
                break;
            default:
                break;
        }

        return idsPlazas;
    }

    private void agregarReserva(Spinner comboBoxPlaza, Long rid, String tipoPlaza, String fechaReserva, String horaInicio, String horaFin, Reserva reserva) {
        // Obtener el ID de la plaza seleccionada
        String idPlaza = comboBoxPlaza.getSelectedItem().toString();

        HashMap<String, Object> reservaData = new HashMap<>();
        reservaData.put("FechaReserva", fechaReserva);
        reservaData.put("usuarioId", FirebaseAuth.getInstance().getCurrentUser().getUid());  // Nombre de usuario
        reservaData.put("intervaloHoras", horaInicio + "-" + horaFin);
        reservaData.put("tipoPlaza", tipoPlaza);
        reservaData.put("plazaId", idPlaza);

        DocumentReference docRef = db.collection("Parking").document(String.valueOf(rid));

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "El documento ya existe");
                } else {
                    Map<String, Object> data = new HashMap<>();
                    data.put("reserva", reservaData);

                    docRef.set(data)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getActivity(), "Reserva realizada correctamente", Toast.LENGTH_SHORT).show();
                                setupAlarm(reserva,getContext());
                            })
                            .addOnFailureListener(e -> Log.d(TAG, "Error al crear el documento de reserva en Parking"));
                }
            } else {
                Log.d(TAG, "Error al obtener el documento de reserva en Parking");
            }
        });
    }

    private void comprobarDisponibilidad(String horaInicio, String horaFin, String fechaReserva, TipoEstacionamiento tipoPlaza, String plazaId, DisponibilidadCallback callback) {
        // Obtener el intervalo de tiempo seleccionado
        int horaInicioSeleccionada = obtenerHora(horaInicio);
        int horaFinSeleccionada = obtenerHora(horaFin);

        // Consultar todas las reservas existentes para la plaza y la fecha seleccionadas
        Query ReservasPlaza = db.collection("Parking").whereEqualTo("reserva.plazaId", plazaId).whereEqualTo("reserva.FechaReserva",fechaReserva);
        ReservasPlaza.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        Log.d(TAG,task.getResult().toString());
                        checkReservasDisponibilidad(task.getResult(), horaInicioSeleccionada, horaFinSeleccionada, callback);
                    }
                }else {
                    callback.onDisponibilidadChecked(false);
                }
            }
        });
    }
    private void checkReservasDisponibilidad(QuerySnapshot snapshot, int horaInicioSeleccionada, int horaFinSeleccionada, DisponibilidadCallback callback) {
        for (QueryDocumentSnapshot ReservaU: snapshot) {
            Map<String, Object> reserva = (Map<String, Object>) ReservaU.getData().get("reserva");
            Log.d(TAG,reserva.toString());
            String intervaloHoras = (String) reserva.get("intervaloHoras");
            int horaInicioReservaInt = obtenerHora(intervaloHoras.split("-")[0]);
            int horaFinReservaInt = obtenerHora(intervaloHoras.split("-")[1]);
            if ((horaInicioSeleccionada <= horaFinReservaInt && horaInicioSeleccionada >= horaInicioReservaInt) || (horaFinSeleccionada >= horaInicioReservaInt && horaInicioSeleccionada <= horaFinReservaInt)) {
                // Hay una reserva existente que se superpone con el intervalo seleccionado
                callback.onDisponibilidadChecked(false);
                return;
            }
        }
        callback.onDisponibilidadChecked(true);
    }

    private void setupAlarm(Reserva reserva, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Date fechaReserva = reserva.getFechaReserva();
        String horaFinReserva = reserva.getHoraFin();

        String[] horaMinutos = horaFinReserva.split(":");
        int hora = Integer.parseInt(horaMinutos[0]);
        int minutos = Integer.parseInt(horaMinutos[1]);

        Calendar currentTime = Calendar.getInstance();

        Calendar reservaTime = Calendar.getInstance();
        reservaTime.setTime(fechaReserva);
        reservaTime.set(Calendar.HOUR_OF_DAY, hora);
        reservaTime.set(Calendar.MINUTE, minutos);

        long remainingMilliseconds = reservaTime.getTimeInMillis() - currentTime.getTimeInMillis();
        int remainingMinutes = (int) (remainingMilliseconds / (1000 * 60));

        if (remainingMinutes <= 0) {
            Log.d(TAG,"Reserva terminada");
        }
        else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaReserva);
            cal.set(Calendar.HOUR_OF_DAY, hora);
            cal.set(Calendar.MINUTE, minutos);
            cal.set(Calendar.SECOND, 0);
            cal.add(Calendar.MINUTE, -15);

            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_IMMUTABLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                Log.d(TAG,"alarma creada");
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                Log.d(TAG,"alarma creada");
            }
        }
    }

    public interface DisponibilidadCallback {
        void onDisponibilidadChecked(boolean disponible);
    }

}