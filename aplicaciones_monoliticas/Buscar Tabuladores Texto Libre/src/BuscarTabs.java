
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class BuscarTabs extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JButton btGenerarFichero;     

	public JLabel lbImagen;

	public JLabel lbTitulo;

	public JLabel lbSubTitulo;
	
	public JTextArea taTextArea;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BuscarTabs frame = new BuscarTabs();
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
	public BuscarTabs() {

		//Parametros asociados a la ventana
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 310);                 
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
		btGenerarFichero = new JButton();
		btGenerarFichero.setBounds(21, 100, 300, 23);

		btGenerarFichero.setText("Buscar tabuladores Mutuas");
		getContentPane().add(btGenerarFichero);
		
		taTextArea = new JTextArea(10,1);
		taTextArea.setBounds(21, 150, 300, 103);
	    getContentPane().add(taTextArea);

	  
	  
		btGenerarFichero.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e){
			
				String user = "USUARIO_BD";
				String password = "PWD_BD";
				Connection conn = null;
				Calendar c1 = Calendar.getInstance();
				int anyo = c1.get(Calendar.YEAR);
				String mutuas_afectadas="Mutuas: ";
				String[] mutuas_Activas = {"2001","2002","2003","2007",
						"2010","2011", "2015","2021",
						"2039","2061","2072","2115",
						"2151","2183","2267","2272",
						"2274","2275","2276","2291","2292"};

				//Querys a usar
				for (int i = 0; i < mutuas_Activas.length; i++) {
				String query_naturaleza_financiacion = "SELECT count(*)"
						+ " FROM SICOCOINDICEDOCUMENTOS"
						+ " WHERE CODC_ENTE = \'"+ mutuas_Activas[i] + "\'"
						+ " AND CODC_EJERCICIO='2021'"
						+ " AND instr(CODC_TXTLIBRE, CHR(9)) <> 0";

			System.out.println(query_naturaleza_financiacion);

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
					rs1 = stmt.executeQuery(query_naturaleza_financiacion); 
					if (rs1.next()) {
						int resultados=rs1.getInt(1);

						
						if (resultados > 0) {
						mutuas_afectadas = mutuas_afectadas + mutuas_Activas[i] + ", ";
							}
						//else {
						//taTextArea.setText("Todo en orden");
						//}
						
					}
					conn.close();
				} catch (SQLException sQLException) {
					sQLException.printStackTrace();
				} 
				taTextArea.setText(mutuas_afectadas);	
			}
			}
		});            

	}



}