package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JComboBox;

import cu.edu.cujae.ceis.graph.vertex.Vertex;
import logica.Red;
import logica.Usuario;
import util.MyButtonModel;
import util.PropiedadesComboBox;
import util.Validaciones;

public class CrearCuenta extends JDialog{
	private static final long serialVersionUID = 1L;
	private Inicial padre;
	private InicioSesion anterior;
	private JPanel contentPane;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel panelInferior;
	private JPanel panelSuperior;
	private JButton btnCerrar;
	private JTextField textUsuario;
	private JPasswordField textPassword;
	private JButton btnMostrarPassword;
	private JLabel lblContrasea;
	private JLabel lblNombreUsuario;
	private JComboBox<String> comboBoxPais;
	private JLabel lblPais;
	private JComboBox<String> comboBoxProfesion;
	private JLabel lblProfesion;
	private JButton btnRegistrarse;
	private JLabel logoCUJAE;
	private JButton btnAtras;
	private boolean nValido = false, pValido = false;
	private Red red;

	public CrearCuenta(Inicial p, InicioSesion a, Red r) {
		super(p, true);
		padre = p;
		anterior = a;
		red = r;
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(pantalla.width/2-125, pantalla.height/2-265, 400, 600);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		panelInferior = new JPanel();
		panelInferior.setBackground(Color.WHITE);
		panelInferior.setBounds(0, 30, 400, 570);
		contentPane.add(panelInferior);
		panelInferior.setLayout(null);
		
		panelSuperior = new JPanel();
		panelSuperior.setBackground(new Color(46, 139, 87));
		panelSuperior.setBounds(0, 0, 400, 30);
		contentPane.add(panelSuperior);
		panelSuperior.setLayout(null);
		
		btnCerrar = new JButton("");
		btnCerrar.setModel(new MyButtonModel());
		btnCerrar.setBounds(355, 0, 45, 30);
		panelSuperior.add(btnCerrar);
		btnCerrar.setIcon(new ImageIcon(InicioSesion.class.getResource("/imagenes/salir 15.png")));
		btnCerrar.setBackground(Color.RED);
		btnCerrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnCerrar.setContentAreaFilled(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnCerrar.setContentAreaFilled(false);
			}
		});
		btnCerrar.setContentAreaFilled(false);
		btnCerrar.setBorderPainted(false);
		btnCerrar.setFocusable(false);
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		textUsuario = new JTextField();
		textUsuario.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try{
					Validaciones.nick(textUsuario.getText());
					textUsuario.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
					if(red.yaExiste(textUsuario.getText()))
						throw new Exception();
					nValido = true;
					verificacion();
				}catch(Exception e1){
					textUsuario.setBorder(new LineBorder(Color.RED, 3, true));
					nValido = false;
					verificacion();
				}
			}
		});
		textUsuario.setBounds(75, 250, 250, 30);
		textUsuario.setFont(new Font("Arial", Font.PLAIN, 15));
		textUsuario.setBorder(new LineBorder(Color.RED, 3, true));
		panelInferior.add(textUsuario);
		
		btnMostrarPassword = new JButton("");
		btnMostrarPassword.setModel(new MyButtonModel());
		btnMostrarPassword.setBounds(288, 292, 35, 27);
		btnMostrarPassword.setIcon(new ImageIcon(InicioSesion.class.getResource("/imagenes/contraseña oculta.png")));
		btnMostrarPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textPassword.getEchoChar() == '●'){
					textPassword.setEchoChar((char) 0);
					btnMostrarPassword.setIcon(new ImageIcon(InicioSesion.class.getResource("/imagenes/contraseña visible.png")));
				}
				else{
					textPassword.setEchoChar('●');
					btnMostrarPassword.setIcon(new ImageIcon(InicioSesion.class.getResource("/imagenes/contraseña oculta.png")));
				}
			}
		});
		btnMostrarPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnMostrarPassword.setContentAreaFilled(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnMostrarPassword.setContentAreaFilled(false);
			}
		});
		btnMostrarPassword.setBackground(new Color(46, 139, 87));
		btnMostrarPassword.setFocusable(false);
		btnMostrarPassword.setBorderPainted(false);
		btnMostrarPassword.setContentAreaFilled(false);
		panelInferior.add(btnMostrarPassword);

		textPassword = new JPasswordField();
		textPassword.addKeyListener(new KeyAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void keyReleased(KeyEvent e) {
				try{
					Validaciones.password(textPassword.getText());
					textPassword.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
					pValido = true;
					verificacion();
				}
				catch(Exception e1){
					textPassword.setBorder(new LineBorder(Color.RED, 3, true));
					pValido = false;
					verificacion();
				}
			}
		});
		textPassword.setBounds(75, 320, 250, 30);
		textPassword.setMinimumSize(new Dimension(8, 20));
		textPassword.setFont(new Font("Arial", Font.PLAIN, 15));
		textPassword.setEchoChar('●');
		textPassword.setBorder(new LineBorder(Color.RED, 3, true));
		panelInferior.add(textPassword);

		lblNombreUsuario = new JLabel("Nombre de Usuario");
		lblNombreUsuario.setBounds(75, 230, 150, 20);
		lblNombreUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
		panelInferior.add(lblNombreUsuario);

		lblContrasea = new JLabel("Contraseña");
		lblContrasea.setBounds(75, 300, 150, 20);
		lblContrasea.setFont(new Font("Arial", Font.PLAIN, 16));
		panelInferior.add(lblContrasea);
		
		lblPais = new JLabel("País");
		lblPais.setFont(new Font("Arial", Font.PLAIN, 16));
		lblPais.setBounds(75, 370, 150, 20);
		panelInferior.add(lblPais);
		
		lblProfesion = new JLabel("Profesión");
		lblProfesion.setFont(new Font("Arial", Font.PLAIN, 16));
		lblProfesion.setBounds(75, 440, 150, 20);
		panelInferior.add(lblProfesion);
		
		comboBoxPais = new JComboBox<String>();
		comboBoxPais.setFocusable(false);
		comboBoxPais.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
		comboBoxPais.setBackground(Color.WHITE);
		comboBoxPais.setMaximumRowCount(5);
		comboBoxPais.setFont(new Font("Arial", Font.PLAIN, 15));
		comboBoxPais.setModel(util.ComboBoxModel.paisesModel());
		comboBoxPais.setBounds(75, 390, 250, 30);
		comboBoxPais.setUI(PropiedadesComboBox.createUI(rootPane));
		panelInferior.add(comboBoxPais);
		
		comboBoxProfesion = new JComboBox<String>();
		comboBoxProfesion.setFocusable(false);
		comboBoxProfesion.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
		comboBoxProfesion.setModel(util.ComboBoxModel.profesionesModel());
		comboBoxProfesion.setMaximumRowCount(5);
		comboBoxProfesion.setFont(new Font("Arial", Font.PLAIN, 15));
		comboBoxProfesion.setBackground(Color.WHITE);
		comboBoxProfesion.setBounds(75, 460, 250, 30);
		comboBoxProfesion.setUI(PropiedadesComboBox.createUI(rootPane));
		panelInferior.add(comboBoxProfesion);
		
		btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.setModel(new MyButtonModel());
		btnRegistrarse.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				String nick = textUsuario.getText();
				String password = textPassword.getText();
				String pais = (String)comboBoxPais.getSelectedItem();
				String profesion = (String)comboBoxProfesion.getSelectedItem();
				Usuario u = new Usuario(nick, password, pais, profesion);
				red.getGrafo().insertVertex(u);
				try {
					red.agregarUsuarioAFichero(u);
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
				Vertex vU = red.buscarUsuario(nick);
				padre.setVUsuario(vU);
				dispose();
			}
		});
		btnRegistrarse.setEnabled(false);
		btnRegistrarse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnRegistrarse.setBorder(new LineBorder(new Color(0, 255, 127), 3));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnRegistrarse.setBorder(null);
			}
		});
		btnRegistrarse.setBorder(null);
		btnRegistrarse.setBounds(75, 520, 250, 30);
		btnRegistrarse.setForeground(new Color(0, 0, 0));
		btnRegistrarse.setBackground(new Color(46, 139, 87));
		btnRegistrarse.setFont(new Font("Arial", Font.BOLD, 16));
		btnRegistrarse.setFocusable(false);
		panelInferior.add(btnRegistrarse);
		
		logoCUJAE = new JLabel("");
		logoCUJAE.setBounds(125, 35, 150, 165);
		logoCUJAE.setIcon(new ImageIcon(InicioSesion.class.getResource("/imagenes/logo CUJAE.png")));
		panelInferior.add(logoCUJAE);
		
		btnAtras = new JButton("");
		btnAtras.setModel(new MyButtonModel());
		btnAtras.setBackground(new Color(46, 139, 87));
		btnAtras.setIcon(new ImageIcon(CrearCuenta.class.getResource("/imagenes/flecha izquierda 35.png")));
		btnAtras.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnAtras.setContentAreaFilled(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAtras.setContentAreaFilled(false);
			}
		});
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				anterior.setVisible(true);
			}
		});
		btnAtras.setFont(new Font("Arial", Font.PLAIN, 12));
		btnAtras.setContentAreaFilled(false);
		btnAtras.setFocusable(false);
		btnAtras.setBorderPainted(false);
		btnAtras.setBounds(10, 10, 42, 35);
		panelInferior.add(btnAtras);
		
	}
	
	private void verificacion(){
		if(nValido && pValido)
			btnRegistrarse.setEnabled(true);
		else
			btnRegistrarse.setEnabled(false);
	}
}
