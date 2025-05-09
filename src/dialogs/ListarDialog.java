/**
 * 
 */
package dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import dao.GestionTrabajadores;
import exceptions.BDException;
import modelo.Empresa;

/**
 * 
 * @author usuario
 *
 */
public class ListarDialog extends JDialog implements ActionListener {

	Empresa empresa;
	JTable tabla;
	JButton cerrar;
	JButton exportarCSV;
	JButton exportarJSON;

	public ListarDialog(Empresa empresa) throws BDException {
		this.empresa = empresa;

		setResizable(false);
		// t�tulo del di�log
		setTitle("Listado Trabajadores");
		// tama�o
		setSize(750, 700);
		setLayout(new FlowLayout());
		// colocaci�n en el centro de la pantalla
		setLocationRelativeTo(null);

		// Crea un JTable, cada fila será un trabajador
		String[] columnas = { "Identificador", "DNI", "Nombre", "Apellidos", "Direcci�n", "Tel�fono", "Puesto" };
		String[][] datos = empresa.listarTrabajadores();
		tabla = new JTable(datos, columnas);
		// Mete la tabla en un JCrollPane
		JScrollPane jsp = new JScrollPane(tabla);
		jsp.setPreferredSize(new Dimension(700, 600));
		add(jsp);

		cerrar = new JButton("Cerrar");
		cerrar.addActionListener(this);
		add(cerrar);

		exportarCSV = new JButton("Exportar a CSV");
		exportarCSV.addActionListener(this);
		add(exportarCSV);

		exportarJSON = new JButton("Exportar a JSON");
		exportarJSON.addActionListener(this);
		add(exportarJSON);

		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cerrar) {
			dispose();
		} else if (e.getSource() == exportarCSV) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Guardar como CSV");
			int seleccion = fileChooser.showSaveDialog(this);

			if (seleccion == JFileChooser.APPROVE_OPTION) {
				String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();

				if (!rutaArchivo.toLowerCase().endsWith(".csv")) {
					rutaArchivo += ".csv";
				}

				try {
					GestionTrabajadores.exportarACSV(rutaArchivo);
					JOptionPane.showMessageDialog(this, "Archivo exportado correctamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (BDException ex) {
					JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (e.getSource() == exportarJSON) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Guardar como JSON");
			int seleccion = fileChooser.showSaveDialog(this);

			if (seleccion == JFileChooser.APPROVE_OPTION) {
				String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();

				if (!rutaArchivo.toLowerCase().endsWith(".json")) {
					rutaArchivo += ".json";
				}
				try {
					GestionTrabajadores.exportarTrabajadoresAJSON(rutaArchivo);
					JOptionPane.showMessageDialog(this, "Archivo exportado correctamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (BDException ex) {
					JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

	}
}