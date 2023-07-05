package com.lksnext.parkingmlonbide.DataClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plaza {
    private String id;
    private List<Map<String, String>> reservas;
    private boolean disponible;

    public Plaza(String id) {
        this.id = id;
        this.reservas = new ArrayList<>();
        this.disponible = true;
    }

    public String getId() {
        return id;
    }

    public void agregarReserva(String username, String horaInicio, String horaFin) {
        Map<String, String> reserva = new HashMap<>();
        reserva.put("username", username);
        reserva.put("intervaloHoras", horaInicio + "-" + horaFin);
        reservas.add(reserva);
        disponible = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n");
        sb.append("Reservas:\n");
        for (Map<String, String> reserva : reservas) {
            sb.append("  - Usuario: ").append(reserva.get("username"))
                    .append(", Intervalo Horas: ").append(reserva.get("intervaloHoras")).append("\n");
        }
        sb.append("Disponible: ").append(disponible).append("\n");
        return sb.toString();
    }

}

