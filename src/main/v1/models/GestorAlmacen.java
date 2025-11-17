package main.v1.models;

import main.v1.utils.AccesoGestor;

import static main.logging.LoggerProvider.getLogger;


/**
 * Clase que define un gestor de almacen, implementa Runnable.
 * El gestor procesa los pedidos de la utils.ColaPedidos.
 *
 * @author mati
 */
public class GestorAlmacen implements Runnable {

    int idGestor;
    static int contador = 0;
    private final AccesoGestor tienda;

    public GestorAlmacen(AccesoGestor tienda) {
        this.tienda = tienda;
        this.idGestor = ++contador;
    }

    @Override
    public void run() {

        while (tienda.isOpen() && !tienda.isColaRecibidosEmpty()) {
            getLogger().info("Gestor "+ idGestor +" procesando pedidos.");

            Pedido pedido = tienda.prepararPedido();

            // Si no quedan pedidos o se ha interrumpido
            if (pedido == null){
                getLogger().info("Gestor "+ idGestor +" ha finalizado su jornada");
                break;
            }

        }



    }


}
