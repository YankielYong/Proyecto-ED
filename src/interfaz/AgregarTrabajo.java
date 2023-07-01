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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import util.MyButtonModel;
import util.UsuariosTableModel;
import util.Validaciones;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import logica.Notificacion;
import logica.Red;
import logica.Trabajo;
import logica.Usuario;

import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class AgregarTrabajo extends JDialog{

	private static final long serialVersionUID = 1L;
	private Inicial padre;
	private MiPerfil anterior;
	private AgregarTrabajo este;
	private JPanel contentPane;
	private JPanel panelSuperior;
	private JPanel panelInferiorIzquierdo;
	private JPanel panelInferiorDerecho;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JLabel lblLineaInvestigacion;
	private JLabel lblTema;
	private JLabel logoCUJAE;
	private JLabel lblFoto;
	private JButton btnAgregar;
	private JButton btnAtras;
	private JLabel lblOrden;
	private JScrollPane scrollPaneUsuarios;
	private JScrollPane scrollPaneAutores;
	private JTable tableUsuarios;
	private JTable tableAutores;
	private UsuariosTableModel tableModelUsuarios;
	private UsuariosTableModel tableModelAutores;
	private Red red;
	private Vertex vUsuario;
	private Usuario usuario;
	private JLabel titulo;
	private JTextField txtLinea;
	private JTextField txtTema;
	private ArrayList<Usuario> usuarios;
	private ArrayList<Usuario> autores;
	private String lineaInvestigacion;
	private String tema;
	private boolean temaV = false;
	private boolean lineaV = false;
	private boolean valido = false;
	
	public AgregarTrabajo(Inicial p, MiPerfil a, Red r, Vertex v){
		super(p, true);
		padre = p;
		anterior = a;
		este = this;
		red = r;
		vUsuario = v;
		usuario = (Usuario)vUsuario.getInfo();
		obtenerUsuarios();
		autores = new ArrayList<Usuario>();
		setUndecorated(true);
		setResizable(false);
		setBounds(pantalla.width/2-425, pantalla.height/2-270, 1000, 600);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		panelSuperior = new JPanel();
		panelSuperior.setBackground(new Color(46, 139, 87));
		panelSuperior.setBounds(0, 0, 1000, 30);
		contentPane.add(panelSuperior);
		panelSuperior.setLayout(null);
		
		titulo = new JLabel("Agregar Trabajo/Proyecto");
		titulo.setForeground(Color.BLACK);
		titulo.setFont(new Font("Arial", Font.PLAIN, 20));
		titulo.setBounds(8, 0, 300, 30);
		panelSuperior.add(titulo);

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
				anterior.setVisible(true);
			}
		});
		btnAtras.setFont(new Font("Arial", Font.PLAIN, 12));
		btnAtras.setContentAreaFilled(false);
		btnAtras.setFocusable(false);
		btnAtras.setBorderPainted(false);
		btnAtras.setBounds(10, 10, 42, 35);
		panelInferiorIzquierdo.add(btnAtras);
		
		lblLineaInvestigacion = new JLabel("Línea de Investigación:");
		lblLineaInvestigacion.setForeground(Color.BLACK);
		lblLineaInvestigacion.setFont(new Font("Arial", Font.PLAIN, 22));
		lblLineaInvestigacion.setBounds(30, 280, 300, 40);
		panelInferiorIzquierdo.add(lblLineaInvestigacion);

		lblTema = new JLabel("Tema:");
		lblTema.setForeground(Color.BLACK);
		lblTema.setFont(new Font("Arial", Font.PLAIN, 22));
		lblTema.setBounds(30, 380, 200, 40);
		panelInferiorIzquierdo.add(lblTema);

		logoCUJAE = new JLabel("");
		logoCUJAE.setIcon(new ImageIcon(MiPerfil.class.getResource("/imagenes/logo CUJAE 181x200.png")));
		logoCUJAE.setBounds(55, 50, 181, 200);
		panelInferiorIzquierdo.add(logoCUJAE);
		
		lblFoto = new JLabel("");
		lblFoto.setIcon(new ImageIcon(MiPerfil.class.getResource("/imagenes/documento.png")));
		lblFoto.setBounds(270, 50, 166, 200);
		panelInferiorIzquierdo.add(lblFoto);
		
		txtLinea = new JTextField();
		txtLinea.setForeground(Color.BLACK);
		txtLinea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try{
					lineaInvestigacion = txtLinea.getText();
					Validaciones.lineaInvestigacion(lineaInvestigacion);
					txtLinea.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
					lineaV = true;
					verificacion();
				}
				catch(Exception e1){
					txtLinea.setBorder(new LineBorder(Color.red, 3, true));
					lineaV = false;
					verificacion();
				}
			}
		});
		txtLinea.setFont(new Font("Arial", Font.PLAIN, 20));
		txtLinea.setBorder(new LineBorder(Color.red, 3, true));
		txtLinea.setBounds(30, 320, 430, 40);
		panelInferiorIzquierdo.add(txtLinea);
		
		txtTema = new JTextField();
		txtTema.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try{
					tema = txtTema.getText();
					Validaciones.tema(tema);
					txtTema.setBorder(new LineBorder(new Color(46, 139, 87), 3, true));
					temaV = true;
					verificacion();
				}
				catch(Exception e1){
					txtTema.setBorder(new LineBorder(Color.red, 3, true));
					temaV = false;
					verificacion();
				}
			}
		});
		txtTema.setForeground(Color.BLACK);
		txtTema.setFont(new Font("Arial", Font.PLAIN, 20));
		txtTema.setBorder(new LineBorder(Color.red, 3, true));
		txtTema.setBounds(30, 420, 430, 40);
		panelInferiorIzquierdo.add(txtTema);
		
		btnAgregar = new JButton("Agregar");
		btnAgregar.setModel(new MyButtonModel());
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(valido){
					ArrayList<Usuario> aut = new ArrayList<Usuario>();
					aut.add(usuario);
					aut.addAll(autores);
					Trabajo t = new Trabajo(tema, lineaInvestigacion, aut);
					red.getTrabajos().add(t);
					try {
						red.agregarTrabajoAFichero(t);
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					Notificacion n = new Notificacion(vUsuario, Notificacion.PUBLICA_TRABAJO);
					for(int h=0; h<autores.size(); h++){
						Usuario c = autores.get(h);
						c = (Usuario)red.buscarUsuario(c.getNick()).getInfo();
						c.getNotificaciones().add(n);
					}
					dispose();
					MensajeAviso m = new MensajeAviso(padre, este, "Se ha publicado con éxito", MensajeAviso.CORRECTO);
					m.setVisible(true);
					MiPerfil mi = new MiPerfil(padre, red, vUsuario);
					mi.ponerTrabajos();
					mi.setVisible(true);
				}
				else{
					este.setVisible(false);
					MensajeAviso m = new MensajeAviso(padre, este, "Ya existe este trabajo en esa linea de investigación", MensajeAviso.ERROR);
					m.agrandar();
					m.setVisible(true);
				}
			}
		});
		btnAgregar.setForeground(Color.BLACK);
		btnAgregar.setBorder(null);
		btnAgregar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnAgregar.setBorder(new LineBorder(new Color(0, 255, 127), 3));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAgregar.setBorder(null);
			}
		});
		btnAgregar.setFont(new Font("Arial", Font.BOLD, 22));
		btnAgregar.setBackground(new Color(46, 139, 87));
		btnAgregar.setBounds(90, 495, 300, 40);
		btnAgregar.setFocusable(false);
		btnAgregar.setEnabled(false);
		panelInferiorIzquierdo.add(btnAgregar);
		
		lblOrden = new JLabel("Seleccione los otros autores");
		lblOrden.setFont(new Font("Arial", Font.PLAIN, 20));
		lblOrden.setForeground(Color.BLACK);
		lblOrden.setBounds(25, 20, 250, 40);
		panelInferiorDerecho.add(lblOrden);
		
		scrollPaneUsuarios = new JScrollPane();
		scrollPaneUsuarios.setBackground(Color.WHITE);
		scrollPaneUsuarios.setForeground(Color.BLACK);
		scrollPaneUsuarios.setFont(new Font("Arial", Font.PLAIN, 22));
		scrollPaneUsuarios.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Usuarios", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		((TitledBorder)scrollPaneUsuarios.getBorder()).setTitleFont(new Font("Arial", Font.PLAIN, 22));
		scrollPaneUsuarios.setBounds(25, 60, 450, 270);
		panelInferiorDerecho.add(scrollPaneUsuarios);
		
		scrollPaneAutores = new JScrollPane();
		scrollPaneAutores.setBackground(Color.WHITE);
		scrollPaneAutores.setForeground(Color.BLACK);
		scrollPaneAutores.setFont(new Font("Arial", Font.PLAIN, 22));
		scrollPaneAutores.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Autores", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		((TitledBorder)scrollPaneAutores.getBorder()).setTitleFont(new Font("Arial", Font.PLAIN, 22));
		scrollPaneAutores.setBounds(25, 350, 450, 185);
		panelInferiorDerecho.add(scrollPaneAutores);
		
		tableUsuarios = new JTable();
		tableUsuarios.setFocusable(false);
		tableUsuarios.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int pos = tableUsuarios.getSelectedRow();
				Usuario u = usuarios.get(pos);
				usuarios.remove(pos);
				autores.add(u);
				tablaAutores();
				tablaUsuarios();
			}
		});
		tableUsuarios.getTableHeader().setReorderingAllowed(false);
		tableUsuarios.setRowHeight(30);
		tableUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableUsuarios.setForeground(Color.BLACK);
		tableUsuarios.setFont(new Font("Arial", Font.PLAIN, 15));
		tableUsuarios.setBackground(Color.WHITE);
		scrollPaneUsuarios.setViewportView(tableUsuarios);
		
		tableAutores = new JTable();
		tableAutores.setFocusable(false);
		tableAutores.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int pos = tableAutores.getSelectedRow();
				Usuario u = autores.get(pos);
				autores.remove(pos);
				usuarios.add(u);
				tablaAutores();
				tablaUsuarios();
			}
		});
		tableAutores.getTableHeader().setReorderingAllowed(false);
		tableAutores.setRowHeight(30);
		tableAutores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableAutores.setForeground(Color.BLACK);
		tableAutores.setFont(new Font("Arial", Font.PLAIN, 15));
		tableAutores.setBackground(Color.WHITE);
		scrollPaneAutores.setViewportView(tableAutores);
		
		tablaUsuarios();
		tablaAutores();
	}
	
	private void tablaUsuarios(){
		tableModelUsuarios = new UsuariosTableModel(){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableUsuarios.setModel(tableModelUsuarios);
		tableUsuarios.getColumnModel().getColumn(0).setPreferredWidth(130);
		tableUsuarios.getColumnModel().getColumn(0).setResizable(false);
		tableUsuarios.getColumnModel().getColumn(1).setPreferredWidth(230);
		tableUsuarios.getColumnModel().getColumn(1).setResizable(false);
		tableUsuarios.getColumnModel().getColumn(2).setPreferredWidth(90);
		tableUsuarios.getColumnModel().getColumn(2).setResizable(false);
		scrollPaneUsuarios.getViewport().setBackground(Color.WHITE);
		
		for(int i=0; i<usuarios.size(); i++){
			Usuario us = usuarios.get(i);
			String[] datos = {us.getNick(), us.getProfesion(), us.getPais()};
			tableModelUsuarios.addRow(datos);
		}
	}
	
	private void tablaAutores(){
		tableModelAutores = new UsuariosTableModel(){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableAutores.setModel(tableModelAutores);
		tableAutores.getColumnModel().getColumn(0).setPreferredWidth(130);
		tableAutores.getColumnModel().getColumn(0).setResizable(false);
		tableAutores.getColumnModel().getColumn(1).setPreferredWidth(230);
		tableAutores.getColumnModel().getColumn(1).setResizable(false);
		tableAutores.getColumnModel().getColumn(2).setPreferredWidth(90);
		tableAutores.getColumnModel().getColumn(2).setResizable(false);
		scrollPaneAutores.getViewport().setBackground(Color.WHITE);
		
		for(int i=0; i<autores.size(); i++){
			Usuario us = autores.get(i);
			String[] datos = {us.getNick(), us.getProfesion(), us.getPais()};
			tableModelAutores.addRow(datos);
		}
	}
	
	private void obtenerUsuarios(){
		usuarios = new ArrayList<Usuario>();
		String miNick = usuario.getNick();
		Iterator<Vertex> iter = red.getGrafo().getVerticesList().iterator();
		while(iter.hasNext()){
			Usuario u = (Usuario)iter.next().getInfo();
			if(!u.getNick().equals(miNick))
				usuarios.add(u);
		}
	}
	
	private void verificacion(){
		if(lineaV && temaV){
			valido = true;
			btnAgregar.setEnabled(true);
			if(red.existeTrabajo(lineaInvestigacion, tema))
				valido = false;
		}
		else{
			valido = false;
			btnAgregar.setEnabled(false);
		}
	}
}
