import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

public class UpdateProductUI {

    public JFrame view;

    public JButton btnLoad = new JButton("Load Product");
    public JButton btnSave = new JButton("Save Product");

    public JTextField txtProductID = new JTextField(20);
    public JTextField txtName = new JTextField(20);
    public JTextField txtPrice = new JTextField(20);
    public JTextField txtQuantity = new JTextField(20);


    public UpdateProductUI() {
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
        line1.add(new JLabel("ProductID "));
        line1.add(txtProductID);
        view.getContentPane().add(line1);

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("Name "));
        line2.add(txtName);
        view.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("Price "));
        line3.add(txtPrice);
        view.getContentPane().add(line3);

        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Quantity "));
        line4.add(txtQuantity);
        view.getContentPane().add(line4);


        btnLoad.addActionListener(new LoadButtonListerner());

        btnSave.addActionListener(new SaveButtonListener());

    }

    public void run() {
        view.setVisible(true);
    }

    class LoadButtonListerner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ProductModel product = new ProductModel();
            String id = txtProductID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "ProductID cannot be null!");
                return;
            }

            try {
                product.mProductID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ProductID is invalid!");
                return;
            }
            Gson gson = new Gson();
            MessageModel msg = new MessageModel();
            msg.code = MessageModel.GET_PRODUCT;
            msg.data = gson.toJson(product);

            SocketNetworkAdapter net = new SocketNetworkAdapter();

            try {
                msg = net.send(msg, "localhost", 8001);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (msg.code == MessageModel.OPERATION_FAILED) {
                JOptionPane.showMessageDialog(null, "Product does not exist.");
            } else {
                product = gson.fromJson(msg.data, ProductModel.class);
                if (product.mName.equals("null")) {
                    JOptionPane.showMessageDialog(null, "Product does not exist.");
                    return;
                }

                txtName.setText(product.mName);
                txtPrice.setText(Double.toString(product.mPrice));
                txtQuantity.setText(Double.toString(product.mQuantity));
            }

        }
    }

    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ProductModel product = new ProductModel();
            String id = txtProductID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "ProductID cannot be null!");
                return;
            }

            try {
                product.mProductID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ProductID is invalid!");
                return;
            }

            String name = txtName.getText();
            if (name.length() == 0) {
                JOptionPane.showMessageDialog(null, "Product name cannot be empty!");
                return;
            }

            product.mName = name;

            String price = txtPrice.getText();
            try {
                product.mPrice = Double.parseDouble(price);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Price is invalid!");
                return;
            }

            String quant = txtQuantity.getText();
            try {
                product.mQuantity = Double.parseDouble(quant);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantity is invalid!");
                return;
            }

            // all product infor is ready! Send to Server!

            Gson gson = new Gson();
            MessageModel msg = new MessageModel();
            msg.code = MessageModel.PUT_PRODUCT;
            msg.data = gson.toJson(product);

            SocketNetworkAdapter network = new SocketNetworkAdapter();

            try {
                msg = network.send(msg, "localhost", 8001);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (msg.code == MessageModel.OPERATION_FAILED) {
                JOptionPane.showMessageDialog(null, "Product not added successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Product added successfully.");
            }
        }
        }
    }