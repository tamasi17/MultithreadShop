package models;

import log4Mats.LogLevel;
import utils.AccesoTransportista;

import static logging.LoggerProvider.getLogger;

public class Transportista {

    Thread thread;
    AccesoTransportista tienda;

    public Transportista(AccesoTransportista tienda) {
        this.tienda = tienda;
        getLogger().log(LogLevel.TRACE, "Transportista ha llegado a la tienda");
        this.thread = new Thread(tienda::transportarPedido);
    }

    public Thread getThread(){
        return this.thread;
    }
}
