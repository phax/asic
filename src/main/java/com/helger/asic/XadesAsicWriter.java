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
import java.io.OutputStream;

public class XadesAsicWriter extends AbstractAsicWriter
{
  public XadesAsicWriter (final ESignatureMethod signatureMethod,
                          final OutputStream outputStream,
                          final boolean closeStreamOnClose) throws IOException
  {
    super (outputStream, closeStreamOnClose, new XadesAsicManifest (signatureMethod.getMessageDigestAlgorithm ()));
  }

  @Override
  public IAsicWriter setRootEntryName (final String name)
  {
    throw new IllegalStateException ("ASiC-E XAdES does not support defining root file.");
  }

  @Override
  protected void performSign (final SignatureHelper signatureHelper) throws IOException
  {
    // Generate and write manifest (META-INF/signatures.xml)
    final byte [] manifestBytes = ((XadesAsicManifest) m_aAsicManifest).toBytes (signatureHelper);
    m_aAsicOutputStream.writeZipEntry ("META-INF/signatures.xml", manifestBytes);

    // System.out.println(new String(manifestBytes));
  }
}
