package main.v2;

import main.logging.Logger;
import main.logging.LoggerProvider;
import main.v2.modelsv2.ArticuloV2;
import main.v2.modelsv2.ClienteV2;
import main.v2.modelsv2.GestorV2;
import main.v2.modelsv2.TransportistaV2;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static main.logging.LoggerProvider.getLogger;

public class Mainv2 {

    public static final Logger LOGGER = LoggerProvider.getLogger();

    public static final ArrayBlockingQueue<ArticuloV2> COLA_RECIBIDOS = new ArrayBlockingQueue<>(25);
    public static final LinkedBlockingQueue<ArticuloV2> COLA_PROCESADOS = new LinkedBlockingQueue<>();
    public static final Semaphore semaforo = new Semaphore(6, true);
    public static final AtomicInteger ATOMIC_RECIBIDOS = new AtomicInteger(0);
    public static final AtomicInteger ATOMIC_PROCESADOS = new AtomicInteger(0);


    static void main() {

        try (ExecutorService poolClientes = Executors.newCachedThreadPool();
             ExecutorService poolGestores = Executors.newFixedThreadPool(10);
             ExecutorService poolTransportistas = Executors.newVirtualThreadPerTaskExecutor();) {

            for (int i = 0; i < 20; i++) {
                poolClientes.submit(new ClienteV2(COLA_RECIBIDOS));
            }

            for (int i = 0; i < 15; i++) {
                poolGestores.submit(new GestorV2(
                        COLA_RECIBIDOS, COLA_PROCESADOS,
                        ATOMIC_RECIBIDOS, ATOMIC_PROCESADOS,
                        semaforo));
            }

            for (int i = 0; i < 12; i++) {
                poolTransportistas.submit(new TransportistaV2(COLA_PROCESADOS));
            }


            Thread.sleep(1000);

            poolClientes.shutdownNow();

            // Un poco más de tiempo para trabajadores
            // (sin esto no suelen llegar si hay mas hilos cliente)
            Thread.sleep(300);

            poolGestores.shutdownNow();
            poolTransportistas.shutdownNow();

            if (!poolClientes.awaitTermination(15, TimeUnit.SECONDS) ||
                    !poolGestores.awaitTermination(15, TimeUnit.SECONDS) ||
                    !poolTransportistas.awaitTermination(15, TimeUnit.SECONDS)) {

                LOGGER.warn("Algunos hilos no terminaron correctamente.");
            }


            getLogger().info("Tienda cerrada. " +
                    "\nRecibidos: " + ATOMIC_RECIBIDOS.get() +
                    "\nProcesados: " + ATOMIC_PROCESADOS.get());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // levantamos flag
            LOGGER.error("Hilo principal interrumpido");
        }


    }

}
