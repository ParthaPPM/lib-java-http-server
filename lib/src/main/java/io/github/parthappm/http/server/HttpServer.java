package io.github.parthappm.http.server;

import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * The HTTP server implementation for non-secured HTTP connection.
 */
public class HttpServer extends Server
{
	/**
	 * Creates an instance of HttpServer class with default properties.
	 */
	public HttpServer()
	{
		this(new ServerProperties());
	}

	/**
	 * Creates an instance of HttpServer class with custom properties.
	 * @param properties An instance of ServerProperties class.
	 */
	public HttpServer(ServerProperties properties)
	{
		super(properties);
	}

	/**
	 * Start the server and start listening to new connection from clients.
	 */
	public void start()
	{
		try
		{
			serverSocket = new ServerSocket(properties.getPort(), 0, properties.getHost() == null ? null : InetAddress.getByName(properties.getHost()));
		}
		catch (Exception e)
		{
			logger.error(e);
		}
		super.start();
	}
}