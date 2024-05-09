package com.yarem.gui.JframeClass;

import com.yarem.gui.JPanel.CustomerPanel;

import javax.swing.*;
import java.awt.*;

public class AutomatMenu extends JFrame {

    public AutomatMenu(){
        setResizable(false);
        setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon("src/main/resources/images/ТА.png");
        Image image = imageIcon.getImage();
        CustomerPanel panelWithBackground = new CustomerPanel(image);

        add(BorderLayout.CENTER,panelWithBackground);

    }
}
