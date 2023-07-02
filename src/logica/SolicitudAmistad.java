package logica;

import java.io.Serializable;

import cu.edu.cujae.ceis.graph.vertex.Vertex;

public class SolicitudAmistad implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	
	public SolicitudAmistad(Vertex v){
		usuario = (Usuario)v.getInfo();
	}
	
	public Usuario getUsuario(){
		return usuario;
	}
}
