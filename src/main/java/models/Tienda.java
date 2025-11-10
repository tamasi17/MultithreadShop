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

    public Tienda(){
        productManager = new ProductManager();
        productManager.loadFromCsv(new File("src/main/resources/productos_tienda_tech.csv"));
        colaPedidos = new ColaPedidosClasica();
        colaClientes = new LinkedList<>();

        getLogger().log(LogLevel.TRACE,
                "Tienda abierta con "+ productManager.getProductos().size()+ " productos.");

    }


    @Override
    public List<Producto> getProductos() { return productManager.getProductos(); }

    @Override
    public void enviarPedido(List<Producto> carrito) {
        Pedido pedido = new Pedido(carrito);
        colaPedidos.añadirPedido(pedido);
        getLogger().log(LogLevel.INFO, "Pedido añadido a la cola: "+ pedido.getIdPedido());
    }

    public void prepararPedido(){

        if ( colaPedidos.getColaPedidos().peek() != null){
        Pedido pedido = colaPedidos.getColaPedidos().peek();

        try {
            Thread.sleep(60);
        } catch (InterruptedException ie) {
            System.err.println(ie.getLocalizedMessage());
            getLogger().log(LogLevel.INFO, "Preparando pedido: "+ pedido.idPedido);
        }

        pedido.setEstado(Estado.EN_PROCESO);

        }
    }

    public void marcarParaEnvio(){
        colaPedidos.procesarPedido();
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
        for (Pedido p : colaPedidos.getColaPedidos()) {
            System.out.println(p.toString());
        }
    }

    public LinkedList<ClienteNormal> getColaClientes() {
        return colaClientes;
    }
}
