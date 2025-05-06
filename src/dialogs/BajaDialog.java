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
public class BajaDialog extends JDialog implements ActionListener {

	Empresa empresa;
    JTable tabla;
    JButton baja;
    JButton cerrar;
   

	public BajaDialog(Empresa empresa) throws BDException {
		this.empresa = empresa;

        setResizable(false);
        setTitle("Listado Trabajadores");
        setSize(750, 700);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        cargarTabla(); // ← Método para cargar la tabla al principio

        baja = new JButton("Dar de baja");
        baja.addActionListener(this);
        add(baja);

        cerrar = new JButton("Cerrar");
        cerrar.addActionListener(this);
        add(cerrar);

        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
	
	 private void cargarTabla() throws BDException{
	    	// Crea un JTable, cada fila será un trabajador
		 			String[] columnas = { "Identificador", "DNI", "Nombre", "Apellidos", "Dirección", "Teléfono", "Puesto" };
	    			String[][] datos = GestionTrabajadores.listarTrabajadores();
	    			
	    			if (tabla != null) {
	                    remove(tabla.getParent()); // Eliminar el JScrollPane antiguo
	                }
	    			
	    			tabla = new JTable(datos, columnas);
	    			// Mete la tabla en un JCrollPane
	    			JScrollPane jsp = new JScrollPane(tabla);
	    			jsp.setPreferredSize(new Dimension(700, 600));
	    			add(jsp);
	    			
	    			revalidate();
	    			repaint();

	    }



	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource() == baja) {
			int filaSeleccionada = tabla.getSelectedRow();
			
			if (filaSeleccionada == -1) {
				JOptionPane.showMessageDialog(this, "Debe seleccionar un trabajador para darlo de baja");
				return;
			}
			
			try {
				int id = Integer.parseInt(tabla.getValueAt(filaSeleccionada, 0).toString());
				
				int confirmacion = JOptionPane.showConfirmDialog(this, 
						"Estas seguro de dar de baja el trabajador con el id " + id + " ?", "Confirmar baja", JOptionPane.YES_NO_OPTION);
						
				if ( confirmacion == JOptionPane.YES_OPTION) {
					if (empresa.bajaTrabajador(id)) {
						JOptionPane.showMessageDialog(this, "Trabajador eliminado correctamente");
						cargarTabla();
					} else {
						JOptionPane.showMessageDialog(this, "No se pudo dar de baja", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error al conseguir el ID del trabajador", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == cerrar) {
			dispose();
		

		}

	}
}
