package util;

import javax.swing.table.DefaultTableModel;

public class TrabajosTableModel extends DefaultTableModel{

	private static final long serialVersionUID = 1L;
	public TrabajosTableModel(){
		String[] nombreColumnas = {"L�nea de Investigaci�n", "Tema"};
		this.setColumnIdentifiers(nombreColumnas);
	}
}
