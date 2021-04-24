package Rubrica_481_482_485;


import java.awt.EventQueue;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Formatter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class obtenerOrigenGastoRubricaADOK_Entidades2021 extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JTextField tfRubrica;
	private JTextField tfFechaLimite;

	private JButton btGenerarFichero;
	private JButton btGenerarNuevoFichero;     

	public JLabel lbImagen;

	public JLabel lbTitulo;

	public JLabel lbSubTitulo;
	public JLabel lbFicheroGenerado;
	
	public JLabel lbEntidad;
	public JLabel lbFechaLimie;
	
	int mes_ultimo =1;
	
	Calendar c1 = Calendar.getInstance();

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)  {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					obtenerOrigenGastoRubricaADOK_Entidades2021 frame = new obtenerOrigenGastoRubricaADOK_Entidades2021();
					frame.setVisible(true);
					frame.setTitle(" Entidades - Obtener Origen Gasto ADOK ");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public obtenerOrigenGastoRubricaADOK_Entidades2021() {

		//Parametros asociados a la ventana
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 350);                 
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);       
		getContentPane().setLayout((LayoutManager)null);
		
		lbImagen = new JLabel();
		lbImagen.setBounds(21, 11, 405, 60);
		lbImagen.setIcon(new ImageIcon(getClass().getResource("/giss.png")));
		getContentPane().add(lbImagen);
		
		lbTitulo = new JLabel();
		lbTitulo.setBounds(300, 10, 141, 39);
		lbTitulo.setFont(new Font("Times New Roman", 1, 18));
		lbTitulo.setText("SICOSS");
		getContentPane().add(lbTitulo);
		lbSubTitulo = new JLabel();
		lbSubTitulo.setBounds(300, 35, 141, 39);
		lbSubTitulo.setFont(new Font("Times New Roman", 1, 18));
		lbSubTitulo.setText("CD. IGSS 2021");
		getContentPane().add(lbSubTitulo);
		
		
		lbEntidad = new JLabel();
		lbEntidad.setBounds(21, 100, 141, 39);
		lbEntidad.setFont(new Font("Times New Roman", 1, 18));
		lbEntidad.setText("Rúbrica:");
		getContentPane().add(lbEntidad);		
		
		
		tfRubrica = new JTextField();
		tfRubrica.setBounds(170, 100, 150, 30);
		tfRubrica.setFont(new Font("Times New Roman", 1, 18));
		getContentPane().add(tfRubrica);		
		
		lbFechaLimie = new JLabel();
		lbFechaLimie.setBounds(21, 140, 141, 39);
		lbFechaLimie.setFont(new Font("Times New Roman", 1, 14));
		lbFechaLimie.setText("Hasta(YYYYMMDD):");
		getContentPane().add(lbFechaLimie);		
		
		
		tfFechaLimite = new JTextField();
		tfFechaLimite.setBounds(170, 140, 150, 30);
		tfFechaLimite.setFont(new Font("Times New Roman", 1, 18));
		tfFechaLimite.setText(String.valueOf(c1.get(Calendar.YEAR)+"MM"+"DD"));	
		
		getContentPane().add(tfFechaLimite);		
		
				
		btGenerarFichero = new JButton();
		btGenerarFichero.setBounds(21, 200, 300, 23);
		btGenerarFichero.setText("Obtener Fichero");
		getContentPane().add(btGenerarFichero);

		lbFicheroGenerado = new JLabel();
		lbFicheroGenerado.setBounds(21, 220, 2001, 39);
		lbFicheroGenerado.setFont(new Font("Times New Roman", 1, 11));
		getContentPane().add(lbFicheroGenerado);		
		
		btGenerarNuevoFichero = new JButton();
		btGenerarNuevoFichero.setBounds(21, 260, 300, 23);
		btGenerarNuevoFichero.setText("Generar Nuevo Fichero");
		btGenerarNuevoFichero.setEnabled(false);
		getContentPane().add(btGenerarNuevoFichero);
		
		
	  
		btGenerarFichero.addActionListener(new ActionListener(){
			
			public void actionPerformed (ActionEvent e){

								
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				//Variables	
				String rubrica=tfRubrica.getText();
				String user_path=System.getProperty("user.home");
				String path_file=user_path +"\\Desktop\\481_482_485\\";
				String nombre_fichero = path_file + rubrica +"_origen_gasto_adok_ENTIDADES"+".txt";
				String csv_separator=";";
				
				mes_ultimo=Integer.parseInt(tfFechaLimite.getText().substring(4,6));
				
				//Fichero_centro_gestor
				FileWriter fichero = null;
				PrintWriter pw = null;
				
				//Conexión BD
				String user = "USUARIO_BD";
				String password = "PWD_BD";
				Connection conn = null; 

				try {
					conn = DriverManager.getConnection(
							"jdbc:oracle:thin:@ldap://oraoid1.portal.ss:389/PR15,cn=OracleContext,dc=portal,dc=ss", user, password);
				} catch (SQLException e5) {
					e5.printStackTrace();
				} 
				
				try {
				Statement stmt = null;
				stmt = conn.createStatement();
				//Creamos directorio
				 
				File directorio = new File(path_file);
			        if (!directorio.exists()) {
			            if (directorio.mkdirs()) {
			                System.out.println("Directorio creado");
			            } else {
			                System.out.println("Error al crear directorio");
			            }
				
			        }
							
			
				fichero = new FileWriter(nombre_fichero);
				pw = new PrintWriter(fichero);
				pw.println("CENTRO GESTOR;PROGRAMA;ECONOMICA;IMPORTE;DEVENGO;ANYO_ORIGEN_GASTO;GRUPO_ECONOMICA");
		
		
				
					for (int j =1; j<=mes_ultimo;j++) {						
									
						String sql_obener_documentos_ADOK="select  doc.PGDC_CENTROGESTOREMISOR as CENTRO_GESTOR,"
								+ " apl.PGAD_CLASIFFUNCIONAL as PROGRAMA,"
								+ " apl.PGAD_CLASIFECONOMICA as ECONOMICA,"
								+ " sum(apl.PGAD_IMPORTEPARCIAL)/100 as IMPORTE,"
								+ " doc.PGDC_MESDEVENGO as DEVENGO,"
								+ " doc.PGDC_ORIGENGASTO"
						+ " from SICOSS.SICOPGDOCUMENTO doc,SICOSS.SICOPGAPLICPRESUPDOCUMENTO apl"
						+ " where doc.PGDC_EJERCICIODOCUMENTO=apl.PGAD_EJERCICIODOCUMENTO "
						+ " and doc.PGDC_CENTROGESTOREMISOR=apl.PGAD_CENTROGESTOREMISOR"
						+ " and doc.PGDC_NUMERODOCUMENTO=apl.PGAD_NUMERODOCUMENTO"
						+ " and apl.PGAD_CLASIFECONOMICA like '"+ tfRubrica.getText() +"%'"
						+ " and TO_CHAR(doc.PGDC_FECHACONTABLE, 'YYYYMMDD') >= '" +calcular_fecha_inicial(j)+ "'"
						+ " and TO_CHAR(doc.PGDC_FECHACONTABLE, 'YYYYMMDD') <='" +calcular_fecha_final(j)+ "'"
						+ " and doc.PGDC_EJERCICIODOCUMENTO=2021"
						+ "	and apl.pgad_ejerciciopredocumento=2021"
						+ " and doc.PGDC_ENTE =1000"
						+ " and doc.PGDC_CODOPERACION in ('01004400','01004401','01004410','01004411','01004500','01004501','01004800','01004801','01004900','01004901','01006100','01006101','01006200','01006201') "
						+ " group by doc.PGDC_CENTROGESTOREMISOR, apl.PGAD_CLASIFFUNCIONAL, apl.PGAD_CLASIFECONOMICA, doc.PGDC_MESDEVENGO, doc.PGDC_ORIGENGASTO"
						+ " order by doc.PGDC_CENTROGESTOREMISOR,apl.PGAD_CLASIFECONOMICA, doc.PGDC_MESDEVENGO,doc.PGDC_ORIGENGASTO";
				
				System.out.println(sql_obener_documentos_ADOK);		
				ResultSet rs1 = null;
				rs1 = stmt.executeQuery(sql_obener_documentos_ADOK);
					
					while (rs1.next()) {
						
											
							//Escribir fichero
							pw.println(rs1.getString(1)+csv_separator
							+rs1.getString(2)+ csv_separator
							+rs1.getString(3)+ csv_separator
							+rs1.getString(4).replace(".", ",")+ csv_separator
							+rs1.getString(5)+ csv_separator
							+rs1.getString(6)+ csv_separator
							+rs1.getString(3).substring(0,4));					
							
						}						
					
					}	
					
			

				fichero.close();
				conn.close();
				}
				
			
			catch (SQLException | IOException e5) {
				e5.printStackTrace();
			}
				
				lbFicheroGenerado.setText("Listo en "+ nombre_fichero);			
				btGenerarNuevoFichero.setEnabled(true);
				btGenerarFichero.setEnabled(false);	

				
			}

			private String calcular_fecha_inicial(int inicio_mes) {
				Formatter mes = new Formatter();
				mes.format("%02d",inicio_mes);
				String fecha_inicio ="2021"+mes+"01";
				System.out.println(fecha_inicio);				
				return fecha_inicio;
			}


		private String calcular_fecha_final(int fin_mes) {
			@SuppressWarnings("resource")
			Formatter mes = new Formatter();
			mes.format("%02d",fin_mes);
			
			String dia_mes="31";
			if (fin_mes == 1 || fin_mes == 3 || fin_mes == 5 || fin_mes == 7 || fin_mes == 8 || fin_mes == 10 || fin_mes == 12 ) {
				dia_mes="31";
			} else if (fin_mes ==4  || fin_mes == 6 || fin_mes == 9 || fin_mes == 11 ) {
				
				dia_mes="30";
			} else {//febrero
				dia_mes="29";
			}
			
			
			String fecha_final ="2021"+ mes+ dia_mes;
			if (fin_mes == mes_ultimo) {
				fecha_final=tfFechaLimite.getText();
			}
			System.out.println(fecha_final);				
			return fecha_final;
		}
		});
		btGenerarNuevoFichero.addActionListener(new ActionListener(){
			
			public void actionPerformed (ActionEvent e){
				btGenerarFichero.setEnabled(true);
				btGenerarNuevoFichero.setEnabled(false);
				tfRubrica.setText("");
				lbFicheroGenerado.setText("");
			}
	});
	
	}
}