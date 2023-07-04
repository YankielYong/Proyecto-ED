package logica;

import java.util.LinkedList;

import cu.edu.cujae.ceis.graph.vertex.Vertex;

public class Comunidad {
	private LinkedList<Vertex> usuarios;
	
	public Comunidad(LinkedList<Vertex> usuarios){
		this.usuarios = usuarios;
	}

	public LinkedList<Vertex> getUsuarios() {
		return usuarios;
	}
}
