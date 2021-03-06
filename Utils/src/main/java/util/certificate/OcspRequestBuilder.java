package util.certificate;

import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.bouncycastle.operator.DigestCalculator;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

/**
 * Class to help build Ocsp request
 */
public class OcspRequestBuilder {
    private static final SecureRandom GENERATOR = new SecureRandom();

    private SecureRandom generator = GENERATOR;

    private DigestCalculator calculator = DigesterSHA1.sha1();

    private X509Certificate certificate;

    private X509Certificate issuer;

    /**
     * Set certificate to builder
     *
     * @param certificate main certificate
     * @return OcspRequestBuilder object
     */
    OcspRequestBuilder certificate(X509Certificate certificate) {
        this.certificate = certificate;
        return this;
    }

    /**
     * Set the certificate of issuer to builder
     *
     * @param issuer the certificate of issuer
     * @return OcspRequestBuilder object
     */
    OcspRequestBuilder issuer(X509Certificate issuer) {
        this.issuer = issuer;
        return this;
    }

    /**
     *
     *
     * @return the builded OCSPReq object
     * @throws org.bouncycastle.cert.ocsp.OCSPException
     * @throws java.io.IOException
     * @throws java.security.cert.CertificateEncodingException
     */
    OCSPReq build() throws OCSPException, IOException, CertificateEncodingException {
        SecureRandom generator = this.generator;
        DigestCalculator calculator = this.calculator;
        X509Certificate certificate = this.certificate;
        X509Certificate issuer = this.issuer;

        BigInteger serial = certificate.getSerialNumber();

        CertificateID certId = new CertificateID(calculator, new X509CertificateHolder(issuer.getEncoded()), serial);

        OCSPReqBuilder builder = new OCSPReqBuilder();
        builder.addRequest(certId);

        byte[] nonce = new byte[8];
        generator.nextBytes(nonce);

        Extension[] extensions = new Extension[] {
                new Extension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce, false, new DEROctetString(nonce)) };

        builder.setRequestExtensions(new Extensions(extensions));

        return builder.build();
    }

}
