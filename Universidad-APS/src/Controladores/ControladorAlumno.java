package Controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import Conector.DriverBD;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import Modelos.Alumno;
import Modelos.Modelo;
import quick.dbtable.DBTable;

public class ControladorAlumno implements ControladorModelo {
	
	private static ControladorAlumno instancia = null;
	
	/**
	 * controlador: retorna la instancia asociada al controlador
	 * @return controlador asociado a alumnos
	 */
	public static ControladorAlumno controlador () {
		if (instancia == null) {
			instancia = new ControladorAlumno();
		}
		return instancia;
	}
	
	/**
	 * registrar: permite el registro de un alumno en base a datos de entrada asociados a sus
	   atributos
	 * @param inputs: arreglo de atributos del nuevo alumno a registrar
	 */
	public void registrar (String [] inputs) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserci�n
			sentenciaSQL = "INSERT INTO alumnos (dni, nombre, apellido, genero) VALUES " +
					"(" + Integer.parseInt(inputs[0]) + "," +
					"\'" + inputs[1] + "\'," +
					"\'" + inputs[2] + "\'," +
					"\'" + inputs[3] + "\');";
			// Ejecuto la inserci�n del nuevo alumno
			DriverBD.driver().nuevaConexion();
			DriverBD.driver().actualizar(sentenciaSQL);
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
			
		}
		if (!solicitudExitosa) {
			throw new DBUpdateException("�ERROR! fall� el registro del nuevo alumno.\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * modificar: modifica datos de registro de un alumno en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos del nuevo alumno a modificar
	 */
	public void modificar (String [] inputs) throws DBUpdateException {
		String sentenciaSQL = "";
		String asignacionesSQL = null;
		String mensajeError = null;
		boolean solicitudExitosa = true;	
		try
		{
			asignacionesSQL = generarAsignacionesSQL(inputs);
			
			if (asignacionesSQL.length() > 0) {
				sentenciaSQL = "UPDATE alumnos SET " + asignacionesSQL + " WHERE (LU = " + inputs[0] + ")";
				DriverBD.driver().nuevaConexion();
				DriverBD.driver().actualizar(sentenciaSQL);
				DriverBD.driver().cerrarConexion();
			}		
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBUpdateException("�ERROR! fall� la modificaci�n de datos del alumno con LU: " + inputs[0] + ".\n" +
					"Detalle: " + mensajeError);
		}
	}
	
	
	/**
	 * eliminar: elimina datos de registro de un alumno en base a datos
	   de entrada asociados a sus atributos
	 * @param inputs: arreglo de atributos del nuevo alumno a eliminar
	 */
	public void eliminar (String input) throws DBUpdateException {
		String sentenciaSQL;
		String mensajeError = null;
		boolean solicitudExitosa = true;
		try
		{
			// Genero la sentencia de inserci�n
			sentenciaSQL = "DELETE FROM alumnos WHERE ( LU = " + input + ")";
			// Ejecuto la inserci�n del nuevo alumno
			DriverBD.driver().nuevaConexion();
			DriverBD.driver().actualizar(sentenciaSQL);
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
			
		}
		if (!solicitudExitosa) {
			throw new DBUpdateException("�ERROR! fall� la baja del alumno con LU: " + input + ".\n" +
					"Detalle: " + mensajeError);
		}		
	}
	
	
	/**
	 * volcar: solicita la carga de datos asociados a los alumnos registrados en
	   el sistema.
	 * tb: objeto DBTable (tabla de datos) donde se aplicar� la carga de los datos de alumnos.
	 */
	public void volcar (DBTable tb) throws DBRetrieveException {
		String sentenciaSQL;
		boolean consultaExitosa = true;
		
		try
	    {
			sentenciaSQL = "SELECT * FROM alumnos";
			DriverBD.driver().volcar(tb,sentenciaSQL);
	    }		
		catch (SQLException ex)
		{
			consultaExitosa = false;
	    }
		// Si la consulta no fue exitosa, entonces fall� el volcado de datos
		if (!consultaExitosa) {
			throw new DBRetrieveException("�ERROR! fall� el volcado de los datos de alumnos en la tabla");
		}
	}
	
	
	public Alumno recuperar (String [] inputs) throws DBRetrieveException {
		String sentenciaSQL;
		String asignacionesSQL = null;
		String mensajeError = null;
		Alumno alumno = null;		
		boolean solicitudExitosa = true;	
		try
		{
			asignacionesSQL = generarAsignacionesSQL(inputs);
			sentenciaSQL = "SELECT * FROM alumnos WHERE (LU = " + inputs[0];
			// Si hay asignaciones adicionales, las incorporo a la sentencia
			if (asignacionesSQL.length() > 0) {
				sentenciaSQL +=  ", " + asignacionesSQL;
			}
			sentenciaSQL += ")";
			DriverBD.driver().nuevaConexion();
			ResultSet rs = DriverBD.driver().consultar(sentenciaSQL);
			// Si hay un resultado siguiente, lo recupero
			if (rs.next()) {
				alumno = Alumno.extraerModelo(rs);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("�ERROR! fall� la recuperaci�n del alumno con par�metros " + Arrays.deepToString(inputs) + ".\n" +
					"Detalle: " + mensajeError);
		}
		return alumno;
	}
	
	
	private String generarAsignacionesSQL (String [] inputs) {
		String asignacionesSQL = "";
		boolean hayAsignacionAnterior = false;
		// Genero la sentencia de inserci�n
		if (inputs[1] != null) {
			asignacionesSQL += "dni = " + inputs[1];
			hayAsignacionAnterior = true;
		}
		if (inputs[2] != null) {
			if (hayAsignacionAnterior) {
				asignacionesSQL += ", ";				
			}
			else hayAsignacionAnterior = true;
			asignacionesSQL += "nombre = \'" + inputs[2] + "\'";
			
		}
		if (inputs[3] != null) {
			if (hayAsignacionAnterior) {
				asignacionesSQL += ", ";
			}
			else hayAsignacionAnterior = true;
			asignacionesSQL += "apellido = \'" + inputs[3] + "\'";
		}
		if (inputs[4] != null) {
			if (hayAsignacionAnterior) {
				asignacionesSQL += ", ";
			}
			else hayAsignacionAnterior = true;
			asignacionesSQL += "genero = \'" + inputs[4] + "\'";
		}
		return asignacionesSQL;
	}
	
	
	/**
	 * elementos: permite obtener una lista de modelos de alumnos, asociados a
	   las alumnos registradas en la base de datos
	 * @return lista de modelos de alumnos
	 */
	public List<Modelo> elementos () throws DBRetrieveException {
		List<Modelo> listaAlumnos = new LinkedList<Modelo>();
		ResultSet rs;
		Alumno alumno;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;	
		try
		{
			sentenciaSQL = "SELECT * FROM alumnos";
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);
			// Mientras haya un resultado siguiente, lo recupero
			while (rs.next()) {
				alumno = Alumno.extraerModelo(rs);
				listaAlumnos.add(alumno);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("�ERROR! fall� la recuperaci�n todos los alumnos.\n" +
					"Detalle: " + mensajeError);
		}
		return listaAlumnos;
	}
	
	
	public List<Alumno> alumnosDelDictado (int idDict) throws DBRetrieveException {
		List<Alumno> listaAlumnos = new LinkedList<Alumno>();
		ResultSet rs;
		Alumno alumno = null;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;
		try
		{
			sentenciaSQL = "SELECT LU, dni, nombre, apellido, genero " +
					"FROM (\r\n" + 
					"				SELECT *\r\n" + 
					"				FROM inscripciones_dictados\r\n" + 
					"				WHERE (id_dictado = " + idDict + ")\r\n" + 
					"			) as ID\r\n" + 
					"			NATURAL JOIN\r\n" + 
					"			alumnos as A\r\n" + 
					"WHERE (ID.LU_alumno = A.LU);";
			
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);			
			while (rs.next()) {
				alumno = Alumno.extraerModelo(rs);
				listaAlumnos.add(alumno);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("�ERROR! fall� la recuperaci�n todas los alumnos del dictado con ID: " + idDict + ".\n" +
					"Detalle: " + mensajeError);
		}
		return listaAlumnos;
	}
	
	
	public List<Alumno> alumnosEnMesa (int idMesa) throws DBRetrieveException {
		List<Alumno> listaAlumnos = new LinkedList<Alumno>();
		ResultSet rs;
		Alumno alumno = null;
		String sentenciaSQL;
		String mensajeError = null;	
		boolean solicitudExitosa = true;
		try
		{
			sentenciaSQL = "SELECT DISTINCT LU, dni, nombre, apellido, genero\r\n" + 
					"FROM (\r\n" + 
					"				SELECT LU_alumno\r\n" + 
					"				FROM inscripciones_finales\r\n" + 
					"				WHERE (id_mesa = " + idMesa + ")\r\n" + 
					"			) as I\r\n" + 
					"			NATURAL JOIN\r\n" + 
					"			alumnos\r\n" + 
					"WHERE (LU = LU_alumno);";
			
			DriverBD.driver().nuevaConexion();
			rs = DriverBD.driver().consultar(sentenciaSQL);			
			while (rs.next()) {
				alumno = Alumno.extraerModelo(rs);
				listaAlumnos.add(alumno);
			}
			DriverBD.driver().cerrarConexion();
		}
		catch (SQLException ex)
		{
			solicitudExitosa = false;
			mensajeError = ex.getMessage();
		}
		if (!solicitudExitosa) {
			throw new DBRetrieveException("�ERROR! fall� la recuperaci�n todas los alumnos de la mesa de examen con ID: " + idMesa + ".\n" +
					"Detalle: " + mensajeError);
		}
		return listaAlumnos;
	}

}
