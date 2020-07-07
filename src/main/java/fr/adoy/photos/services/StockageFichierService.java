package fr.adoy.photos.services;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import fr.adoy.photos.exception.FichierNonTrouveException;
import fr.adoy.photos.exception.FileStorageException;
import fr.adoy.photos.properties.ProprietesStockageFichiers;

@Service
public class StockageFichierService {

    private final Path cheminStockage;

    @Autowired
    public StockageFichierService(ProprietesStockageFichiers proprietesStockageFichiers) {
        this.cheminStockage = Paths.get(proprietesStockageFichiers.getDossier())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.cheminStockage);
        } catch (Exception ex) {
            throw new FileStorageException("Impossible de créer le dossier qui va contenir les fichiers.", ex);
        }
    }

    public String enregistrerFichier(MultipartFile fichier) {
        // Normalisation du nom de fichier
        String nomFichier = StringUtils.cleanPath(fichier.getOriginalFilename());

        try {
            // Check si le fichier comporte des caractères illégaux.
            if(nomFichier.contains("..")) {
                throw new FileStorageException("Désolé votre nom de fichier contient des caractères illégaux " + nomFichier);
            }

            // Copie du fichier dans le dossier destination.
            Path localisationCible = this.cheminStockage.resolve(nomFichier);
            Files.copy(fichier.getInputStream(), localisationCible, StandardCopyOption.REPLACE_EXISTING);

            return nomFichier;
        } catch (IOException ex) {
            throw new FileStorageException("Impossible d'enregistrer le fichier " + nomFichier + ". Réessayez svp", ex);
        }
    }

    public Resource chargementFichierEnTantQueRessource(String fileName) {
        try {
            Path filePath = this.cheminStockage.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FichierNonTrouveException("Fichier non trouvé " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FichierNonTrouveException("Fichier non trouvé " + fileName, ex);
        }
    }

	public List<String> getAllFichiers() {
		try {
			return Files.walk(this.cheminStockage).filter(Files::isRegularFile)
					.map(f -> f.getFileName().toString())
					.collect(Collectors.toList());
		} catch (IOException e) {
			throw new FichierNonTrouveException("Impossible de lister les fichiers dans le dossier.");
		}
	}
}