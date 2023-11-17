package br.com.ifes.backend_pit.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeOperations {
    public static final Double tratarSomatorioCargaHoraria(Double cargaHoraria1, Double cargaHoraria2){
        if(cargaHoraria1 == null) cargaHoraria1 = 0D;
        if(cargaHoraria2 == null) cargaHoraria2 = 0D;

        Double resultado = Double.sum(cargaHoraria1, cargaHoraria2);
        resultado = TimeOperations.formatDoubleDuasCasas(resultado);

        Double valorInteiro = Double.valueOf(resultado.intValue());
        Integer valorQuebrado = Double.valueOf ((TimeOperations.formatDoubleDuasCasas((resultado - valorInteiro) * 100))).intValue();

        if(valorQuebrado >= 60){
            valorInteiro ++;
            valorQuebrado = valorQuebrado - 60;
        }

        return Double.valueOf(valorInteiro + TimeOperations.formatDoubleDuasCasas(Double.valueOf(valorQuebrado) / 100));
    }

    public static final Double formatDoubleDuasCasas(Double numero) {
        // Usando String.format para formatar com duas casas decimais
        String resultadoFormatado = String.format("%.2f", numero); // Saída: 2.60

        return Double.parseDouble(resultadoFormatado.replace(',', '.'));
    }

    public static final String formatDoubleToDateString(Double numero){
        Integer valorInteiro = numero.intValue();
        Integer valorQuebrado = Double.valueOf ((TimeOperations.formatDoubleDuasCasas((numero - valorInteiro) * 100))).intValue();

        String horas = valorInteiro > 9 ? valorInteiro.toString() : "0" + valorInteiro;
        String minutos = valorQuebrado > 9 ? valorQuebrado.toString() : "0" + valorQuebrado;

        return horas + ":" + minutos;
    }

    public static final String formatDateString(Timestamp data){
        Date date = new Date(data.getTime());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}
