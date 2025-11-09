package utils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import static logging.LoggerProvider.getLogger;

import log4Mats.LogLevel;
import models.Producto;


/**
 * Clase que convierte la informacion de un fichero CSV en objetos java (o lista de objetos)
 * La lista productos es static, solo hay una para toda la tienda.
 * Potencialmente: instance-based para tener listas para diferentes tiendas.
 *
 * @author mati
 */
public class ProductManager {

    static List<Producto> productos;

    /**
     * Carga una lista de Productos desde un fichero CSV
     * @param csv
     * @return List<Producto>
     */
    public static List<Producto> loadFromCsv(File csv) {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader(); // usa la primera linea como schema
        List<Producto> productos = null;

        try {
        // Iterador, clase Producto como modelo, siguiendo el Schema marcado en la primera linea.
        // Lee de un archivo csv, devuelve una lista de productos.
        MappingIterator<Producto> iterator = mapper.readerFor(Producto.class)
                .with(schema)
                .readValues(csv);

            productos = iterator.readAll();
        } catch (IOException ioe) {
            System.err.println("Error al cargar archivos desde CSV: "+ ioe.getLocalizedMessage());
            getLogger().log(LogLevel.ERROR, "Error al cargar archivos CSV");
        }

        // throws AssertionError si lo activamos
        assert productos != null;
        productos = Collections.synchronizedList(productos);

        return productos;
    }

    /**
     *
     * @return productos list
     */
    public static List<Producto> getProductos() {
        return productos;
    }

    // getStockStatus() ?
}
