package org.jorgechato;

import java.io.IOException;

/**
 * Created by jorge on 27/01/15.
 */
public class ServerThreadSocket {
    public static final int PORT = 6633;

    public static void main(String[] args) {
        Main server = new Main(PORT);
        Customer customer = null;

        try{
            server.connect();

            while (server.isConnected()){
                customer = new Customer(server.listen(),server);
                server.addCustomer(customer);
                customer.getWriter().println("/server Nuevo cliente conectado");
                customer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
