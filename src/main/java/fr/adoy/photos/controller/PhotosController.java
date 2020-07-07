package fr.adoy.photos.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.adoy.photos.model.UploadFichierResponse;
import fr.adoy.photos.services.StockageFichierService;

@RestController
public class PhotosController {

	private static final Logger logger = LoggerFactory.getLogger(PhotosController.class);

    @Autowired
    private StockageFichierService stockageFichierService;
    
    @PostMapping("/upload-fichier")
    public UploadFichierResponse uploadFile(@RequestParam("fichier") MultipartFile fichier) {
        // On enregistre le fichier.
    	String fileName = stockageFichierService.enregistrerFichier(fichier);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(fileName)
                .toUriString();

        return new UploadFichierResponse(fileName, fileDownloadUri,
                fichier.getContentType(), fichier.getSize());
    }
    
    @PostMapping("/upload-multiple-files")
    public List<UploadFichierResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/download-file/{nomFichier:.+}")
    public ResponseEntity<Resource> downloadFichier(@PathVariable String nomFichier, HttpServletRequest request) {
        // Chargement des fichiers en tant que ressource
        Resource resource = stockageFichierService.chargementFichierEnTantQueRessource(nomFichier);

        // Determination du type de fichier
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Impossible de déterminer le type de fichier.");
        }

        // Defaut content type
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    @GetMapping("files")
    public ResponseEntity<List<String>> downloadAllFichiers() {
		return ResponseEntity.ok(this.stockageFichierService.getAllFichiers());
    }
}
