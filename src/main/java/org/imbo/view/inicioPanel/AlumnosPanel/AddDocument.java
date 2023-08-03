package org.imbo.view.inicioPanel.AlumnosPanel;

import org.imbo.dao.DocAlumnoDao;
import org.imbo.model.alumno.Alumno;
import org.imbo.model.alumno.DocAlumno;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddDocument extends JPanel {
    private Alumno alumno;

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    private JTextField[] filePathFields;

    public AddDocument(Alumno alumno) {
        this.alumno = alumno;

        // Obtener nombres de los documentos
        String[] nombresDocumentos = {"Titulo", "Cédula Profesional", "Acta de nacimiento", "Fotografias", "CURP", "Comprobante de domicilio", "Carta de intencion", "INE"};

        // Crear panel para mostrar los campos de ruta de los documentos
        JPanel panelDocumentos = new JPanel(new GridLayout(nombresDocumentos.length, 2));

        // Crear campos de texto para las rutas de los documentos
        filePathFields = new JTextField[nombresDocumentos.length];

        for (int i = 0; i < nombresDocumentos.length; i++) {
            JLabel lblDocumento = new JLabel(nombresDocumentos[i]);

            JTextField txtRuta = new JTextField() {
                @Override
                public Dimension getPreferredSize() {
                    Dimension preferredSize = super.getPreferredSize();
                    preferredSize.height = this.getFontMetrics(getFont()).getHeight();
                    return preferredSize;
                }
            };

            txtRuta.setEditable(false); // Desactivar la edición directa

            JButton btnSeleccionar = new JButton("Agregar");

            // Configurar acción del botón "Seleccionar"
            final int index = i;
            btnSeleccionar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    int option = fileChooser.showOpenDialog(AddDocument.this);
                    if (option == JFileChooser.APPROVE_OPTION) {
                        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                        txtRuta.setText(filePath);
                    }
                }
            });

            // Agregar los componentes al panel
            panelDocumentos.add(lblDocumento);
            panelDocumentos.add(txtRuta);
            panelDocumentos.add(btnSeleccionar);

            // Guardar referencia al campo de texto en el array
            filePathFields[i] = txtRuta;
        }

        // Crear botón "Guardar"
        JButton btnGuardar = new JButton("Guardar");

        // Configurar acción del botón "Guardar"
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener las rutas de los documentos
                List<String> rutasDocumentos = new ArrayList<>();
                for (JTextField filePathField : filePathFields) {
                    String rutaDocumento = filePathField.getText();
                    rutasDocumentos.add(rutaDocumento);
                }

                // Asignar las rutas de los documentos al alumno seleccionado
                alumno.setRutasDocumentos(rutasDocumentos);

                // Guardar las rutas de los documentos en la tabla DocAlumno
                DocAlumnoDao docAlumnoDao = new DocAlumnoDao();
                docAlumnoDao.guardarDocumentosAlumno(alumno);

            }
        });

        // Crear panel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);

        // Crear panel principal y agregar los paneles anteriores con un margen de 10 píxeles
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        panelPrincipal.add(panelDocumentos, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Agregar el panel principal al frame
        add(panelPrincipal);
    }

    public void obtenerRutasDocumentosExistentes() {
        DocAlumnoDao docAlumnoDao = new DocAlumnoDao();
        List<DocAlumno> documentos = docAlumnoDao.obtenerDocumentosPorAlumno(alumno.getMatricula());

        for (DocAlumno documento : documentos) {
            int numDoc = documento.getNum_doc();
            if (numDoc > 0 && numDoc <= filePathFields.length) {
                JTextField textField = filePathFields[numDoc - 1];
                textField.setText(documento.getRuta_doc());
            }
        }
    }
}
