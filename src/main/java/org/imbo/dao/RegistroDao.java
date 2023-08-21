package org.imbo.dao;

import org.imbo.model.Registro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistroDao {
    public Connection conectar() {
        String baseDeDatos = "IMBO_DB"; 
        String usuario = "root"; 
        String password = "2301";
        String host = "localhost"; 
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

    public void guardarRegistro(Registro registro) {
        try {
            Connection conexion = conectar();

            String sql = "INSERT INTO Registro (nombre_completo, telefono, email, ciudad, fuente, especialidad, status, comentarios, fecha_registro, Inscrito) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, registro.getNombre());
            statement.setString(2, registro.getTelefono());
            statement.setString(3, registro.getEmail());
            statement.setString(4, registro.getCiudad());
            statement.setString(5, registro.getFuente());
            statement.setString(6, registro.getEspecialidad());
            statement.setString(7, registro.getStatus());
            statement.setString(8, registro.getComentarios());
            statement.setDate(9, new java.sql.Date(registro.getFechaRegistro().getTime()));
            statement.setBoolean(10, registro.isInscrito());

            statement.executeUpdate();

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Consultar todos los registros
        public List<Registro> obtenerTodosRegistros() {
        List<Registro> registros = new ArrayList<>();
        try {
            Connection conexion = conectar();

            String sql = "SELECT * FROM Registro";
            PreparedStatement statement = conexion.prepareStatement(sql);

            ResultSet resultado = statement.executeQuery();

            while (resultado.next()) {
                Registro registro = new Registro();
                registro.setId_usuario(resultado.getInt("id_usuario"));
                registro.setNombre(resultado.getString("nombre_completo"));
                registro.setTelefono(resultado.getString("telefono"));
                registro.setEmail(resultado.getString("email"));
                registro.setCiudad(resultado.getString("ciudad"));
                registro.setFuente(resultado.getString("fuente"));
                registro.setEspecialidad(resultado.getString("especialidad"));
                registro.setStatus(resultado.getString("status"));
                registro.setComentarios(resultado.getString("comentarios"));
                registro.setFechaRegistro(resultado.getDate("fecha_registro"));

                registros.add(registro);
            }

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return registros;
    }

    public List<Registro> buscarRegistrosConFiltro(String especialidad, String status, String nombre) {
        List<Registro> registros = new ArrayList<>();

        try {
            Connection conexion = conectar();
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Registro WHERE 1=1");

            // Verificar los filtros y agregar condiciones a la consulta
            if (!especialidad.equals("Todo")) {
                sqlBuilder.append(" AND especialidad = ?");
            }

            if (!status.equals("Todo")) {
                sqlBuilder.append(" AND status = ?");
            }

            if (!nombre.isEmpty()) {
                sqlBuilder.append(" AND nombre_completo LIKE ?");
            }

            String sql = sqlBuilder.toString();
            PreparedStatement statement = conexion.prepareStatement(sql);

            // Configurar los parámetros de la consulta
            int paramIndex = 1;
            if (!especialidad.equals("Todo")) {
                statement.setString(paramIndex++, especialidad);
            }

            if (!status.equals("Todo")) {
                statement.setString(paramIndex++, status);
            }

            if (!nombre.isEmpty()) {
                statement.setString(paramIndex++, "%" + nombre + "%");
            }

            ResultSet resultado = statement.executeQuery();

            while (resultado.next()) {
                Registro registro = new Registro();
                registro.setId_usuario(resultado.getInt("id_usuario"));
                registro.setNombre(resultado.getString("nombre_completo"));
                registro.setTelefono(resultado.getString("telefono"));
                registro.setEmail(resultado.getString("email"));
                registro.setCiudad(resultado.getString("ciudad"));
                registro.setFuente(resultado.getString("fuente"));
                registro.setEspecialidad(resultado.getString("especialidad"));
                registro.setStatus(resultado.getString("status"));
                registro.setComentarios(resultado.getString("comentarios"));
                registro.setFechaRegistro(resultado.getDate("fecha_registro"));
                registro.setInscrito(resultado.getBoolean("Inscrito"));

                registros.add(registro);
            }

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return registros;
    }

    public String obtenerComentarioPorId(int idRegistro) {
        String comentario = null;
        try {
            Connection conexion = conectar();

            String sql = "SELECT comentarios FROM Registro WHERE id_usuario = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, idRegistro);

            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                comentario = resultado.getString("comentarios");
            }

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return comentario;
    }

    public void actualizarComentario(int idRegistro, String nuevoComentario) {
        try {
            Connection conexion = conectar();

            String sql = "UPDATE Registro SET comentarios = ? WHERE id_usuario = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setString(1, nuevoComentario);
            statement.setInt(2, idRegistro);

            statement.executeUpdate();

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Registro obtenerRegistroPorId(int idRegistro) {
        //Get the record by id
        Registro registro = null;
    try {
            Connection conexion = conectar();

            String sql = "SELECT * FROM Registro WHERE id_usuario = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, idRegistro);

            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                registro = new Registro();
                registro.setId_usuario(resultado.getInt("id_usuario"));
                registro.setNombre(resultado.getString("nombre_completo"));
                registro.setTelefono(resultado.getString("telefono"));
                registro.setEmail(resultado.getString("email"));
                registro.setCiudad(resultado.getString("ciudad"));
                registro.setFuente(resultado.getString("fuente"));
                registro.setEspecialidad(resultado.getString("especialidad"));
                registro.setStatus(resultado.getString("status"));
                registro.setComentarios(resultado.getString("comentarios"));
                registro.setFechaRegistro(resultado.getDate("fecha_registro"));
                registro.setInscrito(resultado.getBoolean("Inscrito"));
            }

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return registro;
    }

    public void actualizarRegistro(Registro registro) {
        try {
            Connection conexion = conectar();

            String sql = "UPDATE Registro SET nombre_completo = ?, telefono = ?, email = ?, ciudad = ?, fuente = ?, especialidad = ?, status = ?, comentarios = ?, fecha_registro = ?, Inscrito = ? WHERE id_usuario = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setString(1, registro.getNombre());
            statement.setString(2, registro.getTelefono());
            statement.setString(3, registro.getEmail());
            statement.setString(4, registro.getCiudad());
            statement.setString(5, registro.getFuente());
            statement.setString(6, registro.getEspecialidad());
            statement.setString(7, registro.getStatus());
            statement.setString(8, registro.getComentarios());
            statement.setDate(9, new java.sql.Date(registro.getFechaRegistro().getTime()));
            statement.setBoolean(10, registro.isInscrito());
            statement.setInt(11, registro.getId_usuario());

            statement.executeUpdate();

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void eliminarRegistro(int idRegistro) {
        try {
            Connection conexion = conectar();

            String sql = "DELETE FROM Registro WHERE id_usuario = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, idRegistro);

            statement.executeUpdate();

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public int obtenerTotalRegistros() {
        int totalRegistros = 0;
        try {
            Connection conexion = conectar();

            String sql = "SELECT COUNT(*) AS total FROM Registro";
            PreparedStatement statement = conexion.prepareStatement(sql);

            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                totalRegistros = resultado.getInt("total");
            }

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return totalRegistros;
    }
    public void actualizarStatusEInscrito(int idRegistro) {
        try {
            Connection conexion = conectar();

            String sql = "UPDATE Registro SET status = ?, Inscrito = ? WHERE id_usuario = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setString(1, "Etapa 4");
            statement.setBoolean(2, true);
            statement.setInt(3, idRegistro);

            statement.executeUpdate();

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
