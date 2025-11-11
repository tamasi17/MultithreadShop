import log4Mats.LogLevel;
import log4Mats.Logger;
import logging.LoggerProvider;
import models.ClienteNormal;
import models.GestorAlmacen;
import models.Tienda;
import models.Transportista;

import java.util.ArrayList;

public class Main {

    static Logger LOGGER = LoggerProvider.getLogger();
    static final int NUM_CLIENTES = 50;
    static final int NUM_GESTORES = 20;

    static void main() {
        // Logger pasa informacion por consola
        LOGGER.setLogToConsole(true);

        // Abrimos tienda
        Tienda tienda = new Tienda();

        ArrayList<Thread> clientes = generarClientes(tienda);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            LOGGER.log(LogLevel.TRACE, "Tiempo para que los clientes hagan sus pedidos");
        }

        ArrayList<Thread> gestores = generarGestores(tienda);

        ArrayList<Thread> transportistas = generarTransportistas(tienda);

        try {
            for (Thread c : clientes) c.join();
        } catch (InterruptedException ie) {
            LOGGER.log(LogLevel.TRACE, "Esperando a que terminen Clientes");
        }


        // AQUI ENTRA EN BUCLE:
        // PERO INCLUSO COMENTADA LOS GESTORES Y TRANSPORTISTAS NO CONTINUAN SU TRABAJO
        /*
        while (!tienda.isColaRecibidosEmpty() || !tienda.isColaProcesadosEmpty()) {
            try {
                Thread.sleep(300); // small pause, don’t busy-loop
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
         */

        tienda.muestraPedidos();

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
                LOGGER.log(LogLevel.TRACE, "Ha llegado cliente "+ cliente.getID() +" a la tienda");
            }
        });
        generadorClientes.start();

        try {
            Thread.sleep(90);
        } catch (InterruptedException e) {
            LOGGER.log(LogLevel.TRACE, "Clientes miran los productos");
        }

        while (!tienda.getColaClientes().isEmpty()) {
            ClienteNormal c = tienda.getColaClientes().poll();
            if (c != null) {
                c.start();
            }
        }

        return listaClientes;
    }

    private static ArrayList<Thread> generarTransportistas(Tienda tienda) {
        ArrayList<Thread> listaTransportistas = new ArrayList<>();
        while (tienda.isOpen() && !tienda.isColaProcesadosEmpty()){
            Transportista transportista = new Transportista(tienda);
            listaTransportistas.add(transportista.getThread());
            transportista.getThread().start();
        }
        return listaTransportistas;
    }

    private static ArrayList<Thread> generarGestores(Tienda tienda) {
        ArrayList<Thread> listaGestores = new ArrayList<>();
        for (int i = 0; i < NUM_GESTORES; i++) {
            Thread gestor = new Thread(new GestorAlmacen(tienda));
            listaGestores.add(gestor);
            gestor.start();
        }
        return listaGestores;
    }



    private static void trabajadoresSeVanACasa(ArrayList<Thread> gestores, ArrayList<Thread> transportistas) {
        try {
            for (Thread g : gestores) g.join();
            for (Thread t : transportistas) t.join();
        } catch (InterruptedException ie) {
            LOGGER.log(LogLevel.TRACE, "Esperando a que terminen Gestores y Transportistas");
        }
    }
}
