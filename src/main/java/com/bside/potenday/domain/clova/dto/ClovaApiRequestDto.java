package com.bside.potenday.domain.clova.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClovaApiRequestDto {
    private ArrayList<Message> messages;
    private double topP;
    private double temperature;
    private int maxTokens;
    private double repeatPenalty;

    public static ClovaApiRequestDto promptRequestOf() {
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(Message.pushDesignSystemOf());

        return ClovaApiRequestDto.builder()
                .messages(messages)
                .topP(0.8)
                .temperature(0.3)
                .maxTokens(256)
                .repeatPenalty(5.0)
                .build();
    }
}
