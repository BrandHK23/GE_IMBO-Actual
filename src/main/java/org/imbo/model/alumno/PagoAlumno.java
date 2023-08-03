package org.imbo.model.alumno;

import java.sql.Date;

public class PagoAlumno {
    private int idPago;
    private int matricula_Alumno;
    private String tipoPago;
    private Date fechaPago;
    private String formaPago;
    private int cantidad;
    private String comentario;

    public PagoAlumno(int idPago, int matricula, String tipoPago, Date fechaPago, int cantidad, String formaPago, String comentario) {
        this.idPago = idPago;
        this.matricula_Alumno = matricula_Alumno;
        this.tipoPago = tipoPago;
        this.fechaPago = fechaPago;
        this.formaPago = formaPago;
        this.cantidad = cantidad;
        this.comentario = comentario;
    }

    public PagoAlumno() {

    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getMatricula_Alumno() {
        return matricula_Alumno;
    }

    public void setMatricula_Alumno(int matricula_Alumno) {
        this.matricula_Alumno = matricula_Alumno;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentarios) {
        this.comentario = comentarios;
    }
}
