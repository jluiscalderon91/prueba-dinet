package com.dinet.application.dto;

import java.util.List;
import java.util.Map;

public record ResultadoCarga(
        int totalProcesados,
        int guardados,
        Map<String, List<Integer>> erroresAgrupados
) {}
