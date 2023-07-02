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

public class PerfilUsuario extends JDialog{

	private static final long serialVersionUID = 1L;
	private Inicial padre;
	private PerfilUsuario este;
	private JDialog anterior;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel contentPane;
	private JPanel panelSuperior;
	private JPanel panelInferiorIzquierdo;
	private JPanel panelInferiorDerecho;
	private JButton btnCerrar;
	private JButton btnSolicitud;
	private JButton btnCancelarSolicitud;
	private JButton btnAtras;
	private JLabel lblUsuario;
	private JLabel lblProfesion;
	private JLabel lblPais;
	private JLabel logoCUJAE;
	private JButton btnAmigos;
	private JButton btnTrabajos;
	private JScrollPane scrollPane;
	private JTable table;
	private AmigosTableModel tableModelAmigos;
	private TrabajosTableModel tableModelTrabajos;
	private Red red;
	private Vertex vUsuario;
	private Vertex vPerfil;
	private Usuario usuario;
	private Usuario perfil;
	private LinkedList<Vertex> amigos;
	private LinkedList<Trabajo> trabajos;
	private JLabel titulo;

	public PerfilUsuario(Inicial p, JDialog an, Red r, Vertex v, Vertex vp){
		super(p, true);
		padre = p;
		anterior = an;
		red = r;
		vUsuario = v;
		vPerfil = vp;
		usuario = (Usuario)vUsuario.getInfo();
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

		if(red.sonAmigos(vUsuario, vPerfil))
			btnSolicitud = new JButton("Amigos");
		else{
			boolean esta = false;
			for(int i=0; i<perfil.getSolicitudes().size() && !esta; i++){
				SolicitudAmistad s = perfil.getSolicitudes().get(i);
				Usuario pe = s.getUsuario();
				if(pe.getNick().equals(usuario.getNick())){
					btnSolicitud = new JButton("Solicitud Enviada");
					esta = true;
				}
			}
			if(!esta){
				for(int i=0; i<usuario.getSolicitudes().size() && !esta; i++){
					SolicitudAmistad s = usuario.getSolicitudes().get(i);
					Usuario pe = s.getUsuario();
					if(pe.getNick().equals(perfil.getNick())){
						btnSolicitud = new JButton("Aceptar Solicitud");
						esta = true;
					}
				}
			}
			if(!esta)
				btnSolicitud = new JButton("Enviar Solicitud");
		}
		btnSolicitud.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnSolicitud.getText().equals("Enviar Solicitud")){
					SolicitudAmistad s = new SolicitudAmistad(vUsuario);
					Notificacion n = new Notificacion(vUsuario, Notificacion.ENVIA_SOLICITUD);
					perfil.getSolicitudes().add(s);
					perfil.getNotificaciones().add(n);
					btnSolicitud.setText("Solicitud Enviada");
					try {
						red.modificarUsuarioEnFichero(perfil);
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
				}
				else if(btnSolicitud.getText().equals("Aceptar Solicitud")){
					btnCancelarSolicitud.setVisible(false);
					btnCancelarSolicitud = null;
					int pos1 = red.getGrafo().getVerticesList().indexOf(vUsuario);
					int pos2 = red.getGrafo().getVerticesList().indexOf(vPerfil);
					red.getGrafo().insertWEdgeNDG(pos1, pos2, red.calcularPeso(pos1, pos2));
					try {
						red.agregarArcoAFichero(pos1, pos2);
					} catch (ClassNotFoundException | IOException e2) {
						e2.printStackTrace();
					}
					eliminarSolicitud();
					dispose();
					Notificacion n = new Notificacion(vUsuario, Notificacion.ACEPTA_SOLICITUD);
					perfil.getNotificaciones().add(n);
					try {
						red.modificarUsuarioEnFichero(perfil);
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					PerfilUsuario pe = new PerfilUsuario(padre, anterior, red, vUsuario, vPerfil);
					pe.setVisible(true);
				}
				else if(btnSolicitud.getText().equals("Solicitud Enviada")){
					dispose();
					MensajeAviso m = new MensajeAviso(padre, este, "¿Desea cancelar esta solicitud?", MensajeAviso.INFORMACION);
					m.setVisible(true);
					if(m.getValor()){
						cancelarSolicitud();
						Notificacion n = new Notificacion(vUsuario, Notificacion.CANCELA_SOLICITUD);
						perfil.getNotificaciones().add(n);
						try {
							red.modificarUsuarioEnFichero(perfil);
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						}
						PerfilUsuario pe = new PerfilUsuario(padre, anterior, red, vUsuario, vPerfil);
						pe.setVisible(true);
					}
				}
				else if(btnSolicitud.getText().equals("Amigos")){
					dispose();
					MensajeAviso m = new MensajeAviso(padre, este, "¿Desea eliminar este usuario de sus amigos?", MensajeAviso.INFORMACION);
					m.setVisible(true);
					if(m.getValor()){
						eliminarAmigo();
						Notificacion n = new Notificacion(vUsuario, Notificacion.ELIMINA_AMIGO);
						perfil.getNotificaciones().add(n);
						try {
							red.modificarUsuarioEnFichero(perfil);
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						}
						PerfilUsuario pe = new PerfilUsuario(padre, anterior, red, vUsuario, vPerfil);
						pe.setVisible(true);
					}
				}
			}
		});
		btnSolicitud.setModel(new MyButtonModel());
		btnSolicitud.setForeground(Color.BLACK);
		btnSolicitud.setBorder(null);
		btnSolicitud.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnSolicitud.setBorder(new LineBorder(new Color(0, 255, 127), 3));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnSolicitud.setBorder(null);
			}
		});
		btnSolicitud.setFont(new Font("Arial", Font.BOLD, 22));
		btnSolicitud.setBackground(new Color(46, 139, 87));
		btnSolicitud.setBounds(80, 495, 300, 40);
		btnSolicitud.setFocusable(false);
		panelInferiorIzquierdo.add(btnSolicitud);
		if(btnSolicitud.getText().equals("Aceptar Solicitud")){
			btnSolicitud.setBounds(30, 495, 200, 40);
			crearBtnCancSol();
		}

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
					DescubrirPersonas ven = new DescubrirPersonas(padre, red, vUsuario);
					ven.setVisible(true);
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

		btnAmigos = new JButton("Amigos");
		btnAmigos.setModel(new MyButtonModel());
		btnAmigos.setBackground(Color.WHITE);
		btnAmigos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.getModel().equals(tableModelTrabajos))
					tablaAmigos();
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
				if(table.getModel().equals(tableModelAmigos))
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
				if(btnAmigos.isBorderPainted()){
					Vertex ver = amigos.get(pos);
					if(vUsuario.equals(ver)){
						dispose();
						MiPerfil frame = new MiPerfil(padre, red, vUsuario);
						frame.setVisible(true);

					}
					else{
						este.setVisible(false);
						PerfilUsuario ventanaPerfil = new PerfilUsuario(padre, este, red, vUsuario, ver);
						ventanaPerfil.setVisible(true);
					}
				}
				else{
					este.setVisible(false);
					Trabajo tr = trabajos.get(pos);
					PerfilTrabajo ventTrab = new PerfilTrabajo(padre, este, red, tr, vUsuario);
					ventTrab.setVisible(true);
				}
			}
		});
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(30);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("Arial", Font.PLAIN, 15));
		table.setBackground(Color.WHITE);
		scrollPane.setViewportView(table);

		tablaAmigos();

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
			tableModelAmigos.addRow(datos);
		}
	}

	private void crearBtnCancSol(){
		btnCancelarSolicitud = new JButton("Cancelar Solicitud");
		btnCancelarSolicitud.setModel(new MyButtonModel());
		btnCancelarSolicitud.setForeground(Color.BLACK);
		btnCancelarSolicitud.setBorder(null);
		btnCancelarSolicitud.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				MensajeAviso m = new MensajeAviso(padre, este, "¿Desea rechazar esta solicitud?", MensajeAviso.INFORMACION);
				m.setVisible(true);
				if(m.getValor()){
					eliminarSolicitud();
					Notificacion n = new Notificacion(vUsuario, Notificacion.RECHAZA_SOLICITUD);
					perfil.getNotificaciones().add(n);
					try {
						red.modificarUsuarioEnFichero(perfil);
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					PerfilUsuario pe = new PerfilUsuario(padre, anterior, red, vUsuario, vPerfil);
					pe.setVisible(true);
				}
			}
		});
		btnCancelarSolicitud.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnCancelarSolicitud.setBorder(new LineBorder(new Color(0, 255, 127), 3));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnCancelarSolicitud.setBorder(null);
			}
		});
		btnCancelarSolicitud.setFont(new Font("Arial", Font.BOLD, 22));
		btnCancelarSolicitud.setBackground(new Color(46, 139, 87));
		btnCancelarSolicitud.setBounds(260, 495, 200, 40);
		btnCancelarSolicitud.setFocusable(false);
		panelInferiorIzquierdo.add(btnCancelarSolicitud);
	}

	private void eliminarSolicitud(){
		ArrayList<SolicitudAmistad> solicitudes = usuario.getSolicitudes();
		boolean eliminado = false;
		for(int i=0; i<solicitudes.size() && !eliminado; i++){
			SolicitudAmistad s = solicitudes.get(i);
			Usuario us = s.getUsuario();
			if(us.getNick().equals(perfil.getNick())){
				solicitudes.remove(i);
				eliminado = true;
			}
			try {
				red.modificarUsuarioEnFichero(usuario);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		int cantN = usuario.getNotificaciones().size();
		int cantS = usuario.getSolicitudes().size();
		padre.actualizarCantidades(cantN, cantS);
	}
	
	private void cancelarSolicitud(){
		ArrayList<SolicitudAmistad> solicitudes = perfil.getSolicitudes();
		boolean cancelada = false;
		for(int i=0; i<solicitudes.size() && !cancelada; i++){
			SolicitudAmistad s = solicitudes.get(i);
			Usuario u = s.getUsuario();
			if(u.getNick().equals(usuario.getNick())){
				solicitudes.remove(i);
				cancelada = true;
			}
		}
	}
	
	private void eliminarAmigo(){
		int pos1 = red.getGrafo().getVerticesList().indexOf(vUsuario);
		int pos2 = red.getGrafo().getVerticesList().indexOf(vPerfil);
		red.getGrafo().deleteEdgeND(pos1, pos2);
		try {
			red.eliminarArcoFichero(pos1, pos2);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
}
