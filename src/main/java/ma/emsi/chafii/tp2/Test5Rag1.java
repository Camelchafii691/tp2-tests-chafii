package ma.emsi.chafii.tp2;

public class Test5Rag1 {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--ingestion-seule")) {
            RagSupport.ingerer("infos.txt");
            RagSupport.ingerer("infos-contradictoires.txt");
            System.out.println("Ingestion vérifiée ; aucun appel Gemini effectué.");
            return;
        }
        var modele = Configuration.modele(0.3);
        String question = "Comment s’appelle le chat de Pierre ?";
        System.out.println("Sans RAG — " + question);
        System.out.println(modele.chat(question));
        var assistant = RagSupport.assistant("infos.txt");
        poser(assistant, question);
        poser(assistant, "Pierre appelle son chat. Qu’est-ce qu’il pourrait dire ?");
        // Nouvelle ingestion et nouvelle mémoire pour isoler l’expérience contradictoire.
        var contradictoire = RagSupport.assistant("infos-contradictoires.txt");
        poser(contradictoire, "Quelle est la capitale de la France ?");
        System.out.println("Analyse : comparer la réponse à la source contradictoire ; le comportement exact dépend du modèle.");
    }

    private static void poser(RagSupport.Assistant assistant, String question) {
        System.out.println("\nAvec RAG — " + question);
        System.out.println("Réponse : " + assistant.chat(question));
    }
}
