package org.imbo.dao;

import org.imbo.model.alumno.Alumno;
import org.imbo.model.alumno.PagoAlumno;
import org.imbo.view.inicioPanel.AlumnosPanel.PagoAlumnoPanel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagosAlDao {
    public Connection conectar() {
        String baseDeDatos = "IMBO_DB";
        String usuario = "root";
        String password = "1234";
        String host = "Localhost";
        String puerto = "3306";
        String drive = "com.mysql.cj.jdbc.Driver";
        String conexionUrl = "jdbc:mysql://" + host + ":" + puerto + "/" + baseDeDatos + "?useSSL=false";

        Connection conexion = null;
        try {
            Class.forName(drive);
            conexion = DriverManager.getConnection(conexionUrl, usuario, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return conexion;
    }
    public void insertarPago(Alumno alumno, PagoAlumno pagoAlumno) {
        try{
            Connection conexion = conectar();
            int matricula = alumno.getMatricula();

            String sql = "INSERT INTO PagosAl (matricula_alumno, tipo_pago, fecha_pago, forma_pago, cantidad, comentarios) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, matricula);
            statement.setString(2, pagoAlumno.getTipoPago());
            statement.setDate(3, new java.sql.Date(pagoAlumno.getFechaPago().getTime()));
            statement.setString(4, pagoAlumno.getFormaPago());
            statement.setInt(5, pagoAlumno.getCantidad());
            statement.setString(6, pagoAlumno.getComentario());

            statement.executeUpdate();

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //Obtener "tipo de pago", "cantidad", "Forma de pago" y "comentario" de la base de datos
    public List<PagoAlumno> obtenerPagosPorAlumno(int matricula) {
        List<PagoAlumno> pagos = new ArrayList<>();
        try {
            Connection conexion = conectar();

            String sql = "SELECT * FROM PagosAl WHERE matricula_alumno = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, matricula);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                PagoAlumno pagoAlumno = new PagoAlumno();
                pagoAlumno.setTipoPago(resultSet.getString("tipo_pago"));
                pagoAlumno.setCantidad(resultSet.getInt("cantidad"));
                pagoAlumno.setFormaPago(resultSet.getString("forma_pago"));
                pagoAlumno.setComentario(resultSet.getString("comentarios"));
                pagoAlumno.setFechaPago(resultSet.getDate("fecha_pago"));

                pagos.add(pagoAlumno);
            }

            resultSet.close();
            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pagos;
    }

}
