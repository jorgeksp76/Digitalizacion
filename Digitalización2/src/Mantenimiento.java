import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Mantenimiento extends JFrame {

    private static final long serialVersionUID = 1L;
    private JComboBox<String> comboVehiculos;  // Cambié el nombre aquí
    private JPanel contentPane;
    private JTextField txtFecha;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Mantenimiento frame = new Mantenimiento();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Mantenimiento() {
        setTitle("Mantenimiento");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 576, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(12, 12, 552, 239);
        contentPane.add(panel);
        panel.setLayout(null);
        
        JLabel lblVehiculo = new JLabel("Vehículo:");
        lblVehiculo.setBounds(109, 42, 70, 15);
        panel.add(lblVehiculo);
        
        // Asigné comboVehiculos correctamente
        comboVehiculos = new JComboBox<>();  // Inicializamos el comboVehiculos
        comboVehiculos.setBounds(190, 37, 264, 24);
        panel.add(comboVehiculos);
        
        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        lblFecha.setBounds(25, 88, 154, 15);
        panel.add(lblFecha);
        
        txtFecha = new JTextField();
        txtFecha.setBounds(190, 77, 264, 37);
        panel.add(txtFecha);
        txtFecha.setColumns(10);
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(246, 153, 117, 25);
        panel.add(btnGuardar);
        
        JButton btnAtras = new JButton("Atras");
        btnAtras.setBounds(25, 202, 117, 25);
        panel.add(btnAtras);
        
        JButton btnVerMantenimientos = new JButton("Ver Mantenimientos");
        btnVerMantenimientos.setBounds(25, 153, 177, 25);
        panel.add(btnVerMantenimientos);
        
        btnAtras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InicioAdmin inicioAdmin = new InicioAdmin();
                inicioAdmin.setVisible(true);
                dispose(); // Opcional: cerrar la ventana actual
            }
        });
        
        btnVerMantenimientos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListadoMantenimientos listadoMantenimientos = new ListadoMantenimientos();
                listadoMantenimientos.setVisible(true);
                dispose(); // Opcional: cerrar la ventana actual
            }
        });

        // Llamamos a cargarDatos para llenar el JComboBox con los datos
        cargarDatos();

        // Acción al hacer clic en el botón "Guardar"
        btnGuardar.addActionListener(e -> guardarMantenimiento());
    }
    
    private void cargarDatos() {
        try (Connection conn = BBDD.getConexion()) {
            Statement datos = conn.createStatement();

            // Aquí estamos obteniendo los datos de la tabla Vehiculo
            ResultSet rs = datos.executeQuery("SELECT id, matricula, marca, modelo FROM Vehiculo");
            while (rs.next()) {
                // Agregamos los items al JComboBox comboVehiculos
                comboVehiculos.addItem(rs.getInt("id") + " - " + rs.getString("matricula") + " - " + rs.getString("marca") + " - " + rs.getString("modelo"));
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar datos.");
        }
    }

    // Método para guardar el mantenimiento en la base de datos
    private void guardarMantenimiento() {
        try (Connection conn = BBDD.getConexion()) {
            // Obtener los datos del formulario
            String vehiculoId = comboVehiculos.getSelectedItem().toString().split(" - ")[0]; // Obtener el ID del vehículo
            String fecha = txtFecha.getText(); // Obtener la fecha del JTextField

            // Validar la fecha (opcional)
            if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Usa YYYY-MM-DD.");
                return;
            }

            // Insertar el mantenimiento en la base de datos
            String query = "INSERT INTO Mantenimiento (fecha, vehiculo_id) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, fecha); // Fecha
            stmt.setInt(2, Integer.parseInt(vehiculoId)); // ID del vehículo

            // Ejecutar la inserción
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Mantenimiento guardado correctamente.");
                
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el mantenimiento.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar el mantenimiento.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
