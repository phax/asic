package no.difi.asic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class AsicReaderFactory
{

  public static AsicReaderFactory newFactory ()
  {
    return newFactory (MessageDigestAlgorithm.SHA256);
  }

  public static AsicReaderFactory newFactory (SignatureMethod signatureMethod)
  {
    return newFactory (signatureMethod.getMessageDigestAlgorithm ());
  }

  static AsicReaderFactory newFactory (MessageDigestAlgorithm messageDigestAlgorithm)
  {
    return new AsicReaderFactory (messageDigestAlgorithm);
  }

  private MessageDigestAlgorithm messageDigestAlgorithm;

  private AsicReaderFactory (MessageDigestAlgorithm messageDigestAlgorithm)
  {
    this.messageDigestAlgorithm = messageDigestAlgorithm;
  }

  public AsicReader open (File file) throws IOException
  {
    return open (file.toPath ());
  }

  public AsicReader open (Path file) throws IOException
  {
    return open (Files.newInputStream (file));
  }

  public AsicReader open (InputStream inputStream) throws IOException
  {
    return new AsicReaderImpl (messageDigestAlgorithm, inputStream);
  }
}
