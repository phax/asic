package no.difi.asic;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManifestVerifierTest
{

  private static Logger log = LoggerFactory.getLogger (ManifestVerifierTest.class);

  @Test
  public void validateMessageDigestAlgorithm ()
  {
    final ManifestVerifier manifestVerifier = new ManifestVerifier (MessageDigestAlgorithm.SHA256);

    // Not to fail
    manifestVerifier.update ("sha256", null, null, MessageDigestAlgorithm.SHA256.getUri (), null);

    try
    {
      // Should fail
      manifestVerifier.update ("sha384", null, null, MessageDigestAlgorithm.SHA384.getUri (), null);
      fail ("Exception expected");
    }
    catch (final IllegalStateException e)
    {
      log.info (e.getMessage ());
    }

    try
    {
      // Should fail
      manifestVerifier.update ("sha512", null, null, MessageDigestAlgorithm.SHA512.getUri (), null);
      fail ("Exception expected");
    }
    catch (final IllegalStateException e)
    {
      log.info (e.getMessage ());
    }
  }

  @Test
  public void testValidDigest ()
  {
    final ManifestVerifier manifestVerifier = new ManifestVerifier (MessageDigestAlgorithm.SHA256);
    manifestVerifier.update ("file", new byte [] { 'c', 'a', 'f', 'e' }, null);
    manifestVerifier.update ("file", "text/plain", new byte [] { 'c', 'a', 'f', 'e' }, null, null);

    // All files is verified
    manifestVerifier.verifyAllVerified ();
  }

  @Test
  public void testInvalidDigest ()
  {
    final ManifestVerifier manifestVerifier = new ManifestVerifier (MessageDigestAlgorithm.SHA256);
    manifestVerifier.update ("file", new byte [] { 'c', 'a', 'f', 'e' }, null);

    try
    {
      manifestVerifier.update ("file", null, new byte [] { 'c', 'a', 'f', 'f' }, null, null);
      fail ("Exception expected.");
    }
    catch (final IllegalStateException e)
    {
      log.info (e.getMessage ());
    }

    try
    {
      manifestVerifier.verifyAllVerified ();
      fail ("Exception expected.");
    }
    catch (final IllegalStateException e)
    {
      log.info (e.getMessage ());
    }
  }
}
