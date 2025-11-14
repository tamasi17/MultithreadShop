package main.java.utils;

import main.java.models.Producto;
import main.java.models.ProductoExclusivo;

import java.util.List;

public interface AccesoCliente {

    List<Producto> getProductos();
    void enviarPedido(List<Producto> carrito);
    ProductoExclusivo[] generarPackExclusivo();
    boolean todosLosExclusivosVendidos();
}
