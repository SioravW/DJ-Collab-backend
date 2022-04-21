package nl.fontys.djcollab.Domain.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddUserDTO {

    private String username;
    private String password;
}
