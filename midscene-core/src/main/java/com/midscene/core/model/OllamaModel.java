package com.midscene.core.model;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class OllamaModel implements AIModel {

  private final ChatModel model;

  public OllamaModel(String baseUrl, String modelName) {
    this(baseUrl, modelName, null);
  }

  public OllamaModel(String baseUrl, String modelName, String apiKey) {
    OllamaChatModel.OllamaChatModelBuilder builder = OllamaChatModel.builder()
        .modelName(modelName)
        .baseUrl(baseUrl);

    if (apiKey != null && !apiKey.isEmpty() && !"ollama".equalsIgnoreCase(apiKey)) {
      builder.customHeaders(Map.of("Authorization", "Bearer " + apiKey));
    }

    this.model = builder.build();
  }

  @Override
  public ChatResponse chat(List<ChatMessage> messages) {
    return model.chat(messages);
  }
}
