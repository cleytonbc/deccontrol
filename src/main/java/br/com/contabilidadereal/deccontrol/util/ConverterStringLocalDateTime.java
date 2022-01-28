package br.com.contabilidadereal.deccontrol.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class ConverterStringLocalDateTime {
	
	public LocalDate transformarLocalDate(String mesAno) {
		 DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM");
		 YearMonth yearMonth = YearMonth.parse(mesAno, dateFormat);
		 LocalDate inicioMes = yearMonth.atDay(1);
		return inicioMes;
	}
	
	public LocalDateTime convertInicioMes(String mesAno) {
		LocalDateTime dataInicio = transformarLocalDate(mesAno).atTime(LocalTime.MIN);
		return dataInicio;		
	}
	
	public LocalDateTime convertFimMes(String mesAno) {
		LocalDate fimMes = transformarLocalDate(mesAno).withDayOfMonth(transformarLocalDate(mesAno).lengthOfMonth());
		LocalDateTime dataFim = fimMes.atTime(LocalTime.MAX);
		return dataFim;
	}
}
