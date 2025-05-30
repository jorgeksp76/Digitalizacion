import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class LoginAdministrador extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textnombre;
    private JPasswordField textContraseña;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginAdministrador frame = new LoginAdministrador();
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
    public LoginAdministrador() {
    	setTitle("Login Adminn");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(12, 12, 426, 239);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(63, 39, 100, 15);
        panel.add(lblNombre);

        textnombre = new JTextField();
        textnombre.setBounds(161, 37, 114, 19);
        panel.add(textnombre);
        textnombre.setColumns(10);

        JLabel lblContrasea = new JLabel("Contraseña:");
        lblContrasea.setBounds(63, 82, 100, 15);
        panel.add(lblContrasea);

        textContraseña = new JPasswordField();  // Corregir el nombre de la variable
        textContraseña.setBounds(161, 80, 114, 19);
        panel.add(textContraseña);
        textContraseña.setColumns(10);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(158, 131, 117, 25);
        panel.add(btnLogin);

        JButton btnAtras = new JButton("Atras");
        btnAtras.setBounds(12, 202, 117, 25);
        panel.add(btnAtras);

        btnAtras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Inicio inicio = new Inicio();
                inicio.setVisible(true);
                dispose(); // Opcional: cerrar la ventana actual
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = textnombre.getText();
                char[] pasword = textContraseña.getPassword();  // Obtener la contraseña
                String contrasenaString = new String(pasword);  // Convertir el array a String

                // Validación de campos vacíos
                if (nombre.isEmpty() || contrasenaString.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
                    return;  // No continuar si los campos están vacíos
                }

                Connection conn = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;

                try {
                    // Usamos la clase BBDD para obtener la conexión
                    conn = BBDD.getConexion();

                    // Consulta para validar el nombre, contraseña y rol
                    String sql = "SELECT * FROM Empleado WHERE nombre = ? AND contraseña = ? AND rol = 'Administrador'";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, nombre);
                    stmt.setString(2, contrasenaString);

                    rs = stmt.executeQuery();

                    if (rs.next()) {
                        // Login exitoso: Rol Administrador
                        InicioAdmin inicioAdmin = new InicioAdmin();
                        inicioAdmin.setVisible(true);
                        dispose(); // Cierra la ventana de login
                    } else {
                        // Si no se encuentra el usuario o contraseña
                        JOptionPane.showMessageDialog(null, "Nombre, contraseña o rol incorrecto.");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error de conexión con la base de datos.");
                } finally {
                    // Asegurarse de cerrar la conexión, PreparedStatement y ResultSet
                    try {
                        if (rs != null) rs.close();
                        if (stmt != null) stmt.close();
                        if (conn != null) conn.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}
