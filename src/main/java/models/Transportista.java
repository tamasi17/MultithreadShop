package models;

import log4Mats.LogLevel;
import utils.AccesoTransportista;

import static logging.LoggerProvider.getLogger;

public class Transportista {

    int idTransportista;
    static int contador = 0;
    Thread thread;
    AccesoTransportista tienda;

    public Transportista(AccesoTransportista tienda) {
        this.tienda = tienda;
        this.idTransportista = ++contador;
        getLogger().log(LogLevel.TRACE, "Transportista "+ idTransportista +" ha llegado a la tienda");
        this.thread = new Thread(tienda::transportarPedido);
    }

    public Thread getThread(){
        return this.thread;
    }
}
