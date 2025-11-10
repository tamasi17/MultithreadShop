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
    static final int NUM_TRANSPORTISTAS = 10;

    static void main() {
        // Logger pasa informacion por consola
        logger.setLogToConsole(true);

        // Abrimos tienda
        Tienda tienda = new Tienda();

        generarClientes(tienda);

        // en run() - while(tienda.isOpen y !tienda.colaProcesados/Pedidos.isEmpty) ?
        generarGestores(tienda);

        generarTransportistas(tienda);


        // joins?
        tienda.muestraPedidos();

    }

    private static void generarTransportistas(Tienda tienda) {
        List<Thread> transportistas = new ArrayList<>();
        for (int i = 0; i < NUM_TRANSPORTISTAS; i++) {
            Transportista t = new Transportista(tienda);
            transportistas.add(t.getThread());
            t.getThread().start();
        }
    }

    private static void generarGestores(Tienda tienda) {
        List<Thread> gestores = new ArrayList<>();
        for (int i = 0; i < NUM_GESTORES; i++) {
            Thread gestor = new Thread(new GestorAlmacen(tienda));
            gestores.add(gestor);
            gestor.start();
        }
    }

    private static void generarClientes(Tienda tienda) {
        // Clientes generan pedidos, Gestores procesan, Transportadores reparten.

        Thread generadorClientes = new Thread(() -> {
            for (int i = 0; i < NUM_CLIENTES; i++) {
                ClienteNormal cliente = new ClienteNormal(tienda);
                tienda.getColaClientes().add(cliente);
                logger.log(LogLevel.TRACE, "Ha llegado un cliente a la tienda");
            }
        });
        generadorClientes.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.log(LogLevel.TRACE, "Clientes miran los productos");
        }

        while (!tienda.getColaClientes().isEmpty()) {
            ClienteNormal c = tienda.getColaClientes().poll();
            if (c != null) {
                c.start();
            }
        }
    }
}
