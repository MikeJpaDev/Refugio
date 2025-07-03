package UI.src.ui.utils;

import javax.swing.table.DefaultTableModel;

public class RepoAlimTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 120L;
    public RepoAlimTableModel() {
        String[] encabezado = {"Nombre","Direccion" ,"Provincia", "Tipo Alimento",  "Fecha Inicio", "Fecha Fin", "Fecha Conciliacion", "descripcion"};
        this.setColumnIdentifiers(encabezado);
    }

    public void agregarRepop(String nombre,String direccion,  String pro, String tip, String fechaIn, String fechaFIn, String fechaConc, String desc){
        Object[] row = new Object[]{nombre,direccion, pro, tip , fechaIn, fechaFIn, fechaConc, desc};
        addRow(row);
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
