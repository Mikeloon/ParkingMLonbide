package com.lksnext.parkingmlonbide.NavFragments;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.util.List;
import java.util.Map;

public class BookingFragment extends Fragment {

    private TextView toolbarTitle;
    private FirebaseFirestore db;
    private TextView fechaTextView;
    private Calendar calendar;
    private TextView fechaCorrespondienteTextView;
    private LinearLayout linearLayout;

    String[] horasInicio = {"08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30"};
    String[] horasFin = {"08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "15:32"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        db = FirebaseFirestore.getInstance();
        fechaTextView = view.findViewById(R.id.fechaTextView);
        fechaCorrespondienteTextView = view.findViewById(R.id.fechaCorrespondienteTextView);
        linearLayout = view.findViewById(R.id.LinearLayoutFechaReserva);
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

    private void actualizarFechaActual() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MMM/yyyy");
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
        // Configura los datos para los ComboBoxes comboBoxHoraInicio y comboBoxHoraFin

        ArrayAdapter<String> horaInicioAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, horasInicio);
        comboBoxHoraInicio.setAdapter(horaInicioAdapter);

        ArrayAdapter<String> horaFinAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, horasFin);
        comboBoxHoraFin.setAdapter(horaFinAdapter);
        Button btnAceptar = popupView.findViewById(R.id.btnAceptar);

        String tipoPlaza = getTipoPlaza(tipoReserva).toString();
        String fechaReserva = fechaTextView.getText().toString();
        String documentoFecha = fechaReserva.replace("/", "-");

        Spinner comboBoxPlaza = popupView.findViewById(R.id.comboBoxPlaza);
        obtenerIdsPlazas(documentoFecha, tipoPlaza, new OnIdsPlazasObtenidosListener() {
            @Override
            public void onIdsPlazasObtenidos(ArrayList<String> idsPlazas) {
                ArrayAdapter<String> plazaAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, idsPlazas);
                comboBoxPlaza.setAdapter(plazaAdapter);
            }
        });

        db.collection("Parking").document(documentoFecha)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "El documento ya existe");
                            } else {
                                // El documento no existe, crearlo
                                db.collection("Parking").document(documentoFecha)
                                        .set(new HashMap<String, Object>())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                crearPlazas(documentoFecha);
                                                Log.d(TAG, "Documento creado correctamente");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Error al crear el documento
                                                Log.d(TAG, "Error al crear el documento de fecha en Parking");
                                            }
                                        });
                            }
                        } else {
                            // Error al obtener el documento
                            Log.d(TAG, "Error al obtener el documento de fecha en Parking");
                        }
                    }
                });

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
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
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
                // Check if the reservation is valid
                boolean reservaValida = esReservaValida(fechaReserva,horaInicio, horaFin);
                comprobarDisponibilidad(horaInicio, horaFin, fechaReserva, plaza, comboBoxPlaza.getSelectedItem().toString(), new DisponibilidadCallback() {
                    @Override
                    public void onDisponibilidadChecked(boolean disponible) {
                        if (disponible) {
                            String fechaReserva = fechaTextView.getText().toString();
                            Date fechaDate = null;
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
                            try {
                                fechaDate = dateFormat.parse(fechaReserva);
                            } catch (ParseException e) {
                            }
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            Reserva reservation = new Reserva(comboBoxPlaza.getSelectedItem().toString(),fechaDate, horaInicio, horaFin, plaza);
                            Long rid = reservation.getId();

                            // Add the reservation to the appropriate document in the "parking" collection
                            agregarReserva(comboBoxPlaza,rid,tipoPlaza,fechaReserva,horaInicio,horaFin);

                            DocumentReference documentReference = db.collection("users").document(uid);
                            documentReference.update("reservas", FieldValue.arrayUnion(reservation))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Reserva en el dia:" + fechaReserva +" confirmada", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Error al guardar la reserva", Toast.LENGTH_SHORT).show();
                                        }
                                    });
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

    private int calcularDuracionHoras(String horaInicio, String horaFin) {
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
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

    private int obtenerHoras(String hora) {
        String[] partes = hora.split(":");
        return Integer.parseInt(partes[0]);
    }

    private int obtenerMinutos(String hora) {
        String[] partes = hora.split(":");
        return Integer.parseInt(partes[1]);
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
        return tipoReserva;
    }

    private void crearPlazas(String documentoFecha) {
        crearColeccionPlazas(documentoFecha, "Coche", Parking.PlazaCoches);
        crearColeccionPlazas(documentoFecha, "Electrico", Parking.PlazasElectricos);
        crearColeccionPlazas(documentoFecha, "Minusvalido", Parking.PlazasMinusvalidos);
        crearColeccionPlazas(documentoFecha, "Moto", Parking.Motos);
    }

    private void crearColeccionPlazas(String documentoFecha, String tipoPlaza, int cantidad) {
        Map<String, Object> plazaData = new HashMap<>();
        plazaData.put("reservas", new ArrayList<>());

        for (int i = 0; i < cantidad; i++) {
            String plazaId = tipoPlaza.replaceAll("\\s+", "") + (i + 1);
            crearPlazaEnFirestore(documentoFecha, tipoPlaza, plazaId, plazaData);
        }
    }

    private void crearPlazaEnFirestore(String documentoFecha, String tipoPlaza, String plazaId, Map<String, Object> plazaData) {
        db.collection("Parking").document(documentoFecha).collection(tipoPlaza).document(plazaId)
                .set(plazaData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, plazaId + " creada correctamente en " + tipoPlaza);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error al crear " + plazaId + " en " + tipoPlaza + ": " + e.getMessage());
                    }
                });
    }

    private void obtenerIdsPlazas(String documentoFecha, String tipoPlaza, OnIdsPlazasObtenidosListener listener) {
        db.collection("Parking").document(documentoFecha).collection(tipoPlaza)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<String> idsPlazas = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String plazaId = documentSnapshot.getId();
                            idsPlazas.add(plazaId);
                        }
                        listener.onIdsPlazasObtenidos(idsPlazas);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error al obtener los ids de las plazas
                        listener.onIdsPlazasObtenidos(new ArrayList<>());
                    }
                });
    }

    private void agregarReserva(Spinner comboBoxPlaza, Long rid, String tipoPlaza, String fechaReserva, String horaInicio, String horaFin) {
        String documentoFecha = fechaReserva.replace("/", "-");

        // Obtener el ID de la plaza seleccionada
        String idPlaza = comboBoxPlaza.getSelectedItem().toString();

        HashMap<String, Object> reservaData = new HashMap<>();
        reservaData.put("id", rid);  // ID de la plaza
        reservaData.put("usuario", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());  // Nombre de usuario
        reservaData.put("intervaloHoras", horaInicio + "-" + horaFin);  // Intervalo de horas

        // Actualizar la reserva en la colección "Parking"
        db.collection("Parking").document(documentoFecha)
                .collection(tipoPlaza).document(idPlaza)
                .update("reservas", FieldValue.arrayUnion(reservaData))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Reserva agregada correctamente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error al agregar la reserva");
                    }
                });
    }

    private void comprobarDisponibilidad(String horaInicio, String horaFin, String fechaReserva, TipoEstacionamiento tipoPlaza, String plazaId, DisponibilidadCallback callback) {
        String documentoFecha = fechaReserva.replace("/", "-");

        // Obtener el intervalo de tiempo seleccionado
        int horaInicioSeleccionada = obtenerHora(horaInicio);
        int horaFinSeleccionada = obtenerHora(horaFin);

        // Consultar todas las reservas existentes para la plaza y la fecha seleccionadas
        db.collection("Parking").document(documentoFecha)
                .collection(String.valueOf(tipoPlaza))
                .document(plazaId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<Map<String, Object>> reservas = (List<Map<String, Object>>) documentSnapshot.get("reservas");
                            if (reservas != null) {
                                for (Map<String, Object> reserva : reservas) {
                                    String intervaloHoras = (String) reserva.get("intervaloHoras");
                                    String horaInicioReserva = intervaloHoras.split("-")[0];
                                    String horaFinReserva = intervaloHoras.split("-")[1];

                                    // Obtener las horas de inicio y fin de la reserva actual como enteros
                                    int horaInicioReservaInt = obtenerHora(horaInicioReserva);
                                    int horaFinReservaInt = obtenerHora(horaFinReserva);

                                    // Comprobar si el intervalo de la reserva actual se superpone con el intervalo seleccionado
                                    if ((horaInicioSeleccionada <= horaFinReservaInt && horaInicioSeleccionada >= horaInicioReservaInt) || (horaFinSeleccionada >= horaInicioReservaInt && horaInicioSeleccionada <= horaFinReservaInt)) {
                                        // Hay una reserva existente que se superpone con el intervalo seleccionado
                                        callback.onDisponibilidadChecked(false);
                                        return;
                                    }
                                }
                            }
                            callback.onDisponibilidadChecked(true);
                        } else {
                            callback.onDisponibilidadChecked(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error al comprobar la disponibilidad de la plaza", Toast.LENGTH_SHORT).show();
                        callback.onDisponibilidadChecked(false);
                    }
                });
    }

    private int obtenerHora(String hora) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = dateFormat.parse(hora);
        } catch (ParseException e) {
            e.printStackTrace();
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

    // Interfaz para el callback de los ids de las plazas obtenidos
    public interface OnIdsPlazasObtenidosListener {
        void onIdsPlazasObtenidos(ArrayList<String> idsPlazas);
    }

    public interface DisponibilidadCallback {
        void onDisponibilidadChecked(boolean disponible);
    }

}
