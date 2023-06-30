package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Cursor;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JTable;

import util.AmigosTableModel;
import util.MyButtonModel;
import util.TrabajosTableModel;

import javax.swing.border.MatteBorder;

import cu.edu.cujae.ceis.graph.edge.Edge;
import cu.edu.cujae.ceis.graph.edge.WeightedEdge;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import logica.Red;
import logica.Trabajo;
import logica.Usuario;

public class MiPerfil extends JDialog{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Inicial padre;
	private MiPerfil este;
	private JButton btnCerrar;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel panelInferiorIzquierdo;
	private JPanel panelInferiorDerecho;
	private JPanel panelSuperior;
	private JLabel lblUsuario;
	private JLabel lblProfesion;
	private JLabel lblPais;
	private JLabel logoCUJAE;
	private JLabel lblFoto;
	private JButton btnCerrarSesion;
	private JButton btnConfig;
	private JButton btnAmigos;
	private JButton btnTrabajos;
	private JScrollPane scrollPane;
	private JTable table;
	private AmigosTableModel tableModelAmigos;
	private TrabajosTableModel tableModelTrabajos;
	private Red red;
	private Vertex vUsuario;
	private Usuario usuario;
	private LinkedList<Vertex> amigos;
	private LinkedList<Trabajo> trabajos;
	private JLabel titulo;
	private JButton btnPublicarTrabajo;
	private JButton btnEliminarTrabajo;

