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
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingmlonbide.DataClasses.Reserva;
import com.lksnext.parkingmlonbide.NavFragments.ProfileFragment;
import com.lksnext.parkingmlonbide.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        holder.textViewFecha.setText(formato.format(reserva.getFechaReserva()) + " ");
        holder.textViewHoras.setText(horaInicio + " -" + horaFin);
        holder.textViewTipoPlaza.setText(reserva.getTipoPlaza().toString());

        holder.buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = mAuth.getCurrentUser().getUid();
                long rid = reserva.getId();

                DocumentReference ReservaAEliminar = db.collection("Parking").document(String.valueOf(rid));

                ReservaAEliminar.delete()
                        .addOnSuccessListener(aVoid -> {
                            // El documento se eliminó exitosamente
                            Toast.makeText(view.getContext(), "Reserva eliminada correctamente", Toast.LENGTH_SHORT).show();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Fragment profileFragment = new ProfileFragment();
                            fragmentTransaction.replace(R.id.profileFragment, profileFragment);
                            fragmentTransaction.commit();
                        })
                        .addOnFailureListener(e -> {
                            // Ocurrió un error al eliminar el documento
                            Log.e(TAG, "Error al eliminar el documento", e);
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
