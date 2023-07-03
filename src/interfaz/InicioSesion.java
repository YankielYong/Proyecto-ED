package interfaz;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JPasswordField;
import javax.swing.JLabel;

import java.awt.Cursor;

import javax.swing.border.LineBorder;

import util.MyButtonModel;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import logica.Red;
import logica.Usuario;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class InicioSesion extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Inicial padre;
	private JButton btnCerrar;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel panelInferior;
	private JTextField textUsuario;
	private JPasswordField textPassword;
	private JButton btnMostrarPassword;
	private JLabel lblPregunta;
	private JButton btnIniciar;
	private JPanel panelSuperior;
	private JLabel lblContrasea;
	private JLabel lblNombreUsuario;
	private JButton btnRegistrarse;
	private JLabel logoCUJAE;
	private InicioSesion este;
	private Red red;

	public InicioSesion(Inicial p, Red r) throws ParseException {
		super(p, true);
		padre = p;
		red = r;
		este = this;
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(pantalla.width/2-125, pantalla.height/2-240, 400, 550);

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		panelInferior = new JPanel();
		panelInferior.setBackground(Color.WHITE);
		panelInferior.setBounds(0, 30, 400, 520);
		contentPane.add(panelInferior);
		panelInferior.setLayout(null);

		textUsuario = new JTextField();
		textUsuario.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int key=e.getKeyChar();
				boolean si = false;
				if(key == 46 || key == 95 || (key >= 48 && key <= 57) || (key >= 65 && key <= 90) || (key >= 97 && key <=122))
					si = true;
				if(!si)
					e.consume();
			}
		});
		textUsuario.setBounds(75, 280, 250, 30);
		textUsuario.setFont(new Font("Arial", Font.PLAIN, 15));
		textUsuario.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
		panelInferior.add(textUsuario);

		btnMostrarPassword = new JButton("");
		btnMostrarPassword.setModel(new MyButtonModel());
		btnMostrarPassword.setBounds(288, 322, 35, 27);
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
			@Override
			public void keyTyped(KeyEvent e) {
				int key=e.getKeyChar();
				boolean si = key==32;
				if(si)
					e.consume();
			}
		});
		textPassword.setBounds(75, 350, 250, 30);
		textPassword.setMinimumSize(new Dimension(8, 20));
		textPassword.setFont(new Font("Arial", Font.PLAIN, 15));
		textPassword.setEchoChar('●');
		textPassword.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
		panelInferior.add(textPassword);

		lblNombreUsuario = new JLabel("Nombre de Usuario");
		lblNombreUsuario.setBounds(75, 260, 150, 20);
		lblNombreUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
		panelInferior.add(lblNombreUsuario);

		lblContrasea = new JLabel("Contraseña");
		lblContrasea.setBounds(75, 330, 150, 20);
		lblContrasea.setFont(new Font("Arial", Font.PLAIN, 16));
		panelInferior.add(lblContrasea);

		lblPregunta = new JLabel("¿No tienes una cuenta?");
		lblPregunta.setBounds(95, 485, 133, 14);
		lblPregunta.setFont(new Font("Arial", Font.PLAIN, 12));
		panelInferior.add(lblPregunta);

		btnIniciar = new JButton("Iniciar Sesión");
		btnIniciar.setModel(new MyButtonModel());
		btnIniciar.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				String nick = textUsuario.getText();
				String password = textPassword.getText();
				Vertex v = red.buscarUsuario(nick);
				if(v != null){
					Usuario aux = (Usuario)v.getInfo();
					if(aux.getPassword().equals(password)){
						padre.setVUsuario(v);
						dispose();
						MensajeAviso m = new MensajeAviso(padre, este, "Usted ha iniciado sesión correctamente", MensajeAviso.CORRECTO);
						m.setVisible(true);
						int cantN = aux.getNotificaciones().size();
						int cantS = aux.getSolicitudes().size();
						padre.actualizarCantidades(cantN, cantS);
					}
					else{
						este.setVisible(false);
						MensajeAviso m = new MensajeAviso(padre, este, "Contraseña Incorrecta", MensajeAviso.ERROR);
						m.setVisible(true);
					}
				}
				else{
					este.setVisible(false);
					MensajeAviso m = new MensajeAviso(padre, este, "No existe este usuario", MensajeAviso.ERROR);
					m.setVisible(true);
				}
			}
		});
		btnIniciar.setBorder(null);
		btnIniciar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnIniciar.setBorder(new LineBorder(new Color(0, 255, 127), 3));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnIniciar.setBorder(null);
			}
		});
		btnIniciar.setBounds(75, 410, 250, 30);
		btnIniciar.setForeground(new Color(0, 0, 0));
		btnIniciar.setBackground(new Color(46, 139, 87));
		btnIniciar.setFont(new Font("Arial", Font.BOLD, 16));
		btnIniciar.setFocusable(false);
		panelInferior.add(btnIniciar);

		btnRegistrarse = new JButton("REGÍSTRATE");
		btnRegistrarse.setModel(new MyButtonModel());
		btnRegistrarse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnRegistrarse.setForeground(new Color(0, 130, 255));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnRegistrarse.setForeground(new Color(0, 0, 255));
			}
		});
		btnRegistrarse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				este.setVisible(false);
				CrearCuenta ventanaCrear = new CrearCuenta(padre, este, red);
				ventanaCrear.setVisible(true);
			}
		});
		btnRegistrarse.setBorder(null);
		btnRegistrarse.setHorizontalAlignment(SwingConstants.LEADING);
		btnRegistrarse.setBounds(230, 485, 74, 14);
		btnRegistrarse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnRegistrarse.setForeground(new Color(0, 0, 255));
		btnRegistrarse.setFont(new Font("Arial", Font.BOLD, 12));
		btnRegistrarse.setFocusable(false);
		btnRegistrarse.setBorderPainted(false);
		btnRegistrarse.setContentAreaFilled(false);
		panelInferior.add(btnRegistrarse);

		logoCUJAE = new JLabel("New label");
		logoCUJAE.setIcon(new ImageIcon(InicioSesion.class.getResource("/imagenes/logo CUJAE.png")));
		logoCUJAE.setBounds(125, 50, 150, 165);
		panelInferior.add(logoCUJAE);

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
				if(padre.getVUsuario() == null)
					padre.salir();
			}
		});
	}
}
