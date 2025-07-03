package UI.src.ui.utils;

import javax.swing.table.DefaultTableModel;

public class ActividadTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 42L;
    public ActividadTableModel() {
        String[] encabezado = {"Id","Horario", "Animal", "Servicio", "Descripción"};
        this.setColumnIdentifiers(encabezado);
    }

    public void agregarAlim(String id,String hor, String anim, String serv, String desc){
        Object[] row = new Object[]{id, hor, anim, serv, desc};
        addRow(row);
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
