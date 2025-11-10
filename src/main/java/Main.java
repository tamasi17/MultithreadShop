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

        Cliente cliente = new Cliente(tienda);
        cliente.start();

        ColaPedidosClasica.muestraPedidos();

    }
}
