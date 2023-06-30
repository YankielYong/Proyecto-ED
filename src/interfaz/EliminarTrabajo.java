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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import util.MyButtonModel;
import util.TrabajosTableModel;
import logica.Red;
import logica.Trabajo;
import cu.edu.cujae.ceis.graph.vertex.Vertex;

public class EliminarTrabajo extends JDialog{
	private static final long serialVersionUID = 1L;
	private Inicial padre;
	private MiPerfil anterior;
	private EliminarTrabajo este;
	private JPanel contentPane;
	private JPanel panelSuperior;
	private JPanel panelInferior;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private JButton btnEliminar;
	private JButton btnCancelar;
	private JScrollPane scrollPane;
	private JTable table;
	private TrabajosTableModel tableModel;
	private JLabel lblOrden;
	private Red red;
	private Vertex vUsuario;
	private JLabel titulo;
	private LinkedList<Trabajo> trabajos;

	public EliminarTrabajo(Inicial p, MiPerfil a, Red r, Vertex v){
		super(p, true);
		padre = p;
		anterior = a;
		red = r;
		este = this;
		vUsuario = v;
		trabajos = red.trabajosDeUsuario(vUsuario);
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

		titulo = new JLabel("Eliminar Trabajo/Proyecto");
		titulo.setForeground(Color.BLACK);
		titulo.setFont(new Font("Arial", Font.PLAIN, 20));
		titulo.setBounds(8, 0, 300, 30);
		panelSuperior.add(titulo);

		panelInferior = new JPanel();
		panelInferior.setBackground(Color.white);
		panelInferior.setBounds(0, 30, 1000, 570);
		contentPane.add(panelInferior);
		panelInferior.setLayout(null);

		lblOrden = new JLabel("Seleccione el/los trabajos que desea eliminar");
		lblOrden.setFont(new Font("Arial", Font.PLAIN, 22));
		lblOrden.setBounds(25, 25, 450, 40);
		panelInferior.add(lblOrden);

		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setFont(new Font("Arial", Font.PLAIN, 22));
		scrollPane.setBounds(25, 65, 950, 400);
		panelInferior.add(scrollPane);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.setModel(new MyButtonModel());
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnEliminar.setBorder(null);
				int cant = table.getSelectedRowCount();
				dispose();
				if(cant != 0){
					String mens;
					if(cant == 1)
						mens = "¿Desea eliminar este trabajo?";
					else
						mens = "¿Desea eliminar estos trabajos?";
					MensajeAviso m = new MensajeAviso(padre, este, mens, MensajeAviso.INFORMACION);
					m.setVisible(true);
					if(m.getValor()){
						int[] sel = table.getSelectedRows();
						for(int i=0; i<cant; i++){
							Trabajo t = trabajos.get(sel[i]);
							String linea = t.getLineaInvestigacion();
							String tema = t.getTema();
							red.eliminarTrabajo(linea, tema);
						}
						anterior.ponerTrabajos();
						anterior.setVisible(true);
					}
				}
				else{
					MensajeAviso m = new MensajeAviso(padre, este, "No seleccionó ningún trabajo", MensajeAviso.ERROR);
					m.setVisible(true);
					anterior.ponerTrabajos();
					anterior.setVisible(true);
				}
			}
		});
		btnEliminar.setForeground(Color.BLACK);
		btnEliminar.setBorder(null);
		btnEliminar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnEliminar.setBorder(new LineBorder(new Color(0, 255, 127), 3));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnEliminar.setBorder(null);
			}
		});
		btnEliminar.setFont(new Font("Arial", Font.BOLD, 22));
		btnEliminar.setBackground(new Color(46, 139, 87));
		btnEliminar.setBounds(630, 500, 150, 40);
		btnEliminar.setFocusable(false);
		panelInferior.add(btnEliminar);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setModel(new MyButtonModel());
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				anterior.setVisible(true);
			}
		});
		btnCancelar.setForeground(Color.BLACK);
		btnCancelar.setBorder(null);
		btnCancelar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnCancelar.setBorder(new LineBorder(new Color(0, 255, 127), 3));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnCancelar.setBorder(null);
			}
		});
		btnCancelar.setFont(new Font("Arial", Font.BOLD, 22));
		btnCancelar.setBackground(new Color(46, 139, 87));
		btnCancelar.setBounds(820, 500, 150, 40);
		btnCancelar.setFocusable(false);
		panelInferior.add(btnCancelar);

		table = new JTable();
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
		Iterator<Trabajo> iter = trabajos.iterator();
		while(iter.hasNext()){
			Trabajo t = iter.next();
			String[] datos = {t.getLineaInvestigacion(), t.getTema()};
			tableModel.addRow(datos);
		}
	}

}
