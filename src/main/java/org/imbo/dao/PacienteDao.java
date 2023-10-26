package org.imbo.dao;

import org.imbo.model.Paciente;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {
    public Connection conectar() {
        String baseDeDatos = "IMBO_DB"; // Reemplazar con el nombre de tu base de datos
        String usuario = "root"; // Reemplazar con el usuario de tu base de datos
        String password = "2301"; // Reemplazar con la contraseña de tu base de datos
        String host = "localhost"; // Reemplazar con el host de tu base de datos
        String puerto = "3306"; // Reemplazar con el puerto de tu base de datos
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

    public void registrarPaciente(Paciente paciente) {
        Connection conexion = conectar();
        try {
            String query = "INSERT INTO Paciente (nombre_paciente, telefono, correo, motivo, tratamiento) VALUES (?, ?, ?, ?, ?)";
            java.sql.PreparedStatement preparedStmt = conexion.prepareStatement(query);
            preparedStmt.setString(1, paciente.getNombre());
            preparedStmt.setString(2, paciente.getTelefono());
            preparedStmt.setString(3, paciente.getCorreo());
            preparedStmt.setString(4, paciente.getMotivo());
            preparedStmt.setString(5, paciente.getTratamiento());
            preparedStmt.execute();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Paciente> obtenerTodosPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        try{
            Connection conexion = conectar();

            String sql = "SELECT * FROM Paciente";
            PreparedStatement statement = conexion.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Paciente paciente = new Paciente();
                paciente.setId_paciente(resultSet.getInt("id_pac"));
                paciente.setNombre(resultSet.getString("nombre_paciente"));
                paciente.setTelefono(resultSet.getString("telefono"));
                paciente.setCorreo(resultSet.getString("correo"));
                paciente.setMotivo(resultSet.getString("motivo"));
                paciente.setTratamiento(resultSet.getString("tratamiento"));

                pacientes.add(paciente);
            }

            statement.close();
            conexion.close();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return pacientes;
    }

    public List<Paciente> obtenerPacientesPorFiltros(String nombre, String tratamiento) {
        List<Paciente> pacientes = new ArrayList<>();
        try{
            Connection conexion = conectar();

            String sql = "SELECT * FROM Paciente WHERE nombre_paciente LIKE ? AND tratamiento LIKE ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setString(1, "%" + nombre + "%");
            statement.setString(2, "%" + tratamiento + "%");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Paciente paciente = new Paciente();
                paciente.setId_paciente(resultSet.getInt("id_pac"));
                paciente.setNombre(resultSet.getString("nombre_paciente"));
                paciente.setTelefono(resultSet.getString("telefono"));
                paciente.setCorreo(resultSet.getString("correo"));
                paciente.setMotivo(resultSet.getString("motivo"));
                paciente.setTratamiento(resultSet.getString("tratamiento"));

                pacientes.add(paciente);
            }

            statement.close();
            conexion.close();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return pacientes;
    }

    public void eliminarPaciente(int idPaciente) {
        try{
            Connection conexion = conectar();

            String sql = "DELETE FROM Paciente WHERE id_pac = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, idPaciente);
            statement.executeUpdate();

            statement.close();
            conexion.close();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
