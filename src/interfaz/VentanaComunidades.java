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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import util.ComunidadTableModel;
import util.MyButtonModel;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import logica.Comunidad;
import logica.Red;

public class VentanaComunidades extends JDialog{

	private static final long serialVersionUID = 1L;
	private Inicial padre;
	private Reportes anterior;
	private VentanaComunidades este;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel contentPane;
	private JPanel panelSuperior;
	private JPanel panelInferior;
	private JButton btnAtras;
	private JLabel titulo;
	private JLabel orden;
	private JScrollPane scrollPane;
	private JTable table;
	private ComunidadTableModel tableModel;
	private Red red;
	private Vertex vUsuario;
	private ArrayList<Comunidad> comunidades;

	public VentanaComunidades(Inicial p, Reportes a, Red r, Vertex v){
		super(p, true);
		padre = p;
		anterior = a;
		este = this;
		red = r;
		vUsuario = v;
		comunidades = red.obtenerComunidades();
		setUndecorated(true);
		setResizable(false);
		setBounds(pantalla.width/2-175, pantalla.height/2-270, 500, 600);
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

		titulo = new JLabel("Comunidades");
		titulo.setForeground(Color.BLACK);
		titulo.setFont(new Font("Arial", Font.PLAIN, 20));
		titulo.setBounds(8, 0, 200, 30);
		panelSuperior.add(titulo);

		panelInferior = new JPanel();
		panelInferior.setBackground(Color.WHITE);
		panelInferior.setBounds(0, 30, 500, 570);
		panelInferior.setLayout(null);
		contentPane.add(panelInferior);

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
		btnAtras.setBounds(20, 10, 42, 35);
		panelInferior.add(btnAtras);
		
		orden = new JLabel("Seleccione una comunidad para verla");
		orden.setBounds(80, 10, 400, 35);
		orden.setForeground(Color.black);
		orden.setFont(new Font("Arial", Font.PLAIN, 22));
		panelInferior.add(orden);
		
		scrollPane = new JScrollPane();
		scrollPane.setForeground(Color.BLACK);
		scrollPane.setFont(new Font("Arial", Font.PLAIN, 12));
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(20, 50, 460, 500);
		panelInferior.add(scrollPane);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFocusable(false);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int pos = table.getSelectedRow();
				Comunidad c = comunidades.get(pos);
				dispose();
				PerfilComunidad pc = new PerfilComunidad(padre, este, red, vUsuario, c, pos);
				pc.setVisible(true);
			}
		});
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(40);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("Arial", Font.PLAIN, 20));
		table.setBackground(Color.WHITE);
		scrollPane.setViewportView(table);
		
		inicializar();
	}
	
	private void inicializar(){
		tableModel = new ComunidadTableModel(){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(tableModel);
		scrollPane.getViewport().setBackground(Color.WHITE);
		
		for(int i=0; i<comunidades.size(); i++){
			String[] datos = {"Comunidad "+(i+1)};
			tableModel.addRow(datos);
		}
	}

}
