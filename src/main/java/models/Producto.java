package main.java.models;

/**
 * Define un main.java.models.Producto a la venta en la tienda.
 * Modelo para Jackson CSV reader. Schema:
 * idProducto,nombre,marca,categoria,precio,year,stock
 *
 * @author mati
 */

public class Producto {
    int idProducto;
    String nombre;
    String marca;
    Categorias categoria;
    double precio;
    int year;
    int stock;

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Categorias getCategoria() {
        return categoria;
    }

    public void setCategoria(Categorias categoria) {
        this.categoria = categoria;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * toString no incluye stock para ver los pedidos mejor
     * @return
     */
    @Override
    public String toString() {
        return "("+idProducto + ") [" + categoria + "] "
                + nombre + " - " + marca +", "+ year +": "+ precio + " euros.";
    }
}
