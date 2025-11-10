import log4Mats.LogLevel;
import log4Mats.Logger;
import logging.LoggerProvider;
import models.ClienteNormal;
import models.GestorAlmacen;
import models.Tienda;
import models.Transportista;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static Logger logger = LoggerProvider.getLogger();
    static final int NUM_CLIENTES = 50;
    static final int NUM_GESTORES = 20;

    static void main() {
        // Logger pasa informacion por consola
        logger.setLogToConsole(true);

        // Abrimos tienda
        Tienda tienda = new Tienda();

        ArrayList<Thread> clientes = generarClientes(tienda);

        ArrayList<Thread> gestores = generarGestores(tienda);

        ArrayList<Thread> transportistas = generarTransportistas(tienda);

        try {
            for (Thread c : clientes) c.join();
        } catch (InterruptedException ie) {
            logger.log(LogLevel.TRACE, "Esperando a que terminen Clientes");
        }

        tienda.muestraPedidos();

        tienda.close();

        try {
        for (Thread g : gestores) g.join();
        for (Thread t : transportistas) t.join();
        } catch (InterruptedException ie) {
            logger.log(LogLevel.TRACE, "Esperando a que terminen Gestores y Transportistas");
        }


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

    private static ArrayList<Thread> generarClientes(Tienda tienda) {
        ArrayList<Thread> listaClientes = new ArrayList<>();

        Thread generadorClientes = new Thread(() -> {
            for (int i = 0; i < NUM_CLIENTES; i++) {
                ClienteNormal cliente = new ClienteNormal(tienda);
                tienda.getColaClientes().add(cliente);
                listaClientes.add(cliente);
                logger.log(LogLevel.TRACE, "Ha llegado un cliente a la tienda");
            }
        });
        generadorClientes.start();

        try {
            Thread.sleep(90);
        } catch (InterruptedException e) {
            logger.log(LogLevel.TRACE, "Clientes miran los productos");
        }

        while (!tienda.getColaClientes().isEmpty()) {
            ClienteNormal c = tienda.getColaClientes().poll();
            if (c != null) {
                c.start();
            }
        }

        return listaClientes;
    }
}
