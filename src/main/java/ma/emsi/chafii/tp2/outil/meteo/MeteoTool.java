package ma.emsi.chafii.tp2.outil.meteo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Locale;

/** Adaptation de l’outil Open-Meteo de Richard Grin. */
public class MeteoTool {
    private final double seuilPluie;
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();

    public MeteoTool() { this(25.0); }
    public MeteoTool(double seuilPluie) {
        if (!Double.isFinite(seuilPluie) || seuilPluie < 0 || seuilPluie > 100) {
            throw new IllegalArgumentException("Le seuil doit être compris entre 0 et 100.");
        }
        this.seuilPluie = seuilPluie;
    }

    @Tool("""
        Indique si une probabilité quotidienne de pluie dépasse le seuil pendant le séjour.
        Utiliser les coordonnées latitude/longitude de la ville.
        nbJoursDebut vaut 0 pour aujourd’hui, 1 pour demain, etc.
        nbJours est la durée du séjour. La période complète doit tenir dans les 7 prochains jours.
        Les jours sont ceux du fuseau horaire du lieu ; true conseille un parapluie.
        """)
    public boolean isPluiePrevue(
            @P("Latitude entre -90 et 90") double latitude,
            @P("Longitude entre -180 et 180") double longitude,
            @P("Nombre de jours après aujourd’hui avant le départ") int nbJoursDebut,
            @P("Durée du séjour en jours") int nbJours) {
        if (!Double.isFinite(latitude) || !Double.isFinite(longitude)
                || latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Coordonnées invalides.");
        }
        if (nbJoursDebut < 0 || nbJoursDebut > 6 || nbJours < 1 || nbJours > 7 - nbJoursDebut) {
            throw new IllegalArgumentException("Le séjour doit être entièrement compris dans les 7 prochains jours.");
        }
        int fin = nbJoursDebut + nbJours;
        String url = String.format(Locale.ROOT,
                "https://api.open-meteo.com/v1/forecast?latitude=%.6f&longitude=%.6f&daily=precipitation_probability_max&forecast_days=%d&timezone=auto",
                latitude, longitude, fin);
        JsonObject daily = lireJson(url).getAsJsonObject("daily");
        if (daily == null || !daily.has("precipitation_probability_max") || !daily.has("time")) {
            throw new IllegalStateException("Réponse météo incomplète.");
        }
        var probabilites = daily.getAsJsonArray("precipitation_probability_max");
        var dates = daily.getAsJsonArray("time");
        if (probabilites.size() < fin || dates.size() < fin) {
            throw new IllegalStateException("Prévisions indisponibles pour toute la période demandée.");
        }
        boolean pluie = false;
        for (int i = nbJoursDebut; i < fin; i++) {
            if (probabilites.get(i).isJsonNull()) {
                throw new IllegalStateException("Probabilité de pluie indisponible pour " + dates.get(i));
            }
            double proba = probabilites.get(i).getAsDouble();
            System.out.printf(Locale.ROOT, "Open-Meteo %s : %.0f %% (seuil : %.0f %%)%n", dates.get(i).getAsString(), proba, seuilPluie);
            pluie |= proba > seuilPluie;
        }
        return pluie;
    }

    @Tool("""
        Recherche les coordonnées d’une ville par son nom en anglais avant d’utiliser l’outil pluie.
        Retourne [latitude, longitude]. Pour lever une ambiguïté, utiliser Ville,Pays en anglais,
        par exemple Rabat,Morocco. Ne pas inventer de coordonnées si la recherche échoue.
        """)
    public double[] getCoordonnees(@P("Nom de ville en anglais, éventuellement suivi de ,Pays en anglais") String ville) {
        if (ville == null || ville.isBlank()) throw new IllegalArgumentException("Le nom de ville est obligatoire.");
        String[] morceaux = ville.trim().split(",", 2);
        String nom = morceaux[0].trim();
        if (nom.isBlank()) throw new IllegalArgumentException("Le nom de ville est obligatoire.");
        String pays = morceaux.length == 2 ? morceaux[1].trim() : "";
        String url = "https://geocoding-api.open-meteo.com/v1/search?name="
                + URLEncoder.encode(nom, StandardCharsets.UTF_8) + "&count=10&language=en&format=json";
        JsonObject json = lireJson(url);
        if (json.has("results")) {
            for (var valeur : json.getAsJsonArray("results")) {
                JsonObject lieu = valeur.getAsJsonObject();
                boolean paysCorrect = pays.isEmpty()
                        || (lieu.has("country") && lieu.get("country").getAsString().equalsIgnoreCase(pays))
                        || (lieu.has("country_code") && lieu.get("country_code").getAsString().equalsIgnoreCase(pays));
                if (paysCorrect) {
                    double latitude = lieu.get("latitude").getAsDouble();
                    double longitude = lieu.get("longitude").getAsDouble();
                    System.out.printf(Locale.ROOT, "Géocodage %s : %.5f, %.5f%n", ville, latitude, longitude);
                    return new double[]{latitude, longitude};
                }
            }
        }
        throw new IllegalArgumentException("Aucune coordonnée trouvée pour la ville : " + ville);
    }

    private JsonObject lireJson(String url) {
        try {
            var requete = HttpRequest.newBuilder(URI.create(url)).timeout(Duration.ofSeconds(30)).GET().build();
            var reponse = client.send(requete, HttpResponse.BodyHandlers.ofString());
            if (reponse.statusCode() != 200) {
                throw new IllegalStateException("Open-Meteo a retourné HTTP " + reponse.statusCode());
            }
            return JsonParser.parseString(reponse.body()).getAsJsonObject();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Appel Open-Meteo interrompu.", e);
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de contacter Open-Meteo.", e);
        }
    }
}
