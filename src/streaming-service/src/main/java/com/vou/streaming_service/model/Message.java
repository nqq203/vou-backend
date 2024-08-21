/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 08/08/2024 - 10:30
 */
package com.vou.streaming_service.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Message extends BaseModel {

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    private String content;
    private String room;

    private String username;


    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convert this object to a JSON string
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // Handle error if conversion fails
            e.printStackTrace();
            return super.toString(); // Fallback to default toString if JSON conversion fails
        }
    }

}
