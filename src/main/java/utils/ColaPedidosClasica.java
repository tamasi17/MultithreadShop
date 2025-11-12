package main.java.utils;



import main.java.models.Estado;
import main.java.logging.LogLevel;
import main.java.models.Pedido;

import java.util.LinkedList;
import java.util.Queue;
import static main.java.logging.LoggerProvider.getLogger;

/**
 * Funciona como un almacen sincronizado en base a dos Queues de Pedidos: Recibidos y Procesados.
 *
 * @author mati
 */

public class ColaPedidosClasica {

    private Queue<Pedido> colaRecibidos;
    private Queue<Pedido> colaProcesados;
    private boolean isOpen;


    public ColaPedidosClasica() {
        this.colaRecibidos = new LinkedList<>();
        this.colaProcesados = new LinkedList<>();
        isOpen = true;
    }


    /**  CLIENTES
     * Metodo que añade un pedido a la cola de Recibidos. Max 25 simultaneamente.
     * Sleep simulando añadir pedido.
     * @param pedido
     */
    synchronized public void añadirPedido(Pedido pedido) {
        while (colaRecibidos.size() >= 25) {
            try {
                wait();
            } catch (InterruptedException ie) {
                getLogger().error("Cola llena, cliente esperando: " + Thread.currentThread().toString());
            }
        }
        colaRecibidos.add(pedido);

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            getLogger().trace("Cliente: " + Thread.currentThread().toString() +
                    "\nAñadiendo pedido: " + pedido.getIdPedido());
        }

        notifyAll();
    }

    /** GESTORES
     * Metodo que procesa un pedido, pasandolo de Recibidos a Procesados.
     * Sleep simulando preparacion del pedido.
     */
    synchronized public Pedido procesarPedido() {
        // Si la cola esta vacia, se espera
        while (colaRecibidos.isEmpty()) {

            if (!this.isOpen) {
                // Si esta cerrada, gestor para.
                getLogger().trace("Gestor para, la tienda esta cerrando: " + Thread.currentThread().getName());
                return null;
            }

            try {
                getLogger().trace(Thread.currentThread().getName() +" esperando a procesar pedido");
                wait();
            } catch (InterruptedException ie) {
                getLogger().error(Thread.currentThread().getName() + " interrumpido mientras espera");
            }
        }

        // Cambiamos el estado del pedido a EN_PROCESO
        Pedido pedidoEnProceso = colaRecibidos.poll();
        if (pedidoEnProceso != null) {
            pedidoEnProceso.setEstado(Estado.EN_PROCESO);
            // Pasamos el pedido a procesados
            colaProcesados.add(pedidoEnProceso);
            getLogger().info("Pedido "+ pedidoEnProceso.getIdPedido() +" pasa a ColaProcesados");
        }

        // Si no queda nada en Recibidos y la tienda esta cerrada:
        if (colaRecibidos.isEmpty() && !this.isOpen) return null;

        notifyAll();
        return pedidoEnProceso;
    }


    /** TRANSPORTISTAS
     * Metodo que transporta un pedido procesado, sacandolo de la lista Procesados.
     * Sleep para simular transporte.
     */
    synchronized public Pedido transportarProcesado(){
            // Si la cola esta vacia y la tienda sigue abierta, se espera
            while (colaProcesados.isEmpty() && this.isOpen) {
                try {
                    wait();
                } catch (InterruptedException ie) {
                    getLogger().error("ColaProcesados vacia, transportista esperando: " + Thread.currentThread().getName());
                }
            }

            // Cambiamos el pedido de estado
            Pedido pedidoEnviado = colaProcesados.poll();
            if (pedidoEnviado != null) {
                pedidoEnviado.setEstado(Estado.ENVIADO);
                getLogger().info("Pedido "+ pedidoEnviado.getIdPedido() +" enviado correctamente.");
                try {
                    Thread.sleep(80);
                } catch (InterruptedException ie) {
                    getLogger().trace("Preparando pedido: " + pedidoEnviado.getIdPedido());
                }
            }

            // Si no quedan Procesados y la tienda esta cerrada:
            if (colaProcesados.isEmpty() && !this.isOpen) return null;

            notifyAll();
            return pedidoEnviado;
        }

    public Queue<Pedido> getColaRecibidos() {
        return colaRecibidos;
    }

    public Queue<Pedido> getColaProcesados() {
        return colaProcesados;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void close() {
        isOpen = false;
        synchronized (this){
        notifyAll();
        }
    }
}
