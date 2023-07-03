package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
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

import util.IconCellRenderer;
import util.MyButtonModel;
import util.NotificacionesTableModel;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import logica.Notificacion;
import logica.Red;
import logica.Usuario;

public class VentanaNotificaciones extends JDialog{

	private static final long serialVersionUID = 1L;
	private Inicial padre;
	private JPanel contentPane;
	private JPanel panelSuperior;
	private JPanel panelInferior;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JButton btnCerrar;
	private JButton btnLimpiar;
	private JScrollPane scrollPane;
	private Red red;
	private Vertex vUsuario;
	private Usuario usuario;
	private JTable table;
	private NotificacionesTableModel model;
	private ArrayList<Notificacion> notificaciones;
	private JLabel titulo;

	public VentanaNotificaciones(Inicial p, Red r, Vertex v){
		super(p, true);
		padre = p;
		red = r;
		vUsuario = v;
		usuario = (Usuario)vUsuario.getInfo();
		notificaciones = usuario.getNotificaciones();
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
		
		titulo = new JLabel("Notificaciones");
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

		panelInferior = new JPanel();
		panelInferior.setBackground(Color.WHITE);
		panelInferior.setBounds(0, 30, 1000, 570);
		panelInferior.setLayout(null);
		contentPane.add(panelInferior);

		btnLimpiar = new JButton("");
		btnLimpiar.setIcon(new ImageIcon(VentanaNotificaciones.class.getResource("/imagenes/limpiar 20.png")));
		btnLimpiar.setModel(new MyButtonModel());
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notificaciones.clear();
				try {
					red.modificarUsuarioEnFichero(usuario);
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
				dispose();
				int cantN = usuario.getNotificaciones().size();
				int cantS = usuario.getSolicitudes().size();
				padre.actualizarCantidades(cantN, cantS);
				VentanaNotificaciones ventana = new VentanaNotificaciones(padre, red, vUsuario);
				ventana.setVisible(true);
			}
		});
		btnLimpiar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnLimpiar.setContentAreaFilled(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnLimpiar.setContentAreaFilled(false);
			}
		});
		btnLimpiar.setBounds(910, 0, 45, 30);
		btnLimpiar.setBackground(new Color(0, 255, 127));
		btnLimpiar.setFocusable(false);
		btnLimpiar.setBorderPainted(false);
		btnLimpiar.setContentAreaFilled(false);
		panelSuperior.add(btnLimpiar);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, -20, 1000, 590);
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setFont(new Font("Arial", Font.PLAIN, 22));

		table = new JTable();
		table.setGridColor(Color.WHITE);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFocusable(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(80);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("Arial", Font.PLAIN, 22));
		table.setBackground(Color.WHITE);
		scrollPane.setViewportView(table);
		panelInferior.add(scrollPane);

		Inicializar();
	}

	private void Inicializar(){
		model = new NotificacionesTableModel(){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(90);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(910);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getTableHeader().setVisible(false);
		table.setCellSelectionEnabled(false);
		scrollPane.getViewport().setBackground(Color.white);
		table.setDefaultRenderer(Object.class, new IconCellRenderer());

		int cantNotificaciones = notificaciones.size();
		if(cantNotificaciones != 0){
			for(int i=cantNotificaciones-1; i>=0; i--){
				Notificacion n = notificaciones.get(i);
				Object[] fila = {new JLabel(n.getImage()), n.getMensaje()};
				model.addRow(fila);
			}
		}
		else{
			Object[] fila = {new JLabel(), "No tiene notificaciones"};
			model.addRow(fila);
		}
	}

}
