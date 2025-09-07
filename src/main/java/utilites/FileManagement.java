package utilites;

import com.sun.istack.NotNull;
import org.example.data_access_layer.*;
import org.example.domain_layer.*;

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
    @NotNull
    public static FileStore<Medicamentos> getMedicamentosFileStore(String fileName) {
        File medicamentosXml = new File(baseDir, fileName);
        return new MedicamentosFileStore(medicamentosXml);
    }
    @NotNull
    public static FileStore<RecetaMedica> getRecetaMedicaFileStore(String fileName) {
        File RecetaMedicaXml = new File(baseDir, fileName);
        return new RecetaMedicaFileStore(RecetaMedicaXml);
    }

}
