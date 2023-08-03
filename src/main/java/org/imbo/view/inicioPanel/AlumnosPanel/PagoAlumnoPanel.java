package org.imbo.view.inicioPanel.AlumnosPanel;

import org.imbo.dao.PagosAlDao;
import org.imbo.model.alumno.Alumno;
import org.imbo.model.alumno.PagoAlumno;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import javax.swing.table.DefaultTableModel;

public class PagoAlumnoPanel extends JPanel {
    private Alumno alumno;
    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
    private JComboBox<String> tipoPagoComboBox;
    private JTextField cantidadTextField;
    private JRadioButton efectivoRadioButton;
    private JRadioButton tarjetaRadioButton;
    private JTextArea comentarioTextArea;
    private JButton agregarPagoButton;
    private JTable pagosTable;
    private DefaultTableModel tableModel;

    public PagoAlumnoPanel(Alumno alumno) {
        this.alumno = alumno;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 400));

        // Panel superior izquierdo
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Combo box tipo de pago
        JLabel tipoPagoLabel = new JLabel("Tipo de Pago:");
        tipoPagoComboBox = new JComboBox<>(new String[]{"Inscripción", "Colegiatura", "Reinscripción", "Otro"});
        tipoPagoComboBox.setPreferredSize(new Dimension(150, tipoPagoComboBox.getPreferredSize().height));
        leftPanel.add(tipoPagoLabel, gbc);

        gbc.gridx++;
        leftPanel.add(tipoPagoComboBox, gbc);

        // Label y campo de cantidad en la misma línea
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel cantidadLabel = new JLabel("Cantidad:");
        leftPanel.add(cantidadLabel, gbc);

        gbc.gridx++;
        cantidadTextField = new JTextField();
        cantidadTextField.setPreferredSize(new Dimension(90, cantidadTextField.getPreferredSize().height));
        leftPanel.add(cantidadTextField, gbc);

        // Panel superior derecho
        JPanel rightPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Cajas de selección (Efectivo y Tarjeta) en la misma línea
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel formaPagoLabel = new JLabel("Forma de Pago:");
        rightPanel.add(formaPagoLabel, gbc);

        gbc.gridx++;
        efectivoRadioButton = new JRadioButton("Efectivo");
        tarjetaRadioButton = new JRadioButton("Tarjeta");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(efectivoRadioButton);
        buttonGroup.add(tarjetaRadioButton);
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioPanel.add(efectivoRadioButton);
        radioPanel.add(tarjetaRadioButton);
        rightPanel.add(radioPanel, gbc);

        // Comentario con caja de texto
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel comentarioLabel = new JLabel("Comentario:");
        rightPanel.add(comentarioLabel, gbc);

        gbc.gridy++;
        comentarioTextArea = new JTextArea();
        comentarioTextArea.setPreferredSize(new Dimension(200, comentarioTextArea.getPreferredSize().height));
        rightPanel.add(comentarioTextArea, gbc);

        // Panel inferior con el botón "Agregar Pago"
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);
        agregarPagoButton = new JButton("Agregar Pago");
        bottomPanel.add(agregarPagoButton, gbc);

        // Agregamos los subpaneles al panel principal
        JPanel upperPanel = new JPanel(new BorderLayout());
        upperPanel.add(leftPanel, BorderLayout.WEST);
        upperPanel.add(rightPanel, BorderLayout.CENTER);
        add(upperPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);

        // Panel inferior con la tabla de pagos
        JPanel lowerPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Tipo de Pago");
        tableModel.addColumn("Cantidad");
        tableModel.addColumn("Forma de Pago");
        tableModel.addColumn("Comentario");
        pagosTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(pagosTable);
        lowerPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Agregamos la tabla al panel principal
        add(lowerPanel, BorderLayout.SOUTH);

        // Acción del botón Agregar Pago
        agregarPagoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int matricula = alumno.getMatricula();
                String tipoPago = (String) tipoPagoComboBox.getSelectedItem();
                int cantidad = Integer.parseInt(cantidadTextField.getText());
                String formaPago = "";
                Date fechaPago = new Date(System.currentTimeMillis());
                if (efectivoRadioButton.isSelected()) {
                    formaPago = "Efectivo";
                } else if (tarjetaRadioButton.isSelected()) {
                    formaPago = "Tarjeta";
                }
                String comentario = comentarioTextArea.getText();
                PagoAlumno pagoAlumno = new PagoAlumno(0,matricula, tipoPago,fechaPago, cantidad, formaPago, comentario);

                //Guardar el pago en la base de datos
                PagosAlDao pagosAlDao = new PagosAlDao();
                pagosAlDao.insertarPago(alumno, pagoAlumno);
                actualizarTablaPagos(alumno);
            }
        });
    }
    public void actualizarTablaPagos(Alumno alumno) {
        tableModel.setRowCount(0); //Limpia la tabla

        //Obtener los pagos del alumno desde la base de datos
        PagosAlDao pagosAlDao = new PagosAlDao();
        PagoAlumno[] pagos = pagosAlDao.obtenerPagosPorAlumno(alumno.getMatricula()).toArray(new PagoAlumno[0]); //Convierte el ArrayList a arreglo
        for (PagoAlumno pago : pagos) {
            Object[] row = new Object[4];
            row[0] = pago.getTipoPago();
            row[1] = pago.getCantidad();
            row[2] = pago.getFormaPago();
            row[3] = pago.getComentario();
            tableModel.addRow(row);
        }
        tableModel.fireTableDataChanged(); //Actualiza la tabla
    }
}

