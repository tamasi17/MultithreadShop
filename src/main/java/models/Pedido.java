package models;

import java.util.List;

public class Pedido {
    /**
     * Define una clase models.Pedido con los atributos id, producto y estado.
     * Cada pedido representa una operación de compra o envío dentro del sistema.
     *
     * @author mati
     */

    int idPedido;
    static int contador = 0;
    List<Producto> carrito;
    Estado estado;

    /**
     * Constructor comienza siempre en estado PENDIENTE.
     * Cada pedido tiene su propio ID.
     * @param carrito
     */
    public Pedido(List<Producto> carrito) {
        this.carrito = carrito;
        this.estado = Estado.PENDIENTE;
        this.idPedido = ++contador;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public List<Producto> getCarrito() {
        return carrito;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setCarrito(List<Producto> carrito) {
        this.carrito = carrito;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
