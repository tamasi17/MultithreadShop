package utils;

import models.Producto;

import java.util.List;

public interface AccesoCliente {

    List<Producto> getProductos();
    void enviarPedido(List<Producto> carrito);
}
