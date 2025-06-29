package UI.src.ui.utils;

import javax.swing.table.DefaultTableModel;

public class ProveedTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 10L;
    public ProveedTableModel() {
        String[] encabezado = {"Nombre", "Provincia", "Tipo", "Responsable"};
        this.setColumnIdentifiers(encabezado);
    }

    public void agregarProveed(String nombre, String pro, String tipo, String responsable){
        Object[] row = new Object[]{nombre, pro, tipo, responsable,};
        addRow(row);
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
