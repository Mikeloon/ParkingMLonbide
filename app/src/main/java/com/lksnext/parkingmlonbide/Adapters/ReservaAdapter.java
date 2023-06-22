package com.lksnext.parkingmlonbide.Adapters;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingmlonbide.DataClasses.Reserva;
import com.lksnext.parkingmlonbide.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder> {
    private Fragment fragment;

    private List<Reserva> reservas;
    private SimpleDateFormat formato;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public ReservaAdapter(List<Reserva> reservas, SimpleDateFormat formato) {
        this.reservas = reservas;
        this.formato = formato;
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

        // Mostrar los datos de la reserva en la fila
        holder.textViewFecha.setText(formato.format(reserva.getFechaReserva()));
        holder.textViewHoras.setText(reserva.getHorasReserva() + " horas");
        holder.textViewTipoPlaza.setText(reserva.getTipoPlaza().toString());

        // Manejar el evento de clic del botón "Cancelar"
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
                                        // Actualización exitosa
                                        Log.d(TAG, "La reserva se ha eliminado correctamente");
                                        notifyDataSetChanged();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error al actualizar el campo
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
