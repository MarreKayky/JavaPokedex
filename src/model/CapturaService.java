package model;

import java.util.Random;

public class CapturaService {
    private Random random = new Random();

    public boolean tentarCapturar(Pokemon pokemon, String tipoBola) {
        if (tipoBola.equals("Master")) return true; // Master Ball é 100%

        double chanceBase = 10.0; // 10% base
        double multiplicadorBola = 1.0;

        if (tipoBola.equals("Great")) multiplicadorBola = 1.5;
        else if (tipoBola.equals("Ultra")) multiplicadorBola = 2.0;

        double multiplicadorRaridade = 1.0;
        if (pokemon.getRaridade().equals("Lendário")) multiplicadorRaridade = 0.1;
        else if (pokemon.getRaridade().equals("Mítico")) multiplicadorRaridade = 0.05;

        double chanceFinal = chanceBase * multiplicadorBola * multiplicadorRaridade;
        return (random.nextDouble() * 100) <= chanceFinal;
    }
}