package br.com.rhyan.api_java.repository;

import br.com.rhyan.api_java.model.UsuarioLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioLoginRepository extends JpaRepository<UsuarioLogin, Long> {
    Optional<UsuarioLogin> findByUsuario(String usuario);

}
