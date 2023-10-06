package br.com.criandoapi.projeto.repository;

import br.com.criandoapi.projeto.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
