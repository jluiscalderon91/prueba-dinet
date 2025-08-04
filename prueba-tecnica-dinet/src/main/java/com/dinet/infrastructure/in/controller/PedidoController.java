package com.dinet.infrastructure.in.controller;

import com.dinet.application.dto.ResultadoCarga;
import com.dinet.application.service.CargaPedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final CargaPedidoService cargaPedidoService;

    public PedidoController(CargaPedidoService cargaPedidoService) {
        this.cargaPedidoService = cargaPedidoService;
    }

    @PostMapping("/cargar")
    public ResponseEntity<ResultadoCarga> cargarPedidos(@RequestParam("file") MultipartFile file) {
        ResultadoCarga resultado = cargaPedidoService.procesarArchivo(file);
        if (resultado.guardados() > 0) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
        }
    }
}
