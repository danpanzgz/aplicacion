package dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import modelo.Empresa;
import modelo.Trabajador;

public class VerDialog extends JDialog implements ActionListener {

    private Empresa empresa;
    private JTextField codigoField;
    private JButton buscarButton, cancelarButton;
    private JTextArea resultadoArea;
    private JComboBox<String> trabajadorComboBox;

    public VerDialog(Empresa empresa) {
        this.empresa = empresa;

        setTitle("Buscar Trabajador");
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null); // Centra la ventana

        setLayout(new FlowLayout());

        // Opción 1: Buscar por código de trabajador
        add(new JLabel("Introduzca el código del trabajador:"));
        codigoField = new JTextField(15);
        add(codigoField);

        buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(this);
        add(buscarButton);

        // Opción 2: Buscar desde lista desplegable de trabajadores
        add(new JLabel("O seleccione un trabajador:"));
        trabajadorComboBox = new JComboBox<>(empresa.getIdentificador());
        trabajadorComboBox.addActionListener(this);
        add(trabajadorComboBox);

        // Área para mostrar el resultado de la búsqueda
        resultadoArea = new JTextArea(8, 30);
        resultadoArea.setEditable(false);
        add(new JScrollPane(resultadoArea));

        // Botón para cancelar
        cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(this);
        add(cancelarButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buscarButton) {
            // Buscar trabajador por código introducido
            String codigo = codigoField.getText();
            try {
                int codigoInt = Integer.parseInt(codigo);
                if (codigoInt <= 0) {
                    throw new NumberFormatException(); // Si es menor o igual a cero, lo consideramos un error
                }

                Trabajador trabajador = empresa.getTrabajadorPorId(String.valueOf(codigoInt));
                if (trabajador != null) {
                    mostrarDatos(trabajador);
                } else {
                    JOptionPane.showMessageDialog(this, "Trabajador no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un número entero mayor a 0.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == trabajadorComboBox) {
            // Buscar trabajador desde el ComboBox
            String id = (String) trabajadorComboBox.getSelectedItem();
            if (id != null) {
                Trabajador trabajador = empresa.getTrabajadorPorId(id);
                if (trabajador != null) {
                    mostrarDatos(trabajador);
                } else {
                    JOptionPane.showMessageDialog(this, "Trabajador no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == cancelarButton) {
            dispose(); // Cerrar el diálogo si se cancela
        }
    }

    // Mostrar los datos del trabajador en el JTextArea
    private void mostrarDatos(Trabajador trabajador) {
        StringBuilder datos = new StringBuilder();
        datos.append("Código: " + trabajador.getIdentificador() + "\n");
        datos.append("DNI: " + trabajador.getDni() + "\n");
        datos.append("Nombre: " + trabajador.getNombre() + "\n");
        datos.append("Apellidos: " + trabajador.getApellidos() + "\n");
        datos.append("Dirección: " + trabajador.getDireccion() + "\n");
        datos.append("Teléfono: " + trabajador.getTelefono() + "\n");
        datos.append("Puesto: " + trabajador.getPuesto() + "\n");

        resultadoArea.setText(datos.toString()); // Mostrar en el área de texto
    }
}
