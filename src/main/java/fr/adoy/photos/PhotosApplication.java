package fr.adoy.photos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import fr.adoy.photos.properties.ProprietesStockageFichiers;

@SpringBootApplication
@EnableConfigurationProperties({
	ProprietesStockageFichiers.class
})
public class PhotosApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotosApplication.class, args);
	}

}
