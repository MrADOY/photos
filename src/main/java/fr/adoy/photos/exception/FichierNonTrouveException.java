package fr.adoy.photos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Note that, we’ve annotated the above exception class with @ResponseStatus(HttpStatus.NOT_FOUND). 
 * This ensures that Spring boot responds with a 404 Not Found status when this exception is thrown.
 * @author MrADOY
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FichierNonTrouveException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FichierNonTrouveException(String message) {
        super(message);
    }

    public FichierNonTrouveException(String message, Throwable cause) {
        super(message, cause);
    }
}