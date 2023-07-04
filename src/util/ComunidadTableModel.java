package util;

import javax.swing.table.DefaultTableModel;

public class ComunidadTableModel extends DefaultTableModel{

	private static final long serialVersionUID = 1L;
	public ComunidadTableModel(){
		String[] nombreColumnas = {"Comunidades"};
		this.setColumnIdentifiers(nombreColumnas);
	}

}
