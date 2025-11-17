package main.v2.modelsv2;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static main.logging.LoggerProvider.getLogger;

public class VIPv2 implements Runnable {

    private int idVIP;
    private static int contador = 0;
    private ExclusivoV2[] pack;
    private GestorExclusivos gestorExclusivos;

    public VIPv2(GestorExclusivos gestorExclusivos) {
        this.idVIP = ++contador;
        this.gestorExclusivos = gestorExclusivos;
    }

    @Override
    public void run() {

        // Recibimos pack de exclusivos
        pack = gestorExclusivos.generarPack();

        // Orden ascendente
        ExclusivoV2 primero = pack[0].getTipo().getNumero() < pack[1].getTipo().getNumero() ? pack[0] : pack[1];
        ExclusivoV2 segundo = primero == pack[0] ? pack[1] : pack[0];

        getLogger().info("VIP [" + idVIP + "] llega a la tienda");

        primero.getLock().lock();
        try {
            if (primero.reservar(this)) {

                getLogger().info("[" + idVIP + "] ha reservado " + primero.getTipo());

                boolean segundoReservado = false;
                if (segundo.getLock().tryLock(2, TimeUnit.SECONDS)) {

                    try {
                        segundoReservado = segundo.reservar(this);
                        getLogger().info("[" + idVIP + "] ha reservado " + segundo.getTipo());

                    } finally {
                        if (!segundoReservado) segundo.getLock().unlock();
                    }
                }

                if (segundoReservado) {  // funciono la segunda reserva
                    primero.confirmar(this);
                    segundo.confirmar(this);
                } else { // no funciono la reserva del segundo:
                    getLogger().warn("[" + idVIP + "] libera " + primero.getTipo());
                    primero.liberar();
                }


            }
        } catch (InterruptedException e) { // catch del primer try
            getLogger().warn(segundo.getTipo() + " interrumpido mientras espera a reservar");
        } finally {
            primero.getLock().unlock();
            segundo.getLock().unlock();
        }

    }


    public int getIdVIP() {
        return idVIP;
    }

    @Override
    public String toString() {
        return "VIP [" + idVIP + "]";
    }
}
