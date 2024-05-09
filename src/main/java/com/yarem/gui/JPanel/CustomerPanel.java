package com.yarem.gui.JPanel;

import com.yarem.gui.JbuttonClass.BasketButton;

import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JPanel {
    private final Image backgroundImage;
    private BasketButton basketButton;
    private AdminPanel adminPanel;
    private  ProductPanel productPanel;
    private AdmitProductPanel admitProductPanel;

    public CustomerPanel(Image backgroundImage){
        setLayout(null);
        this.backgroundImage = backgroundImage;
        setPreferredSize(new Dimension(backgroundImage.getWidth(null),backgroundImage.getHeight(null)));

        //Панель з продуктами
        productPanel = new ProductPanel(this);
        productPanel.setBounds(39,28,309,450);
        add(productPanel);


        // Панель корзини
        basketButton = new BasketButton(new ImageIcon("src/main/resources/images/basket.png"),productPanel);
        basketButton.setBounds(430,50,230,230);
        add(basketButton);
//        basketButton.setVisible(false);

        //Панель адміна
        adminPanel = new AdminPanel();
        adminPanel.setBounds(430,50,230,230);

        //Панель з продуктами адміна
        admitProductPanel = new AdmitProductPanel(adminPanel);
        admitProductPanel.setBounds(39,28,309,450);

        adminPanel.setPanel(admitProductPanel);



        int width = 100;
        int height = 50;
        SecretLayerPane secretPanel = new SecretLayerPane(width,height,this);

        secretPanel.setBounds(600,350,width,height);


        add(secretPanel);

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    public BasketButton getBasketButton() {
        return basketButton;
    }

    public AdminPanel getCheckAdminPanel() {
        return adminPanel;
    }

    public AdmitProductPanel getAdmitProductPanel() {
        return admitProductPanel;
    }

    public ProductPanel getProductPanel() {
        return productPanel;
    }

}
