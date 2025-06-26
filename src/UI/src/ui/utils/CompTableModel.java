package UI.src.ui.utils;

import javax.swing.table.DefaultTableModel;

public class CompTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 3L;
    public CompTableModel() {
        String[] encabezado = {"Id","Nombre", "Provincia", "Tipo de Complementario", "Responsable"};
        this.setColumnIdentifiers(encabezado);
    }

    public void agregarComp(String id,String nombre, String pro, String tipo, String responsable){
        Object[] row = new Object[]{id, nombre, pro, tipo, responsable};
        addRow(row);
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
