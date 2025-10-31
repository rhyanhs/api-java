package br.com.rhyan.api_java.repository;

import br.com.rhyan.api_java.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
