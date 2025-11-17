package main.v1.models;

public enum ProductoTipo {

    A(0), B(1), C(2), D(3), E(4), F(5);

    private final int numero;

    ProductoTipo(int i) {
        this.numero = i;
    }

    public int getNumero() {
        return numero;
    }

    // Para obtener el enum desde un numero, en este caso Random de la tienda.
    public static ProductoTipo fromNumero(int numero) {
        for (ProductoTipo producto : values()) {
            if (producto.getNumero() == numero) {
                return producto;
            }
        }
        throw new IllegalArgumentException("Numero no valido: " + numero); // sustituye al return
    }
}
