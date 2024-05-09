package com.yarem.gui.JbuttonClass;

import com.yarem.dbConnect.HibernateSessionFactory;
import com.yarem.dbConnect.entity.AllSold;
import com.yarem.dbConnect.entity.Product;
import com.yarem.dbConnect.entity.ProductAllSold;
import com.yarem.gui.ClassLabel.CircleLabel;
import com.yarem.gui.JPanel.ProductPanel;
import org.hibernate.Session;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.List;

public class BasketButton extends JButton {

    private final CircleLabel circleLabel;
    private Map<Product,Integer> productIntegerMap = new HashMap<>();
    private ProductPanel productPanel;
    public BasketButton(ImageIcon imageIcon, ProductPanel productPanel){
        setBackground(new Color(225, 239, 165));
        setLayout(new BorderLayout());
        this.productPanel = productPanel;

        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(150,150,Image.SCALE_SMOOTH));
        JLabel jLabel = new JLabel(imageIcon);
        jLabel.setLayout(null);
        add(jLabel);

        setBorder(new LineBorder(new Color(101, 101, 101),6));

        // К-сть товару в корзині
        circleLabel = new CircleLabel();
        circleLabel.setBounds(150,30,35,35);
        jLabel.add(circleLabel);

        circleLabel.setVisible(false);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(productIntegerMap.size() > 0) {
                    BasketFrame basketFrame = new BasketFrame(BasketButton.this);
                    basketFrame.setModal(true);
                    basketFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null,"У вашому списку 0 товарів");
                }
            }
        });

//        setContentAreaFilled(false);
    }

    public void putProductInBasket(Product product){
        Integer countthisProduct = productIntegerMap.get(product);
        if(countthisProduct == null || countthisProduct < product.getCount()){
            circleLabel.setNumber(circleLabel.getNumber()+1);
            productIntegerMap.put(product, productIntegerMap.get(product) != null ? productIntegerMap.get(product) + 1 : 1);
        } else {
            JOptionPane.showMessageDialog(null,"Вибачте, но у автоматі більше немає даного продукту.");
        }
    }

    public Map<Product, Integer> getProductIntegerMap() {
        return productIntegerMap;
    }

    public CircleLabel getCircleLabel() {
        return circleLabel;
    }

    public ProductPanel getProductPanel() {
        return productPanel;
    }

    public void clearProduct(){
        productIntegerMap.clear();
        circleLabel.setNumber(0);
    }
}



class BasketFrame extends JDialog {

    private ProductsSelected productsSelected;
    private OrderConfirm orderConfirm;
    private int currentMainPanel;
    private List<JComponent> mainPanel = new ArrayList<>();
    private JButton payment;
    private BasketButton basketButton;

