package models;

public enum Categorias {

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
    public static Categorias desdeCsv(String valor) {
        String arreglo = valor.trim().toUpperCase();
        return Categorias.valueOf(arreglo);
    }

}
