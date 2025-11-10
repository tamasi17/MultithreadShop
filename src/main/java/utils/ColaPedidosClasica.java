package utils;

import log4Mats.LogLevel;
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

    static Queue<Pedido> colaPedidos = new LinkedList<>();

    static synchronized public void añadir(Pedido pedido){
        if (colaPedidos.size()>=25){
            try {
                Thread.currentThread().wait();
            } catch (InterruptedException ie) {
                getLogger().log(LogLevel.TRACE, "Cola llena, cliente esperando."); // idCliente aqui?
            }
        }
        colaPedidos.add(pedido);
    }

    synchronized public void retirar(){
        if (colaPedidos.isEmpty()){
            try {
                Thread.currentThread().wait();
            } catch (InterruptedException ie) {
                getLogger().log(LogLevel.TRACE, "Cola vacia, transportista esperando."); // idTransportista aqui?
            }
        }
        colaPedidos.poll();
    }


    public static void muestraPedidos(){
        for (Pedido p : colaPedidos) {
            System.out.println(p.toString());
        }
    }



}