    BasketFrame(BasketButton basketButton){
        super();
        setTitle("Покупка товарів");
        setLayout(null);
        setResizable(false);
        this.basketButton = basketButton;

        int heightScroll = 303;

        productsSelected = new ProductsSelected(basketButton.getProductIntegerMap(),this);


        final JScrollPane scroll = new JScrollPane(productsSelected);
        scroll.setBounds(0,0,525,heightScroll);

        JPanel slide = new JPanel();
        slide.setLayout(new GridLayout(1,3,20,0));
        JButton products = new JButton("Продукти");
        payment = new JButton("Спосіб оплати");
        slide.add(products);
        slide.add(payment);
        slide.setBounds(0,heightScroll+3,525,40);
        slide.setBorder(new EmptyBorder(0,10,1,10));

        orderConfirm = new OrderConfirm(this);
        orderConfirm.setBounds(0,heightScroll+50,525,75);

        mainPanel.add(scroll);
        currentMainPanel = 0;
        mainPanel.add(new PaymentMethod.BankCard(this));


        PaymentMethod.Cash cash;
        try {
            File file = new File("src/main/resources/serializableObject/inputCash.ser");
            if (file.exists()) {
                ObjectInputStream deserCash = new ObjectInputStream(new FileInputStream("src/main/resources/serializableObject/inputCash.ser"));
                int countMoney = (Integer) deserCash.readObject();
                cash = new PaymentMethod.Cash(orderConfirm, payment,countMoney);
                mainPanel.add(cash);
            } else {
                cash = new PaymentMethod.Cash(orderConfirm, payment,0);
                mainPanel.add(cash);
            }
        } catch (IOException e) {
            throw new RuntimeException("При отримані вікна CASH сталась помилка");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        add(slide);
        add(scroll);
        add(orderConfirm);
        setSize(540, heightScroll+160);

        products.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentMainPanel == 0){
                    return;
                }
                int answer = JOptionPane.showConfirmDialog(null,"Якщо ви повернетесь до продуктів, то спосіб оплати буде скасовано." +
                        " Ви дійсно бажаєте це зробити?","Перейти до вкладки з продуктами",JOptionPane.YES_NO_OPTION);
                if(answer == JOptionPane.YES_OPTION){
                    JButton byButton = orderConfirm.getByButton();
                    byButton.setText("Для придбання оберіть спосіб оплати");
                    byButton.setEnabled(false);
                    byButton.setBackground(Color.GRAY);
                    byButton.setForeground(Color.BLACK);
                    byButton.setFont(new Font("TimesRoman", Font.BOLD, 16));
                    if(currentMainPanel == 1){
                        remove(mainPanel.get(1));
                    } else if(currentMainPanel == 2){
                        remove(mainPanel.get(2));
                    }
                    add(mainPanel.get(0));
                    currentMainPanel = 0;
                    payment.setEnabled(true);
                    BasketFrame.this.revalidate();
                    BasketFrame.this.repaint();
                }
            }
        });

        payment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Оплата карткою", "Оплата готівкою"};
                int choice = JOptionPane.showOptionDialog(BasketFrame.this, "Оберіть спосіб оплати", "Спосіб оплати", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, null);
                if (choice == 0) {
                    if(currentMainPanel == 1){
                        JOptionPane.showMessageDialog(null,"Вам вже продемонстровано дане меню.");
                        return;
                    }
                    if(currentMainPanel == 0) {
                        remove(mainPanel.get(0));
                    } else if(currentMainPanel == 2){
                        int answer = JOptionPane.showConfirmDialog(null,"Ви дійсно бажаєте змінити спосіб оплати?","Зміна способу оплати",JOptionPane.YES_NO_OPTION);
                        if(answer == JOptionPane.YES_OPTION){
                            remove(mainPanel.get(2));
                            JButton byButton = orderConfirm.getByButton();
                            byButton.setText("Для придбання оберіть спосіб оплати");
                            byButton.setEnabled(false);
                            byButton.setBackground(Color.GRAY);
                            byButton.setForeground(Color.BLACK);
                            byButton.setFont(new Font("TimesRoman", Font.BOLD, 16));
                        } else {
                            return;
                        }
                    }
                    add(mainPanel.get(1));
                    currentMainPanel = 1;
                } else if (choice == 1) {
                    if(currentMainPanel == 2){
                        return;
                    }
                    if(currentMainPanel == 0) {
                        remove(mainPanel.get(0));
                    } else if(currentMainPanel == 1){
                        int answer = JOptionPane.showConfirmDialog(null,"Ви дійсно бажаєте змінити спосіб оплати?","Зміна способу оплати",JOptionPane.YES_NO_OPTION);
                        if(answer == JOptionPane.YES_OPTION){
                            remove(mainPanel.get(1));
                            JButton byButton = orderConfirm.getByButton();
                            byButton.setText("Для придбання оберіть спосіб оплати");
                            byButton.setEnabled(false);
                            byButton.setBackground(Color.GRAY);
                            byButton.setForeground(Color.BLACK);
                            byButton.setFont(new Font("TimesRoman", Font.BOLD, 16));
                        } else {
                            return;
                        }
                    }
                    add(mainPanel.get(2));
                    ((PaymentMethod.Cash) mainPanel.get(2)).setPassBy();
                    currentMainPanel = 2;
                } else {
                    return;
                }
                BasketFrame.this.revalidate();
                BasketFrame.this.repaint();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ObjectInputStream deserCash = new ObjectInputStream(new FileInputStream("src/main/resources/serializableObject/inputCash.ser"));
                    int countMoney = (Integer) deserCash.readObject();
                    if(cash.getCash() == countMoney){
                        return;
                    }
                    ObjectOutputStream serCash;
                    serCash = new ObjectOutputStream(new FileOutputStream("src/main/resources/serializableObject/inputCash.ser"));
                    serCash.writeObject(cash.getCash());
                } catch (IOException ex){
                    System.out.println(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                windowClosing(e);
            }
        });

    }

    public ProductsSelected getProductsSelected() {
        return productsSelected;
    }

    public OrderConfirm getOrderConfirm() {
        return orderConfirm;
    }


    public JButton getPayment() {
        return payment;
    }

    public BasketButton getBasketButton() {
        return basketButton;
    }

    public int getCurrentMainPanel() {
        return currentMainPanel;
    }

    public List<JComponent> getMainPanel() {
        return mainPanel;
    }
}

