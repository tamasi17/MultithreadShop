package utils;

import models.Pedido;
import models.Producto;

import java.util.List;

public interface AccesoTienda {
    List<Producto> getProductos();
    void enviarPedido(Pedido pedido);
}
