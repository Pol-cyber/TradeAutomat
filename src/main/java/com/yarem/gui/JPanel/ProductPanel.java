package com.yarem.gui.JPanel;

import com.yarem.dbConnect.HibernateSessionFactory;
import com.yarem.dbConnect.entity.Product;
import com.yarem.gui.JbuttonClass.ButtonProduct;
import org.hibernate.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class ProductPanel extends JPanel {

    private final LinkedList<ButtonProduct> listProduct = new LinkedList<>();
    private ActionListener productAction;

    public ProductPanel(CustomerPanel customerPanel){
        setBackground(Color.black);
        GridLayout layout = new GridLayout(5, 4);
        layout.setHgap(3);
        layout.setVgap(3);
        setLayout(layout);

        productAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object buttonAction = e.getSource();
                if(buttonAction instanceof ButtonProduct) {
                    if(((ButtonProduct) buttonAction).getProduct() != null) {
                        customerPanel.getBasketButton().putProductInBasket(((ButtonProduct) buttonAction).getProduct());
                    }
                }
            }
        };

        for(int i = 0; i < 20; i++){
            ButtonProduct jButton = new ButtonProduct(i,null);
            listProduct.add(jButton);
            add(jButton);
            jButton.addActionListener(productAction);
        }
        putProduct();
        repaintProduct();

    }

    public void putProduct(){
        Session session = HibernateSessionFactory.openSession();
        try {
            session.getTransaction().begin();
            for (int i = 0; i < 20; i++) {
                List lProduct = session.createQuery("SELECT p FROM Product p WHERE p.automatSlot = :slotId AND p.useInAutomat = true AND p.count > 0")
                        .setParameter("slotId", i).getResultList();
                Product product = null;
                if (lProduct.size() > 0) {
                    product = (Product) lProduct.get(0);
                }
                if (product != null) {
                    listProduct.get(i).setProductIm(product);
                } else {
                    listProduct.get(i).setProductIm(null);
                }
            }
            session.getTransaction().commit();
        } catch (Exception e){
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void repaintProduct(){
        Session session = HibernateSessionFactory.openSession();

        try {
            session.getTransaction().begin();

            for (int i = 0; i < 20; i++) {
                if (listProduct.get(i).getProduct() != null) {
                    if (listProduct.get(i).getProduct().getCount() == 0) {
                        Product product = (Product) session.merge(listProduct.get(i).getProduct());
                        product.setUseInAutomat(false);
                        listProduct.get(i).deleteProduct();
                    } else {
                        continue;
                    }
                }
                List lProduct = session.createQuery("SELECT p FROM Product p WHERE p.automatSlot = :slotId AND p.useInAutomat = false AND p.count > 0")
                        .setParameter("slotId", i).getResultList();
                if (lProduct.size() > 0) {
                    Product product = (Product) lProduct.get(0);
                    product.swapUseInAutomat();
                    listProduct.get(i).setProductIm(product);
                }
            }

            session.getTransaction().commit();
        } catch (Exception e){
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

}
