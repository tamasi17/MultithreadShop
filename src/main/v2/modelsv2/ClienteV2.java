package main.v2.modelsv2;

import main.v1.models.Cliente;
import main.v1.models.Producto;
import main.v1.utils.AccesoCliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import static main.logging.LoggerProvider.getLogger;

/**
 * Define un ClienteV2, extiende Thread.
 * El cliente genera pedidos aleatorios.
 * Funciona con ArrayBlockingQueue
 * @author mati
 */

public class ClienteV2 extends Thread {

        private int idCliente;
        private static int contador = 0;
        private List<ArticuloV2> carrito;
    private ArrayBlockingQueue<ArticuloV2> cola;


    public ClienteV2(ArrayBlockingQueue<ArticuloV2> colaRecibidos) {
        this.idCliente = ++contador;
        this.carrito = new ArrayList<>();
        this.cola = colaRecibidos;
    }

    @Override
    public void run() {

        elegirArticulos();

        for (ArticuloV2 articulo : carrito) {
            try {
                cola.put(articulo);
                getLogger().info("Cliente ["+idCliente+"] añade articulo a la cola de recibidos");
            } catch (InterruptedException e) {
                getLogger().error("Cliente "+ idCliente + " interrumpido mientras pone articulo en la cola");
            }
        }


    }

    /**
     * Metodo que elige entre 1 y 3 articulos aleatorios, comprobando si hay stock.
     */
    public List<ArticuloV2> elegirArticulos() {

        // Cantidad de articulos normales que se lleva el cliente
        Random random = new Random();
        int numArticulos = random.nextInt(3) + 1;

        for (int i = 0; i < numArticulos; i++) {
            ArticuloV2 elegido = new ArticuloV2(random.nextInt(11)); // elige un producto aleatorio
            getLogger().info("Articulo añadido al carrito: " + elegido.getId());
            carrito.add(elegido);
        }

        return carrito;
    }

    @Override
    public String toString() {
        return "Cliente [" + idCliente + "]";
    }

    public int getID() {
        return idCliente;
    }
}
