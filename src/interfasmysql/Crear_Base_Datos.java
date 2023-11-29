package interfasmysql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import packeteria.Paqueteria;

public class Crear_Base_Datos extends JPanel {
    private JTextField txtNombreBd;

    public Crear_Base_Datos(Inicio inicio) {
        setBackground(new Color(230, 204, 255));

        // Utiliza un GridBagLayout para centrar la interfaz
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Crear un panel para los componentes de creación de base de datos con disposición BoxLayout vertical
        JPanel panelCreacion = new JPanel();
        panelCreacion.setBackground(new Color(230, 204, 255));
        panelCreacion.setLayout(new BoxLayout(panelCreacion, BoxLayout.Y_AXIS));

        // Crear etiqueta y campo de texto para el nombre de la base de datos
        JLabel lblNombreBd = new JLabel("Nombre de la Base de Datos:");
        txtNombreBd = new JTextField(20);
        panelCreacion.add(lblNombreBd);
        panelCreacion.add(txtNombreBd);

        // Crear el botón "Crear" para crear la base de datos
        JButton btnCrearBd = new JButton("Crear");
        panelCreacion.add(btnCrearBd);

        // Agregar el panel de creación a la ventana de Crear_Base_Datos
        add(panelCreacion, gbc);

        // Aumentar el tamaño de la fuente
        Font labelFont = new Font("Arial", Font.BOLD, 24);
        lblNombreBd.setFont(labelFont);
        txtNombreBd.setFont(new Font("Arial", Font.PLAIN, 24)); // Usar Font.PLAIN para el campo de texto
        txtNombreBd.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));

        btnCrearBd.setFont(labelFont);

        // Agregar un ActionListener al botón "Crear"
        btnCrearBd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Obtener el nombre de la base de datos desde el campo de texto
                String nombreBd = txtNombreBd.getText();

                // Crear una instancia de Paqueteria
                int tu_puerto = 3306; // Reemplaza 3306 con el número de puerto correcto
                Paqueteria paqueteria = new Paqueteria("localhost", "root", "1234", 3306);

                // Llama al método crearBd con el nombre de la base de datos
                paqueteria.crearBd(nombreBd);

                // Muestra un mensaje emergente
                JOptionPane.showMessageDialog(Crear_Base_Datos.this, "La base de datos '" + nombreBd + "' ha sido creada.", "Base de Datos Creada", JOptionPane.INFORMATION_MESSAGE);

                // Cierra la ventana de Crear_Base_Datos
            }
        });

        // Hacer visible la ventana de Crear_Base_Datos
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        });
    }
}
