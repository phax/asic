package com.helger.asic.extras;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.util.Collection;

import org.bouncycastle.cms.CMSEnvelopedDataParser;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;

import com.helger.asic.AsicUtils;
import com.helger.asic.BCHelper;
import com.helger.asic.IAsicReader;
import com.helger.asic.jaxb.asic.AsicManifest;

/**
 * Wrapper to seamlessly decode encoded files.
 */
public class CmsEncryptedAsicReader implements IAsicReader
{
  static
  {
    BCHelper.getProvider ();
  }

  private final IAsicReader m_aAsicReader;
  private final PrivateKey m_aPrivateKey;

  private String m_sCurrentFile;

  public CmsEncryptedAsicReader (final IAsicReader asicReader, final PrivateKey privateKey)
  {
    this.m_aAsicReader = asicReader;
    this.m_aPrivateKey = privateKey;
  }

  @Override
  public String getNextFile () throws IOException
  {
    m_sCurrentFile = m_aAsicReader.getNextFile ();
    if (m_sCurrentFile == null)
      return null;

    return m_sCurrentFile.endsWith (".p7m") ? m_sCurrentFile.substring (0, m_sCurrentFile.length () - 4)
                                            : m_sCurrentFile;
  }

  @Override
  public void writeFile (final File file) throws IOException
  {
    writeFile (file.toPath ());
  }

  @Override
  public void writeFile (final Path path) throws IOException
  {
    try (OutputStream outputStream = Files.newOutputStream (path))
    {
      writeFile (outputStream);
    }
  }

  @Override
  public void writeFile (final OutputStream outputStream) throws IOException
  {
    if (m_sCurrentFile.endsWith (".p7m"))
    {
      try
      {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream ();
        m_aAsicReader.writeFile (byteArrayOutputStream);

        final CMSEnvelopedDataParser cmsEnvelopedDataParser = new CMSEnvelopedDataParser (new ByteArrayInputStream (byteArrayOutputStream.toByteArray ()));
        // expect exactly one recipient
        final Collection <?> recipients = cmsEnvelopedDataParser.getRecipientInfos ().getRecipients ();
        if (recipients.size () != 1)
          throw new IllegalArgumentException ();

        // retrieve recipient and decode it
        final RecipientInformation recipient = (RecipientInformation) recipients.iterator ().next ();
        final byte [] decryptedData = recipient.getContent (new JceKeyTransEnvelopedRecipient (m_aPrivateKey).setProvider (BCHelper.getProvider ()));

        AsicUtils.copyStream (new ByteArrayInputStream (decryptedData), outputStream);
      }
      catch (final Exception e)
      {
        throw new IOException (e.getMessage (), e);
      }
    }
    else
    {
      m_aAsicReader.writeFile (outputStream);
    }
  }

  @Override
  public InputStream inputStream () throws IOException
  {
    final PipedInputStream pipedInputStream = new PipedInputStream ();
    final PipedOutputStream pipedOutputStream = new PipedOutputStream (pipedInputStream);

    writeFile (pipedOutputStream);
    return pipedInputStream;
  }

  @Override
  public void close () throws IOException
  {
    m_aAsicReader.close ();
  }

  @Override
  public AsicManifest getAsicManifest ()
  {
    final AsicManifest asicManifest = m_aAsicReader.getAsicManifest ();

    final String rootfile = asicManifest.getRootfile ();
    if (rootfile != null && rootfile.endsWith (".p7m"))
      asicManifest.setRootfile (rootfile.substring (0, rootfile.length () - 4));

    return asicManifest;
  }
}
