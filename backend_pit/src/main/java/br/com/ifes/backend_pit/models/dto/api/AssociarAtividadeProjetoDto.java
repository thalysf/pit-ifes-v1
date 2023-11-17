package br.com.ifes.backend_pit.models.dto.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AssociarAtividadeProjetoDto {
    List<UUID> entidades;
}
