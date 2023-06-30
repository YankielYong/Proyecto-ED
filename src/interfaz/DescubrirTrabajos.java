package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import cu.edu.cujae.ceis.graph.vertex.Vertex;
import util.MyButtonModel;
import util.TrabajosTableModel;
import logica.Red;
import logica.Trabajo;

public class DescubrirTrabajos extends JDialog{
	
	private static final long serialVersionUID = 1L;
	private Inicial padre;
	private DescubrirTrabajos este;
	private JPanel contentPane;
	private JPanel panelSuperior;
	private JPanel panelInferior;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JButton btnCerrar;
	private JScrollPane scrollPane;
	private JTable table;
	private TrabajosTableModel tableModel;
	private Red red;
	private Vertex vUsuario;
	private LinkedList<Trabajo> trabajos;
	private JLabel titulo;
	
	public DescubrirTrabajos(Inicial p, Red r, Vertex v){
		super(p, true);
		padre = p;
		este = this;
		red = r;
		vUsuario = v;
		trabajos = red.getTrabajos();
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
		
		titulo = new JLabel("Trabajos y/o Proyectos");
		titulo.setForeground(Color.BLACK);
		titulo.setFont(new Font("Arial", Font.PLAIN, 20));
		titulo.setBounds(8, 0, 300, 30);
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
		
		panelInferior = new JPanel();
		panelInferior.setBackground(Color.WHITE);
		panelInferior.setBounds(0, 30, 1000, 570);
		panelInferior.setLayout(null);
		contentPane.add(panelInferior);
		
		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setFont(new Font("Arial", Font.PLAIN, 22));
		scrollPane.setBounds(0, 0, 1000, 570);
		panelInferior.add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				este.setVisible(false);
				int pos = table.getSelectedRow();
				Trabajo t = trabajos.get(pos);
				PerfilTrabajo ventanaTrab = new PerfilTrabajo(padre, este, red, t, vUsuario);
				ventanaTrab.setVisible(true);
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFocusable(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(40);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("Arial", Font.PLAIN, 16));
		table.setBackground(Color.WHITE);
		scrollPane.setViewportView(table);
		
		inicializar();
	}
	
	private void inicializar(){
		tableModel = new TrabajosTableModel(){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(tableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(280);
		table.getColumnModel().getColumn(1).setPreferredWidth(670);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		scrollPane.getViewport().setBackground(Color.WHITE);
		mostrarTrabajos();
	}
	
	private void mostrarTrabajos(){
		Iterator<Trabajo> iter = trabajos.iterator();
		while(iter.hasNext()){
			Trabajo t = iter.next();
			String[] datos = {t.getLineaInvestigacion(), t.getTema()};
			tableModel.addRow(datos);
		}
	}
}
