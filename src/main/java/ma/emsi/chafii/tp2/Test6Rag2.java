package ma.emsi.chafii.tp2;

public class Test6Rag2 {
    public static void main(String[] args) {
        String fichier = Configuration.variable("RAG_PDF", "Machine learning.pdf");
        if (args.length > 0 && args[0].equals("--ingestion-seule")) {
            RagSupport.ingerer(fichier);
            System.out.println("PDF chargé, découpé et vectorisé localement ; aucun appel Gemini effectué.");
            return;
        }
        var assistant = RagSupport.assistant(fichier);
        System.out.println("Exemple : Quel est l’objectif du cours de Richard Grin ?");
        Conversation.demarrer(assistant::chat);
    }
}
