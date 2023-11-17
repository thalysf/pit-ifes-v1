package br.com.ifes.backend_pit.controllers.usuarios;

import br.com.ifes.backend_pit.models.usuarios.Servidor;
import br.com.ifes.backend_pit.services.usuarios.ServidorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/servidores")
@RequiredArgsConstructor
public class ServidorController {

    private final ServidorService servidorService;

    @GetMapping
    public List<Servidor> getServidores() {
        return this.servidorService.getServidores();
    }

    @GetMapping(value = "{id}")
    public Servidor getServidor(@PathVariable UUID id) {
        return this.servidorService.getServidor(id);
    }

    @GetMapping(value = "usuario/{userId}")
    public Servidor getServidorByUserId(@PathVariable UUID userId) {
        return this.servidorService.getServidorByUserId(userId);
    }

    @PostMapping
    public Servidor createServidor(@Valid @RequestBody Servidor servidor) {
        return this.servidorService.saveServidor(servidor);
    }

    @PutMapping(value = "{id}")
    public Servidor atualizarServidor(@PathVariable UUID id, @Valid @RequestBody Servidor servidor) {
        servidor.setIdServidor(id);
        return this.servidorService.updateServidor(servidor);
    }

    @DeleteMapping(value = "{id}")
    public void deletarServidor(@PathVariable UUID id) {
        this.servidorService.deleteServidor(id);
    }
}
