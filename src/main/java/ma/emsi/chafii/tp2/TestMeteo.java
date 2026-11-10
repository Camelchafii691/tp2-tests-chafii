package ma.emsi.chafii.tp2;

import ma.emsi.chafii.tp2.outil.meteo.MeteoTool;

public class TestMeteo {
    public static void main(String[] args) {
        double seuil = args.length > 0 ? Double.parseDouble(args[0]) : 25.0;
        var outil = new MeteoTool(seuil);
        System.out.println("Paris, aujourd’hui pour 3 jours : " + outil.isPluiePrevue(48.85, 2.35, 0, 3));
        System.out.println("Paris, demain pour 3 jours : " + outil.isPluiePrevue(48.85, 2.35, 1, 3));
        try {
            outil.isPluiePrevue(48.85, 2.35, 6, 3);
            throw new AssertionError("Une période hors limite doit être refusée.");
        } catch (IllegalArgumentException attendu) {
            System.out.println("Période hors limite correctement refusée : " + attendu.getMessage());
        }
    }
}
