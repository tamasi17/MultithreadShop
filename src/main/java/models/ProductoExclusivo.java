package main.java.models;

import static main.java.logging.LoggerProvider.getLogger;

/**
 * Clase que define un ProductoExclusivo
 * ProductoTipo de la A a la F
 * Solo una unidad asi que booleans reservado y vendido en vez de un stock.
 */
public class ProductoExclusivo {

    ProductoTipo tipo;
    boolean reservado;
    boolean vendido;

    /**
     * Constructor por tipo de producto A-F
     * Recibe un número entre 0 y 5 de la Tienda
     * @param numero
     */
    public ProductoExclusivo(int numero) {
        this.tipo = ProductoTipo.fromNumero(numero);
        this.reservado = false;
        this.vendido = false;
    }

    public synchronized boolean reservar(ClienteVIP vip) throws InterruptedException {
        if (this.vendido) {
            getLogger().info("Ya no quedan existencias de este producto " + tipo);
            return false;
        }

        // Si el producto no estaba reservado, salta este while y reserva directamente
        long inicio = System.currentTimeMillis();
        while (this.reservado) {
            long transcurrido = System.currentTimeMillis() - inicio;
            long restante = 150 - transcurrido;
            if (restante <= 0) { // si no se ha vendido en timeout, devolvemos falso
                getLogger().info(vip.toString() + ": Liberado producto " + tipo);
                reservado = false;
                notifyAll();
                return false;
            }
            try {
                wait(restante);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        //Si llega aqui, reserva
        reservado = true;
        getLogger().info(vip.toString() + ": Reservado producto "+ tipo);
        // return id del cliente aqui tb?
        return true;

    }

    public synchronized void liberar(ClienteVIP vip){
        reservado = false;
        getLogger().info(vip.toString() + ":Liberado: producto "+ tipo);
        notifyAll();
    }

    public synchronized void confirmarCompra(ClienteVIP vip){
        if(!reservado || vendido) return;
        vendido = true;
        getLogger().info(">>>> "+ vip.toString() + ": comprado producto "+ tipo);
        reservado = false;
        notifyAll();
    }

    public boolean isReservado() {
        return reservado;
    }

    public boolean isVendido() {
        return vendido;
    }

    public ProductoTipo getTipo() {
        return tipo;
    }
}
