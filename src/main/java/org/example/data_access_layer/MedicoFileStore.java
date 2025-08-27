package org.example.data_access_layer;

import org.example.domain_layer.Medico;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.stream.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MedicoFileStore implements FileStore<Medico> {
    private final File xmlFile;

    public MedicoFileStore(File xmlFile) {
        this.xmlFile = xmlFile;
        ensureFile();
    }

    @Override
    public List<Medico> Leer() {
        List<Medico> out = new ArrayList<>();
        if (xmlFile.length() == 0) return out;

        try (FileInputStream in = new FileInputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(Medico.class);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xr = xif.createXMLStreamReader(in);
            while (xr.hasNext()) {
                if (xr.getEventType() == XMLStreamConstants.START_ELEMENT && "medico".equals(xr.getLocalName())) {
                    Unmarshaller u = ctx.createUnmarshaller();
                    Medico c = u.unmarshal(xr, Medico.class).getValue();
                    out.add(c);
                    continue;
                }
                xr.next();
            }
            xr.close();

        } catch (Exception ex) {
            System.err.println("[WARN] Error leyendo " + xmlFile + ": " + ex.getMessage());
            ex.printStackTrace();
        }
        return out;
    }

    @Override
    public void Escribir(List<Medico> lista) {
        try (FileOutputStream out = new FileOutputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(Medico.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);

            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter xw = xof.createXMLStreamWriter(out, "UTF-8");

            xw.writeStartDocument("UTF-8", "1.0");
            xw.writeStartElement("medicos");

            if (lista != null) {
                for (Medico c : lista) {
                    m.marshal(c, xw);
                }
            }

            xw.writeEndElement();
            xw.writeEndDocument();
            xw.flush();
            xw.close();
        } catch (Exception ex) {
            System.err.println("[WARN] Error escribiendo " + xmlFile);
            ex.printStackTrace();
        }
    }
    private void ensureFile() {
        try {
            File parent = xmlFile.getParentFile();

            if (parent != null) {
                parent.mkdirs();
            }

            if (!xmlFile.exists()) {
                xmlFile.createNewFile();
                Escribir(new ArrayList<>());
            }
        } catch (Exception ignored) {}
    }
}


