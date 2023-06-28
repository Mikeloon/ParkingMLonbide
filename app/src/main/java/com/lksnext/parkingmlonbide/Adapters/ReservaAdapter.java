package com.lksnext.parkingmlonbide.Adapters;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingmlonbide.DataClasses.Reserva;
import com.lksnext.parkingmlonbide.NavFragments.ProfileFragment;
import com.lksnext.parkingmlonbide.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder> {
    private View view;
    private FragmentManager fragmentManager;

    private List<Reserva> reservas;
    private SimpleDateFormat formato;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public ReservaAdapter(List<Reserva> reservas, SimpleDateFormat formato, View view, FragmentManager fragmentManager) {
        this.reservas = reservas;
        this.formato = formato;
        this.view = view;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reserva, parent, false);
        return new ReservaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        Reserva reserva = reservas.get(position);
        String horaInicio = reserva.getHoraInicio();
        String horaFin = reserva.getHoraFin();

        Date documentFecha = reserva.getFechaReserva();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String documentoFecha = dateFormat.format(documentFecha);

        String tipoPlaza = String.valueOf(reserva.getTipoPlaza());
        String plazaId = reserva.getPlazaId();
        holder.textViewFecha.setText(formato.format(reserva.getFechaReserva()));
        holder.textViewHoras.setText(horaInicio + " - " + horaFin);
        holder.textViewTipoPlaza.setText(reserva.getTipoPlaza().toString());

        holder.buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = mAuth.getCurrentUser().getUid();
                long rid = reserva.getId();
                DocumentReference userRef = db.collection("users").document(uid);

                userRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Obtener el array de reservas del documento
                            List<Map<String, Object>> reservas = (List<Map<String, Object>>) document.get("reservas");
                            // Recorrer el array de reservas y eliminar el elemento con el id deseado
                            for (int i = 0; i < reservas.size(); i++) {
                                Map<String, Object> reserva = reservas.get(i);
                                long id = (long) reserva.get("id");

                                if (id == rid) {
                                    // Eliminar el elemento del array
                                    reservas.remove(i);
                                    break; // Terminar el bucle si se encontró el elemento con el id deseado
                                }
                            }

                            // Actualizar el campo 'reservas' en el documento
                            userRef.update("reservas", reservas)
                                    .addOnSuccessListener(aVoid -> {

                                        DocumentReference plazaRef = db.collection("Parking").document(documentoFecha)
                                                .collection(tipoPlaza).document(plazaId);

                                        plazaRef.get().addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                DocumentSnapshot plazaDocument = task1.getResult();
                                                if (plazaDocument.exists()) {
                                                    List<Map<String, Object>> plazaReservas = (List<Map<String, Object>>) plazaDocument.get("reservas");
                                                    for (int i = 0; i < plazaReservas.size(); i++) {
                                                        Map<String, Object> reserva = plazaReservas.get(i);
                                                        long id = (long) reserva.get("id");

                                                        if (id == rid) {
                                                            // Eliminar el elemento del array
                                                            plazaReservas.remove(i);
                                                            break; // Terminar el bucle si se encontró el elemento con el id deseado
                                                        }
                                                    }

                                                    // Actualizar el campo 'reservas' en el documento del plazaId
                                                    plazaRef.update("reservas", plazaReservas)
                                                            .addOnSuccessListener(aVoid1 -> {
                                                                // Actualización exitosa
                                                                Toast.makeText(view.getContext(), "Reserva eliminada correctamente", Toast.LENGTH_SHORT).show();
                                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                                Fragment profileFragment = new ProfileFragment();
                                                                fragmentTransaction.replace(R.id.profileFragment, profileFragment);
                                                                fragmentTransaction.commit();
                                                            })
                                                            .addOnFailureListener(e1 -> {
                                                                Log.e(TAG, "Error al actualizar el campo 'reservas' en el documento del plazaId", e1);
                                                            });
                                                } else {
                                                    Log.e(TAG, "El documento del plazaId no existe.");
                                                }
                                            } else {
                                                Log.e(TAG, "Error al obtener el documento del plazaId: " + task1.getException().getMessage());
                                            }
                                        });
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.d(TAG, "No se ha actualizado correctamente");
                                    });
                        } else {
                            // El documento no existe
                            System.err.println("El documento del usuario no existe.");
                        }
                    } else {
                        // Error al obtener el documento
                        System.err.println("Error al obtener el documento del usuario: " + task.getException().getMessage());
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    public class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFecha;
        TextView textViewHoras;
        TextView textViewTipoPlaza;
        Button buttonCancelar;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
            textViewHoras = itemView.findViewById(R.id.textViewHoras);
            textViewTipoPlaza = itemView.findViewById(R.id.textViewTipoPlaza);
            buttonCancelar = itemView.findViewById(R.id.buttonCancelar);
        }
    }
}
