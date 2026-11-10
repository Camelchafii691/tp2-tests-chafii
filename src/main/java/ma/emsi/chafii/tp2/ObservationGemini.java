package ma.emsi.chafii.tp2;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/** Espacement des appels et observation JSON au niveau des messages LangChain4j. */
public final class ObservationGemini implements ChatModelListener {
    private static long prochainAppel;
    private final long intervalleMs;
    private final boolean logs;
    private final Gson gson = new GsonBuilder().disableHtmlEscaping()
            .addSerializationExclusionStrategy(new ExclusionStrategy() {
                public boolean shouldSkipField(FieldAttributes champ) { return champ.getName().equals("attributes"); }
                public boolean shouldSkipClass(Class<?> classe) { return false; }
            }).create();

    public ObservationGemini(boolean logs) {
        this.logs = logs;
        this.intervalleMs = Long.parseLong(Configuration.variable("GEMINI_MIN_INTERVAL_MS", "15000"));
        if (intervalleMs < 0) throw new IllegalArgumentException("GEMINI_MIN_INTERVAL_MS doit être positif ou nul.");
    }

    private static synchronized void attendre(long intervalleMs) {
        long delai = prochainAppel - System.nanoTime();
        try {
            if (delai > 0) TimeUnit.NANOSECONDS.sleep(delai);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Attente entre les appels interrompue.", e);
        }
        prochainAppel = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(intervalleMs);
    }

    @Override public void onRequest(ChatModelRequestContext contexte) {
        attendre(intervalleMs);
        if (logs) {
            Map<String, Object> evenement = new LinkedHashMap<>();
            evenement.put("emetteur", "application");
            evenement.put("destinataire", "Gemini");
            evenement.put("type", "requete");
            evenement.put("messages", contexte.chatRequest().messages());
            evenement.put("outils", contexte.chatRequest().toolSpecifications() == null ? List.of() : contexte.chatRequest().toolSpecifications());
            System.out.println("JSON_REQUETE " + gson.toJson(evenement));
        }
    }

    @Override public void onResponse(ChatModelResponseContext contexte) {
        if (logs) {
            Map<String, Object> evenement = new LinkedHashMap<>();
            evenement.put("emetteur", "Gemini");
            evenement.put("destinataire", "application");
            evenement.put("type", "reponse");
            evenement.put("message", contexte.chatResponse().aiMessage());
            evenement.put("tokens", contexte.chatResponse().tokenUsage());
            System.out.println("JSON_REPONSE " + gson.toJson(evenement));
        }
    }
}
