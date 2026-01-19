package com.estudos.transacao_api.business.services;

import com.estudos.transacao_api.controller.dtos.EstatisticasResponseDTO;
import com.estudos.transacao_api.controller.dtos.TransacaoRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstatisticasService {

    public final TransacaoService transacaoService;

    public EstatisticasService(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    public EstatisticasResponseDTO calcularEstatisticasTransacoes(Integer intervaloBusca) {
        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes((intervaloBusca));

        DoubleSummaryStatistics estatisticasTransacoes = transacoes.stream()
                .mapToDouble(TransacaoRequestDTO::valor).summaryStatistics();
        return new EstatisticasResponseDTO(estatisticasTransacoes.getCount(),
                estatisticasTransacoes.getSum(),
                estatisticasTransacoes.getAverage(),
                estatisticasTransacoes.getMin(),
                estatisticasTransacoes.getMax());
    }
}
