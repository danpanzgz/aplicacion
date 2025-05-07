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

	private Empresa empresa;
	private JTable tabla;
	private JButton guardar;
	private JButton cerrar;
	private final String[] columnas = { "Identificador", "DNI", "Nombre", "Apellidos", "Dirección", "Teléfono",
			"Puesto" };
	private String[][] datos;

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

		if (tabla != null) {
			remove(tabla.getParent()); // eliminar JScrollPane anterior
		}

		DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column != 0; // ID no editable
			}
		};

		tabla = new JTable(modelo);

		// Editor con JComboBox para la columna "Puesto"
		String[] puestos = { "Administrativo", "Gerente", "Técnico", "Otro" };
		JComboBox<String> comboBox = new JComboBox<>(puestos);
		tabla.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(comboBox));

		JScrollPane jsp = new JScrollPane(tabla);
		jsp.setPreferredSize(new Dimension(700, 600));
		add(jsp);

		revalidate();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == guardar) {
			if (tabla.isEditing()) {
				tabla.getCellEditor().stopCellEditing();
			}

			// Guardar los cambios de la tabla en la base de datos
			for (int i = 0; i < tabla.getRowCount(); i++) {
				int id = Integer.parseInt(tabla.getValueAt(i, 0).toString());
				String dni = tabla.getValueAt(i, 1).toString();
				String nombre = tabla.getValueAt(i, 2).toString();
				String apellidos = tabla.getValueAt(i, 3).toString();
				String direccion = tabla.getValueAt(i, 4).toString();
				String telefono = tabla.getValueAt(i, 5).toString();
				String puesto = tabla.getValueAt(i, 6).toString();

				Trabajador t = new Trabajador(id, dni, nombre, apellidos, direccion, telefono, puesto);
				try {
					boolean actualizado = GestionTrabajadores.modificaTrabajador(t);
					if (!actualizado) {
						JOptionPane.showMessageDialog(this, "Error al actualizar el trabajador con ID: " + id);
					}
				} catch (BDException ex) {
					JOptionPane.showMessageDialog(this, "Error al guardar los cambios: " + ex.getMessage());
				}
			}
			JOptionPane.showMessageDialog(this, "Los cambios se han guardado correctamente.");
			try {
				cargarTabla(); // Recargar tabla con los datos actualizados
			} catch (BDException ex) {
				JOptionPane.showMessageDialog(this, "Error al recargar la tabla: " + ex.getMessage());
			}
		} else if (e.getSource() == cerrar) {
			dispose(); // Cierra el diálogo
		}
	}
}
