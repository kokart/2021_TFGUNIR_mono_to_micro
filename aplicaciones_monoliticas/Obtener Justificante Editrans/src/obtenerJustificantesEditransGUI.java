


import java.awt.EventQueue;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class obtenerJustificantesEditransGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JTextField tfRubrica;

	private JButton btGenerarFichero;
 

	public JLabel lbImagen;

	public JLabel lbTitulo;

	public JLabel lbSubTitulo;
	public JLabel lbFicheroGenerado;
	
	public JLabel lbEntidad;
	public JLabel lbFechaLimie;
	
	int mes_ultimo =1;
	

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)  {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					obtenerJustificantesEditransGUI frame = new obtenerJustificantesEditransGUI();
					frame.setVisible(true);
					frame.setTitle(" Obtener Justificantes Editrans ");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public obtenerJustificantesEditransGUI() {

		//Parametros asociados a la ventana
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 250);                 
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
		lbSubTitulo.setText("CD. IGSS");
		getContentPane().add(lbSubTitulo);
		
		
		lbEntidad = new JLabel();
		lbEntidad.setBounds(21, 100, 151, 39);
		lbEntidad.setFont(new Font("Times New Roman", 1, 18));
		lbEntidad.setText("Num.Justificantes:");
		getContentPane().add(lbEntidad);				
		
		tfRubrica = new JTextField();
		tfRubrica.setBounds(190, 100, 50, 30);
		tfRubrica.setFont(new Font("Times New Roman", 1, 18));
		getContentPane().add(tfRubrica);		
				
		btGenerarFichero = new JButton();
		btGenerarFichero.setBounds(21, 150, 300, 23);
		btGenerarFichero.setText("Obtener Fichero");
		getContentPane().add(btGenerarFichero);

		lbFicheroGenerado = new JLabel();
		lbFicheroGenerado.setBounds(21, 220, 2001, 39);
		lbFicheroGenerado.setFont(new Font("Times New Roman", 1, 11));
		getContentPane().add(lbFicheroGenerado);		
		
		
	  
		btGenerarFichero.addActionListener(new ActionListener(){
			
			public void actionPerformed (ActionEvent e){

				URL test = null;
				try {
					test = new URL("https://www2.agenciatributaria.gob.es/L/inwinvoc/es.aeat.dit.adu.adht.editran.NumRefEditran?mod=347");
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				String inputLine;
				String user_path=System.getProperty("user.home");
				String path_file=user_path +"\\Desktop\\";
				String nombre_fichero = path_file + "_justificanteEditans"+".txt";
				//Fichero_centro_gestor
				FileWriter fichero = null;
				PrintWriter pw = null;
				try {
					fichero = new FileWriter(nombre_fichero);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				pw = new PrintWriter(fichero);
				for (int i=1; i<=Integer.parseInt(tfRubrica.getText());i++) {
				URLConnection uc = null;
				try {
					uc = test.openConnection();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				uc.addRequestProperty("User-Agent", "Mozilla/4.0");
				BufferedReader in = null;
				try {
					in = new BufferedReader(new InputStreamReader(uc
							.getInputStream()));
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				try {
					while ((inputLine = in.readLine()) != null) {
							if (inputLine.contains("Justificante: <strong>")){
								String numero =inputLine.substring(inputLine.indexOf("g>")+2,inputLine.indexOf("g>")+16).replace(" ","");
								//System.out.println(numero);
								if (i==Integer.parseInt(tfRubrica.getText())) {
									pw.print(numero);
								}else {
								pw.println(numero);
							}
							}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				}
				pw.close();
			}
			
				
		});
	}			

}