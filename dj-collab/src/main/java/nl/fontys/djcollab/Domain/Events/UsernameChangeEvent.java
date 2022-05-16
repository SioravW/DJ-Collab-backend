package nl.fontys.djcollab.Domain.Events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameChangeEvent {
    private String userId;
    private String username;
}
