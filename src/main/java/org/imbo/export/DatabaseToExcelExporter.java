package org.imbo.export;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.imbo.dao.RegistroDao;
import org.imbo.model.Registro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.Iterator;

public class DatabaseToExcelExporter {

    String excelFolder = System.getProperty("user.home") + File.separator + "Documents";
    String fileNameRegistros = "Registros.xlsx";
    String excelFilePath = excelFolder + File.separator + fileNameRegistros;

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
    public void exportExcelRegistros(String excelFilePath) {
        try (Connection conexion = conectar();
             Statement statement = conexion.createStatement();
             ResultSet result = statement.executeQuery("SELECT * FROM Registro")) {

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Datos");
            int rowNum = 0;

            ResultSetMetaData metaData = result.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Agregar fila de encabezado con los nombres de las columnas
            Row headerRow = sheet.createRow(rowNum++);
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                headerRow.createCell(i - 1).setCellValue(columnName);
            }

            // Agregar los datos de cada fila
            while (result.next()) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 1; i <= columnCount; i++) {
                    Object value = result.getObject(i);
                    row.createCell(i - 1).setCellValue(value != null ? value.toString() : "");
                }
            }

            // Ajustar el tamaño de las celdas al contenido
            for (int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
            }

            // Generar el nombre de archivo con versión
            int version = 1;
            String newFileName = fileNameRegistros;
            File archivo = new File(excelFolder + File.separator + newFileName);
            while (archivo.exists()) {
                // Generar nuevo nombre de archivo añadiendo la versión
                version++;
                newFileName = String.format("%s (%d).xlsx", fileNameRegistros.substring(0, fileNameRegistros.lastIndexOf('.')), version);
                archivo = new File(excelFolder + File.separator + newFileName);
            }

            // Crear el archivo en la ruta especificada
            try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
                workbook.write(outputStream);
            }

            System.out.println(newFileName + " exportado correctamente a Excel.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Importar datos de Excel a la base de datos
    public void importExcelRegistros(String excelFilePath) {
        try (Connection conexion = conectar()) {
            FileInputStream fileInputStream = new FileInputStream(excelFilePath);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    // Ignoramos la primera fila que contiene los encabezados
                    continue;
                }

                String nombreCompleto = getCellValueAsString(row.getCell(0));
                String telefono = getCellValueAsString(row.getCell(1));
                String email = getCellValueAsString(row.getCell(2));
                String ciudad = getCellValueAsString(row.getCell(3));
                String fuente = getCellValueAsString(row.getCell(4));
                String especialidad = getCellValueAsString(row.getCell(5));
                String status = getCellValueAsString(row.getCell(6));
                String comentarios = getCellValueAsString(row.getCell(7));
                boolean inscrito = row.getCell(8).getBooleanCellValue();

                Registro registro = new Registro();
                registro.setNombre(nombreCompleto);
                registro.setTelefono(telefono);
                registro.setEmail(email);
                registro.setCiudad(ciudad);
                registro.setFuente(fuente);
                registro.setEspecialidad(especialidad);
                registro.setStatus(status);
                registro.setComentarios(comentarios);
                registro.setFechaRegistro(new Date(new java.util.Date().getTime())); // Conversión de java.util.Date a java.sql.Date
                registro.setInscrito(inscrito);

                RegistroDao registroDao = new RegistroDao();
                registroDao.guardarRegistro(registro);
            }

            workbook.close();
            fileInputStream.close();

            System.out.println("Datos importados desde Excel a la base de datos correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Convert numeric value to string if needed
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
