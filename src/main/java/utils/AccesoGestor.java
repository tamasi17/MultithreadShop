package utils;

import models.Pedido;
import models.Producto;

import java.util.List;

public interface AccesoGestor {

    void prepararPedido();
    void marcarParaEnvio();

}
