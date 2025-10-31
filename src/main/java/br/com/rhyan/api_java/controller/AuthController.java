package br.com.rhyan.api_java.controller;

import br.com.rhyan.api_java.model.UsuarioLogin;
import br.com.rhyan.api_java.repository.UsuarioLoginRepository;
import br.com.rhyan.api_java.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioLoginRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioLoginRepository repository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    //registrar novo usuario
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsuarioLogin user){
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        repository.save(user);
        return ResponseEntity.ok(Map.of("message", "Usuário registrado com sucesso!"));
    }

    //login e geração do token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioLogin user){
        Optional<UsuarioLogin> usuario = repository.findByUsuario(user.getUsuario());

        if(usuario.isEmpty() || !passwordEncoder.matches(user.getSenha(),usuario.get().getSenha())){
            return ResponseEntity.status(401).body(Map.of("error", "Usuário ou senha inválidos"));
        }
        String token = jwtUtil.gerarToken(user.getUsuario());
        return ResponseEntity.ok(Map.of("token",token));
    }
}
