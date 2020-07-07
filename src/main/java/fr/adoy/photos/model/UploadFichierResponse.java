package fr.adoy.photos.model;

public class UploadFichierResponse {
    private String nomFichier;
    private String urlDownload;
    private String typeFichier;
    private long fichier;
    
	public UploadFichierResponse(String nomFichier, String urlDownload, String typeFichier, long fichier) {
		super();
		this.nomFichier = nomFichier;
		this.urlDownload = urlDownload;
		this.typeFichier = typeFichier;
		this.fichier = fichier;
	}
	
	public String getNomFichier() {
		return nomFichier;
	}
	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}
	public String getUrlDownload() {
		return urlDownload;
	}
	public void setUrlDownload(String urlDownload) {
		this.urlDownload = urlDownload;
	}
	public String getTypeFichier() {
		return typeFichier;
	}
	public void setTypeFichier(String typeFichier) {
		this.typeFichier = typeFichier;
	}
	public long getFichier() {
		return fichier;
	}
	public void setFichier(long fichier) {
		this.fichier = fichier;
	}

  
    
}