package utils;

import models.Pedido;

public interface AccesoGestor {

    Pedido prepararPedido();
    boolean isOpen();
    boolean isColaRecibidosEmpty();
}
