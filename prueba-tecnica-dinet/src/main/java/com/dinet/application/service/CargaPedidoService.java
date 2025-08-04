package com.dinet.application.service;

import com.dinet.application.dto.ErrorRegistro;
import com.dinet.application.dto.PedidoDTO;
import com.dinet.application.dto.ResultadoCarga;
import com.dinet.domain.model.Zona;
import com.dinet.domain.service.ValidadorPedidoService;
import com.dinet.infrastructure.out.persistence.PedidoEntity;
import com.dinet.infrastructure.out.persistence.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CargaPedidoService {
    private final PedidoRepository pedidoRepository;
    private final ValidadorPedidoService validador;

    public CargaPedidoService(PedidoRepository pedidoRepository, ValidadorPedidoService validador) {
        this.pedidoRepository = pedidoRepository;
        this.validador = validador;
    }

    @Transactional
    public ResultadoCarga procesarArchivo(MultipartFile archivoCsv) {
        List<PedidoEntity> pedidosValidos = new ArrayList<>();
        List<ErrorRegistro> errores = new ArrayList<>();
        Set<String> pedidosEnBD = pedidoRepository.findAll()
                .stream().map(PedidoEntity::getNumeroPedido)
                .collect(Collectors.toSet());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(archivoCsv.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            int fila = 0;

            while ((linea = reader.readLine()) != null) {
                fila++;

                // Saltar encabezado
                if (fila == 1 && linea.toLowerCase().contains("numero")) continue;

                String[] partes = linea.split(",");

                if (partes.length != 6) {
                    errores.add(new ErrorRegistro(fila, List.of("Formato inválido: columnas insuficientes")));
                    continue;
                }

                PedidoDTO dto = new PedidoDTO(
                        partes[0].trim(),
                        partes[1].trim(),
                        partes[2].trim(),
                        partes[3].trim(),
                        partes[4].trim(),
                        Boolean.parseBoolean(partes[5].trim())
                );

                List<String> erroresFila = validador.validar(dto, pedidosEnBD);

                if (erroresFila.isEmpty()) {
                    PedidoEntity pedido = mapearADominio(dto);
                    pedidosValidos.add(pedido);
                    pedidosEnBD.add(pedido.getNumeroPedido()); // evitar duplicados en la misma carga
                } else {
                    errores.add(new ErrorRegistro(fila, erroresFila));
                }
            }

            pedidoRepository.saveAll(pedidosValidos);

        } catch (Exception e) {
            errores.add(new ErrorRegistro(0, List.of("Error procesando el archivo: " + e.getMessage())));
        }

        return new ResultadoCarga(
                pedidosValidos.size() + errores.size(),
                pedidosValidos.size(),
                agruparErrores(errores)
        );
    }

    private PedidoEntity mapearADominio(PedidoDTO dto) {
        return new PedidoEntity(
                dto.numeroPedido(),
                dto.clienteId(),
                LocalDate.parse(dto.fechaEntrega()),
                PedidoEntity.Estado.valueOf(dto.estado()),
                dto.zonaEntrega(),
                dto.requiereRefrigeracion()
        );
    }

    private Map<String, List<Integer>> agruparErrores(List<ErrorRegistro> errores) {
        Map<String, List<Integer>> agrupado = new HashMap<>();

        for (ErrorRegistro error : errores) {
            for (String motivo : error.motivos()) {
                agrupado.computeIfAbsent(motivo, k -> new ArrayList<>()).add(error.fila());
            }
        }

        return agrupado;
    }
}
