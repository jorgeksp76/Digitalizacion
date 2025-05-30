import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;

public class InicioAdmin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InicioAdmin frame = new InicioAdmin();
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
	public InicioAdmin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 0, 426, 251);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblDarDeAlta = new JLabel("Dar de Alta");
		lblDarDeAlta.setBounds(47, 12, 104, 15);
		panel.add(lblDarDeAlta);
		
		JLabel lblModificar = new JLabel("Modificar datos vehiculos");
		lblModificar.setBounds(209, 12, 194, 15);
		panel.add(lblModificar);
		
		JLabel lblMantenimiento = new JLabel("Fecha Mantenimiento");
		lblMantenimiento.setBounds(12, 86, 170, 15);
		panel.add(lblMantenimiento);
		
		JButton btnAlta = new JButton("Alta");
		btnAlta.setBounds(34, 39, 117, 25);
		panel.add(btnAlta);
		
		JButton btnModificar = new JButton("Modificar");
		btnModificar.setBounds(244, 39, 117, 25);
		panel.add(btnModificar);
		
		JButton btnMantenimiento = new JButton("Mantenimiento");
		btnMantenimiento.setBounds(22, 113, 141, 25);
		panel.add(btnMantenimiento);
		
		JButton btnAtras = new JButton("Atras");
		btnAtras.setBounds(12, 194, 80, 25);
		panel.add(btnAtras);
		
		JLabel lblVerAlquileres = new JLabel("Ver Alquileres");
		lblVerAlquileres.setBounds(254, 86, 104, 15);
		panel.add(lblVerAlquileres);
		
		JButton btnAlquileres = new JButton("Alquileres");
		btnAlquileres.setBounds(244, 113, 117, 25);
		panel.add(btnAlquileres);
		
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginAdministrador loginAdministrador = new LoginAdministrador();
				loginAdministrador.setVisible(true);
				dispose(); // Opcional: cerrar la ventana actual
			}
		});
		
		btnAlta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DisponibilidadAdmin alta = new DisponibilidadAdmin();
				alta.setVisible(true);
				dispose(); // Opcional: cerrar la ventana actual
			}
		});
		
		btnAlquileres.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Alquileres alquileres = new Alquileres();
				alquileres.setVisible(true);
				dispose(); // Opcional: cerrar la ventana actual
			}
		});
		
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Modificar modificar = new Modificar();
				modificar.setVisible(true);
				dispose(); // Opcional: cerrar la ventana actual
			}
		});
		
		btnMantenimiento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mantenimiento mantenimiento = new Mantenimiento();
				mantenimiento.setVisible(true);
				dispose(); // Opcional: cerrar la ventana actual
			}
		});
	}
}
