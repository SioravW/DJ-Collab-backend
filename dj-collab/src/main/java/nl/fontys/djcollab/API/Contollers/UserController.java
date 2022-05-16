package nl.fontys.djcollab.API.Contollers;

import nl.fontys.djcollab.Domain.DTO.AddUserDTO;
import nl.fontys.djcollab.Domain.DTO.UpdateUserDTO;
import nl.fontys.djcollab.Domain.Models.User;
import nl.fontys.djcollab.Domain.Service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/user")
@RestController
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;

    public UserController(UserService service)
    {
        this.service = service;
    }

    @PostMapping
    public User AddUser(@RequestBody AddUserDTO DTO) {

        return service.addUser(DTO);
    }

    @GetMapping
    public List<User> GetAllUsers() {
        return service.getAllUsers();
    }

    @PutMapping(value = "/{id}")
    public User updateUser(@PathVariable long id, @RequestBody UpdateUserDTO DTO){
        return service.updateUser(id, DTO);
    }

    @DeleteMapping(value = "/{id}")
    public boolean deleteUser(@PathVariable long id){
        return service.deleteUser(id);
    }

}