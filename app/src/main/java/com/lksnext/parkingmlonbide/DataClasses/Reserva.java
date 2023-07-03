package com.lksnext.parkingmlonbide.DataClasses;


import java.util.Date;
import java.util.UUID;

public class Reserva {
    private long id;
    private String plazaId;
    private Date fechaReserva;
    private String horaInicio;
    private String horaFin;
    private TipoEstacionamiento tipoPlaza;

    public Reserva(String plazaId, Date fechaReserva, String horaInicio, String horaFin, TipoEstacionamiento tipoPlaza) {
        this.plazaId = plazaId;
        this.id = generateRandomId();
        this.fechaReserva = fechaReserva;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.tipoPlaza = tipoPlaza;
    }

    public Reserva(String plazaId,long rid, Date fechaReserva, String horaInicio, String horaFin, TipoEstacionamiento tipoPlaza) {
        this.plazaId = plazaId;
        this.id = rid;
        this.fechaReserva = fechaReserva;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.tipoPlaza = tipoPlaza;
    }


    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public TipoEstacionamiento getTipoPlaza() {
        return tipoPlaza;
    }

    public static long generateRandomId() {
        UUID uuid = UUID.randomUUID();
        long mostSignificantBits = uuid.getMostSignificantBits();
        return Math.abs(mostSignificantBits);
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", fechaReserva=" + fechaReserva +
                ", horaInicio='" + horaInicio + '\'' +
                ", horaFin='" + horaFin + '\'' +
                ", tipoPlaza=" + tipoPlaza +
                '}';
    }
}

