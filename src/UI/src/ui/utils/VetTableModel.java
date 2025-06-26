/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.utils;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pc8
 */
public class VetTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 1L;
    public VetTableModel() {
        String[] encabezado = {"Nombre", "Provincia", "Especialidad", "Responsable", "Modalidad"};
        this.setColumnIdentifiers(encabezado);
    }
    
    public void agregarVet(String nombre, String pro, String esp, String responsable, String modalidad){
        Object[] row = new Object[]{nombre, pro, esp, responsable, modalidad};
        addRow(row);
    }
    
    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
