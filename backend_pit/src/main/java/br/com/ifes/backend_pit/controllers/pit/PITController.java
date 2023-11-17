package br.com.ifes.backend_pit.controllers.pit;

import br.com.ifes.backend_pit.models.dto.api.RequestPITDto;
import br.com.ifes.backend_pit.models.pit.PIT;
import br.com.ifes.backend_pit.models.pit.RevisaoDto;
import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.services.pit.PITService;
import br.com.ifes.backend_pit.services.usuarios.ServidorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/pit")
@RequiredArgsConstructor
@Validated
public class PITController {

    private final PITService pitService;
    private final ServidorService servidorService;


    @GetMapping("{idPit}")
    public PIT obterPit(@PathVariable UUID idPit){
        return this.pitService.getPitById(idPit);
    }

    @GetMapping()
    public List<PIT> obterPitsProfessor(@RequestParam UUID idUsuario){
        Servidor servidor = servidorService.getServidorByUserId(idUsuario);
        return this.pitService.obterPitsProfessor(servidor);
    }

    @GetMapping("emRevisao")
    public List<PIT> obterPitsEmRevisao(){
        return this.pitService.obterPitsEmRevisao();
    }

    @GetMapping("aprovados")
    public List<PIT> obterPitsAprovados(){
        return this.pitService.obterPitsAprovados();
    }

    @GetMapping("{idUsuario}/em_aberto")
    public Optional<PIT> getPitEmAberto(@PathVariable UUID idUsuario){
        return this.pitService.getPitEmAberto(idUsuario);
    }

    @PostMapping("{idUsuario}/inicializar")
    public PIT iniciarPITProfessor(@PathVariable UUID idUsuario, @Valid @RequestBody RequestPITDto requestPITDto) {
        return this.pitService.iniciarPITProfessor(idUsuario, requestPITDto);
    }

    @PostMapping("{idPit}/encerrar")
    public void encerrar(@PathVariable UUID idPit){
        this.pitService.encerrar(idPit);
    }

    @PostMapping("{idPit}/enviar_revisao")
    public void enviarParaRevisao(@PathVariable UUID idPit){
        this.pitService.enviarParaRevisao(idPit);
    }

    @GetMapping("{idPit}/totalHoras")
    public Double getTotalHoras(@PathVariable UUID idPit){
        return this.pitService.getTotalHoras(idPit);
    }

    @PreAuthorize("hasAuthority('DIRETOR')")
    @PostMapping("{idPit}/pedirAlteracoes")
    public void pedirAlteracoes(@PathVariable UUID idPit, @RequestBody RevisaoDto revisaoDto){
        this.pitService.pedirAlteracoes(idPit, revisaoDto.getTexto());
    }

    @PreAuthorize("hasAuthority('DIRETOR')")
    @PostMapping("{idPit}/aprovar")
    public void aprovar(@PathVariable UUID idPit){
        this.pitService.aprovar(idPit);
    }
}
