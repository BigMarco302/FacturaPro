package com.seph_worker.worker.controller;


import com.seph_worker.worker.core.dto.SepomexService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/direcciones")
@RequiredArgsConstructor
public class DireccionController {



    private final SepomexService sepomexService;

    @GetMapping("/buscar")
    public String obtenerInfoCP(@RequestHeader String cp) {
        return sepomexService.consultarCodigoPostal(cp);
    }
}
