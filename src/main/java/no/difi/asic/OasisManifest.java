package no.difi.asic;

import oasis.names.tc.opendocument.xmlns.manifest._1.FileEntry;
import oasis.names.tc.opendocument.xmlns.manifest._1.Manifest;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

class OasisManifest {

    private static JAXBContext jaxbContext; // Thread safe

    static {
        try {
            jaxbContext = JAXBContext.newInstance(Manifest.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(String.format("Unable to create JAXBContext: %s ", e.getMessage()), e);
        }
    }

    public static Manifest read(InputStream inputStream) {
        return new OasisManifest(inputStream).getManifest();
    }

    private Manifest manifest = new Manifest();

    public OasisManifest(MimeType mimeType) {
        add("/", mimeType);
    }

    public OasisManifest(InputStream inputStream) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            manifest = (Manifest) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            throw new IllegalStateException("Unable to read XML as OASIS OpenDocument Manifest.", e);
        }
    }

    public void add(String path, MimeType mimeType) {
        FileEntry fileEntry = new FileEntry();
        fileEntry.setMediaType(mimeType.toString());
        fileEntry.setFullPath(path);
        manifest.getFileEntries().add(fileEntry);
    }

    public void append(OasisManifest oasisManifest) {
        for (FileEntry fileEntry : oasisManifest.getManifest().getFileEntries())
            if (!fileEntry.getFullPath().equals("/"))
                manifest.getFileEntries().add(fileEntry);
    }

    public int size() {
        return manifest.getFileEntries().size();
    }

    public Manifest getManifest() {
        return manifest;
    }

    public byte[] toBytes() {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            /* marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new NamespacePrefixMapper() {
                @Override
                public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
                    return "manifest";
                }
            }); */

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            marshaller.marshal(manifest, byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (JAXBException e) {
            throw new IllegalStateException("Unable to create OASIS OpenDocument Manifest.", e);
        }
    }
}