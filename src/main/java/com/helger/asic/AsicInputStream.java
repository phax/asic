/**
 * Copyright (C) 2015-2017 difi (www.difi.no)
 * Copyright (C) 2018 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * This Source Code Form is subject to the terms of the
 * Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/
 */
package com.helger.asic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;

public class AsicInputStream extends ZipInputStream
{
  private static final Logger logger = LoggerFactory.getLogger (AsicInputStream.class);

  public AsicInputStream (final InputStream aIS)
  {
    super (aIS);
  }

  @Override
  public ZipEntry getNextEntry () throws IOException
  {
    ZipEntry zipEntry = super.getNextEntry ();

    if (zipEntry != null && zipEntry.getName ().equals ("mimetype"))
    {
      try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();)
      {
        AsicUtils.copyStream (this, aBAOS);
        final String sMimeType = aBAOS.getAsString (StandardCharsets.ISO_8859_1);

        if (logger.isDebugEnabled ())
          logger.debug ("Content of mimetype: " + sMimeType);
        if (!AsicUtils.MIMETYPE_ASICE.getAsString ().equals (sMimeType))
          throw new IllegalStateException ("Content is not ASiC-E container.");
      }

      // Fetch next
      zipEntry = super.getNextEntry ();
    }

    return zipEntry;
  }
}