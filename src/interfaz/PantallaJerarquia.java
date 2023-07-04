package interfaz;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import logica.Notificacion;
import logica.Red;
import logica.SolicitudAmistad;
import logica.Trabajo;
import logica.Usuario;
import cu.edu.cujae.ceis.graph.edge.Edge;
import cu.edu.cujae.ceis.graph.edge.WeightedEdge;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import util.AmigosTableModel;
import util.MyButtonModel;
import util.TrabajosTableModel;
import util.UsuariosTableModel;

public class PantallaJerarquia extends JDialog{

	private static final long serialVersionUID = 1L;
	private Inicial padre;
	private PantallaJerarquia este;
	private JDialog anterior;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel contentPane;
	private JPanel panelSuperior;
	private JPanel panelInferiorIzquierdo;
	private JPanel panelInferiorDerecho;
	private JButton btnCerrar;
	private JButton btnAtras;
	private JLabel lblUsuario;
	private JLabel lblProfesion;
	private JLabel lblPais;
	private JLabel logoCUJAE;
	private JButton btnAmigos;
	private JButton btnTrabajos;
	private JScrollPane scrollPane;
	private JTable table;
	private UsuariosTableModel tableModelJerarquia;
	private TrabajosTableModel tableModelTrabajos;
	private Red red;
	private Vertex vPerfil;
	private Usuario usuario;
	private Usuario perfil;
	private LinkedList<Vertex> amigos;
	private LinkedList<Trabajo> trabajos;
	private JLabel titulo;

	public PantallaJerarquia(Inicial p, JDialog an, Red r, Vertex vp){
		super(p, true);
		padre = p;
		anterior = an;
		red = r;
		vPerfil = vp;
		perfil = (Usuario)vPerfil.getInfo();
		este = this;
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

		lblUsuario = new JLabel("Nombre de Usuario: "+perfil.getNick());
		lblUsuario.setForeground(Color.BLACK);
		lblUsuario.setFont(new Font("Arial", Font.PLAIN, 22));
		lblUsuario.setBounds(30, 300, 450, 40);
		panelInferiorIzquierdo.add(lblUsuario);

		lblPais = new JLabel("Pa\u00EDs: "+perfil.getPais());
		lblPais.setForeground(Color.BLACK);
		lblPais.setFont(new Font("Arial", Font.PLAIN, 22));
		lblPais.setBounds(30, 420, 450, 40);
		panelInferiorIzquierdo.add(lblPais);

		lblProfesion = new JLabel("Profesión: "+perfil.getProfesion());
		lblProfesion.setForeground(Color.BLACK);
		lblProfesion.setFont(new Font("Arial", Font.PLAIN, 22));
		lblProfesion.setBounds(30, 360, 450, 40);
		panelInferiorIzquierdo.add(lblProfesion);

		logoCUJAE = new JLabel("");
		logoCUJAE.setIcon(new ImageIcon(MiPerfil.class.getResource("/imagenes/logo CUJAE 181x200.png")));
		logoCUJAE.setBounds(160, 60, 181, 200);
		panelInferiorIzquierdo.add(logoCUJAE);

		panelSuperior = new JPanel();
		panelSuperior.setBackground(new Color(46, 139, 87));
		panelSuperior.setBounds(0, 0, 1000, 30);
		contentPane.add(panelSuperior);
		panelSuperior.setLayout(null);
		
		titulo = new JLabel("Perfil de Usuario");
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

		

		btnAtras = new JButton("");
		btnAtras.setModel(new MyButtonModel());
		btnAtras.setBackground(new Color(46, 139, 87));
		btnAtras.setIcon(new ImageIcon(PerfilUsuario.class.getResource("/imagenes/flecha izquierda 35.png")));
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
				if(anterior instanceof DescubrirPersonas){
					dispose();
					anterior.setVisible(true);
				}
				else
					anterior.setVisible(true);
			}
		});
		btnAtras.setFont(new Font("Arial", Font.PLAIN, 12));
		btnAtras.setContentAreaFilled(false);
		btnAtras.setFocusable(false);
		btnAtras.setBorderPainted(false);
		btnAtras.setBounds(10, 10, 42, 35);
		panelInferiorIzquierdo.add(btnAtras);

		btnAmigos = new JButton("Hijos");
		btnAmigos.setModel(new MyButtonModel());
		btnAmigos.setBackground(Color.WHITE);
		btnAmigos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.getModel().equals(tableModelTrabajos))
					tablaJerarquia();
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
				if(table.getModel().equals(tableModelJerarquia))
					tablaTrabajos();
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

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFocusable(false);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int pos = table.getSelectedRow();
				
			}
		});
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(30);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("Arial", Font.PLAIN, 15));
		table.setBackground(Color.WHITE);
		scrollPane.setViewportView(table);

		tablaJerarquia();

	}

	private void tablaJerarquia(){
		tableModelJerarquia = new UsuariosTableModel(){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(tableModelJerarquia);
		table.getColumnModel().getColumn(0).setPreferredWidth(130);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(230);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setPreferredWidth(90);
		table.getColumnModel().getColumn(2).setResizable(false);
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
		mostrarTrabajos();
	}

	private void mostrarTrabajos(){
		trabajos = red.trabajosDeUsuario(vPerfil);
		Iterator<Trabajo> iter = trabajos.iterator();
		while(iter.hasNext()){
			Trabajo t = iter.next();
			String[] datos = {t.getLineaInvestigacion(), t.getTema()};
			tableModelTrabajos.addRow(datos);
		}
	}

	private void mostrarAmigos(){
		LinkedList<Edge> arcos = vPerfil.getEdgeList();
		amigos = vPerfil.getAdjacents();
		Iterator<Edge> it = arcos.iterator();
		Iterator<Vertex> iter = amigos.iterator();
		while(iter.hasNext()){
			WeightedEdge a = (WeightedEdge)it.next();
			Vertex v = iter.next();
			Usuario u = (Usuario)v.getInfo();
			String[] datos = {u.getNick(), u.getProfesion(), u.getPais(), String.valueOf((int)a.getWeight())};
			tableModelJerarquia.addRow(datos);
		}
	}

	
}
