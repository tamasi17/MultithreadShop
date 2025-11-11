package models;

import log4Mats.LogLevel;
import utils.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static logging.LoggerProvider.getLogger;

/**
 * Clase para hacer el setup de los productos disponibles.
 * Llama a ProductManager para generar la lista de productos.
 */
public class Tienda implements AccesoCliente, AccesoGestor, AccesoTransportista {

    private final ProductManager productManager;
    private final ColaPedidosClasica colaPedidos;
    private final LinkedList<ClienteNormal> colaClientes;
    private boolean isOpen;

    /**
     * Constructor de Tienda
     * Genera un productManager que carga productos de un CSV.
     * Inicializa la cola de Pedidos (Recibidos y Procesados)
     * Inicializa la cola de Clientes.
     */
    public Tienda(){
        productManager = new ProductManager();
        productManager.loadFromCsv(new File("src/main/resources/productos_tienda_tech.csv"));
        colaPedidos = new ColaPedidosClasica();
        colaClientes = new LinkedList<>();
        isOpen = true;
        getLogger().log(LogLevel.TRACE,
                "Tienda abierta con "+ productManager.getProductos().size()+ " productos.");

    }




    @Override
    public void enviarPedido(List<Producto> carrito) {
        Pedido pedido = new Pedido(carrito);
        colaPedidos.añadirPedido(pedido);
        getLogger().log(LogLevel.INFO, "Pedido añadido a ColaRecibidos: "+ pedido.getIdPedido());
    }

    public Pedido prepararPedido(){
        return colaPedidos.procesarPedido();
    }


    public void transportarPedido(){

        try {
            Thread.sleep(70);
        } catch (InterruptedException ie) {
            System.err.println(ie.getLocalizedMessage());
            getLogger().log(LogLevel.INFO, "Transportando pedido: "+" IDPEDIDO AQUI");
        }
    }

    public void muestraPedidos(){
        System.out.println("========================");
        for (Pedido p : colaPedidos.getColaRecibidos()) {
            System.out.println(p.toString());
        }
    }

    @Override
    public List<Producto> getProductos() { return productManager.getProductos(); }

    public LinkedList<ClienteNormal> getColaClientes() {
        return colaClientes;
    }

    public boolean isColaProcesadosEmpty(){
        return colaPedidos.getColaProcesados().isEmpty();
    }

    @Override
    public boolean isColaRecibidosEmpty() {
        return colaPedidos.getColaRecibidos().isEmpty();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void close(){
        isOpen = false;
        colaPedidos.close();

        // Damos algo de tiempo para que cierren los threads restantes.
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
