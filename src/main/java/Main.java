import log4Mats.LogLevel;
import log4Mats.Logger;
import logging.LoggerProvider;
import models.Cliente;
import models.Tienda;
import utils.ColaPedidosClasica;

public class Main {

        static Logger logger = LoggerProvider.getLogger();


    static void main() {
        // Logger pasa informacion por consola
        logger.setLogToConsole(true);

        // Abrimos tienda
        Tienda tienda = new Tienda();

        // Clientes generan pedidos, Gestores procesan, Transportadores reparten.
        Cliente c1 = new Cliente(tienda);
        c1.start();

        try {
            c1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ColaPedidosClasica.muestraPedidos();

    }
}
