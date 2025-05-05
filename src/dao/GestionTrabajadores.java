package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.ConfigSQLite;
import exceptions.BDException;
import modelo.Trabajador;

public class GestionTrabajadores {
	public static boolean altaTrabajador(Trabajador t) throws BDException {
		String sql = "INSERT INTO empleados (id, dni, nombre, apellido, direccion, telefono, puesto) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = ConfigSQLite.abrirConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, t.getIdentificador());
			pstmt.setString(2, t.getDni());
			pstmt.setString(3, t.getNombre());
			pstmt.setString(4, t.getApellidos());
			pstmt.setString(5, t.getDireccion());
			pstmt.setString(6, t.getTelefono());
			pstmt.setString(7, t.getPuesto());

			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			if (e.getMessage().contains("UNIQUE") || e.getMessage().contains("PRIMARY")) {
				return false; // ya existe el ID
			} else {
				throw new BDException("Error al insertar trabajador: " + e.getMessage());
			}
		}
	}

	public static boolean bajaTrabajador(int id) throws BDException {
		String sql = "DELETE FROM empleados WHERE id = ?";
		try (Connection conn = ConfigSQLite.abrirConexion(); 
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);
			int filasAfectadas = pstmt.executeUpdate();

			return filasAfectadas > 0;

		} catch (SQLException e) {
			throw new BDException("Error al eliminar trabajador: " + e.getMessage());
		}
	}
	
	public static String[][] listarTrabajadores() throws BDException {
	    List<String[]> lista = new ArrayList<>();
	    String sql = "SELECT * FROM empleados";

	    try (Connection conn = ConfigSQLite.abrirConexion();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            String[] fila = new String[7];
	            fila[0] = String.valueOf(rs.getInt("id"));
	            fila[1] = rs.getString("dni");
	            fila[2] = rs.getString("nombre");
	            fila[3] = rs.getString("apellido");
	            fila[4] = rs.getString("direccion");
	            fila[5] = rs.getString("telefono");
	            fila[6] = rs.getString("puesto");
	            lista.add(fila);
	        }

	    } catch (SQLException e) {
	        throw new BDException("Error al listar trabajadores: " + e.getMessage());
	    }

	    return lista.toArray(new String[0][0]);
	}

	public static boolean validarDNI(String dni) {
		if (dni == null || dni.length() != 9) {
			return false;
		}

		String numeros = dni.substring(0, 8);
		char letra = Character.toUpperCase(dni.charAt(8));

		if (!numeros.matches("\\d+")) {
			return false;
		}

		// Letras válidas según el módulo 23 del número
		String letrasValidas = "TRWAGMYFPDXBNJZSQVHLCKE";
		int numero = Integer.parseInt(numeros);
		char letraCorrecta = letrasValidas.charAt(numero % 23);

		return letra == letraCorrecta;

	}
	public static boolean validarTelefono(String telefono) {
	    // Elimina espacios y guiones opcionales
	    telefono = telefono.replaceAll("[\\s-]", "");

	    // Expresión regular:
	    // - Opcionalmente empieza con +34
	    // - Luego un 6, 7, 8 o 9 (móviles y fijos)
	    // - Seguido de 8 dígitos
	    return telefono.matches("^(\\+34)?[6789]\\d{8}$");
	}

}