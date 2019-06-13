package com.practice.tomcat.tomcatpractice.exceptionsimulation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * https://examples.javacodegeeks.com/core-java/net/sockettimeoutexception/java-net-sockettimeoutexception-how-to-solve-sockettimeoutexception/
 * <p>
 *     From the javadoc we read that this exception :” Signals that a timeout has occurred on a socket read or accept”. That means that this exception emerges when a blocking operation of the two, an accept or a read, is blocked for a certain amount of time, called the timeout. Let’s say that the socket is configured with a timeout of 5 seconds.
 * If either the accept() or read() method, blocks for more than 5 seconds, a SocketTimeoutException is thrown, designating that a timeout has occurred. It is important to note that after this exception is thrown. the socket remains valid, so you can retry the blocking call or do whatever you want with the valid socket.
 * </p>
 */
public class SocketTimeoutExceptionExample {

	public static void main(String[] args) {

		new Thread(new SimpleServer()).start();
		new Thread(new SimpleClient()).start();

	}

	static class SimpleServer implements Runnable {

		@Override
		public void run() {

			ServerSocket serverSocket = null;
			try {
				
				serverSocket = new ServerSocket(3333);

				serverSocket.setSoTimeout(7000);
				
				while (true) {

					Socket clientSocket = serverSocket.accept();

					BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

					System.out.println("Client said :" + inputReader.readLine());
				}

			} catch (SocketTimeoutException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (serverSocket != null)
						serverSocket.close();

				} catch (IOException e) {
					e.printStackTrace();
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

				PrintWriter outWriter = new PrintWriter(
						socket.getOutputStream(), true);

				outWriter.println("Hello Mr. Server!");

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

				try {
					if (socket != null)
						socket.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
	}
}