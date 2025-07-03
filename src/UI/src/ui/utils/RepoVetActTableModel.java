package UI.src.ui.utils;

import javax.swing.table.DefaultTableModel;

public class RepoVetActTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 150L;
    public RepoVetActTableModel() {
        String[] encabezado = {"Nombre","fecha" ,"Provincia", "Especialidad", "Clinica", "email", "modalidad"};
        this.setColumnIdentifiers(encabezado);
    }

    public void agregarRepop(String nombre,String fecha,  String pro, String esp, String clin, String emial, String mod){
        Object[] row = new Object[]{nombre,fecha, pro, esp, clin, emial, mod};
        addRow(row);
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
