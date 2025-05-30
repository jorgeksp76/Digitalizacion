import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AgregarVehiculo extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtMatricula, txtMarca, txtModelo, txtCategoria, txtTipo, txtPrecio, txtFecha;
    private JCheckBox chkDisponible;

    public AgregarVehiculo() {
        setTitle("Agregar Vehículo");
        setBounds(100, 100, 400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblMatricula = new JLabel("Matrícula:");
        lblMatricula.setBounds(30, 30, 100, 25);
        getContentPane().add(lblMatricula);

        txtMatricula = new JTextField();
        txtMatricula.setBounds(150, 30, 200, 25);
        getContentPane().add(txtMatricula);

        JLabel lblMarca = new JLabel("Marca:");
        lblMarca.setBounds(30, 70, 100, 25);
        getContentPane().add(lblMarca);

        txtMarca = new JTextField();
        txtMarca.setBounds(150, 70, 200, 25);
        getContentPane().add(txtMarca);

        JLabel lblModelo = new JLabel("Modelo:");
        lblModelo.setBounds(30, 110, 100, 25);
        getContentPane().add(lblModelo);

        txtModelo = new JTextField();
        txtModelo.setBounds(150, 110, 200, 25);
        getContentPane().add(txtModelo);

        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setBounds(30, 150, 100, 25);
        getContentPane().add(lblCategoria);

        txtCategoria = new JTextField();
        txtCategoria.setBounds(150, 150, 200, 25);
        getContentPane().add(txtCategoria);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(30, 190, 100, 25);
        getContentPane().add(lblTipo);

        txtTipo = new JTextField();
        txtTipo.setBounds(150, 190, 200, 25);
        getContentPane().add(txtTipo);

        JLabel lblPrecio = new JLabel("Precio/Día:");
        lblPrecio.setBounds(30, 230, 100, 25);
        getContentPane().add(lblPrecio);

        txtPrecio = new JTextField();
        txtPrecio.setBounds(150, 230, 200, 25);
        getContentPane().add(txtPrecio);

        JLabel lblFechaAlta = new JLabel("Fecha Alta (yyyy-MM-dd):");
        lblFechaAlta.setBounds(30, 270, 150, 25);
        getContentPane().add(lblFechaAlta);

        txtFecha = new JTextField();
        txtFecha.setBounds(190, 270, 160, 25);
        getContentPane().add(txtFecha);

        JLabel lblDisponible = new JLabel("Disponible:");
        lblDisponible.setBounds(30, 310, 100, 25);
        getContentPane().add(lblDisponible);

        chkDisponible = new JCheckBox();
        chkDisponible.setBounds(150, 310, 20, 25);
        getContentPane().add(chkDisponible);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(150, 360, 100, 30);
        getContentPane().add(btnGuardar);

        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarVehiculo();
            }
        });
        JButton btnSalir = new JButton("Salir");
        btnSalir.setBounds(260, 360, 100, 30);
        getContentPane().add(btnSalir);

        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra esta ventana
                Modificar modificar = new Modificar(); // Asegúrate de que esta clase exista
                modificar.setVisible(true); // Abre la ventana de administración
            }
        });

    }

    private void guardarVehiculo() {
        try (Connection conn = BBDD.getConexion()) {
            int nuevoId = 1;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM Vehiculo");
            if (rs.next()) {
                nuevoId = rs.getInt(1) + 1;
            }

            String query = "INSERT INTO Vehiculo (id, matricula, marca, modelo, categoria, tipo, precio_alquiler_dia, fecha_alta, disponible) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, nuevoId);
            ps.setString(2, txtMatricula.getText());
            ps.setString(3, txtMarca.getText());
            ps.setString(4, txtModelo.getText());
            ps.setString(5, txtCategoria.getText());
            ps.setString(6, txtTipo.getText());
            ps.setDouble(7, Double.parseDouble(txtPrecio.getText()));
            ps.setDate(8, Date.valueOf(txtFecha.getText())); // yyyy-MM-dd
            ps.setInt(9, chkDisponible.isSelected() ? 1 : 0);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Vehículo agregado correctamente.");
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar el vehículo.");
        }
    }
    

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AgregarVehiculo frame = new AgregarVehiculo();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
