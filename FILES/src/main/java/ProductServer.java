import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;
import com.google.gson.Gson;

public class ProductServer {
    static String dbfile = "/home/ken0018/Academics/COMP3700/IDEA/ModelingAndDesign/store.db";

    public static void main(String[] args) {

        int port = 8001;

        if (args.length > 0) {
            System.out.println("Running arguments: ");
            for (String arg : args)
                System.out.println(arg);
            port = Integer.parseInt(args[0]);
            dbfile = args[1];
        }

        try {
            SQLiteDataAdapter adapter = new SQLiteDataAdapter();
            Gson gson = new Gson();
            adapter.connect(dbfile);

            ServerSocket server = new ServerSocket(port);
            System.out.println("Server is listening at port = " + port);

            while (true) {
                Socket pipe = server.accept();
                PrintWriter out = new PrintWriter(pipe.getOutputStream(), true);
                Scanner in = new Scanner(pipe.getInputStream());

                MessageModel msg = gson.fromJson(in.nextLine(), MessageModel.class);
                if (msg.code == MessageModel.GET_CUSTOMER) {
                    String[] temp = msg.data.split(":");
                    temp = temp[1].split("}");
                    String data = temp[0];
                    System.out.println("GET customer with id = " + data);
                    CustomerModel cust = adapter.loadCustomer(Integer.parseInt(data));

                    if (cust == null) {
                        msg.code = MessageModel.OPERATION_FAILED;
                    } else {
                        msg.code = MessageModel.OPERATION_OK;
                        msg.data = gson.toJson(cust);
                    }
                    out.println(gson.toJson(msg));

                } else if (msg.code == MessageModel.PUT_CUSTOMER) {
                    CustomerModel cust = gson.fromJson(msg.data, CustomerModel.class);
                    System.out.println("PUT customer with customer = " + cust);
                    int res = adapter.saveCustomer(cust);
                    if (res == IDataAdapter.CUSTOMER_SAVED_OK) {
                        msg.code = MessageModel.OPERATION_OK;
                    } else {
                        msg.code = MessageModel.OPERATION_FAILED;
                    }
                    out.println(gson.toJson(msg));

                } else if (msg.code == MessageModel.GET_PRODUCT) {
                    String[] temp = msg.data.split(",");
                    temp = temp[0].split(":");
                    String data = temp[1];
                    System.out.println("GET product with id = " + data);
                    ProductModel prod = adapter.loadProduct(Integer.parseInt(data));

                    if (prod == null) {
                        msg.code = MessageModel.OPERATION_FAILED;
                    } else {
                        msg.code = MessageModel.OPERATION_OK;
                        msg.data = gson.toJson(prod);
                    }
                    out.println(gson.toJson(msg));

                } else if (msg.code == MessageModel.PUT_PRODUCT) {
                    ProductModel prof = gson.fromJson(msg.data, ProductModel.class);
                    System.out.println("PUT product with product = " + prof);
                    int res = adapter.saveProduct(prof);
                    if (res == IDataAdapter.PRODUCT_SAVED_OK) {
                        msg.code = MessageModel.OPERATION_OK;
                    } else {
                        msg.code = MessageModel.OPERATION_FAILED;
                    }
                    out.println(gson.toJson(msg));

                } else if (msg.code == MessageModel.GET_PURCHASE) {
                    String[] temp = msg.data.split(",");
                    temp = temp[0].split(":");
                    String data = temp[1];

                    System.out.println("GET purchase with id = " + data);
                    PurchaseModel pur = adapter.loadPurchase(Integer.parseInt(data));
                    if (pur == null) {
                        msg.code = MessageModel.OPERATION_FAILED;
                    } else {
                        msg.code = MessageModel.OPERATION_OK;
                        msg.data = gson.toJson(pur);
                    }
                    out.println(gson.toJson(msg));

                } else if (msg.code == MessageModel.PUT_PURCHASE) {
                    PurchaseModel pur = gson.fromJson(msg.data, PurchaseModel.class);
                    System.out.println("PUT purchase with purchase = " + pur);
                    int res = adapter.savePurchase(pur);
                    if (res == IDataAdapter.PURCHASE_SAVED_OK) {
                        msg.code = MessageModel.OPERATION_OK;
                    } else {
                        msg.code = MessageModel.OPERATION_FAILED;
                    }
                    out.println(gson.toJson(msg));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}