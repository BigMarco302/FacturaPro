package com.seph_worker.worker.service;


import com.seph_worker.worker.repository.Organizations.OrganizationRepository;
import lombok.AllArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class ImageTextReaderService {

    private final OrganizationRepository organizationRepository;

    public Object getAll (){
        return organizationRepository.getAll();
    }




    public List<Map<String, String>> processImages(MultipartFile[] files) {
        List<Map<String, String>> results = new ArrayList<>();

        List<Map<String,String>> orgs = new ArrayList<>( organizationRepository.getAll());


        for (MultipartFile file : files) {
            Map<String, String> result = new HashMap<>();
            try {
                // Leer imagen
                BufferedImage original = ImageIO.read(file.getInputStream());

                // Convertir a blanco y negro
                BufferedImage processed = preprocessImage(original);

                // Inicializar Tesseract
                Tesseract tesseract = new Tesseract();
                tesseract.setDatapath("C:\\Users\\marco\\OneDrive\\Escritorio");
                tesseract.setLanguage("spa");

                // Ejecutar OCR
                String text = tesseract.doOCR(processed);

                orgs.stream().filter(f->(text.substring(1,9)).contains(f.get("words").substring(1,3)))
                        .findFirst()
                                .ifPresent(data->{
                                    result.put("name", data.get("name"));
                                    String clave = data.get("clave");
                                    if(data.get("line").equals("inline")){
                                        result.put("clave", extract(text, ""+clave+":?\\s*(\\d{3,})"));
                                    }else{
                                        result.put("clave", extractTokenFromLineBelow(text, clave));
                                    }
                                });
                result.put("nombre_imagen", file.getOriginalFilename());

                if(!result.get("clave").equals("NO_ENCONTRADO")){
                    result.putAll(organizationRepository.boletos(result.get("clave")));
                }

                // Guardar resultados
//                result.put("ticket", extract(text, "TICKET:?\\s*(\\d{3,})"));
//                result.put("token", extract(text, "TOKEN:?\\s*(\\d{3,})"));
//                result.put("token2", extractTokenFromLineBelow(text, "Token de Facturación"));

            } catch (IOException | TesseractException e) {
                result.put("error", e.getMessage());
            }
            results.add(result);
        }

        return results;
    }
    private String extractTokenFromLineBelow(String text, String keyword) {
        String[] lines = text.split("\\n");
        for (int i = 0; i < lines.length - 1; i++) {
            if (lines[i].toLowerCase().contains(keyword.toLowerCase())) {
                // Buscar un token en la línea siguiente al "Token de Facturación"
                Matcher matcher = Pattern.compile("([A-Z0-9]{6,})").matcher(lines[i + 1]);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
        }
        return "NO_ENCONTRADO";
    }

    // Preprocesar imagen para mejorar lectura OCR
    private BufferedImage preprocessImage(BufferedImage original) {
        BufferedImage grayscale = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayscale.getGraphics();
        g.drawImage(original, 0, 0, null);
        g.dispose();
        return grayscale;
    }

    // Extraer usando regex
    private String extract(String text, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(text);
        return matcher.find() ? matcher.group(1) : "NO_ENCONTRADO";
    }
}


