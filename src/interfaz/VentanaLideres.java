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

import logica.Red;
import logica.Usuario;
import util.MyButtonModel;
import util.UsuariosTableModel;
import cu.edu.cujae.ceis.graph.vertex.Vertex;

public class VentanaLideres extends JDialog{

	private static final long serialVersionUID = 1L;
	private Inicial padre;
	private Reportes anterior;
	private VentanaLideres este;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel contentPane;
	private JPanel panelSuperior;
	private JPanel panelInferior;
	private JButton btnCerrar;
	private JButton btnAtras;
	private JLabel titulo;
	private JScrollPane scrollPane;
	private JTable table;
	private UsuariosTableModel tableModel;
	private Red red;
	private Vertex vUsuario;
	private ArrayList<Usuario> listaMostrar;
	private ArrayList<Usuario> lideres;
	private JTextField txtBuscar;
	private JLabel lblBuscar;
	
	public VentanaLideres(Inicial p, Reportes a, Red r, Vertex v){
		super(p, true);
		padre = p;
		anterior = a;
		este = this;
		red = r;
		vUsuario = v;
		lideres = red.obtenerLideresInvestigacion();
		listaMostrar = lideres;
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

		titulo = new JLabel("Líderes de Investigación");
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
		panelInferior.setBounds(0, 30, 600, 570);
		panelInferior.setLayout(null);
		contentPane.add(panelInferior);
		
		btnAtras = new JButton("Regresar");
		btnAtras.setModel(new MyButtonModel());
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				anterior.setVisible(true);
			}
		});
		btnAtras.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnAtras.setBorder(new LineBorder(new Color(0, 255, 127), 3));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAtras.setBorder(null);
			}
		});
		btnAtras.setBorder(null);
		btnAtras.setBounds(450, 505, 120, 40);
		btnAtras.setForeground(Color.BLACK);
		btnAtras.setBackground(new Color(46, 139, 87));
		btnAtras.setFont(new Font("Arial", Font.BOLD, 22));
		btnAtras.setFocusable(false);
		panelInferior.add(btnAtras);
		
		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setForeground(Color.BLACK);
		scrollPane.setFont(new Font("Arial", Font.PLAIN, 22));
		scrollPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Líderes de Investigación", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		((TitledBorder)scrollPane.getBorder()).setTitleFont(new Font("Arial", Font.PLAIN, 22));
		scrollPane.setBounds(25, 55, 550, 425);
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
		
		txtBuscar = new JTextField();
		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				listaMostrar = new ArrayList<Usuario>();
				String nombre=txtBuscar.getText();
				if(nombre.length() > 0){
					for(int i=0; i<lideres.size(); i++){
						Usuario u = lideres.get(i);
						String name = u.getNick();
						if(nombre.length()<=name.length()){
							boolean igual = true;
							for(int j=0; j<nombre.length() && igual;j++){
								if(!((Character)nombre.charAt(j)).equals((Character)name.charAt(j))){
									igual = false;
								}
							}
							if(igual)
								listaMostrar.add(u);
						}
					}
				}
				else{
					listaMostrar = lideres;
				}
				mostrarLideres();
			}
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
		txtBuscar.setForeground(Color.BLACK);
		txtBuscar.setFont(new Font("Arial", Font.PLAIN, 16));
		txtBuscar.setBounds(25, 25, 500, 30);
		panelInferior.add(txtBuscar);

		lblBuscar = new JLabel("");
		lblBuscar.setIcon(new ImageIcon(DescubrirPersonas.class.getResource("/imagenes/lupa buscar 25.png")));
		lblBuscar.setBounds(535, 28, 25, 25);
		panelInferior.add(lblBuscar);
	
		mostrarLideres();
	}
	
	private void mostrarLideres(){
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
