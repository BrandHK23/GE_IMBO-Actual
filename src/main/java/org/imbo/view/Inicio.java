package org.imbo.view;

import org.imbo.view.inicioPanel.AlumnosPanel.AlumnosPanel;
import org.imbo.view.inicioPanel.RegistroPanel.RegistrosPanel;

import javax.swing.*;

public class Inicio extends JFrame {
    // Create JTabbedPane
    private JTabbedPane tabbedPane;

    // Create constructor
    public Inicio() {
        // Configure the JFrame
        setTitle("Opciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1420, 720);
        setLocationRelativeTo(null); // Center the window on the screen

        // Create the JTabbedPane
        tabbedPane = new JTabbedPane();

        // Create panels for Alumnos, Profesores, and Calendarios functions
        RegistrosPanel panelRegistros = new RegistrosPanel(); // Create an instance of RegistroPanel
        AlumnosPanel panelAlumnos = new AlumnosPanel(); // Crear una instancia de AlumnosPanel
        JPanel panelProfesores = new JPanel();
        JPanel panelCalendarios = new JPanel();

        // Add the panels to the JTabbedPane with tab titles
        tabbedPane.addTab("Alumnos", panelAlumnos); // Agregar el panel de Alumnos a la pestaña
        tabbedPane.addTab("Registros", panelRegistros);
        tabbedPane.addTab("Profesores", panelProfesores);
        tabbedPane.addTab("Calendarios", panelCalendarios);

        // Add the JTabbedPane to the JFrame
        add(tabbedPane);
    }

}

