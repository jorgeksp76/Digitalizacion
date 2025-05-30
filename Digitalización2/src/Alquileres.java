import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Alquileres extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTextField txtBuscarCliente;
    private JButton btnBuscar;

    public Alquileres() {
        setTitle("Listado de Alquileres");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 752, 400);
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Campo de texto para buscar por cliente
        txtBuscarCliente = new JTextField();
        txtBuscarCliente.setBounds(20, 10, 150, 25);
        contentPane.add(txtBuscarCliente);

        // Botón de búsqueda
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(180, 10, 100, 25);
        contentPane.add(btnBuscar);

        // Crear modelo de tabla con las columnas necesarias
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Vehículo");
        tableModel.addColumn("Marca");
        tableModel.addColumn("Cliente");
        tableModel.addColumn("Empleado");
        tableModel.addColumn("Fecha Inicio");
        tableModel.addColumn("Fecha Fin");
        tableModel.addColumn("Precio Total");
        tableModel.addColumn("Eliminar"); // Columna para el botón de eliminar

        // Crear la tabla con el modelo
        table = new JTable(tableModel);
        table.setBounds(47, 38, 502, 250);

        // Añadir la tabla a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 45, 730, 235); // Ajusta el tamaño del JScrollPane
        contentPane.add(scrollPane);

        JButton btnAtras = new JButton("Atras");
        btnAtras.setBounds(20, 289, 117, 25);
        contentPane.add(btnAtras);

        btnAtras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InicioAdmin inicioAdmin = new InicioAdmin();
                inicioAdmin.setVisible(true);
                dispose();
            }
        });

        // Acción del botón de búsqueda
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombreCliente = txtBuscarCliente.getText().trim();
                buscarAlquileresPorCliente(tableModel, nombreCliente);
            }
        });

        // Cargar todos los alquileres al inicio
        cargarAlquileres(tableModel);
    }

    // Método para cargar los alquileres desde la base de datos
    private void cargarAlquileres(DefaultTableModel tableModel) {
        try (Connection conn = BBDD.getConexion()) {
            // Consulta SQL para obtener los alquileres junto con los detalles del vehículo, cliente y empleado
            String query = "SELECT a.id, a.fecha_inicio, a.fecha_fin, a.precio_total, v.matricula, v.marca, c.nombre AS cliente, e.nombre AS empleado, v.id AS vehiculo_id " +
                           "FROM Alquiler a " +
                           "JOIN Vehiculo v ON a.vehiculo_id = v.id " +
                           "JOIN Cliente c ON a.cliente_dni = c.DNI " +
                           "JOIN Empleado e ON a.empleado_dni = e.DNI";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Limpiar la tabla antes de cargar nuevos datos
            tableModel.setRowCount(0);

            // Recorrer los resultados y añadirlos a la tabla
            while (rs.next()) {
                int alquilerId = rs.getInt("id");
                Date fechaInicio = rs.getDate("fecha_inicio");
                Date fechaFin = rs.getDate("fecha_fin");
                double precioTotal = rs.getDouble("precio_total");
                String matricula = rs.getString("matricula");
                String marca = rs.getString("marca");
                String cliente = rs.getString("cliente");
                String empleado = rs.getString("empleado");
                int vehiculoId = rs.getInt("vehiculo_id");

                // Añadir los datos a la tabla como una nueva fila
                Object[] row = {
                    alquilerId,
                    matricula + " (" + marca + ")", 
                    marca, 
                    cliente,
                    empleado,
                    fechaInicio,
                    fechaFin,
                    precioTotal,
                    "Eliminar", // Columna con el texto "Eliminar"
                    vehiculoId // Incluimos el ID del vehículo para usarlo al eliminar el alquiler
                };
                tableModel.addRow(row);
            }

            // Establecer el renderizador y editor del botón de eliminar
            table.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox(), tableModel));

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los alquileres.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para buscar alquileres por nombre de cliente
    private void buscarAlquileresPorCliente(DefaultTableModel tableModel, String nombreCliente) {
        try (Connection conn = BBDD.getConexion()) {
            // Consulta SQL para obtener los alquileres de un cliente específico
            String query = "SELECT a.id, a.fecha_inicio, a.fecha_fin, a.precio_total, v.matricula, v.marca, c.nombre AS cliente, e.nombre AS empleado, v.id AS vehiculo_id " +
                           "FROM Alquiler a " +
                           "JOIN Vehiculo v ON a.vehiculo_id = v.id " +
                           "JOIN Cliente c ON a.cliente_dni = c.DNI " +
                           "JOIN Empleado e ON a.empleado_dni = e.DNI " +
                           "WHERE c.nombre LIKE ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + nombreCliente + "%"); 
            ResultSet rs = stmt.executeQuery();

            // Limpiar la tabla antes de cargar los nuevos resultados
            tableModel.setRowCount(0);

            // Recorrer los resultados y añadirlos a la tabla
            while (rs.next()) {
                int alquilerId = rs.getInt("id");
                Date fechaInicio = rs.getDate("fecha_inicio");
                Date fechaFin = rs.getDate("fecha_fin");
                double precioTotal = rs.getDouble("precio_total");
                String matricula = rs.getString("matricula");
                String marca = rs.getString("marca");
                String cliente = rs.getString("cliente");
                String empleado = rs.getString("empleado");
                int vehiculoId = rs.getInt("vehiculo_id");

                // Añadir los datos a la tabla como una nueva fila
                Object[] row = {
                    alquilerId,
                    matricula + " (" + marca + ")", 
                    marca, 
                    cliente,
                    empleado,
                    fechaInicio,
                    fechaFin,
                    precioTotal,
                    "Eliminar", // Columna con el texto "Eliminar"
                    vehiculoId // Incluimos el ID del vehículo para usarlo al eliminar el alquiler
                };
                tableModel.addRow(row);
            }

            // Establecer el renderizador y editor del botón de eliminar
            table.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox(), tableModel));

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al buscar los alquileres.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Clase para crear el botón de eliminar en cada celda
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Eliminar");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Clase para manejar la acción de eliminar al hacer clic en el botón
    class ButtonEditor extends DefaultCellEditor {
        private JButton btn;
        private DefaultTableModel tableModel;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel) {
            super(checkBox);
            this.tableModel = tableModel;
            btn = new JButton();
            btn.setText("Eliminar");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    eliminarAlquiler();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            selectedRow = row;
            return btn;
        }

        // Método para eliminar el alquiler de la base de datos y poner el vehículo disponible
        private void eliminarAlquiler() {
            try (Connection conn = BBDD.getConexion()) {
                // Obtener el ID del alquiler y el ID del vehículo seleccionado
                int alquilerId = (int) table.getValueAt(selectedRow, 0); // Índice 0 para el ID del alquiler
                int vehiculoId = (int) table.getValueAt(selectedRow, 8); // Índice 8 para el ID del vehículo (última columna)

                // Eliminar alquiler de la base de datos
                String deleteQuery = "DELETE FROM Alquiler WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(deleteQuery);
                stmt.setInt(1, alquilerId);
                stmt.executeUpdate();

                // Poner el vehículo como disponible
                String updateVehicleQuery = "UPDATE Vehiculo SET disponible = 1 WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateVehicleQuery);
                updateStmt.setInt(1, vehiculoId);
                updateStmt.executeUpdate();

                // Eliminar el alquiler de la tabla
                tableModel.removeRow(selectedRow);

                JOptionPane.showMessageDialog(null, "Alquiler eliminado correctamente. El vehículo ahora está disponible.");

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al eliminar el alquiler.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Alquileres frame = new Alquileres();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
