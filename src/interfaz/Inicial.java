package interfaz;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import util.MyButtonModel;

import com.bolivia.label.CLabel;

import cu.edu.cujae.ceis.graph.vertex.Vertex;
import logica.Red;

import java.awt.event.MouseMotionAdapter;
import java.awt.Font;

public class Inicial extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelSuperior;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JButton btnSalir;
	private JPanel panelIzquierdo;
	private JLabel borde;
	private JButton btnPerfil;
	private JButton btnAmigos;
	private JButton btnNotificaciones;
	private JButton btnProyectos;
	private Inicial este;
	private CLabel cantSolct; int cantN;
	private CLabel cantNotif; int cantS;
	private Red red;
	private Vertex vUsuario;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Inicial frame = new Inicial();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public Inicial() throws IOException, ClassNotFoundException{
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if(vUsuario == null){
					try {
						iniciarSesion();
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		este = this;
		red = new Red();
		cantN = cantS = 0;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, (int)pantalla.getWidth(), (int)pantalla.getHeight());
		setExtendedState(MAXIMIZED_BOTH);
		contentPane = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			public void paintComponent(Graphics g){
				Image img = Toolkit.getDefaultToolkit().getImage(Inicial.class.getResource("/imagenes/Fondo CUJAE.jpg"));
				g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);
		setUndecorated(true);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panelIzquierdo = new JPanel();
		panelIzquierdo.setBounds(0, 0, 125, 1080);
		panelIzquierdo.setBackground(new Color(46, 139, 87));
		contentPane.add(panelIzquierdo);
		panelIzquierdo.setLayout(null);

		btnPerfil = new JButton("");
		btnPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MiPerfil frame = new MiPerfil(este, red, vUsuario);
				frame.setVisible(true);
			}
		});
		btnPerfil.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/perfil 70.png")));
		btnPerfil.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnPerfil.setContentAreaFilled(true);
				btnPerfil.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/perfil 80.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnPerfil.setContentAreaFilled(false);
				btnPerfil.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/perfil 70.png")));
			}
		});
		btnPerfil.setBackground(new Color(0, 255, 127));
		btnPerfil.setBounds(0, 140, 125, 110);
		btnPerfil.setModel(new MyButtonModel());
		btnPerfil.setContentAreaFilled(false);
		btnPerfil.setFocusable(false);
		btnPerfil.setBorderPainted(false);
		panelIzquierdo.add(btnPerfil);

		btnAmigos = new JButton("");
		btnAmigos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DescubrirPersonas ventanaDescubrir = new DescubrirPersonas(este, red, vUsuario);
				ventanaDescubrir.setVisible(true);
			}
		});
		btnAmigos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnAmigos.setBackground(new Color(0, 255, 127));
				btnAmigos.setContentAreaFilled(true);
				btnAmigos.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/amigos 80.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAmigos.setBackground(new Color(46, 139, 87));
				btnAmigos.setContentAreaFilled(false);
				btnAmigos.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/amigos 70.png")));
			}
		});
		btnAmigos.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/amigos 70.png")));
		btnAmigos.setFocusable(false);
		btnAmigos.setModel(new MyButtonModel());
		btnAmigos.setContentAreaFilled(false);
		btnAmigos.setBorderPainted(false);
		btnAmigos.setBackground(new Color(46, 139, 87));
		btnAmigos.setLayout(null);
		btnAmigos.setBounds(0, 250, 125, 110);
		panelIzquierdo.add(btnAmigos);

		btnProyectos = new JButton("");
		btnProyectos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DescubrirTrabajos ventanaTrab = new DescubrirTrabajos(este, red, vUsuario);
				ventanaTrab.setVisible(true);
			}
		});
		btnProyectos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnProyectos.setContentAreaFilled(true);
				btnProyectos.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/proyectos 80.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnProyectos.setContentAreaFilled(false);
				btnProyectos.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/proyectos 70.png")));
			}
		});
		btnProyectos.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/proyectos 70.png")));
		btnProyectos.setFocusable(false);
		btnProyectos.setModel(new MyButtonModel());
		btnProyectos.setContentAreaFilled(false);
		btnProyectos.setBorderPainted(false);
		btnProyectos.setBackground(new Color(0, 255, 127));
		btnProyectos.setBounds(0, 360, 125, 110);
		panelIzquierdo.add(btnProyectos);

		btnNotificaciones = new JButton("");
		btnNotificaciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaNotificaciones ventanaNot = new VentanaNotificaciones(este, red, vUsuario);
				ventanaNot.setVisible(true);
			}
		});
		btnNotificaciones.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnNotificaciones.setBackground(new Color(0, 255, 127));
				btnNotificaciones.setContentAreaFilled(true);
				btnNotificaciones.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/notificaciones 80.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnNotificaciones.setBackground(new Color(46, 139, 87));
				btnNotificaciones.setContentAreaFilled(false);
				btnNotificaciones.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/notificaciones 70.png")));
			}
		});
		btnNotificaciones.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/notificaciones 70.png")));
		btnNotificaciones.setFocusable(false);
		btnNotificaciones.setModel(new MyButtonModel());
		btnNotificaciones.setBackground(new Color(46, 139, 87));
		btnNotificaciones.setContentAreaFilled(false);
		btnNotificaciones.setBorderPainted(false);
		btnNotificaciones.setBounds(0, 470, 125, 110);
		btnNotificaciones.setLayout(null);
		panelIzquierdo.add(btnNotificaciones);



		borde = new JLabel("");
		borde.setBounds(25, -11, 480, 360);
		contentPane.add(borde);
		borde.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/borde.png")));

		panelSuperior = new JPanel();
		panelSuperior.setBounds(0, 0, this.getWidth(), 60);
		panelSuperior.setBackground(new Color(46, 139, 87));
		panelSuperior.setLayout(null);
		contentPane.add(panelSuperior);

		btnSalir = new JButton("");
		btnSalir.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/salir 30.png")));
		btnSalir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnSalir.setContentAreaFilled(true);
				btnSalir.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/salir 40.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnSalir.setContentAreaFilled(false);
				btnSalir.setIcon(new ImageIcon(Inicial.class.getResource("/imagenes/salir 30.png")));
			}
		});
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salir();
			}
		});
		btnSalir.setBounds(panelSuperior.getWidth()-80, 0, 80, 60);
		btnSalir.setBackground(Color.RED);
		btnSalir.setFocusable(false);
		btnSalir.setModel(new MyButtonModel());
		btnSalir.setBorderPainted(false);
		btnSalir.setContentAreaFilled(false);
		panelSuperior.add(btnSalir);

		cantNotif = new CLabel();
		cantNotif.setBounds(95, 80, 25, 25);
		cantNotif.setForeground(new Color(0, 0, 0));
		cantNotif.setFont(new Font("Arial", Font.BOLD, 20));
		cantNotif.setText(String.valueOf(cantN));
		cantNotif.setLineBorder(0);
		cantNotif.setBackground(Color.red);
		btnNotificaciones.add(cantNotif);
		cantNotif.setVisible(false);

		cantSolct = new CLabel();
		cantSolct.setBounds(95, 80, 25, 25);
		cantSolct.setForeground(new Color(0, 0, 0));
		cantSolct.setFont(new Font("Arial", Font.BOLD, 20));
		cantSolct.setText(String.valueOf(cantS));
		cantSolct.setLineBorder(0);
		cantSolct.setBackground(Color.red);
		btnAmigos.add(cantSolct);
		cantSolct.setVisible(false);
	}

	public void actualizarCantidades(int cantNo, int cantSo){
		cantN = cantNo;
		cantS = cantSo;
		if(cantN != 0){
			cantNotif.setVisible(true);
			cantNotif.setText(String.valueOf(cantN));
		}
		else{
			cantNotif.setVisible(false);
			cantNotif.setText(String.valueOf(cantN));
		}
		if(cantS != 0){
			cantSolct.setVisible(true);
			cantSolct.setText(String.valueOf(cantS));
		}
		else{
			cantSolct.setVisible(false);
			cantSolct.setText(String.valueOf(cantS));	
		}
	}

	public void setVUsuario(Vertex v){
		vUsuario = v;
	}

	public Vertex getVUsuario(){
		return vUsuario;
	}

	public void iniciarSesion() throws ParseException{
		try{
			InicioSesion ventana = new InicioSesion(this, red);
			ventana.setVisible(true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void salir(){
		System.exit(0);
	}

}
