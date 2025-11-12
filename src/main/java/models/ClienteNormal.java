package main.java.models;

import main.java.logging.LogLevel;
import main.java.utils.AccesoCliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static main.java.logging.LoggerProvider.getLogger;

/**
 * Define un Cliente, extiende Thread.
 * El cliente genera pedidos aleatorios.
 *
 * @author mati
 */

public class ClienteNormal extends Thread implements Cliente {

    private int idCliente;
    private static int contador = 0;
    private final AccesoCliente tienda;
    private List<Producto> carrito;
    private final List<Producto> productosDisponibles;


    public ClienteNormal(AccesoCliente tienda) {
        this.tienda = tienda;
        this.idCliente = ++contador;
        this.carrito = new ArrayList<>();
        this.productosDisponibles = tienda.getProductos();
    }

    @Override
    public void run() {

        elegirArticulos();
        tienda.enviarPedido(carrito);

    }

    /**
     * Metodo que elige entre 1 y 3 articulos aleatorios, comprobando si hay stock.
     */
    public void elegirArticulos() {

        // Cantidad de articulos normales que se lleva el cliente
        Random random = new Random();
        int numArticulos = random.nextInt(3) + 1;

        for (int i = 0; i < numArticulos; i++) {

            int articuloElegido = random.nextInt(productosDisponibles.size()); // elige un producto aleatorio

            // Comprobacion de stock del articuloElegido
            while (productosDisponibles.get(articuloElegido).idProducto < 0) {
                getLogger().error("Product out of stock: " + productosDisponibles.get(articuloElegido).nombre
                                + " > Choosing another.");
                articuloElegido = random.nextInt(productosDisponibles.size());
            }

            // Añadimos articuloElegido al carrito del pedido:
            carrito.add(productosDisponibles.get(articuloElegido));
            getLogger().info("Articulo añadido al carrito: " + productosDisponibles.get(articuloElegido).toString());
        }
    }

    @Override
    public String toString() {
        return "Cliente [" + idCliente + "]";
    }

    public int getID() {
        return idCliente;
    }
}
