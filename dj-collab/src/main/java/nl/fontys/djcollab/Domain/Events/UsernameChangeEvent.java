package nl.fontys.djcollab.Domain.Events;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UsernameChangeEvent {
    private UUID userId;
    private String username;
}
