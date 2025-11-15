package main.java;


import main.java.logging.LogLevel;
import main.java.logging.Logger;
import main.java.logging.LoggerProvider;
import main.java.models.*;

import java.util.ArrayList;

public class Main {

    static Logger LOGGER = LoggerProvider.getLogger();
    static final int NUM_CLIENTES = 0;
    static final int NUM_VIP = 25;
    static final int NUM_GESTORES = 0;
    static final int NUM_TRANSPORTISTAS = 0;

    public static void main(String[] args) {

        // Logger pasa informacion por consola
        LOGGER.setLogToConsole(false);

        // Abrimos tienda
        Tienda tienda = new Tienda();

        // LLegan clientes
        ArrayList<Thread> clientes = generarClientes(tienda);
        ArrayList<Thread> clientesVIP = generarClientesVIP(tienda);

        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            LOGGER.trace("Tiempo para que los clientes hagan sus pedidos");
        }

        // Llegan worker threads
        ArrayList<Thread> gestores = generarGestores(tienda);
        ArrayList<Thread> transportistas = generarTransportistas(tienda);

        try {
            for (Thread c : clientes) c.join();
        } catch (InterruptedException ie) {
            LOGGER.trace("Esperando a que terminen Clientes");
        }

        tienda.close();

        trabajadoresSeVanACasa(gestores, transportistas);

    }


    /**
     * Metodo que añade clientes a traves de un thread.
     * Conecta con ColaClientes de la tienda
     * Genera un ArrayList<Thread> para gestionar los join() de clientes.
     * @param tienda
     * @return
     */
    private static ArrayList<Thread> generarClientes(Tienda tienda) {
        ArrayList<Thread> listaClientes = new ArrayList<>();
        Thread generadorClientes = new Thread(() -> {
            for (int i = 0; i < NUM_CLIENTES; i++) {
                ClienteNormal cliente = new ClienteNormal(tienda);
                tienda.getColaClientes().add(cliente);
                listaClientes.add(cliente);
                LOGGER.trace("Ha llegado cliente "+ cliente.getID() +" a la tienda");
            }
        });
        generadorClientes.start();

        try {
            Thread.sleep(90);
        } catch (InterruptedException e) {
            LOGGER.trace("Clientes miran los productos");
        }

        while (!tienda.getColaClientes().isEmpty()) {
            ClienteNormal c = tienda.getColaClientes().poll();
            if (c != null) {
                c.start();
            }
        }

        return listaClientes;
    }

    private static ArrayList<Thread> generarClientesVIP(Tienda tienda){
        ArrayList<Thread> listaVIP = new ArrayList<>();
        LOGGER.info("Llegan los clientes VIP");
        for (int i = 0; i < NUM_VIP; i++) {
            ClienteVIP vip = new ClienteVIP(tienda);
            listaVIP.add(vip);
            vip.start();
        }
        return listaVIP;
    }

    private static ArrayList<Thread> generarGestores(Tienda tienda) {
        ArrayList<Thread> listaGestores = new ArrayList<>();
        for (int i = 0; i < NUM_GESTORES; i++) {
            Thread gestor = new Thread(new GestorAlmacen(tienda), "G"+i);
            listaGestores.add(gestor);
            gestor.start();
        }
        return listaGestores;
    }


    private static ArrayList<Thread> generarTransportistas(Tienda tienda) {
        LOGGER.info("Transportistas comienzan jornada");
        ArrayList<Thread> listaTransportistas = new ArrayList<>();

        // Create a fixed number of transportistas
        for (int i = 0; i < NUM_TRANSPORTISTAS; i++) {
            Transportista transportista = new Transportista(tienda);
            listaTransportistas.add(transportista.getThread());
            transportista.getThread().start();
        }

        return listaTransportistas;
    }

    private static void trabajadoresSeVanACasa(ArrayList<Thread> gestores, ArrayList<Thread> transportistas) {
        LOGGER.trace("Esperando a que terminen Gestores y Transportistas...");
        try {
            for (Thread g : gestores) g.join();
            for (Thread t : transportistas) t.join();
            Thread.sleep(100);
            System.out.println("=======\n Jornada completada");
        } catch (InterruptedException ie) {
            // ignoramos esto
        }

    }
}
