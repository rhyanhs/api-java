package br.com.rhyan.api_java.controller;

import br.com.rhyan.api_java.model.Usuario;
import br.com.rhyan.api_java.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")

public class UsuarioController {

    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Usuario> Listar() {
    return repository.findAll();
}

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id){
        Optional<Usuario> usuario = repository.findById(id);
        if(usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody @Valid Usuario usuario){
        Usuario salvo = repository.save(usuario);
        return ResponseEntity.status(201).body(salvo);
}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        if(repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody @Valid Usuario dados){
        Optional<Usuario> usuarioExistente = repository.findById(id);

        if(usuarioExistente.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioExistente.get();
        usuario.setNome(dados.getNome());
        usuario.setIdade(dados.getIdade());

        Usuario atualizado = repository.save(usuario);
        return ResponseEntity.ok(atualizado);
    }

}
