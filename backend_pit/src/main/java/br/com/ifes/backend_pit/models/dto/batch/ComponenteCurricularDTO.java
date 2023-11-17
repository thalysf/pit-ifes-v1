package br.com.ifes.backend_pit.models.dto.batch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponenteCurricularDTO {
    private String curso;
    private String componenteCurricular;
    private Double cargaHoraria;
}
