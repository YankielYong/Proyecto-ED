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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.border.LineBorder;

import cu.edu.cujae.ceis.graph.vertex.Vertex;
import logica.Red;
import logica.SolicitudAmistad;
import logica.Usuario;
import util.MyButtonModel;
import util.UsuariosTableModel;

import javax.swing.JTextField;

public class DescubrirPersonas extends JDialog{

	private static final long serialVersionUID = 1L;
	private Inicial padre;
	private DescubrirPersonas este;
	private JPanel contentPane;
	private JPanel panelSuperior;
	private JPanel panelInferiorIzquierdo;
	private JPanel panelInferiorDerecho;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JButton btnCerrar;
	private JScrollPane scrollPaneSolicitudes;
	private JScrollPane scrollPanePersonas;
	private UsuariosTableModel tableModelSolicitudes;
	private UsuariosTableModel tableModelPersonas;
	private JTable tableSolicitudes;
	private JTable tableDescubrir;
	private Red red;
	private Vertex vUsuario;
	private Usuario usuario;
	private LinkedList<Vertex> noAmigos;
	private ArrayList<SolicitudAmistad> solicitudes;
	private JLabel titulo;
	private JTextField txtBuscar;
	private JLabel lblBuscar;


	public DescubrirPersonas(Inicial p, Red r, Vertex v){
		super(p, true);
		padre = p;
		este = this;
		red = r;
		vUsuario = v;
		usuario = (Usuario)vUsuario.getInfo();
		solicitudes = usuario.getSolicitudes();
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
		panelSuperior.setLayout(null);
		contentPane.add(panelSuperior);
		
		titulo = new JLabel("Usuarios");
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

		panelInferiorIzquierdo = new JPanel();
		panelInferiorIzquierdo.setBackground(Color.WHITE);
		panelInferiorIzquierdo.setBounds(0, 30, 490, 570);
		panelInferiorIzquierdo.setLayout(null);
		contentPane.add(panelInferiorIzquierdo);

		scrollPaneSolicitudes = new JScrollPane();
		scrollPaneSolicitudes.setBackground(Color.WHITE);
		scrollPaneSolicitudes.setForeground(Color.BLACK);
		scrollPaneSolicitudes.setFont(new Font("Arial", Font.PLAIN, 22));
		scrollPaneSolicitudes.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Solicitudes de Amistad", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		((TitledBorder)scrollPaneSolicitudes.getBorder()).setTitleFont(new Font("Arial", Font.PLAIN, 22));
		scrollPaneSolicitudes.setBounds(25, 25, 450, 520);
		panelInferiorIzquierdo.add(scrollPaneSolicitudes);

		panelInferiorDerecho = new JPanel();
		panelInferiorDerecho.setBackground(Color.WHITE);
		panelInferiorDerecho.setBounds(510, 30, 490, 570);
		panelInferiorDerecho.setLayout(null);
		contentPane.add(panelInferiorDerecho);

		scrollPanePersonas = new JScrollPane();
		scrollPanePersonas.setBackground(Color.WHITE);
		scrollPanePersonas.setForeground(Color.BLACK);
		scrollPanePersonas.setFont(new Font("Arial", Font.PLAIN, 22));
		scrollPanePersonas.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Descubrir Personas", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		((TitledBorder)scrollPanePersonas.getBorder()).setTitleFont(new Font("Arial", Font.PLAIN, 22));
		scrollPanePersonas.setBounds(15, 65, 450, 480);
		panelInferiorDerecho.add(scrollPanePersonas);

		tableSolicitudes = new JTable();
		tableSolicitudes.setFocusable(false);
		tableSolicitudes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				este.setVisible(false);
				int pos = tableSolicitudes.getSelectedRow();
				Vertex ver = solicitudes.get(pos).getVertex();
				PerfilUsuario ventanaPerfil = new PerfilUsuario(padre, este, red, vUsuario, ver);
				ventanaPerfil.setVisible(true);
			}
		});
		tableSolicitudes.getTableHeader().setReorderingAllowed(false);
		tableSolicitudes.setRowHeight(30);
		tableSolicitudes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSolicitudes.setForeground(Color.BLACK);
		tableSolicitudes.setFont(new Font("Arial", Font.PLAIN, 15));
		tableSolicitudes.setBackground(Color.WHITE);
		scrollPaneSolicitudes.setViewportView(tableSolicitudes);

		tableDescubrir = new JTable();
		tableDescubrir.setFocusable(false);
		tableDescubrir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				este.setVisible(false);
				int pos = tableDescubrir.getSelectedRow();
				Vertex ver = noAmigos.get(pos);
				PerfilUsuario ventanaPerfil = new PerfilUsuario(padre, este, red, vUsuario, ver);
				ventanaPerfil.setVisible(true);
			}
		});
		tableDescubrir.getTableHeader().setReorderingAllowed(false);
		tableDescubrir.setRowHeight(30);
		tableDescubrir.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableDescubrir.setForeground(Color.BLACK);
		tableDescubrir.setFont(new Font("Arial", Font.PLAIN, 15));
		tableDescubrir.setBackground(Color.WHITE);
		scrollPanePersonas.setViewportView(tableDescubrir);
		
		txtBuscar = new JTextField();
		txtBuscar.setForeground(Color.BLACK);
		txtBuscar.setFont(new Font("Arial", Font.PLAIN, 16));
		txtBuscar.setBounds(15, 25, 400, 30);
		panelInferiorDerecho.add(txtBuscar);
		
		lblBuscar = new JLabel("");
		lblBuscar.setIcon(new ImageIcon(DescubrirPersonas.class.getResource("/imagenes/lupa buscar 25.png")));
		lblBuscar.setBounds(425, 28, 25, 25);
		panelInferiorDerecho.add(lblBuscar);

		inicializarTablas();
	}

	private void inicializarTablas(){
		tableModelSolicitudes = new UsuariosTableModel(){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableModelPersonas = new UsuariosTableModel(){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableSolicitudes.setModel(tableModelSolicitudes);
		tableSolicitudes.getColumnModel().getColumn(0).setPreferredWidth(130);
		tableSolicitudes.getColumnModel().getColumn(0).setResizable(false);
		tableSolicitudes.getColumnModel().getColumn(1).setPreferredWidth(230);
		tableSolicitudes.getColumnModel().getColumn(1).setResizable(false);
		tableSolicitudes.getColumnModel().getColumn(2).setPreferredWidth(90);
		tableSolicitudes.getColumnModel().getColumn(2).setResizable(false);
		tableDescubrir.setModel(tableModelPersonas);
		tableDescubrir.getColumnModel().getColumn(0).setPreferredWidth(130);
		tableDescubrir.getColumnModel().getColumn(0).setResizable(false);
		tableDescubrir.getColumnModel().getColumn(1).setPreferredWidth(230);
		tableDescubrir.getColumnModel().getColumn(1).setResizable(false);
		tableDescubrir.getColumnModel().getColumn(2).setPreferredWidth(90);
		tableDescubrir.getColumnModel().getColumn(2).setResizable(false);
		scrollPanePersonas.getViewport().setBackground(Color.WHITE);
		scrollPaneSolicitudes.getViewport().setBackground(Color.WHITE);
		mostrarNoAmigos();
		mostrarSolicitudes();
	}

	private void mostrarNoAmigos(){
		noAmigos = new LinkedList<Vertex>();
		LinkedList<Vertex> amigos = vUsuario.getAdjacents();
		Iterator<Vertex> iter = red.getGrafo().getVerticesList().iterator();
		while(iter.hasNext()){
			Vertex v = iter.next();
			if(!v.equals(vUsuario) && !amigos.contains(v))
				noAmigos.add(v);
		}
		iter = noAmigos.iterator();
		while(iter.hasNext()){
			Usuario u = (Usuario)iter.next().getInfo();
			String[] datos = {u.getNick(), u.getProfesion(), u.getPais()};
			tableModelPersonas.addRow(datos);
		}
	}
	
	private void mostrarSolicitudes(){
		for(int i=0; i<solicitudes.size(); i++){
			Usuario u = (Usuario)solicitudes.get(i).getVertex().getInfo();
			String[] datos = {u.getNick(), u.getProfesion(), u.getPais()};
			tableModelSolicitudes.addRow(datos);
		}
	}
}
