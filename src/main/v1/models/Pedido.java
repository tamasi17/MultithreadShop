package main.v1.models;

import java.util.List;

/**
 * Define una clase Pedido con los atributos id, producto y estado.
 * Cada pedido representa una operación de compra o envío dentro del sistema.
 *
 * @author mati
 */
public class Pedido {

    int idPedido;
    static int contador = 0;
    List<Producto> carrito;
    Estado estado;
    double costeTotal = 0;

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

    @Override
    public String toString() {
        return "Pedido: "+ idPedido + " [" + estado + "] Total: "+ String.format("%.2f", getCosteTotal()) + " euros.\n"+ carrito;
    }

    public double getCosteTotal() {
        for (Producto p : carrito) {
            costeTotal += p.precio;
        }
        return costeTotal;
    }
}
