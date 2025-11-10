package models;

public enum Categoria {

    ORDENADOR,
    ACCESORIO,
    ALMACENAMIENTO,
    AUDIO,
    MOVIL,
    REDES,
    MONITOR;

    /**
     * Convierte desde el CSV a las categorias.
     * @param valor
     * @return
     */
    public static Categoria desdeCsv(String valor) {
        String arreglo = valor.trim().toUpperCase();
        return Categoria.valueOf(arreglo);
    }

}