class PaymentMethod {


    static class Cash extends JPanel{

        private Integer cash;
        private JLabel balanceValue;
        private List<MoneyButton> moneyButtonList;
        private ActionListener actionListener;
        private OrderConfirm orderConfirm;
        private JButton paymentBasket;
        Cash(OrderConfirm orderConfirm,JButton payment, int currentCash){
            setBounds(0,0,523,300);
            setBackground(Color.GRAY);
            setBorder(new LineBorder(Color.BLACK));
            setLayout(null);

            this.orderConfirm = orderConfirm;
            this.paymentBasket = payment;
            this.cash = currentCash;

            JPanel putMoney = new JPanel();
            putMoney.setBorder(new MatteBorder(1,1,2,2,Color.BLACK));
            putMoney.setBackground(Color.WHITE);
            putMoney.setBounds(70,10,385,280);
            putMoney.setLayout(null);


            moneyButtonList = new ArrayList<>();
            actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton jButton = (JButton) e.getSource();
                    jButton.setSelected(true);
                    for(MoneyButton mb : moneyButtonList){
                        if(mb == jButton){
                            mb.setBorder(new MatteBorder(1,1,2,2,Color.BLACK));
                            mb.setSelected(true);
                        } else {
                            mb.setBorder(null);
                            mb.setSelected(false);
                        }
                    }
                }
            };

            MoneyButton button1 = new MoneyButton(1,actionListener,new ImageIcon("src/main/resources/images/1_Dollar.jpg"));
            MoneyButton button2 = new MoneyButton(5,actionListener,new ImageIcon("src/main/resources/images/5_Dollar.jpg"));
            MoneyButton button3 = new MoneyButton(10,actionListener,new ImageIcon("src/main/resources/images/10_Dollar.jpg"));
            MoneyButton button4 = new MoneyButton(20,actionListener,new ImageIcon("src/main/resources/images/20_Dollar.jpg"));
            MoneyButton button5 = new MoneyButton(50,actionListener,new ImageIcon("src/main/resources/images/50_Dollar.jpg"));
            MoneyButton button6 = new MoneyButton(100,actionListener,new ImageIcon("src/main/resources/images/100_Dollar.jpg"));
            button1.setBorder(new MatteBorder(2,2,2,2,Color.BLACK));
            button1.setSelected(true);

            moneyButtonList.add(button1);
            moneyButtonList.add(button2);
            moneyButtonList.add(button3);
            moneyButtonList.add(button4);
            moneyButtonList.add(button5);
            moneyButtonList.add(button6);

            JPanel panelMoney = new JPanel();
            panelMoney.setBorder(new EmptyBorder(5,5,5,5));
            panelMoney.setBounds(30,10,325,180);
            panelMoney.setBackground(Color.WHITE);

            for(MoneyButton mb : moneyButtonList){
                panelMoney.add(mb);
            }

            panelMoney.setLayout(new GridLayout(2,3,5,5));


            JButton putCash = new JButton("Внести кошти");

