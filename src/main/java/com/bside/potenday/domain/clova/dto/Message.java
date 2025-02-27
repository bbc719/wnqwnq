package com.bside.potenday.domain.clova.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;

@Data
@Builder
public class Message {
    private static String prompt;
    private static String reactionPrompt;
    private static String summaryPrompt;
    private static String keywordPrompt;
    private ROLE role;
    private String content;

    public enum ROLE {
        system, assistant, user // 프롬프트, 사용자, 클로바 응답
    }

    public static Message creatSystemOf(String content) {
        return Message.builder()
                .role(ROLE.system)
                .content(content)
                .build();
    }

    public static Message creatAssistantOf(String content) {
        return Message.builder()
                .role(ROLE.assistant)
                .content(content)
                .build();
    }

    public static Message creatUserOf(String content) {
        return Message.builder()
                .role(ROLE.user)
                .content(content)
                .build();
    }

    public static Message pushDesignSystemOf() {
        return Message.builder()
                .role(Message.ROLE.system)
                .content(new String(Base64.getDecoder().decode(prompt)))
                .build();
    }

    public static Message getReactionOf() {
        return Message.builder()
                .role(Message.ROLE.system)
                .content(new String(Base64.getDecoder().decode(reactionPrompt)))
                .build();
    }

//    @Component
//    public static class Config {
//        @Value("${clova.prompt.design}")
//        private String design;
//        @Value("${clova.prompt.discover.reaction}")
//        private String reaction;
//        @Value("${clova.prompt.discover.summary}")
//        private String summary;
//        @Value("${clova.prompt.discover.keyword}")
//        private String keyword;
//
//        @PostConstruct
//        public void init() {
//            designPersonaPrompt = design;
//            reactionPrompt = reaction;
//            summaryPrompt = summary;
//            keywordPrompt = keyword;
//        }
//    }
}