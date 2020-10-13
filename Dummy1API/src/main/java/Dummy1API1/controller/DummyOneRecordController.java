package Dummy1API1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Dummy1API1.model.OneRecordDummyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Map;

@RestController
//@Validated
@Tag(name = "foobar", description = "the foobar API with documentation annotations")
public class DummyOneRecordController extends BaseController {
    
    @Operation(summary = "The Dummy 1R API - it does: Validate TLS Client Certificate, validate IDToken and check if IAP is trusted")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "IAP is trusted", content = { 
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OneRecordDummyResponse.class)))}), 
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "401", description = "IAP is not trusted", content = @Content),
            @ApiResponse(responseCode = "500", description = "Have error", content = @Content)})
    @GetMapping("/api/DummyOneRecord")
    ResponseEntity<Object> get(@RequestHeader Map<String, String> headers, Principal principal) {
    	
    	try {
    		
    		X509Certificate clientCert =  getClientCertificate(principal);
    		
    		// Validate TLS Client Certificate
            validateTLSClientCertificate(clientCert);
            
            // Validate IDToken and Check if IAP is trsuted
            validateIDTokenAndSignature(headers);
            
    		return processRequest(new OneRecordDummyResponse(clientCert));
		} catch (Exception e) {
			return processException(e);
		}
    }
    
}
