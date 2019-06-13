package com.practice.tomcat.tomcatpractice.exceptionsimulation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * https://examples.javacodegeeks.com/core-java/net/connectexception/java-net-connectexception-how-to-solve-connect-exception/
 * <p>
 *     ConnectException is a subclass of SocketException, and that alone reveals that it is meant to inform you about an error that occurred when you tried to create or access a socket. To be more specific, ConnectException is an exception that you will see in the client side of your application and it can emerge when a client fails to create a connection with a remote server socket.
 * </p>
 */
public class ConnectExceptionExample {

	public static void main(String[] args) {

		//new Thread(new SimpleServer()).start();
		new Thread(new SimpleClient()).start();

	}

	static class SimpleServer implements Runnable {

		@Override
		public void run() {

			ServerSocket serverSocket = null;
			while (true) {
				
				try {
					serverSocket = new ServerSocket(3333);

					Socket clientSocket = serverSocket.accept();

					BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					
					System.out.println("Client said :"+inputReader.readLine());

				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					try {
						serverSocket.close();
					
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

	}

	static class SimpleClient implements Runnable {

		@Override
		public void run() {

			Socket socket = null;
			try {

				Thread.sleep(3000);
				
				socket = new Socket("localhost", 3333);
				
			    PrintWriter outWriter = new PrintWriter(socket.getOutputStream(),true);
			    
			    outWriter.println("Hello Mr. Server!");
			   

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}