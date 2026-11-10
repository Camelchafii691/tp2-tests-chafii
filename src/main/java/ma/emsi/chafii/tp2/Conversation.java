package ma.emsi.chafii.tp2;

import java.util.Scanner;
import java.util.function.Function;

public final class Conversation {
    private Conversation() {}
    public static void demarrer(Function<String, String> repondre) {
        System.out.println("Pose une question ; écris fin pour quitter.");
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String question = scanner.nextLine().trim();
                if (question.equalsIgnoreCase("fin")) break;
                if (question.isBlank()) continue;
                System.out.println("Question : " + question);
                System.out.println("Assistant : " + repondre.apply(question));
            }
        }
    }
}
