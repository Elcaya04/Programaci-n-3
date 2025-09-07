package org.example.data_access_layer;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.example.domain_layer.Paciente;
import org.example.domain_layer.RecetaMedica;

import javax.xml.stream.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RecetaMedicaFileStore implements FileStore<RecetaMedica>{
    private final File xmlFile;

    public RecetaMedicaFileStore(File xmlFile) {
        this.xmlFile = xmlFile;
        ensureFile();
    }

    @Override
    public List<RecetaMedica> Leer() {
        List<RecetaMedica> out = new ArrayList<>();
        if (xmlFile.length() == 0) return out;

        try (FileInputStream in = new FileInputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(RecetaMedica.class);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xr = xif.createXMLStreamReader(in);
            while (xr.hasNext()) {
                if (xr.getEventType() == XMLStreamConstants.START_ELEMENT && "receta".equals(xr.getLocalName())) {
                    Unmarshaller u = ctx.createUnmarshaller();
                    RecetaMedica p = u.unmarshal(xr, RecetaMedica.class).getValue();
                    out.add(p);
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
    public void Escribir(List<RecetaMedica> lista) {
        try (FileOutputStream out = new FileOutputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(RecetaMedica.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);

            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter xw = xof.createXMLStreamWriter(out, "UTF-8");

            xw.writeStartDocument("UTF-8", "1.0");
            xw.writeStartElement("recetas");

            if (lista != null) {
                for (RecetaMedica p : lista) m.marshal(p, xw);
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
            if (parent != null) parent.mkdirs();
            if (!xmlFile.exists()) {
                xmlFile.createNewFile();
                Escribir(new ArrayList<>());
            }
        } catch (Exception ignored) {}
    }
}
