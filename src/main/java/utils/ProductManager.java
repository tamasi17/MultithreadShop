package utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    List<Producto> productos;


    public ProductManager() {
        this.productos = new ArrayList<>();
    }

    /**
     * Carga una lista de Productos desde un fichero CSV
     * @param csv
     * @return List<Producto>
     */
    public List<Producto> loadFromCsv(File csv) {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader(); // usa la primera linea como schema

        if (!csv.exists()) getLogger().log(LogLevel.ERROR, "CSV file does not exist");

        try {
        // Iterador, clase Producto como modelo, siguiendo el Schema marcado en la primera linea.
        // Lee de un archivo csv, devuelve una lista de productos.
        MappingIterator<Producto> iterator = mapper.readerFor(Producto.class)
                .with(schema)
                .readValues(csv);

            productos = iterator.readAll();
        } catch (IOException ioe) {
            getLogger().log(LogLevel.ERROR, "Error al cargar archivos CSV");
        }

        // throws AssertionError si lo activamos
        assert productos != null;
        productos = Collections.synchronizedList(productos);
        getLogger().log(LogLevel.INFO, "Lista de productos cargada.");

        return productos;
    }

    /**
     *
     * @return productos list
     */
    public List<Producto> getProductos() {
        return productos;
    }

    // getStockStatus() ?
}
