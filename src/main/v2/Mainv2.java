package main.v2;

import main.logging.Logger;
import main.logging.LoggerProvider;
import main.v2.modelsv2.*;

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
    public static final GestorExclusivos GESTOR_EXCLUSIVOS = new GestorExclusivos();


    static void main() {

        // Si no quieres que salga la info por consola: setLogToConsole(false) !!
        LOGGER.setLogToConsole(true);

        try (ExecutorService poolClientes = Executors.newCachedThreadPool();
             ScheduledExecutorService poolVIPs = Executors.newScheduledThreadPool(10);
             ExecutorService poolGestores = Executors.newFixedThreadPool(10);
             ExecutorService poolTransportistas = Executors.newVirtualThreadPerTaskExecutor();) {

            // CLIENTES
            for (int i = 0; i < 20; i++) {
                poolClientes.submit(new ClienteV2(COLA_RECIBIDOS));
            }

            // VIPs
            for (int i = 0; i < 20; i++) {
                poolVIPs.submit(new VIPv2(GESTOR_EXCLUSIVOS));
            }

            // GESTORES
            for (int i = 0; i < 15; i++) {
                poolGestores.submit(new GestorV2(
                        COLA_RECIBIDOS, COLA_PROCESADOS,
                        ATOMIC_RECIBIDOS, ATOMIC_PROCESADOS,
                        semaforo));
            }

            // TRANSPORTISTAS
            for (int i = 0; i < 12; i++) {
                poolTransportistas.submit(new TransportistaV2(COLA_PROCESADOS));
            }

            // Esperamos
            Thread.sleep(1000);

            // Cerramos pool de clientes
            poolClientes.shutdownNow();
            poolVIPs.shutdownNow();

            // Un poco más de tiempo para trabajadores
            // (sin esto no suelen llegar si hay mas hilos cliente)
            Thread.sleep(300);

            // Cerramos pool de trabajadores
            poolGestores.shutdownNow();
            poolTransportistas.shutdownNow();

            if (!poolClientes.awaitTermination(15, TimeUnit.SECONDS) ||
                    !poolGestores.awaitTermination(15, TimeUnit.SECONDS) ||
                    !poolTransportistas.awaitTermination(15, TimeUnit.SECONDS)) {

                LOGGER.warn("Algunos hilos no terminaron correctamente.");
            }

            // Confirmamos valores de los atomic integers
            getLogger().info("Tienda cerrada. " +
                    "\nRecibidos: " + ATOMIC_RECIBIDOS.get() +
                    "\nProcesados: " + ATOMIC_PROCESADOS.get());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // levantamos flag
            LOGGER.error("Hilo principal interrumpido");
        }


    }

}
