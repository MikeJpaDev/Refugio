package UI.src.ui.utils;

import javax.swing.table.DefaultTableModel;

public class TransporteTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 25L;
    public TransporteTableModel() {
        String[] encabezado = {"id", "modalidad", "vehiculo", "Descripción"};
        this.setColumnIdentifiers(encabezado);
    }

    public void agregarTransp(int id, String mod, String veh, String descr){
        Object[] row = new Object[]{id, mod, veh, descr};
        addRow(row);
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
