package projetopi.projetopi.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.Usuario;
import projetopi.projetopi.repository.BarbeiroRepository;

@Component
public class Token {

    private String token;

    private final  String  tokenSecretKey = "R2x5q8tBwEzHcTfWjYn3r6u9x$B&E)H@McQfTjWnZr4t7w!z%C*F-JaNdRgUkXp";

    @Autowired
    private BarbeiroRepository barbeiroRepository;


    public String getToken(Usuario usuario) {
        try {
            long exp = System.currentTimeMillis() + (15 * 60 * 1000);

            String token = JWT.create()
                    .withClaim("id", usuario.getId().toString())
                    .withClaim("nome", usuario.getNome())
                    .withClaim("senha", usuario.getSenha())
                    .withClaim("email", usuario.getEmail())
                    .withClaim("exp", exp)
                    .sign(Algorithm.HMAC256(tokenSecretKey));

            return token;
        } catch (JWTCreationException exception) {
            System.err.println("Erro ao criar o token JWT: " + exception.getMessage());
            throw new RuntimeException("Erro ao gerar o token JWT", exception);

        } catch (Exception exception) {
            System.err.println("Erro inesperado ao gerar o token: " + exception.getMessage());
            throw new RuntimeException("Erro inesperado ao gerar o token", exception);
        }
    }

    public String getUserIdByToken(String token) {

        try{
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenSecretKey)).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("id").asString();
        }catch (JWTDecodeException e){
            throw new JWTDecodeException("Erro ao decodificar o tokeN: " +  e.getMessage());
        }

    }

    public  String getNomeByToken(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenSecretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaims().get("nome").asString();
    }

    public  void addTokenToCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setMaxAge(15);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
