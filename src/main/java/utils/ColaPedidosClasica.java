package utils;

import models.Pedido;

import java.util.Queue;

/**
 * Funciona como un almacen sincronizado en base a una Queue de Pedidos.
 *
 * @author mati
 */

public class ColaPedidosClasica {

    static Queue<Pedido> colaPedidos;

    static synchronized public void añadir(Pedido pedido){

    }

    synchronized public void quitar(Pedido pedido){

    }



}
