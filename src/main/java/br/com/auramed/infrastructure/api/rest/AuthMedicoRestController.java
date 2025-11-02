package br.com.auramed.infrastructure.api.rest;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.AuthMedico;
import br.com.auramed.domain.repository.AuthMedicoRepository;
import br.com.auramed.domain.service.AuthenticationService;
import br.com.auramed.domain.service.PasswordService;
import br.com.auramed.interfaces.controllers.AuthMedicoController;
import br.com.auramed.interfaces.dto.request.AuthMedicoRequestDTO;
import br.com.auramed.interfaces.dto.response.AuthMedicoResponseDTO;
import br.com.auramed.interfaces.dto.request.LoginRequestDTO;
import br.com.auramed.interfaces.dto.response.LoginResponseDTO;
import br.com.auramed.interfaces.mappers.MedicoMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;

@Path("/auth/medicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthMedicoRestController {
    private final AuthMedicoController authMedicoController;
    private final AuthenticationService authenticationService;
    private final PasswordService passwordService;
    private final MedicoMapper medicoMapper;
    private final AuthMedicoRepository authMedicoRepository;
    @Inject
    public AuthMedicoRestController(AuthMedicoController authMedicoController,
                                    AuthenticationService authenticationService,
                                    PasswordService passwordService,
                                    MedicoMapper medicoMapper,
                                    AuthMedicoRepository authMedicoRepository) {
        this.authMedicoController = authMedicoController;
        this.authenticationService = authenticationService;
        this.passwordService = passwordService;
        this.medicoMapper = medicoMapper;
        this.authMedicoRepository = authMedicoRepository;
    }

    @POST
    @Path("/login")
    public Response login(LoginRequestDTO loginRequest) {
        try {
            System.out.println("=== LOGIN COMPLETO ===");

            String token = authMedicoController.validarCredenciais(
                    loginRequest.getEmail(),
                    loginRequest.getSenha()
            );

            System.out.println("DEBUG: Token: " + token);
            var medico = authenticationService.obterMedicoPorToken(token);
            System.out.println("DEBUG: Médico: " + medico);

            LoginResponseDTO response = new LoginResponseDTO();
            response.setToken(token);
            response.setTipoToken("Bearer");
            response.setDataExpiracao(LocalDateTime.now().plusHours(24));
            response.setMedico(medicoMapper.toResponseDTO(medico));

            return Response.ok(response).build();

        } catch (Exception e) {
            System.out.println("DEBUG: ERRO: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Credenciais inválidas").build();
        }
    }

    @POST
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") String authorization) {
        try {
            if (authorization != null && authorization.startsWith("Bearer ")) {
                String token = authorization.substring(7);
                authMedicoController.logout(token);
            }
            return Response.ok("Logout realizado com sucesso").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/hash")
    public Response hashPassword(String senha) {
        try {
            String senhaHash = passwordService.hashPassword(senha);
            return Response.ok(senhaHash).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Integer id) {
        try {
            AuthMedicoResponseDTO authMedico = authMedicoController.getAuthMedicoById(id);
            return Response.ok(authMedico).build();
        } catch (EntidadeNaoLocalizadaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/email/{email}")
    public Response buscarPorEmail(@PathParam("email") String email) {
        try {
            AuthMedicoResponseDTO authMedico = authMedicoController.getAuthMedicoByEmail(email);
            return Response.ok(authMedico).build();
        } catch (EntidadeNaoLocalizadaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/medico/{medicoId}")
    public Response buscarPorMedicoId(@PathParam("medicoId") Integer medicoId) {
        try {
            AuthMedicoResponseDTO authMedico = authMedicoController.getAuthMedicoByMedicoId(medicoId);
            return Response.ok(authMedico).build();
        } catch (EntidadeNaoLocalizadaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response buscarTodosAuthMedicos() {
        try {
            List<AuthMedicoResponseDTO> authMedicos = authMedicoController.getAllAuthMedicos();
            return Response.ok(authMedicos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response criarAuthMedico(AuthMedicoRequestDTO authMedicoRequest) {
        try {
            AuthMedicoResponseDTO authMedico = authMedicoController.criarAuthMedico(authMedicoRequest);
            return Response.status(Response.Status.CREATED).entity(authMedico).build();
        } catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarAuthMedico(@PathParam("id") Integer id, AuthMedicoRequestDTO authMedicoRequest) {
        try {
            AuthMedicoResponseDTO authMedico = authMedicoController.editarAuthMedico(id, authMedicoRequest);
            return Response.status(Response.Status.OK).entity(authMedico).build();
        } catch (EntidadeNaoLocalizadaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarAuthMedico(@PathParam("id") Integer id) {
        try {
            authMedicoController.deleteAuthMedico(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (EntidadeNaoLocalizadaException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/{id}/senha")
    public Response atualizarSenha(@PathParam("id") Integer id, String novaSenha) {
        try {
            AuthMedicoResponseDTO authMedico = authMedicoController.atualizarSenha(id, novaSenha);
            return Response.ok(authMedico).build();
        } catch (EntidadeNaoLocalizadaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/{id}/bloquear")
    public Response bloquearConta(@PathParam("id") Integer id) {
        try {
            AuthMedicoResponseDTO authMedico = authMedicoController.bloquearConta(id);
            return Response.ok(authMedico).build();
        } catch (EntidadeNaoLocalizadaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/{id}/ativar")
    public Response ativarConta(@PathParam("id") Integer id) {
        try {
            AuthMedicoResponseDTO authMedico = authMedicoController.ativarConta(id);
            return Response.ok(authMedico).build();
        } catch (EntidadeNaoLocalizadaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/validar-token")
    public Response validarToken(@HeaderParam("Authorization") String authorization) {
        try {
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Token não fornecido").build();
            }

            String token = authorization.substring(7);
            boolean tokenValido = authMedicoController.validarToken(token);

            if (tokenValido) {
                return Response.ok("Token válido").build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Token inválido ou expirado").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/debug-login")
    public Response debugLogin(LoginRequestDTO loginRequest) {
        try {
            System.out.println("=== DEBUG LOGIN ===");
            System.out.println("Email: " + loginRequest.getEmail());
            System.out.println("Senha: " + loginRequest.getSenha());

            AuthMedico authMedico = authMedicoRepository.buscarPorEmail(loginRequest.getEmail());
            System.out.println("Auth encontrado: " + (authMedico != null));
            assert authMedico != null;
            System.out.println("Hash no banco: " + authMedico.getSenhaHash());
            System.out.println("Conta ativa: " + authMedico.getContaAtiva());

            boolean senhaCorreta = passwordService.checkPassword(loginRequest.getSenha(), authMedico.getSenhaHash());
            System.out.println("Senha correta: " + senhaCorreta);

            if (senhaCorreta) {
                return Response.ok("DEBUG: Senha CORRETA").build();
            } else {
                return Response.ok("DEBUG: Senha INCORRETA").build();
            }

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("DEBUG ERROR: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/test-bcrypt")
    public Response testBcrypt(String senha) {
        try {
            AuthMedico authMedico = authMedicoRepository.buscarPorEmail("joao.silva@hospital.com");
            String hashNoBanco = authMedico.getSenhaHash();

            boolean resultadoDirect = org.mindrot.jbcrypt.BCrypt.checkpw(senha, hashNoBanco);
            boolean resultadoService = passwordService.checkPassword(senha, hashNoBanco);

            String response = "Senha testada: " + senha +
                    "\nHash no banco: " + hashNoBanco +
                    "\nBCrypt direto: " + resultadoDirect +
                    "\nPasswordService: " + resultadoService +
                    "\nHashes iguais: " + hashNoBanco.equals(passwordService.hashPassword(senha));

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro: " + e.getMessage()).build();
        }
    }
}