package org.imbo.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class Login extends JFrame{
    // Creamos los componentes
    private JPanel MainLogin = new JPanel();
    private JButton btnLogin;
    private JTextField txtFieldUser;
    private JPasswordField passwordField;
    private JLabel lblUser;
    private JLabel lblPassword;
    public static String texto = "";
    public static String texto2 = "";

    public static String admin = "admin";
    public static String psw = "admin";

    public Login() {
        // Configurar el JFrame
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 250);
        setLayout(null); // Layout(null) means that you will be responsible for the layout of the components
        setLocationRelativeTo(null); // This line means that the window will be centered on the screen
        setResizable(false); // This line means that the window will not be resizable

        // Crear y configurar los componentes
        lblUser = new JLabel("Usuario"); // Create a new JLabel with the text "User"
        lblUser.setBounds(45, 40, 100, 30);// Set the position and size of the JLabel
        add(lblUser); // Add the JLabel to the JFrame

        txtFieldUser = new JTextField();
        txtFieldUser.setBounds(135, 40, 150, 30);
        add(txtFieldUser);

        lblPassword = new JLabel("Contraseña");
        lblPassword.setBounds(45, 80, 100, 30);
        add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(135, 80, 150, 30);
        add(passwordField);

        btnLogin = new JButton("Ingresar");
        btnLogin.setBounds(125, 130, 100, 30);
        btnLogin.addActionListener(this::actionPerformed); //This line means that when the button is clicked, the actionPerformed method will be called
        add(btnLogin);

        // Agregar los componentes al JFrame
        add(btnLogin);
        add(txtFieldUser);
        add(passwordField);
        add(lblUser);
        add(lblPassword);
    }
    private boolean isValidCredentials(String username, char[] password) { // This method is called when the button is clicked
        // Lógica para verificar las credenciales en tu sistema
        String validUsername = "admin";
        char[] validPassword = "admin".toCharArray(); // Convert the string to a char array

        return username.equals(validUsername) && Arrays.equals(password, validPassword);
    }

    public void actionPerformed(ActionEvent e) {
        // Lógica para verificar las credenciales en tu sistema
        String username = txtFieldUser.getText();
        char[] password = passwordField.getPassword();

        if (isValidCredentials(username, password)) {
            JOptionPane.showMessageDialog(null, "Ingreso exitoso");
            texto = txtFieldUser.getText();
            texto2 = passwordField.getText();
            dispose(); //this line closes the window
            //Open the Inicio window class
            Inicio inicio = new Inicio();
            inicio.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
        }

        //Limpiar los campos de usuario y contraseña despues de ingresar
        txtFieldUser.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        Login login = new Login();
        login.setVisible(true);
    }
}