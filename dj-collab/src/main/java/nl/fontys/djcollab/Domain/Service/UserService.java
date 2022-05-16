package nl.fontys.djcollab.Domain.Service;

import nl.fontys.djcollab.Database.Repository.IUserRepository;
import nl.fontys.djcollab.Domain.DTO.AddUserDTO;
import nl.fontys.djcollab.Domain.DTO.UpdateUserDTO;
import nl.fontys.djcollab.Domain.Events.UsernameChangeEvent;
import nl.fontys.djcollab.Domain.DTO.UserDTO;
import nl.fontys.djcollab.Domain.Models.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    public List<User> users;
    private IUserRepository repository;

    @Autowired
    private UserService(IUserRepository repository) {
        this.repository = repository;
    }

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${fontys.rabbitmq.exchange}")
    private String exchange;
    @Value("${fontys.rabbitmq.routingkey}")
    private String routingkey;

    public UserDTO addUser(AddUserDTO DTO)
    {
        User user = new User();
        user.setExternalId(UUID.randomUUID());
        user.setUsername(DTO.getUsername());
        user.setPassword(DTO.getPassword());
        user = repository.save(user);
        return UserDTO.builder()
                .id(user.getExternalId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    public List<UserDTO> getAllUsers()
    {
        List<User> users = repository.findAll();
        List<UserDTO> reply = new ArrayList<>();
        for (User user :
                users) {
            UserDTO userDTO = UserDTO.builder()
                    .id(user.getExternalId())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .build();
            reply.add(userDTO);
        }
        return reply;
    }

    public UserDTO getUserById(UUID id)
    {
        Optional<User> optUser = repository.findByExternalId(id);
        User user = optUser.get();
        return UserDTO.builder()
                .id(user.getExternalId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    public UserDTO updateUser(UUID id, UpdateUserDTO DTO)
    {
        Optional<User> optUser = repository.findByExternalId(id);
        User user = optUser.get();
        user.setUsername(DTO.getUsername());
        user.setPassword(DTO.getPassword());

        User savedUser = repository.save(user);

        UsernameChangeEvent usernameChangeEvent = new UsernameChangeEvent();
        usernameChangeEvent.setUsername(savedUser.getUsername());
        usernameChangeEvent.setUserId(savedUser.getExternalId());
        rabbitTemplate.convertAndSend(exchange, routingkey, usernameChangeEvent);

        return UserDTO.builder()
                .id(user.getExternalId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

    }

    public boolean deleteUser(UUID id){
        Optional<User> optUser = repository.findByExternalId(id);
        User user = optUser.get();
        repository.delete(user);

        optUser = repository.findByExternalId(id);
        return optUser.isEmpty();
    }
}
