package nl.fontys.djcollab.Domain.Service;

import nl.fontys.djcollab.Database.Repository.IUserRepository;
import nl.fontys.djcollab.Domain.DTO.AddUserDTO;
import nl.fontys.djcollab.Domain.DTO.UpdateUserDTO;
import nl.fontys.djcollab.Domain.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public List<User> users;
    private IUserRepository repository;

    @Autowired
    public UserService(IUserRepository repository) {
        this.repository = repository;
    }

    public User addUser(AddUserDTO DTO)
    {
        User user = new User();
        user.setUsername(DTO.getUsername());
        user.setPassword(DTO.getPassword());
        return repository.save(user);
    }

    public List<User> getAllUsers()
    {
        return repository.findAll();
    }

    public User updateUser(long id, UpdateUserDTO DTO)
    {
        Optional<User> optUser = repository.findById(id);
        User user = optUser.get();
        user.setUsername(DTO.getUsername());
        user.setPassword(DTO.getPassword());

        return repository.save(user);
    }

    public boolean deleteUser(long id){
        Optional<User> optUser = repository.findById(id);
        User user = optUser.get();
        repository.delete(user);

        optUser = repository.findById(id);
        return optUser.isEmpty();
    }
}
