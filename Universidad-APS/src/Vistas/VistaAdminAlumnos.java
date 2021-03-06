package Vistas;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import Controladores.ControladorAlumno;
import Controladores.ControladorVistas;
import Excepciones.DBRetrieveException;
import Excepciones.DBUpdateException;
import Modelos.Alumno;
import quick.dbtable.DBTable;

public class VistaAdminAlumnos extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static VistaAdminAlumnos instancia = null;
	private DBTable tabla;
	private JTextField txtNombre;
	protected int seleccionado = -1;
	
	/**
	 * vista: retorna la instancia de la vista de administraci�n de alumnos
	 * @return panel de la vista de administraci�n de alumnos
	 */
	public static VistaAdminAlumnos vista () {
		if (instancia == null) {
			instancia = new VistaAdminAlumnos();
		}
		return instancia;
	}
	
	/**
	 * CONSTRUCTOR: Vista para la administraci�n de alumnos
	 */ 	
	private VistaAdminAlumnos() {
		
		this.setBackground(SystemColor.control);
		this.setBounds(0, 0, 1194, 699);
		this.setLayout(null);
		
		JPanel panelTabla = new JPanel();
		panelTabla.setBackground(SystemColor.controlHighlight);
		panelTabla.setBounds(10, 232, 1174, 456);
		this.add(panelTabla);
		
		JButton btnRegAlumno = new JButton("Registrar alumno");
		btnRegAlumno.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 13));
		btnRegAlumno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaRegAlumno();
			}
		});
		btnRegAlumno.setBounds(328, 85, 167, 40);
		this.add(btnRegAlumno);
		
		tabla = new DBTable();
		tabla.setBounds(249, 5, 663, 427);
        
		tabla.addKeyListener(new KeyAdapter() {
           public void keyTyped(KeyEvent evt) {
              tablaKeyTyped(evt);
           }
        });
        
        tabla.addMouseListener(new MouseAdapter() {
           public void mouseClicked(MouseEvent evt) {
              tablaMouseClicked(evt);
           }
    	});
        panelTabla.setLayout(null);
        
    	// Agregar la tabla al frame (no necesita JScrollPane como Jtable)
        panelTabla.add(tabla);           
        tabla.setEditable(false);
        
        JButton btnModAlumno = new JButton("Modificar alumno");
        btnModAlumno.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaBuscarAlumno();
        	}
        });
        btnModAlumno.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnModAlumno.setBounds(505, 85, 167, 40);
        add(btnModAlumno);
        
        JButton btnBajaAlumno = new JButton("Dar de baja alumno");
        btnBajaAlumno.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new VentanaElimAlumno();
        	}
        });
        btnBajaAlumno.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnBajaAlumno.setBounds(682, 85, 167, 40);
        add(btnBajaAlumno);
        
        JButton btnAtras = new JButton("Atr\u00E1s");
        btnAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// Vuelvo a la vista anterior, la vista de administraci�n
        		ControladorVistas.controlador().mostrar(VistaAdmin.vista());
        	}
        });
        btnAtras.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
        btnAtras.setBounds(10, 11, 70, 23);
        add(btnAtras);        
             
        actualizarTabla();
	}
	
	
	private void tablaMouseClicked(MouseEvent evt) 
	{
		if ((this.tabla.getSelectedRow() != -1) && (evt.getClickCount() == 2)) 
		{
			this.seleccionarFila();
		}
	}
	
	
	private void tablaKeyTyped(KeyEvent evt) {
		if ((this.tabla.getSelectedRow() != -1) && (evt.getKeyChar() == ' ')) 
		{
	         this.seleccionarFila();
	    }
	}
	
	
	private void seleccionarFila()
	{
		this.seleccionado = this.tabla.getSelectedRow();
	    this.txtNombre.setText(this.tabla.getValueAt(this.tabla.getSelectedRow(), 0).toString());
	}
	
	/**
	 * actualizarTabla: permite actualizar el contenido de la db table para alumnos,
	   con todos los alumnos registrados y sus datos.
	 */
	private void actualizarTabla () {
		try
	    {
			ControladorAlumno.controlador().volcar(tabla);
	    }		
		catch (DBRetrieveException ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Volcado de datos de alumnos", JOptionPane.ERROR_MESSAGE);
	    }
	}	
	
	
	// CLASE PARA LA VENTANA DE INPUTS PARA EL REGISTRO
	
	
	private class VentanaRegAlumno extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputDNI;	
		private JTextField inputNombre;
		private JTextField inputApellido;
		private JComboBox<String> inputGenero;
		private JTextField inputMail;
		private JTextField inputTelefono;
		private JTextField inputCalle;
		private JTextField inputNum;
		private JTextField inputPiso;
		private JTextField inputDepto;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para el registro de un nuevo alumno
		
		public VentanaRegAlumno() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Registro de nuevo alumno");
			setMaximumSize(new Dimension(322, 430));
			setMinimumSize(new Dimension(322, 430));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACI�N DE INPUTS: registro de alumno
			
			inputDNI = new JTextField();
			inputDNI.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputDNI.setBounds(125, 24, 124, 20);
			getContentPane().add(inputDNI);
			inputDNI.setColumns(10);
			
			inputNombre = new JTextField();
			inputNombre.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputNombre.setColumns(10);
			inputNombre.setBounds(124, 55, 125, 20);
			getContentPane().add(inputNombre);
			
			inputApellido = new JTextField();
			inputApellido.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputApellido.setColumns(10);
			inputApellido.setBounds(124, 88, 125, 20);
			getContentPane().add(inputApellido);
			
			inputGenero = new JComboBox<String>();
			inputGenero.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputGenero.setBounds(125, 119, 124, 20);
			inputGenero.addItem("Masculino");
			inputGenero.addItem("Femenino");
			getContentPane().add(inputGenero);
			
			inputMail = new JTextField();
			inputMail.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputMail.setEnabled(false);
			inputMail.setColumns(10);
			inputMail.setBounds(124, 153, 125, 20);
			getContentPane().add(inputMail);
			
			inputTelefono = new JTextField();
			inputTelefono.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputTelefono.setEnabled(false);
			inputTelefono.setColumns(10);
			inputTelefono.setBounds(124, 184, 125, 20);
			getContentPane().add(inputTelefono);
			
			inputCalle = new JTextField();
			inputCalle.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputCalle.setEnabled(false);
			inputCalle.setColumns(10);
			inputCalle.setBounds(124, 215, 125, 20);
			getContentPane().add(inputCalle);
			
			inputNum = new JTextField();
			inputNum.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputNum.setEnabled(false);
			inputNum.setColumns(10);
			inputNum.setBounds(124, 246, 125, 20);
			getContentPane().add(inputNum);
			
			inputPiso = new JTextField();
			inputPiso.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputPiso.setEnabled(false);
			inputPiso.setColumns(10);
			inputPiso.setBounds(124, 277, 125, 20);
			getContentPane().add(inputPiso);
			
			inputDepto = new JTextField();
			inputDepto.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputDepto.setEnabled(false);
			inputDepto.setColumns(10);
			inputDepto.setBounds(125, 308, 124, 20);
			getContentPane().add(inputDepto);
			
			// CREACI�N DE LABELS: registro de alumno
			
			JLabel lblDNI = new JLabel("DNI:");		
			lblDNI.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblDNI.setBounds(74, 24, 41, 20);
			getContentPane().add(lblDNI);
			
			JLabel lblNombre = new JLabel("Nombre:");
			lblNombre.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblNombre.setBounds(56, 55, 59, 20);
			getContentPane().add(lblNombre);
			
			JLabel lblApellido = new JLabel("Apellido:");
			lblApellido.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblApellido.setBounds(56, 88, 59, 20);
			getContentPane().add(lblApellido);
			
			JLabel lblGenero = new JLabel("G\u00E9nero:");
			lblGenero.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblGenero.setBounds(58, 119, 57, 20);
			getContentPane().add(lblGenero);
			
			JLabel lblMail = new JLabel("Mail:");
			lblMail.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblMail.setBounds(74, 153, 41, 20);
			getContentPane().add(lblMail);
			
			JLabel lblTelfono = new JLabel("Tel\u00E9fono:");
			lblTelfono.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblTelfono.setBounds(44, 184, 71, 20);
			getContentPane().add(lblTelfono);
			
			JLabel lblCalle = new JLabel("Calle:");
			lblCalle.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblCalle.setBounds(72, 215, 41, 20);
			getContentPane().add(lblCalle);
			
			JLabel lblNum = new JLabel("N\u00B0:");
			lblNum.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblNum.setBounds(74, 246, 41, 20);
			getContentPane().add(lblNum);
			
			JLabel lblPiso = new JLabel("Piso:");
			lblPiso.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblPiso.setBounds(74, 277, 39, 20);
			getContentPane().add(lblPiso);
			
			JLabel lblDepto = new JLabel("Depto:");
			lblDepto.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblDepto.setBounds(56, 308, 59, 20);
			getContentPane().add(lblDepto);
			
			btnSiguiente = new JButton("Guardar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {					
					registrarAlumno();
					dispose();
				}
			});
			btnSiguiente.setBounds(95, 354, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		
		/**
		 * registrarAlumno: solicita el registro de un nuevo alumno en el sistema
		 */
		private void registrarAlumno () {
			String [] inputs = {
					inputDNI.getText(), 
					inputNombre.getText(),
					inputApellido.getText(),
					(String) inputGenero.getSelectedItem()
			};
			try 
			{
				ControladorAlumno.controlador().registrar(inputs);
				JOptionPane.showMessageDialog(this,"Alumno registrado exitosamente");
				actualizarTabla();
			}
			catch (DBUpdateException ex)
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Modificaci�n de una carrera", JOptionPane.ERROR_MESSAGE);
			}			
			dispose();			
		}
	}
	
	
	// CLASE PARA LA VENTANA DE B�SQUEDA DE ALUMNO PARA SU MODIFICACI�N	
	
	private class VentanaBuscarAlumno extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JTextField inputLU;
		private JButton btnSiguiente;
		
		
		// CONSTRUCTOR: Ventana para el registro de un nuevo alumno		
		public VentanaBuscarAlumno() {
			super();
			getContentPane().setLayout(null);
	        setVisible(true);
	        
	        getContentPane().setBackground(SystemColor.controlHighlight);
			setTitle("Modificaci�n de un alumno");
			setSize(new Dimension(353, 170));
			setResizable(false);		
			setLocationRelativeTo(null);
				
			// CREACI�N DE INPUTS: registro de alumno
			
			inputLU = new JTextField();
			inputLU.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			inputLU.setBounds(128, 52, 100, 20);
			getContentPane().add(inputLU);
			
			// CREACI�N DE LABELS: registro de alumno
			
			JLabel lblLU = new JLabel("LU:");		
			lblLU.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
			lblLU.setBounds(89, 52, 29, 20);
			getContentPane().add(lblLU);
			
			btnSiguiente = new JButton("A modificar");
			btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {						
					abrirEdicionAlumno();
					dispose();
				}
			});
			btnSiguiente.setBounds(115, 95, 124, 23);
			getContentPane().add(btnSiguiente);
			
		}
		
		/**
		 * abrirEdicionAlumno: abre una nueva ventana que permita ingresar datos de entrada
		   para la edici�n de datos de registro asociados a un alumno
		 */
		private void abrirEdicionAlumno () {
			String [] inputs = {inputLU.getText(), null, null, null, null};
			Alumno alumno = null;
			try
			{
				alumno = ControladorAlumno.controlador().recuperar(inputs);
				if (alumno == null) {
					JOptionPane.showMessageDialog(this,
							"ERROR! No existe un alumno registrado con LU: " + inputLU.getText(),
							"Modificaci�n de un alumno", JOptionPane.ERROR_MESSAGE);
		         }
				else {
					new VentanaEdicionAlumno(alumno);
				}
			}
			catch (DBRetrieveException ex)
			{
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Modificaci�n de una carrera", JOptionPane.ERROR_MESSAGE);
			}
			dispose();			
		}
		
		
		// CLASE ASOCIADA A LA VENTANA PARA INTRODUCIR DATOS PARA MODIFICAR UN ALUMNO
		
		private class VentanaEdicionAlumno extends JFrame {
			
			private static final long serialVersionUID = 1L;
			private JTextField [] inputs;
			private JCheckBox [] checkBoxes;
			private JComboBox<String> switchGenero;
			private JButton btnSiguiente;		
				
			/**
			 * CONSTRUCTOR: Ventana para el registro de un nuevo alumno
			 * @param lu: n�mero de libreta universitaria asociado al alumno a modificar
			 */
			public VentanaEdicionAlumno (Alumno alumno) {
				super();
				getContentPane().setEnabled(false);
				getContentPane().setLayout(null);
				setVisible(true);
				
				getContentPane().setBackground(SystemColor.controlHighlight);
				setTitle("Modificaci�n de un alumno");
				setMaximumSize(new Dimension(322, 430));
				setMinimumSize(new Dimension(322, 430));
				setResizable(false);
				setLocationRelativeTo(null);
				
				// CREACI�N DE INPUTS: modificaci�n de alumno
				inputs = new JTextField[9];
				checkBoxes = new JCheckBox[10];
				// DNI
				inputs[0] = new JTextField();
				inputs[0].setEnabled(false);
				inputs[0].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[0].setBounds(125, 24, 124, 20);
				getContentPane().add(inputs[0]);
				inputs[0].setColumns(10);
				// Nombre
				inputs[1] = new JTextField();
				inputs[1].setEnabled(false);
				inputs[1].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[1].setColumns(10);
				inputs[1].setBounds(124, 55, 125, 20);
				getContentPane().add(inputs[1]);
				// Apellido
				inputs[2] = new JTextField();
				inputs[2].setEnabled(false);
				inputs[2].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[2].setColumns(10);
				inputs[2].setBounds(124, 88, 125, 20);
				getContentPane().add(inputs[2]);
				// Genero
				switchGenero = new JComboBox<String>();
				switchGenero.setEnabled(false);
				switchGenero.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				switchGenero.setBounds(125, 119, 124, 20);
				switchGenero.addItem("Masculino");
				switchGenero.addItem("Femenino");
				getContentPane().add(switchGenero);
				// Mail
				inputs[3] = new JTextField();
				inputs[3].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[3].setEnabled(false);
				inputs[3].setColumns(10);
				inputs[3].setBounds(124, 153, 125, 20);
				getContentPane().add(inputs[3]);
				// Telefono
				inputs[4] = new JTextField();
				inputs[4].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[4].setEnabled(false);
				inputs[4].setColumns(10);
				inputs[4].setBounds(124, 184, 125, 20);
				getContentPane().add(inputs[4]);
				// Calle
				inputs[5] = new JTextField();
				inputs[5].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[5].setEnabled(false);
				inputs[5].setColumns(10);
				inputs[5].setBounds(124, 215, 125, 20);
				getContentPane().add(inputs[5]);
				// Numero
				inputs[6] = new JTextField();
				inputs[6].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[6].setEnabled(false);
				inputs[6].setColumns(10);
				inputs[6].setBounds(124, 246, 125, 20);
				getContentPane().add(inputs[6]);
				// Piso
				inputs[7] = new JTextField();
				inputs[7].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[7].setEnabled(false);
				inputs[7].setColumns(10);
				inputs[7].setBounds(124, 277, 125, 20);
				getContentPane().add(inputs[7]);
				// Depto
				inputs[8] = new JTextField();
				inputs[8].setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputs[8].setEnabled(false);
				inputs[8].setColumns(10);
				inputs[8].setBounds(125, 308, 124, 20);
				getContentPane().add(inputs[8]);
				
				// CREACI�N DE CHECKBOXES:
				
				// DNI
				checkBoxes[0] = new JCheckBox("");
				checkBoxes[0].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[0]);
					}
				});
				checkBoxes[0].setBackground(SystemColor.controlHighlight);
				checkBoxes[0].setBounds(255, 24, 26, 20);
				getContentPane().add(checkBoxes[0]);
				// Nombre
				checkBoxes[1] = new JCheckBox("");
				checkBoxes[1].setBackground(SystemColor.controlHighlight);
				checkBoxes[1].setBounds(255, 55, 26, 20);
				checkBoxes[1].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[1]);
					}
				});
				getContentPane().add(checkBoxes[1]);
				// Apellido
				checkBoxes[2] = new JCheckBox("");
				checkBoxes[2].setBackground(SystemColor.controlHighlight);
				checkBoxes[2].setBounds(255, 88, 26, 20);
				checkBoxes[2].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[2]);
						}
					});
				getContentPane().add(checkBoxes[2]);
				// Genero
				checkBoxes[9] = new JCheckBox("");
				checkBoxes[9].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(switchGenero);
					}
				});
				checkBoxes[9].setBackground(SystemColor.controlHighlight);
				checkBoxes[9].setBounds(255, 119, 26, 20);
				getContentPane().add(checkBoxes[9]);
				// Mail
				checkBoxes[3] = new JCheckBox("");
				checkBoxes[3].setEnabled(false);
				checkBoxes[3].setBackground(SystemColor.controlHighlight);
				checkBoxes[3].setBounds(255, 153, 26, 20);
				checkBoxes[3].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[3]);
					}
				});
				getContentPane().add(checkBoxes[3]);
				// Telefono
				checkBoxes[4] = new JCheckBox("");
				checkBoxes[4].setEnabled(false);
				checkBoxes[4].setBackground(SystemColor.controlHighlight);
				checkBoxes[4].setBounds(255, 184, 26, 20);
				checkBoxes[4].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[4]);
					}
				});
				getContentPane().add(checkBoxes[4]);
				// Calle
				checkBoxes[5] = new JCheckBox("");
				checkBoxes[5].setEnabled(false);
				checkBoxes[5].setBackground(SystemColor.controlHighlight);
				checkBoxes[5].setBounds(255, 215, 26, 20);
				checkBoxes[5].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[5]);
					}
				});
				getContentPane().add(checkBoxes[5]);
				// Numero
				checkBoxes[6] = new JCheckBox("");
				checkBoxes[6].setEnabled(false);
				checkBoxes[6].setBackground(SystemColor.controlHighlight);
				checkBoxes[6].setBounds(255, 246, 26, 20);
				checkBoxes[6].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[6]);
					}
				});
				getContentPane().add(checkBoxes[6]);
				// Piso
				checkBoxes[7] = new JCheckBox("");
				checkBoxes[7].setEnabled(false);
				checkBoxes[7].setBackground(SystemColor.controlHighlight);
				checkBoxes[7].setBounds(255, 277, 26, 20);
				checkBoxes[7].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[7]);
					}
				});
				getContentPane().add(checkBoxes[7]);
				// Depto
				checkBoxes[8] = new JCheckBox("");
				checkBoxes[8].setEnabled(false);
				checkBoxes[8].setBackground(SystemColor.controlHighlight);
				checkBoxes[8].setBounds(255, 308, 26, 20);
				checkBoxes[8].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switchearEstadoInput(inputs[8]);
					}
				});
				getContentPane().add(checkBoxes[9]);
				
				// CREACI�N DE LABELS: registro de alumno
				
				JLabel lblDNI = new JLabel("DNI:");
				lblDNI.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblDNI.setBounds(74, 24, 41, 20);
				getContentPane().add(lblDNI);
				
				JLabel lblNombre = new JLabel("Nombre:");
				lblNombre.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblNombre.setBounds(56, 55, 59, 20);
				getContentPane().add(lblNombre);
				
				JLabel lblApellido = new JLabel("Apellido:");
				lblApellido.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblApellido.setBounds(56, 88, 59, 20);
				getContentPane().add(lblApellido);
				
				JLabel lblGenero = new JLabel("G\u00E9nero:");
				lblGenero.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblGenero.setBounds(58, 119, 57, 20);
				getContentPane().add(lblGenero);
				
				JLabel lblMail = new JLabel("Mail:");
				lblMail.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblMail.setBounds(74, 153, 41, 20);
				getContentPane().add(lblMail);
				
				JLabel lblTelfono = new JLabel("Tel\u00E9fono:");
				lblTelfono.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblTelfono.setBounds(44, 184, 71, 20);
				getContentPane().add(lblTelfono);
				
				JLabel lblCalle = new JLabel("Calle:");
				lblCalle.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblCalle.setBounds(72, 215, 41, 20);
				getContentPane().add(lblCalle);
				
				JLabel lblNum = new JLabel("N\u00B0:");
				lblNum.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblNum.setBounds(74, 246, 41, 20);
				getContentPane().add(lblNum);
				
				JLabel lblPiso = new JLabel("Piso:");
				lblPiso.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblPiso.setBounds(74, 277, 39, 20);
				getContentPane().add(lblPiso);
				
				JLabel lblDepto = new JLabel("Depto:");
				lblDepto.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblDepto.setBounds(56, 308, 59, 20);
				getContentPane().add(lblDepto);
				
				btnSiguiente = new JButton("Guardar");
				btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
				btnSiguiente.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						modificarAlumno(alumno);
						dispose();
					}
				});
				btnSiguiente.setBounds(107, 354, 124, 23);
				getContentPane().add(btnSiguiente);
			}
				
				/**
				 * switchearEstadoInput: permite alternar el estado de un componente
				   que puede ser habilitado o deshabilitado
				 * @param in: componente a habilitar/deshabilitar
				 */
				private void switchearEstadoInput (JComponent in) {
					if (in.isEnabled()) {
						in.setEnabled(false);
					}
					else in.setEnabled(true);
				}
				
				/**
				 * modificarAlumno: permite cambiar los datos de registro para un alumno
				   particular
				 * @param lu: n�mero de libreta universitaria asociado al alumno a modificar
				 */
				private void modificarAlumno (Alumno alumno) {
					String [] in = {alumno.obtenerLU() + "", null, null, null, null};
					boolean hayParaModificar = false;
					try
					{
						// Verifico si se quiere modificar DNI
						if (inputs[0].isEnabled()) {
							in[1] = inputs[0].getText();
							hayParaModificar = true;
						}
						// Verifico si se quiere modificar Nombre
						if (inputs[1].isEnabled()) {
							in[2] = inputs[1].getText();
							hayParaModificar = true;
						}
						// Verifico si se quiere modificar Apellido
						if (inputs[2].isEnabled()) {
							in[3] = inputs[2].getText();
							hayParaModificar = true;
						}
						// Verifico si se quiere modificar Genero
						if (switchGenero.isEnabled()) {
							in[4] = (String) switchGenero.getSelectedItem();
							hayParaModificar = true;
						}
						// Ejecuto la inserci�n del nuevo alumno
						ControladorAlumno.controlador().modificar(in);
						if (hayParaModificar) {
							JOptionPane.showMessageDialog(this,"Alumno con LU: " + alumno.obtenerLU() +" modificado exitosamente");
							actualizarTabla();
						}						
						
					}
					catch (DBUpdateException ex)
					{
						JOptionPane.showMessageDialog(this, ex.getMessage(), "Modificaci�n de un alumno", JOptionPane.ERROR_MESSAGE);
					}
					dispose();
				}		   
		   }		   
		}	
	
	
		// CLASE PARA LA VENTANA DE INPUTS PARA LA BAJA
	
		private class VentanaElimAlumno extends JFrame {
			
			private static final long serialVersionUID = 1L;
			private JTextField inputLU;
			private JButton btnSiguiente;
			
			/**
			 * CONSTRUCTOR: Ventana para el registro de un nuevo alumno
			 */
			public VentanaElimAlumno() {
				super();
				getContentPane().setLayout(null);
		        setVisible(true);
		        
		        getContentPane().setBackground(SystemColor.controlHighlight);
				setTitle("Baja de un alumno");
				setSize(new Dimension(289, 170));
				setResizable(false);		
				setLocationRelativeTo(null);
					
				// CREACI�N DE INPUTS: registro de alumno
				
				inputLU = new JTextField();
				inputLU.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				inputLU.setBounds(95, 52, 100, 20);
				getContentPane().add(inputLU);
				
				// CREACI�N DE LABELS: registro de alumno
				
				JLabel lblLU = new JLabel("LU:");		
				lblLU.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
				lblLU.setBounds(56, 52, 29, 20);
				getContentPane().add(lblLU);
				
				btnSiguiente = new JButton("Dar de baja");
				btnSiguiente.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 13));
				btnSiguiente.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {						
						eliminarAlumno();
						dispose();
					}
				});
				btnSiguiente.setBounds(82, 98, 124, 23);
				getContentPane().add(btnSiguiente);
				
			}

			
			/**
			 * eliminarAlumno: solicita la baja de un alumno, con cierto LU, que est�
			   registrado en el sistema
			 */
			private void eliminarAlumno () {
				try
				{
					ControladorAlumno.controlador().eliminar(inputLU.getText());
					JOptionPane.showMessageDialog(this,"Alumno dado de baja exitosamente");
					actualizarTabla();
					dispose();
				}
				catch (DBUpdateException ex)
				{
					JOptionPane.showMessageDialog(this, ex.getMessage(), "Baja de una carrera", JOptionPane.ERROR_MESSAGE);
				}
			}
			   
		}
	
		
}