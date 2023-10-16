package br.com.criandoapi.projeto.repository;

import br.com.criandoapi.projeto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);

    Optional<User> findById(Long id);

}