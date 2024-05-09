package com.yarem.gui.JbuttonClass;

import com.yarem.dbConnect.entity.Product;
import com.yarem.gui.JPanel.AdminPanel;

import javax.swing.*;
import java.awt.*;

public class ButtonProduct extends JButton {

    private int index;
    private Product product;
    private final String linkEmptyImage = "src/main/resources/images/empty.png";
    public ButtonProduct(ImageIcon imageIcon,int i){
        setLayout(null);
        index = i;
        setBackground(Color.cyan);
        Image image = imageIcon.getImage();

        ImageIcon icon = new ImageIcon(image.getScaledInstance(73, 85, Image.SCALE_SMOOTH));
        setIcon(icon);
    }

    public ButtonProduct(int i, Product product){
        setLayout(null);
        this.product = product;
        index = i;
        setBackground(Color.cyan);
        ImageIcon imageIcon;
        if(product != null){
            imageIcon = new ImageIcon(product.getByteImage());
        } else {
            imageIcon = new ImageIcon(linkEmptyImage);
        }
        Image image = imageIcon.getImage();

        ImageIcon icon = new ImageIcon(image.getScaledInstance(73, 85, Image.SCALE_SMOOTH));
        setIcon(icon);
    }


    public Product getProduct() {
        return product;
    }

    public void deleteProduct(){
        this.product = null;
        ImageIcon imageIcon = new ImageIcon(linkEmptyImage);
        Image image = imageIcon.getImage();

        ImageIcon icon = new ImageIcon(image.getScaledInstance(73, 85, Image.SCALE_SMOOTH));
        setIcon(icon);
        revalidate();
        repaint();
    }

    public void setProductIm(Product product) {
        if(product == null){
            this.product = null;
            setBackground(Color.cyan);
            ImageIcon imageIcon;
            imageIcon = new ImageIcon(linkEmptyImage);
            Image image = imageIcon.getImage();
            ImageIcon icon = new ImageIcon(image.getScaledInstance(73, 85, Image.SCALE_SMOOTH));
            setIcon(icon);
            revalidate();
            repaint();
        } else {
            this.product = product;
            Image image = product.getImageIcon().getImage();
            ImageIcon icon = new ImageIcon(image.getScaledInstance(73, 85, Image.SCALE_SMOOTH));
            setIcon(icon);
            revalidate();
            repaint();
        }
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "ButtonProduct{" +
                "index=" + index +
                '}';
    }
}
