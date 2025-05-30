import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class DisponibilidadAdmin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtFechainicio;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DisponibilidadAdmin frame = new DisponibilidadAdmin();
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
	public DisponibilidadAdmin() {
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
		
		JLabel lblFechaInicio = new JLabel("Fecha Inicio (YYYY-MM-DD):");
		lblFechaInicio.setBounds(12, 58, 188, 15);
		panel.add(lblFechaInicio);
		
		txtFechainicio = new JTextField();
		txtFechainicio.setBounds(201, 56, 166, 19);
		panel.add(txtFechainicio);
		txtFechainicio.setColumns(10);
		
		JLabel lblFechaFin = new JLabel("Fecha Fin (YYYY-MM-DD):");
		lblFechaFin.setBounds(12, 111, 188, 15);
		panel.add(lblFechaFin);
		
		textField = new JTextField();
		textField.setBounds(201, 109, 166, 19);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(152, 152, 117, 25);
		panel.add(btnBuscar);
		
		JButton btnAtras = new JButton("Atras");
		btnAtras.setBounds(12, 202, 117, 25);
		panel.add(btnAtras);
		
		 btnAtras.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                InicioAdmin inicioAdmin = new InicioAdmin();
	                inicioAdmin.setVisible(true);
	                dispose(); // Opcional: cerrar la ventana actual
	            }
	        });
		 btnBuscar.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			        String fechaInicio = txtFechainicio.getText().trim();
			        String fechaFin = textField.getText().trim();

			        // Abrimos el formulario de alta con las fechas seleccionadas
			        AltaAdmin altaAlquiler = new AltaAdmin(fechaInicio, fechaFin);
			        altaAlquiler.setVisible(true);
			        dispose(); // Opcional: cerrar esta ventana
			    }
			});

	}
	
}
