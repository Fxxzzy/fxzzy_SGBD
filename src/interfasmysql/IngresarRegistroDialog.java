package interfasmysql;

import packeteria.Paqueteria;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IngresarRegistroDialog extends JDialog {
    private String nombreBd;
    private String nombreTabla;
    private JTextField[] camposDeTexto;
    private JButton insertarButton;
    private Paqueteria paqueteria;

    public IngresarRegistroDialog(Frame parent, String nombreBd, String nombreTabla) {
        
        super(parent, "Ingresar Registro", true);
         setBackground(new Color(230, 204, 255));
        this.nombreBd = nombreBd;
        this.nombreTabla = nombreTabla;
        this.paqueteria = new Paqueteria("localhost", "root", "1234", 3306); // Ajusta estos valores
 setBackground(new Color(230, 204, 255));
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));

        // Obtén los nombres de las columnas de la tabla y crea campos de texto correspondientes
        String[] nombresColumnas = paqueteria.obtenerNombresColumnas(nombreBd, nombreTabla);
        camposDeTexto = new JTextField[nombresColumnas.length];

        for (int i = 0; i < nombresColumnas.length; i++) {
            JLabel label = new JLabel(nombresColumnas[i]);
            camposDeTexto[i] = new JTextField(20);
            panel.add(label);
            panel.add(camposDeTexto[i]);
        }

        insertarButton = new JButton("Insertar Registro");
        panel.add(new JLabel());
        panel.add(insertarButton);

        insertarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertarRegistro();
            }
        });

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    private void insertarRegistro() {
        // Obtén los valores ingresados en los campos de texto
        String[] valores = new String[camposDeTexto.length];
        for (int i = 0; i < camposDeTexto.length; i++) {
            valores[i] = camposDeTexto[i].getText();
        }

        // Insertar el registro en la tabla
        boolean exitoso = paqueteria.insertarRegistro(nombreBd, nombreTabla, valores);

        if (exitoso) {
            JOptionPane.showMessageDialog(this, "Registro insertado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al insertar registro.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
