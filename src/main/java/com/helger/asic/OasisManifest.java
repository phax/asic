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

import java.io.InputStream;

import javax.annotation.Nullable;

import com.helger.asic.jaxb.AsicReader;
import com.helger.asic.jaxb.AsicWriter;
import com.helger.asic.jaxb.opendocument.manifest.FileEntry;
import com.helger.asic.jaxb.opendocument.manifest.Manifest;

class OasisManifest
{
  @Nullable
  public static Manifest read (final InputStream inputStream)
  {
    return AsicReader.oasisManifest ().read (inputStream);
  }

  private final Manifest m_aManifest;

  public OasisManifest (final MimeType mimeType)
  {
    m_aManifest = new Manifest ();
    add ("/", mimeType);
  }

  public OasisManifest (final InputStream inputStream)
  {
    m_aManifest = AsicReader.oasisManifest ().read (inputStream);
    if (m_aManifest == null)
      throw new IllegalStateException ("Failed to read Manifest from IS");
  }

  public void add (final String path, final MimeType mimeType)
  {
    final FileEntry fileEntry = new FileEntry ();
    fileEntry.setMediaType (mimeType.toString ());
    fileEntry.setFullPath (path);
    m_aManifest.getFileEntry ().add (fileEntry);
  }

  public void addAll (final OasisManifest aOther)
  {
    for (final FileEntry fileEntry : aOther.m_aManifest.getFileEntry ())
      if (!fileEntry.getFullPath ().equals ("/"))
        m_aManifest.getFileEntry ().add (fileEntry);
  }

  public int size ()
  {
    return m_aManifest.getFileEntry ().size ();
  }

  public byte [] toBytes ()
  {
    return AsicWriter.oasisManifest ().getAsBytes (m_aManifest);
  }
}
