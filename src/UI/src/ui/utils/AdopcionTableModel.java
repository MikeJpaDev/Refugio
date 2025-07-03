package UI.src.ui.utils;

import javax.swing.table.DefaultTableModel;

public class AdopcionTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 32L;
    public AdopcionTableModel() {
        String[] encabezado = {"Id","Animal", "fecha", "precio"};
        this.setColumnIdentifiers(encabezado);
    }

    public void agregarAdop(String id,String nombre, String fecha, Double precio){
        Object[] row = new Object[]{id, nombre, fecha, precio};
        addRow(row);
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
