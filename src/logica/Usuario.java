package logica;

import java.io.Serializable;
import java.util.ArrayList;

import util.*;

public class Usuario implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String nick;
	private String password;
	private String pais;
	private String profesion;
	private ArrayList<Notificacion> notificaciones;
	private ArrayList<SolicitudAmistad> solicitudes;

	public Usuario(String nick, String password, String pais, String profesion) {
		setNick(nick);
		setPassword(password);
		this.pais = pais;
		this.profesion = profesion;
		notificaciones = new ArrayList<Notificacion>();
		solicitudes = new ArrayList<SolicitudAmistad>();
	}

	public String getNick() {
		return nick;
	}

	public ArrayList<Notificacion> getNotificaciones() {
		return notificaciones;
	}

	public ArrayList<SolicitudAmistad> getSolicitudes() {
		return solicitudes;
	}

	public void setNick(String nick) throws IllegalArgumentException{
		try{
			Validaciones.nick(nick);
			this.nick = nick;
		}catch(IllegalArgumentException e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws IllegalArgumentException{
		try{
			Validaciones.password(password);
			this.password = password;
		}catch(IllegalArgumentException e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getProfesion() {
		return profesion;
	}

	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}
}
