package com.example.socialNetwork.util;


import com.example.socialNetwork.domain.GeneralMessage;
import com.example.socialNetwork.domain.PrivateMessage;
import com.example.socialNetwork.dto.EventType;
import com.example.socialNetwork.dto.ObjectType;
import com.example.socialNetwork.dto.WsEventDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import java.util.function.BiConsumer;

@Component
public class WsSender {
    private final SimpMessagingTemplate template;
    private final ObjectMapper mapper;

    public WsSender(SimpMessagingTemplate template, ObjectMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    public <T> BiConsumer<EventType, T> getSender(ObjectType objectType, Class view){
        ObjectWriter writer = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(view);

        return (EventType eventType, T payload) -> {
            String value = null;

            try{
                value = writer.writeValueAsString(payload);
            } catch (JsonProcessingException e){
                throw new RuntimeException();
            }


            if (payload.getClass().equals(PrivateMessage.class)){
                PrivateMessage privateMessage = (PrivateMessage) payload;

                String addressee = privateMessage.getAddressee().getId();
                String author = privateMessage.getAuthor().getId();


                template.convertAndSend("/topic/activity/" + addressee,
                        new WsEventDto(objectType, eventType, value)
                );
            }

            if (payload.getClass().equals(GeneralMessage.class)){
                template.convertAndSend("/topic/activity",
                        new WsEventDto(objectType, eventType, value)
                );
            }

            if (payload.getClass().equals(String.class)){
                String id = (String) payload;

                template.convertAndSend("/topic/activity/" + id,
                        new WsEventDto(objectType, eventType, value)
                );
            }
        };
    }
}
