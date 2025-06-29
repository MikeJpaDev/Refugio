package UI.src.ui.utils;

import javax.swing.table.DefaultTableModel;

public class ContratoTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 5L;
    public ContratoTableModel() {
        String[] encabezado = {"Id","Proveedor", "Fecha Inicio", "Fecha Fin", "Fecha Conciliación", "Descripción"};
        this.setColumnIdentifiers(encabezado);
    }

    public void agregarComp(int id,String nombre, String fechaInic, String fechaFin, String fechaConc, String descrip){
        Object[] row = new Object[]{id, nombre, fechaInic, fechaFin, fechaConc, descrip};
        addRow(row);
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
