package br.com.criandoapi.projeto.service;


import br.com.criandoapi.projeto.entity.User;
import br.com.criandoapi.projeto.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticatorService {

    private UserRepository userRepository;

    private String jwtSigningKey = "minhachavesecretaminhachavesecretaminhachavesecretaminhachavesecretaminhachavesecreta";

    @Autowired
    public AuthenticatorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Map<String, String> authenticateUser(User pathUser) {
        List<User> storedUsers = userRepository.findByUsername(pathUser.getUsername());
        Map<String, String> response = new HashMap<>();

        if (!storedUsers.isEmpty()) {
            for (User storedUser : storedUsers) {
                if (storedUser.getActive()) {
                    boolean passwordMatches = passwordEncoder.matches(pathUser.getPassword(), storedUser.getPassword());

                    if (passwordMatches) {
                        String token = generateToken(pathUser.getUsername());
                        response.put("message", "Autenticação bem sucedida");
                        response.put("token", token);
                        return response;
                    }
                }
            }

            response.put("message", "A senha do usuário está incorreta.");
        } else {
            response.put("message", "Usuário não encontrado");
        }
        return response;
    }


    public String generateToken(String login) {


        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}