package logica;

import java.io.Serializable;

import cu.edu.cujae.ceis.graph.vertex.Vertex;

public class SolicitudAmistad implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Vertex vertex;
	
	public SolicitudAmistad(Vertex v){
		vertex = v;
	}
	
	public Vertex getVertex(){
		return vertex;
	}
}
