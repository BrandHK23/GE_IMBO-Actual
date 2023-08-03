package org.imbo.view.inicioPanel;
import org.apache.batik.css.engine.value.RGBColorValue;
import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StatusRowRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Centro el texto horizontalmente en todas las celdas de la tabla
        ((DefaultTableCellRenderer) component).setHorizontalAlignment(CENTER);

        String status = table.getValueAt(row, 7).toString(); // 7 es el índice de la columna "Status"

        if (isSelected){
            component.setBackground(rgb(84,154,199)); // Color de fondo cuando la fila está seleccionada
            component.setForeground(Color.WHITE); // Color del texto cuando la fila está seleccionada

        }else if (status.equals("Etapa 4")) {
            component.setBackground(rgb(88, 214, 141));
            component.setForeground(table.getForeground());

        }else {
            component.setBackground(table.getBackground());
            component.setForeground(table.getForeground());
        }



        return component;
    }

    private Color rgb(int i, int i1, int i2) {
        return new Color(i, i1, i2);
    }

}
