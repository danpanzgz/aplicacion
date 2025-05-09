
package gui;

import java.awt.GridLayout;
import javax.swing.UIManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import dialogs.AltaDialog;
import dialogs.BajaDialog;
import dialogs.ListarDialog;
import dialogs.ModificaDialog;
import dialogs.VerDialog;
import exceptions.BDException;
import ficheros.FicheroDatos;
import modelo.Empresa;

/**
 * 
 * @author usuario
 *
 */
public class EmpresaGUI extends JFrame implements ActionListener {

	Empresa empresa;
	
	JButton altaTrabajador;
	JButton bajaTrabajador;
	JButton modificaTrabajador;
	JButton buscaTrabajador;
	JButton listarTrabajadores;
	JButton salir;

	public EmpresaGUI() {
		super("Gestión de personal");

		empresa = new Empresa();

		// Tamaño JFrame
		setSize(800, 750);
		// Cerrar al salir
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout(3, 2));
		setLocationRelativeTo(null);
		// Creación de los botones y se añaden al JFrame
		// Se añade una imagen para cada botón
		altaTrabajador = new JButton("Añadir Trabajador");
		altaTrabajador.addActionListener(this);
		altaTrabajador.setIcon(new ImageIcon("images/addUser.png"));
		add(altaTrabajador);

		bajaTrabajador = new JButton("Borrar Trabajador");
		bajaTrabajador.addActionListener(this);
		bajaTrabajador.setIcon(new ImageIcon("images/removeUser.png"));
		add(bajaTrabajador);

		modificaTrabajador = new JButton("Modificar Trabajador");
		modificaTrabajador.addActionListener(this);
		modificaTrabajador.setIcon(new ImageIcon("images/editUser.png"));
		add(modificaTrabajador);

		buscaTrabajador = new JButton("Buscar Trabajador");
		buscaTrabajador.addActionListener(this);
		buscaTrabajador.setIcon(new ImageIcon("images/searchUser.png"));
		add(buscaTrabajador);

		listarTrabajadores = new JButton("Listar Trabajadores");
		listarTrabajadores.addActionListener(this);
		listarTrabajadores.setIcon(new ImageIcon("images/list.png"));
		add(listarTrabajadores);

		salir = new JButton("Salir");
		salir.addActionListener(this);
		salir.setIcon(new ImageIcon("images/exit.png"));
		add(salir);
		// Visible
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == altaTrabajador) {
			new AltaDialog(empresa);
		} else if (e.getSource() == bajaTrabajador) {
			try {
				new BajaDialog(empresa);
			} catch (BDException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == modificaTrabajador) {
			try {
				new ModificaDialog(empresa);
			} catch (BDException e1) { 
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == buscaTrabajador) {
			new VerDialog(empresa);
		} else if (e.getSource() == listarTrabajadores) {
			try {
				new ListarDialog(empresa);
			} catch (BDException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		}
		// Cuando se sale se vuelca a fichero.
		else if (e.getSource() == salir) {
			FicheroDatos.escribirTrabajadores("ficheroDatos\\empresa.dat", empresa.getTrabajadores());
			System.exit(0);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 try {
		        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		new EmpresaGUI();
	}

}
