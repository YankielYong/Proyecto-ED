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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import util.MyButtonModel;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import logica.Red;

import javax.swing.SwingConstants;

public class Reportes extends JDialog{

	private static final long serialVersionUID = 1L;
	private Inicial padre;
	private Reportes este;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel contentPane;
	private JPanel panelSuperior;
	private JPanel panelInferior;
	private JButton btnCerrar;
	private JLabel titulo;
	private JButton btnArbolAmigos;
	private JButton btnArbolConexiones;
	private JButton btnLideresInvestigacion;
	private JButton btnIslas;
	private JButton btnComunidades;
	private Red red;
	private Vertex vUsuario;
	
	public Reportes(Inicial p, Red r, Vertex v){
		super(p, true);
		padre = p;
		este = this;
		red = r;
		vUsuario = v;
		setUndecorated(true);
		setResizable(false);
		setBounds(pantalla.width/2-175, pantalla.height/2-110, 500, 280);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		panelSuperior = new JPanel();
		panelSuperior.setBackground(new Color(46, 139, 87));
		panelSuperior.setBounds(0, 0, 500, 30);
		panelSuperior.setLayout(null);
		contentPane.add(panelSuperior);
		
		titulo = new JLabel("Reportes");
		titulo.setForeground(Color.BLACK);
		titulo.setFont(new Font("Arial", Font.PLAIN, 20));
		titulo.setBounds(8, 0, 200, 30);
		panelSuperior.add(titulo);

		btnCerrar = new JButton("");
		btnCerrar.setModel(new MyButtonModel());
		btnCerrar.setBounds(455, 0, 45, 30);
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

		panelInferior = new JPanel();
		panelInferior.setBackground(Color.WHITE);
		panelInferior.setBounds(0, 30, 500, 250);
		panelInferior.setLayout(null);
		contentPane.add(panelInferior);
		
		btnArbolAmigos = new JButton("Relación jerárquica de amigos");
		btnArbolAmigos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnArbolAmigos.setBorderPainted(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnArbolAmigos.setBorderPainted(false);
			}
		});
		btnArbolAmigos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnArbolAmigos.setBorderPainted(false);
				dispose();
				SeleccionarUsuario su = new SeleccionarUsuario(padre, este, red, vUsuario, "Relación jerárquica de amigos");
				su.setVisible(true);
			}
		});
		btnArbolAmigos.setModel(new MyButtonModel());
		btnArbolAmigos.setHorizontalAlignment(SwingConstants.LEFT);
		btnArbolAmigos.setIcon(new ImageIcon(Reportes.class.getResource("/imagenes/arbol usuarios 40.png")));
		btnArbolAmigos.setForeground(Color.BLACK);
		btnArbolAmigos.setFont(new Font("Arial", Font.PLAIN, 20));
		btnArbolAmigos.setFocusable(false);
		btnArbolAmigos.setBorder(new LineBorder(new Color(0, 255, 127), 3));
		btnArbolAmigos.setContentAreaFilled(false);
		btnArbolAmigos.setBorderPainted(false);
		btnArbolAmigos.setBounds(0, 0, 500, 50);
		panelInferior.add(btnArbolAmigos);
		
		btnArbolConexiones = new JButton("Relación jerárquica de conexiones");
		btnArbolConexiones.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnArbolConexiones.setBorderPainted(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnArbolConexiones.setBorderPainted(false);
			}
		});
		btnArbolConexiones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnArbolConexiones.setBorderPainted(false);
				dispose();
				SeleccionarUsuario su = new SeleccionarUsuario(padre, este, red, vUsuario, "Relación jerárquica de conexiones");
				su.setVisible(true);
			}
		});
		btnArbolConexiones.setModel(new MyButtonModel());
		btnArbolConexiones.setHorizontalAlignment(SwingConstants.LEFT);
		btnArbolConexiones.setIcon(new ImageIcon(Reportes.class.getResource("/imagenes/arbol usuarios 40.png")));
		btnArbolConexiones.setForeground(Color.BLACK);
		btnArbolConexiones.setFont(new Font("Arial", Font.PLAIN, 20));
		btnArbolConexiones.setFocusable(false);
		btnArbolConexiones.setBorder(new LineBorder(new Color(0, 255, 127), 3));
		btnArbolConexiones.setContentAreaFilled(false);
		btnArbolConexiones.setBorderPainted(false);
		btnArbolConexiones.setBounds(0, 50, 500, 50);
		panelInferior.add(btnArbolConexiones);
		
		btnLideresInvestigacion = new JButton("Líderes de investigación");
		btnLideresInvestigacion.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnLideresInvestigacion.setBorderPainted(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnLideresInvestigacion.setBorderPainted(false);
			}
		});
		btnLideresInvestigacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*btnLideresInvestigacion.setBorderPainted(false);
				dispose();*/
			}
		});
		btnLideresInvestigacion.setModel(new MyButtonModel());
		btnLideresInvestigacion.setHorizontalAlignment(SwingConstants.LEFT);
		btnLideresInvestigacion.setIcon(new ImageIcon(Reportes.class.getResource("/imagenes/lideres inv 40.png")));
		btnLideresInvestigacion.setForeground(Color.BLACK);
		btnLideresInvestigacion.setFont(new Font("Arial", Font.PLAIN, 20));
		btnLideresInvestigacion.setFocusable(false);
		btnLideresInvestigacion.setBorder(new LineBorder(new Color(0, 255, 127), 3));
		btnLideresInvestigacion.setContentAreaFilled(false);
		btnLideresInvestigacion.setBorderPainted(false);
		btnLideresInvestigacion.setBounds(0, 100, 500, 50);
		panelInferior.add(btnLideresInvestigacion);
		
		btnIslas = new JButton("Usuarios Islas");
		btnIslas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnIslas.setBorderPainted(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnIslas.setBorderPainted(false);
			}
		});
		btnIslas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnIslas.setBorderPainted(false);
				dispose();
				VentanaIslas vi = new VentanaIslas(padre, este, red, vUsuario);
				vi.setVisible(true);
			}
		});
		btnIslas.setModel(new MyButtonModel());
		btnIslas.setHorizontalAlignment(SwingConstants.LEFT);
		btnIslas.setIcon(new ImageIcon(Reportes.class.getResource("/imagenes/usuario 40.png")));
		btnIslas.setForeground(Color.BLACK);
		btnIslas.setFont(new Font("Arial", Font.PLAIN, 20));
		btnIslas.setFocusable(false);
		btnIslas.setBorder(new LineBorder(new Color(0, 255, 127), 3));
		btnIslas.setContentAreaFilled(false);
		btnIslas.setBorderPainted(false);
		btnIslas.setBounds(0, 150, 500, 50);
		panelInferior.add(btnIslas);
		
		btnComunidades = new JButton("Comunidades");
		btnComunidades.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnComunidades.setBorderPainted(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnComunidades.setBorderPainted(false);
			}
		});
		btnComunidades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnComunidades.setBorderPainted(false);
				dispose();
				VentanaComunidades vc = new VentanaComunidades(padre, este, red, vUsuario);
				vc.setVisible(true);
			}
		});
		btnComunidades.setModel(new MyButtonModel());
		btnComunidades.setHorizontalAlignment(SwingConstants.LEFT);
		btnComunidades.setIcon(new ImageIcon(Reportes.class.getResource("/imagenes/comunidad 40.png")));
		btnComunidades.setForeground(Color.BLACK);
		btnComunidades.setFont(new Font("Arial", Font.PLAIN, 20));
		btnComunidades.setFocusable(false);
		btnComunidades.setBorder(new LineBorder(new Color(0, 255, 127), 3));
		btnComunidades.setContentAreaFilled(false);
		btnComunidades.setBorderPainted(false);
		btnComunidades.setBounds(0, 200, 500, 50);
		panelInferior.add(btnComunidades);
	}

}
