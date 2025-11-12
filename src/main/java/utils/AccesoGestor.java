package main.java.utils;

import main.java.models.Pedido;

public interface AccesoGestor {

    Pedido prepararPedido();
    boolean isOpen();
    boolean isColaRecibidosEmpty();
}
