package models;

import log4Mats.LogLevel;
import utils.AccesoTienda;
import utils.ProductManager;

import java.io.File;
import java.util.List;

import static logging.LoggerProvider.getLogger;

/**
 * Clase para hacer el setup de los productos disponibles.
 * Llama a ProductManager para generar la lista de productos.
 */
public class Tienda implements AccesoTienda {

    private ProductManager productManager;

    public Tienda(){
        productManager = new ProductManager();
        productManager.loadFromCsv(new File("src/main/resources/productos_tienda_tech.csv"));

        getLogger().log(LogLevel.TRACE,
                "Tienda abierta con "+ productManager.getProductos().size()+ " productos.");
    }

    @Override
    public List<Producto> getProductos() { return productManager.getProductos(); }

    @Override
    public void enviarPedido(Pedido pedido) {

    }
}
