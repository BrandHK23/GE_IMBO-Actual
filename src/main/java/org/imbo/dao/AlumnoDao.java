package org.imbo.dao;

import org.imbo.model.Registro;
import org.imbo.model.alumno.Alumno;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlumnoDao {
    private final String BASE_DE_DATOS = "IMBO_DB"; // Reemplazar con el nombre de tu base de datos
    private final String USUARIO = "root"; // Reemplazar con el usuario de tu base de datos
    private final String PASSWORD = "2301"; // Reemplazar con la contraseña de tu base de datos - CAMBIAR CONTRASEÑA A 1234
    private final String HOST = "localhost"; // Reemplazar con el host de tu base de datos
    private final String PUERTO = "3306"; // Reemplazar con el puerto de tu base de datos

    private final String URL_CONEXION = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DE_DATOS + "?useSSL=false";

    public Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL_CONEXION, USUARIO, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return conexion;
    }

    public void guardarAlumno(Alumno alumno) {
        try {
            Connection conexion = conectar();

            String sql = "INSERT INTO Alumno (nombre, correo, telefono, especialidad, fecha_inscripcion) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, alumno.getNombre());
            statement.setString(2, alumno.getCorreo());
            statement.setString(3, alumno.getTelefono());
            statement.setString(4, alumno.getEspecialidad());

            // Generar fecha de inscripción (utilizando la fecha actual)
            Calendar cal = Calendar.getInstance();
            java.sql.Date fechaInscripcion = new java.sql.Date(cal.getTimeInMillis());
            statement.setDate(5, fechaInscripcion);
            alumno.setFechaInscripcion(fechaInscripcion);

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int matriculaGenerada = generatedKeys.getInt(1);
                alumno.setMatricula(matriculaGenerada);
            }

            generatedKeys.close();
            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Alumno> obtenerTodosAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        try {
            Connection conexion = conectar();

            String sql = "SELECT * FROM Alumno";
            PreparedStatement statement = conexion.prepareStatement(sql);

            ResultSet resultado = statement.executeQuery();

            while (resultado.next()) {
                Alumno alumno = new Alumno();
                alumno.setMatricula(resultado.getInt("matricula"));
                alumno.setNombre(resultado.getString("nombre"));
                alumno.setEspecialidad(resultado.getString("especialidad"));
                alumno.setFechaInscripcion(resultado.getDate("fecha_inscripcion"));

                alumnos.add(alumno);
            }

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return alumnos;
    }

    public List<Alumno> obtenerAlumnosPorFiltro(String matricula, String nombre, String especialidad) {
        List<Alumno> alumnos = new ArrayList<>();
        try {
            Connection conexion = conectar();
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Alumno WHERE 1=1");

            //Verificar los filtros y agregar condiciones a la consulta
            if (!matricula.isEmpty()) {
                sqlBuilder.append(" AND matricula = ?");
            }

            if (!nombre.isEmpty()) {
                sqlBuilder.append(" AND nombre LIKE ?");
            }

            if (!especialidad.equals("Todos")) {
                sqlBuilder.append(" AND especialidad = ?");
            }

            String sql = sqlBuilder.toString();
            PreparedStatement statement = conexion.prepareStatement(sql);

            //Configurar los parametros de la consulta
            int paramIndex = 1;
            if (!matricula.isEmpty()) {
                statement.setInt(paramIndex, Integer.parseInt(matricula));
                paramIndex++;
            }

            if (!nombre.isEmpty()) {
                statement.setString(paramIndex, "%" + nombre + "%");
                paramIndex++;
            }

            if (!especialidad.equals("Todos")) {
                statement.setString(paramIndex, especialidad);
            }

            ResultSet resultado = statement.executeQuery();

            while (resultado.next()) {
                Alumno alumno = new Alumno();
                alumno.setMatricula(resultado.getInt("matricula"));
                alumno.setNombre(resultado.getString("nombre"));
                alumno.setEspecialidad(resultado.getString("especialidad"));

                alumnos.add(alumno);
            }

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return alumnos;
    }

    public Alumno ObtenerAlumnoPorMatricula(int matriculaAlumno) {
        Alumno alumno = null;
        try {
            Connection conexion = conectar();

            String sql = "SELECT * FROM Alumno WHERE matricula = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, matriculaAlumno);

            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                alumno = new Alumno();
                alumno.setMatricula(resultado.getInt("matricula"));
                alumno.setNombre(resultado.getString("nombre"));
                alumno.setCorreo(resultado.getString("correo"));
                alumno.setTelefono(resultado.getString("telefono"));
                alumno.setEspecialidad(resultado.getString("especialidad"));
                alumno.setFechaInscripcion(resultado.getDate("fecha_inscripcion"));
            }

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return alumno;
    }
    public boolean existeAlumnoPorMatricula(int matriculaAlumno) {
        try {
            Connection conexion = conectar();
            String sqlSelect = "SELECT COUNT(*) FROM Alumno WHERE matricula = ?";
            PreparedStatement statementSelect = conexion.prepareStatement(sqlSelect);
            statementSelect.setInt(1, matriculaAlumno);
            ResultSet resultSet = statementSelect.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            statementSelect.close();
            conexion.close();
            return count > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Alumno obtenerAlumnoPorMatricula(int matriculaAlumno) {
        // Get alumno from database
        Alumno alumno = null;
        try {
            Connection conexion = conectar();

            String sql = "SELECT * FROM Alumno WHERE matricula = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, matriculaAlumno);

            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                alumno = new Alumno();
                alumno.setMatricula(resultado.getInt("matricula"));
                alumno.setNombre(resultado.getString("nombre"));
                alumno.setCorreo(resultado.getString("correo"));
                alumno.setTelefono(resultado.getString("telefono"));
                alumno.setEspecialidad(resultado.getString("especialidad"));
                alumno.setFechaInscripcion(resultado.getDate("fecha_inscripcion"));
            }

            statement.close();
            conexion.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return alumno;
    }
}
