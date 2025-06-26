/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Font;
import javax.swing.UIManager;
import ui.jframes.VentanaPrincipal;

/**
 *
 * @author pc8
 */
public class UI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 14));
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            
            java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
    java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
        Object key = keys.nextElement();
        Object value = UIManager.get(key);
        if (value instanceof javax.swing.plaf.FontUIResource) {
            UIManager.put(key, f);
        }
    }
    }
}
