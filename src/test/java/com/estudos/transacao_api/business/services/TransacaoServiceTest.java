package com.estudos.transacao_api.business.services;

import com.estudos.transacao_api.controller.dtos.EstatisticasResponseDTO;
import com.estudos.transacao_api.controller.dtos.TransacaoRequestDTO;
import com.estudos.transacao_api.infraestructure.excepions.UnprocessableEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {
    @InjectMocks
    TransacaoService transacaoService;

    TransacaoRequestDTO transacao;

    EstatisticasResponseDTO estatisticas;
    @BeforeEach
    void setUp() {
        transacao = new TransacaoRequestDTO(20.0, OffsetDateTime.now());
        estatisticas = new EstatisticasResponseDTO(1L, 20.0, 20.0, 20.0, 20.0);
    }

    @Test
    void DeveAdicionarTransacaoComSucesso(){
        transacaoService.adicionarTrasacoes(transacao);

        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(5000);
        assertTrue(transacoes.contains(transacao));
    }

    @Test
    void DeveLancarExcecaoCasoValorSejaNegativo(){
        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class,
                () -> transacaoService.adicionarTrasacoes(new TransacaoRequestDTO(-10.0, OffsetDateTime.now())));

        assertEquals("Valor não pode ser menor que 0", exception.getMessage());

    }

    @Test
    void DeveLancarExcecaoCasoDataOuHoraMaiorQueAtual(){
        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class,
                () -> transacaoService.adicionarTrasacoes(new TransacaoRequestDTO(10.0, OffsetDateTime.now().plusDays(1))));

        assertEquals("Data e hora maiores que a data e hora atuais", exception.getMessage());
    }

    @Test
    void DeveLimparTransacaoComSucesso(){
        transacaoService.limparTransacoes();

        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(5000);
        assertTrue(transacoes.isEmpty());
    }

    @Test
    void DeveBuscarDentroDoIntervalo() {
        TransacaoRequestDTO dto = new TransacaoRequestDTO(10.0, OffsetDateTime.now().minusHours(1));
        transacaoService.adicionarTrasacoes(transacao);
        transacaoService.adicionarTrasacoes(dto);
        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(60);

        assertTrue(transacoes.contains(transacao));
        assertFalse(transacoes.contains(dto));
    }
}
