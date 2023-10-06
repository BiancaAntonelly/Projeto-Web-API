package br.com.criandoapi.projeto.controller;

import br.com.criandoapi.projeto.entity.Usuario;
import br.com.criandoapi.projeto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @GetMapping
    public List<Usuario> listaUsuarios(){
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @PostMapping
    public Usuario criarUsuario(@RequestBody Usuario usuario){
        Usuario usuarioNovo = usuarioRepository.save(usuario);
        return usuarioNovo;
    }

    @PutMapping
    public Usuario editarUsuario(@RequestBody Usuario usuario){
        Usuario usuarioNovo = usuarioRepository.save(usuario);
        return usuarioNovo;
    }
}
