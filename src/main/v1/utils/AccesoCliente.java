package main.v1.utils;

import main.v1.models.Producto;
import main.v1.models.ProductoExclusivo;

import java.util.List;

public interface AccesoCliente {

    List<Producto> getProductos();
    void enviarPedido(List<Producto> carrito);
    ProductoExclusivo[] generarPackExclusivo();
    boolean todosLosExclusivosVendidos();
}
