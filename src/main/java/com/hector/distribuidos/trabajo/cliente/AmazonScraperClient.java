package com.hector.distribuidos.trabajo.cliente;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class AmazonScraperClient {

	public static void main(String[] args) {
		File f = new File("productList.txt");
		List<String> idList = new ArrayList<>();
		String line;
		DataInputStream dis_file = null;
		BufferedWriter writer_socket = null;
		BufferedWriter writer_file = null;
		DataInputStream dis_socket = null;
		Socket s = null;
		try {
			if (f.exists() && f.isFile()) {
				dis_file = new DataInputStream(new FileInputStream(f));
				while ((line = dis_file.readLine()) != null) {
					if(!line.isEmpty()) 
						idList.add(line);
				}
				dis_file.close();
			}
			if (idList.size() > 0) {
				s = new Socket("localhost", 12000);
				if (s != null) {
					writer_socket = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
					for (String id: idList) {
						writer_socket.write(id);
						writer_socket.newLine();
					}
					writer_socket.newLine();
					writer_socket.flush();
					dis_socket = new DataInputStream(s.getInputStream());
					writer_file = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("pricesReport.txt")));
					while ((line = dis_socket.readLine()) != null && !line.isEmpty()) {
						writer_file.write(line);
						writer_file.newLine();
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (dis_file != null)
					dis_file.close();
				if (s != null)
					s.close();
				if (writer_file != null)
					writer_file.close();
				if (writer_socket != null)
					writer_socket.close();
				if (dis_socket != null)
					dis_socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
