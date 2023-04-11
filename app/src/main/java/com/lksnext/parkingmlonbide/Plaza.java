package com.lksnext.parkingmlonbide;

public class Plaza {
    private String id;
    private Enum<TipoEstacionamiento> tipo;
    private Boolean reservado;

    public Plaza(String id, Enum<TipoEstacionamiento> tipo, Boolean reservado) {
        this.id = id;
        this.tipo = tipo;
        this.reservado = reservado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Enum<TipoEstacionamiento> getTipo() {
        return tipo;
    }

    public void setTipo(Enum<TipoEstacionamiento> tipo) {
        this.tipo = tipo;
    }

    public Boolean getReservado() {
        return reservado;
    }

    public void setReservado(Boolean reservado) {
        this.reservado = reservado;
    }

    @Override
    public String toString() {
        return "Plaza{" +
                "id='" + id + '\'' +
                ", tipo=" + tipo +
                ", reservado=" + reservado +
                '}';
    }
}
