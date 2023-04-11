package com.lksnext.parkingmlonbide;

import java.util.Date;

public class Reserva {
    private String idPlaza;
    private Date diaReserva;
    private Integer horasReserva;
    private Enum<TipoEstacionamiento> tipoEstacionamiento;

    public Reserva(String idPlaza, Date diaReserva, Integer horasReserva, Enum<TipoEstacionamiento> tipoEstacionamiento) {
        this.idPlaza = idPlaza;
        this.diaReserva = diaReserva;
        this.horasReserva = horasReserva;
        this.tipoEstacionamiento = tipoEstacionamiento;
    }

    public String getIdPlaza() {
        return idPlaza;
    }

    public void setIdPlaza(String idPlaza) {
        this.idPlaza = idPlaza;
    }

    public Date getDiaReserva() {
        return diaReserva;
    }

    public void setDiaReserva(Date diaReserva) {
        this.diaReserva = diaReserva;
    }

    public Integer getHorasReserva() {
        return horasReserva;
    }

    public void setHorasReserva(Integer horasReserva) {
        this.horasReserva = horasReserva;
    }

    public Enum<TipoEstacionamiento> getTipoEstacionamiento() {
        return tipoEstacionamiento;
    }

    public void setTipoEstacionamiento(Enum<TipoEstacionamiento> tipoEstacionamiento) {
        this.tipoEstacionamiento = tipoEstacionamiento;
    }
}
