package br.com.criandoapi.projeto.controller.rest;

import br.com.criandoapi.projeto.DTO.CreateUserDTO;
import br.com.criandoapi.projeto.entity.User;
import br.com.criandoapi.projeto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserRestController {

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody CreateUserDTO createUserDTO) {
        Map<String, String> response = userService.createUser(createUserDTO);

        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<Map<String, String>> editUser(@PathVariable Long id, @RequestBody User updateUser) {

        Map<String, String> response = userService.editUser(id, updateUser);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        Map<String, String> response = userService.deactivateUser(id);

        HttpStatus httpStatus = response.containsKey("error") ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(response);
    }



/*
    @GetMapping
    public List<br.com.criandoapi.projeto.entity.User> listaUsuarios(){
        return (List<br.com.criandoapi.projeto.entity.User>) usuarioRepository.findAll();
    }

    @PostMapping
    public br.com.criandoapi.projeto.entity.User criarUsuario(@RequestBody br.com.criandoapi.projeto.entity.User usuario){
        br.com.criandoapi.projeto.entity.User usuarioNovo = usuarioRepository.save(usuario);
        return usuarioNovo;
    }

    @PutMapping
    public br.com.criandoapi.projeto.entity.User editarUsuario(@RequestBody br.com.criandoapi.projeto.entity.User usuario){
        br.com.criandoapi.projeto.entity.User usuarioNovo = usuarioRepository.save(usuario);
        return usuarioNovo;
    }

*/
}
