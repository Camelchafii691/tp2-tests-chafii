package ma.emsi.chafii.tp2;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.AiServices;
import ma.emsi.chafii.tp2.outil.meteo.MeteoTool;

public class Test7Outils {
    public static void main(String[] args) {
        boolean logs = java.util.Arrays.asList(args).contains("--logs");
        var modele = logs
                ? Configuration.modele(Configuration.variable("GEMINI_MODEL", "gemini-3.5-flash"), 0.7, true)
                : Configuration.modele();
        if (args.length > 0 && args[0].equals("--sans-outil")) {
            String question = "Je dois partir dans 2 jours à Paris ; j’y resterai 3 jours. Est-ce que je dois mettre un parapluie dans mes valises ?";
            System.out.println("Question sans outils : " + question);
            System.out.println("Réponse : " + modele.chat(question));
            return;
        }
        var assistant = AiServices.builder(AssistantMeteo.class)
                .chatModel(modele)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .tools(new MeteoTool())
                .build();
        System.out.println("Exemple : séjour de 3 jours à partir d’aujourd’hui, latitude 48.85, longitude 2.35 ; faut-il un parapluie ?");
        Conversation.demarrer(assistant::chat);
    }
}
