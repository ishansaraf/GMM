import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class CSCListCellRenderer extends DefaultListCellRenderer {
	
	private static final long serialVersionUID = 1L;
	private Color selectionColor;

	public CSCListCellRenderer(Color selectionColor) {
		this.selectionColor = selectionColor;
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		list.setSelectionBackground(Main.FIELD_COLOR);
		list.setSelectionForeground(Main.TEXT_COLOR);
		if (isSelected) {
        	setBackground(this.selectionColor);
        }
        else {
        	setBackground(Main.BG_COLOR);
        }
        setForeground(Main.TEXT_COLOR);
        setText((String)value);
        setFont(list.getFont());
        return this;
    }
}
