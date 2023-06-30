package logica;

import interfaz.MiPerfil;

import java.io.Serializable;

import javax.swing.ImageIcon;

import cu.edu.cujae.ceis.graph.vertex.Vertex;

public class Notificacion implements Serializable{

	private static final long serialVersionUID = 1L;

	public static int ENVIA_SOLICITUD = 0;
	public static int ACEPTA_SOLICITUD = 1;
	public static int CANCELA_SOLICITUD = 2;
	public static int RECHAZA_SOLICITUD = 3;
	public static int ELIMINA_AMIGO = 4;

	private Vertex vertex;
	private String mensaje;
	private Usuario usuario;
	private ImageIcon image;
	private int type;

	public Notificacion(Vertex v, int t){
		vertex = v;
		type = t;
		usuario = (Usuario)vertex.getInfo();
		mensaje = "El usuario "+"\""+usuario.getNick()+"\"";
		switch(type){
		case 0: mensaje += " te ha enviado una solicitud de amistad"; 
		image = new ImageIcon(MiPerfil.class.getResource("/imagenes/env solc.png"));
		break;
		case 1: mensaje += " ha aceptado tu solicitud de amistad";
		image = new ImageIcon(MiPerfil.class.getResource("/imagenes/acp solc.png"));
		break;
		case 2: mensaje += " ha cancelado la solicitud de amistad";
		image = new ImageIcon(MiPerfil.class.getResource("/imagenes/canc solc.png"));
		break;
		case 3: mensaje += " ha rechazado tu solicitud de amistad";
		image = new ImageIcon(MiPerfil.class.getResource("/imagenes/canc solc.png"));
		break;
		case 4: mensaje += " te ha eliminado de su lista de amigos";
		image = new ImageIcon(MiPerfil.class.getResource("/imagenes/canc solc.png"));
		break;
		}
	}

	public Vertex getVertex() {
		return vertex;
	}
	public String getMensaje() {
		return mensaje;
	}
	public ImageIcon getImage() {
		return image;
	}
}
