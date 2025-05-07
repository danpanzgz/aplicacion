package modelo;

import java.util.ArrayList;
import java.util.List;

import dao.GestionTrabajadores;
import exceptions.BDException;

public class Empresa {

	private ArrayList<Trabajador> trabajadores;

	public Empresa() {
		// No se necesita lista interna

	}

	/**
	 * Da de alta un trabajador en la base de datos.
	 * 
	 * @param t El trabajador a insertar.
	 * @return true si se insertó correctamente, false si ya existe el ID.
	 * @throws BDException si hay un error de base de datos.
	 */
	public boolean altaTrabajador(Trabajador t) throws BDException {
		return GestionTrabajadores.altaTrabajador(t);
	}

	/**
	 * Da de baja un trabajador por su ID.
	 * 
	 * @param codigo ID del trabajador a eliminar.
	 * @return true si fue eliminado correctamente, false si no se encontró.
	 * @throws BDException si hay un error de base de datos.
	 */
	public boolean bajaTrabajador(int id) throws BDException {
	    boolean eliminado = GestionTrabajadores.bajaTrabajador(id); // Llamamos a la eliminación en la base de datos
	    if (eliminado) {
	        // Actualizamos la lista de trabajadores después de la eliminación
	        trabajadores.removeIf(trabajador -> trabajador.getIdentificador() == id);
	    }
	    return eliminado;
	}

	/**
	 * Devuelve una matriz con todos los trabajadores para mostrar en tabla.
	 * 
	 * @return matriz de Strings con los datos de los trabajadores.
	 * @throws BDException si ocurre un error al consultar.
	 */
	public String[][] listarTrabajadores() throws BDException {
		return GestionTrabajadores.listarTrabajadores();
	}

	/**
	 * Valida si un DNI es correcto.
	 * 
	 * @param dni El DNI a comprobar.
	 * @return true si es válido, false en caso contrario.
	 */
	public boolean validarDNI(String dni) {
		return GestionTrabajadores.validarDNI(dni);
	}

	public Empresa(ArrayList<Trabajador> trabajadores) {
		this.trabajadores = trabajadores;
	}

	public ArrayList<Trabajador> getTrabajadores() {
		if (trabajadores == null) {
			trabajadores = new ArrayList<>();
		}
		return trabajadores;
	}
	
	public Trabajador buscarTrabajadorPorID(int id) {
	    for (Trabajador t : trabajadores) {
	        if (t.getIdentificador() == id) {
	            return t;
	        }
	    }
	    return null;
	}

	public Trabajador buscarTrabajadorPorDNI(String dni) {
	    for (Trabajador t : trabajadores) {
	        if (t.getDni().equalsIgnoreCase(dni)) {
	            return t;
	        }
	    }
	    return null;
	}
}
