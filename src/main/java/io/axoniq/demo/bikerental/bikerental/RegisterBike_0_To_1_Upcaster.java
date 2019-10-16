package io.axoniq.demo.bikerental.bikerental;

import org.axonframework.serialization.SimpleSerializedType;
import org.axonframework.serialization.upcasting.event.IntermediateEventRepresentation;
import org.axonframework.serialization.upcasting.event.SingleEventUpcaster;
import org.dom4j.Document;
import org.springframework.stereotype.Component;

@Component
public class RegisterBike_0_To_1_Upcaster extends SingleEventUpcaster {
    @Override
    protected boolean canUpcast(IntermediateEventRepresentation intermediateRepresentation) {
        return intermediateRepresentation.getType().getName().equals("io.axoniq.demo.bikerental.bikerental.coreapi.BikeRegisteredEvent")
                && intermediateRepresentation.getType().getRevision() == null;
    }

    @Override
    protected IntermediateEventRepresentation doUpcast(IntermediateEventRepresentation intermediateRepresentation) {
        return intermediateRepresentation.upcastPayload(
                new SimpleSerializedType(intermediateRepresentation.getType().getName(), "1"),
                Document.class,
                event -> {
                    event.getRootElement().addElement("bikeType").setText("regular");
                    return event;
                }
        );
    }
}
