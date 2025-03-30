package org.ngcvfb.eventhubkz.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class NotificationMessage implements Serializable {
    private String emailName;
    private Long eventId;
    private String action;

    public NotificationMessage(String emailName, Long eventId, String action) {
        this.emailName = emailName;
        this.eventId = eventId;
        this.action = action;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "userEmail='" + emailName + '\'' +
                ", eventId=" + eventId +
                ", action='" + action + '\'' +
                '}';
    }
}
