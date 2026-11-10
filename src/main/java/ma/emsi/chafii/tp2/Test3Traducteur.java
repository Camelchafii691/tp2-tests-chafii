package ma.emsi.chafii.tp2;

import dev.langchain4j.model.input.PromptTemplate;
import java.util.Map;

public class Test3Traducteur {
    public static void main(String[] args) {
        var modele = Configuration.modele();
        String texte = "Bonjour, je suis étudiant à Casablanca et j’apprends Java.";
        var anglais = PromptTemplate.from("Traduis en anglais le texte suivant. Retourne uniquement la traduction : {{texte}}")
                .apply(Map.of("texte", texte));
        System.out.println("Prompt à une variable : " + anglais.text());
        System.out.println("Réponse : " + modele.chat(anglais.text()));

        var multilingue = PromptTemplate.from("Traduis en {{langue}} le texte suivant. Retourne uniquement la traduction : {{texte}}")
                .apply(Map.of("langue", args.length > 0 ? args[0] : "espagnol", "texte", texte));
        System.out.println("\nPrompt à deux variables : " + multilingue.text());
        System.out.println("Réponse : " + modele.chat(multilingue.text()));
    }
}
