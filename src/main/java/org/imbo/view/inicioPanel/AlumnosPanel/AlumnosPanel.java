package org.imbo.view.inicioPanel.AlumnosPanel;

import org.imbo.dao.AlumnoDao;
import org.imbo.model.alumno.Alumno;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AlumnosPanel extends JPanel {
    Color fondo = new Color( 127, 179, 213 );
    private JLabel matriculaLbl = new JLabel("Matrícula:");
    private JLabel nombrelbl = new JLabel("Nombre:");
    private JLabel especialidadLbl = new JLabel("Especialidad:");
    private JLabel correoLbl = new JLabel("Correo:");
    private JLabel telefonoLbl = new JLabel("Teléfono:");
    private JLabel fechaInscripcionLbl = new JLabel("Fecha de inscripción:");
    private AlumnoDao alumnoDao;
    private JTable table;
    JPanel ladoDerechoPanel = new JPanel();

    private PagoAlumnoPanel pagosPanel; // Movemos la declaración aquí
    private AddDocument documentosPanel; // Movemos la declaración aquí
    public AlumnosPanel() {

        alumnoDao = new AlumnoDao();

        setLayout(new BorderLayout());
        setBackground(fondo);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de filtros
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtroPanel.setBackground(Color.white);
        // Configurar el panel de filtros (añadir componentes y lógica de filtrado)
        JLabel matriculaLabel = new JLabel("Matrícula:");
        JTextField matriculaTextField = new JTextField(10);

        JLabel nombreLabel = new JLabel("Nombre:");
        JTextField nombreTextField = new JTextField(10);

        JLabel especialidadLabel = new JLabel("Especialidad:");
        String[] especialidades = {"Todos", "Ortodoncia", "Endodoncia"};
        JComboBox<String> especialidadComboBox = new JComboBox<>(especialidades);
        JButton buscarButton = new JButton("Buscar");

        // Agregar componentes al panel de filtros
        filtroPanel.add(matriculaLabel);
        filtroPanel.add(matriculaTextField);
        filtroPanel.add(nombreLabel);
        filtroPanel.add(nombreTextField);
        filtroPanel.add(especialidadLabel);
        filtroPanel.add(especialidadComboBox);
        filtroPanel.add(buscarButton);

        // Panel de tabla
        JPanel tablaPanel = new JPanel(new BorderLayout());
        tablaPanel.setBackground(Color.white);
        String[] columnNames = {"Matrícula", "Nombre", "Especialidad", "Fecha de inscripción"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // Configurar el panel de tabla (añadir componentes y lógica para mostrar los alumnos)
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
            public void mouseClicked(MouseEvent e) { // Listener para cuando se hace click en una fila de la tabla
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    int matriculaAlumno = (int) table.getValueAt(row, 0);
                    // Lógica para mostrar los datos, documentos y pagos del alumno seleccionado

                    Alumno alumnoSeleccionado = alumnoDao.ObtenerAlumnoPorMatricula(matriculaAlumno);
                    if (alumnoSeleccionado != null) {
                        matriculaLbl.setText("Matrícula: " + alumnoSeleccionado.getMatricula());
                        nombrelbl.setText("Nombre: " + alumnoSeleccionado.getNombre());
                        especialidadLbl.setText("Especialidad: " + alumnoSeleccionado.getEspecialidad());
                        correoLbl.setText("Correo: " + alumnoSeleccionado.getCorreo());
                        telefonoLbl.setText("Teléfono: " + alumnoSeleccionado.getTelefono());
                        fechaInscripcionLbl.setText("Fecha de inscripción: " + alumnoSeleccionado.getFechaInscripcion());

                    }
                    // Utilizar el método setAlumnoSeleccionado para conectar el SubPanelDocumentosAlumno con el AlumnosPanel
                    documentosPanel.setAlumno(alumnoSeleccionado);
                    // Crear o actualizar el panel de documentos con el alumno seleccionado
                    createDocumentosPanel(alumnoSeleccionado);
                    createPagosPanel(alumnoSeleccionado);
                    documentosPanel.obtenerRutasDocumentosExistentes();
                    pagosPanel.actualizarTablaPagos(alumnoSeleccionado);
                }
            }
        });

        //Crear tabla vacia con las columnas especificadas
        JScrollPane scrollPane = new JScrollPane(table);
        tablaPanel.add(scrollPane, BorderLayout.CENTER);
        tablaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de datos
        JPanel datosPanel = new JPanel(new GridBagLayout());
        datosPanel.setBackground(Color.white);
        Border datosPanelBorder = BorderFactory.createEtchedBorder();
        datosPanel.setBorder(BorderFactory.createTitledBorder(datosPanelBorder, "Datos del alumno"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST; // Alineación a la izquierda

        // Configurar el panel de datos (añadir componentes y lógica para mostrar los datos del alumno seleccionado)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 5, 5, 5);

        datosPanel.add(matriculaLbl, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        datosPanel.add(nombrelbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        datosPanel.add(especialidadLbl, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        datosPanel.add(correoLbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        datosPanel.add(telefonoLbl, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        datosPanel.add(fechaInscripcionLbl, gbc);


        // Panel de documentos
        documentosPanel = new AddDocument(null);
        // Configurar el panel de documentos (añadir componentes y lógica para mostrar los documentos del alumno seleccionado)
        documentosPanel.setLayout(new BoxLayout(documentosPanel, BoxLayout.Y_AXIS));
        documentosPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        documentosPanel.setBorder(BorderFactory.createTitledBorder("Documentos del alumno"));

        // Panel de pagos
        pagosPanel = new PagoAlumnoPanel(null);

        // Configurar el panel de pagos (añadir componentes y lógica para mostrar los pagos del alumno seleccionado)
        pagosPanel.setLayout(new BoxLayout(pagosPanel, BoxLayout.Y_AXIS));
        pagosPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pagosPanel.setBorder(BorderFactory.createTitledBorder("Pagos del alumno"));

        // Panel de lado derecho (que contiene datos, documentos y pagos)
        ladoDerechoPanel.setLayout(new BoxLayout(ladoDerechoPanel, BoxLayout.Y_AXIS));
        ladoDerechoPanel.add(datosPanel); // Agregar el panel de datos al lado derecho
        ladoDerechoPanel.add(Box.createVerticalGlue()); // Espacio flexible vertical para separar los paneles
        ladoDerechoPanel.add(Box.createVerticalGlue());

        // No agregamos el pagosPanel y documentos aquí inicialmente

        // Agregar los subpaneles al panel principal usando GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(filtroPanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(tablaPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(ladoDerechoPanel, gbc);

        //Confiurar botones
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para filtrar los alumnos
                String matricula = matriculaTextField.getText();
                String nombre = nombreTextField.getText();
                String especialidad = especialidadComboBox.getSelectedItem().toString();

                //Realizar la busqueda en la base de datos
                List<Alumno> alumnos;
                if (especialidad.equals("Todos") && matricula.isEmpty() && nombre.isEmpty()) {
                    alumnos = alumnoDao.obtenerTodosAlumnos();
                }else{
                    alumnos = alumnoDao.obtenerAlumnosPorFiltro(matricula, nombre, especialidad);
                }

                //Limpiar la tabla
                model.setRowCount(0);

                //Actualizar el modelo de la tabla
                for (Alumno alumno : alumnos){
                    Object[] row ={
                            alumno.getMatricula(),
                            alumno.getNombre(),
                            alumno.getEspecialidad(),
                            alumno.getFechaInscripcion()
                    };
                    model.addRow(row);
                }
            }
        });
    }

    // Método para crear el documentosPanel con el alumno seleccionado
    private void createDocumentosPanel(Alumno alumnoSeleccionado) {
        // Si ya existe un documentosPanel, lo removemos antes de crear uno nuevo
        if (documentosPanel != null) {
            ladoDerechoPanel.remove(documentosPanel);
        }

        // Creamos un nuevo documentosPanel con el alumno seleccionado
        documentosPanel = new AddDocument(alumnoSeleccionado);
        documentosPanel.setLayout(new BoxLayout(documentosPanel, BoxLayout.Y_AXIS));
        documentosPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        documentosPanel.setBorder(BorderFactory.createTitledBorder("Documentos del alumno"));

        // Agregamos el nuevo documentosPanel al lado derechoPanel
        ladoDerechoPanel.add(documentosPanel);

        // Repintamos el ladoDerechoPanel para reflejar los cambios
        ladoDerechoPanel.revalidate();
        ladoDerechoPanel.repaint();
    }
    private void createPagosPanel(Alumno alumnoSeleccionado){
        //Si ya existe un pagosPanel, lo removemos antes de crear uno nuevo
        if (pagosPanel != null){
            ladoDerechoPanel.remove(pagosPanel);
        }

        //Creamos un nuevo pagosPanel con el alumno seleccionado
        pagosPanel = new PagoAlumnoPanel(alumnoSeleccionado);
        pagosPanel.setLayout(new BoxLayout(pagosPanel, BoxLayout.Y_AXIS));
        pagosPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pagosPanel.setBorder(BorderFactory.createTitledBorder("Pagos del alumno"));

        //Agregamos el nuevo pagosPanel al lado derechoPanel
        ladoDerechoPanel.add(pagosPanel);

        //Repintamos el ladoDerechoPanel para reflejar los cambios
        ladoDerechoPanel.revalidate();
        ladoDerechoPanel.repaint();
    }
}

