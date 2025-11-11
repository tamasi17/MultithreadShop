package utils;

import log4Mats.LogLevel;
import models.Estado;
import models.Pedido;
import java.util.LinkedList;
import java.util.Queue;
import static logging.LoggerProvider.getLogger;

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
        if (colaRecibidos.size() >= 25) {
            try {
                wait();
            } catch (InterruptedException ie) {
                getLogger().log(LogLevel.ERROR,
                        "Cola llena, cliente esperando: " + Thread.currentThread().toString());
            }
        }
        colaRecibidos.add(pedido);

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            getLogger().log(LogLevel.TRACE, "Cliente: " + Thread.currentThread().toString() +
                    "\nAñadiendo pedido: " + pedido.getIdPedido());
        }
    }

    /** GESTORES
     * Metodo que procesa un pedido, pasandolo de Recibidos a Procesados.
     * Sleep simulando preparacion del pedido.
     */
    synchronized public Pedido procesarPedido() {
        // Si la cola esta vacia, se espera
        while (colaRecibidos.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ie) {
                getLogger().log(LogLevel.ERROR,
                        "Cola vacia, gestor esperando: " + Thread.currentThread().toString());
            }
        }

        Pedido pedido = moverAProcesados();

        // Si no queda nada en Recibidos y la tienda esta cerrada:
        if (colaRecibidos.isEmpty() && !this.isOpen) return null;

        notifyAll();
        return pedido;
    }

    private Pedido moverAProcesados() {
        // Cambiamos el pedido de estado
        Pedido pedidoEnProceso = colaRecibidos.poll();
        if (pedidoEnProceso != null) {
            pedidoEnProceso.setEstado(Estado.EN_PROCESO);
            // Pasamos el pedido a procesados
            colaProcesados.add(pedidoEnProceso);
        }
        return pedidoEnProceso;
    }

    /** TRANSPORTISTAS
     * Metodo que transporta un pedido procesado, sacandolo de la lista Procesados.
     * Sleep para simular transporte.
     */
    synchronized public void enviarProcesado(){
            // Si la cola esta vacia y la tienda sigue abierta, se espera
            while (colaProcesados.isEmpty() && this.isOpen) {
                try {
                    wait();
                } catch (InterruptedException ie) {
                    getLogger().log(LogLevel.ERROR,
                            "ColaProcesados vacia, transportista esperando: " + Thread.currentThread().toString());
                }
            }

            // Cambiamos el pedido de estado
            Pedido pedidoEnviado = colaRecibidos.poll();
            if (pedidoEnviado != null) {
                pedidoEnviado.setEstado(Estado.ENVIADO);

                try {
                    Thread.sleep(80);
                } catch (InterruptedException ie) {
                    getLogger().log(LogLevel.TRACE,
                            "Preparando pedido: " + pedidoEnviado.getIdPedido());
                }
            }

            // Si no quedan Procesados y la tienda esta cerrada:
            if (colaProcesados.isEmpty() && !this.isOpen) return;

            notifyAll();

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
