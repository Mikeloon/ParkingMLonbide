package com.lksnext.parkingmlonbide.DataClasses;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.lksnext.parkingmlonbide.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder> {
    private Fragment fragment;

    private List<Reserva> reservas;
    private SimpleDateFormat formato;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


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
                int rid = reserva.getId();
                DocumentReference userRef = db.collection("users").document(uid);
                db.runTransaction(new Transaction.Function<Void>() {
                            @Override
                            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                DocumentSnapshot userSnapshot = transaction.get(userRef);
                                if (userSnapshot.exists()) {
                                    List<Reserva> reservas = new ArrayList<>();
                                    List<Map<String, Object>> reservasList = (List<Map<String, Object>>) userSnapshot.get("reservas");
                                    for (Iterator<Map<String, Object>> iterator = reservasList.iterator(); iterator.hasNext();) {
                                        Map<String, Object> reserva = iterator.next();
                                        if (reserva.get("id").equals(rid)) {
                                            iterator.remove();
                                            break;
                                        }
                                    }
                                    transaction.update(userRef, "reservas", reservasList);
                                }
                                return null;
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Reserva borrada correctamente");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "El documento no existe");
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

