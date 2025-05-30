import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AltaUsuario extends JFrame {
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JComboBox<String> comboVehiculos;
    private JComboBox<String> comboClientes;
    private JComboBox<String> comboEmpleados;
    private String fechaInicio;
    private String fechaFin;

    public AltaUsuario(String fechaInicio, String fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;

        setTitle("Alta Usuario");
        setBounds(100, 100, 600, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblFechaInicio = new JLabel("Fecha inicio:");
        lblFechaInicio.setBounds(30, 30, 100, 25);
        getContentPane().add(lblFechaInicio);

        txtFechaInicio = new JTextField(fechaInicio);
        txtFechaInicio.setBounds(140, 30, 150, 25);
        txtFechaInicio.setEditable(false);
        getContentPane().add(txtFechaInicio);

        JLabel lblFechaFin = new JLabel("Fecha fin:");
        lblFechaFin.setBounds(30, 70, 100, 25);
        getContentPane().add(lblFechaFin);

        txtFechaFin = new JTextField(fechaFin);
        txtFechaFin.setBounds(140, 70, 150, 25);
        txtFechaFin.setEditable(false);
        getContentPane().add(txtFechaFin);

        JLabel lblVehiculo = new JLabel("Vehículo:");
        lblVehiculo.setBounds(30, 110, 100, 25);
        getContentPane().add(lblVehiculo);

        comboVehiculos = new JComboBox<>();
        comboVehiculos.setBounds(140, 110, 400, 25);
        getContentPane().add(comboVehiculos);

        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setBounds(30, 150, 100, 25);
        getContentPane().add(lblCliente);

        comboClientes = new JComboBox<>();
        comboClientes.setBounds(140, 150, 400, 25);
        getContentPane().add(comboClientes);

        JLabel lblEmpleado = new JLabel("Empleado:");
        lblEmpleado.setBounds(30, 190, 100, 25);
        getContentPane().add(lblEmpleado);

        comboEmpleados = new JComboBox<>();
        comboEmpleados.setBounds(140, 190, 400, 25);
        getContentPane().add(comboEmpleados);

        JButton btnRegistrar = new JButton("Registrar Alquiler");
        btnRegistrar.setBounds(200, 240, 180, 30);
        getContentPane().add(btnRegistrar);

        JButton btnAtras = new JButton("Atras");
        btnAtras.setBounds(33, 264, 117, 25);
        getContentPane().add(btnAtras);

        btnAtras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DisponibilidadAdmin disponibilidadAdmin = new DisponibilidadAdmin();
                disponibilidadAdmin.setVisible(true);
                dispose();
            }
        });

        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarAlquiler();
            }
        });

        cargarVehiculosDisponibles();
        cargarClientes();
        cargarEmpleados();
    }

    private void cargarVehiculosDisponibles() {
        try (Connection conn = BBDD.getConexion()) {
            String sql = "SELECT * FROM Vehiculo v WHERE v.disponible = 1 AND v.id NOT IN (" +
                    "SELECT a.vehiculo_id FROM Alquiler a WHERE " +
                    "(a.fecha_inicio <= ? AND a.fecha_fin >= ?) OR " +
                    "(a.fecha_inicio <= ? AND a.fecha_fin >= ?) OR " +
                    "(a.fecha_inicio >= ? AND a.fecha_fin <= ?))";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, fechaFin);
            stmt.setString(2, fechaInicio);
            stmt.setString(3, fechaInicio);
            stmt.setString(4, fechaFin);
            stmt.setString(5, fechaInicio);
            stmt.setString(6, fechaFin);

            ResultSet rs = stmt.executeQuery();
            boolean hayVehiculos = false;

            while (rs.next()) {
                hayVehiculos = true;
                int id = rs.getInt("id");
                String matricula = rs.getString("matricula");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                comboVehiculos.addItem(id + " - " + matricula + " (" + marca + " " + modelo + ")");
            }

            if (!hayVehiculos) {
                comboVehiculos.addItem("No hay vehículos disponibles");
                comboVehiculos.setEnabled(false);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar vehículos.");
        }
    }

    private void cargarClientes() {
        try (Connection conn = BBDD.getConexion()) {
            String sql = "SELECT DNI, nombre FROM Cliente";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String dni = rs.getString("DNI");
                String nombre = rs.getString("nombre");
                comboClientes.addItem(dni + " - " + nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar clientes.");
        }
    }

    private void cargarEmpleados() {
        try (Connection conn = BBDD.getConexion()) {
            String sql = "SELECT DNI, nombre FROM Empleado";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String dni = rs.getString("DNI");
                String nombre = rs.getString("nombre");
                comboEmpleados.addItem(dni + " - " + nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar empleados.");
        }
    }

    private void registrarAlquiler() {
        if (!comboVehiculos.isEnabled()) {
            JOptionPane.showMessageDialog(this, "No hay vehículo disponible.");
            return;
        }

        try (Connection conn = BBDD.getConexion()) {
            conn.setAutoCommit(false);

            int vehiculoId = Integer.parseInt(comboVehiculos.getSelectedItem().toString().split(" - ")[0]);
            String clienteDNI = comboClientes.getSelectedItem().toString().split(" - ")[0];
            String empleadoDNI = comboEmpleados.getSelectedItem().toString().split(" - ")[0];

            java.sql.Date inicio = java.sql.Date.valueOf(fechaInicio);
            java.sql.Date fin = java.sql.Date.valueOf(fechaFin);
            long dias = (fin.getTime() - inicio.getTime()) / (1000 * 60 * 60 * 24) + 1;

            PreparedStatement stmtPrecio = conn.prepareStatement("SELECT precio_alquiler_dia FROM Vehiculo WHERE id = ?");
            stmtPrecio.setInt(1, vehiculoId);
            ResultSet rsPrecio = stmtPrecio.executeQuery();

            if (!rsPrecio.next()) {
                JOptionPane.showMessageDialog(this, "Error al obtener precio del vehículo.");
                return;
            }

            double precioDia = rsPrecio.getDouble("precio_alquiler_dia");
            double precioTotal = dias * precioDia;

            Statement stmtId = conn.createStatement();
            ResultSet rsId = stmtId.executeQuery("SELECT MAX(id) + 1 AS nuevoId FROM Alquiler");
            int nuevoId = 1;
            if (rsId.next() && rsId.getInt("nuevoId") > 0) {
                nuevoId = rsId.getInt("nuevoId");
            }

            PreparedStatement stmtInsert = conn.prepareStatement(
                "INSERT INTO Alquiler (id, fecha_inicio, fecha_fin, precio_total, vehiculo_id, cliente_dni, empleado_dni) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            stmtInsert.setInt(1, nuevoId);
            stmtInsert.setDate(2, inicio);
            stmtInsert.setDate(3, fin);
            stmtInsert.setDouble(4, precioTotal);
            stmtInsert.setInt(5, vehiculoId);
            stmtInsert.setString(6, clienteDNI);
            stmtInsert.setString(7, empleadoDNI);
            stmtInsert.executeUpdate();

            PreparedStatement stmtUpdate = conn.prepareStatement(
                "UPDATE Vehiculo SET disponible = 0 WHERE id = ?"
            );
            stmtUpdate.setInt(1, vehiculoId);
            stmtUpdate.executeUpdate();

            conn.commit();
            JOptionPane.showMessageDialog(this, "Alquiler registrado correctamente.");
            dispose();
            InicioAdmin inicioAdmin = new InicioAdmin();  // Crear instancia de la ventana anterior
            inicioAdmin.setVisible(true);  // Hacerla visible
            this.setVisible(false);  // Ocultar la ventana actual (sin cerrarla)

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar el alquiler.");
        }
    }
}
