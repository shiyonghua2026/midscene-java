package com.midscene.core.model;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import java.util.List;

public class OpenAIModel implements AIModel {

  private final ChatModel model;

  public OpenAIModel(String apiKey, String modelName) {
    this(apiKey, modelName, null);
  }

  public OpenAIModel(String apiKey, String modelName, String baseUrl) {
    OpenAiChatModel.OpenAiChatModelBuilder builder = OpenAiChatModel.builder()
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
