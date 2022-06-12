package nl.fontys.djcollab.API.Contollers;

import nl.fontys.djcollab.Domain.DTO.AddUserDTO;
import nl.fontys.djcollab.Domain.DTO.UpdateUserDTO;
import nl.fontys.djcollab.Domain.DTO.UserDTO;
import nl.fontys.djcollab.Domain.Models.User;
import nl.fontys.djcollab.Domain.Service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;

@RequestMapping(path = "/user")
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@PreAuthorize("hasRole('api-user')")
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