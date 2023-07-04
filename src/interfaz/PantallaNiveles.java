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
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import util.MyButtonModel;
import util.UsuariosTableModel;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import logica.Red;
import logica.Usuario;

public class PantallaNiveles extends JDialog{

	private static final long serialVersionUID = 1L;
	private Inicial padre;
	private Reportes anterior;
	private PantallaNiveles este;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel contentPane;
	private JPanel panelSuperior;
	private JPanel panelInferior;
	private JButton btnCerrar;
	private JLabel titulo;
	private JScrollPane scrollPane;
	private JTable table;
	private UsuariosTableModel tableModel;
	private Red red;
	private Vertex vUsuario;
	private ArrayList<Usuario> listaMostrar;
	private ArrayList<Usuario> islas;
	private JButton btnalante;
	private JButton btnatras;
	private JButton btnAtras;
	
	public PantallaNiveles(Inicial p, Reportes a, Red r, Vertex v){
		super(p, true);
		padre = p;
		anterior = a;
		este = this;
		red = r;
		vUsuario = v;
		islas = red.obtenerIslas();
		listaMostrar = islas;
		setResizable(false);
		setUndecorated(true);
		setBounds(pantalla.width/2-225, pantalla.height/2-270, 600, 600);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		panelSuperior = new JPanel();
		panelSuperior.setBackground(new Color(46, 139, 87));
		panelSuperior.setBounds(0, 0, 600, 30);
		contentPane.add(panelSuperior);
		panelSuperior.setLayout(null);

		titulo = new JLabel("Relación jerárquica de amigos");
		titulo.setForeground(Color.BLACK);
		titulo.setFont(new Font("Arial", Font.PLAIN, 20));
		titulo.setBounds(8, 0, 300, 30);
		panelSuperior.add(titulo);
		

		btnCerrar = new JButton("");
		btnCerrar.setModel(new MyButtonModel());
		btnCerrar.setBounds(555, 0, 45, 30);
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
		panelInferior.setBounds(10, 16, 600, 613);
		panelInferior.setLayout(null);
		contentPane.add(panelInferior);
		
		
		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setForeground(Color.BLACK);
		scrollPane.setFont(new Font("Arial", Font.PLAIN, 22));
		scrollPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Jerarqu\u00EDa", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		((TitledBorder)scrollPane.getBorder()).setTitleFont(new Font("Arial", Font.PLAIN, 22));
		scrollPane.setBounds(25, 67, 550, 464);
		panelInferior.add(scrollPane);

		table = new JTable();
		table.setFocusable(false);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int pos = table.getSelectedRow();
				Usuario us = listaMostrar.get(pos);
				Vertex ver = red.buscarUsuario(us.getNick());
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
		});
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(30);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("Arial", Font.PLAIN, 15));
		table.setBackground(Color.WHITE);
		scrollPane.setViewportView(table);
		
		btnalante = new JButton("");
		btnalante.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnalante.setIcon(new ImageIcon(PantallaNiveles.class.getResource("/imagenes/derecha40.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnalante.setIcon(new ImageIcon(PantallaNiveles.class.getResource("/imagenes/derecha30.png")));
			}
		});
		btnalante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnalante.setIcon(new ImageIcon(PantallaNiveles.class.getResource("/imagenes/derecha30.png")));
		btnalante.setBounds(520, 535, 55, 42);
		btnalante.setContentAreaFilled(false);
		btnalante.setFocusable(false);
		btnalante.setBorderPainted(false);
		panelInferior.add(btnalante);
		
		btnatras = new JButton((String) null);
		btnatras.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnatras.setIcon(new ImageIcon(PantallaNiveles.class.getResource("/imagenes/izquierda40.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnatras.setIcon(new ImageIcon(PantallaNiveles.class.getResource("/imagenes/izquierda30.png")));
			}
		});
		btnatras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnatras.setIcon(new ImageIcon(PantallaNiveles.class.getResource("/imagenes/izquierda30.png")));
		btnatras.setBounds(25, 535, 55, 42);
		btnatras.setContentAreaFilled(false);
		btnatras.setFocusable(false);
		btnatras.setBorderPainted(false);
		panelInferior.add(btnatras);
		
		JLabel lblNewLabel = new JLabel(":Nivel Anterior");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel.setBounds(83, 546, 143, 20);
		panelInferior.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Nivel Siguiente:");
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(393, 547, 143, 20);
		panelInferior.add(lblNewLabel_1);
		
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
				SeleccionarUsuario su= new SeleccionarUsuario(padre, anterior, red, vUsuario, "Relación jerárquica de amigos");
				su.setVisible(true);
				//anterior.setVisible(true);
			}
		});
		btnAtras.setFont(new Font("Arial", Font.PLAIN, 12));
		btnAtras.setContentAreaFilled(false);
		btnAtras.setFocusable(false);
		btnAtras.setBorderPainted(false);
		btnAtras.setBounds(15, 27, 42, 35);
		panelInferior.add(btnAtras);
		
		mostrarIslas();
	}
	
	private void mostrarIslas(){
		tableModel = new UsuariosTableModel(){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(tableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(130);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(230);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setPreferredWidth(90);
		table.getColumnModel().getColumn(2).setResizable(false);
		scrollPane.getViewport().setBackground(Color.WHITE);
		
		for(int i=0; i<listaMostrar.size(); i++){
			Usuario u = listaMostrar.get(i);
			String[] datos = {u.getNick(), u.getProfesion(), u.getPais()};
			tableModel.addRow(datos);
		}
	}
}
