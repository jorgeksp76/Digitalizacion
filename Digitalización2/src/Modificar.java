import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Modificar extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;

    public Modificar() {
        setTitle("Listado de Vehiculos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 843, 483);
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Crear modelo de tabla
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("MATRICULA");
        tableModel.addColumn("MARCA");
        tableModel.addColumn("MODELO");
        tableModel.addColumn("CATEGORIA");
        tableModel.addColumn("TIPO");
        tableModel.addColumn("PRECIO ALQUILER POR DIA");
        tableModel.addColumn("FECHA ALTA");

        // Crear la tabla
        table = new JTable(tableModel);
        table.setBounds(47, 38, 502, 250);
        table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField())); // permite edición

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 821, 251);
        contentPane.add(scrollPane);

        JButton btnAtras = new JButton("Atras");
        btnAtras.setBounds(28, 273, 117, 25);
        contentPane.add(btnAtras);

        btnAtras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InicioAdmin inicioAdmin = new InicioAdmin();
                inicioAdmin.setVisible(true);
                dispose();
            }
        });
        
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(292, 273, 117, 25);
        contentPane.add(btnEliminar);

        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = table.getSelectedRow();
                if (filaSeleccionada != -1) {
                    int id = (int) table.getValueAt(filaSeleccionada, 0);

                    try (Connection conn = BBDD.getConexion()) {
                        // Comprobar si existen reservas para ese vehículo
                        String checkQuery = "SELECT COUNT(*) FROM Alquiler WHERE vehiculo_id = ?";
                        PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                        checkStmt.setInt(1, id);
                        ResultSet rs = checkStmt.executeQuery();

                        boolean tieneReservas = false;
                        if (rs.next()) {
                            tieneReservas = rs.getInt(1) > 0;
                        }

                        boolean continuar = true;

                        if (tieneReservas) {
                            int opcion = JOptionPane.showConfirmDialog(null,
                                "Este vehículo tiene reservas asociadas, ¿Estás seguro que quieres eliminar el vehículo y las reservas?",
                                "Confirmar eliminación",
                                JOptionPane.YES_NO_OPTION);
                            
                            if (opcion == JOptionPane.NO_OPTION) {
                                continuar = false;
                            }
                        }

                        if (continuar) {
                            // Eliminar reservas si las hay
                            String deleteAlquiler = "DELETE FROM Alquiler WHERE vehiculo_id = ?";
                            PreparedStatement stmtAlquiler = conn.prepareStatement(deleteAlquiler);
                            stmtAlquiler.setInt(1, id);
                            stmtAlquiler.executeUpdate();

                            // Eliminar el vehículo
                            String deleteVehiculo = "DELETE FROM Vehiculo WHERE id = ?";
                            PreparedStatement stmtVehiculo = conn.prepareStatement(deleteVehiculo);
                            stmtVehiculo.setInt(1, id);
                            int filasAfectadas = stmtVehiculo.executeUpdate();

                            if (filasAfectadas > 0) {
                                ((DefaultTableModel) table.getModel()).removeRow(filaSeleccionada);
                                JOptionPane.showMessageDialog(null, "Vehículo eliminado correctamente.");
                            } else {
                                JOptionPane.showMessageDialog(null, "No se pudo eliminar el vehículo.");
                            }
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al eliminar el vehículo.");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Por favor selecciona una fila para eliminar.");
                }
            }
        });

        // Botón Editar
        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(160, 273, 117, 25);
        contentPane.add(btnEditar);
        
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(421, 273, 117, 25);
        contentPane.add(btnAgregar);
        
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarVehiculo agregarVehiculo = new AgregarVehiculo();
                agregarVehiculo.setVisible(true);
                dispose();
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = table.getSelectedRow();
                if (filaSeleccionada != -1) {
                    try (Connection conn = BBDD.getConexion()) {
                        // Obtener valores de la fila
                        int id = (int) table.getValueAt(filaSeleccionada, 0);
                        String matricula = table.getValueAt(filaSeleccionada, 1).toString();
                        String marca = table.getValueAt(filaSeleccionada, 2).toString();
                        String modelo = table.getValueAt(filaSeleccionada, 3).toString();
                        String categoria = table.getValueAt(filaSeleccionada, 4).toString();
                        String tipo = table.getValueAt(filaSeleccionada, 5).toString();
                        String precio = table.getValueAt(filaSeleccionada, 6).toString();
                        String fechaAlta = table.getValueAt(filaSeleccionada, 7).toString();

                        // Validación
                        if (matricula.isEmpty() || marca.isEmpty() || modelo.isEmpty() || precio.isEmpty() || fechaAlta.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Por favor llena todos los campos.");
                            return;
                        }

                        // Actualizar en la base de datos
                        String query = "UPDATE Vehiculo SET matricula=?, marca=?, modelo=?, categoria=?, tipo=?, precio_alquiler_dia=?, fecha_alta=? WHERE id=?";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, matricula);
                        stmt.setString(2, marca);
                        stmt.setString(3, modelo);
                        stmt.setString(4, categoria);
                        stmt.setString(5, tipo);
                        stmt.setString(6, precio);
                        stmt.setDate(7, Date.valueOf(fechaAlta));  // Convertir String a Date
                        stmt.setInt(8, id); // Cambiar a 8 porque hay 8 parámetros

                        int filasActualizadas = stmt.executeUpdate();

                        if (filasActualizadas > 0) {
                            JOptionPane.showMessageDialog(null, "Registro actualizado correctamente.");
                            cargarVehiculos(tableModel); // Recargar la tabla después de la actualización
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo actualizar el registro.");
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al actualizar el vehiculo.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Verifica los datos ingresados.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor selecciona una fila para editar.");
                }
            }
        });

        // Cargar datos
        cargarVehiculos(tableModel);
    }

    private void cargarVehiculos(DefaultTableModel tableModel) {
        try (Connection conn = BBDD.getConexion()) {
            String query = "SELECT id, matricula, marca, modelo, categoria, tipo, precio_alquiler_dia, fecha_alta FROM Vehiculo";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            tableModel.setRowCount(0); // Limpiar la tabla antes de agregar nuevos datos

            while (rs.next()) {
                // Imprimir el resultado de cada fila del ResultSet
                System.out.println("ID: " + rs.getInt("id") + ", Matricula: " + rs.getString("matricula"));
                
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("matricula"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("categoria"),
                    rs.getString("tipo"),
                    rs.getString("precio_alquiler_dia"),
                    rs.getDate("fecha_alta")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los vehiculos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Modificar frame = new Modificar();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
