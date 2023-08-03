package org.imbo.view.inicioPanel.RegistroPanel;

import org.imbo.dao.RegistroDao;
import org.imbo.export.DatabaseToExcelExporter;
import org.imbo.model.Registro;
import org.imbo.view.inicioPanel.StatusRowRenderer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class RegistrosPanel extends JPanel {
    private RegistroDao registroDao;
    private boolean isRegistroSeleccionado = false;
    //Comentario
    private JTable table;
    private String comentarioModificado;
    private boolean cambiosGuardados;
    int idRegistroSeleccionado;
    public RegistrosPanel() {
        registroDao = new RegistroDao(); // Instanciar el DAO
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Subpanel Filtros
        JPanel filtrosPanel = new JPanel();
        JLabel especialidadLabel = new JLabel("Especialidad");
        String[] especialidadOptions = {"Todo", "Endodoncia", "Ortodoncia", "Ambas"};
        JComboBox<String> especialidadComboBox = new JComboBox<>(especialidadOptions);
        JLabel statusLabel = new JLabel("Status");
        String[] statusOptions = {"Todo", "Etapa 1", "Etapa 2", "Etapa 3", "Etapa 4"};
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
        JLabel nombreLabel = new JLabel("Nombre");
        JTextField nombreTextField = new JTextField(20);
        JButton buscarButton = new JButton("Buscar");

        filtrosPanel.add(especialidadLabel);
        filtrosPanel.add(especialidadComboBox);
        filtrosPanel.add(statusLabel);
        filtrosPanel.add(statusComboBox);
        filtrosPanel.add(nombreLabel);
        filtrosPanel.add(nombreTextField);
        filtrosPanel.add(buscarButton);

        // Subpanel tabla
        JPanel tablaPanel = new JPanel(new BorderLayout());

        // Table configuration
        String[] columnNames = {"ID", "Nombre", "Teléfono", "Correo", "Ciudad", "Fuente", "Especialidad", "Status","Fecha Registro"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar la edición de celdas
            }
        };
        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));

                return component;
            }
        };
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { // Listener para doble clic en una fila
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        // Obtener el ID del registro seleccionado
                        int idRegistro = (int) table.getValueAt(row, 0);

                        // Obtener el comentario del registro desde la base de datos
                        String comentario = registroDao.obtenerComentarioPorId(idRegistro);

                        // Mostrar la ventana emergente con el comentario y permitir la edición
                        mostrarVentanaComentario(idRegistro, comentario);
                    }
                }
            }
        });


        // Crea una tabla vacía con las columnas especificadas
        JScrollPane scrollPane = new JScrollPane(table);
        tablaPanel.add(scrollPane, BorderLayout.CENTER);
        tablaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Subpanel datos y botones
        JPanel datosBotonesPanel = new JPanel();
        datosBotonesPanel.setLayout(new BoxLayout(datosBotonesPanel, BoxLayout.Y_AXIS));
        datosBotonesPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel datosPanel = new JPanel();
        datosPanel.setLayout(new BoxLayout(datosPanel, BoxLayout.Y_AXIS));
        JLabel totalAlumnosLabel = new JLabel("Total de Alumnos: 0");
        JLabel totalRegistrosLabel = new JLabel("Total de Registros: " + registroDao.obtenerTotalRegistros());
        datosPanel.add(totalAlumnosLabel);
        datosPanel.add(totalRegistrosLabel);

        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new BoxLayout(botonesPanel, BoxLayout.Y_AXIS));
        JButton nuevoRegistroButton = new JButton("Nuevo registro");
        JButton editarRegistroButton = new JButton("Editar Registro");
        JButton eliminarRegistroButton = new JButton("Eliminar Registro");
        JButton inscribirAlumnoButton = new JButton("Inscribir Alumno");
        JButton exportarRegistrosButton = new JButton("Exportar Registros");
        JButton importarRegistrosButton = new JButton("Importar Registros");

        botonesPanel.add(nuevoRegistroButton);
        botonesPanel.add(editarRegistroButton);
        botonesPanel.add(eliminarRegistroButton);
        botonesPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio de 10

        botonesPanel.add(inscribirAlumnoButton);
        botonesPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio de 10

        botonesPanel.add(exportarRegistrosButton);
        botonesPanel.add(importarRegistrosButton);

        datosBotonesPanel.add(datosPanel);
        datosBotonesPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio de 10
        datosBotonesPanel.add(botonesPanel);

        // Añade los subpaneles al panel principal
        add(filtrosPanel, BorderLayout.NORTH);
        add(tablaPanel, BorderLayout.CENTER);
        add(datosBotonesPanel, BorderLayout.EAST);

        //Configure buttons
        nuevoRegistroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Show window class NuevoRegistro from package org.imbo.view.nuevoRegistro
                JFrame frame = new JFrame("Nuevo Registro");
                frame.setContentPane(new NuevoRegistro().getNuevoRegistroPanel());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });

        //Configure button Buscar
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                totalRegistrosLabel.setText("Total de Registros: " + registroDao.obtenerTotalRegistros());
                String especialidad = especialidadComboBox.getSelectedItem().toString();
                String status = statusComboBox.getSelectedItem().toString();
                String nombre = nombreTextField.getText();

                // Realizar la búsqueda según los filtros seleccionados
                List<Registro> registros;
                if (especialidad.equals("Todo") && status.equals("Todo") && nombre.equals("")) {
                    registros = registroDao.obtenerTodosRegistros();
                    table.setDefaultRenderer(Object.class, new StatusRowRenderer());//Cambiar el render de la tabla para mostrar los colores de status
                } else {
                    registros = registroDao.buscarRegistrosConFiltro(especialidad, status, nombre);
                }

                // Limpiar el modelo de tabla antes de actualizarlo
                model.setRowCount(0);

                // Actualizar el modelo de tabla con los resultados de la búsqueda
                for (Registro registro : registros) {
                    Object[] row = {
                            registro.getId_usuario(),
                            registro.getNombre(),
                            registro.getTelefono(),
                            registro.getEmail(),
                            registro.getCiudad(),
                            registro.getFuente(),
                            registro.getEspecialidad(),
                            registro.getStatus(),
                            registro.getFechaRegistro()
                    };
                    model.addRow(row);
                }
            }
        });
        // Configure button Editar Registro
        // Listener para el botón "Editar Registro"
        editarRegistroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    // Obtener el ID del registro seleccionado
                    int idRegistro = (int) table.getValueAt(row, 0);

                    // Obtener el registro desde la base de datos
                    Registro registro = registroDao.obtenerRegistroPorId(idRegistro);

                    // Abrir la ventana NuevoRegistro con los datos del registro seleccionado
                    JFrame frame = new JFrame("Editar Registro");
                    frame.setContentPane(new NuevoRegistro(registro).getNuevoRegistroPanel());
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                }
            }
        });

        eliminarRegistroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    // Obtener el ID del registro seleccionado
                    int idRegistro = (int) table.getValueAt(row, 0);

                    // Mostrar confirmación antes de eliminar el registro
                    String nombreRegistro = (String) table.getValueAt(row, 1);
                    int response = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres eliminar el registro '" + nombreRegistro + "'?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);                    if (response == JOptionPane.YES_OPTION) {
                        // Eliminar el registro de la base de datos
                        RegistroDao registroDao = new RegistroDao();
                        registroDao.eliminarRegistro(idRegistro);

                        // Opcional: Mostrar mensaje de éxito
                        JOptionPane.showMessageDialog(null, "Registro eliminado con éxito.");

                        // Actualizar la lista de registros en la tabla
                        actualizarListaRegistros();
                    }
                }
            }
        });
        inscribirAlumnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    // Obtener el nombre del registro seleccionado
                    String nombreRegistro = (String) table.getValueAt(row, 1);

                    // Preguntar al usuario si quiere inscribir al alumno seleccionado o a uno nuevo
                    int option = JOptionPane.showOptionDialog(null, "¿Quieres inscribir a " + nombreRegistro + "?", "Confirmar inscripción",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sí", "No, Alumno Nuevo"}, "Sí");

                    // Dependiendo de la opción seleccionada, realizar las acciones correspondientes
                    if (option == JOptionPane.YES_OPTION) {

                        // Opción Sí: realizar acciones para inscribir al alumno seleccionado
                        registroSeleccionado();

                    } else if (option == JOptionPane.NO_OPTION) {
                        abrirFormularioInscripion();
                    }
                } else {
                    // Si no hay un alumno seleccionado, simplemente abre la ventana para inscribir a un nuevo alumno
                    abrirFormularioInscripion();
                }
            }
        });
        // Configurara boton Exportar Registros
        exportarRegistrosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Guardar archivo Excel");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                // Establecer un filtro para asegurarnos de que solo se permitan archivos con extensión .xlsx
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Excel (.xlsx)", "xlsx");
                fileChooser.setFileFilter(filter);

                int userSelection = fileChooser.showSaveDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                    // Asegurarnos de que el archivo tenga la extensión .xlsx
                    if (!filePath.toLowerCase().endsWith(".xlsx")) {
                        filePath += ".xlsx";
                    }

                    DatabaseToExcelExporter databaseToExcelExporter = new DatabaseToExcelExporter();
                    databaseToExcelExporter.exportExcelRegistros(filePath);
                    JOptionPane.showMessageDialog(null, "Datos exportados correctamente");
                }
            }
        });


        // Configurar botón Importar Registros
        importarRegistrosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String excelFilePath = selectedFile.getAbsolutePath();

                    DatabaseToExcelExporter exporter = new DatabaseToExcelExporter();
                    exporter.importExcelRegistros(excelFilePath);
                }
            }
        });
    }
    private void mostrarVentanaComentario(int idRegistro, String comentario) {
        JFrame frame = new JFrame("Comentarios");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200); // Tamaño de la ventana

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel tituloLabel = new JLabel("Comentarios del Registro #" + idRegistro);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Fuente y tamaño del título

        JTextArea comentarioTextArea = new JTextArea(comentario);
        comentarioTextArea.setFont(new Font("Arial", Font.PLAIN, 14)); // Fuente y tamaño del texto
        JScrollPane scrollPane = new JScrollPane(comentarioTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton guardarButton = new JButton("Guardar");
        guardarButton.setFont(new Font("Arial", Font.BOLD, 14)); // Fuente y tamaño del botón
        guardarButton.setBackground(Color.GREEN); // Color de fondo del botón
        guardarButton.setForeground(Color.WHITE); // Color de texto del botón

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener el comentario modificado y guardar en la base de datos
                comentarioModificado = comentarioTextArea.getText();
                registroDao.actualizarComentario(idRegistro, comentarioModificado);

                // Marcar los cambios como guardados y cerrar la ventana
                cambiosGuardados = true;
                frame.dispose();
            }
        });

        // Panel para el botón centrado en la parte inferior
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(guardarButton);

        panel.add(tituloLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public void abrirFormularioInscripion(){
        JFrame frame = new JFrame("Inscribir Alumno");
        frame.setContentPane(new FormularioInscripcion().getContentPane());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public void registroSeleccionado(){
        int row = table.getSelectedRow();
        String nombreCompleto = (String) table.getValueAt(row, 1);
        String telefono = (String) table.getValueAt(row, 2);
        String correo = (String) table.getValueAt(row, 3);
        String especialidad = (String) table.getValueAt(row, 6);

        // Obtener el ID del registro seleccionado
        int idRegistroSeleccionado = (int) table.getValueAt(row, 0); // Obtener el ID del registro seleccionado
        isRegistroSeleccionado = true; // Indicar que hay un registro seleccionado
        this.idRegistroSeleccionado = idRegistroSeleccionado; // Guardar el ID del registro seleccionado
        JOptionPane.showMessageDialog(null, "Registro seleccionado", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        // Abrir el formulario de inscripción y llenar los datos del alumno
        JFrame frame = new JFrame("Inscribir Alumno seleccionado");
        FormularioInscripcion formularioInscripcion = new FormularioInscripcion();

        //Establecer la instancia de RegistrosPanel en el formulario de inscripción
        formularioInscripcion.setRegistrosPanelInstance(this);

        formularioInscripcion.setNombreCompleto(nombreCompleto);
        formularioInscripcion.setTelefono(telefono);
        formularioInscripcion.setCorreo(correo);
        formularioInscripcion.setEspecialidad(especialidad);
        frame.setContentPane(formularioInscripcion.getContentPane());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public void actualizarListaRegistros() {
        // Obtener los registros actualizados de la base de datos
        List<Registro> registros = registroDao.obtenerTodosRegistros();

        // Limpiar el modelo de tabla antes de actualizarlo
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        // Actualizar el modelo de tabla con los resultados de la búsqueda
        for (Registro registro : registros) {
            Object[] row = {
                    registro.getId_usuario(),
                    registro.getNombre(),
                    registro.getTelefono(),
                    registro.getEmail(),
                    registro.getCiudad(),
                    registro.getFuente(),
                    registro.getEspecialidad(),
                    registro.getStatus(),
                    registro.getFechaRegistro()
            };
            model.addRow(row);
        }
    }

    public boolean isRegistroSeleccionado() {
        return isRegistroSeleccionado;
    }

    public boolean setisRegistroSeleccionado(boolean isRegistroSeleccionado) {
        this.isRegistroSeleccionado = isRegistroSeleccionado;
        return isRegistroSeleccionado;
    }
    public boolean getIsRegistroSeleccionado() {
        return isRegistroSeleccionado;
    }
    public int getIdRegistroSeleccionado() {
        return idRegistroSeleccionado;
    }
}

