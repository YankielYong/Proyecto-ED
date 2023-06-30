package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class PropiedadesComboBox extends BasicComboBoxUI{

	public static ComboBoxUI createUI(JComponent com){
		return new PropiedadesComboBox();
	}

	@Override
	protected JButton createArrowButton(){
		JButton btn = new JButton();
		btn.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(PropiedadesComboBox.class.getResource("/imagenes/flecha abajo 25.png"))));
		btn.setBackground(new Color(46, 139, 87));
		//btn.setContentAreaFilled(false);
		btn.setFocusable(false);
		btn.setBorderPainted(false);
		return btn;
	}

	@Override
	public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
		g.setColor(new Color(46, 139, 87));
		g.drawLine(bounds.x+bounds.width-1, bounds.y, bounds.x+bounds.width-1, bounds.y+bounds.height);
		g.drawLine(bounds.x+bounds.width-2, bounds.y, bounds.x+bounds.width-2, bounds.y+bounds.height);
		g.drawLine(bounds.x+bounds.width-3, bounds.y, bounds.x+bounds.width-3, bounds.y+bounds.height);
	}

	@Override
	protected ListCellRenderer<Object> createRenderer() {
		
		return new DefaultListCellRenderer(){

			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				list.setSelectionBackground(new Color(46, 139, 87));
				return this;
			}
		};
	}

}
