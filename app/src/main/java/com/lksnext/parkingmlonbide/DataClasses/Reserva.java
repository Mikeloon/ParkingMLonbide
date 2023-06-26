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

    public Reserva() {
    }

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


    public String getPlazaId() {
        return plazaId;
    }

    public void setPlazaId(String plazaId) {
        this.plazaId = plazaId;
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

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public TipoEstacionamiento getTipoPlaza() {
        return tipoPlaza;
    }

    public void setTipoPlaza(TipoEstacionamiento tipoPlaza) {
        this.tipoPlaza = tipoPlaza;
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

