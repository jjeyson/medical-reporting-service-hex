package org.indra.medicalreportingservice.adapter.in.messaging.deserializer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.indra.medicalreportingservice.adapter.in.messaging.dto.DocumentAttachedEvent;

public class DocumentAttachedEventDeserializer
        extends ObjectMapperDeserializer<DocumentAttachedEvent> {

    public DocumentAttachedEventDeserializer() {
        super(DocumentAttachedEvent.class);
    }
}
