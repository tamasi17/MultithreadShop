package main.v2.modelsv2;

import main.v1.models.Producto;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Clase que gestiona pedidos recibidos con ArrayBlockingQueue y procesados con LinkedlockingBQueue
 */
public class BlockingQueueV2 {

    private int capacidad = 25;
    private BlockingQueue<Producto> recibidos;
    private BlockingQueue<Producto> procesados;

    public BlockingQueueV2(int capacidad) {
        this.capacidad = capacidad;
        this.recibidos = new ArrayBlockingQueue<>(capacidad);
        this.procesados = new LinkedBlockingQueue<>();
    }

    public BlockingQueueV2() {
        this.capacidad = 25;
        this.recibidos = new ArrayBlockingQueue<>(capacidad);
        this.procesados = new LinkedBlockingQueue<>();
    }

}
