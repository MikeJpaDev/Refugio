package UI.src.ui.utils;

import javax.swing.table.DefaultTableModel;

public class RepoCompTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 30L;
    public RepoCompTableModel() {
        String[] encabezado = {"Provincia", "Tipo de Servicio",  "Fecha Inicio", "Fecha Fin", "Fecha Conciliacion", "descripcion", "suma"};
        this.setColumnIdentifiers(encabezado);
    }

    public void agregarRepop(String pro, String tip, String fechaIn, String fechaFIn, String fechaConc, String desc, Double suma){
        Object[] row = new Object[]{pro, tip , fechaIn, fechaFIn, fechaConc, desc, suma};
        addRow(row);
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
