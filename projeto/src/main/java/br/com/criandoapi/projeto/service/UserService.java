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

        String login = createUserDTO.getUsername();
        String password = createUserDTO.getPassword();
        String encodedPassword = senhaOriginal.encode(password);


        if (login.length() < 3) {
            response.put("message", "O nome do usuário deve ter pelo menos 3 letras.");
            return response;
        }


        List<User> existingUsers = userRepository.findByLogin(login);

        if (!existingUsers.isEmpty()) {
            boolean hasActiveUser = existingUsers.stream().anyMatch(User::isActive);

            if (hasActiveUser) {
                response.put("message", "Já existe um usuário com esse login.");
            }
        } else {

            User newUser = new User();
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

        List<User> usersWithSameLogin = userRepository.findByLogin(updateUser.getUsername());

        if (!usersWithSameLogin.isEmpty() && !usersWithSameLogin.contains(existingUser)) {
            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("message", "Já existe um usuário com o login " + updateUser.getUsername() + ".");
            return jsonResponse;
        }

        existingUser.setUsername(updateUser.getUsername());
        String encodedPassword = senhaOriginal.encode(updateUser.getPassword());
        existingUser.setPassword(encodedPassword);

        userRepository.save(existingUser);

        Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "Usuário " + updateUser.getUsername() + " editado com sucesso!");
        return jsonResponse;
    }


    public Map<String, String> deactivateUser(Long id) {
        Map<String, String> jsonResponse = new HashMap<>();

        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser != null) {
            existingUser.setActive(false);
            userRepository.save(existingUser);


            jsonResponse.put("message", "Usuário desativado com sucesso.");
        } else {
            jsonResponse.put("error", "Não existe um usuário com esse ID.");
        }

        return jsonResponse;
    }
}
