package logica;

import java.io.Serializable;
import java.util.ArrayList;

import util.Validaciones;

public class Trabajo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String tema;
	private String lineaInvestigacion;
	private int identificador;
	private ArrayList<Usuario> autores;

	public Trabajo(String lineaInvestigacion, String tema) throws IllegalArgumentException{
		setTema(tema);
		setLineaInvestigacion(lineaInvestigacion);
		generarIdentificador();
		autores = new ArrayList<Usuario>();
	}
	
	public Trabajo(String tema, String lineaInvestigacion, ArrayList<Usuario> autores){
		setTema(tema);
		setLineaInvestigacion(lineaInvestigacion);
		generarIdentificador();
		this.autores = autores;
	}

	public ArrayList<Usuario> getAutores() {
		return autores;
	}
	public String getTema() {
		return tema;
	}
	private void setTema(String tema) throws IllegalArgumentException{
		try{
			Validaciones.tema(tema);
			this.tema = tema;
		}catch(IllegalArgumentException e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	public String getLineaInvestigacion() {
		return lineaInvestigacion;
	}
	private void setLineaInvestigacion(String lineaInvestigacion) {
		try{
			Validaciones.lineaInvestigacion(lineaInvestigacion);
			this.lineaInvestigacion = lineaInvestigacion;
		}catch(IllegalArgumentException e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	public int getIdentificador() {
		return identificador;
	}
	private void generarIdentificador(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lineaInvestigacion == null) ? 0 : lineaInvestigacion.hashCode());
		result = prime * result + ((tema == null) ? 0 : tema.hashCode());
		this.identificador = result;
	}

}
