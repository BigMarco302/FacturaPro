package com.seph_worker.worker.service;


import com.seph_worker.worker.core.dto.WebServiceResponse;
import com.seph_worker.worker.core.entity.RoleModuleUser.User;
import com.seph_worker.worker.core.entity.organizations.DigitalSeal;
import com.seph_worker.worker.core.entity.organizations.FiscalAddress;
import com.seph_worker.worker.core.entity.organizations.Organization;
import com.seph_worker.worker.core.exception.ResourceNotFoundException;
import com.seph_worker.worker.model.OrganizationDTO;
import com.seph_worker.worker.repository.Organizations.DigitalSealRepository;
import com.seph_worker.worker.repository.Organizations.FiscalAddressRepository;
import com.seph_worker.worker.repository.Organizations.OrganizationRepository;
import com.seph_worker.worker.repository.UserRoleModule.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Log4j2
@AllArgsConstructor
public class FacturaService {
    private OrganizationRepository organizationRepository;
    private UserRepository userRepository;
    private DigitalSealRepository digitalSealRepository;
    private FiscalAddressRepository fiscalAddressRepository;

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/images-app/uploads/";

    public String saveImage(MultipartFile file) {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR)); // Asegura que exista la carpeta

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            file.transferTo(filePath);
            System.out.println(filename);
            return "factura/image/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la imagen", e);
        }
    }

    public String getUrl(Integer organizationId){
         Organization org = organizationRepository.findById(organizationId).orElseThrow(()-> new RuntimeException("No se encontro la imagen"));

        return org.getUri().toString();
    }
    public Resource loadImage(String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se puede leer la imagen: " + filename);
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Error al cargar la imagen", e);
        }
    }


    @Transactional
    public WebServiceResponse addorganization(Integer userId,OrganizationDTO dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No se encontro el usuario"));

        Organization org = new Organization();

        org.setName(dto.getNombre());
        org.setUri(dto.getUri());
        org.setRazonSocial(dto.getRazonSocial());
        org.setRfc(dto.getRfc());
        org.setRegimenFiscal(dto.getRegimenFiscal());
        org.setFolio(dto.getFolioInicio());
        org.setSave(Boolean.TRUE);
        org.setDeleted(Boolean.FALSE);

        DigitalSeal digital = new DigitalSeal();
        digital.setPassword(dto.getPasswordKey());
        digital.setCer("prueba/92hf/");
        digital.setKey("pruebaKey/oieqhd/");
        digital.setConfig("");
        digitalSealRepository.save(digital);
        org.setDigitalSealId(digital.getId());

        FiscalAddress fiscal = new FiscalAddress();
        fiscal.setColonia(dto.getColonia());
        fiscal.setMunicipio(dto.getMunicipio());
        fiscal.setEstado(dto.getEstado());
        fiscal.setCodigo(dto.getCodigoPostal());
        fiscal.setCalle(dto.getCalle());
        fiscal.setInterior(dto.getInterior());
        fiscal.setExterior(dto.getExterior());
        fiscalAddressRepository.save(fiscal);
        org.setFiscalAddressId(fiscal.getId());

        organizationRepository.save(org);

        user.setOrganizationId(org.getId());
        userRepository.save(user);

        return new WebServiceResponse("Se guardo correctamente la organizacion");
    }

}
