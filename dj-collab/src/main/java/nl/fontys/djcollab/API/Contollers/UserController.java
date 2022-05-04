package nl.fontys.djcollab.API.Contollers;

import nl.fontys.djcollab.Domain.DTO.AddUserDTO;
import nl.fontys.djcollab.Domain.DTO.UpdateUserDTO;
import nl.fontys.djcollab.Domain.DTO.UserDTO;
import nl.fontys.djcollab.Domain.Models.User;
import nl.fontys.djcollab.Domain.Service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController(value = "/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;

    public UserController(UserService service)
    {
        this.service = service;
    }

    @PostMapping
    public UserDTO AddUser(@RequestBody AddUserDTO DTO) {

        return service.addUser(DTO);
    }

    @GetMapping
    public List<UserDTO> GetAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    public UserDTO GetUser(@PathVariable UUID id) {
        return service.getUserById(id);
    }

    @PutMapping(value = "/{id}")
    public UserDTO updateUser(@PathVariable UUID id, @RequestBody UpdateUserDTO DTO){
        return service.updateUser(id, DTO);
    }

    @DeleteMapping(value = "/{id}")
    public boolean deleteUser(@PathVariable UUID id){
        return service.deleteUser(id);
    }

}