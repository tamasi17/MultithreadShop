package main.v2.modelsv2;

public enum TipoExclusivoV2 {

    A(0), B(1), C(2), D(3), E(4), F(5);

    private final int numero;

    TipoExclusivoV2(int i) {
        this.numero = i;
    }

    public int getNumero() {
        return numero;
    }

    // Para obtener el enum desde un numero, en este caso Random de la tienda.
    public static TipoExclusivoV2 fromNumero(int numero) {
        for (TipoExclusivoV2 producto : values()) {
            if (producto.getNumero() == numero) {
                return producto;
            }
        }
        throw new IllegalArgumentException("Numero no valido: " + numero); // sustituye al return
    }
}
