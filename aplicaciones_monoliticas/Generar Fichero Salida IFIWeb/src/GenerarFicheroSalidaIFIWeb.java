import java.awt.EventQueue;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class GenerarFicheroSalidaIFIWeb extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JButton btGenerarFichero;     

	public JLabel lbImagen;

	public JLabel lbTitulo;

	public JLabel lbSubTitulo;

	public JLabel lbRutaFichero;

	public JLabel IbNombreFicheroEntrada;

	public JLabel IbNombreFicheroSalida;

	static int total_registros_ficheros;
	static int total_registros_procesados_fichero_entrada;

	static int total_registros_OK;

	static int total_registros_KO;

	static String nombre_fichero_entrada;

	static String nombre_fichero_salida;

	static String ente;

	static String path;

	static String cabecera_fichero;

	static String nombre_fichero_detalleout;
	static String mensaje_error_defecto ="E1Registro no procesado";

	static String pie_fichero;

	static String total_registros_OK_formateado;

	static String total_registros_KO_formateado;

	static boolean fichero_procesado_completamente = false;

	static HashMap<String, String> cabecera_segun_ente = new HashMap<String, String>();

	static HashMap<String, String> pie_segun_ente_con_errores = new HashMap<String, String>();

	static HashMap<String, String> pie_segun_ente_sin_errores = new HashMap<String, String>();





	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GenerarFicheroSalidaIFIWeb frame = new GenerarFicheroSalidaIFIWeb();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GenerarFicheroSalidaIFIWeb() {

		//Parametros asociados a la ventana
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 310);                 
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);       
		JButton btnSeleccionar = new JButton("Seleccionar Fichero de Entrada");
		btnSeleccionar.setBounds(21, 100, 250, 20);
		contentPane.add(btnSeleccionar);        
		setDefaultCloseOperation(3);
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
		lbSubTitulo.setText("CD. IGSS");
		getContentPane().add(lbSubTitulo);
		lbRutaFichero = new JLabel();
		lbRutaFichero.setBounds(21, 120, 400, 39);
		lbRutaFichero.setFont(new Font("Times New Roman", 1, 14));
		lbRutaFichero.setText("Ruta de destino");
		getContentPane().add(lbRutaFichero);
		IbNombreFicheroEntrada = new JLabel();
		IbNombreFicheroEntrada.setBounds(21, 150, 400, 39);
		IbNombreFicheroEntrada.setFont(new Font("Times New Roman", 1, 14));
		IbNombreFicheroEntrada.setText("Fichero de Entrada");
		getContentPane().add(IbNombreFicheroEntrada);
		btGenerarFichero = new JButton();
		btGenerarFichero.setBounds(21, 200, 180, 23);

		btGenerarFichero.setText("Generar Fichero");
		getContentPane().add(btGenerarFichero);
		IbNombreFicheroSalida = new JLabel();
		IbNombreFicheroSalida.setBounds(21, 220, 400, 39);
		IbNombreFicheroSalida.setFont(new Font("Times New Roman", 1, 14));
		IbNombreFicheroSalida.setText("Fichero de Salida Generado");
		getContentPane().add(IbNombreFicheroSalida);



		btGenerarFichero.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e){
				nombre_fichero_entrada = IbNombreFicheroEntrada.getText();
				path = String.valueOf(lbRutaFichero.getText().replace("\\", "\\\\")) + "\\\\";
				ente = nombre_fichero_entrada.substring(1, 5);
				nombre_fichero_salida = nombre_fichero_entrada.replaceFirst("E", "S");
				int total_OK = 0;
				int total_lineas_fichero_entrada = 0;
				String user = "USUARIO_BD";
				String password = "PWD_BD";
				Connection conn = null;


				//Querys a usar

				String query_total_lineas_fichero = "SELECT SF.GFFI_NUMDOCUMENTOS FROM SICOSS.SICOGFFICHEROS SF WHERE SF.GFFI_NOMFICHERO='" + 

                   nombre_fichero_salida + "'";


				//query obtenemos detalle registros procesados
				String query_detalle_lineas_procesadas ="SELECT TIPO_REGISTRO|| LPAD(sd.GFDO_NUMORDEN,5,0) || GFDO_CODRESULTADO || NVL(sd.GFDO_NOMERROR, 'xxyyzz')\r\n" + 
						"FROM SICOSS.SICOGFDETALLEOUT SD\r\n" + 
						"FULL OUTER JOIN (select LPAD(GFDI_TIPOREGISTRO,2,0) TIPO_REGISTRO , GFDI_NUMORDEN   from sicogfdetallein"
						+ "  where gfdi_codfichero= (SELECT SF.GFFI_CODFICHEROASOC FROM SICOSS.SICOGFFICHEROS SF"
						+ " WHERE SF.GFFI_NOMFICHERO='"
						+ nombre_fichero_salida 
						+ "'))\r\n" + 
						"ON GFDO_NUMORDEN =GFDI_NUMORDEN\r\n" + 
						"WHERE SD.GFDO_CODFICHERO = (SELECT SF.GFFI_CODFICHERO FROM SICOSS.SICOGFFICHEROS SF WHERE SF.GFFI_NOMFICHERO='"
						+ nombre_fichero_salida 
						+ "')";

				//total lineas procesadas 
				String query_total_lineas_de_entrada_procesadas ="SELECT MAX(GFDO_NUMORDEN) as TOTAL_PROCESADOS  FROM  SICOSS.SICOGFDETALLEOUT SD"
						+ " WHERE SD.GFDO_CODFICHERO IN (SELECT SF.GFFI_CODFICHERO FROM SICOSS.SICOGFFICHEROS SF WHERE SF.GFFI_NOMFICHERO='"
						+   nombre_fichero_salida + 
						"')";

				//total lineas procesadas OK
				String query_total_procesadas_OK =" SELECT count(*) AS TOTAL_OK FROM \r\n" + 
						"   (SELECT distinct(gfdo_numorden) FROM SICOSS.SICOGFDETALLEOUT SD"
						+ "  WHERE SD.GFDO_CODFICHERO IN (SELECT SF.GFFI_CODFICHERO FROM SICOSS.SICOGFFICHEROS SF"
						+ " WHERE SF.GFFI_NOMFICHERO='"
						+   nombre_fichero_salida 
						+	"') and gfdo_codresultado='OK')\r\n" + 
						"";

				try {
					conn = DriverManager.getConnection(
							"jdbc:oracle:thin:@ldap://oraoid1.portal.ss:389/PR15,cn=OracleContext,dc=portal,dc=ss", user, password);
				} catch (SQLException e5) {
					e5.printStackTrace();
				} 

				Statement stmt = null;
				try {
					stmt = conn.createStatement();
					ResultSet rs1 = null;
					rs1 = stmt.executeQuery(query_total_lineas_fichero);                
					while (rs1.next()) {

						total_lineas_fichero_entrada = rs1.getInt("GFFI_NUMDOCUMENTOS");                       
					} 
				} catch (SQLException sQLException) {
					sQLException.printStackTrace();
				} 

				try {
					stmt = conn.createStatement();
					ResultSet rs1 = null;
					rs1 = stmt.executeQuery(query_total_procesadas_OK);                
					while (rs1.next()) {

						total_OK = rs1.getInt("TOTAL_OK");                       
					} 
				} catch (SQLException sQLException) {
					sQLException.printStackTrace();
				}                                


				try {
					stmt = conn.createStatement();
					ResultSet rs1 = null;
					rs1 = stmt.executeQuery(query_total_lineas_de_entrada_procesadas);                
					while (rs1.next()) {

						total_registros_procesados_fichero_entrada = rs1.getInt("TOTAL_PROCESADOS");                       
					} 
				} catch (SQLException sQLException) {
					sQLException.printStackTrace();
				}       
				//Obtenemos numeros
				total_registros_OK_formateado=  String.format("%05d", Integer.parseInt(String.valueOf(total_OK)));
				total_registros_KO_formateado=  String.format("%05d", Integer.parseInt(String.valueOf(total_lineas_fichero_entrada - total_OK)));



				//Escribir fichero de salida
				FileWriter fichero_respuesta = null;
				PrintWriter pw = null;
				try {
					fichero_respuesta = new FileWriter(String.valueOf(path) + nombre_fichero_salida);
					pw = new PrintWriter(fichero_respuesta);
					
					pw.println(obtener_cabecera(ente));
					obtener_detalle_lineas_procesadas(stmt, pw, query_detalle_lineas_procesadas);
					escribir_lineas_no_procesadas(pw, total_registros_procesados_fichero_entrada, total_lineas_fichero_entrada);

					if (total_lineas_fichero_entrada ==  total_OK) {
						fichero_procesado_completamente = true;
					}
					pw.println(obtener_pie(ente));
					JOptionPane.showMessageDialog(null, "Fichero Generado correctamente");
					IbNombreFicheroSalida.setText(nombre_fichero_salida);
				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					try {
						if (fichero_respuesta != null)
							fichero_respuesta.close(); 
					} catch (Exception e2) {
						e2.printStackTrace();
					} 
				} 
			}

			private void escribir_lineas_no_procesadas(PrintWriter pw, int total_registros_procesados_fichero_entrada, int total_lineas_fichero_entrada) throws IOException {


				File archivo = new File(String.valueOf(path) + nombre_fichero_entrada);
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(archivo);		        

					br = new BufferedReader(fr);
					int i = 1;
					String linea = br.readLine();
					while ((linea = br.readLine()) != null && i <= total_lineas_fichero_entrada) {
						if( i > total_registros_procesados_fichero_entrada) { 		        	  
							pw.println(linea.substring(0, 7) + mensaje_error_defecto);
						}
						i++;

					} 

				}catch (IOException e3) {
					e3.printStackTrace();
				} 
				br.close();
				fr.close();
			}

			private void obtener_detalle_lineas_procesadas(Statement stmt, PrintWriter pw, String query_detalles_lineas) {
				String detalle_resultado ="null";
				ResultSet rs = null;
				try {
					rs = stmt.executeQuery(query_detalles_lineas);
					while (rs.next()) {

						detalle_resultado = rs.getString(1);
						if (detalle_resultado.contains("xxyyzz")) {
							detalle_resultado = detalle_resultado.replace("xxyyzz", "");			           			            
						}
						pw.println(detalle_resultado);

					}
				}
				catch (SQLException e1) {
					e1.printStackTrace();
				} 

			}



		});            


		btnSeleccionar.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e){

				JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int r = j.showOpenDialog(null);
				if (r == 0) {
					IbNombreFicheroEntrada.setText(j.getSelectedFile().getName());
					lbRutaFichero.setText(j.getSelectedFile().getParentFile().toString());
				} 

			}

		});
	}


	public static String obtener_cabecera(String ente) {
		String cabecera_fichero_ente = "";
		cabecera_segun_ente.put("1005", "011005");
		cabecera_segun_ente.put("2001", "a");
		cabecera_segun_ente.put("2002", "012002");
		cabecera_segun_ente.put("2003", "012003");
		cabecera_segun_ente.put("2007", "012007");
		cabecera_segun_ente.put("2010", "012010");
		cabecera_segun_ente.put("2011", "012011");
		cabecera_segun_ente.put("2015", "012015");
		cabecera_segun_ente.put("2021", "012021");
		cabecera_segun_ente.put("2039", "012039");
		cabecera_segun_ente.put("2061", "012061");
		cabecera_segun_ente.put("2072", "012072");
		cabecera_segun_ente.put("2115", "012115");
		cabecera_segun_ente.put("2151", "012151");
		cabecera_segun_ente.put("2183", "012183");
		cabecera_segun_ente.put("2267", "012267");
		cabecera_segun_ente.put("2272", "012272");
		cabecera_segun_ente.put("2274", "012274");
		cabecera_segun_ente.put("2275", "012275");
		cabecera_segun_ente.put("2276", "012276");
		cabecera_segun_ente.put("2291", "012291");
		cabecera_segun_ente.put("2292", "012292");
		cabecera_fichero_ente = cabecera_segun_ente.get(ente);
		return cabecera_fichero_ente;
	}

	public static String obtener_pie(String ente) {
		String pie_fichero_ente = "";
		pie_segun_ente_con_errores.put("1005", "041005TGSS ENTIDAD                            NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2001", "042001001 MUTUAL MIDAT CYCLOPS                NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2002", "042002002 MUTUALIA                            NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2003", "042003003 ACTIVA MUTUA 2008                   NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2007", "042007007 MUTUA MONTA                    NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2010", "042010010 MUTUA UNIVERSAL MUGENAT             NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2011", "042011011 MAZ                                 NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2015", "042015015 UMIVALE                             NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2021", "042021021 MUTUA NAVARRA                       NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2039", "042039039 MUTUA INTERCOMARCAL                 NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2061", "042061061 FREMAP                              NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2072", "042072072 SOLIMAT                             NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2115", "042115115 MUTUA DE CEUTA-SMAT                 NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2151", "042151151 A.S.E.P.E.Y.O                       NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2183", "042183183 MUTUA BALEAR                        NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2267", "042267267 UNION DE MUTUAS                     NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2272", "042272272 MAC, MUTUA DE ACCIDENTES DE CANARIASNUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2274", "042274274 IBERMUTUA                           NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2275", "042275275 FRATERNIDAD-MUPRESPA                NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2276", "042276276 MUTUA EGARSAT                       NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2291", "042291291 CENTRO INTERMUTUAL DE EUSKADI       NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_con_errores.put("2292", "042292292 CENTRO DE LEVANTE                   NUMOKNUMKOOKNFichero Procesado con errores. ");
		pie_segun_ente_sin_errores.put("1005", "041005TGSS ENTIDAD                            NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2001", "042001001 MUTUAL MIDAT CYCLOPS                NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2002", "042002002 MUTUALIA                            NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2003", "042003003 ACTIVA MUTUA 2008                   NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2007", "042007007 MUTUA MONTA                    NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2010", "042010010 MUTUA UNIVERSAL MUGENAT             NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2011", "042011011 MAZ                                 NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2015", "042015015 UMIVALE                             NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2021", "042021021 MUTUA NAVARRA                       NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2039", "042039039 MUTUA INTERCOMARCAL                 NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2061", "042061061 FREMAP                              NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2072", "042072072 SOLIMAT                             NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2115", "042115115 MUTUA DE CEUTA-SMAT                 NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2151", "042151151 A.S.E.P.E.Y.O                       NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2183", "042183183 MUTUA BALEAR                        NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2267", "042267267 UNION DE MUTUAS                     NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2272", "042272272 MAC, MUTUA DE ACCIDENTES DE CANARIASNUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2274", "042274274 IBERMUTUA                           NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2275", "042275275 FRATERNIDAD-MUPRESPA                NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2276", "042276276 MUTUA EGARSAT                       NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2291", "042291291 CENTRO INTERMUTUAL DE EUSKADI       NUMOKNUMKOOK Fichero Procesado sin errores. ");
		pie_segun_ente_sin_errores.put("2292", "042292292 CENTRO DE LEVANTE                   NUMOKNUMKOOK Fichero Procesado sin errores. ");
		if (fichero_procesado_completamente) {
			pie_fichero_ente = ((String)pie_segun_ente_sin_errores.get(ente)).replace("NUMOK", total_registros_OK_formateado).replace("NUMKO", "00000");
			fichero_procesado_completamente = false;
		} else {
			pie_fichero_ente = ((String)pie_segun_ente_con_errores.get(ente)).replace("NUMOK", total_registros_OK_formateado).replace("NUMKO", total_registros_KO_formateado);
		} 
		return pie_fichero_ente;
	}



}