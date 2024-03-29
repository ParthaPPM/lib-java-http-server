package io.github.parthappm.http.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * The HTTP server implementation for non-secured HTTP connection.
 */
public class HttpServer extends Server
{
	private final int port;
	private final String host;

	/**
	 * Creates an instance of HttpServer class which listens to port 80 by default.
	 */
	public HttpServer()
	{
		this(ApplicationProperties.getInstance().httpPort());
	}

	/**
	 * Creates an instance of HttpServer class which listens to the port specified.
	 * @param port The server listens to this port.
	 */
	public HttpServer(int port)
	{
		this(port, null);
	}

	/**
	 * Creates an instance of HttpServer class which listens to the port specified and accepts connection only from the host specified.
	 * @param port The server listens to this port.
	 * @param host The server accepts connection only from this host.
	 */
	public HttpServer(int port, String host)
	{
		this.port = port;
		this.host = host;
	}

	/**
	 * Start the server and start listening to new connection from clients.
	 */
	public void start()
	{
		Log.getInstance().debug("Port: " + port);
		Log.getInstance().debug("Host: " + host);
		try
		{
			setServerSocket(new ServerSocket(port, 0, host == null ? null : InetAddress.getByName(host)));
			Log.getInstance().info("Server started at port: " + port);
		}
		catch (IOException e)
		{
			Log.getInstance().error(e);
		}
		super.start();
	}
}