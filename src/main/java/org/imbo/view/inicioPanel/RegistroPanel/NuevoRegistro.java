package org.imbo.view.inicioPanel.RegistroPanel;

import org.imbo.dao.RegistroDao;
import org.imbo.model.Registro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class NuevoRegistro extends JFrame {

    private Registro registro;
    private RegistrosPanel registrosPanel = new RegistrosPanel();

    // Campos de texto y etiquetas
    JLabel nombreLabel = new JLabel("Nombre:");
    JTextField nombreTextField = new JTextField(20);
    JLabel telefonoLabel = new JLabel("Teléfono:");
    JTextField telefonoTextField = new JTextField(20);
    JLabel emailLabel = new JLabel("Email:");
    JTextField correoTextField = new JTextField(20);
    JLabel ciudadLabel = new JLabel("Ciudad:");
    JTextField ciudadTextField = new JTextField(20);
    JLabel fuenteLabel = new JLabel("Fuente:");
    String[] fuenteOptions = {"Facebook", "WhatsApp", "Otro"};
    JComboBox<String> fuenteComboBox = new JComboBox<>(fuenteOptions);
    JLabel especialidadLabel = new JLabel("Especialidad:");
    String[] especialidadOptions = {"Endodoncia", "Ortodoncia", "Ambas"};
    JComboBox<String> especialidadComboBox = new JComboBox<>(especialidadOptions);
    JLabel statusLabel = new JLabel("Status:");
    String[] statusOptions = {"Etapa 1", "Etapa 2", "Etapa 3", "Etapa 4"};
    JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
    JLabel comentariosLabel = new JLabel("Comentarios:");
    JTextArea comentariosTextArea = new JTextArea(5, 20);
    JScrollPane comentariosScrollPane = new JScrollPane(comentariosTextArea);
    public NuevoRegistro() {
        setTitle("Nuevo Registro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Panel contenedor con borde y margen
        JPanel containerPanel = new JPanel();
        containerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        containerPanel.setLayout(new BorderLayout());

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);



        // Botón de guardar
        JButton guardarButton = new JButton("Guardar");

        // Agregar componentes al panel principal
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(nombreLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(nombreTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(telefonoLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(telefonoTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(correoTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(ciudadLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(ciudadTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(fuenteLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(fuenteComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(especialidadLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(especialidadComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(statusLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(statusComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(comentariosLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(comentariosScrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(guardarButton, gbc);

        // Espacios en blanco para estética
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        mainPanel.add(new JLabel(""), gbc);

        // Agregar panel principal al panel contenedor
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        add(containerPanel);
        pack();

        //Configure save button action
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreTextField.getText();
                String telefono = telefonoTextField.getText();
                String email = correoTextField.getText();
                String ciudad = ciudadTextField.getText();
                String fuente = (String) fuenteComboBox.getSelectedItem();
                String especialidad = (String) especialidadComboBox.getSelectedItem();
                String status = (String) statusComboBox.getSelectedItem();
                String comentarios = comentariosTextArea.getText();

                // Verificar que los campos no estén vacíos
                if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || ciudad.isEmpty() || comentarios.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos.");
                } else {
                    if (registro != null) {
                        // Establecer los nuevos valores en el registro existente
                        registro.setNombre(nombre);
                        registro.setTelefono(telefono);
                        registro.setEmail(email);
                        registro.setCiudad(ciudad);
                        registro.setFuente(fuente);
                        registro.setEspecialidad(especialidad);
                        registro.setStatus(status);
                        registro.setComentarios(comentarios);

                        // Actualizar el registro en la base de datos
                        RegistroDao registroDAO = new RegistroDao();
                        registroDAO.actualizarRegistro(registro);

                        // Mostar mensaje de éxito
                        JOptionPane.showMessageDialog(null, "Registro actualizado con éxito.");
                    } else {
                        // Crear objeto Registro y establecer los valores
                        Registro nuevoRegistro = new Registro();
                        nuevoRegistro.setNombre(nombre);
                        nuevoRegistro.setTelefono(telefono);
                        nuevoRegistro.setEmail(email);
                        nuevoRegistro.setCiudad(ciudad);
                        nuevoRegistro.setFuente(fuente);
                        nuevoRegistro.setEspecialidad(especialidad);
                        nuevoRegistro.setStatus(status);
                        nuevoRegistro.setComentarios(comentarios);

                        //Establecer la fecha de registro
                        nuevoRegistro.setFechaRegistro(new java.sql.Date(new Date().getTime()));

                        // Guardar el registro en la base de datos
                        RegistroDao registroDAO = new RegistroDao();
                        registroDAO.guardarRegistro(nuevoRegistro);

                        //Mostar mensaje de éxito
                        JOptionPane.showMessageDialog(null, "Registro guardado con éxito.");
                    }

                    // Cerrar la ventana
                    Window window = SwingUtilities.getWindowAncestor(guardarButton); // Obtener el contenedor de la ventana
                    if (window instanceof JFrame) {
                        JFrame frame = (JFrame) window;
                        frame.dispose(); // Cerrar la ventana
                    }
                }
            }
        });
    }
    // Constructor para edición de registro
    public NuevoRegistro(Registro registro) {
        this(); // Llamar al constructor predeterminado para configurar la ventana
        this.registro = registro;

        // Establecer los valores de los campos según el registro proporcionado
        nombreTextField.setText(registro.getNombre());
        telefonoTextField.setText(registro.getTelefono());
        correoTextField.setText(registro.getEmail());
        ciudadTextField.setText(registro.getCiudad());
        fuenteComboBox.setSelectedItem(registro.getFuente());
        especialidadComboBox.setSelectedItem(registro.getEspecialidad());
        statusComboBox.setSelectedItem(registro.getStatus());
        comentariosTextArea.setText(registro.getComentarios());
    }
    public Container getNuevoRegistroPanel() {
        return getContentPane();
    }
}
