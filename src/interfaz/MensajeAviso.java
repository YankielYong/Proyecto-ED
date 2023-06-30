package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

import util.MyButtonModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MensajeAviso extends JDialog{
	private static final long serialVersionUID = 1L;

	public final static int CORRECTO = 0;
	public final static int ERROR = 1;
	public final static int INFORMACION = 2;

	private JDialog anterior;
	private JPanel contentPane;
	private JPanel panelSuperior;
	private JPanel panelInferior;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
	private String mensaje;
	private JLabel lblMensaje;
	private JLabel tipo;
	private JLabel imagen;
	private int type;
	private boolean valor;

	public MensajeAviso(Inicial p, JDialog a, String m, int t){
		super(p, true);
		anterior = a;
		mensaje = m;
		type = t;
		valor = true;
		setResizable(false);
		setUndecorated(true);
		setBounds(pantalla.width/2-135, pantalla.height/2-45, 420, 150);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		panelSuperior = new JPanel();
		panelSuperior.setBackground(new Color(46, 139, 87));
		panelSuperior.setBounds(0, 0, 420, 30);
		contentPane.add(panelSuperior);
		panelSuperior.setLayout(null);

		panelInferior = new JPanel();
		panelInferior.setBackground(Color.WHITE);
		panelInferior.setBounds(0, 30, 420, 120);
		panelInferior.setLayout(null);
		contentPane.add(panelInferior);

		tipo = new JLabel("");
		tipo.setFont(new Font("Arial", Font.PLAIN, 18));
		tipo.setForeground(Color.BLACK);
		tipo.setBounds(8, 0, 150, 30);
		panelSuperior.add(tipo);
		switch(type){
		case 0: tipo.setText("Correcto"); break;
		case 1: tipo.setText("Error"); break;
		case 2: tipo.setText("Información"); break;
		}

		imagen = new JLabel("");
		imagen.setBounds(15, 15, 50, 50);
		panelInferior.add(imagen);
		switch(type){
		case 0: imagen.setIcon(new ImageIcon(MensajeAviso.class.getResource("/imagenes/icono verificacion 50.png"))); break;
		case 1: imagen.setIcon(new ImageIcon(MensajeAviso.class.getResource("/imagenes/icono error 50.png"))); break;
		case 2: imagen.setIcon(new ImageIcon(MensajeAviso.class.getResource("/imagenes/icono advertencia 50.png"))); break;
		}

		lblMensaje = new JLabel(mensaje);
		lblMensaje.setFont(new Font("Arial", Font.BOLD, 16));
		lblMensaje.setBounds(70, 20, 350, 40);
		panelInferior.add(lblMensaje);

		btnAceptar = new JButton("Aceptar");
		btnAceptar.setModel(new MyButtonModel());
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				if(type==1)
					anterior.setVisible(true);
			}
		});
		btnAceptar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnAceptar.setBorder(new LineBorder(new Color(0, 255, 127), 3));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAceptar.setBorder(null);
			}
		});
		btnAceptar.setForeground(Color.BLACK);
		btnAceptar.setBorder(null);
		btnAceptar.setFont(new Font("Arial", Font.PLAIN, 18));
		btnAceptar.setBackground(new Color(46, 139, 87));
		btnAceptar.setBounds(160, 75, 100, 30);
		btnAceptar.setFocusable(false);
		panelInferior.add(btnAceptar);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setModel(new MyButtonModel());
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				valor = false;
				anterior.setVisible(true);
			}
		});
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
		btnCancelar.setForeground(Color.BLACK);
		btnCancelar.setBorder(null);
		btnCancelar.setFont(new Font("Arial", Font.PLAIN, 18));
		btnCancelar.setBackground(new Color(46, 139, 87));
		btnCancelar.setBounds(280, 75, 100, 30);
		btnCancelar.setFocusable(false);
		if(type == 2)
			panelInferior.add(btnCancelar);
	}

	public void agrandar(){
		this.setBounds(getX()-50, getY(), 470, 150);
		panelSuperior.setBounds(0, 0, 470, 30);
		panelInferior.setBounds(0, 30, 470, 120);
		btnAceptar.setBounds(185, 75, 100, 30);
		lblMensaje.setBounds(70, 20, 400, 40);
	}
	
	public boolean getValor(){
		return valor;
	}

}
