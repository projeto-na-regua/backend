package projetopi.projetopi.controle;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetopi.projetopi.dominio.Servico;
import projetopi.projetopi.dto.request.CadastroServico;
import projetopi.projetopi.dto.response.InfoServico;
import projetopi.projetopi.repositorio.BarbeariasRepository;
import projetopi.projetopi.repositorio.BarbeiroRepository;
import projetopi.projetopi.repositorio.ServicoRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoRepository servicoRepository;


    @Autowired
    private BarbeariasRepository barbeariasRepository;


    @Autowired
    private BarbeiroRepository barbeiroRepository;


    @GetMapping("/{fkBarbearia}")
    public ResponseEntity<List<InfoServico>> getServicos(@PathVariable Integer fkBarbearia){

        if (!barbeariasRepository.existsById(fkBarbearia)){
            return status(404).build();
        }

        List<InfoServico> servicos = servicoRepository.findByInfoServicoBarbearia(fkBarbearia);
        return servicos.isEmpty() ? status(204).build() : status(200).body(servicos);
    }


    @GetMapping("/especifico/{id}")
    public ResponseEntity<List<InfoServico>> getServico(@PathVariable Integer id){
        List<InfoServico> servicos = servicoRepository.findByInfoServico(id);
        return servicos.isEmpty() ? status(204).build() : status(200).body(servicos);

    }


    @PostMapping("/{fkBarbearia}")
    public ResponseEntity<CadastroServico> cadastrarServivco(@PathVariable Integer fkBarbearia,  @Valid @RequestBody CadastroServico nvServico){

        if (!barbeiroRepository.existsById(nvServico.getFkBarbeiro())){
            return status(404).build();
        }

        Servico servico = nvServico.gerarServico();
        servico.setBarbearia(barbeariasRepository.getReferenceById(fkBarbearia));
        servico.setBarbeiro(barbeiroRepository.getReferenceById(nvServico.getFkBarbeiro()));

        servicoRepository.save(servico);
        return status(201).body(nvServico);
    }

    @PutMapping("/{fkBarbearia}/{idServico}")
    public ResponseEntity<Servico> editarServivco(@PathVariable Integer fkBarbearia,
                                                     @PathVariable Integer idServico,
                                                     @Valid @RequestBody CadastroServico nvServico){

        if (!barbeariasRepository.existsById(fkBarbearia)){
            return status(404).build();
        }

        if (!barbeiroRepository.existsById(nvServico.getFkBarbeiro())){
            return status(404).build();
        }

        if (!servicoRepository.existsById(idServico)){
            return status(404).build();
        }

        Servico servico = nvServico.gerarServico();
        servico.setBarbearia(barbeariasRepository.getReferenceById(fkBarbearia));
        servico.setBarbeiro(barbeiroRepository.getReferenceById(nvServico.getFkBarbeiro()));
        servico.setId(idServico);
        servicoRepository.save(servico);
        return status(201).body(servico);
    }


    @DeleteMapping("/{fkBarbearia}/{idServico}")
    public ResponseEntity deletarServico(@PathVariable Integer fkBarbearia, @PathVariable Integer idServico){

        if (!barbeariasRepository.existsById(fkBarbearia)){
            return status(404).build();
        }


        if (!servicoRepository.existsById(idServico)){
            return status(404).build();
        }

        servicoRepository.deleteById(idServico);
        return status(200).build();
    }



}
