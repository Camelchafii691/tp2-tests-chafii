package ma.emsi.chafii.tp2;

/** Trois expériences : question simple, heure actuelle, absence de mémoire. */
public class Test1 {
    public static final String QUESTION = "Quelle est la capitale du Maroc ?";

    public static void main(String[] args) {
        var modele = Configuration.modele();
        for (String question : new String[]{
                QUESTION,
                "Quelle heure est-il actuellement à Casablanca ?",
                "Bonjour, je m’appelle Camel Chafii.",
                "Quel est mon nom ?"
        }) {
            System.out.println("\nQuestion : " + question);
            System.out.println("Réponse : " + modele.chat(question));
        }
        System.out.println("\nAnalyse : les appels ci-dessus sont indépendants, aucun historique n’est transmis.");
        System.out.println("Sans outil, le modèle ne dispose pas d’une horloge en temps réel.");
    }
}
