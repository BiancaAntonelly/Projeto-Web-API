package br.com.criandoapi.projeto.controller.rest;

import br.com.criandoapi.projeto.entity.User;
import br.com.criandoapi.projeto.service.AuthenticatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/authenticator")
public class AuthenticatorRestController {


    private AuthenticatorService authenticatorService;

    @Autowired
    public AuthenticatorRestController(AuthenticatorService authenticatorService ){
        this.authenticatorService = authenticatorService;
    }

    @PostMapping("")
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody User user) {
        Map<String, String> authResult = authenticatorService.authenticateUser(user);

        if (authResult.containsKey("token")) {
            return ResponseEntity.ok(authResult);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResult);
        }
    }
}

