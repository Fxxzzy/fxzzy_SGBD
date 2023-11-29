package interfasmysql;

import java.awt.*;
import javax.swing.*;
import java.sql.Connection;
import packeteria.Paqueteria;

public class Conectar extends JPanel {
    private Inicio inicio;

    public Conectar(Inicio inicio) {
        this.inicio = inicio; // Guardar la referencia de la ventana Inicio
        setBackground(new Color(230, 204, 255));

        setLayout(new BorderLayout());

        JPanel panelConexion = new JPanel();
        panelConexion.setLayout(new BoxLayout(panelConexion, BoxLayout.Y_AXIS));
        panelConexion.setBackground(new Color(204, 153, 204));

        Font labelFont = new Font("Arial", Font.BOLD, 18); // Cambiado a Font.PLAIN y tamaño 16

        JLabel lblTitulo = new JLabel("Conectar a MySQL");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18)); // Tamaño original, negrita
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblHost = new JLabel("Host:");
        lblHost.setFont(labelFont);

        JLabel lblPuerto = new JLabel("Puerto:");
        lblPuerto.setFont(labelFont);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(labelFont);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(labelFont);

        lblHost.setForeground(Color.BLACK);
        lblPuerto.setForeground(Color.BLACK);
        lblUsuario.setForeground(Color.BLACK);
        lblContraseña.setForeground(Color.BLACK);

        JTextField txtHost = new JTextField(10);
        txtHost.setText("127.0.0.1");
           txtHost.setFont(new Font("Arial", Font.BOLD, 18));
        txtHost.setBackground(new Color(255, 255, 255));
        txtHost.setBorder(BorderFactory.createLineBorder(Color.BLACK,3)); // Agregar un borde



        JTextField txtPuerto = new JTextField(10);
        txtPuerto.setText("3306");
        txtPuerto.setFont(new Font("Arial", Font.BOLD, 18));
        txtPuerto.setBackground(new Color(255, 255, 255));
        
txtPuerto.setBorder(BorderFactory.createLineBorder(Color.BLACK,3)); // Agregar un borde

        JTextField txtUsuario = new JTextField(10);
        txtUsuario.setText("root");
           txtUsuario.setFont(new Font("Arial", Font.BOLD, 18));
        txtUsuario.setBackground(new Color(255, 255, 255));
txtUsuario.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); // Agregar un borde

        JPasswordField txtContraseña = new JPasswordField(10);
        txtContraseña.setText("1234");
           txtContraseña.setFont(new Font("Arial", Font.BOLD, 18));
        txtContraseña.setBackground(new Color(255, 255, 255));
        txtContraseña.setBorder(BorderFactory.createLineBorder(Color.BLACK,3)); // Agregar un borde

        

        JButton btnConectar = new JButton("Conectar");
        btnConectar.setFont(labelFont);
        btnConectar.setBackground(new Color(0, 128, 0));
        btnConectar.setForeground(Color.WHITE);

        btnConectar.addActionListener(e -> {
            String host = txtHost.getText();
            int puerto = Integer.parseInt(txtPuerto.getText());
            String usuario = txtUsuario.getText();
            String contraseña = new String(txtContraseña.getPassword());

            Paqueteria paqueteria = new Paqueteria(host, usuario, contraseña, puerto);
            Connection conexion = paqueteria.getConexion();

            if (conexion != null) {
                JOptionPane.showMessageDialog(this, "Conexión exitosa a la base de datos MySQL.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                inicio.cargarBasesDeDatos(paqueteria); // Cargar bases de datos en la ventana Inicio
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelConexion.add(Box.createVerticalStrut(20));
        panelConexion.add(lblTitulo);
        panelConexion.add(Box.createVerticalStrut(20));
        panelConexion.add(lblHost);
        panelConexion.add(txtHost);
        panelConexion.add(lblPuerto);
        panelConexion.add(txtPuerto);
        panelConexion.add(lblUsuario);
        panelConexion.add(txtUsuario);
        panelConexion.add(lblContraseña);
        panelConexion.add(txtContraseña);
        panelConexion.add(Box.createVerticalStrut(20));
        panelConexion.add(btnConectar);
        panelConexion.add(Box.createVerticalStrut(20));

        add(panelConexion, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Inicio inicio = new Inicio(); // Crea una instancia de Inicio
            inicio.setVisible(true);
        });
    }
}
