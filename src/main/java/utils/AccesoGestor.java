package utils;

public interface AccesoGestor {

    void prepararPedido();
    void marcarParaEnvio();
    boolean isOpen();
    boolean isColaRecibidosEmpty();
}
