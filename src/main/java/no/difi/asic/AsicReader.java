package no.difi.asic;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import no.difi.commons.asic.jaxb.asic.AsicManifest;

public interface AsicReader extends Closeable
{

  /**
   * Provides the name of the next entry in the ASiC archive and positions the
   * inputstream at the beginning of the data.
   *
   * @return name of next entry in archive.
   * @throws IOException
   */
  String getNextFile () throws IOException;

  /**
   * Writes the contents of the current entry into a file
   *
   * @param file
   *        into which the contents should be written.
   * @throws IOException
   */
  void writeFile (File file) throws IOException;

  /**
   * Writes contents of current archive entry into a file.
   *
   * @param path
   *        into which the contents of current entry should be written.
   * @throws IOException
   */
  void writeFile (Path path) throws IOException;

  /**
   * Writes contents of current archive entry to the supplied output stream.
   *
   * @param outputStream
   *        into which data from current entry should be written.
   * @throws IOException
   */
  void writeFile (OutputStream outputStream) throws IOException;

  /**
   * Returns InputStream to read the content.
   *
   * @return Content
   */
  InputStream inputStream () throws IOException;

  AsicManifest getAsicManifest ();
}
