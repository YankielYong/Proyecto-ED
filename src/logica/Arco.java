package logica;

import java.io.Serializable;

public class Arco implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int origen;
	private int destino;
	
	public Arco(int origen, int destino) {
		this.origen = origen;
		this.destino = destino;
	}
	public int getOrigen() {
		return origen;
	}
	public void setOrigen(int origen) {
		this.origen = origen;
	}
	public int getDestino() {
		return destino;
	}
	public void setDestino(int destino) {
		this.destino = destino;
	}
}
