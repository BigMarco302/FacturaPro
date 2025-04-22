package com.seph_worker.worker.controller;


import com.seph_worker.worker.core.dto.WebServiceResponse;
import com.seph_worker.worker.core.entity.organizations.Organization;
import com.seph_worker.worker.model.OrganizationDTO;
import com.seph_worker.worker.service.FacturaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Log4j2
@CrossOrigin(origins = "*")
@RequestMapping("/factura")
public class FacturaController {

    private final FacturaService facturaService;

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource image = facturaService.loadImage(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG) // Puedes usar lógica para detectar tipo (ver más abajo)
                .body(image);
    }

    @GetMapping("/url")
    public WebServiceResponse getImageByOrganization(@RequestHeader Integer organizationId){
        return new WebServiceResponse(facturaService.getUrl(organizationId));
    }





    //POST--------------------------------------->
    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        String imageUrl = facturaService.saveImage(file);
        return ResponseEntity.ok(imageUrl);
    }

    @PostMapping("")
    public WebServiceResponse addOrganization(
            @RequestHeader Integer userId,
            @RequestBody OrganizationDTO organization) {
        return facturaService.addorganization(userId,organization);}
}
