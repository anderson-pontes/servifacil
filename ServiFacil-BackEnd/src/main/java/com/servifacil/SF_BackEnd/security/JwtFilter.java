package com.servifacil.SF_BackEnd.security;

import com.servifacil.SF_BackEnd.repositories.UserRepository;
import com.servifacil.SF_BackEnd.responses.EntityResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Endpoints públicos
        if (path.equals("/api/users/login") || path.equals("/api/users/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token não fornecido");
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtUtil.validateToken(token)) {
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado");
                return;
            }

            String email = jwtUtil.extractEmail(token);
            var user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Usuário não encontrado para o token");
                return;
            }

            // Adiciona ID e tipo no request
            request.setAttribute("userId", user.getUserId());
            request.setAttribute("userType", user.getUserType().name());

            // Se não for profissional → 403
//            if (!"PROFISSIONAL".equalsIgnoreCase(user.getUserType().name())) {
//                sendError(response, HttpServletResponse.SC_FORBIDDEN, "Acesso permitido apenas a usuários profissionais");
//                return;
//            }

            // Configura autenticação
            List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + user.getUserType().name().toUpperCase())
            );
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authToken);

            // ✅ Continua a cadeia apenas se tudo der certo
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Erro ao processar token");
        }
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}

//@Override
//protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//        throws ServletException, IOException {
//
//    String path = request.getServletPath();
//
//    // ignora endpoints públicos
//    if (path.equals("/api/users/login") || path.equals("/api/users/register")) {
//        filterChain.doFilter(request, response);
//        return;
//    }
//
//    try {
//        String authHeader = request.getHeader("Authorization");
//        String token = null;
//        String email = null;
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            token = authHeader.substring(7);
//
//            try {
//                email = jwtUtil.extractEmail(token);
//            } catch (Exception e) {
//                System.out.println("Erro ao extrair email do token: " + e.getMessage());
//            }
//
//            try {
//                int userId = jwtUtil.extractId(token);
//                request.setAttribute("userId", userId); // agora o id estará disponível na Controller
//            } catch (Exception e) {
//                System.out.println("Erro ao extrair id do token: " + e.getMessage());
//            }
//
//            try {
//                String userType = jwtUtil.extractUserType(token);
//                request.setAttribute("userType", userType);
//            } catch (Exception e) {
//                System.out.println("Erro ao extrair userType do token: " + e.getMessage());
//            }
//        }
//
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            if (jwtUtil.validateToken(token)) {
//                var user = userRepository.findByEmail(email).orElse(null);
//
//                if (user != null) {
//                    // Configura roles do usuário
//                    List<SimpleGrantedAuthority> authorities = List.of(
//                            new SimpleGrantedAuthority("ROLE_" + user.getUserType().name())
//                    );
//
//                    UsernamePasswordAuthenticationToken authToken =
//                            new UsernamePasswordAuthenticationToken(user, null, authorities);
//
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//                else {
//                    // Token inválido → 401 Unauthorized
//                    response.setContentType("application/json");
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    response.getWriter().write("{\"error\": \"Token inválido ou expirado\"}");
//                    return;
//                }
//            }
//        }
//
//    } catch (Exception e) {
//        System.out.println("Erro no JwtFilter: " + e.getMessage());
//    }
//
//    filterChain.doFilter(request, response);
//}

//@Override
//protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//        throws ServletException, IOException {
//
//    String path = request.getServletPath();
//
//    if (path.equals("/api/users/login") || path.equals("/api/users/register")) {
//        filterChain.doFilter(request, response);
//        return;
//    }
//
//    try {
//        String authHeader = request.getHeader("Authorization");
//        String token = null;
//        String email = null;
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            token = authHeader.substring(7);
//
//            try {
//                email = jwtUtil.extractEmail(token);
//            } catch (Exception e) {
//                response.setContentType("application/json");
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("{\"error\":\"Token inválido ou expirado\"}");
//                return;
//            }
//        } else {
//            // Sem token → 401 Unauthorized
//            response.setContentType("application/json");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("{\"error\":\"Token não fornecido\"}");
//            return;
//        }
//
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            if (jwtUtil.validateToken(token)) {
//                var user = userRepository.findByEmail(email).orElse(null);
//
//                if (user == null) {
//                    response.setContentType("application/json");
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    response.getWriter().write("{\"error\":\"Token inválido ou expirado\"}");
//                    return;
//                }
//
//                String userType = jwtUtil.extractUserType(token);
//                if (!"Profissional".equalsIgnoreCase(userType)) {
//                    // Sem permissão → 403 Forbidden
//                    response.setContentType("application/json");
//                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                    response.getWriter().write("{\"error\":\"Não autorizado a acessar essa rota\"}");
//                    return;
//                }
//
//                List<SimpleGrantedAuthority> authorities = List.of(
//                        new SimpleGrantedAuthority("ROLE_" + userType.toUpperCase())
//                );
//
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(user, null, authorities);
//
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            } else {
//                response.setContentType("application/json");
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("{\"error\":\"Token inválido ou expirado\"}");
//                return;
//            }
//        }
//
//        filterChain.doFilter(request, response);
//
//    } catch (Exception e) {
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("{\"error\":\"Erro ao processar token\"}");
//    }
//}