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
import javax.swing.JPasswordField; // Importar JPasswordField
import javax.swing.JButton;

public class LoginUsuario extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textNombre;
    private JPasswordField textContraseña;  // Cambiar JTextField a JPasswordField

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginUsuario frame = new LoginUsuario();
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
    public LoginUsuario() {
    	setTitle("Login Usuario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(12, 0, 426, 239);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(62, 58, 92, 15);
        panel.add(lblNombre);

        textNombre = new JTextField();
        textNombre.setBounds(172, 56, 114, 19);
        panel.add(textNombre);
        textNombre.setColumns(10);

        JLabel lblContrasea = new JLabel("Contraseña:");
        lblContrasea.setBounds(62, 99, 92, 15);
        panel.add(lblContrasea);

        textContraseña = new JPasswordField(); // Cambiar a JPasswordField
        textContraseña.setBounds(172, 97, 114, 19);
        panel.add(textContraseña);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(169, 153, 117, 25);
        panel.add(btnLogin);

        JButton btnAtras = new JButton("Atras");
        btnAtras.setBounds(12, 214, 117, 25);
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
                String nombre = textNombre.getText();
                char[] contrasena = textContraseña.getPassword(); // Usar getPassword para obtener la contraseña
                String contrasenaString = new String(contrasena); // Convertir a String

                try {
                    // Usamos la clase BBDD para obtener la conexión
                    Connection conn = BBDD.getConexion();

                    // Consulta para validar el nombre, contraseña y rol 'Usuario'
                    String sql = "SELECT * FROM Empleado WHERE nombre = ? AND contraseña = ? AND rol = 'Usuario'";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, nombre);
                    stmt.setString(2, contrasenaString); // Usar contrasenaString

                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        // Login exitoso: Rol Usuario
                        DisponibilidadUsuario disponibilidadUsuario = new DisponibilidadUsuario();
                        disponibilidadUsuario.setVisible(true);
                        dispose(); // Cierra la ventana de login	
                    } else {
                        // Si no se encuentra el usuario o contraseña
                        JOptionPane.showMessageDialog(null, "Nombre, contraseña o rol incorrecto.");
                    }

                    rs.close();
                    stmt.close();
                    conn.close();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error de conexión con la base de datos.");
                }
            }
        });
    }
}
