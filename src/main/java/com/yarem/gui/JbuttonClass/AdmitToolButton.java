package com.yarem.gui.JbuttonClass;

import com.yarem.dbConnect.HibernateSessionFactory;
import com.yarem.dbConnect.entity.AllSold;
import com.yarem.dbConnect.entity.Product;
import com.yarem.dbConnect.entity.ProductAllSold;
import org.hibernate.Session;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.List;

public class AdmitToolButton extends JButton {
    public AdmitToolButton() {
        ImageIcon imageIcon = new ImageIcon("src/main/resources/images/adminT.png");
        setBackground(new Color(225, 239, 165));
        setLayout(new BorderLayout());

        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        JLabel jLabel = new JLabel(imageIcon);
        jLabel.setLayout(null);
        add(jLabel);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminToolFrame adminToolFrame = new AdminToolFrame();
                adminToolFrame.setModal(true);
                adminToolFrame.setVisible(true);
            }
        });
    }
}



class AdminToolFrame extends JDialog{

    private Color color = new Color(135, 169, 92);
    private List<JPanel> panels = new ArrayList<>();
    private int currentPanel = 0;
    AdminToolFrame(){
        setTitle("Панель адміністратора");
        setResizable(false);
        panels.add(new CheckDataOrder());
        panels.add(new UpdateProductInfo());
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(new LineBorder(Color.BLACK));
        JMenu fileMenu = new JMenu("Меню");
        JMenuItem listBy = new JMenuItem("Список придбань");
        JMenuItem updateProduct = new JMenuItem("Редагування продуктів");
        fileMenu.add(listBy);
        fileMenu.addSeparator();
        fileMenu.add(updateProduct);
        fileMenu.setBorder(new MatteBorder(0,0,0,1,Color.BLACK));

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        add(panels.get(0));

        setSize(600,560);

        listBy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentPanel == 1){
                    AdminToolFrame.this.remove(panels.get(currentPanel));
                }  else if(currentPanel == 0){
                    return;
                }
                currentPanel = 0;
                AdminToolFrame.this.add(panels.get(currentPanel));
                AdminToolFrame.this.setSize(600,560);
                AdminToolFrame.this.revalidate();
                AdminToolFrame.this.repaint();
            }
        });

        updateProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentPanel == 0){
                    AdminToolFrame.this.remove(panels.get(0));
                } else if(currentPanel == 1){
                    return;
                }
                currentPanel = 1;
                AdminToolFrame.this.add(panels.get(currentPanel));
                ((UpdateProductInfo) panels.get(currentPanel)).updateProductInComboBox();
                AdminToolFrame.this.setSize(600,250);
                AdminToolFrame.this.revalidate();
                AdminToolFrame.this.repaint();
            }
        });

    }

    private class UpdateProductInfo extends JPanel{

        private JComboBox<String> stringJComboBox;
        private Color color = new Color(176, 220, 200);
        private Product currentProduct;
        UpdateProductInfo(){
            setBackground(color);
            setLayout(new GridLayout(3,1));

            JPanel panelChooseProduct  = new JPanel();
            panelChooseProduct.setBorder(new EmptyBorder(10,30,10,30));
            panelChooseProduct.setBackground(color);
            panelChooseProduct.setLayout(new GridLayout(1,2,80,5));

            JLabel chooseProduct = new JLabel("Оберіть продукт для зміни");
            chooseProduct.setFont(new Font("TimesRoman", Font.BOLD, 16));
            chooseProduct.setHorizontalAlignment(SwingConstants.CENTER);
            stringJComboBox = new JComboBox<>();
            stringJComboBox.setMaximumRowCount(3);
            updateProductInComboBox();

            JScrollPane scrollCombo = new JScrollPane(stringJComboBox);

            panelChooseProduct.add(chooseProduct);
            panelChooseProduct.add(scrollCombo);
            panelChooseProduct.setBounds(200,20,200,30);

            JPanel inputData = new JPanel();
            inputData.setBackground(color);
            inputData.setBorder(new EmptyBorder(0,15,0,15));
            GridLayout gridLayout = new GridLayout(2,5);
            gridLayout.setHgap(6);
            gridLayout.setVgap(6);
            inputData.setLayout(gridLayout);

            NameField descriptionProd = new NameField("Опис");
            JTextField descriptionProdField = new JTextField();
            PromptSupport.setPrompt("Менше 20 символів...", descriptionProdField);

            NameField countProd = new NameField("К-сть");
            JTextField countProdField = new JTextField();

            NameField priceProd = new NameField("Вартість");
            JTextField priceProdField = new JTextField();

            NameField tradeSlot = new NameField("Слот автомату");
            JComboBox<Integer> tradeSlotCombo = new JComboBox<>();
            tradeSlotCombo.setMaximumRowCount(4);
            JScrollPane tradeSlotScroll = new JScrollPane(tradeSlotCombo);
            for(int  i = 1; i <= 20; i++){
                tradeSlotCombo.addItem(i);
            }

            NameField tradeNow = new NameField("Продається зараз");
            tradeNow.setFont(new Font("TimesRoman", Font.BOLD, 11));
            JComboBox<String> tradeNowCombo = new JComboBox<>();
            tradeNowCombo.addItem("Так");
            tradeNowCombo.addItem("Ні");

            inputData.add(descriptionProd);
            inputData.add(countProd);
            inputData.add(priceProd);
            inputData.add(tradeSlot);
            inputData.add(tradeNow);
            inputData.add(descriptionProdField);
            inputData.add(countProdField);
            inputData.add(priceProdField);
            inputData.add(tradeSlotScroll);
            inputData.add(tradeNowCombo);

            JPanel panelButton = new JPanel();
            panelButton.setBackground(color);
            panelButton.setLayout(new BorderLayout());
            panelButton.setBorder(new EmptyBorder(15,120,10,120));
            JButton change = new JButton("Внести зміни");
            panelButton.add(change);



            add(panelChooseProduct);
            add(inputData);
            add(panelButton);


            stringJComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JComboBox comboBox = (JComboBox) e.getSource();
                    Object selected = comboBox.getSelectedItem();
                    if(selected == null){
                        return;
                    }
                    Session session = HibernateSessionFactory.openSession();
                    try {
                        session.getTransaction().begin();
                        currentProduct = session.find(Product.class, selected);
                        descriptionProdField.setText(currentProduct.getDescription());
                        countProdField.setText(Integer.toString(currentProduct.getCount()));
                        priceProdField.setText(Double.toString(currentProduct.getPriceForOne()));
                        tradeSlotCombo.setSelectedItem(currentProduct.getAutomatSlot() + 1);
                        if(currentProduct.isUseInAutomat()) {
                            tradeNowCombo.setSelectedItem("Так");
                        } else {
                            tradeNowCombo.setSelectedItem("Ні");
                        }
                        session.getTransaction().commit();
                    } catch (Exception ex){
                        System.err.println(ex);
                        session.getTransaction().rollback();
                    } finally {
                        session.close();
                    }
                }
            });

            change.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        boolean haveChange = false;
                        boolean changeSlot = false;
                        boolean changeUse = false;
                        if(descriptionProdField.getText().length() > 20){
                            JOptionPane.showMessageDialog(null,"Опис продукту не може бути більше 20 символів.");
                            return;
                        }
                        if (!descriptionProdField.getText().equals(currentProduct.getDescription())) {
                            currentProduct.setDescription(descriptionProdField.getText());
                            haveChange = true;
                        }
                        if(Integer.parseInt(countProdField.getText()) != currentProduct.getCount()){
                            if(Integer.parseInt(countProdField.getText()) < 0){
                                countProdField.setText( Integer.toString(currentProduct.getCount()));
                                JOptionPane.showMessageDialog(null,"Значення не може бути менше за 0");
                                return;
                            } else if (Integer.parseInt(countProdField.getText()) == 0) {
                                tradeNowCombo.setSelectedItem("Ні");
                            }
                            currentProduct.setCount(Integer.parseInt(countProdField.getText()));
                            haveChange = true;
                        }
                        if(Double.parseDouble(priceProdField.getText()) != currentProduct.getPriceForOne()){
                            currentProduct.setPriceForOne(Double.parseDouble(priceProdField.getText()));
                            haveChange = true;
                        }
                        if(((Integer) tradeSlotCombo.getSelectedItem())-1 != currentProduct.getAutomatSlot()){
                            currentProduct.setAutomatSlot(((Integer) tradeSlotCombo.getSelectedItem())-1);
                            haveChange = true;
                            changeSlot = true;
                        }
                        boolean currentUseInAutomat = tradeNowCombo.getSelectedItem() != "Ні";
                        if(currentUseInAutomat != currentProduct.isUseInAutomat()){
                            currentProduct.setUseInAutomat(currentUseInAutomat);
                            haveChange = true;
                            changeUse = true;
                        }
                        if(haveChange){
                            Session session = HibernateSessionFactory.openSession();
                            try {
                                session.getTransaction().begin();
                                currentProduct = (Product) session.merge(currentProduct);
                                if((changeSlot && currentProduct.isUseInAutomat()) || (changeUse && currentProduct.isUseInAutomat())){
                                    List<Product> list = session.createQuery("SELECT p FROM Product p WHERE p.automatSlot = :slot").
                                            setParameter("slot",currentProduct.getAutomatSlot()).getResultList();
                                    for(Product p : list){
                                        if(p == currentProduct){
                                            continue;
                                        }
                                        p.setUseInAutomat(false);
                                    }
                                }
                                session.getTransaction().commit();
                                JOptionPane.showMessageDialog(null,"Продукт успішно змінено");
                            } catch (Exception ex){
                                JOptionPane.showMessageDialog(null,"При внесені змін у даних БД сталась помилка, спробуйте пізніше.");
                                System.err.println(ex);
                                session.getTransaction().rollback();
                                return;
                            } finally {
                                session.close();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,"Ви нічого не змінили.");
                        }
                    } catch (IllegalArgumentException ex){
                        JOptionPane.showMessageDialog(null,"Будь-ласка вводьте коректні дані.");
                        System.err.println(ex);
                    }
                }
            });
        }

        private class NameField extends JLabel{
            NameField(String s){
                super(s);
                setFont(new Font("TimesRoman", Font.BOLD, 15));
                setHorizontalAlignment(SwingConstants.CENTER);
            }
        }




        public void updateProductInComboBox(){
            stringJComboBox.removeAllItems();
            String[] nameProd;
            Session session = HibernateSessionFactory.openSession();
            try {
                session.getTransaction().begin();
                List<Product> products = session.createQuery("SELECT p FROM Product p").getResultList();
                nameProd = new String[products.size()];
                int index = 0;
                for(Product product : products){
                    nameProd[index++] = product.getName();
                }
                session.getTransaction().commit();
            } catch (Exception e){
                System.err.println(e);
                session.getTransaction().rollback();
                return;
            } finally {
                session.close();
            }
            if(nameProd == null || nameProd.length == 0){
                JOptionPane.showMessageDialog(null,"У базі даних немає продуктів, будь ласка добавте їх");
                return;
            }
            for(String s : nameProd){
                stringJComboBox.addItem(s);
            }
        }
    }

    private class CheckDataOrder extends JPanel {


        private JTable databaseTable;
        private JScrollPane scrollPane;
        private Color color = AdminToolFrame.this.color;

        CheckDataOrder(){
            setLayout(null);
            setBackground(color);

            databaseTable = new JTable();

            JPanel labelFieldPanel = new JPanel();
            labelFieldPanel.setBackground(color);
            JLabel firstData = new JLabel("Оберіть дату");
            firstData.setFont(new Font("TimesRoman", Font.BOLD, 15));
            firstData.setHorizontalAlignment(SwingConstants.CENTER);
            firstData.setBackground(color);
            JTextField firstDataField = new JTextField();
            firstDataField.setFont(new Font("TimesRoman", Font.BOLD, 15));
            PromptSupport.setPrompt("YYYY-MM-DD", firstDataField);
            PromptSupport.setForeground(new Color(65, 112, 229),firstDataField);
            firstDataField.setBackground(color);
            JLabel secondData = new JLabel("Оберіть дату 2");
            secondData.setFont(new Font("TimesRoman", Font.BOLD, 15));
            secondData.setHorizontalAlignment(SwingConstants.CENTER);
            secondData.setBackground(color);
            JTextField secondDataField = new JTextField();
            secondDataField.setBackground(color);
            PromptSupport.setPrompt("YYYY-MM-DD", secondDataField);
            PromptSupport.setForeground(new Color(65, 112, 229),secondDataField);
            labelFieldPanel.setBounds(50,10,485,25);
            labelFieldPanel.setBorder(new EmptyBorder(0,130,0,130));

            labelFieldPanel.setLayout(new GridLayout(1,4,10,0));
            labelFieldPanel.add(firstData);
            labelFieldPanel.add(firstDataField);

            scrollPane = new JScrollPane(databaseTable);
            scrollPane.setBounds(0,120,585,330);

            JPanel radioButtonPanel = new JPanel();
            JRadioButton j1 = new JRadioButton("BEFORE");
            JRadioButton j2 = new JRadioButton("DURING");
            JRadioButton j3 = new JRadioButton("AFTER");
            JRadioButton j4 = new JRadioButton("BETWEEN");
            j1.setFont(new Font("TimesRoman", Font.BOLD, 14));
            j2.setFont(new Font("TimesRoman", Font.BOLD, 14));
            j3.setFont(new Font("TimesRoman", Font.BOLD, 14));
            j4.setFont(new Font("TimesRoman", Font.BOLD, 14));
            radioButtonPanel.setBorder(new EmptyBorder(2,2,3,3));
            j4.setToolTipText("<html><h4>При виборі даного параметра ви побачите нове місце для заповнення." +
                    "</h4><p>Інші параметри дат будуть використовувати лише перший.</p></html>");
            j1.setBackground(color);
            j1.setFocusPainted(false);
            j2.setBackground(color);
            j2.setFocusPainted(false);
            j3.setBackground(color);
            j3.setFocusPainted(false);
            j4.setBackground(color);
            j4.setFocusPainted(false);
            radioButtonPanel.setLayout(new GridLayout(1,4));
            radioButtonPanel.add(j1);
            radioButtonPanel.add(j2);
            radioButtonPanel.add(j3);
            radioButtonPanel.add(j4);
            radioButtonPanel.setBounds(50,45,485,30);
            radioButtonPanel.setBackground(new Color(82, 107, 227));

            JButton takeData = new JButton("Отримати дані");
            takeData.setBounds(200,82,185,30);

            JButton moreInfo = new JButton("Отримати більше інформації");
            moreInfo.setToolTipText("Працює для всіх обраних строк таблиці.");

            moreInfo.setBounds(150,460,300,30);
            moreInfo.setEnabled(false);


            add(labelFieldPanel);
            add(radioButtonPanel);
            add(scrollPane);
            add(takeData);
            add(moreInfo);


            j4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(j4.isSelected()){
                        labelFieldPanel.add(secondData);
                        labelFieldPanel.add(secondDataField);
                        firstData.setText("Оберіть дату 1");
                        labelFieldPanel.setBorder(new EmptyBorder(0,0,0,0));
                        labelFieldPanel.revalidate();
                        labelFieldPanel.repaint();
                    } else {
                        labelFieldPanel.remove(secondData);
                        labelFieldPanel.remove(secondDataField);
                        firstData.setText("Оберіть дату");
                        labelFieldPanel.setBorder(new EmptyBorder(0,130,0,130));
                        labelFieldPanel.revalidate();
                        labelFieldPanel.repaint();
                    }
                }
            });

            takeData.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] columnHeaders = {"Id", "ByDate", "TotalPrice"};

                    if(!j1.isSelected() && !j2.isSelected() && !j3.isSelected() && !j4.isSelected()){
                        JOptionPane.showMessageDialog(null,"Будь-ласка оберіть більше одного параметра відрізка часу.");
                        return;
                    }

                    Set<AllSold> listProd = new HashSet<>();
                    Session session = HibernateSessionFactory.openSession();
                    try {
                        session.getTransaction().begin();
                        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                        CriteriaQuery<AllSold> allSoldCriteriaQuery = criteriaBuilder.createQuery(AllSold.class);
                        Root<AllSold> allSoldRoot = allSoldCriteriaQuery.from(AllSold.class);
                        List<Predicate> predicateList = new ArrayList<>();
                        if(j1.isSelected()){
                            predicateList.add(criteriaBuilder.lessThan(allSoldRoot.get("byDate"), Date.valueOf(firstDataField.getText())));
                        }
                        if(j2.isSelected()){
                            predicateList.add(criteriaBuilder.equal(allSoldRoot.get("byDate"), Date.valueOf(firstDataField.getText())));
                        }
                        if(j3.isSelected()){
                            predicateList.add(criteriaBuilder.greaterThan(allSoldRoot.get("byDate"), Date.valueOf(firstDataField.getText())));
                        }
                        if(j4.isSelected()){
                            predicateList.add(criteriaBuilder.between(allSoldRoot.get("byDate"), Date.valueOf(firstDataField.getText()),Date.valueOf(secondDataField.getText())));
                        }
                        for(Predicate p : predicateList) {
                            allSoldCriteriaQuery.select(allSoldRoot).where(p);
                            listProd.addAll(session.createQuery(allSoldCriteriaQuery).getResultList());
                        }
                        session.getTransaction().commit();
                    } catch (IllegalArgumentException ilEx){
                        JOptionPane.showMessageDialog(null,"Вводьте коректні дані полів дат. Формат дати - YYYY-MM-DD.");
                        System.err.println(ilEx);
                        session.getTransaction().rollback();
                        return;
                    } catch (Exception ex){
                        System.err.println(ex);
                        session.getTransaction().rollback();
                    } finally {
                        session.close();
                    }


                    Object[][] data = new Object[listProd.size()][3];

                    int index = 0;
                    for(AllSold allSold : listProd){
                        data[index][0] = allSold.getId();
                        data[index][1] = allSold.getByDate();
                        data[index][2] = allSold.getFinalPrice();
                        index++;
                    }
                    JOptionPane.showMessageDialog(null,"За вашим запитом знайдено "+listProd.size()+" купівлі.");
                    setNewTableData(columnHeaders,data);
                    if(listProd.size() == 0){
                        moreInfo.setEnabled(false);
                        return;
                    }
                    if(!moreInfo.isEnabled()){
                        moreInfo.setEnabled(true);
                    }
                }
            });

            moreInfo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DefaultTableModel model = (DefaultTableModel) databaseTable.getModel();
                    int[] selectedRows = databaseTable.getSelectedRows();
                    String[] id = new String[selectedRows.length];
                    int index = 0;
                    for (int row : selectedRows) {
                        // Отримання даних строки
                        Object[] rowData = new Object[model.getColumnCount()];
                        for (int col = 0; col < model.getColumnCount(); col++) {
                            rowData[col] = model.getValueAt(row, col);
                        }
                        id[index++] = Integer.toString( (Integer) rowData[0]);
                    }
                    MoreInfoByProd moreInfoByProd = new MoreInfoByProd(id, AdminToolFrame.this);
                    moreInfoByProd.setVisible(true);
                }
            });
        }

        public void setNewTableData(String[] columnHeaders,Object[][] data){
            scrollPane.getViewport().remove(databaseTable);
            DefaultTableModel model = getDefaultTableModel(columnHeaders, data);
            databaseTable = new JTable(model);
            scrollPane.getViewport().add(databaseTable);
            scrollPane.revalidate();
            scrollPane.repaint();
        }

        public DefaultTableModel getDefaultTableModel(String[] columnHeaders,Object[][] data){
            return new DefaultTableModel(data,columnHeaders) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        }

        private class MoreInfoByProd extends JDialog{

            private JScrollPane scrollAdditionalInfo;
            private JTable additionalInfo;
            MoreInfoByProd(String[] strings,JDialog owner){
                super(owner,"Додаткова інформація",true);
                setLayout(null);
                setResizable(true);
                getContentPane().setBackground(new Color(176, 220, 200));

                JPanel panelChooseCheck  = new JPanel();
                panelChooseCheck.setBackground(new Color(176, 220, 200));
                panelChooseCheck.setLayout(new GridLayout(1,2,20,5));
                JLabel chooseData = new JLabel("Оберіть номер покупки");
                chooseData.setFont(new Font("TimesRoman", Font.BOLD, 14));
                chooseData.setHorizontalAlignment(SwingConstants.CENTER);
                JComboBox<String> stringJComboBox = new JComboBox<>();
                for(String s : strings){
                    stringJComboBox.addItem(s);
                }
                stringJComboBox.setMaximumRowCount(3);
                JScrollPane scrollCombo = new JScrollPane(stringJComboBox);
                panelChooseCheck.add(chooseData);
                panelChooseCheck.add(scrollCombo);
                panelChooseCheck.setBorder(new EmptyBorder(0,15,0,15));
                panelChooseCheck.setBounds(0,5,385,30);
                JButton getInfo = new JButton("Отримати");
                getInfo.setBounds(100,43,200,30);
                additionalInfo = new JTable();
                scrollAdditionalInfo = new JScrollPane();
                scrollAdditionalInfo.add(additionalInfo);
                scrollAdditionalInfo.setBounds(5,80,375,130);


                add(panelChooseCheck);
                add(getInfo);
                add(scrollAdditionalInfo);
                setSize(400,255);

                getInfo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String[] columnHeaders = {"Name", "CountBy", "TotalCost"};
                        Session session = HibernateSessionFactory.openSession();
                        List<ProductAllSold> list = new ArrayList<>();
                        try {
                            session.getTransaction().begin();
                            list.addAll(session.createQuery("SELECT pa FROM ProductAllSold pa WHERE pa.id.allSold_id = :allSoldId").
                                    setParameter("allSoldId",Integer.parseInt((String) stringJComboBox.getSelectedItem())).getResultList());
                            session.getTransaction().commit();
                        } catch (Exception ex){
                            System.err.println(ex);
                            session.getTransaction().rollback();
                        } finally {
                            session.close();
                        }
                        if(list.size() == 0){
                            JOptionPane.showMessageDialog(null,"При завантажені інформації даної покупки сталась помилка.\n" +
                                    "Можливо її було видалено - зверніться до програмістів.");
                            return;
                        }
                        Object[][] data = new Object[list.size()][3];
                        int index = 0;
                        DecimalFormat df = new DecimalFormat("0.##",new DecimalFormatSymbols(Locale.ENGLISH));
                        for(ProductAllSold productAllSold : list){
                            data[index][0] = productAllSold.getProductId();
                            data[index][1] = productAllSold.getCount();
                            data[index][2] = df.format(productAllSold.getTotalCost());
                            index++;
                        }
                        MoreInfoByProd.this.scrollAdditionalInfo.getViewport().remove(additionalInfo);
                        DefaultTableModel model = getDefaultTableModel(columnHeaders,data);
                        MoreInfoByProd.this.additionalInfo = new JTable(model);
                        MoreInfoByProd.this.scrollAdditionalInfo.getViewport().add(additionalInfo);
                        MoreInfoByProd.this.scrollAdditionalInfo.revalidate();
                        MoreInfoByProd.this.scrollAdditionalInfo.repaint();
                    }
                });
            }

        }

    }
}

