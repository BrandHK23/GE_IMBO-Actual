package org.imbo.view.inicioPanel;

import org.imbo.dao.PacienteDao;
import org.imbo.export.DatabaseToExcelExporter;
import org.imbo.model.Paciente;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PacientesPanel  extends JPanel {
    private PacienteDao pacienteDao;
    private JTable table;

    public PacientesPanel(){
        pacienteDao = new PacienteDao();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //SubPanel filtros
        JPanel subPanelFiltros = new JPanel();
        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField(10);
        JLabel lblTratamiento = new JLabel("Tratamiento:");
        String[] tratamientosOptions = {"", "Ortodoncia", "Endodoncia", "Ambos"};
        JComboBox<String> cmbTratamiento = new JComboBox<>(tratamientosOptions);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnExportar = new JButton("Exportar");

        subPanelFiltros.add(lblNombre);
        subPanelFiltros.add(txtNombre);
        subPanelFiltros.add(lblTratamiento);
        subPanelFiltros.add(cmbTratamiento);
        subPanelFiltros.add(btnBuscar);
        subPanelFiltros.add(btnExportar);

        //SubPanel tabla
        JPanel subPanelTabla = new JPanel(new BorderLayout());

        //Configuracion de la tabla
        String[] columnNames = {"ID", "Nombre", "Telefono", "Correo", "Motivo", "Tratamiento"};
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
                tableColumn.setWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getWidth()));
                return component;
            }
        };

        //Crea una tabla vacia con las columnas especificadas
        JScrollPane scrollPane = new JScrollPane(table);
        subPanelTabla.add(scrollPane, BorderLayout.CENTER);
        subPanelTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //SubPanel RegistroPacientes
        JPanel subPanelRegistroPacientes = new JPanel();
        subPanelRegistroPacientes.setBorder(BorderFactory.createTitledBorder("Formulario de registro"));
        //Formulario de registro
        JLabel lblNombrePaciente = new JLabel("Nombre:");
        JTextField txtNombrePaciente = new JTextField(10);
        JLabel lblTelefonoPaciente = new JLabel("Telefono:");
        JTextField txtTelefonoPaciente = new JTextField(10);
        JLabel lblCorreoPaciente = new JLabel("Correo:");
        JTextField txtCorreoPaciente = new JTextField(10);
        JLabel lblMotivoPaciente = new JLabel("Motivo:");
        JTextField txtMotivoPaciente = new JTextField(10);
        JLabel lblTratamientoPaciente = new JLabel("Tratamiento:");
        String[] tratamientosOptionsPaciente = {"", "Ortodoncia", "Endodoncia", "Ambos"};

        JButton btnRegistrarPaciente = new JButton("Registrar");

        subPanelRegistroPacientes.add(lblNombrePaciente);
        subPanelRegistroPacientes.add(txtNombrePaciente);
        subPanelRegistroPacientes.add(lblTelefonoPaciente);
        subPanelRegistroPacientes.add(txtTelefonoPaciente);
        subPanelRegistroPacientes.add(lblCorreoPaciente);
        subPanelRegistroPacientes.add(txtCorreoPaciente);
        subPanelRegistroPacientes.add(lblMotivoPaciente);
        subPanelRegistroPacientes.add(txtMotivoPaciente);
        subPanelRegistroPacientes.add(lblTratamientoPaciente);
        subPanelRegistroPacientes.add(new JComboBox<>(tratamientosOptionsPaciente));
        subPanelRegistroPacientes.add(btnRegistrarPaciente);

        //Agrega los subPaneles al panel principal
        add(subPanelFiltros, BorderLayout.NORTH);
        add(subPanelTabla, BorderLayout.CENTER);
        add(subPanelRegistroPacientes, BorderLayout.SOUTH);

        //Configurar boton registrar
        btnRegistrarPaciente.addActionListener(e -> {
            String nombre = txtNombrePaciente.getText();
            String telefono = txtTelefonoPaciente.getText();
            String correo = txtCorreoPaciente.getText();
            String motivo = txtMotivoPaciente.getText();
            String tratamiento = (String) cmbTratamiento.getSelectedItem();

            pacienteDao.registrarPaciente(nombre, telefono, correo, motivo, tratamiento);
            JOptionPane.showMessageDialog(null, "Paciente registrado correctamente");
        });

        //Configurar boton buscar
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombre.getText();
                String tratamiento = (String) cmbTratamiento.getSelectedItem();

                //Realizar la busqueda segundo los filtros
                //Si no se especifica un filtro, se debe de buscar por todos los pacientes
                List<Paciente> pacientes;
                if(nombre.isEmpty() && tratamiento.isEmpty()){
                    pacientes = pacienteDao.obtenerTodosPacientes();
                }else{
                    pacientes = pacienteDao.obtenerPacientesPorFiltros(nombre, tratamiento);
                }

                //Limpiar la tabla
                model.setRowCount(0);

                //Actualizar el modelo de la tabla con los resultados de la busqueda
                for(Paciente paciente : pacientes){
                    model.addRow(new Object[]{
                            paciente.getId_paciente(),
                            paciente.getNombre(),
                            paciente.getTelefono(),
                            paciente.getCorreo(),
                            paciente.getMotivo(),
                            paciente.getTratamiento()
                    });
                }
            }
        });

        //Configurar boton exportar a excel
        btnExportar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Exporta la lista de pacientes a un archivo excel
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
                    databaseToExcelExporter.exportarPacientes(filePath);
                    JOptionPane.showMessageDialog(null, "Datos exportados correctamente");
                }
            }
        });

    }
}
