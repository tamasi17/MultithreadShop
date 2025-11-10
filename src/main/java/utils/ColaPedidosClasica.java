package utils;

import models.Pedido;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Funciona como un almacen sincronizado en base a una Queue de Pedidos.
 *
 * @author mati
 */

public class ColaPedidosClasica {

    static Queue<Pedido> colaPedidos = new LinkedList<>();

    static synchronized public void añadir(Pedido pedido){
        colaPedidos.add(pedido);
    }

    synchronized public void quitar(){
        colaPedidos.poll();
    }

    public static void muestraPedidos(){
        for (Pedido p : colaPedidos) {
            System.out.println(p.toString());
        }
    }


}
