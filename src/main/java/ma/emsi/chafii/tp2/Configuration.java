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
        String cle = System.getenv("GEMINI_API_KEY");
        if (cle == null || cle.isBlank()) {
            throw new IllegalStateException("Définir GEMINI_API_KEY dans les variables d’environnement avant ce test.");
        }
        return cle;
    }

    public static GoogleGenAiChatModel modele() {
        return GoogleGenAiChatModel.builder()
                .apiKey(cleApi())
                .modelName(variable("GEMINI_MODEL", "gemini-3.5-flash"))
                .temperature(0.7)
                .timeout(Duration.ofSeconds(60))
                .maxRetries(0)
                .logRequestsAndResponses(Boolean.parseBoolean(variable("GEMINI_LOG", "false")))
                .build();
    }
}
