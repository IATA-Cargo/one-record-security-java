package Dummy1API1.controller;


import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import Dummy1API1.config.*;
import Dummy1API1.model.TrustOidcIP;
import util.base.ApiResponseBase;
import util.base.Common;
import util.certificate.OcspUtils;
import util.openidconnect.*;



public class BaseController {
	
	@Value("${ocspCacheDir}") 
	private String cacheFolder;

	protected ResponseEntity<Object> processRequest(Object data)
    {
		return new ResponseEntity<Object>(data, HttpStatus.OK);
    }
	
	protected ResponseEntity<Object> processException(Exception ex)
    {
		ApiResponseBase resp = new ApiResponseBase();
		resp.setDetails(ex.getMessage());
		if (ex instanceof  UnauthorizedException)
        {
			resp.setCode(HttpStatus.UNAUTHORIZED.value());
			resp.setMessage(HttpStatus.UNAUTHORIZED.toString());
			
        	return new ResponseEntity<Object>(resp, HttpStatus.UNAUTHORIZED);
        }
        else if (ex instanceof BadRequestException)
        {
        	resp.setCode(HttpStatus.BAD_REQUEST.value());
			resp.setMessage(HttpStatus.BAD_REQUEST.toString());
			
        	return new ResponseEntity<Object>(resp, HttpStatus.BAD_REQUEST);
        }
        
		resp.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		resp.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		
		return new ResponseEntity<Object>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	protected void validateTLSClientCertificate(X509Certificate clientCert)
    {
		if (clientCert == null)
        {
            throw new UnauthorizedException("1R TLS Client Certificate for Subcriber is required.");
        }
        
        String certStatus = "";
        try {
			certStatus = (new OcspUtils(cacheFolder)).validate(clientCert);
		} catch (Exception e) {
			certStatus = e.getMessage();
		}
        if (certStatus != "Good")
        {
            throw new UnauthorizedException("Client certificate is + " + certStatus);
        }
        
    }
	
	protected X509Certificate getClientCertificate(Principal principal) {
		
		X509Certificate x509 = null;
		Certificate cert = null;
		try {
			if (principal == null) return x509;
			
			if (!(principal instanceof PreAuthenticatedAuthenticationToken)) return x509;
			
			PreAuthenticatedAuthenticationToken pre = (PreAuthenticatedAuthenticationToken)principal; 
			
			if (!(pre.getCredentials() instanceof Certificate)) return x509;
			
			cert = (Certificate)pre.getCredentials();
			
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
		    ByteArrayInputStream bais = new ByteArrayInputStream(cert.getEncoded());
		    x509 = (X509Certificate) cf.generateCertificate(bais);
		} catch (Exception e) {
			
		}
		
		return x509;
	}

	protected void validateIDTokenAndSignature(Map<String, String> headers)
    {
        if (headers == null)
        {
            throw new UnauthorizedException("Unauthenticated.");
        }

        if (!headers.containsKey("authorization"))
        {
            throw new UnauthorizedException("Authorization Header not found.");
        }

        String strAuthorization = headers.get("authorization") + "";

        
        String idToken = strAuthorization.replace("Bearer ", "");
        if (idToken == null || idToken == "")
        {
            throw new UnauthorizedException("IDToken not found.");
        }
        
        //Parse the IDToken
        verifyToken2Authen(idToken);
        
        OidcUtils oidcUtils = new OidcUtils();
        String ski = "";
        try {
			ski = oidcUtils.getSKI(idToken);
		} catch (Exception e) {
			throw new UnauthorizedException(e.getMessage());
		}
        
        if (Common.isStringEmpty(ski))
        {
            throw new UnauthorizedException("Unsupported algorithm: The valid algorithm is SHA256RSA.");
        }
        ValidateIAP(ski);
    }
	
	void ValidateIAP(String ski)
    {
		List<TrustOidcIP> iapList = GetTrustIAPList();
		for(TrustOidcIP iap : iapList){
			if (iap.getIdentifier().equals(ski)) 
                return;
		}
        throw new UnauthorizedException("Untrusted IAP error.");
    }
	
	private List<TrustOidcIP> trustIAPList = null;
	List<TrustOidcIP> GetTrustIAPList()
    {
		
        if (trustIAPList == null)
        {
        	trustIAPList = new ArrayList<TrustOidcIP>();
        	TrustOidcIP item = new TrustOidcIP();
        	item.setIdentifier("c40642c7e970dcc0dd3c368ec3410fe68e647c6f");
        	item.setSubject("WISEID OIDC Demo");
        	trustIAPList.add(item );
        }
        return trustIAPList;
    }
	
	/**
	 * 
	 * @param idToken
	 * @return
	 */
	private void verifyToken2Authen(String idToken) {

		// Validate id token
		try {
			OidcUtils oidcUtils = new OidcUtils();
			JwtTokenInfo idTokenInfo = oidcUtils.getJwtTokenInfo(idToken);
			if (idTokenInfo == null ) {
				throw new UnauthorizedException("Id token is invalid format");
			}
			if (Common.isStringEmpty(idTokenInfo.getClaimISS())) {
				throw new UnauthorizedException("ISS url is invalid");
			}
			
			DiscoveryDocument doc = oidcUtils.getDiscoveryDocument(idTokenInfo.getClaimISS());
			if (doc == null ) {
				throw new UnauthorizedException("DiscoveryDocument is invalid format");
			}
			if (Common.isStringEmpty(doc.getJwksUri())) {
				throw new UnauthorizedException("Jwks is invalid format");
			}
			
			PublicKey publicKey = oidcUtils.getOidcPublicKeyWithJwksUri(doc.getJwksUri());
			if (publicKey == null) {
				throw new UnauthorizedException( "Can not parse public key");
			}
			
			//String isserUri = getIssuerUri();
			//if (isStringEmpty(isserUri)) {
			//	return "Can not get config";
			//}
			
			boolean validateTokenResult = oidcUtils.validateToken(idTokenInfo);
			if (!validateTokenResult) {
				throw new UnauthorizedException("IdToken is invalid");
			}
			
			validateTokenResult = oidcUtils.verifySignature(idTokenInfo, publicKey);
			if (!validateTokenResult) {
				throw new UnauthorizedException("IdToken is invalid.");
			}
			
		} catch (OidcException oidcEx ) {
			throw new UnauthorizedException("Error: " + oidcEx.getMessage() );
		} catch (Exception ex) {
			throw new UnauthorizedException("Error all: " + ex.getMessage() );
		}
	}

}
