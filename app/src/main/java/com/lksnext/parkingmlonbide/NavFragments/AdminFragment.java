package com.lksnext.parkingmlonbide.NavFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingmlonbide.R;

import java.util.HashMap;
import java.util.Map;

public class AdminFragment extends Fragment {

    private TextView numCocheNormalTextView, numCocheElecTextView, numMinusvTextView, numMotoTextView;
    private ImageView restaNormalImageView, sumaNormalImageView, restaCocheElecImageView, sumaCocheElecImageView,
            restaMinusvImageView, sumaMinusvImageView, restaMotoImageView, sumaMotoImageView;
    private Button guardarButton, restablecerButton;

    private int numCocheNormal, numCocheElec, numMinusv, numMoto;

    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        numCocheNormalTextView = view.findViewById(R.id.NumCocheNormal);
        numCocheElecTextView = view.findViewById(R.id.NumCocheElec);
        numMinusvTextView = view.findViewById(R.id.NumMinusv);
        numMotoTextView = view.findViewById(R.id.NumMoto);

        restaNormalImageView = view.findViewById(R.id.btnRestaNormal);
        sumaNormalImageView = view.findViewById(R.id.btonSumaNormal);
        restaCocheElecImageView = view.findViewById(R.id.btnRestaCocheElec);
        sumaCocheElecImageView = view.findViewById(R.id.btonSumaElec);
        restaMinusvImageView = view.findViewById(R.id.btnRestaMinusv);
        sumaMinusvImageView = view.findViewById(R.id.btonSumaMinusv);
        restaMotoImageView = view.findViewById(R.id.btnRestaMoto);
        sumaMotoImageView = view.findViewById(R.id.btonSumaMoto);

        guardarButton = view.findViewById(R.id.guardarButton);
        restablecerButton = view.findViewById(R.id.restablecerButton);

        db = FirebaseFirestore.getInstance();

        obtenerNumeroPlazas();

        configurarBotonesRestaSuma();

        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });

        restablecerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restablecerValores();
            }
        });

        return view;
    }

    private void obtenerNumeroPlazas() {
        db.collection("Parking").document("Coche").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Long numPlazas = document.getLong("NumPlazas");
                                if (numPlazas != null) {
                                    numCocheNormal = numPlazas.intValue();
                                    numCocheNormalTextView.setText(String.valueOf(numCocheNormal));
                                }
                            } else {
                                // El documento no existe
                            }
                        } else {
                            // Error al obtener el documento
                        }
                    }
                });

        db.collection("Parking").document("Electrico").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Long numPlazas = document.getLong("NumPlazas");
                                if (numPlazas != null) {
                                    numCocheElec = numPlazas.intValue();
                                    numCocheElecTextView.setText(String.valueOf(numCocheElec));
                                }
                            } else {
                                // El documento no existe
                            }
                        } else {
                            // Error al obtener el documento
                        }
                    }
                });

        db.collection("Parking").document("Minusvalido").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Long numPlazas = document.getLong("NumPlazas");
                                if (numPlazas != null) {
                                    numMinusv = numPlazas.intValue();
                                    numMinusvTextView.setText(String.valueOf(numMinusv));
                                }
                            } else {
                                // El documento no existe
                            }
                        } else {
                            // Error al obtener el documento
                        }
                    }
                });

        db.collection("Parking").document("Moto").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Long numPlazas = document.getLong("NumPlazas");
                                if (numPlazas != null) {
                                    numMoto = numPlazas.intValue();
                                    numMotoTextView.setText(String.valueOf(numMoto));
                                }
                            } else {
                                // El documento no existe
                            }
                        } else {
                            // Error al obtener el documento
                        }
                    }
                });
    }

    private void configurarBotonesRestaSuma() {
        restaNormalImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numCocheNormal > 0) {
                    numCocheNormal--;
                    numCocheNormalTextView.setText(String.valueOf(numCocheNormal));
                }
            }
        });

        sumaNormalImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numCocheNormal++;
                numCocheNormalTextView.setText(String.valueOf(numCocheNormal));
            }
        });

        restaCocheElecImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numCocheElec > 0) {
                    numCocheElec--;
                    numCocheElecTextView.setText(String.valueOf(numCocheElec));
                }
            }
        });

        sumaCocheElecImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numCocheElec++;
                numCocheElecTextView.setText(String.valueOf(numCocheElec));
            }
        });

        restaMinusvImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numMinusv > 0) {
                    numMinusv--;
                    numMinusvTextView.setText(String.valueOf(numMinusv));
                }
            }
        });

        sumaMinusvImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numMinusv++;
                numMinusvTextView.setText(String.valueOf(numMinusv));
            }
        });

        restaMotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numMoto > 0) {
                    numMoto--;
                    numMotoTextView.setText(String.valueOf(numMoto));
                }
            }
        });

        sumaMotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numMoto++;
                numMotoTextView.setText(String.valueOf(numMoto));
            }
        });
    }

    private void guardarCambios() {
        Map<String, Object> cocheData = new HashMap<>();
        cocheData.put("NumPlazas", numCocheNormal);

        Map<String, Object> cocheElecData = new HashMap<>();
        cocheElecData.put("NumPlazas", numCocheElec);

        Map<String, Object> minusvData = new HashMap<>();
        minusvData.put("NumPlazas", numMinusv);

        Map<String, Object> motoData = new HashMap<>();
        motoData.put("NumPlazas", numMoto);

        db.collection("Parking").document("Coche").set(cocheData);
        db.collection("Parking").document("Electrico").set(cocheElecData);
        db.collection("Parking").document("Minusvalido").set(minusvData);
        db.collection("Parking").document("Moto").set(motoData);

        Toast.makeText(getActivity(), "Cambios guardados", Toast.LENGTH_SHORT).show();
    }

    private void restablecerValores() {
        numCocheNormal = 0;
        numCocheElec = 0;
        numMinusv = 0;
        numMoto = 0;

        numCocheNormalTextView.setText(String.valueOf(numCocheNormal));
        numCocheElecTextView.setText(String.valueOf(numCocheElec));
        numMinusvTextView.setText(String.valueOf(numMinusv));
        numMotoTextView.setText(String.valueOf(numMoto));

        guardarCambios();
    }
}
