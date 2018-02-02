/**
 * Copyright (C) 2016-2018 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.asic.jaxb;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.asic.jaxb.cades.ASiCManifestType;
import com.helger.asic.jaxb.opendocument.manifest.Manifest;
import com.helger.jaxb.builder.JAXBValidationBuilder;

/**
 * A validator builder for Asic documents.
 *
 * @author Philip Helger
 * @param <JAXBTYPE>
 *        The Asic implementation class to be read
 */
@NotThreadSafe
public class AsicValidator <JAXBTYPE> extends JAXBValidationBuilder <JAXBTYPE, AsicValidator <JAXBTYPE>>
{
  public AsicValidator (@Nonnull final EAsicDocumentType eDocType)
  {
    super (eDocType);
  }

  /**
   * Create a validator builder for ASiCManifestType.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static AsicValidator <ASiCManifestType> asicManifest ()
  {
    return new AsicValidator <> (EAsicDocumentType.ASIC_MANIFEST);
  }

  /**
   * Create a validator builder for Manifest.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static AsicValidator <Manifest> oasisManifest ()
  {
    return new AsicValidator <> (EAsicDocumentType.MANIFEST);
  }
}
