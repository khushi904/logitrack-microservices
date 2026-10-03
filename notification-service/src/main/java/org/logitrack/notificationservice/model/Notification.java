package org.logitrack.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {

//* id → MongoDB ID
//* userId → user who receives the notification
//* type → e.g. SHIPMENT_STATUS
//* message → notification text
//* channel → EMAIL for now, later SMS, PUSH
//* read → whether the notification was read
//* createdAt → creation timestamp
   @Id
    private String id;

    private String userId;
    private String type;
    private String message;
    private String channel;
    private boolean read;
    private Instant createdAt;
}
