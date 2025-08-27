package utilites;

import com.sun.istack.NotNull;
import org.example.data_access_layer.FileStore;
import org.example.data_access_layer.MedicoFileStore;
import org.example.domain_layer.Medico;

import java.io.File;

public class FileManagement {
    static File baseDir = new File(System.getProperty("user.dir"));

    @NotNull
    public static FileStore<Medico> getMedicosFileStore(String fileName) {
        File medicosXml = new File(baseDir, fileName);
        return new MedicoFileStore(medicosXml);
    }
}
