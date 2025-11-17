package main.v2.modelsv2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import static main.logging.LoggerProvider.getLogger;

/**
 * Define un Gestor que implementa Runnable.
 * Procesa pedidos.
 * Funciona con ArrayBlockingQueue y LinkedBlockingQueue
 *
 * @author mati
 */

public class GestorV2 implements Runnable {

    private int idGestor;
    private static int contador = 0;
    private final AtomicInteger procesados;
    private final AtomicInteger recibidos;
    private final Semaphore semaforo;
    private ArrayBlockingQueue<ArticuloV2> colaRecibidos;
    private LinkedBlockingQueue<ArticuloV2> colaProcesados;


    public GestorV2(ArrayBlockingQueue<ArticuloV2> colaRecibidos,
                    LinkedBlockingQueue<ArticuloV2> colaProcesados,
                    AtomicInteger recibidos, AtomicInteger procesados,
                    Semaphore semaforo) {
        this.idGestor = ++contador;
        this.recibidos = recibidos;
        this.procesados = procesados;
        this.semaforo = semaforo;
        this.colaRecibidos = colaRecibidos;
        this.colaProcesados = colaProcesados;
    }

    @Override
    public void run() {

        while (true) { // continuamente
            try {
                ArticuloV2 articulo = colaRecibidos.take(); // bloquea si esta vacio
                recibidos.incrementAndGet();
                getLogger().info("Gestor ["+idGestor+"] coge articulo de la cola de recibidos");

                semaforo.acquire(); // entramos en semaforo

                try {
                    getLogger().info("Gestor [" + idGestor + "] procesa pedido " + articulo.id);
                    Thread.sleep(200); // procesa articulo
                    colaProcesados.put(articulo); // añade a procesados
                    procesados.incrementAndGet();
                } finally {
                    semaforo.release(); // salimos del semaforo
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break; // salimos del while(true)
            }
        }


    }


    @Override
    public String toString() {
        return "Gestor [" + idGestor + "]";
    }

    public int getID() {
        return idGestor;
    }
}
