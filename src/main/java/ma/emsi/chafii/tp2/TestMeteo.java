package ma.emsi.chafii.tp2;

import ma.emsi.chafii.tp2.outil.meteo.MeteoTool;

public class TestMeteo {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--geocodage")) {
            var outil = new MeteoTool();
            for (String ville : new String[]{"Paris", "Rabat,Morocco", "Zzyzx"}) {
                System.out.println(ville + " : " + java.util.Arrays.toString(outil.getCoordonnees(ville)));
            }
            try {
                outil.getCoordonnees("VilleInexistanteChafiiXYZ123456");
                throw new AssertionError("Une ville inexistante ne doit pas produire de coordonnées.");
            } catch (IllegalArgumentException attendu) {
                System.out.println("Ville introuvable correctement signalée : " + attendu.getMessage());
            }
            return;
        }
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
