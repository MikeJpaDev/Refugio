package UI.src.ui.utils;

import javax.swing.table.DefaultTableModel;

public class AlimTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 2L;
    public AlimTableModel() {
        String[] encabezado = {"Id","Nombre", "Provincia", "Tipo de Alimento", "Responsable"};
        this.setColumnIdentifiers(encabezado);
    }

    public void agregarAlim(String id,String nombre, String pro, String tipo, String responsable){
        Object[] row = new Object[]{id, nombre, pro, tipo, responsable};
        addRow(row);
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
