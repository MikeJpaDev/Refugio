package UI.src.ui.utils;

import javax.swing.table.DefaultTableModel;

public class AnimalTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 4L;

    public AnimalTableModel() {
        String[] encabezado = {"Id","Nombre", "Especie", "Raza"};
        this.setColumnIdentifiers(encabezado);
    }

    public void agregarAnimal(String id ,String nombre, String especie, String raza){
        Object[] row = new Object[]{id, nombre, especie, raza};
        addRow(row);
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
