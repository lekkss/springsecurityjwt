package com.lekkss.springsecurityjwt.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lekkss.springsecurityjwt.dto.ReqRes;
import com.lekkss.springsecurityjwt.entity.Users;
import com.lekkss.springsecurityjwt.repository.UserRepository;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    public ReqRes signup(ReqRes registerationRequest) {
        ReqRes resp = new ReqRes();
        try {
            Users users = new Users();
            users.setEmail(registerationRequest.getEmail());
            users.setPassword(passwordEncoder.encode(registerationRequest.getPassword()));
            users.setRole(registerationRequest.getRole());

            Users userResult = userRepository.save(users);
            if (userResult != null && userResult.getId() > 0) {
                resp.setUser(userResult);
                resp.setMessage("User saved successfully");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes signin(ReqRes signinRequest) {
        ReqRes resp = new ReqRes();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
            var user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow();

            System.out.println("USER IS: " + user);

            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            resp.setToken(jwt);
            resp.setRefreshToken(refreshToken);
            resp.setMessage("User signed in successfully");
            resp.setStatusCode(200);
            resp.setExpirationTime("24Hr");
        } catch (

        Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes resp = new ReqRes();
        String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        Users users = userRepository.findByEmail(email).orElseThrow();
        if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            resp.setToken(jwt);
            resp.setRefreshToken(refreshTokenRequest.getToken());
            resp.setMessage("Successfully refreshed token");
            resp.setStatusCode(200);
            resp.setExpirationTime("24Hr");
        }
        resp.setStatusCode(500);
        return resp;
    }
}
