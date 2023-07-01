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
	public static int ELIMINA_TRABAJO = 5;
	public static int PUBLICA_TRABAJO = 6;

	private String mensaje;
	private Usuario usuario;
	private ImageIcon image;
	private int type;

	public Notificacion(Vertex v, int t){
		type = t;
		usuario = (Usuario)v.getInfo();
		mensaje = "El usuario "+"\""+usuario.getNick()+"\"";
		inicializar();
	}
	
	private void inicializar(){
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
		case 5: mensaje += " ha eliminado un trabajo en el que colaboraste";
		image = new ImageIcon(MiPerfil.class.getResource("/imagenes/elim trab.png"));
		break;
		case 6: mensaje += " ha publicado un trabajo en el que colaboraste";
		image = new ImageIcon(MiPerfil.class.getResource("/imagenes/agr trab.png"));
		break;
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}
	public String getMensaje() {
		return mensaje;
	}
	public ImageIcon getImage() {
		return image;
	}
}
