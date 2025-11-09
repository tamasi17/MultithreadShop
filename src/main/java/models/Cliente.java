package models;

import log4Mats.LogLevel;
import utils.ColaPedidosClasica;
import utils.ProductManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static logging.LoggerProvider.getLogger;

/**
 * Define un models.Cliente, extiende Thread.
 * El cliente genera pedidos aleatorios.
 *
 * @author mati
 */

public class Cliente extends Thread {

    int idCliente;
    static int cliente = 100;


    @Override
    public void run() {

        Random random = new Random();
        int numArticulos = random.nextInt(2) + 1; // cantidad de articulos que se lleva el cliente
        List<Producto> productos = ProductManager.getProductos();
        List<Producto> carrito = new ArrayList<>();

        for (int i = 0; i < numArticulos; i++) {
            int articuloElegido = random.nextInt(productos.size()); // elige un producto aleatorio

            // Comprobacion de stock del articuloElegido
            while (productos.get(articuloElegido).idProducto < 0) {
                getLogger().log(LogLevel.ERROR,
                        "Product out of stock: " + productos.get(articuloElegido).nombre + " > Choosing another.");
                articuloElegido = random.nextInt(productos.size());
            }

            // Añadimos articuloElegido al carrito del pedido:
            carrito.add(productos.get(articuloElegido));

        }

        Pedido pedido = new Pedido(carrito);

        ColaPedidosClasica.añadir(pedido);
        System.out.println("Pedido añadido a la cola.");
        getLogger().log(LogLevel.INFO, "Pedido añadido correctamente a la cola");

    }
}
