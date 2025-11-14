package main.java.models;

import main.java.utils.AccesoCliente;

import static main.java.logging.LoggerProvider.getLogger;

/**
 * Define un Cliente, extiende Thread.
 * El cliente genera pedidos aleatorios.
 *
 * @author mati
 */

public class ClienteVIP extends Thread implements Cliente {

    private int idCliente;
    private static int contador = 0;
    private final AccesoCliente tienda;


    public ClienteVIP(AccesoCliente tienda) {
        this.tienda = tienda;
        this.idCliente = ++contador;
    }

    @Override
    public void run() {
        getLogger().trace("VIP " + idCliente + " entra a la tienda");
        elegirArticulos();
    }

    /**
     * Metodo que elige entre 1 y 3 articulos aleatorios, comprobando si hay stock.
     */
    public void elegirArticulos() {

        int intentos = 0;

        // Si quedan ProductosExclusivos
        while (!tienda.todosLosExclusivosVendidos()) {

            ProductoExclusivo[] pack = tienda.generarPackExclusivo();
            ProductoExclusivo p1 = pack[0];
            ProductoExclusivo p2 = pack[1];

            boolean reservado1 = false;
            boolean reservado2 = false;


            getLogger().trace("VIP " + idCliente + " intentando reservar primer articulo");
            if (!p1.isVendido()) { // solo optimiza, comprobacion real es atomica dentro de reservar()
                try {
                    reservado1 = p1.reservar(this);
                    Thread.sleep(30);
                } catch (InterruptedException ie) {
                    getLogger().error("Cliente " + idCliente + " interrumpido reservando primer articulo");
                    throw new RuntimeException(ie);
                }
            }

            // Si reservado1 funciona, reservamos el segundo
            getLogger().trace("VIP " + idCliente + " intentando reservar segundo articulo");
            try {
                if (reservado1) reservado2 = p2.reservar(this);
                Thread.sleep(40);
            } catch (InterruptedException ie) {
                getLogger().error("Cliente " + idCliente + " interrumpido reservando segundo articulo");
                throw new RuntimeException(ie);
            }


            // Falló reservado2
            if (reservado1 && !reservado2) {
                p1.liberar(this);
                continue;
            }

            // Se confirma la compra de los ProductosExclusivos
            if (reservado1 && reservado2) {
                p1.confirmarCompra(this);
                p2.confirmarCompra(this);
                getLogger().trace("VIP " + idCliente + " se va de la tienda.");
                return;
            }

            intentos++;

            if (intentos >= 4) {
                getLogger().info("VIP " + idCliente + ": tras 4 intentos, intento otro pack nuevo");
                intentos = 0;
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    // ignoramos interrupcion
                }
            }


        } // fin while exclusivosVendidos()


    }

    @Override
    public String toString() {
        return "VIP [" + idCliente + "]";
    }
}

