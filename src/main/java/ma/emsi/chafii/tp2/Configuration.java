package ma.emsi.chafii.tp2;

import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import java.time.Duration;

/** Configuration commune ; aucun secret n’est écrit dans le dépôt. */
public final class Configuration {
    private Configuration() {}

    public static String variable(String nom, String valeurParDefaut) {
        String valeur = System.getenv(nom);
        return valeur == null || valeur.isBlank() ? valeurParDefaut : valeur;
    }

    public static String cleApi() {
        String cle = variable("GEMINI_API_KEY", System.getenv("GEMINI_KEY"));
        if (cle == null || cle.isBlank()) {
            throw new IllegalStateException("Définir GEMINI_API_KEY dans les variables d’environnement avant ce test.");
        }
        return cle;
    }

    public static GoogleGenAiChatModel modele() {
        return modele(0.7);
    }

    public static GoogleGenAiChatModel modele(double temperature) {
        return modele(variable("GEMINI_MODEL", "gemini-3.5-flash"), temperature,
                Boolean.parseBoolean(variable("GEMINI_LOG", "false")));
    }

    public static GoogleGenAiChatModel modele(String nom, double temperature, boolean logs) {
        return GoogleGenAiChatModel.builder()
                .apiKey(cleApi())
                .modelName(nom)
                .temperature(temperature)
                .timeout(Duration.ofSeconds(60))
                .listeners(java.util.List.of(new ObservationGemini(logs)))
                .maxRetries(0)
                .logRequestsAndResponses(logs)
                .build();
    }
}
