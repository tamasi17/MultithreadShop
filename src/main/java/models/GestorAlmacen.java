package models;

import log4Mats.LogLevel;
import utils.AccesoCliente;
import utils.AccesoGestor;

import static logging.LoggerProvider.getLogger;

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

        while (true) {
            getLogger().log(LogLevel.INFO,
                    "Gestor "+ Thread.currentThread().toString() +" preparando pedidos.");
            tienda.prepararPedido();
            tienda.marcarParaEnvio();
        }


    }


}