            putCash.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource() == putCash){
                        MoneyButton moneyButton = selectedMoney();
                        if(moneyButton == null){
                            return;
                        }
                        cash += moneyButton.getCost();
                        balanceValue.setText(cash.toString());
                        setPassBy();
                        Cash.this.revalidate();
                        Cash.this.repaint();
                    }
                }
            });

            JButton returnCash = new JButton("Повернути кошти");

            returnCash.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource() == returnCash){
                        MoneyButton moneyButton = selectedMoney();
                        if(moneyButton == null){
                            return;
                        }
                        int value = moneyButton.getCost();
                        if(cash - value <= 0 ) {
                            cash = 0;
                        } else {
                            cash -= value;
                        }
                        balanceValue.setText(cash.toString());
                        setPassBy();
                    }
                }
            });

            JPanel panelUseMoney = new JPanel();
            panelUseMoney.setLayout(new GridLayout(1,2,20,0));
            panelUseMoney.setBorder(new EmptyBorder(5,5,5,5));
            panelUseMoney.setBackground(Color.WHITE);

            panelUseMoney.add(putCash);
            panelUseMoney.add(returnCash);
            panelUseMoney.setBounds(30,190,325,40);

            JLabel balanceText = new JLabel("Ваш баланс: ");
            balanceValue = new JLabel(cash.toString());

            balanceText.setFont(new Font("TimesRoman", Font.BOLD, 18));
            balanceValue.setFont(new Font("TimesRoman", Font.BOLD, 18));

            balanceText.setBounds(100,230,120,40);
            balanceValue.setBounds(250,230,100,40);




            putMoney.add(balanceText);
            putMoney.add(balanceValue);
            putMoney.add(panelMoney);
            putMoney.add(panelUseMoney);



            add(putMoney);
        }

        public void setPassBy(){
            if(cash >= Double.parseDouble(orderConfirm.getCost().getText())){
//                paymentBasket.setEnabled(false);
                JButton ordConfByButton = orderConfirm.getByButton();
                ordConfByButton.setEnabled(true);
                ordConfByButton.setBackground(new Color(49, 211, 55));
                ordConfByButton.setForeground(Color.BLACK);
                ordConfByButton.setText("ПРИДБАТИ");
            } else {
                paymentBasket.setEnabled(true);
                JButton ordConfByButton = orderConfirm.getByButton();
                ordConfByButton.setEnabled(false);
                ordConfByButton.setBackground(Color.GRAY);
                ordConfByButton.setForeground(Color.BLACK);
                ordConfByButton.setText("Для придбання оберіть спосіб оплати");
            }
        }


        public MoneyButton selectedMoney(){
            MoneyButton moneyButton = null;
            for(MoneyButton mb : moneyButtonList){
                if(mb.isSelected()){
                    moneyButton = mb;
                    break;
                }
            }
            return moneyButton;
        }

        public class MoneyButton extends JButton{
            private int cost;

            MoneyButton(int cost, ActionListener actionListener, ImageIcon imageIcon){
                super();
                this.cost = cost;
                setBorder(null);
                addActionListener(actionListener);

                imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(98,80,Image.SCALE_SMOOTH));
                setIcon(imageIcon);


                ToolTipManager.sharedInstance().setDismissDelay(1000);
                ToolTipManager.sharedInstance().setInitialDelay(500);

                setToolTipText("Сума - "+cost);
            }

            public int getCost() {
                return cost;
            }

        }

        public Integer getCash() {
            return cash;
        }
        public void resetCash(){
           cash = 0;
        }
    }

    static class BankCard extends JPanel{
        BankCard(BasketFrame basketFrame){
            setBounds(0,0,524,300);
            setBackground(Color.GRAY);
            setBorder(new LineBorder(Color.BLACK));
            setLayout(null);

            JPanel selectPayment = new JPanel();
            selectPayment.setBorder(new MatteBorder(1,1,2,2,Color.BLACK));
            selectPayment.setBackground(Color.WHITE);
            selectPayment.setBounds(100,10,325,280);
            selectPayment.setLayout(null);

            JLabel paymentData = new JLabel("Платіжні дані");

            paymentData.setBounds(10,3,140,40);
            paymentData.setFont(new Font("TimesRoman", Font.BOLD, 18));

            JLabel cardNumber = new JLabel("Номер вашої картки");
            cardNumber.setBounds(12,30,120,40);
            cardNumber.setFont(new Font("TimesRoman", Font.PLAIN, 12));
            cardNumber.setForeground(new Color(133, 134, 133));

            JTextArea cardNumberArea = new JTextArea();
            cardNumberArea.setBounds(10,65,305,20);
            cardNumberArea.setBorder(new LineBorder(Color.BLACK));

            JLabel dateEnd = new JLabel("Термін закінчення");
            dateEnd.setBounds(12,80,120,40);
            dateEnd.setFont(new Font("TimesRoman", Font.PLAIN, 12));
            dateEnd.setForeground(new Color(133, 134, 133));

            JLabel svv = new JLabel("SVV2");
            svv.setBounds(177,80,100,40);
            svv.setFont(new Font("TimesRoman", Font.PLAIN, 12));
            svv.setForeground(new Color(133, 134, 133));


            JTextArea dateEndArea = new JTextArea();
            dateEndArea.setBounds(10,115,140,20);
            dateEndArea.setBorder(new LineBorder(Color.BLACK));

            JTextArea ssvArea = new JTextArea();
            ssvArea.setBounds(175,115,140,20);
            ssvArea.setBorder(new LineBorder(Color.BLACK));

            JLabel chooseTypeCard = new JLabel("Оберіть спосіб оплати");
            chooseTypeCard.setBounds(10,135,220,40);
            chooseTypeCard.setFont(new Font("TimesRoman", Font.BOLD, 18));

            JPanel typeCard = new JPanel();
            typeCard.setBorder(new EmptyBorder(10,10,10,10));
            typeCard.setLayout(new GridLayout(1,2,10,0));
            Image imageV = new ImageIcon("src/main/resources/images/Visa.png").getImage();
            ImageIcon visaIcon = new ImageIcon(imageV.getScaledInstance(134,60,Image.SCALE_SMOOTH));
            Image imageM = new ImageIcon("src/main/resources/images/mastercard.jpg").getImage();
            ImageIcon masterIcon = new ImageIcon(imageM.getScaledInstance(134,60,Image.SCALE_SMOOTH));
            JButton visa = new JButton(visaIcon);
            JButton mastercard = new JButton(masterIcon);
            visa.setBorder(new MatteBorder(2,2,2,2,Color.BLACK));
            mastercard.setBorder(new LineBorder(Color.GRAY));
            typeCard.add(visa);
            typeCard.add(mastercard);;
            typeCard.setBackground(Color.WHITE);
            typeCard.setBounds(10,160,300,80);

            visa.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mastercard.setBorder(new LineBorder(Color.GRAY));
                    visa.setBorder(new MatteBorder(2,2,2,2,Color.BLACK));
                    mastercard.setSelected(true);
                    visa.setSelected(false);
                }
            });

            mastercard.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mastercard.setBorder(new MatteBorder(2,2,2,2,Color.BLACK));
                    visa.setBorder(new LineBorder(Color.GRAY));
                    mastercard.setSelected(false);
                    visa.setSelected(true);
                }
            });

            JButton input = new JButton("Внести дані");
            input.setFont(new Font("TimesRoman", Font.BOLD, 16));
            input.setBounds(50,240,225,30);
            input.setBackground(Color.GREEN);


            input.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(cardNumberArea.getText().equals("") || dateEndArea.getText().equals("") || ssvArea.getText().equals("")){
                        JOptionPane.showMessageDialog(null,"Будь-ласка вводьте коректні дані картки.");
                    } else {
                        JButton ordConfByButton = basketFrame.getOrderConfirm().getByButton();
                        ordConfByButton.setEnabled(true);
                        ordConfByButton.setBackground(new Color(49, 211, 55));
                        ordConfByButton.setForeground(Color.BLACK);
                        ordConfByButton.setText("ПРИДБАТИ");
                    }
                }
            });

            selectPayment.add(paymentData);
            selectPayment.add(cardNumber);
            selectPayment.add(cardNumberArea);
            selectPayment.add(dateEnd);
            selectPayment.add(svv);
            selectPayment.add(dateEndArea);
            selectPayment.add(ssvArea);
            selectPayment.add(chooseTypeCard);
            selectPayment.add(typeCard);
            selectPayment.add(input);
            add(selectPayment);
        }
    }
}



