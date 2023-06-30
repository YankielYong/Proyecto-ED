package util;

import javax.swing.table.DefaultTableModel;

public class NotificacionesTableModel extends DefaultTableModel{

	private static final long serialVersionUID = 1L;
	public NotificacionesTableModel(){
		String[] nombreColumnas = {"" ,"Notificaciones"};
		this.setColumnIdentifiers(nombreColumnas);
	}

}
