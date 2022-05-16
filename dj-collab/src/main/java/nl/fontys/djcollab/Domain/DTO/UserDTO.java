package nl.fontys.djcollab.Domain.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class UserDTO {

    private UUID id;
    private String username;
    private String password;
}
