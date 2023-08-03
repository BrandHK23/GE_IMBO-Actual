package org.imbo.view.inicioPanel.RegistroPanel;

import org.imbo.dao.AlumnoDao;
import org.imbo.dao.RegistroDao;
import org.imbo.model.alumno.Alumno;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class FormularioInscripcion extends JFrame {
    private RegistrosPanel registrosPanelInstance;

    private int idRegistroSeleccionado;
    private JTextField nombreCompletoField, telefonoField, correoField;
    private JComboBox<String> especialidadComboBox;
    private Map<String, JCheckBox> documentosCheckBoxes;
    private Alumno alumno;

    public FormularioInscripcion() {
        // Configuración del JFrame
        setTitle("Inscripción");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(650, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        // Crear los componentes del formulario
        JLabel nombreLabel = new JLabel("Nombre Completo:");
        nombreCompletoField = new JTextField(25);

        JLabel telefonoLabel = new JLabel("Teléfono:");
        telefonoField = new JTextField(15);

        JLabel correoLabel = new JLabel("Correo:");
        correoField = new JTextField(25);

        JLabel especialidadLabel = new JLabel("Especialidad:");
        String[] especialidades = {"Endodoncia", "Ortodoncia"};
        especialidadComboBox = new JComboBox<>(especialidades);

        JButton guardarButton = new JButton("Guardar");

        // Crear los componentes para los documentos
        documentosCheckBoxes = new HashMap<>();
        String[] nombresDocumentos = {
                "Comprobante de pago de inscripción",
                "Título o constancia de titulación",
                "Cédula profesional",
                "Acta de nacimiento",
                "Curp",
                "Fotografías (En físico)",
                "Identificación oficial",
                "Comprobante de domicilio",
                "Carta de intención"
        };

        for (String documento : nombresDocumentos) {
            JCheckBox checkBox = new JCheckBox(documento);
            documentosCheckBoxes.put(documento, checkBox);
        }

        // Panel para los datos personales
        JPanel datosPanel = new JPanel();
        datosPanel.setLayout(new GridBagLayout());
        datosPanel.setBorder(BorderFactory.createTitledBorder("Datos")); // Margen con el nombre del panel
        GridBagConstraints gbcDatos = new GridBagConstraints();
        gbcDatos.gridx = 0;
        gbcDatos.gridy = 0;
        gbcDatos.anchor = GridBagConstraints.WEST;
        gbcDatos.insets = new Insets(5, 10, 5, 10); // Espaciado entre los componentes

        // Agregar los componentes de datos personales al panel
        datosPanel.add(nombreLabel, gbcDatos);
        gbcDatos.gridy = 1;
        datosPanel.add(nombreCompletoField, gbcDatos);
        gbcDatos.gridy = 2;
        datosPanel.add(telefonoLabel, gbcDatos);
        gbcDatos.gridy = 3;
        datosPanel.add(telefonoField, gbcDatos);
        gbcDatos.gridy = 4;
        datosPanel.add(correoLabel, gbcDatos);
        gbcDatos.gridy = 5;
        datosPanel.add(correoField, gbcDatos);
        gbcDatos.gridy = 6;
        datosPanel.add(especialidadLabel, gbcDatos);
        gbcDatos.gridy = 7;
        datosPanel.add(especialidadComboBox, gbcDatos);

        // Panel para los documentos
        JPanel documentosPanel = new JPanel();
        documentosPanel.setLayout(new BoxLayout(documentosPanel, BoxLayout.Y_AXIS));
        documentosPanel.setBorder(BorderFactory.createTitledBorder("Documentos")); // Margen con el nombre del panel

        for (JCheckBox checkBox : documentosCheckBoxes.values()) {
            documentosPanel.add(checkBox);
        }

        // Agregar los paneles al JFrame
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre los paneles
        add(datosPanel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(documentosPanel, gbc);

        // Botón "Guardar" en la parte inferior del formulario
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        RegistrosPanel registrosPanel = new RegistrosPanel();

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // LLamar al metodo que guarda la opcion seleccionada
                RegistroDao registroDao = new RegistroDao();
                boolean valor = registrosPanelInstance.getIsRegistroSeleccionado();
                registrosPanel.getIsRegistroSeleccionado();
                if ((valor) == true){
                    JOptionPane.showMessageDialog(null, "Registro Alumno seleccionado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    int idR = registrosPanelInstance.getIdRegistroSeleccionado();
                    int idRegistro = idR;

                    // Actualizar el status del registro a "Inscrito"
                    registroDao.actualizarStatusEInscrito(idRegistro);

                    // Guardar los datos del formulario
                    guardarDatos();
                }else if ((valor) == false){
                    JOptionPane.showMessageDialog(null, "Registro Alumno no seleccionado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    // Guardar los datos del formulario
                    guardarDatos();
                    JOptionPane.showMessageDialog(null, "Alumno guardado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }

                // Cerrar la ventana
                Window window = SwingUtilities.getWindowAncestor(guardarButton); // Obtener el contenedor de la ventana
                if (window instanceof JFrame) {
                    JFrame frame = (JFrame) window;
                    frame.dispose(); // Cerrar la ventana
                }
            }
        });
        add(guardarButton, gbc);
    }
    private void guardarDatos() {
        String nombreCompleto = nombreCompletoField.getText();
        String telefono = telefonoField.getText();
        String correo = correoField.getText();
        String especialidad = (String) especialidadComboBox.getSelectedItem();

        //Crear objeto de tipo alumno
        Alumno alumno = new Alumno(nombreCompleto, correo, telefono, especialidad);
        alumno.setNombre(nombreCompleto);
        alumno.setCorreo(correo);
        alumno.setTelefono(telefono);
        alumno.setEspecialidad(especialidad);

        //Actualizar los datos del alumno en la base de datos
        AlumnoDao alumnoDao = new AlumnoDao();
        alumnoDao.guardarAlumno(alumno);

        //Mostrar mensaje de éxito
        JOptionPane.showMessageDialog(null, "Alumno guardado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setNombreCompleto(String nombreCompleto) {
        nombreCompletoField.setText(nombreCompleto);
    }

    public void setTelefono(String telefono) {
        telefonoField.setText(telefono);
    }

    public void setCorreo(String correo) {
        correoField.setText(correo);
    }

    public void setEspecialidad(String especialidad) {
        especialidadComboBox.setSelectedItem(especialidad);
    }
    // Método para establecer la instancia de RegistrosPanel
    public void setRegistrosPanelInstance(RegistrosPanel registrosPanel) {
        this.registrosPanelInstance = registrosPanel;
    }
}
