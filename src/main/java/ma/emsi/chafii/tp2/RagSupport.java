package ma.emsi.chafii.tp2;

import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import java.nio.file.Files;
import java.nio.file.Path;

/** Adaptation du code RAG fourni dans l’énoncé de Richard Grin. */
public final class RagSupport {
    public interface Assistant { String chat(String question); }
    private RagSupport() {}

    public static InMemoryEmbeddingStore<TextSegment> ingerer(String fichier) {
        if (!Files.isRegularFile(Path.of(fichier))) {
            throw new IllegalArgumentException("Document introuvable : " + Path.of(fichier).toAbsolutePath());
        }
        var document = FileSystemDocumentLoader.loadDocument(fichier);
        var magasin = new InMemoryEmbeddingStore<TextSegment>();
        EmbeddingStoreIngestor.ingest(document, magasin);
        System.out.println("Document ingéré dans le magasin vectoriel : " + fichier);
        return magasin;
    }

    public static Assistant assistant(String fichier) {
        var modele = Configuration.modele(0.3);
        var magasin = ingerer(fichier);
        return AiServices.builder(Assistant.class)
                .chatModel(modele)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(EmbeddingStoreContentRetriever.from(magasin))
                .build();
    }
}
