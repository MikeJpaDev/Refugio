package UI.src.ui.utils;

import javax.swing.table.DefaultTableModel;

public class RepoVetTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 150L;
    public RepoVetTableModel() {
        String[] encabezado = {"Nombre","Direccion" ,"Provincia", "Especialidad", "Clinica", "Fecha Inicio", "Fecha Fin", "Fecha Conciliacion", "descripcion"};
        this.setColumnIdentifiers(encabezado);
    }

    public void agregarRepop(String nombre,String direccion,  String pro, String esp, String clin, String fechaIn, String fechaFIn, String fechaConc, String desc){
        Object[] row = new Object[]{nombre,direccion, pro, esp, clin, fechaIn, fechaFIn, fechaConc, desc};
        addRow(row);
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
