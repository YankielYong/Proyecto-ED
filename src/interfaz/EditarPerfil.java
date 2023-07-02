package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import cu.edu.cujae.ceis.graph.vertex.Vertex;
import logica.Red;
import logica.Trabajo;
import logica.Usuario;
import util.MyButtonModel;
import util.PropiedadesComboBox;
import util.Validaciones;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class EditarPerfil extends JDialog{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Inicial padre;
	private MiPerfil anterior;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel panelInferior;
	private JPanel panelSuperior;
	private JLabel logoCUJAE;
	private JButton btnAtras;
	private JLabel lblNombreUsuario;
	private JLabel lblContrasea;
	private JLabel lblPais;
	private JLabel lblProfesion;
	private JTextField textUsuario;
	private JPasswordField textPassword;
	private JButton btnMostrarPassword;
	private JComboBox<String> comboBoxPais;
	private JTextField textPais;
	private JComboBox<String> comboBoxProfesion;
	private JTextField textProfesion;
	private JButton btnGuardarCambios;
	private JButton btnEditarNombre;
	private JButton btnEditarPassword;
	private JButton btnEditarPais;
	private JButton btnEditarProfesion;
	private Red red;
	private Vertex vUsuario;
	private Usuario usuario;
	private boolean nValido = true, pValido = true; 
	private JLabel titulo;
	
	private String nick;
	private String password;
	private String profesion;
	private String pais;

	public EditarPerfil(Inicial p, MiPerfil a, Red r, Vertex v){
		super(p, true);
		padre = p;
		anterior = a;
		red = r;
		vUsuario = v;
		usuario = (Usuario)vUsuario.getInfo();
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
		
		titulo = new JLabel("Editar Perfil");
		titulo.setForeground(Color.BLACK);
		titulo.setFont(new Font("Arial", Font.PLAIN, 20));
		titulo.setBounds(8, 0, 200, 30);
		panelSuperior.add(titulo);

		textPais = new JTextField(usuario.getPais());
		textPais.setBackground(Color.WHITE);
		textPais.setEditable(false);
		textPais.setBounds(57, 390, 250, 30);
		textPais.setFont(new Font("Arial", Font.PLAIN, 15));
		textPais.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
		panelInferior.add(textPais);

		textProfesion = new JTextField(usuario.getProfesion());
		textProfesion.setBackground(Color.WHITE);
		textProfesion.setEditable(false);
		textProfesion.setBounds(57, 460, 250, 30);
		textProfesion.setFont(new Font("Arial", Font.PLAIN, 15));
		textProfesion.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
		panelInferior.add(textProfesion);

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

		lblNombreUsuario = new JLabel("Nombre de Usuario");
		lblNombreUsuario.setBounds(57, 230, 150, 20);
		lblNombreUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
		panelInferior.add(lblNombreUsuario);

		lblContrasea = new JLabel("Contraseña");
		lblContrasea.setBounds(57, 300, 150, 20);
		lblContrasea.setFont(new Font("Arial", Font.PLAIN, 16));
		panelInferior.add(lblContrasea);

		lblPais = new JLabel("País");
		lblPais.setFont(new Font("Arial", Font.PLAIN, 16));
		lblPais.setBounds(57, 370, 150, 20);
		panelInferior.add(lblPais);

		lblProfesion = new JLabel("Profesión");
		lblProfesion.setFont(new Font("Arial", Font.PLAIN, 16));
		lblProfesion.setBounds(57, 440, 150, 20);
		panelInferior.add(lblProfesion);

		textUsuario = new JTextField(usuario.getNick());
		textUsuario.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try{
					Validaciones.nick(textUsuario.getText());
					textUsuario.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
					if(!textUsuario.getText().equals(usuario.getNick()))
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
		textUsuario.setBackground(Color.WHITE);
		textUsuario.setEditable(false);
		textUsuario.setBounds(57, 250, 250, 30);
		textUsuario.setFont(new Font("Arial", Font.PLAIN, 15));
		textUsuario.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
		panelInferior.add(textUsuario);

		btnMostrarPassword = new JButton("");
		btnMostrarPassword.setModel(new MyButtonModel());
		btnMostrarPassword.setBounds(270, 292, 35, 27);
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

		textPassword = new JPasswordField(usuario.getPassword());
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
		textPassword.setBackground(Color.WHITE);
		textPassword.setEditable(false);
		textPassword.setBounds(57, 320, 250, 30);
		textPassword.setMinimumSize(new Dimension(8, 20));
		textPassword.setFont(new Font("Arial", Font.PLAIN, 15));
		textPassword.setEchoChar('●');
		textPassword.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
		panelInferior.add(textPassword);

		comboBoxPais = new JComboBox<String>();
		comboBoxPais.setFocusable(false);
		comboBoxPais.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
		comboBoxPais.setBackground(Color.WHITE);
		comboBoxPais.setMaximumRowCount(5);
		comboBoxPais.setFont(new Font("Arial", Font.PLAIN, 15));
		comboBoxPais.setModel(util.ComboBoxModel.paisesModel());
		comboBoxPais.setBounds(57, 390, 250, 30);
		comboBoxPais.setUI(PropiedadesComboBox.createUI(rootPane));
		comboBoxPais.setSelectedItem(usuario.getPais());
		comboBoxPais.setVisible(false);
		panelInferior.add(comboBoxPais);

		comboBoxProfesion = new JComboBox<String>();
		comboBoxProfesion.setFocusable(false);
		comboBoxProfesion.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
		comboBoxProfesion.setModel(util.ComboBoxModel.profesionesModel());
		comboBoxProfesion.setMaximumRowCount(5);
		comboBoxProfesion.setFont(new Font("Arial", Font.PLAIN, 15));
		comboBoxProfesion.setBackground(Color.WHITE);
		comboBoxProfesion.setBounds(57, 460, 250, 30);
		comboBoxProfesion.setUI(PropiedadesComboBox.createUI(rootPane));
		comboBoxProfesion.setSelectedItem(usuario.getProfesion());
		comboBoxProfesion.setVisible(false);
		panelInferior.add(comboBoxProfesion);

		btnGuardarCambios = new JButton("Guardar Cambios");
		btnGuardarCambios.setModel(new MyButtonModel());
		btnGuardarCambios.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				nick = textUsuario.getText();
				password = textPassword.getText();
				pais = (String)comboBoxPais.getSelectedItem();
				profesion = (String)comboBoxProfesion.getSelectedItem();
				corregirTrabajos();
				usuario.setNick(nick);
				usuario.setPassword(password);
				usuario.setPais(pais);
				usuario.setProfesion(profesion);
				try {
					red.modificarUsuarioEnFichero(usuario);
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
				dispose();
				anterior = new MiPerfil(padre, red, vUsuario);
				anterior.setVisible(true);
			}
		});
		btnGuardarCambios.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnGuardarCambios.setBorder(new LineBorder(new Color(0, 255, 127), 3));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnGuardarCambios.setBorder(null);
			}
		});
		btnGuardarCambios.setBorder(null);
		btnGuardarCambios.setBounds(57, 520, 286, 30);
		btnGuardarCambios.setForeground(new Color(0, 0, 0));
		btnGuardarCambios.setBackground(new Color(46, 139, 87));
		btnGuardarCambios.setFont(new Font("Arial", Font.BOLD, 16));
		btnGuardarCambios.setFocusable(false);
		panelInferior.add(btnGuardarCambios);

		btnEditarNombre = new JButton("");
		btnEditarNombre.setModel(new MyButtonModel());
		btnEditarNombre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textUsuario.setEditable(true);
			}
		});
		btnEditarNombre.setIcon(new ImageIcon(EditarPerfil.class.getResource("/imagenes/editar 20.png")));
		btnEditarNombre.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnEditarNombre.setContentAreaFilled(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnEditarNombre.setContentAreaFilled(false);
			}
		});
		btnEditarNombre.setBounds(310, 250, 33, 30);
		btnEditarNombre.setBackground(new Color(46, 139, 87));
		btnEditarNombre.setFocusable(false);
		btnEditarNombre.setContentAreaFilled(false);
		btnEditarNombre.setBorderPainted(false);
		panelInferior.add(btnEditarNombre);

		btnEditarPassword = new JButton("");
		btnEditarPassword.setModel(new MyButtonModel());
		btnEditarPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPassword.setEditable(true);
			}
		});
		btnEditarPassword.setIcon(new ImageIcon(EditarPerfil.class.getResource("/imagenes/editar 20.png")));
		btnEditarPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnEditarPassword.setContentAreaFilled(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnEditarPassword.setContentAreaFilled(false);
			}
		});
		btnEditarPassword.setFocusable(false);
		btnEditarPassword.setContentAreaFilled(false);
		btnEditarPassword.setBorderPainted(false);
		btnEditarPassword.setBackground(new Color(46, 139, 87));
		btnEditarPassword.setBounds(310, 320, 33, 30);
		panelInferior.add(btnEditarPassword);

		btnEditarPais = new JButton("");
		btnEditarPais.setModel(new MyButtonModel());
		btnEditarPais.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboBoxPais.setVisible(true);
				textPais.setVisible(false);
			}
		});
		btnEditarPais.setIcon(new ImageIcon(EditarPerfil.class.getResource("/imagenes/editar 20.png")));
		btnEditarPais.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnEditarPais.setContentAreaFilled(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnEditarPais.setContentAreaFilled(false);
			}
		});
		btnEditarPais.setFocusable(false);
		btnEditarPais.setContentAreaFilled(false);
		btnEditarPais.setBorderPainted(false);
		btnEditarPais.setBackground(new Color(46, 139, 87));
		btnEditarPais.setBounds(310, 390, 33, 30);
		panelInferior.add(btnEditarPais);

		btnEditarProfesion = new JButton("");
		btnEditarProfesion.setModel(new MyButtonModel());
		btnEditarProfesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboBoxProfesion.setVisible(true);
				textProfesion.setVisible(false);
			}
		});
		btnEditarProfesion.setIcon(new ImageIcon(EditarPerfil.class.getResource("/imagenes/editar 20.png")));
		btnEditarProfesion.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnEditarProfesion.setContentAreaFilled(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnEditarProfesion.setContentAreaFilled(false);
			}
		});
		btnEditarProfesion.setFocusable(false);
		btnEditarProfesion.setContentAreaFilled(false);
		btnEditarProfesion.setBorderPainted(false);
		btnEditarProfesion.setBackground(new Color(46, 139, 87));
		btnEditarProfesion.setBounds(310, 460, 33, 30);
		panelInferior.add(btnEditarProfesion);
	}

	private void corregirTrabajos(){
		LinkedList<Trabajo> trab = red.getTrabajos();
		Iterator<Trabajo> iter = trab.iterator();
		while(iter.hasNext()){
			Trabajo t = iter.next();
			boolean esta = false;
			for(int i=0; i<t.getAutores().size() && !esta; i++){
				Usuario u = t.getAutores().get(i);
				if(u.getNick().equals(usuario.getNick())){
					esta = true;
					u.setNick(nick);
					u.setPassword(password);
					u.setPais(pais);
					u.setProfesion(profesion);
					try {
						red.modificarTrabajoEnFichero(t);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void verificacion(){
		if(nValido && pValido)
			btnGuardarCambios.setEnabled(true);
		else
			btnGuardarCambios.setEnabled(false);
	}
}
