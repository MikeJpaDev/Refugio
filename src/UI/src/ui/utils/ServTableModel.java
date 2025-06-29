package UI.src.ui.utils;

import javax.swing.table.DefaultTableModel;

public class ServTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 15L;
    public ServTableModel() {
        String[] encabezado = {"Precio(CUP)", "Recarga(CUP)", "Duración(Dias)", "Descripción"};
        this.setColumnIdentifiers(encabezado);
    }

    public void agregarServ(String prec, String rec, String durac, String descr){
        Object[] row = new Object[]{prec, rec, durac, descr};
        addRow(row);
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
