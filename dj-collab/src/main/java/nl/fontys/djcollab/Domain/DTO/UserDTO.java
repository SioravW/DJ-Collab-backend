package nl.fontys.djcollab.Domain.DTO;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private UUID id;
    private String username;
    private String password;
}
