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

import cu.edu.cujae.ceis.graph.vertex.Vertex;
import util.MyButtonModel;
import util.UsuariosTableModel;
import logica.Red;
import logica.Trabajo;
import logica.Usuario;

public class PerfilTrabajo extends JDialog{

	private static final long serialVersionUID = 1L;
	private Inicial padre;
	private PerfilTrabajo este;
	private JDialog anterior;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel contentPane;
	private JPanel panelSuperior;
	private JPanel panelInferior;
	private JButton btnCerrar;
	private JButton btnAtras;
	private JLabel lblLineaInvestigacion;
	private JLabel lblTema;
	private JLabel logoCUJAE;
	private JLabel lblFoto;
	private JScrollPane scrollPane;
	private JTable table;
	private UsuariosTableModel tableModel;
	private Red red;
	private Vertex vUsuario;
	private Trabajo trabajo;
	private JLabel titulo;

	public PerfilTrabajo(Inicial p, JDialog a, Red r, Trabajo t, Vertex v){
		super(p, true);
		padre = p;
		anterior = a;
		este = this;
		red = r;
		vUsuario = v;
		trabajo = t;
		setResizable(false);
		setUndecorated(true);
		setBounds(pantalla.width/2-325, pantalla.height/2-270, 800, 600);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		panelSuperior = new JPanel();
		panelSuperior.setBackground(new Color(46, 139, 87));
		panelSuperior.setBounds(0, 0, 800, 30);
		contentPane.add(panelSuperior);
		panelSuperior.setLayout(null);
		
		titulo = new JLabel("Información del Trabajo/Proyecto");
		titulo.setForeground(Color.BLACK);
		titulo.setFont(new Font("Arial", Font.PLAIN, 20));
		titulo.setBounds(8, 0, 300, 30);
		panelSuperior.add(titulo);

		btnCerrar = new JButton("");
		btnCerrar.setModel(new MyButtonModel());
		btnCerrar.setBounds(755, 0, 45, 30);
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
		panelInferior.setBounds(0, 30, 800, 570);
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
		btnAtras.setBounds(10, 10, 42, 35);
		panelInferior.add(btnAtras);

		lblLineaInvestigacion = new JLabel("Línea de Investigación: "+t.getLineaInvestigacion());
		lblLineaInvestigacion.setForeground(Color.BLACK);
		lblLineaInvestigacion.setFont(new Font("Arial", Font.PLAIN, 22));
		lblLineaInvestigacion.setBounds(30, 250, 740, 40);
		panelInferior.add(lblLineaInvestigacion);

		lblTema = new JLabel("Tema: "+t.getTema());
		lblTema.setForeground(Color.BLACK);
		lblTema.setFont(new Font("Arial", Font.PLAIN, 22));
		lblTema.setBounds(30, 300, 740, 40);
		panelInferior.add(lblTema);

		logoCUJAE = new JLabel("");
		logoCUJAE.setIcon(new ImageIcon(MiPerfil.class.getResource("/imagenes/logo CUJAE 181x200.png")));
		logoCUJAE.setBounds(178, 30, 181, 200);
		panelInferior.add(logoCUJAE);
		
		lblFoto = new JLabel("");
		lblFoto.setIcon(new ImageIcon(MiPerfil.class.getResource("/imagenes/documento.png")));
		lblFoto.setBounds(400, 30, 234, 200);
		panelInferior.add(lblFoto);

		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setFont(new Font("Arial", Font.PLAIN, 22));
		scrollPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Autores", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		((TitledBorder)scrollPane.getBorder()).setTitleFont(new Font("Arial", Font.PLAIN, 22));
		scrollPane.setBounds(30, 350, 740, 205);
		panelInferior.add(scrollPane);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				este.setVisible(false);
				int pos = table.getSelectedRow();
				Usuario u = trabajo.getAutores().get(pos);
				Vertex vPerf = red.buscarUsuario(u.getNick());
				if(!vPerf.equals(vUsuario)){
					PerfilUsuario ventP = new PerfilUsuario(padre, este, red, vUsuario, vPerf);
					ventP.setVisible(true);
				}
				else{
					MiPerfil ventMiP = new MiPerfil(padre, red, vUsuario);
					ventMiP.setVisible(true);
				}
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFocusable(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(30);
		table.setForeground(Color.BLACK);
		table.setFont(new Font("Arial", Font.PLAIN, 15));
		table.setBackground(Color.WHITE);
		scrollPane.setViewportView(table);

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
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(380);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setPreferredWidth(160);
		table.getColumnModel().getColumn(2).setResizable(false);
		scrollPane.getViewport().setBackground(Color.WHITE);
		mostrarAutores();
	}

	private void mostrarAutores(){
		for(int i=0; i<trabajo.getAutores().size(); i++){
			Usuario u = trabajo.getAutores().get(i);
			String[] datos = {u.getNick(), u.getProfesion(), u.getPais()};
			tableModel.addRow(datos);
		}
	}

}
