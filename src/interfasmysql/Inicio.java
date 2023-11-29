package interfasmysql;

import packeteria.Paqueteria;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Inicio extends JFrame {

    private DefaultListModel<String> listModel;
    protected JList<String> listaBD;
    private Conectar conectarPanel;
    private consultas consultasBD;
    private Crear_Base_Datos crearBD;
    private Bases_Datos_desc descripcionBD;
    private CardLayout cardLayout;
    private ImageIcon inicioImage; // Imagen para la pantalla de inicio
    private JPanel panelDerecho;
    private boolean isConnected = false;

    public Inicio() {
        panelDerecho = new JPanel();
        setTitle("Mi propio MYSQL By:Fxzzy");
         JTextField holaMundoLabel = new JTextField("Sistema Gestor de Base de Datos");
        holaMundoLabel.setHorizontalAlignment(SwingConstants.CENTER); // Alinea el texto al centro
        holaMundoLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Cambiar la fuente y el tamaño según tus preferencias
        holaMundoLabel.setForeground(Color.BLACK); // Cambiar el color del texto si es necesario
        holaMundoLabel.setBackground(new Color(204, 153, 204));
        holaMundoLabel.setEditable(false);

        // Agregar el JLabel al contenedor principal
        add(holaMundoLabel, BorderLayout.NORTH);

        setExtendedState(JFrame.MAXIMIZED_BOTH); // Establecer el estado de la ventana a pantalla completa
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setBackground(new Color(204, 153, 204)); // Azul acero
        panelDerecho.setBackground(new Color(204, 153, 204)); // Azul pálido

        panelDerecho.setOpaque(false);
        panelDerecho.setBackground(new Color(204, 153, 204));
        panelDerecho.setLayout(new BorderLayout());
        UIManager.put("Button.background", new Color(204, 153, 204)); // Azul acero oscuro
        UIManager.put("Button.foreground", Color.BLACK);
        Font customFont = new Font("Arial", Font.PLAIN, 14); // Puedes cambiar la fuente según tus preferencias
        UIManager.put("Button.font", customFont);
        UIManager.put("Label.font", customFont);
        UIManager.put("TextPane.font", customFont);

        listModel = new DefaultListModel<>();
        listaBD = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(listaBD);
        scrollPane.setPreferredSize(new Dimension(230, 500));

        panelIzquierdo.add(scrollPane);
        cardLayout = new CardLayout();
        panelDerecho.setPreferredSize(new Dimension(600, 600));
        panelDerecho.setOpaque(false);
        panelDerecho.setBackground(new Color(204, 153, 204));
        conectarPanel = new Conectar(this);
        crearBD = new Crear_Base_Datos(this);
        descripcionBD = new Bases_Datos_desc(this);
        descripcionBD.setLocation(0, 0);
        consultasBD = new consultas(this);
        panelDerecho.setLayout(cardLayout);
        panelDerecho.add(conectarPanel, "ConectarPanel");
        panelDerecho.add(crearBD, "Crear_Base_DatosPanel");
        panelDerecho.add(descripcionBD, "Bases_Datos_descPanel");
        panelDerecho.add(consultasBD, "Consultas");

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("logo.png"));
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        inicioImage = resizedIcon;

        JPanel inicioPanel = new JPanel();
        inicioPanel.setLayout(new BorderLayout());
        JLabel imageLabel = new JLabel(inicioImage);
        inicioPanel.setBackground(new Color(230, 204, 255));
        inicioPanel.add(imageLabel, BorderLayout.CENTER);

        // Agregar el nuevo panel al panelDerecho existente (en lugar de crear una nueva instancia)
        panelDerecho.add(inicioPanel, "Inicio");
        // Crear un nuevo panel para mostrar la imagen

        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("DB.png"));
            Image xd = icono.getImage();
            Image resizedImage2 = xd.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon2 = new ImageIcon(resizedImage2);

            // Configurar el ícono del JFrame (usando esta instancia real de Inicio)
            setIconImage(resizedImage2);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configurar el texto en el panel derecho
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setText("<html><p style='font-size: 18px; color: black; text-align: center;'>Bienvenido a esta aplicación MySQL.</p></html>");
        textPane.setEditable(false);
        panelDerecho.add(textPane);
        panelDerecho.setBackground(new Color(204, 153, 204));

        cardLayout.show(panelDerecho, "Inicio"); // Mostrar el panel "Inicio" al inicio

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opciones");

        JMenuItem inicio = new JMenuItem("Inicio");
        inicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelDerecho, "Inicio");
            }
        });
        menu.add(inicio);

        JMenuItem conectarMenuItem = new JMenuItem("Conectar");
        conectarMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("ConectarMenuItem ActionListener ejecutado."); // Mensaje de depuración
                cardLayout.show(panelDerecho, "ConectarPanel"); // Mostrar el panel Conectar
            }
        });
        menu.add(conectarMenuItem);

        JMenuItem crearBdMenuItem = new JMenuItem("Crear Base de Datos");
        crearBdMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isConnected) {
                    cardLayout.show(panelDerecho, "Crear_Base_DatosPanel"); // Mostrar el panel Crear_Base_DatosPanel solo si está conectado
                } else {
                    JOptionPane.showMessageDialog(Inicio.this, "Debe conectar a la base de datos primero.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        menu.add(crearBdMenuItem);
JMenuItem crearTablasMenuItem = new JMenuItem("Crear Tablas");
crearTablasMenuItem.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        // Crear una instancia de la clase 'tablas'
        tablas tablasPanelInstance = new tablas();

        // Agregar la instancia al panelDerecho
        panelDerecho.add(tablasPanelInstance, "TablasPanel");

        // Mostrar la instancia
        cardLayout.show(panelDerecho, "TablasPanel");
    }
});



        JMenuItem basesDeDatosMenuItem = new JMenuItem("Bases de Datos");
        basesDeDatosMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isConnected) {
                    cardLayout.show(panelDerecho, "Bases_Datos_descPanel"); // Mostrar el panel Bases_Datos_descPanel solo si está conectado
                } else {
                    JOptionPane.showMessageDialog(Inicio.this, "Debe conectar a la base de datos primero.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        menu.add(basesDeDatosMenuItem);
        menu.add(crearTablasMenuItem);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        JMenuItem consultasMenuItem = new JMenuItem("Consultas");
        consultasMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isConnected) {
                    cardLayout.show(panelDerecho, "Consultas"); // Mostrar el panel Consultas solo si está conectado
                } else {
                    JOptionPane.showMessageDialog(Inicio.this, "Debe conectar a la base de datos primero.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        listaBD.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedDb = getSelectedDatabase();
                    consultasBD.onDatabaseSelected(selectedDb);
                }
            }
        });

        menu.add(consultasMenuItem);

        add(panelIzquierdo, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);
        listaBD.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedDb = getSelectedDatabase();
                    // Llama al método onDatabaseSelected en Bases_Datos_desc para cargar las tablas y habilitar los botones
                    descripcionBD.onDatabaseSelected(selectedDb);
                }
            }
        });

    }

    public JPanel getPanelDerecho() {
        return panelDerecho;
    }

    public void cargarBasesDeDatos(Paqueteria paqueteria) {
        listModel.clear();

        List<String> basesDeDatos = paqueteria.listarBasesDeDatos();

        for (String nombreBd : basesDeDatos) {
            listModel.addElement(nombreBd);
        }

        // Establecer el estado de conexión
        isConnected = true;
    }

    public String getSelectedDatabase() {
        return listaBD.getSelectedValue();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Inicio app = new Inicio();
            app.setVisible(true);
        });
    }
}
