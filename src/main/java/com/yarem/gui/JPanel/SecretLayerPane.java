package com.yarem.gui.JPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecretLayerPane extends JLayeredPane {

    public SecretLayerPane(int width, int height, CustomerPanel jCustomerPanel){

        setLayout(null);
        setBorder(new LineBorder(new Color(19, 9, 19)));

        JButton secretButton = new JButton("");
        secretButton.setBounds(60,10,30,30);
        secretButton.setEnabled(false);
        secretButton.setBackground(new Color(215, 215, 98));


        secretButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Component> components = new ArrayList<>(Arrays.asList(jCustomerPanel.getComponents()));
                if(components.contains(jCustomerPanel.getBasketButton())){
                    jCustomerPanel.remove(jCustomerPanel.getBasketButton());
                    jCustomerPanel.remove(jCustomerPanel.getProductPanel());
                    jCustomerPanel.add(jCustomerPanel.getCheckAdminPanel());
                    jCustomerPanel.add(jCustomerPanel.getAdmitProductPanel());
                    jCustomerPanel.revalidate();
                    jCustomerPanel.repaint();
                } else {
                    jCustomerPanel.remove(jCustomerPanel.getCheckAdminPanel());
                    jCustomerPanel.remove(jCustomerPanel.getAdmitProductPanel());
                    jCustomerPanel.getCheckAdminPanel().setCheckPanel();
                    jCustomerPanel.add(jCustomerPanel.getBasketButton());
                    jCustomerPanel.getBasketButton().clearProduct();
                    jCustomerPanel.add(jCustomerPanel.getProductPanel());
                    jCustomerPanel.getProductPanel().putProduct();
                    jCustomerPanel.getProductPanel().repaintProduct();
                    jCustomerPanel.revalidate();
                    jCustomerPanel.repaint();
                }
            }
        });


        ImageIcon imageIcon = new ImageIcon("src/main/resources/images/SecretPane.png");
        FollowMousePanel followMousePanel = new FollowMousePanel(height,secretButton,imageIcon);

        followMousePanel.setBounds(width/2,0,width/2,height);

        add(followMousePanel,Integer.parseInt("2"));
        add(secretButton,Integer.parseInt("1"));
    }
}
