package main.v2.modelsv2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static main.logging.LoggerProvider.getLogger;

/**
 * Define un Transportista hecho con CachedThreadPool o hilos virtuales.
 * Envia pedidos.
 * Funciona con LinkedBlockingQueue
 *
 * @author mati
 */

public class TransportistaV2 implements Runnable {

    private int idTransportista;
    private static int contador = 0;
    private LinkedBlockingQueue<ArticuloV2> procesados;


    public TransportistaV2(LinkedBlockingQueue<ArticuloV2> procesados) {
        this.idTransportista = ++contador;
        this.procesados = procesados;
    }

    @Override
    public void run() {

        try {
            while (true) { // continuo

                ArticuloV2 articulo = procesados.take(); // bloquea si esta vacio

                getLogger().info("Transportista ["+ idTransportista +"] procesa pedido " + articulo.id);
                Thread.sleep(400); // envia articulo
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }


    @Override
    public String toString() {
        return "Transportista [" + idTransportista + "]";
    }

    public int getID() {
        return idTransportista;
    }
}
