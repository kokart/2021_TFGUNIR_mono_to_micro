package obDescClasOr;

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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class obtenerNombre extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JTextField tfID;

	private JButton btGenerarFichero;     

	public JLabel lbImagen;

	public JLabel lbTitulo;

	public JLabel lbSubTitulo;
	
	public JLabel lbID;
	
	public JTextArea taTextArea;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					obtenerNombre frame = new obtenerNombre();
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
	public obtenerNombre() {

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
		
		
		lbID = new JLabel();
		lbID.setBounds(21, 100, 141, 39);
		lbID.setFont(new Font("Times New Roman", 1, 18));
		lbID.setText("ID a Buscar:");
		getContentPane().add(lbID);		
		
		
		tfID = new JTextField();
		tfID.setBounds(170, 100, 150, 30);
		tfID.setFont(new Font("Times New Roman", 1, 18));
		getContentPane().add(tfID);		
		
				
		
		btGenerarFichero = new JButton();
		btGenerarFichero.setBounds(21, 140, 300, 23);

		btGenerarFichero.setText("Obener Descripción");
		getContentPane().add(btGenerarFichero);
		
		taTextArea = new JTextArea(10,1);
		taTextArea.setBounds(21, 170, 300, 40);
	    getContentPane().add(taTextArea);

	  
	  
		btGenerarFichero.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e){
			
				String user = "USUARIO_BD";
				String password = "PWD_BD";
				Connection conn = null;


				//Querys a usar

				String query_obtener_identificador = "SELECT  NMCONOMIDENTIFICADOR "
						+ "FROM SICOSS.SICONMCLASIFICACIONORGANICA"
						+ " Where NMCOEJEEJERCICIO = 2020 AND "
						+ "NMCOCODCLASIFORG ='"
						+ tfID.getText()+"'";
						
						

			

				try {
					conn = DriverManager.getConnection(
							"jdbc:oracle:thin:@ldap://oraoid1.portal.ss:389/PR15,cn=OracleContext,dc=portal,dc=ss", user, password);
				} catch (SQLException e5) {
					e5.printStackTrace();
				} 
				String mensaje="No se ha obtenido identificador";
				Statement stmt = null;
				try {
					stmt = conn.createStatement();
					ResultSet rs1 = null;
					rs1 = stmt.executeQuery(query_obtener_identificador); 
					if (rs1.next()) {
						
						taTextArea.setText(rs1.getString(1));
					}
					else {
						taTextArea.setText(mensaje);
					}
					conn.close();
				} catch (SQLException sQLException) {
					sQLException.printStackTrace();
				} 
				
			}
		});            

	}



}