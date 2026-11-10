package ma.emsi.chafii.tp2;

import dev.langchain4j.service.SystemMessage;

public interface AssistantMeteo {
    @SystemMessage("""
        Tu réponds en français. Pour les prévisions de pluie, utilise les outils disponibles.
        Si la ville est donnée par son nom, appelle getCoordonnees puis isPluiePrevue.
        Ne fabrique ni prévisions, ni coordonnées d’une ville inconnue ; signale toute information manquante.
        Si un outil échoue, explique que la météo n’a pas pu être déterminée.
        Réponds aussi aux questions générales sans utiliser les outils météo lorsque ce n’est pas utile.
        """)
    String chat(String question);
}
