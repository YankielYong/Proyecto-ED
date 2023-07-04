package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.border.TitledBorder;

import util.MyButtonModel;
import util.UsuariosTableModel;
import logica.Red;
import logica.Usuario;
import cu.edu.cujae.ceis.graph.vertex.Vertex;

import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SeleccionarUsuario extends JDialog{

	private static final long serialVersionUID = 1L;
	private Inicial padre;
	private Reportes anterior;
	private SeleccionarUsuario este;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel contentPane;
	private JPanel panelSuperior;
	private JPanel panelInferior;
	private JButton btnAtras;
	private JScrollPane scrollPane;
	private JTable table;
	private UsuariosTableModel tableModel;
	private JLabel titulo;
	private Red red;
	private Vertex vUsuario;
	private ArrayList<Usuario> usuarios;
	private ArrayList<Usuario> listaMostrar;
	private JTextField txtBuscar;
	private JLabel lblBuscar;

	public SeleccionarUsuario(Inicial p, Reportes a, Red r, Vertex v, String tit){
		super(p, true);
		padre = p;
		anterior = a;
		este = this;
		red = r;
		vUsuario = v;
		usuarios = new ArrayList<Usuario>();
		LinkedList<Vertex> lista = red.getGrafo().getVerticesList();
		Iterator<Vertex> iter = lista.iterator();
		while(iter.hasNext()){
			Usuario u = (Usuario)iter.next().getInfo();
			usuarios.add(u);
		}
		listaMostrar = usuarios;
		setUndecorated(true);
		setResizable(false);
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
		panelSuperior.setLayout(null);
		contentPane.add(panelSuperior);

		titulo = new JLabel(tit);
		titulo.setForeground(Color.BLACK);
		titulo.setFont(new Font("Arial", Font.PLAIN, 20));
		titulo.setBounds(8, 0, 400, 30);
		panelSuperior.add(titulo);

		panelInferior = new JPanel();
		panelInferior.setBackground(Color.WHITE);
		panelInferior.setBounds(0, 30, 600, 570);
		panelInferior.setLayout(null);
		contentPane.add(panelInferior);

		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setForeground(Color.BLACK);
		scrollPane.setFont(new Font("Arial", Font.PLAIN, 22));
		scrollPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Seleccione un usuario", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
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
				dispose();
				if(titulo.getText().equals("Relación jerárquica de amigos")){
					red.obtenerRelacionJerDeAmigos(ver);
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
					for(int i=0; i<usuarios.size(); i++){
						Usuario u = usuarios.get(i);
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
					listaMostrar = usuarios;
				}
				inicializar();
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

		inicializar();
	}

	private void inicializar(){
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

		mostrarUsuarios();
	}

	private void mostrarUsuarios(){
		for(int i=0; i<listaMostrar.size(); i++){
			Usuario u = listaMostrar.get(i);
			String[] datos = {u.getNick(), u.getProfesion(), u.getPais()};
			tableModel.addRow(datos);
		}
	}
}
