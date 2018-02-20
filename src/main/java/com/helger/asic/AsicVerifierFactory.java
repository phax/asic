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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.Nonnull;
import javax.annotation.WillCloseWhenClosed;

import com.helger.commons.ValueEnforcer;

public class AsicVerifierFactory
{
  private final EMessageDigestAlgorithm m_eMessageDigestAlgorithm;

  @Nonnull
  public static AsicVerifierFactory newFactory ()
  {
    return newFactory (EMessageDigestAlgorithm.SHA256);
  }

  @Nonnull
  public static AsicVerifierFactory newFactory (@Nonnull final ESignatureMethod eSM)
  {
    return newFactory (eSM.getMessageDigestAlgorithm ());
  }

  @Nonnull
  public static AsicVerifierFactory newFactory (@Nonnull final EMessageDigestAlgorithm eMDAlgorithm)
  {
    return new AsicVerifierFactory (eMDAlgorithm);
  }

  protected AsicVerifierFactory (@Nonnull final EMessageDigestAlgorithm eMDAlgo)
  {
    ValueEnforcer.notNull (eMDAlgo, "MDAlgo");
    m_eMessageDigestAlgorithm = eMDAlgo;
  }

  @Nonnull
  public AsicVerifier verify (@Nonnull final File aFile) throws IOException
  {
    return verify (aFile.toPath ());
  }

  @Nonnull
  public AsicVerifier verify (@Nonnull final Path aFile) throws IOException
  {
    return verify (Files.newInputStream (aFile));
  }

  @Nonnull
  public AsicVerifier verify (@Nonnull @WillCloseWhenClosed final InputStream inputStream) throws IOException
  {
    return new AsicVerifier (m_eMessageDigestAlgorithm, inputStream);
  }
}
