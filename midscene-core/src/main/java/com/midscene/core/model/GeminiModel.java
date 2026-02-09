package com.midscene.core.model;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GeminiModel implements AIModel {

  private final ChatModel model;

  public GeminiModel(String apiKey, String modelName) {
    this(apiKey, modelName, null);
  }

  public GeminiModel(String apiKey, String modelName, String baseUrl) {
    GoogleAiGeminiChatModel.GoogleAiGeminiChatModelBuilder builder =
        GoogleAiGeminiChatModel.builder()
            .apiKey(apiKey)
            .modelName(modelName);

    if (baseUrl != null && !baseUrl.isEmpty()) {
      builder.baseUrl(baseUrl);
    }

    this.model = builder.build();
  }

  @Override
  public ChatResponse chat(List<ChatMessage> messages) {
    return model.chat(messages);
  }
}
