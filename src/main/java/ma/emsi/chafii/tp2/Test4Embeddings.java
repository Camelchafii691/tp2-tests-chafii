package ma.emsi.chafii.tp2;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.google.genai.GoogleGenAiEmbeddingModel;
import dev.langchain4j.model.google.genai.GoogleGenAiEmbeddingModel.TaskTypeEnum;
import dev.langchain4j.store.embedding.CosineSimilarity;
import java.time.Duration;

public class Test4Embeddings {
    private static final String[][] PHRASES = {
            {"Le chat dort sur le canapé.", "Un félin se repose sur le sofa."},
            {"J’aime les journées chaudes.", "Je déteste les journées chaudes."},
            {"Il pleut beaucoup aujourd’hui.", "La météo est très pluvieuse ce jour."},
            {"Je programme une application Java.", "Je prépare une tarte aux pommes."},
            {"The dog is running in the park.", "A dog runs through the garden."},
            {"Le chien joue dans le jardin.", "The dog is playing in the garden."}
    };

    public static void main(String[] args) {
        comparer("BGE small anglais quantifié (local, fourni par EasyRAG)", new BgeSmallEnV15QuantizedEmbeddingModel());
        if (args.length > 0 && args[0].equals("--local")) {
            System.out.println("Mode local : comparaison Google non exécutée.");
            return;
        }
        long timeoutMs = Long.parseLong(Configuration.variable("EMBEDDING_TIMEOUT_MS", "30000"));
        var google = GoogleGenAiEmbeddingModel.builder()
                .apiKey(Configuration.cleApi())
                .modelName(Configuration.variable("GEMINI_EMBEDDING_MODEL", "gemini-embedding-001"))
                .taskType(TaskTypeEnum.SEMANTIC_SIMILARITY)
                .timeout(Duration.ofMillis(timeoutMs))
                .maxRetries(0)
                .build();
        comparer("Google multilingue", google);
    }

    private static void comparer(String nom, EmbeddingModel modele) {
        System.out.println("\nModèle : " + nom);
        for (String[] couple : PHRASES) {
            var a = modele.embed(couple[0]).content();
            var b = modele.embed(couple[1]).content();
            double similarite = CosineSimilarity.between(a, b);
            System.out.printf(java.util.Locale.ROOT, "%s | %s | %.6f%n", couple[0], couple[1], similarite);
        }
    }
}
