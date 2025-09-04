package presentation_layer.Views.LoginView;

import org.example.Main;
import presentation_layer.Views.MainWindow.MainWindow;

import javax.swing.*;

public class LoginView extends JFrame {

    private JPanel panel1;
    private JTextField ID_Texto;
    private JButton IngresarButton;
    private JButton LimpiarButton;
    private JButton Cambio_ContraseñaButton;
    private JPasswordField Contraseña_Texto;

    // contraseñas iniciales
    private static String passMedico = "medico";
    private static String passPaciente = "paciente";
    private static String passFarmaceutica = "farmaceutica";
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

        if (validarUsuario(usuario, contrasena)) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + usuario);
            SwingUtilities.invokeLater(() -> abrirVista(usuario));
            dispose(); // cerrar login
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
        }
    }

    private boolean validarUsuario(String usuario, String contrasena) {
        switch (usuario.toLowerCase()) {
            case "medico": return contrasena.equals(passMedico);
            case "paciente": return contrasena.equals(passPaciente);
            case "farmaceutica": return contrasena.equals(passFarmaceutica);
            case "administrador": return contrasena.equals(passAdmin);
            default: return false;
        }
    }
    private void abrirVista(String usuario) {
        switch (usuario.toLowerCase()) {
            case "medico":
                new JFrame("Medicos") {{
                    setContentPane(Main.getMedicoView().getContentPanel());
                    setSize(800, 600);
                    setDefaultCloseOperation(EXIT_ON_CLOSE);
                    setVisible(true);
                }};
                break;

            case "farmaceutica":
                new JFrame("Farmaceutas") {{
                    setContentPane(Main.getFarmaceutaView().getContentPanel());
                    setSize(800, 600);
                    setDefaultCloseOperation(EXIT_ON_CLOSE);
                    setVisible(true);
                }};
                break;

            case "paciente":
                new JFrame("Pacientes") {{
                    setContentPane(Main.getPacienteView().getContentPanel());
                    setSize(800, 600);
                    setDefaultCloseOperation(EXIT_ON_CLOSE);
                    setVisible(true);
                }};
                break;

            case "administrador":
                MainWindow window = new MainWindow();
                window.agregarTabs(Main.getTabs(), Main.getTabs2(), Main.getTabs3());
                window.setVisible(true);
                break;

            default:
                JOptionPane.showMessageDialog(this, "No hay vista para este usuario");
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

            switch (usuario.toLowerCase()) {
                case "medico": passMedico = nueva; break;
                case "paciente": passPaciente = nueva; break;
                case "farmaceutica": passFarmaceutica = nueva; break;
                case "administrador": passAdmin = nueva; break;
            }

            JOptionPane.showMessageDialog(this, "Contraseña cambiada exitosamente");
        } else {
            JOptionPane.showMessageDialog(this, "Contraseña actual incorrecta");
        }
    }
}