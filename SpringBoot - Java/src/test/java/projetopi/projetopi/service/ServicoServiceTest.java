package projetopi.projetopi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import projetopi.projetopi.controller.ServicoController;
import projetopi.projetopi.dto.mappers.ServicoMapper;
import projetopi.projetopi.dto.request.ServicoCriacao;
import projetopi.projetopi.dto.response.ServicoConsulta;
import projetopi.projetopi.entity.Barbearia;
import projetopi.projetopi.entity.Barbeiro;
import projetopi.projetopi.entity.BarbeiroServico;
import projetopi.projetopi.entity.Servico;
import projetopi.projetopi.exception.RecursoNaoEncontradoException;
import projetopi.projetopi.repository.BarbeariasRepository;
import projetopi.projetopi.repository.BarbeiroRepository;
import projetopi.projetopi.repository.BarbeiroServicoRepository;
import projetopi.projetopi.repository.ServicoRepository;
import projetopi.projetopi.util.Token;
import projetopi.projetopi.repository.UsuarioRepository;
import projetopi.projetopi.util.Global;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicoServiceTest {

    @Mock
    ServicoRepository servicoRepository;

    @Mock
    BarbeariasRepository barbeariasRepository;

    @Mock
    BarbeiroRepository barbeiroRepository;

    @Mock
    Token tk;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    BarbeiroServicoRepository barbeiroServicoRepository;

    @Mock
    Global global;

    @InjectMocks
    ServicoService service;

    @Mock
    private ServicoMapper servicoMapper;

    @InjectMocks
    private ServicoController servicoController;

    @BeforeEach
    void setUp() {

        service = new ServicoService(
                barbeariasRepository,
                barbeiroRepository,
                barbeiroServicoRepository,
                global,
                new ModelMapper(),
                servicoRepository,
                tk,
                usuarioRepository,
                servicoMapper);
    }

    @Nested
    class GetAllServicos {

        @DisplayName("Buscar serviços em uma barbearia com serviços e com token válido")
        @Test
        void validTokenWithServices() {
            // Setup
            String token = "validToken";
            Integer barbeariaId = 1;
            Servico servico = new Servico();
            List<Servico> servicos = Arrays.asList(servico);
            ServicoConsulta servicoConsulta = new ServicoConsulta();
            List<ServicoConsulta> servicoConsultas = Arrays.asList(servicoConsulta);

            // Mocking
            Barbearia barbearia = new Barbearia(barbeariaId);
            when(global.getBarbeariaByToken(token)).thenReturn(barbearia);
            when(servicoRepository.findByBarbeariaId(barbeariaId)).thenReturn(servicos);
            when(servicoMapper.toDto(servicos)).thenReturn(servicoConsultas);

            // Action
            List<ServicoConsulta> result = service.getAllServicos(token);

            // Verification
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(global).getBarbeariaByToken(token);
            verify(servicoRepository).findByBarbeariaId(barbeariaId);
            verify(servicoMapper).toDto(servicos);
        }

        @DisplayName("Buscar serivços em uma barbearia sem serviços e com token válido")
        @Test
        void validTokenWithoutServices() {
            // Setup
            String token = "validToken";
            Integer barbeariaId = 70;


            // Mocking
            Barbearia barbearia = new Barbearia(barbeariaId);
            when(global.getBarbeariaByToken(token)).thenReturn(barbearia);
            when(servicoRepository.findByBarbeariaId(barbeariaId)).thenReturn(Collections.emptyList());

            // Action & Verification
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                service.getAllServicos(token);
            });

            assertEquals(204, exception.getStatusCode().value());
            verify(global).getBarbeariaByToken(token);
            verify(servicoRepository).findByBarbeariaId(barbeariaId);
        }

        @DisplayName("Buscar serivços em uma barbearia que não exista")
        @Test
        void nullBarbearia() {
            // Setup
            String token = "validToken";

            // Mocking
            when(global.getBarbeariaByToken(token)).thenReturn(null);

            // Action & Verification
            NullPointerException exception = assertThrows(NullPointerException.class, () -> {
                service.getAllServicos(token);
            });

            assertNotNull(exception);
            verify(global).getBarbeariaByToken(token);
        }
    }

    @Nested
    class getAllServicosByStatus {

        @DisplayName("Buscar todos os serviços com o status ativo")
        @Test
        void ByStatusActive() {
            // Dados de entrada
            String token = "validToken";
            String status = "active";
            Integer barbeariaId = 1;

            // Mocks de dados
            List<Servico> servicos = List.of(new Servico());
            List<ServicoConsulta> servicoConsultas = List.of(new ServicoConsulta());

            // Configuração dos mocks
            when(global.getBarbeariaByToken(token)).thenReturn(new Barbearia(barbeariaId));
            when(servicoRepository.findByBarbeariaIdAndStatus(barbeariaId, true)).thenReturn(servicos);
            when(servicoMapper.toDto(servicos)).thenReturn(servicoConsultas); // Use o mock da instância

            // Chamada ao método a ser testado
            List<ServicoConsulta> result = service.getAllServicosByStatus(token, status);

            // Verificação
            assertEquals(servicoConsultas, result);
        }

        @DisplayName("Buscar todos os serviços com o status desativo")
        @Test
        void ByStatusDeactive() {
            // Dados de entrada
            String token = "validToken";
            String status = "deactive";
            Integer barbeariaId = 1;
            List<Servico> servicos = List.of(new Servico());
            List<ServicoConsulta> servicoConsultas = List.of(new ServicoConsulta());

            // Mocks de dados
            Barbearia barbearia = new Barbearia(barbeariaId);

            // Configuração dos mocks
            when(global.getBarbeariaByToken(token)).thenReturn(barbearia);
            when(servicoRepository.findByBarbeariaIdAndStatus(barbeariaId, false)).thenReturn(servicos);
            when(servicoMapper.toDto(servicos)).thenReturn(servicoConsultas);

            // Chamada ao método a ser testado
            List<ServicoConsulta> result = service.getAllServicosByStatus(token, status);

            // Verificação
            assertEquals(servicoConsultas, result);
        }


        @Test
        void NoServices() {
            String token = "validToken";
            String status = "active";
            Integer barbeariaId = 1;

            when(global.getBarbeariaByToken(token)).thenReturn(new Barbearia(barbeariaId));
            when(servicoRepository.findByBarbeariaIdAndStatus(barbeariaId, true)).thenReturn(Collections.emptyList());

            assertThrows(ResponseStatusException.class, () -> service.getAllServicosByStatus(token, status));
        }

        @Test
        void CaseInsensitive() {
            String token = "validToken";
            String status = "ACTIVE";
            Integer barbeariaId = 1;
            List<Servico> servicos = List.of(new Servico());
            List<ServicoConsulta> servicoConsultas = List.of(new ServicoConsulta());

            when(global.getBarbeariaByToken(token)).thenReturn(new Barbearia(barbeariaId));
            when(servicoRepository.findByBarbeariaIdAndStatus(barbeariaId, true)).thenReturn(servicos);
            when(servicoMapper.toDto(servicos)).thenReturn(servicoConsultas);

            List<ServicoConsulta> result = service.getAllServicosByStatus(token, status);

            assertEquals(servicoConsultas, result);
        }
    }

    @Nested
    class getIdBarbearia {

        @DisplayName("Obter ID da barbearia a partir do token")
        @Test
        void IdBarbearia() {
            String token = "validToken";
            Integer barbeariaId = 1;

            // Mockando o comportamento esperado do global.getBarbeariaByToken(token)
            Barbearia barbeariaMock = new Barbearia();
            barbeariaMock.setId(barbeariaId);
            when(global.getBarbeariaByToken(token)).thenReturn(barbeariaMock);

            // Executando o método que queremos testar
            Integer resultado = service.getIdBarbearia(token);

            // Verificações
            assertEquals(barbeariaId, resultado);
        }
    }
}