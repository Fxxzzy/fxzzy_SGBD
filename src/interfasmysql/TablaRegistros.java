package interfasmysql;

import packeteria.Paqueteria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.JTableHeader;

public class TablaRegistros extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    private Paqueteria paqueteria;
    private String nombreBd;
    private String nombreTabla;
    private JButton agregarRegistroButton;
    private JButton eliminarRegistroButton;
    private JButton actualizarRegistroButton;
    private JLabel consultaLabel;

   public TablaRegistros(String nombreBd, String nombreTabla) {
        this.nombreBd = nombreBd;
        this.nombreTabla = nombreTabla;

        setTitle("Registros de " + nombreTabla);
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        paqueteria = new Paqueteria("localhost", "root", "1234", 3306);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(204, 153, 204)); // Fondo azul oscuro

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        agregarRegistroButton = new JButton("Agregar Registro");
        eliminarRegistroButton = new JButton("Eliminar Registro");
        actualizarRegistroButton = new JButton("Actualizar Registro");
        agregarRegistroButton.setForeground(Color.black);
eliminarRegistroButton.setForeground(Color.BLACK);
actualizarRegistroButton.setForeground(Color.BLACK);

        consultaLabel = new JLabel("Consulta: ");

        consultaLabel.setFont(new Font("Arial", Font.BOLD, 20));
        consultaLabel.setForeground(new Color(0, 0, 0));

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1));
        buttonsPanel.add(agregarRegistroButton);
        buttonsPanel.add(eliminarRegistroButton);
        buttonsPanel.add(actualizarRegistroButton);

        panel.add(buttonsPanel, BorderLayout.SOUTH);
        panel.add(consultaLabel, BorderLayout.NORTH);

        // Configurar color del encabezado de la tabla
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(204, 153, 204)); // Azul real más oscuro
        header.setForeground(Color.white);

        agregarRegistroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarNuevoRegistro();
            }
        });

        eliminarRegistroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarRegistroSeleccionado();
            }
        });

        actualizarRegistroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarRegistroSeleccionado();
            }
        });

        add(panel);

        String[] columnNames = paqueteria.obtenerNombresColumnas(nombreBd, nombreTabla);
        for (String columnName : columnNames) {
            tableModel.addColumn(columnName);
        }

        cargarDatos();

        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                table.setColumnSelectionAllowed(true);
                table.setRowSelectionAllowed(false);
                table.setColumnSelectionInterval(col, col);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    mostrarMenuContextual(e);
                }
            }
        });
    }

    private void agregarNuevoRegistro() {
        try {
            String[] columnNames = paqueteria.obtenerNombresColumnas(nombreBd, nombreTabla);
            String[] nuevoRegistro = new String[columnNames.length];

            for (int i = 0; i < columnNames.length; i++) {
                String valor = JOptionPane.showInputDialog("Ingrese el valor para " + columnNames[i]);
                nuevoRegistro[i] = valor;
            }

            boolean exitoso = paqueteria.insertarRegistro(nombreBd, nombreTabla, nuevoRegistro);

            if (exitoso) {
                tableModel.addRow(nuevoRegistro);
                consultaLabel.setText("Consulta: INSERT INTO " + nombreTabla + " VALUES (" + String.join(", ", nuevoRegistro) + ")");
            } else {
                JOptionPane.showMessageDialog(this, "Error al insertar el nuevo registro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al ingresar datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRegistroSeleccionado() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int modelRow = table.convertRowIndexToModel(selectedRow);
                String[] rowData = new String[tableModel.getColumnCount()];
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    rowData[col] = (String) tableModel.getValueAt(modelRow, col);
                }
                boolean exitoso = paqueteria.eliminarRegistro(nombreBd, nombreTabla, rowData);
                if (exitoso) {
                    tableModel.removeRow(modelRow);
                    consultaLabel.setText("Consulta: " + construirConsultaEliminar(rowData));
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un registro para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarRegistroSeleccionado() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int modelRow = table.convertRowIndexToModel(selectedRow);
                String[] rowData = new String[tableModel.getColumnCount()];
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    rowData[col] = (String) tableModel.getValueAt(modelRow, col);
                }

                String[] columnNames = paqueteria.obtenerNombresColumnas(nombreBd, nombreTabla);
                String[] newValues = new String[columnNames.length];
                for (int i = 0; i < columnNames.length; i++) {
                    String newValue = JOptionPane.showInputDialog("Ingrese el nuevo valor para " + columnNames[i]);
                    newValues[i] = newValue;
                }

                String condition = columnNames[0] + " = '" + rowData[0] + "'";
                boolean exitoso = paqueteria.actualizarRegistro(nombreBd, nombreTabla, columnNames, newValues, condition);

                if (exitoso) {
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        tableModel.setValueAt(newValues[col], modelRow, col);
                    }
                    consultaLabel.setText("Consulta: " + construirConsultaActualizar(rowData, newValues, condition));
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un registro para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String construirConsultaEliminar(String[] rowData) {
        StringBuilder deleteQuery = new StringBuilder("DELETE FROM ");
        deleteQuery.append(nombreBd).append(".").append(nombreTabla).append(" WHERE ");

        String[] columnNames = paqueteria.obtenerNombresColumnas(nombreBd, nombreTabla);

        for (int i = 0; i < columnNames.length; i++) {
            deleteQuery.append(columnNames[i]).append(" = '").append(rowData[i]).append("'");
            if (i < columnNames.length - 1) {
                deleteQuery.append(" AND ");
            }
        }

        return deleteQuery.toString();
    }

    private String construirConsultaActualizar(String[] oldValues, String[] newValues, String condition) {
        return "UPDATE " + nombreTabla + " SET " + construirSetClause(oldValues, newValues) + " WHERE " + condition;
    }

    private String construirSetClause(String[] oldValues, String[] newValues) {
        StringBuilder setClause = new StringBuilder();
        for (int i = 0; i < oldValues.length; i++) {
            setClause.append(oldValues[i]).append(" = '").append(newValues[i]).append("'");
            if (i < oldValues.length - 1) {
                setClause.append(", ");
            }
        }
        return setClause.toString();
    }

    private void cargarDatos() {
        try {
            List<String[]> registros = paqueteria.obtenerRegistros(nombreBd, nombreTabla);
            for (String[] registro : registros) {
                tableModel.addRow(registro);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarMenuContextual(MouseEvent e) {
        // Puedes implementar la lógica del menú contextual aquí si es necesario
    }
     private void configurarBoton(JButton boton) {
        boton.setBorder(new CompoundBorder(
                new LineBorder(Color.BLACK), // Borde negro
                new EmptyBorder(10, 20, 10, 20) // Espacio interno entre el texto y el borde
        ));
        boton.setUI(new BasicButtonUI() {
            @Override
            protected void paintButtonPressed(Graphics g, AbstractButton b) {
                g.setColor(b.getBackground().darker());
                g.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 10, 10);
            }
        });
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TablaRegistros ventana = new TablaRegistros("NombreBaseDeDatos", "NombreTabla");
            ventana.setVisible(true);
        });
    }
}