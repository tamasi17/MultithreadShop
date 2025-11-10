package models;

import utils.AccesoTransportista;

public class Transportistas {

    Thread transportista;
    AccesoTransportista tienda;

    public Transportistas(AccesoTransportista tienda) {
        this.tienda = tienda;
        this.transportista = new Thread(tienda::transportarPedido);
        transportista.start();
    }
}
