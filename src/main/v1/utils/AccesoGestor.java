package main.v1.utils;

import main.v1.models.Pedido;

public interface AccesoGestor {

    Pedido prepararPedido();
    boolean isOpen();
    boolean isColaRecibidosEmpty();
}
