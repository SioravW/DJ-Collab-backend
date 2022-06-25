package nl.fontys.djcollab.Domain.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import java.util.UUID;


@Getter
@Setter
@Document("users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private String id;

    private UUID externalId;
    private String username;
    private String password;
}
