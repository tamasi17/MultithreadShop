package utils;

import log4Mats.LogLevel;
import models.Estado;
import models.Pedido;

import java.util.LinkedList;
import java.util.Queue;

import static logging.LoggerProvider.getLogger;

/**
 * Funciona como un almacen sincronizado en base a una Queue de Pedidos.
 *
 * @author mati
 */

public class ColaPedidosClasica {

    private Queue<Pedido> colaRecibidos;
    private Queue<Pedido> colaProcesados;


    public ColaPedidosClasica() {
        this.colaRecibidos = new LinkedList<>();
        this.colaProcesados = new LinkedList<>();
    }

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
            getLogger().log(LogLevel.TRACE, "Añadiendo pedido: " + pedido.getIdPedido());
        }

    }

    synchronized public void procesarPedido() {

        // Si la cola esta vacia, se espera
        if (colaRecibidos.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ie) {
                getLogger().log(LogLevel.ERROR,
                        "Cola vacia, gestor esperando: " + Thread.currentThread().toString());
            }
        }

        // Cambiamos el pedido de estado
        Pedido pedidoEnProceso = colaRecibidos.poll();
        if (pedidoEnProceso != null) {
            pedidoEnProceso.setEstado(Estado.EN_PROCESO);

            try {
                Thread.sleep(80);
            } catch (InterruptedException ie) {
                getLogger().log(LogLevel.TRACE,
                        "Preparando pedido: " + pedidoEnProceso.getIdPedido());
            }
            // Pasamos el pedido a procesados
            colaProcesados.add(pedidoEnProceso);
        }

        notifyAll();
    }

        synchronized public void enviarProcesado(){
            // Si la cola esta vacia, se espera
            if (colaProcesados.isEmpty()) {
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

            notifyAll();

        }

    public Queue<Pedido> getColaRecibidos() {
        return colaRecibidos;
    }

    public Queue<Pedido> getColaProcesados() {
        return colaProcesados;
    }
}
