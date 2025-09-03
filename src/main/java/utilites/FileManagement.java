package utilites;

import com.sun.istack.NotNull;
import org.example.data_access_layer.FarmaceutaFileStore;
import org.example.data_access_layer.FileStore;
import org.example.data_access_layer.MedicoFileStore;
import org.example.data_access_layer.PacienteFileStore;
import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medico;
import org.example.domain_layer.Paciente;

import java.io.File;

public class FileManagement {
    static File baseDir = new File(System.getProperty("user.dir"));

    @NotNull
    public static FileStore<Medico> getMedicosFileStore(String fileName) {
        File medicosXml = new File(baseDir, fileName);
        return new MedicoFileStore(medicosXml);
    }
    @NotNull
    public static FileStore<Farmaceuta> getFarmaceutasFileStore(String fileName) {
        File farmaceutasXml = new File(baseDir, fileName);
        return new FarmaceutaFileStore(farmaceutasXml);
    }
    @NotNull
    public static FileStore<Paciente> getPacientesFileStore(String fileName) {
        File pacientesXml = new File(baseDir, fileName);
        return new PacienteFileStore(pacientesXml);
    }

}
