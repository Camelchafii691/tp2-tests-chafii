package ma.emsi.chafii.tp2;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.google.genai.GoogleGenAiTokenUsage;

/** Tarifs standard Flash-Lite vérifiés le 10 novembre 2026 ; surcharge possible. */
public class Test2Couts {
    public static void main(String[] args) {
        if (args.length == 1) {
            throw new IllegalArgumentException("Usage : Test2Couts tarifEntreeParMillion tarifSortieParMillion [question]. Consulter les tarifs du modèle choisi.");
        }
        String nom = Configuration.variable("GEMINI_COST_MODEL", "gemini-3.5-flash-lite");
        if (!nom.equals("gemini-3.5-flash-lite") && args.length < 2) {
            throw new IllegalArgumentException("Fournir les deux tarifs du modèle alternatif choisi.");
        }
        double tarifEntree = args.length >= 2 ? Double.parseDouble(args[0]) : 0.30;
        double tarifSortie = args.length >= 2 ? Double.parseDouble(args[1]) : 2.50;
        if (!Double.isFinite(tarifEntree) || !Double.isFinite(tarifSortie)
                || tarifEntree < 0 || tarifSortie < 0) {
            throw new IllegalArgumentException("Les tarifs doivent être finis et positifs ou nuls.");
        }
        String question = args.length >= 3 ? args[2] : Test1.QUESTION;
        var reponse = Configuration.modele(nom, 0.7, false).chat(ChatRequest.builder()
                .messages(UserMessage.from(question)).build());
        System.out.println("Question : " + question);
        System.out.println("Réponse : " + reponse.aiMessage().text());
        var tokens = reponse.tokenUsage();
        if (tokens == null || tokens.inputTokenCount() == null || tokens.outputTokenCount() == null) {
            throw new IllegalStateException("L’API n’a pas renvoyé le nombre de tokens nécessaire au calcul.");
        }
        int reflexion = tokens instanceof GoogleGenAiTokenUsage google && google.thoughtsTokenCount() != null
                ? google.thoughtsTokenCount() : 0;
        double cout = (tokens.inputTokenCount() * tarifEntree
                + ((long) tokens.outputTokenCount() + reflexion) * tarifSortie) / 1_000_000.0;
        System.out.println("Modèle : " + nom + " ; tarifs USD / million : " + tarifEntree + " entrée, " + tarifSortie + " sortie");
        System.out.println("Tokens de réflexion facturés en sortie : " + reflexion);
        System.out.printf(java.util.Locale.ROOT,
                "Entrée : %d tokens ; sortie : %d tokens%nCoût estimé de cette requête : %.10f USD%n",
                tokens.inputTokenCount(), tokens.outputTokenCount(), cout);
        if (cout > 0) {
            System.out.printf(java.util.Locale.ROOT,
                    "Nombre de requêtes identiques pour atteindre 1 USD : %.0f%n", Math.ceil(1.0 / cout));
        } else {
            System.out.println("Coût nul avec ces tarifs ; 1 USD ne sera pas atteint.");
        }
        System.out.println("Estimation entrée/sortie au tarif payant saisi ; vérifier les règles de facturation du modèle et du quota.");
    }
}
