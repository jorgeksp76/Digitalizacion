import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ListadoMantenimientos extends JFrame {

    private JPanel contentPane;
    private JTable tableMantenimientos;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ListadoMantenimientos frame = new ListadoMantenimientos();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ListadoMantenimientos() {
        setTitle("Listado de Mantenimientos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        // Crear la tabla y el modelo
        String[] columnNames = {"ID", "Fecha", "Vehículo"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableMantenimientos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableMantenimientos);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Crear botón para cerrar la ventana
        JButton btnAtras = new JButton("Atras");
        btnAtras.addActionListener(e -> dispose());
        JPanel panel = new JPanel();
        panel.add(btnAtras);
        contentPane.add(panel, BorderLayout.SOUTH);
        
        btnAtras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Mantenimiento mantenimiento = new Mantenimiento();
                mantenimiento.setVisible(true);
                dispose(); // Opcional: cerrar la ventana actual
            }
        });

        // Cargar los mantenimientos desde la base de datos
        cargarMantenimientos();
    }
    
    

    private void cargarMantenimientos() {
        try (Connection conn = BBDD.getConexion()) {
            Statement stmt = conn.createStatement();
            String query = "SELECT m.id, m.fecha, v.matricula, v.marca, v.modelo " +
                           "FROM Mantenimiento m " +
                           "JOIN Vehiculo v ON m.vehiculo_id = v.id";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String fecha = rs.getString("fecha");
                String vehiculo = rs.getString("matricula") + " - " + rs.getString("marca") + " - " + rs.getString("modelo");
                
                // Añadir una fila al modelo de la tabla
                tableModel.addRow(new Object[]{id, fecha, vehiculo});
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los mantenimientos.");
        }
    }
}
