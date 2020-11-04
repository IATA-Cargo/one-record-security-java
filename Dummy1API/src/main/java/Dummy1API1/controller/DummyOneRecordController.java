package Dummy1API1.controller;

import Dummy1API1.config.BadRequestException;
import Dummy1API1.config.UnauthorizedException;
import Dummy1API1.model.TrustOidcIP;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import Dummy1API1.model.OneRecordDummyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import util.base.ApiResponseBase;
import util.base.Common;
import util.certificate.OcspUtils;
import util.openidconnect.DiscoveryDocument;
import util.openidconnect.JwtTokenInfo;
import util.openidconnect.OidcException;
import util.openidconnect.OidcUtils;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Dummy1R APIs", description = "")
public class DummyOneRecordController {

    private List<TrustOidcIP> trustIAPList = null;

    @Value("${ocspCacheDir}")
    private String cacheFolder;

    @Operation(summary = "This dummy 1R API shows how Authentication should be performed to strengthen API Security.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "IAP is trusted", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OneRecordDummyResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "401", description = "IAP is not trusted", content = @Content),
            @ApiResponse(responseCode = "500", description = "Have error", content = @Content)})
    @GetMapping("/api/DummyOneRecord")
    ResponseEntity<Object> get(@RequestHeader Map<String, String> headers, Principal principal) {
        try {
            //Trying to load TLS Client Certificate.
            X509Certificate clientCert = getClientCertificate(principal);

            //Validate the TLS Client Certificate
            validateTLSClientCertificate(clientCert);

            // Validate IDToken and Check if IAP is trusted
            validateIDTokenAndSignature(headers);

            //Return Dummy Data
            return new ResponseEntity<Object>(new OneRecordDummyResponse(clientCert), HttpStatus.OK);
        } catch (Exception e) {
            return processException(e);
        }
    }

    /**
     * This function build Response Object for exceptions.
     *
     * @param ex Exception object
     * @return object response to client
     */
    private ResponseEntity<Object> processException(Exception ex) {
        ApiResponseBase resp = new ApiResponseBase();
        resp.setDetails(ex.getMessage());
        if (ex instanceof UnauthorizedException) {
            resp.setCode(HttpStatus.UNAUTHORIZED.value());
            resp.setMessage(HttpStatus.UNAUTHORIZED.toString());

            return new ResponseEntity<Object>(resp, HttpStatus.UNAUTHORIZED);
        } else if (ex instanceof BadRequestException) {

            resp.setCode(HttpStatus.BAD_REQUEST.value());
            resp.setMessage(HttpStatus.BAD_REQUEST.toString());

            return new ResponseEntity<Object>(resp, HttpStatus.BAD_REQUEST);
        }

        resp.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        resp.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.toString());

        return new ResponseEntity<Object>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * This function show how a certificate should be validated over OCSP Service.
     * Calling this function is optional as the certificate may be validated by
     * API Clients or web server.
     * Also note that calling this function may slow down the performance of the API.
     * It is possible to extract Subject or Public Key from the certificate to do some
     * more extra validation.
     * Extract DNS list which include 1R subscriber IDs.
     *
     * @param clientCert The client certificate
     */
    private void validateTLSClientCertificate(X509Certificate clientCert) {
        if (clientCert == null) {
            throw new UnauthorizedException("1R TLS Client Certificate for subscriber is required.");
        }

        String certStatus = "";
        try {
            OcspUtils ocspUtils = new OcspUtils(cacheFolder);
            certStatus = ocspUtils.validate(clientCert);
        } catch (Exception e) {
            certStatus = e.getMessage();
        }
        if (certStatus != "Good") {
            throw new UnauthorizedException("Client certificate is + " + certStatus);
        }
    }

    /**
     * Functioning of this function depends on configuration of the web server. It must be
     * configured to enable TLS Mutual Authentication.
     *
     * @param principal The principal of client
     * @return Client certificate
     */
    private X509Certificate getClientCertificate(Principal principal) {

        X509Certificate x509 = null;
        Certificate cert = null;
        try {
            if (principal == null) {
                return x509;
            }

            if (!(principal instanceof PreAuthenticatedAuthenticationToken)) {
                return x509;
            }

            PreAuthenticatedAuthenticationToken pre = (PreAuthenticatedAuthenticationToken) principal;

            if (!(pre.getCredentials() instanceof Certificate)) {
                return x509;
            }

            cert = (Certificate) pre.getCredentials();

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream bais = new ByteArrayInputStream(cert.getEncoded());
            x509 = (X509Certificate) cf.generateCertificate(bais);
        } catch (Exception e) {

        }
        return x509;
    }

    /**
     * This function parse IDToken, validate signature of the IDToken and
     * may check if the issuer (IAP) of the IDToken is TRUSTED.
     *
     * @param headers
     */
    private void validateIDTokenAndSignature(Map<String, String> headers) {
        if (headers == null) {
            throw new UnauthorizedException("Unauthenticated.");
        }

        if (!headers.containsKey("authorization")) {
            throw new UnauthorizedException("Authorization Header not found.");
        }

        String strAuthorization = headers.get("authorization") + "";

        String idToken = strAuthorization.replace("Bearer ", "");
        if (idToken == null || idToken == "") {
            throw new UnauthorizedException("IDToken not found.");
        }

        //Supposing that IDToken is ready for verification
        //Do verification
        verifyToken2Auth(idToken);

        //Next - Optional: Checking if the issuer of the IDToken is TRUSTED.
        OidcUtils oidcUtils = new OidcUtils();
        String ski = "";
        try {
            ski = oidcUtils.getSKI(idToken);
        } catch (Exception e) {
            throw new UnauthorizedException(e.getMessage());
        }

        if (Common.isStringEmpty(ski)) {
            throw new UnauthorizedException("Unsupported algorithm: The valid algorithm is SHA256RSA.");
        }
        validateIAP(ski);
    }

    /**
     * For Issuers (IAP) that have signing keys are not rotated frequently. It is possible to maintain
     * a list of SKI of TRUSTED Issuers (IAP). For one which use key-rotation, the TRUST-checking should
     * be implemented in your own way.
     *
     * @param ski
     */
    void validateIAP(String ski) {
        /*
         * This validation should be done by the service which implement OneRecord APIs.
         * Please note that SKI often changes due to key rollover (rotation) of the IAP
         */
        return;
        /*
        List<TrustOidcIP> iapList = getTrustIAPList();
        for(TrustOidcIP iap : iapList){
            if (iap.getIdentifier().equals(ski))
                return;
        }
        throw new UnauthorizedException("Untrusted IAP error.");
        */
    }

    /**
     * Building dummy trusted IAP list
     */

    List<TrustOidcIP> getTrustIAPList() {
        if (trustIAPList == null) {
            trustIAPList = new ArrayList<TrustOidcIP>();
            TrustOidcIP item = new TrustOidcIP();
            item.setIdentifier("c40642c7e970dcc0dd3c368ec3410fe68e647c6f");
            item.setSubject("WISEID OIDC Demo");
            trustIAPList.add(item);

            item = new TrustOidcIP();
            item.setIdentifier("b6fdb30a24eac2af027a53995ad8a2bab4125371");
            item.setSubject("WISEID OIDC Demo Dynamic");
            trustIAPList.add(item);
        }

        return trustIAPList;
    }

    /**
     * Parse and verify IDToken
     *
     * @param idToken The token id of client
     */
    private void verifyToken2Auth(String idToken) {

        // Validate id token
        try {
            /*
                Trying to load signing public key. It must be a SHARSA256
            */
            OidcUtils oidcUtils = new OidcUtils();
            JwtTokenInfo idTokenInfo = oidcUtils.getJwtTokenInfo(idToken);
            if (idTokenInfo == null) {
                throw new UnauthorizedException("Id token is invalid format");
            }
            if (Common.isStringEmpty(idTokenInfo.getClaimISS())) {
                throw new UnauthorizedException("ISS url is invalid");
            }

            DiscoveryDocument doc = oidcUtils.getDiscoveryDocument(idTokenInfo.getClaimISS());
            if (doc == null) {
                throw new UnauthorizedException("DiscoveryDocument is invalid format");
            }
            if (Common.isStringEmpty(doc.getJwksUri())) {
                throw new UnauthorizedException("Jwks is invalid format");
            }

            PublicKey publicKey = oidcUtils.getOidcPublicKeyWithJwksUri(doc.getJwksUri());
            if (publicKey == null) {
                throw new UnauthorizedException("Can not parse public key");
            }

            //String isserUri = getIssuerUri();
            //if (isStringEmpty(isserUri)) {
            // return "Can not get config";
            //}

            boolean validateTokenResult = oidcUtils.validateToken(idTokenInfo);
            if (!validateTokenResult) {
                throw new UnauthorizedException("IdToken is invalid");
            }

            // Validate the signature against the public key.
            validateTokenResult = oidcUtils.verifySignature(idTokenInfo, publicKey);
            if (!validateTokenResult) {
                throw new UnauthorizedException("IdToken is invalid.");
            }

        } catch (OidcException oidcEx) {
            throw new UnauthorizedException("Error: " + oidcEx.getMessage());
        } catch (Exception ex) {
            throw new UnauthorizedException("Error all: " + ex.getMessage());
        }
    }

}