class OrderConfirm extends JPanel{
    private JLabel cost;
    private ProductsSelected productsSelected;
    private JButton byButton;

    public OrderConfirm(BasketFrame basketFrame){
        setLayout(new GridLayout(2,1));

        this.productsSelected = basketFrame.getProductsSelected();

        JPanel totalPrice = new JPanel();
        totalPrice.setLayout(new GridLayout(1,2));
        JLabel total = new JLabel("Загальна ціна:");
        total.setFont(new Font("TimesRoman", Font.BOLD, 20));
        total.setHorizontalAlignment(SwingConstants.CENTER);
        cost = new JLabel("0");
        cost.setFont(new Font("TimesRoman", Font.BOLD, 20));
        cost.setHorizontalAlignment(SwingConstants.CENTER);
        totalPrice.add(total);
        totalPrice.add(cost);

        JPanel byProduct = new JPanel();
        byProduct.setLayout(new BorderLayout());
        byProduct.setBorder(new EmptyBorder(5,30,10,30));
        byButton = new JButton("Для придбання оберіть спосіб оплати");
        byButton.setEnabled(false);
        byButton.setBackground(Color.GRAY);
        byButton.setForeground(Color.BLACK);
        byButton.setFont(new Font("TimesRoman", Font.BOLD, 16));
        byProduct.add(byButton);


        byButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Session session = HibernateSessionFactory.openSession();
                try {
                    session.getTransaction().begin();
                    AllSold allSold = new AllSold();
                    session.persist(allSold);
                    allSold.setFinalPrice(Double.parseDouble(cost.getText()));
                    for(Map.Entry<Product,Integer> entry : basketFrame.getBasketButton().getProductIntegerMap().entrySet()){
                        session.saveOrUpdate(entry.getKey());
                        Product product = entry.getKey();
                        product.setCount(product.getCount() - entry.getValue());
                        ProductAllSold productAllSold = new ProductAllSold(entry.getValue(),allSold,product,entry.getValue() * product.getPriceForOne());
                        session.persist(productAllSold);
                    }
                    session.getTransaction().commit();
                } catch (Exception ex){
                    System.err.println(ex);
                    session.getTransaction().rollback();
                    return;
                } finally {
                    session.close();
                }
                String resultMessage = "Купівля пройшла успішно, дякую за довіру.";
                if(basketFrame.getCurrentMainPanel() == 2){
                    DecimalFormat df = new DecimalFormat("0.##",new DecimalFormatSymbols(Locale.ENGLISH));
                    double rest = ((PaymentMethod.Cash) basketFrame.getMainPanel().get(2)).getCash() - Double.parseDouble(cost.getText());
                    if(rest > 0){
                        resultMessage +="\nВаша решта - "+df.format(rest)+".";
                    }
                    ((PaymentMethod.Cash) basketFrame.getMainPanel().get(2)).resetCash();
                }
                JOptionPane.showMessageDialog(null,resultMessage);
                basketFrame.getBasketButton().getProductIntegerMap().clear();
                basketFrame.getBasketButton().getCircleLabel().setNumber(0);
                basketFrame.getBasketButton().getProductPanel().repaintProduct();
                basketFrame.dispose();
            }
        });


        add(totalPrice);
        add(byProduct);

        setCost();

    }

    public JButton getByButton() {
        return byButton;
    }

    public void setCost() {
        double cost = 0;
        for(ProductsSelected.InfoProduct infoProduct : productsSelected.getAllProduct()){
            cost += infoProduct.getCostProduct().getCostProduct();
        }
        DecimalFormat df = new DecimalFormat("0.##",new DecimalFormatSymbols(Locale.ENGLISH));
        this.cost.setText(df.format(cost));
    }

    public JLabel getCost() {
        return cost;
    }
}

