package main.java.models;


import main.java.logging.LogLevel;
import main.java.utils.AccesoTransportista;

import static main.java.logging.LoggerProvider.getLogger;

public class Transportista {

    int idTransportista;
    static int contador = 0;
    Thread thread;
    AccesoTransportista tienda;

    public Transportista(AccesoTransportista tienda) {
        this.tienda = tienda;
        this.idTransportista = ++contador;
        getLogger().log(LogLevel.TRACE, "Transportista " + idTransportista + " ha llegado a la tienda");
        this.thread = new Thread(() -> {
            // Mientras que la tienda este abierta o la cola de procesados tenga algun pedido:
            while (tienda.isOpen() || !tienda.isColaProcesadosEmpty()) {
                // Transporta pedido
                tienda.transportarPedido();
            }
            getLogger().info(Thread.currentThread().getName() + " termina su jornada.");
        }, "T"+idTransportista);

    }


    public Thread getThread() {
        return this.thread;
    }
}
