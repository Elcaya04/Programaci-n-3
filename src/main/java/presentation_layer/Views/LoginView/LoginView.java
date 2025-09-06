package presentation_layer.Views.LoginView;

import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medico;

import service_layer.Service;

import javax.swing.*;


public class LoginView extends JFrame {

    private JPanel panel1;
    private JTextField ID_Texto;
    private JButton IngresarButton;
    private JButton LimpiarButton;
    private JButton Cambio_ContraseñaButton;
    private JPasswordField Contraseña_Texto;

    // Solo servicios necesarios para validación
    private final Service<Farmaceuta> farmaceutaService;
    private final Service<Medico> medicoService;

    // Interfaz para comunicar resultado del login
    private final LoginCallback loginCallback;

    // Contraseña inicial del administrador
    private static String passAdmin = "administrador";

    public LoginView(Service<Farmaceuta> farmaceutaService,
                     Service<Medico> medicoService,
                     LoginCallback loginCallback) {

        this.farmaceutaService = farmaceutaService;
        this.medicoService = medicoService;
        this.loginCallback = loginCallback;

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

        TipoUsuario tipoUsuario = validarUsuario(usuario, contrasena);

        if (tipoUsuario != TipoUsuario.INVALIDO) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + usuario);
            // Notificar al callback sobre el login exitoso
            loginCallback.onLoginSuccess(usuario, tipoUsuario);
            dispose(); // cerrar login
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
        }
    }

    private TipoUsuario validarUsuario(String usuario, String contrasena) {
        // Administrador fijo
        if (usuario.equalsIgnoreCase("administrador")) {
            return contrasena.equals(passAdmin) ? TipoUsuario.ADMINISTRADOR : TipoUsuario.INVALIDO;
        }

        // Buscar Farmaceuta
        Farmaceuta farmaceuta = farmaceutaService.Buscar_porID(usuario);
        if (farmaceuta != null && contrasena.equals(farmaceuta.getClave())) {
            return TipoUsuario.FARMACEUTA;
        }

        // Buscar Medico
        Medico medico = medicoService.Buscar_porID(usuario);
        if (medico != null && contrasena.equals(medico.getClave())) {
            return TipoUsuario.MEDICO;
        }

        return TipoUsuario.INVALIDO;
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

        if (validarUsuario(usuario, actual) != TipoUsuario.INVALIDO) {
            String nueva = JOptionPane.showInputDialog(this, "Ingrese la nueva contraseña:");
            if (nueva == null || nueva.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Contraseña no puede estar vacía");
                return;
            }

            if (cambiarContrasena(usuario, nueva)) {
                JOptionPane.showMessageDialog(this, "Contraseña cambiada exitosamente");
            } else {
                JOptionPane.showMessageDialog(this, "Error al cambiar la contraseña");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña actual incorrecta");
        }
    }

    private boolean cambiarContrasena(String usuario, String nuevaContrasena) {
        // Administrador
        if (usuario.equalsIgnoreCase("administrador")) {
            passAdmin = nuevaContrasena;
            return true;
        }

        // Farmaceuta
        Farmaceuta farmaceuta = farmaceutaService.LeerID(usuario);
        if (farmaceuta != null) {
            farmaceuta.setClave(nuevaContrasena);
            farmaceutaService.actualizar(farmaceuta);
            return true;
        }

        // Médico
        Medico medico = medicoService.LeerID(usuario);
        if (medico != null) {
            medico.setClave(nuevaContrasena);
            medicoService.actualizar(medico);
            return true;
        }

        return false;
    }

    // Interfaz para el callback
    public interface LoginCallback {
        void onLoginSuccess(String usuario, TipoUsuario tipoUsuario);
    }

    // Enum para tipos de usuario
    public enum TipoUsuario {
        ADMINISTRADOR,
        MEDICO,
        FARMACEUTA,
        INVALIDO
    }
}