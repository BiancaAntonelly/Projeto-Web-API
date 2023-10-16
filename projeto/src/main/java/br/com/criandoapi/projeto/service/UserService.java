package br.com.criandoapi.projeto.service;

import br.com.criandoapi.projeto.DTO.CreateUserDTO;
import br.com.criandoapi.projeto.entity.User;
import br.com.criandoapi.projeto.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private final BCryptPasswordEncoder senhaOriginal = new BCryptPasswordEncoder();


    public boolean isIdExists(Long id) {
        return userRepository.findById(id).isPresent();
    }


    public Map<String, String> createUser(CreateUserDTO createUserDTO) {
        Map<String, String> response = new HashMap<>();

        String name = createUserDTO.getName();
        String email = createUserDTO.getEmail();
        String username = createUserDTO.getUsername();
        String password = createUserDTO.getPassword();
        String encodedPassword = senhaOriginal.encode(password);


        if (username.length() < 3) {
            response.put("message", "O nome do usuário deve ter pelo menos 3 letras.");
            return response;
        }


        List<User> existingUsers = userRepository.findByUsername(username);

        if (!existingUsers.isEmpty()) {
            boolean hasActiveUser = existingUsers.stream().anyMatch(user -> user.getActive());

            if (hasActiveUser) {
                response.put("message", "Já existe um usuário com esse username.");
            }
        } else {

            User newUser = new User();
            newUser.setActive(true);
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setUsername(username);
            newUser.setPassword(encodedPassword);

            userRepository.save(newUser);

            response.put("message", "Usuário criado com sucesso.");
        }

        return response;
    }

    public Map<String, String> editUser(Long id, User updateUser) {
        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("message", "Usuário não encontrado.");
            return jsonResponse;
        }

        List<User> usersWithSameUsername = userRepository.findByUsername(updateUser.getUsername());

        if (!usersWithSameUsername.isEmpty() && !usersWithSameUsername.contains(existingUser)) {
            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("message", "Já existe um usuário com o username " + updateUser.getUsername() + ".");
            return jsonResponse;
        }

        existingUser.setUsername(updateUser.getUsername());
        existingUser.setEmail(updateUser.getEmail());
        existingUser.setName(updateUser.getName());
        String encodedPassword = senhaOriginal.encode(updateUser.getPassword());
        existingUser.setPassword(encodedPassword);

        userRepository.save(existingUser);

        Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "Usuário " + updateUser.getUsername() + " editado com sucesso!");
        return jsonResponse;
    }



    public Map<String, String> deactivateUser(User deleteUser) {
        Map<String, String> jsonResponse = new HashMap<>();
        String username = deleteUser.getUsername();

        List<User> existingUsers = userRepository.findByUsername(username);

        if (!existingUsers.isEmpty()) {
            User existingUser = existingUsers.get(0);
            existingUser.setActive(false);
            userRepository.save(existingUser);
            jsonResponse.put("message", "Usuário desativado com sucesso.");
        } else {
            jsonResponse.put("error", "Não existe um usuário com esse username.");
        }

        return jsonResponse;
    }

}
