import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

public class UpdatePurchaseUI {

    public JFrame view;

    public JButton btnLoad = new JButton("Load Product");
    public JButton btnSave = new JButton("Save Product");

    public JTextField txtPurchaseID = new JTextField(20);
    public JTextField txtProductID = new JTextField(20);
    public JTextField txtCustomerID = new JTextField(20);
    public JTextField txtQuantity = new JTextField(20);
    public JTextField txtPrice = new JTextField(20);
    public JTextField txtCost = new JTextField(20);
    public JTextField txtTax = new JTextField(20);
    public JTextField txtTotal = new JTextField(20);
    public JTextField txtDate = new JTextField(20);


    public UpdatePurchaseUI() {
        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Update Product Information");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnLoad);
        panelButtons.add(btnSave);
        view.getContentPane().add(panelButtons);

        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("PurchaseID "));
        line1.add(txtPurchaseID);
        view.getContentPane().add(line1);

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("ProductID "));
        line2.add(txtProductID);
        view.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("CustomerID "));
        line3.add(txtCustomerID);
        view.getContentPane().add(line3);

        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Quantity "));
        line4.add(txtQuantity);
        view.getContentPane().add(line4);

        JPanel line5 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Price "));
        line4.add(txtPrice);
        view.getContentPane().add(line5);

        JPanel line6 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Cost "));
        line4.add(txtCost);
        view.getContentPane().add(line6);

        JPanel line7 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Tax "));
        line4.add(txtTax);
        view.getContentPane().add(line7);

        JPanel line8 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Total "));
        line4.add(txtTotal);
        view.getContentPane().add(line8);

        JPanel line9 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Date "));
        line4.add(txtDate);
        view.getContentPane().add(line9);



        btnLoad.addActionListener(new LoadButtonListerner());

        btnSave.addActionListener(new SaveButtonListener());

    }

    public void run() {
        view.setVisible(true);
    }

    class LoadButtonListerner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            PurchaseModel purchase = new PurchaseModel();
            String id = txtPurchaseID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "PurchaseID cannot be null!");
                return;
            }

            try {
                purchase.mPurchaseID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "PurchaseID is invalid!");
                return;
            }

            Gson gson = new Gson();
            MessageModel msg = new MessageModel();
            msg.code = MessageModel.GET_PURCHASE;
            msg.data = gson.toJson(purchase);

            SocketNetworkAdapter network = new SocketNetworkAdapter();

            try {
                msg = network.send(msg, "localhost", 8001);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(msg.code == MessageModel.OPERATION_FAILED) {
                JOptionPane.showMessageDialog(null, "Purchase does not exist");
            }
            else {
                purchase = gson.fromJson(msg.data, PurchaseModel.class);
                if(purchase.mProductID == 0) {
                    JOptionPane.showMessageDialog(null, "Purchase does not exist");
                    return;
                }
            }
            txtPurchaseID.setText(Integer.toString(purchase.mPurchaseID));
            txtProductID.setText(Integer.toString(purchase.mProductID));
            txtCustomerID.setText(Integer.toString(purchase.mPurchaseID));
            txtQuantity.setText(Double.toString(purchase.mQuantity));
            txtPrice.setText(Double.toString(purchase.mPrice));
            txtCost.setText(Double.toString(purchase.mCost));
            txtTax.setText(Double.toString(purchase.mCost));
            txtTotal.setText(Double.toString(purchase.mTotal));
            txtTotal.setText(purchase.mDate);

        }
    }

    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            PurchaseModel purchase = new PurchaseModel();
            String id = txtPurchaseID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "PurchaseID cannot be null!");
                return;
            }

            try {
                purchase.mPurchaseID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "PurchaseID is invalid!");
                return;
            }

            try {
                purchase.mProductID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ProductID is invalid!");
                return;
            }

            try {
                purchase.mCustomerID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "CustomerID is invalid!");
                return;
            }

            String quantity = txtQuantity.getText();
            purchase.mQuantity = Double.parseDouble(quantity);

            String price = txtPrice.getText();
            purchase.mPrice = Double.parseDouble(price);

            String cost = txtCost.getText();
            purchase.mCost = Double.parseDouble(cost);

            String tax = txtTax.getText();
            purchase.mTax = Double.parseDouble(tax);

            String total = txtTotal.getText();
            purchase.mTotal = Double.parseDouble(total);

            String date = txtDate.getText();
            purchase.mDate = date;

            Gson gson = new Gson();
            MessageModel msg = new MessageModel();
            msg.code = MessageModel.PUT_PURCHASE;
            msg.data = gson.toJson(purchase);

            SocketNetworkAdapter network = new SocketNetworkAdapter();

            try {
                msg = network.send(msg, "localhost", 8001);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (msg.code == MessageModel.OPERATION_FAILED) {
                JOptionPane.showMessageDialog(null, "Purchase not added successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Purchase added successfully.");
            }

        }
    }
}