package models;

import log4Mats.LogLevel;
import utils.AccesoTienda;
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

    private int idCliente;
    private static int contador = 100;
    private AccesoTienda tienda;
    private List<Producto> carrito;
    private List<Producto> productosDisponibles;


    public Cliente(AccesoTienda tienda) {
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

    private void elegirArticulos() {

        Random random = new Random();
        // cantidad de articulos normales que se lleva el cliente
        int numArticulos = random.nextInt(3) + 1;

        for (int i = 0; i < numArticulos; i++) {

            int articuloElegido = random.nextInt(productosDisponibles.size()); // elige un producto aleatorio

            // Comprobacion de stock del articuloElegido
            while (productosDisponibles.get(articuloElegido).idProducto < 0) {
                getLogger().log(LogLevel.ERROR,
                        "Product out of stock: " + productosDisponibles.get(articuloElegido).nombre
                                + " > Choosing another.");
                articuloElegido = random.nextInt(productosDisponibles.size());
            }

            // Añadimos articuloElegido al carrito del pedido:
            carrito.add(productosDisponibles.get(articuloElegido));
            getLogger().log(LogLevel.INFO,
                    "Articulo añadido al carrito: " + productosDisponibles.get(articuloElegido).toString());
        }
    }

    @Override
    public String toString() {
        return "Cliente [" + idCliente + "]";
    }
}
