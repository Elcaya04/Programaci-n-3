package presentation_layer.Views.LoginView;

import org.example.Main;
import org.example.data_access_layer.FarmaceutaFileStore;
import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medico;
import org.example.domain_layer.Paciente;
import org.glassfish.jaxb.core.v2.model.core.ID;
import presentation_layer.Views.FarmaceutaView.FarmaceutaView;
import presentation_layer.Views.MainWindow.MainWindow;
import presentation_layer.Views.MedicoView.MedicoView;
import service_layer.FarmaceutaService;
import service_layer.MedicoService;

import javax.swing.*;

public class LoginView extends JFrame {

    private JPanel panel1;
    private JTextField ID_Texto;
    private JButton IngresarButton;
    private JButton LimpiarButton;
    private JButton Cambio_ContraseñaButton;
    private JPasswordField Contraseña_Texto;


    // contraseñas iniciales
    private static String passAdmin = "administrador";

    public LoginView() {
        setTitle("Login");
        setContentPane(panel1);
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        IngresarButton.addActionListener(e -> onIngresar());
        LimpiarButton.addActionListener(e -> onLimpiar());
        Cambio_ContraseñaButton.addActionListener(e -> onCambioContrasena());
    }

    private void onIngresar() {
        String usuario = ID_Texto.getText().trim();
        String contrasena = new String(Contraseña_Texto.getPassword()).trim();



        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar usuario y contraseña");
            return;
        }

        if (validarUsuario(usuario,contrasena)) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + usuario);
            SwingUtilities.invokeLater(() -> abrirVista(usuario));
            dispose(); // cerrar login
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
        }
    }

    private boolean validarUsuario(String usuario, String contrasena) {
        // Administrador fijo
        if (usuario.equalsIgnoreCase("administrador")) {
            return contrasena.equals(passAdmin);
        }

        // Buscar Farmaceuta
        Farmaceuta far = Main.getFarmaceutaView().getController().Buscar_porID(usuario);
        if (far != null && contrasena.equals(far.getID())) {
            return true;
        }
        // Buscar Medico
        Medico med = Main.getMedicoView().getController().Buscar_porID_M(usuario);
        if (med != null && contrasena.equals(med.getID())) {
            return true;
        }
        return false;
        }

    private void abrirVista(String usuario) {
        // Farmaceuta
        Farmaceuta far = Main.getFarmaceutaView().getController().Buscar_porID(usuario);
        if (far != null && usuario.equals(far.getID())) {
            new JFrame("Farmaceutas") {{
                setContentPane(Main.getFarmaceutaView().getContentPanel());
                setSize(800, 600);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
            }};
            return;
        }

        // Médico
        Medico medico = Main.getMedicoView().getController().Buscar_porID_M(usuario);
        if (medico != null && usuario.equals(medico.getID())) {
            new JFrame("Medicos") {{
                setContentPane(Main.getMedicoView().getContentPanel());
                setSize(800, 600);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
            }};
            return;
        }

        // Administrador
        if (usuario.equalsIgnoreCase("administrador")) {
            MainWindow window = new MainWindow();
            window.agregarTabs(Main.getTabs(), Main.getTabs2(), Main.getTabs3());
            window.setVisible(true);
        }

             if(usuario.equalsIgnoreCase("paciente")) {
            new JFrame("Pacientes") {{
                setContentPane(Main.getPacienteView().getContentPanel());
                setSize(800, 600);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
            }};
            return;
        }


            if(usuario.equalsIgnoreCase("administrador")){

            MainWindow window = new MainWindow();
            window.agregarTabs(Main.getTabs(), Main.getTabs2(), Main.getTabs3());
            window.setVisible(true);


        }

    }

    private void onLimpiar() {
        ID_Texto.setText("");
        Contraseña_Texto.setText("");
    }

    private void onCambioContrasena() {
        String usuario = JOptionPane.showInputDialog(this, "Ingrese el usuario:");
        if (usuario == null || usuario.isEmpty()) return;

        String actual = JOptionPane.showInputDialog(this, "Ingrese la contraseña actual:");
        if (actual == null || actual.isEmpty()) return;

        if (validarUsuario(usuario, actual)) {
            String nueva = JOptionPane.showInputDialog(this, "Ingrese la nueva contraseña:");
            if (nueva == null || nueva.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Contraseña no puede estar vacía");
                return;
            }

            if (usuario.equalsIgnoreCase("administrador")) {
                passAdmin = nueva;
            } else {
                // Buscar farmaceuta
                Farmaceuta farmaceuta = Far.Buscar_porID(usuario);
                if (farmaceuta != null) {
                    farmaceuta.setClave(nueva);
                    Far.actualizar(farmaceuta);
                }

                // Buscar médico
                Medico medico = Med.Buscar_porID_M(usuario);
                if (medico != null) {
                    medico.setClave(nueva);
                    Med.actualizar(medico);
                }
            }

            JOptionPane.showMessageDialog(this, "Contraseña cambiada exitosamente");
        } else {
            JOptionPane.showMessageDialog(this, "Contraseña actual incorrecta");
        }
    }
}
