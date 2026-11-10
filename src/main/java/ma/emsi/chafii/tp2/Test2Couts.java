package ma.emsi.chafii.tp2;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;

/** Tarifs USD par million de tokens, fournis explicitement au lancement. */
public class Test2Couts {
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage : Test2Couts tarifEntreeParMillion tarifSortieParMillion [question]. Consulter les tarifs du modèle choisi.");
        }
        double tarifEntree = Double.parseDouble(args[0]);
        double tarifSortie = Double.parseDouble(args[1]);
        if (!Double.isFinite(tarifEntree) || !Double.isFinite(tarifSortie)
                || tarifEntree < 0 || tarifSortie < 0) {
            throw new IllegalArgumentException("Les tarifs doivent être finis et positifs ou nuls.");
        }
        String question = args.length >= 3 ? args[2] : Test1.QUESTION;
        var reponse = Configuration.modele().chat(ChatRequest.builder()
                .messages(UserMessage.from(question)).build());
        System.out.println("Question : " + question);
        System.out.println("Réponse : " + reponse.aiMessage().text());
        var tokens = reponse.tokenUsage();
        if (tokens == null || tokens.inputTokenCount() == null || tokens.outputTokenCount() == null) {
            throw new IllegalStateException("L’API n’a pas renvoyé le nombre de tokens nécessaire au calcul.");
        }
        double cout = (tokens.inputTokenCount() * tarifEntree
                + tokens.outputTokenCount() * tarifSortie) / 1_000_000.0;
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
