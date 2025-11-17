package main.v2.modelsv2;

import main.v1.models.ProductoExclusivo;

import java.util.Random;

public class GestorExclusivos {

    private ExclusivoV2[] exclusivos;

    public GestorExclusivos() {

        ExclusivoV2[] e = new ExclusivoV2[6];
        for (int i = 0; i < 6; i++) {
            e[i] = new ExclusivoV2(TipoExclusivoV2.fromNumero(i));
        }
        this.exclusivos = e;
    }

    public ExclusivoV2[] generarPack() {

        ExclusivoV2[] pack = new ExclusivoV2[2];
        Random random = new Random();
        pack[0] = exclusivos[random.nextInt(exclusivos.length)];

        do {
            pack[1] = exclusivos[random.nextInt(exclusivos.length)];
        } while (pack[0] == pack[1]);

        return pack;
    }
}
