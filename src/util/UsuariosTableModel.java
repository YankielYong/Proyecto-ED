package util;

import javax.swing.table.DefaultTableModel;

public class UsuariosTableModel extends DefaultTableModel{

	private static final long serialVersionUID = 1L;
	public UsuariosTableModel(){
		String[] nombreColumnas = {"Nombre de Usuario", "Profesi�n", "Pa�s"};
		this.setColumnIdentifiers(nombreColumnas);
	}
}
