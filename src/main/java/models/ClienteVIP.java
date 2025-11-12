package main.java.models;

import main.java.utils.AccesoCliente;

import java.util.Random;

/**
 * Define un Cliente, extiende Thread.
 * El cliente genera pedidos aleatorios.
 *
 * @author mati
 */

public class ClienteVIP extends Thread implements Cliente {

    private int idCliente;
    private static int contador = 0;
    private final AccesoCliente tienda;



    public ClienteVIP(AccesoCliente tienda) {
        this.tienda = tienda;
        this.idCliente = ++contador;
    }

    @Override
    public void run() {

        elegirArticulos();
        //tienda.enviarPedidoExclusivo();

    }

    /**
     * Metodo que elige entre 1 y 3 articulos aleatorios, comprobando si hay stock.
     */
    public void elegirArticulos() {

        // Paquete de dos productos exclusivos (A-F)
        Random random = new Random();
        int numArticulos = random.nextInt(3) + 1;

    }

    @Override
    public String toString() {
        return "Cliente [" + idCliente + "]";
    }
}

