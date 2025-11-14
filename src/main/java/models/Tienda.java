package main.java.models;


import main.java.logging.LogLevel;
import main.java.utils.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static main.java.logging.LoggerProvider.getLogger;


/**
 * Clase para hacer el setup de los productos disponibles.
 * Llama a ProductManager para generar la lista de productos.
 */
public class Tienda implements AccesoCliente, AccesoGestor, AccesoTransportista {

    private final ProductManager productManager;
    private final ColaPedidosClasica colaPedidos;
    private final LinkedList<ClienteNormal> colaClientes;
    private boolean isOpen;
    private ProductoExclusivo[] productoExclusivos;

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

        ProductoExclusivo[] p = new ProductoExclusivo[6];
        for (int i = 0; i < 6; i++) {
            p[i] = new ProductoExclusivo(i);
        }
        this.productoExclusivos = p;

        isOpen = true;
        getLogger().log(LogLevel.TRACE,
                "Tienda abierta con "+ productManager.getProductos().size()+ " productos.");

    }


    /** CLIENTES
     * Metodo que envia un pedido a la ColaRecibidos
     * @param carrito
     */
    @Override
    public void enviarPedido(List<Producto> carrito) {
        Pedido pedido = new Pedido(carrito);
        colaPedidos.añadirPedido(pedido);
        getLogger().info("Pedido añadido a ColaRecibidos: "+ pedido.getIdPedido());
    }

    /**CLIENTES VIP
     * Genera un pack de dos ProductosExclusivos aleatorios
     * @return
     */
    public ProductoExclusivo[] generarPackExclusivo(){
        Random random =new Random();
        ProductoExclusivo p1 = productoExclusivos[random.nextInt(6)];
        ProductoExclusivo p2;

        // Otro producto exclusivo diferente
        do {
            p2 = productoExclusivos[random.nextInt(6)];
        } while (p2 == p1);

        return new ProductoExclusivo[]{p1,p2};
    }

    /**
     * Confirma si todos los ProductosExclusivos han sido vendidos
     * @return
     */
    public boolean todosLosExclusivosVendidos() {
        for (ProductoExclusivo p : productoExclusivos) {
            if (!p.isVendido()) return false; // queda alguno por vender
        }
        getLogger().info("Todos los productos exclusivos han sido vendidos!");
        return true;
    }

    /** GESTORES
     * Procesa un pedido de la cola de Recibidos
     * @return Pedido preparado
     */
    public Pedido prepararPedido(){
        getLogger().info("Preparando pedido en Tienda");
        return colaPedidos.procesarPedido();
    }


    public void transportarPedido(){

        Pedido enviado = colaPedidos.transportarProcesado();
        if (enviado == null) {
            getLogger().trace(Thread.currentThread().getName() +
                    " no hay más pedidos para transportar (cola vacía y tienda cerrada).");
            return;
        }
        getLogger().info("Transportando pedido: "+ enviado.getIdPedido());

        try {
            Thread.sleep(70);
        } catch (InterruptedException ie) {
            System.err.println(ie.getLocalizedMessage());

        }
    }

    public void muestraRecibidos(){
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
        // Damos algo de tiempo para que cierren los threads restantes.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        isOpen = false;
        colaPedidos.close();
        getLogger().info(">>>> Tienda cerrada!");
    }
}
