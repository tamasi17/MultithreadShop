package main.v2.modelsv2;

import java.util.concurrent.locks.ReentrantLock;

import static main.logging.LoggerProvider.getLogger;

/**
 * Clase que define un ProductoExclusivo en la v2, gestionado con ReentrantLock
 */
public class ExclusivoV2 {

    private TipoExclusivoV2 tipo;
    private volatile boolean reservado;
    private volatile boolean vendido;
    private final ReentrantLock lock = new ReentrantLock(true);

    public ExclusivoV2(TipoExclusivoV2 tipo) {
        this.tipo = tipo;
        this.reservado = false;
        this.vendido = false;
    }

    public boolean reservar(VIPv2 vip){
        lock.lock();
        try{
            getLogger().trace(vip.toString() + " intentando reservar exclusivo "+ tipo);
            if (vendido || reservado) return false;
            reservado = true;
            getLogger().info(vip.toString() + " reservó exclusivo "+ tipo);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void liberar(){
        lock.lock();
        try{
            reservado = false;
        } finally {
            lock.unlock();
        }
    }

    public boolean confirmar(VIPv2 vip){
        lock.lock();
        try{
            getLogger().trace(vip.toString() + " intentando confirmar compra: "+ tipo);
            if (!reservado || vendido) return false;
            getLogger().trace(">>>>>>>>>>>> " + vip.toString() + " compró "+ tipo);
            vendido = true;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public TipoExclusivoV2 getTipo() {
        return tipo;
    }

    public boolean isReservado() {
        return reservado;
    }

    public void setReservado(boolean reservado) {
        this.reservado = reservado;
    }

    public boolean isVendido() {
        return vendido;
    }

    public void setVendido(boolean vendido) {
        this.vendido = vendido;
    }

    @Override
    public String toString() {
        return "Articulo Exclusivo [" + tipo + "] " +
                "- Reservado: " + reservado +
                " - Vendido: " + vendido;
    }
}
