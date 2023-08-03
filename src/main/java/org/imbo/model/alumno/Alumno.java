package org.imbo.model.alumno;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Alumno {
    private int matricula;
    private String nombre;
    private String correo;
    private String telefono;
    private String especialidad;
    private Date fechaInscripcion;
    private List<String> rutasDocumentos;

    public Alumno(String nombreCompleto, String correo, String telefono, String especialidad) {
    }

    public Alumno() {

    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }


    public void setRutasDocumentos(List<String> rutasDocumentos) {
            this.rutasDocumentos = rutasDocumentos;
    }
    public void agregarRutaDocumento(String rutaDocumento) {
        rutasDocumentos.add(rutaDocumento);
    }

    public List<String> getRutasDocumentos() {
        return rutasDocumentos;
    }
}