class ProductsSelected extends JPanel{

    private List<InfoProduct> allProduct;
    private BasketFrame basketFrame;
    private int countTypeProduct;
    private int gridRows = 3;
    private GridLayout gridLayout;
    private Map<Product,Integer> productIntegerMap;
    ProductsSelected(Map<Product,Integer> productIntegerMap,BasketFrame basketFrame){
        this.basketFrame = basketFrame;
        this.countTypeProduct = productIntegerMap.size();
        this.productIntegerMap = productIntegerMap;

        setBorder(BorderFactory.createLineBorder(Color.red));
        setPreferredSize(new Dimension(500, countTypeProduct * 100));

        setBackground(Color.GRAY);
        if(countTypeProduct > 3){
            gridRows = countTypeProduct;
        }
        gridLayout = new GridLayout(gridRows,1,5,5);
        setLayout(gridLayout);
        allProduct = new ArrayList<>();
        for(Map.Entry<Product,Integer> entry : productIntegerMap.entrySet()){
            InfoProduct infoProduct = new InfoProduct(entry);
            add(infoProduct);
            allProduct.add(infoProduct);
        }
    }

        class InfoProduct extends JPanel {

            private CostProduct costProduct;
            private Product product;
            private Map.Entry<Product,Integer> productIntegerEntry;
            InfoProduct(Map.Entry<Product,Integer> productIntegerEntry){
                setBackground(Color.WHITE);
                this.productIntegerEntry = productIntegerEntry;
                product = productIntegerEntry.getKey();

                setBorder(new CompoundBorder(new MatteBorder(1,1,3,3,Color.BLACK),new EmptyBorder(5, 10, 5, 10)));
                setLayout(new GridLayout(1,4,10,10));
                setSize(new Dimension(200,150));
                Image image = product.getImageIcon().getImage();
                ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(112,85,Image.SCALE_SMOOTH));
                JLabel imageProduct = new JLabel(imageIcon);
                imageProduct.setBorder(new MatteBorder(2,2,2,2,Color.BLACK));

