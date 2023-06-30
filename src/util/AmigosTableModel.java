package util;

import javax.swing.table.DefaultTableModel;

public class AmigosTableModel extends DefaultTableModel{

	private static final long serialVersionUID = 1L;
	public AmigosTableModel(){
		String[] nombreColumnas = {"Nombre de Usuario", "Profesión", "País", "Peso"};
		this.setColumnIdentifiers(nombreColumnas);
	}
}
