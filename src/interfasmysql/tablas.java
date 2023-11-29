package interfasmysql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import packeteria.Paqueteria;

public class tablas extends JPanel {
    private JComboBox<String> bdComboBox;
    private JTextField nombreTablaTextField;
    private JSpinner columnasSpinner;
    private JButton crearTablaButton;

    public tablas() {
        setBackground(new Color(230, 204, 255));

        // Ajustar el tamaño de la fuente y los componentes al 200%
        Font originalFont = getFont();
        float originalSize = originalFont.getSize();
        float newSize = originalSize * 2.0f;
        setFont(originalFont.deriveFont(newSize));

        // Configuración del panel "Administrar Tablas"
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // ComboBox para seleccionar una base de datos
        JLabel bdLabel = new JLabel("Seleccionar Base de Datos:");
        bdLabel.setFont(bdLabel.getFont().deriveFont(newSize));
        bdComboBox = new JComboBox<>();
        bdComboBox.setFont(bdComboBox.getFont().deriveFont(newSize));

        // Obtén la lista de bases de datos desde la clase Paqueteria
        Paqueteria paqueteria = new Paqueteria("localhost", "root", "1234", 3306);
        DefaultComboBoxModel<String> bdComboBoxModel = new DefaultComboBoxModel<>();
        for (String bd : paqueteria.listarBasesDeDatos()) {
            bdComboBoxModel.addElement(bd);
        }
        bdComboBox.setModel(bdComboBoxModel);

        // TextField para ingresar el nombre de la tabla
        JLabel nombreTablaLabel = new JLabel("Nombre de la Tabla:");
        nombreTablaLabel.setFont(nombreTablaLabel.getFont().deriveFont(newSize));
        nombreTablaTextField = new JTextField(15);
        nombreTablaTextField.setFont(nombreTablaTextField.getFont().deriveFont(newSize));
        nombreTablaTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));

        // Spinner para seleccionar la cantidad de columnas
        JLabel columnasLabel = new JLabel("Cantidad de Columnas:");
        columnasLabel.setFont(columnasLabel.getFont().deriveFont(newSize));
        columnasSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 15, 1));
        columnasSpinner.setFont(columnasSpinner.getFont().deriveFont(newSize));

        // Botón "Crear"
        crearTablaButton = new JButton("Crear");
        crearTablaButton.setFont(crearTablaButton.getFont().deriveFont(newSize));

        // Agregar componentes al panel con GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(bdLabel, gbc);

        gbc.gridx = 1;
        add(bdComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(nombreTablaLabel, gbc);

        gbc.gridx = 1;
        add(nombreTablaTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(columnasLabel, gbc);

        gbc.gridx = 1;
        add(columnasSpinner, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(crearTablaButton, gbc);

        // Añadir espacio
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        add(Box.createVerticalStrut(20), gbc);

      
        // ActionListener para el botón "Crear"
        crearTablaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                crearTabla();
            }
        });
    }

    private void crearTabla() {
        int cantidadColumnas = (int) columnasSpinner.getValue();
        String nombreTabla = nombreTablaTextField.getText();

        if (nombreTabla.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes ingresar un nombre para la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si la tabla ya existe en la base de datos
        if (tablaExiste(nombreTabla)) {
            JOptionPane.showMessageDialog(this, "Ya existe una tabla con el nombre '" + nombreTabla + "'.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener los valores seleccionados por el usuario
        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE ");
        createTableSQL.append(nombreTabla).append(" (");

        for (int i = 0; i < cantidadColumnas; i++) {
            if (i > 0) {
                createTableSQL.append(", ");
            }

            createTableSQL.append(getColumnDefinition(i));
        }

        createTableSQL.append(");");

        // Crear la tabla en la base de datos
        String baseDeDatos = bdComboBox.getSelectedItem().toString();
        Paqueteria paqueteria = new Paqueteria("localhost", "root", "1234", 3306);
        Connection conexion = paqueteria.getConexion();

        if (conexion != null) {
            try {
                Statement statement = conexion.createStatement();
                statement.executeUpdate("USE " + baseDeDatos); // Seleccionar la base de datos
                statement.executeUpdate(createTableSQL.toString());

                // Muestra un mensaje emergente con el resultado
                String mensaje = "La tabla '" + nombreTabla + "' ha sido creada en la base de datos '" + baseDeDatos + "'.";
                JOptionPane.showMessageDialog(this, mensaje, "Tabla Creada", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al crear la tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                paqueteria.desconectar(conexion);
            }
        }
    }

    private boolean tablaExiste(String nombreTabla) {
        // Verificar si la tabla ya existe en la base de datos
        String baseDeDatos = bdComboBox.getSelectedItem().toString();
        Paqueteria paqueteria = new Paqueteria("localhost", "root", "1234", 3306);
        Connection conexion = paqueteria.getConexion();

        if (conexion != null) {
            try {
                Statement statement = conexion.createStatement();
                statement.executeUpdate("USE " + baseDeDatos); // Seleccionar la base de datos

                // Consulta para verificar si la tabla existe
                String consulta = "SELECT table_name FROM information_schema.tables WHERE table_schema = '" + baseDeDatos + "' AND table_name = '" + nombreTabla + "'";
                return statement.executeQuery(consulta).next();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                paqueteria.desconectar(conexion);
            }
        }

        return false;
    }

    private String getColumnDefinition(int columnNumber) {
        String nombreColumna = JOptionPane.showInputDialog(this, "Nombre de la Columna " + (columnNumber + 1) + ":");
        String tipoDato = (String) JOptionPane.showInputDialog(this, "Tipo de Dato Columna " + (columnNumber + 1) + ":", "Seleccionar Tipo de Dato",
                JOptionPane.QUESTION_MESSAGE, null, new String[]{"INT", "VARCHAR", "FLOAT", "DATE"}, "INT");

        // Asegúrate de que el nombre de la columna no esté vacío
        if (nombreColumna.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes ingresar un nombre para la columna.", "Error", JOptionPane.ERROR_MESSAGE);
            return getColumnDefinition(columnNumber); // Vuelve a pedir el nombre de la columna
        }

        if ("VARCHAR".equals(tipoDato)) {
            String longitud = JOptionPane.showInputDialog(this, "Longitud para VARCHAR (por defecto 20):");
            if (longitud.isEmpty()) {
                longitud = "20";  // Valor por defecto si no se ingresa nada
            }
            return nombreColumna + " " + tipoDato + "(" + longitud + ")";
        } else {
            return nombreColumna + " " + tipoDato;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tablas Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new tablas());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
