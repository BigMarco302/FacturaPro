package com.seph_worker.worker.controller;


import com.seph_worker.worker.core.dto.WebServiceResponse;
import com.seph_worker.worker.model.ModulosDTO;
import com.seph_worker.worker.service.ModuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
@CrossOrigin(origins = "*")
@RequestMapping("/modules")
public class ModuleController {

    private final ModuleService moduleService;

    @GetMapping("")
    public WebServiceResponse getAllModules(){
        return new WebServiceResponse(moduleService.getAllModules());
    }

    @GetMapping("/module")
    public WebServiceResponse getModule(@RequestHeader Integer moduleId){return new WebServiceResponse(moduleService.getModule(moduleId)); }


    //POST-------------------------------------------------------->
    @PostMapping("")
    public WebServiceResponse addModule(@RequestBody ModulosDTO modulosDTO){
        return (moduleService.addModule(modulosDTO));
    }

    //UPDATE-------------------------------------------------------->
    @PatchMapping("")
    public WebServiceResponse updateModule(@RequestBody ModulosDTO modulosDTO, @RequestHeader Integer moduleId){
        return (moduleService.updateModule(modulosDTO, moduleId));
    }
    @PatchMapping("/softdeleted")
    public WebServiceResponse softdeleted(@RequestHeader Integer moduleId){
        return (moduleService.softdeleted( moduleId));
    }
}
