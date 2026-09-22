/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.vetcontrol;

import com.formdev.flatlaf.FlatLightLaf; 
import com.mycompany.vetcontrol.vista.FrmLogin;
import javax.swing.SwingUtilities; 
import javax.swing.UIManager;

/**
 *
 * @author weprg
 */
public class VetControl {

    public static void main(String[] args) {
        FlatLightLaf.setup(); // Activa el tema claro de FlatLight
        
        UIManager.put("Component.arc", 10); // define el redondeo para componentes compatibles
        UIManager.put("Button.arc", 10); // redondeo de botones
        UIManager.put("TextComponent.arc", 8); // redondea componentes de texto
        UIManager.put("ScrollBar.width", 12); // Scrollbars con ancho a 12 pixeles
        
        SwingUtilities.invokeLater(() -> {
            FrmLogin ventana= new FrmLogin();
            ventana.setVisible(true);
        });
    }
}
