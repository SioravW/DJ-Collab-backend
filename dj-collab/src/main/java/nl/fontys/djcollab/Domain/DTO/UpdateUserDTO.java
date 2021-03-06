package nl.fontys.djcollab.Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserDTO {
    private UUID externalId;
    private String username;
    private String password;
}