                String description = product.getDescription()+"\n"+"Ціна "+product.getPriceForOne() +"/шт";
                JTextArea infoProduct = new JTextArea(description);
                infoProduct.setBorder(new EmptyBorder(10,10,10,10));
                infoProduct.setOpaque(false);
                infoProduct.setLineWrap(true);
                infoProduct.setFont(new Font("TimesRoman", Font.BOLD, 12));
                infoProduct.setEditable(false);


                costProduct = new CostProduct(this);
                costProduct.setCostProduct(product.getPriceForOne() * productIntegerEntry.getValue());

                EditCount editCount = new EditCount(this,costProduct);

                add(imageProduct);
                add(infoProduct);
                add(editCount);
                add(costProduct);
            }

            public Map.Entry<Product, Integer> getProductIntegerEntry() {
                return productIntegerEntry;
            }

            public CostProduct getCostProduct() {
                return costProduct;
            }

            public Product getProduct() {
                return product;
            }

        }

        class CostProduct extends JPanel{

            private JLabel costProduct;
            CostProduct(InfoProduct infoProduct){
                setBackground(Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 5));
                setLayout(new GridLayout(2,1));

                costProduct = new JLabel("0");
                costProduct.setHorizontalAlignment(SwingConstants.CENTER);
                costProduct.setBorder(new EmptyBorder(0,0,10,0));
                costProduct.setFont(new Font("TimesRoman", Font.BOLD, 14));
                add(costProduct);

                JButton removeBy = new JButton("Видалити");
                removeBy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ProductsSelected.this.removeInfoProduct(infoProduct);
                    }
                });
                removeBy.setBackground(new Color(246, 86, 98));
                add(removeBy);
            }

            public void setCostProduct(double cost) {
                DecimalFormat df = new DecimalFormat("0.##",new DecimalFormatSymbols(Locale.ENGLISH));
                this.costProduct.setText(df.format(cost));
            }

            public double getCostProduct() {
                return Double.parseDouble(costProduct.getText());
            }
        }

        class EditCount extends JPanel {
             EditCount(InfoProduct infoProduct,CostProduct costProduct){
                 setBackground(Color.WHITE);
                 setBorder(BorderFactory.createEmptyBorder(20, 1, 20, 1));
                 setLayout(new GridLayout(1,3,10,10));
                 BasicArrowButton right = new BasicArrowButton(BasicArrowButton.EAST),
                         left = new BasicArrowButton(BasicArrowButton.WEST);

                 JLabel countProduct = new JLabel(Integer.toString(infoProduct.productIntegerEntry.getValue()));
                 countProduct.setHorizontalAlignment(SwingConstants.CENTER);

                 add(left);
                 add(countProduct);
                 add(right);

                 left.addActionListener(new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                         if(infoProduct.productIntegerEntry.getValue() == 1){
                             int answer = JOptionPane.showConfirmDialog(null,"Ви дійсно бажаєте вилучити цей товар?","Чи потрібно видалити товар",JOptionPane.YES_NO_OPTION);
                             if(answer == JOptionPane.YES_OPTION){
                                 ProductsSelected.this.removeInfoProduct(infoProduct);
                             }
                         } else {
                             Map.Entry<Product,Integer> entryInfoProduct = infoProduct.productIntegerEntry;
                             entryInfoProduct.setValue(entryInfoProduct.getValue()-1);
                             countProduct.setText(Integer.toString(entryInfoProduct.getValue()));
                             costProduct.setCostProduct(entryInfoProduct.getValue() * entryInfoProduct.getKey().getPriceForOne());
                             basketFrame.getOrderConfirm().setCost();
                             basketFrame.getBasketButton().getCircleLabel().setNumber(basketFrame.getBasketButton().getCircleLabel().getNumber() - 1);
                             basketFrame.getBasketButton().revalidate();
                             basketFrame.getBasketButton().repaint();
                         }
                     }
                 });

                 right.addActionListener(new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                          if(infoProduct.productIntegerEntry.getValue() >= infoProduct.productIntegerEntry.getKey().getCount()){
                              JOptionPane.showMessageDialog(null,"Вибачте, но у автоматі більше немає даного продукту");
                          } else {
                              Map.Entry<Product,Integer> entryInfoProduct = infoProduct.productIntegerEntry;
                              entryInfoProduct.setValue(entryInfoProduct.getValue()+1);
                              countProduct.setText(Integer.toString(entryInfoProduct.getValue()));
                              costProduct.setCostProduct(entryInfoProduct.getValue() * entryInfoProduct.getKey().getPriceForOne());
                              basketFrame.getOrderConfirm().setCost();
                              basketFrame.getBasketButton().getCircleLabel().setNumber(basketFrame.getBasketButton().getCircleLabel().getNumber() + 1);
                              basketFrame.getBasketButton().revalidate();
                              basketFrame.getBasketButton().repaint();
                          }
                     }
                 });

             }
        }


    public List<InfoProduct> getAllProduct() {
        return allProduct;
    }
    public void removeInfoProduct(InfoProduct infoProduct){
        if(basketFrame.getBasketButton().getCircleLabel().getNumber() - infoProduct.getProductIntegerEntry().getValue() == 0){
            int answer = JOptionPane.showConfirmDialog(null,"Ви дійсно бажаєте видалити останній продукт із списку?\nЯкщо так то форму покупки буде закрито.",
                    "Підтвердження видалення",JOptionPane.YES_NO_OPTION);
            if(answer == JOptionPane.YES_OPTION){
                productIntegerMap.remove(infoProduct.getProduct());
                basketFrame.getBasketButton().getCircleLabel().setNumber(basketFrame.getBasketButton().getCircleLabel().getNumber() - infoProduct.getProductIntegerEntry().getValue());
                basketFrame.getBasketButton().revalidate();
                basketFrame.getBasketButton().repaint();
                basketFrame.dispose();
            }
        } else {
            allProduct.remove(infoProduct);
            remove(infoProduct);
            productIntegerMap.remove(infoProduct.getProduct());
            basketFrame.getOrderConfirm().setCost();
            basketFrame.getBasketButton().getCircleLabel().setNumber(basketFrame.getBasketButton().getCircleLabel().getNumber() - infoProduct.getProductIntegerEntry().getValue());
            basketFrame.getBasketButton().revalidate();
            basketFrame.getBasketButton().repaint();
            countTypeProduct--;
            if (gridRows > 3) {
                gridRows--;
            }
            gridLayout.setRows(gridRows);
            setPreferredSize(new Dimension(500, gridRows * 100));
            revalidate();
            repaint();
        }
    }

}

