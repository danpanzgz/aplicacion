package dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.GestionTrabajadores;
import exceptions.BDException;
import modelo.Empresa;
import modelo.Trabajador;

public class ModificaDialog extends JDialog implements ActionListener {

	int id = 0;
	String dni = "";
	String nombre = "";
	String apellidos = "";
	String direccion = "";
	String telefono = "";
	String puesto = "";

	private JTable tabla;
	private JButton guardar;
	private JButton cerrar;
	private DefaultTableModel modeloTabla;
	private JScrollPane scrollPane;

	Empresa empresa;

	public ModificaDialog(Empresa empresa) throws BDException {
		this.empresa = empresa;

		setResizable(false);
		setTitle("Listado Trabajadores");
		setSize(750, 700);
		setLayout(new FlowLayout());
		setLocationRelativeTo(null);

		cargarTabla();

		guardar = new JButton("Guardar");
		guardar.addActionListener(this);
		add(guardar);

		cerrar = new JButton("Cerrar");
		cerrar.addActionListener(this);
		add(cerrar);

		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}

	private void cargarTabla() throws BDException {
		String[] columnas = { "Identificador", "DNI", "Nombre", "Apellidos", "Dirección", "Teléfono", "Puesto" };
		String[][] datos = GestionTrabajadores.listarTrabajadores();

		if (modeloTabla == null) {
			modeloTabla = new DefaultTableModel(datos, columnas) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return column != 0; // ID no editable
				}
			};

			tabla = new JTable(modeloTabla);
			String[] puestos = { "Administrativo", "Gerente", "Técnico", "Otro" };
			JComboBox<String> comboBox = new JComboBox<>(puestos);
			tabla.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(comboBox));

			scrollPane = new JScrollPane(tabla);
			scrollPane.setPreferredSize(new Dimension(700, 600));
			add(scrollPane);
		} else {
			modeloTabla.setDataVector(datos, columnas);
		}

		revalidate();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == guardar) {

			if (tabla.isEditing()) {
				tabla.getCellEditor().stopCellEditing();
			}

			boolean algunCambioGuardado = false;
			boolean huboErrores = false;

			boolean cambiosRealizados = false;

			for (int i = 0; i < tabla.getRowCount(); i++) {
				id = Integer.parseInt(tabla.getValueAt(i, 0).toString());
				dni = tabla.getValueAt(i, 1).toString();
				nombre = tabla.getValueAt(i, 2).toString();
				apellidos = tabla.getValueAt(i, 3).toString();
				direccion = tabla.getValueAt(i, 4).toString();
				telefono = tabla.getValueAt(i, 5).toString();
				puesto = tabla.getValueAt(i, 6).toString();

				if (comprobarErrores(id, dni, nombre, apellidos, direccion, telefono, puesto)) {
					Trabajador t = new Trabajador(id, dni, nombre, apellidos, direccion, telefono, puesto);
					try {
						boolean actualizado = GestionTrabajadores.modificaTrabajador(t);
						if (actualizado) {
							algunCambioGuardado = true;
						} else {
							JOptionPane.showMessageDialog(this, "No se encontró el trabajador con ID: " + id);
							huboErrores = true;
						}

					} catch (BDException ex) {
						JOptionPane.showMessageDialog(this, "Error al guardar los cambios: " + ex.getMessage());
					}
				}
			}

			if (cambiosRealizados) {
				JOptionPane.showMessageDialog(this, "Los cambios se han guardado correctamente.");
				try {
					cargarTabla(); // recarga los datos después de guardar
				} catch (BDException ex) {
					JOptionPane.showMessageDialog(this, "Error al recargar la tabla: " + ex.getMessage());
				}
			} else {
				JOptionPane.showMessageDialog(this, "No se realizaron cambios.");
			}

		} else if (e.getSource() == cerrar) {
			dispose(); // Cierra el diálogo
		}
	}

	public boolean comprobarErrores(int id, String dni, String nombre, String apellidos, String direccion,
			String telefono, String puesto) {
		if (id < 1) {
			JOptionPane.showMessageDialog(null, "El ID debe ser un número entero positivo", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (!GestionTrabajadores.validarDNI(dni)) {
			JOptionPane.showMessageDialog(null, "El DNI no es válido", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (nombre.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe introducir el nombre del trabajador", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (apellidos.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe introducir los apellidos del trabajador", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (direccion.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe introducir la dirección del trabajador", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (telefono.isEmpty() || telefono.length() != 9 || !telefono.matches("\\d+")) {
			JOptionPane.showMessageDialog(null, "El teléfono debe tener 9 dígitos numéricos", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (puesto.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe introducir el puesto del trabajador", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

}
