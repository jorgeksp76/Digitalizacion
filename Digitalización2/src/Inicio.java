import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Inicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Inicio frame = new Inicio();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Inicio() {
		setTitle("Inicio");
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
		
		JButton btnUsuario = new JButton("Usuario");
		btnUsuario.setBounds(63, 105, 117, 25);
		panel.add(btnUsuario);
		
		JButton btnAdministrador = new JButton("Administrador");
		btnAdministrador.setBounds(226, 105, 134, 25);
		panel.add(btnAdministrador);
		
		JLabel lblqueTipoDe = new JLabel("¿Que tipo de usuario eres?");
		lblqueTipoDe.setBounds(105, 50, 205, 15);
		panel.add(lblqueTipoDe);

		btnUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginUsuario loginUsuario = new LoginUsuario();
				loginUsuario.setVisible(true);
				dispose(); // Opcional: cerrar la ventana actual
			}
		});
		
		btnAdministrador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginAdministrador loginAdministrador = new LoginAdministrador();
				loginAdministrador.setVisible(true);
				dispose(); // Opcional: cerrar la ventana actual
			}
		});
	}
}
