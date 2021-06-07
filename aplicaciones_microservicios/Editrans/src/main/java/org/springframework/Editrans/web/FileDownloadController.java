/*
 * Clase generar fichero y descagarlo
 */
package org.springframework.Editrans.web;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/download")
public class FileDownloadController {

	private static final String EXTERNAL_FILE_PATH = "src/main/resources/";

	@RequestMapping("/file/{fileName:.+}")
	public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileName") String fileName) throws IOException {

		String inputLine;
		String user_path = EXTERNAL_FILE_PATH;
		String numero_a_obtener = "400";
		String nombre_fichero = user_path + fileName;

		URL test = null;
		try {
			test = new URL(
					"https://www2.agenciatributaria.gob.es/L/inwinvoc/es.aeat.dit.adu.adht.editran.NumRefEditran?mod=347");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter(nombre_fichero);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		pw = new PrintWriter(fichero);
		for (int i = 1; i <= Integer.parseInt(numero_a_obtener); i++) {
			URLConnection uc = null;
			try {
				uc = test.openConnection();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			uc.addRequestProperty("User-Agent", "Mozilla/4.0");
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			} catch (IOException e2) {
				e2.printStackTrace();
			}

			try {
				while ((inputLine = in.readLine()) != null) {
					if (inputLine.contains("Justificante: <strong>")) {
						String numero = inputLine.substring(inputLine.indexOf("g>") + 2, inputLine.indexOf("g>") + 16)
								.replace(" ", "");
						if (i == Integer.parseInt(numero_a_obtener)) {
							pw.print(numero);
						} else {
							pw.println(numero);
						}
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		pw.close();

		File file = new File(nombre_fichero);
		if (file.exists()) {

			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);

			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

			response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());

		}
	}
}
