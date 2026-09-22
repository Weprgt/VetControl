/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.vetcontrol;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author weprg
 */
public class VetControl {

    public static void main(String[] args) {
        FlatLightLaf.setup();
        
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.arc", 10);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("ScrollBar.width", 12);
        
        SwingUtilities.invokeLater(() -> {
            System.out.println("VetControl iniciado correctamente");
        });
    }
}