	public MiPerfil(Inicial p, Red r, Vertex v){
		super(p, true);
		padre = p;
		este = this;
		red = r;
		vUsuario = v;
		usuario = (Usuario)vUsuario.getInfo();
		setResizable(false);
		setUndecorated(true);
		setBounds(pantalla.width/2-425, pantalla.height/2-270, 1000, 600);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		panelInferiorIzquierdo = new JPanel();
		panelInferiorIzquierdo.setBackground(Color.WHITE);
		panelInferiorIzquierdo.setBounds(0, 30, 490, 570);
		contentPane.add(panelInferiorIzquierdo);
		panelInferiorIzquierdo.setLayout(null);

		panelInferiorDerecho = new JPanel();
		panelInferiorDerecho.setBackground(Color.WHITE);
		panelInferiorDerecho.setBounds(510, 30, 490, 570);
		panelInferiorDerecho.setLayout(null);
		contentPane.add(panelInferiorDerecho);

		lblUsuario = new JLabel("Nombre de Usuario: "+usuario.getNick());
		lblUsuario.setForeground(Color.BLACK);
		lblUsuario.setFont(new Font("Arial", Font.PLAIN, 22));
		lblUsuario.setBounds(30, 300, 450, 40);
		panelInferiorIzquierdo.add(lblUsuario);

		lblPais = new JLabel("Pa\u00EDs: "+usuario.getPais());
		lblPais.setForeground(Color.BLACK);
		lblPais.setFont(new Font("Arial", Font.PLAIN, 22));
		lblPais.setBounds(30, 420, 450, 40);
		panelInferiorIzquierdo.add(lblPais);

		lblProfesion = new JLabel("Profesión: "+usuario.getProfesion());
		lblProfesion.setForeground(Color.BLACK);
		lblProfesion.setFont(new Font("Arial", Font.PLAIN, 22));
		lblProfesion.setBounds(30, 360, 450, 40);
		panelInferiorIzquierdo.add(lblProfesion);

		logoCUJAE = new JLabel("");
		logoCUJAE.setIcon(new ImageIcon(MiPerfil.class.getResource("/imagenes/logo CUJAE 181x200.png")));
		logoCUJAE.setBounds(30, 70, 181, 200);
		panelInferiorIzquierdo.add(logoCUJAE);

		lblFoto = new JLabel("");
		lblFoto.setIcon(new ImageIcon(MiPerfil.class.getResource("/imagenes/usuario registrado.png")));
		lblFoto.setBounds(245, 58, 207, 200);
		panelInferiorIzquierdo.add(lblFoto);

		panelSuperior = new JPanel();
		panelSuperior.setBackground(new Color(46, 139, 87));
		panelSuperior.setBounds(0, 0, 1000, 30);
		contentPane.add(panelSuperior);
		panelSuperior.setLayout(null);

		titulo = new JLabel("Mi Perfil");
		titulo.setForeground(Color.BLACK);
		titulo.setFont(new Font("Arial", Font.PLAIN, 20));
		titulo.setBounds(8, 0, 200, 30);
		panelSuperior.add(titulo);

		btnCerrar = new JButton("");
		btnCerrar.setModel(new MyButtonModel());
		btnCerrar.setBounds(955, 0, 45, 30);
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

		btnCerrarSesion = new JButton("Cerrar Sesión");
		btnCerrarSesion.setModel(new MyButtonModel());
		btnCerrarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				este.setVisible(false);
				dispose();
				try {
					MensajeAviso m = new MensajeAviso(padre, este, "¿Desea cerrar sesión?", MensajeAviso.INFORMACION);
					m.setVisible(true);
					if(m.getValor()){
						padre.setVUsuario(null);
						padre.iniciarSesion();
					}
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnCerrarSesion.setForeground(Color.BLACK);
		btnCerrarSesion.setBorder(null);
		btnCerrarSesion.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnCerrarSesion.setBorder(new LineBorder(new Color(0, 255, 127), 3));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnCerrarSesion.setBorder(null);
			}
		});
		btnCerrarSesion.setFont(new Font("Arial", Font.BOLD, 22));
		btnCerrarSesion.setBackground(new Color(46, 139, 87));
		btnCerrarSesion.setBounds(90, 495, 300, 40);
		btnCerrarSesion.setFocusable(false);
		panelInferiorIzquierdo.add(btnCerrarSesion);

		btnConfig = new JButton("");
		btnConfig.setModel(new MyButtonModel());
		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				este.setVisible(false);
				EditarPerfil ventanaConfig = new EditarPerfil(padre, este, red, vUsuario);
				ventanaConfig.setVisible(true);
			}
		});
		btnConfig.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnConfig.setContentAreaFilled(true);

			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnConfig.setContentAreaFilled(false);
			}
		});
		btnConfig.setIcon(new ImageIcon(MiPerfil.class.getResource("/imagenes/ajustes 40.png")));
		btnConfig.setBounds(10, 10, 50, 40);
		btnConfig.setBackground(new Color(46, 139, 87));
		btnConfig.setFocusable(false);
		btnConfig.setContentAreaFilled(false);
		btnConfig.setBorderPainted(false);
		panelInferiorIzquierdo.add(btnConfig);

		btnAmigos = new JButton("Amigos");
		btnAmigos.setModel(new MyButtonModel());
		btnAmigos.setBackground(Color.WHITE);
		btnAmigos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ponerAmigos();
			}
		});
		btnAmigos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnAmigos.setForeground(Color.BLACK);
		btnAmigos.setBorder(new MatteBorder(0, 0, 4, 0, (Color) new Color(46, 139, 87)));
		btnAmigos.setFont(new Font("Arial", Font.PLAIN, 22));
		btnAmigos.setFocusable(false);
		btnAmigos.setContentAreaFilled(false);
		btnAmigos.setBounds(5, 44, 88, 30);
		panelInferiorDerecho.add(btnAmigos);

		btnTrabajos = new JButton("Trabajos");
		btnTrabajos.setModel(new MyButtonModel());
		btnTrabajos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ponerTrabajos();
			}
		});
		btnTrabajos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnTrabajos.setForeground(Color.BLACK);
		btnTrabajos.setBorder(new MatteBorder(0, 0, 4, 0, (Color) new Color(46, 139, 87)));
		btnTrabajos.setFont(new Font("Arial", Font.PLAIN, 22));
		btnTrabajos.setFocusable(false);
		btnTrabajos.setContentAreaFilled(false);
		btnTrabajos.setBounds(110, 44, 95, 30);
		panelInferiorDerecho.add(btnTrabajos);

		scrollPane = new JScrollPane();
		scrollPane.setForeground(Color.BLACK);
		scrollPane.setFont(new Font("Arial", Font.PLAIN, 12));
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(5, 80, 450, 455);
		panelInferiorDerecho.add(scrollPane);

		crearTabla();

		tablaAmigos();
	}
	
	public void ponerTrabajos(){
		panelInferiorDerecho.remove(scrollPane);
		scrollPane = new JScrollPane();
		scrollPane.setForeground(Color.BLACK);
		scrollPane.setFont(new Font("Arial", Font.PLAIN, 12));
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(5, 80, 450, 390);
		panelInferiorDerecho.add(scrollPane);
		este.repaint();
		crearTabla();
		tablaTrabajos();
	}
	
	public void ponerAmigos(){
		panelInferiorDerecho.remove(scrollPane);
		panelInferiorDerecho.remove(btnPublicarTrabajo);
		panelInferiorDerecho.remove(btnEliminarTrabajo);
		scrollPane = new JScrollPane();
		scrollPane.setForeground(Color.BLACK);
		scrollPane.setFont(new Font("Arial", Font.PLAIN, 12));
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(5, 80, 450, 455);
		panelInferiorDerecho.add(scrollPane);
		crearTabla();
		tablaAmigos();
	}
	
	private void crearTabla(){
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int pos = table.getSelectedRow();
				if(btnAmigos.isBorderPainted()){
					este.setVisible(false);
					Vertex ver = amigos.get(pos);
					PerfilUsuario ventanaPerfil = new PerfilUsuario(padre, este, red, vUsuario, ver);
					ventanaPerfil.setVisible(true);
					setVisible(false);
				}
				else{
					este.setVisible(false);
					Trabajo tr = trabajos.get(pos);
					PerfilTrabajo ventTrab = new PerfilTrabajo(padre, este, red, tr, vUsuario);
					ventTrab.setVisible(true);
				}
			}
		});
		table.setFocusable(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(30);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("Arial", Font.PLAIN, 15));
		table.setBackground(Color.WHITE);
		scrollPane.setViewportView(table);
	}

	private void tablaAmigos(){
		tableModelAmigos = new AmigosTableModel(){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(tableModelAmigos);
		table.getColumnModel().getColumn(0).setPreferredWidth(125);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setPreferredWidth(85);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(3).setPreferredWidth(40);
		table.getColumnModel().getColumn(3).setResizable(false);
		scrollPane.getViewport().setBackground(Color.WHITE);
		btnAmigos.setBorderPainted(true);
		btnTrabajos.setBorderPainted(false);
		mostrarAmigos();
	}

	private void tablaTrabajos(){
		tableModelTrabajos = new TrabajosTableModel(){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(tableModelTrabajos);
		table.getColumnModel().getColumn(0).setPreferredWidth(170);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(280);
		table.getColumnModel().getColumn(1).setResizable(false);
		scrollPane.getViewport().setBackground(Color.WHITE);
		btnAmigos.setBorderPainted(false);
		btnTrabajos.setBorderPainted(true);
		
		btnPublicarTrabajo = new JButton("Publicar");
		btnPublicarTrabajo.setModel(new MyButtonModel());
		btnPublicarTrabajo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				btnPublicarTrabajo.setBorder(null);
				AgregarTrabajo at = new AgregarTrabajo(padre, este, red, vUsuario);
				at.setVisible(true);
			}
		});
		btnPublicarTrabajo.setForeground(Color.BLACK);
		btnPublicarTrabajo.setBorder(null);
		btnPublicarTrabajo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnPublicarTrabajo.setBorder(new LineBorder(new Color(0, 255, 127), 3));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnPublicarTrabajo.setBorder(null);
			}
		});
		btnPublicarTrabajo.setFont(new Font("Arial", Font.BOLD, 22));
		btnPublicarTrabajo.setBackground(new Color(46, 139, 87));
		btnPublicarTrabajo.setBounds(60, 495, 150, 40);
		btnPublicarTrabajo.setFocusable(false);
		panelInferiorDerecho.add(btnPublicarTrabajo);
		
		btnEliminarTrabajo = new JButton("Eliminar");
		btnEliminarTrabajo.setModel(new MyButtonModel());
		btnEliminarTrabajo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				btnEliminarTrabajo.setBorder(null);
				EliminarTrabajo et = new EliminarTrabajo(padre, este, red, vUsuario);
				et.setVisible(true);
			}
		});
		btnEliminarTrabajo.setForeground(Color.BLACK);
		btnEliminarTrabajo.setBorder(null);
		btnEliminarTrabajo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnEliminarTrabajo.setBorder(new LineBorder(new Color(0, 255, 127), 3));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnEliminarTrabajo.setBorder(null);
			}
		});
		btnEliminarTrabajo.setFont(new Font("Arial", Font.BOLD, 22));
		btnEliminarTrabajo.setBackground(new Color(46, 139, 87));
		btnEliminarTrabajo.setBounds(250, 495, 150, 40);
		btnEliminarTrabajo.setFocusable(false);
		panelInferiorDerecho.add(btnEliminarTrabajo);
		mostrarTrabajos();
	}

	private void mostrarTrabajos(){
		trabajos = red.trabajosDeUsuario(vUsuario);
		Iterator<Trabajo> iter = trabajos.iterator();
		while(iter.hasNext()){
			Trabajo t = iter.next();
			String[] datos = {t.getLineaInvestigacion(), t.getTema()};
			tableModelTrabajos.addRow(datos);
		}
	}

	private void mostrarAmigos(){
		LinkedList<Edge> arcos = vUsuario.getEdgeList();
		amigos = vUsuario.getAdjacents();
		Iterator<Edge> it = arcos.iterator();
		Iterator<Vertex> iter = amigos.iterator();
		while(iter.hasNext()){
			WeightedEdge a = (WeightedEdge)it.next();
			Vertex v = iter.next();
			Usuario u = (Usuario)v.getInfo();
			String[] datos = {u.getNick(), u.getProfesion(), u.getPais(), String.valueOf((int)a.getWeight())};
			tableModelAmigos.addRow(datos);
		}
	}
}
