package io.github.parthappm.http.server;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * An instance of this class is to be created and returned from the methods of the RequestProcessor class.
 */
public class Response
{
	private int statusCode;
	private final Map<String, String> headers;
	private byte[] body;

	/**
	 * Creates a Response object with 404 status code and empty body.
	 */
	public Response()
	{
		this(404);
	}

	/**
	 * Creates a Response object with specified status code and empty body.
	 * @param statusCode The HTTP status code.
	 */
	public Response(int statusCode)
	{
		this.statusCode = statusCode;
		this.headers = new HashMap<>();
		this.body = new byte[0];
	}

	/**
	 * Creates a Response object with 200 status code and the specified body.
	 * @param body The HTTP response body as String.
	 */
	public Response(String body)
	{
		this(body.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Creates a Response object with 200 status code and the specified body.
	 * @param body The HTTP response body as byte array.
	 */
	public Response(byte[] body)
	{
		this.statusCode = 200;
		this.headers = new HashMap<>();
		this.body = (body == null) ? new byte[0] : body;
	}

	/**
	 * Setter method to set the HTTP status code.
	 * @param statusCode The HTTP status code.
	 * @return The reference of the current object for chaining.
	 */
	public Response setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
		return this;
	}

	/**
	 * Setter method to add or modify a single HTTP header.
	 * @param key The header name.
	 * @param value The header value.
	 * @return The reference of the current object for chaining.
	 */
	public Response addHeader(String key, String value)
	{
		headers.put(key, value);
		return this;
	}

	/**
	 * Setter method to set or modify multiple HTTP headers.
	 * @param headers The Map of new or extra headers that are to be added to the HTTP response.
	 * @return The reference of the current object for chaining.
	 */
	public Response setHeader(Map<String, String> headers)
	{
		if (headers != null)
		{
			this.headers.putAll(headers);
		}
		return this;
	}

	/**
	 * Setter method to set the HTTP response body.
	 * @param body The HTTP response body in String.
	 * @return The reference of the current object for chaining.
	 */
	public Response setBody(String body)
	{
		return setBody(body.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Setter method to set the HTTP response body.
	 * @param body The HTTP response body in byte array.
	 * @return The reference of the current object for chaining.
	 */
	public Response setBody(byte[] body)
	{
		this.body = (body == null) ? new byte[0] : body;
		return this;
	}

	int statusCode()
	{
		return statusCode;
	}

	String statusText()
	{
		return switch (statusCode) {
			case 100 -> "Continue";
			case 101 -> "Switching Protocol";
			case 102 -> "Processing";
			case 103 -> "Early Hints";
			case 200 -> "OK";
			case 201 -> "Created";
			case 202 -> "Accepted";
			case 203 -> "Non-Authoritative Information";
			case 204 -> "No Content";
			case 205 -> "Reset Content";
			case 206 -> "Partial Content";
			case 207 -> "Multi-Status";
			case 208 -> "Already Reported";
			case 226 -> "IM Used";
			case 300 -> "Multiple Choice";
			case 301 -> "Moved Permanently";
			case 302 -> "Found";
			case 303 -> "See Other";
			case 304 -> "Not Modified";
			case 305 -> "Use Proxy";
			case 306 -> "unused";
			case 307 -> "Temporary Redirect";
			case 308 -> "Permanent Redirect";
			case 400 -> "Bad Request";
			case 401 -> "Unauthorized";
			case 402 -> "Payment Required";
			case 403 -> "Forbidden";
			case 404 -> "Not Found";
			case 405 -> "Method Not Allowed";
			case 406 -> "Not Acceptable";
			case 407 -> "Proxy Authentication Required";
			case 408 -> "Request Timeout";
			case 409 -> "Conflict";
			case 410 -> "Gone";
			case 411 -> "Length Required";
			case 412 -> "Precondition Failed";
			case 413 -> "Payload Too Large";
			case 414 -> "URI Too Long";
			case 415 -> "Unsupported Media Type";
			case 416 -> "Range Not Satisfiable";
			case 417 -> "Expectation Failed";
			case 418 -> "I'm a teapot";
			case 421 -> "Misdirected Request";
			case 422 -> "Unprocessable Entity";
			case 423 -> "Locked";
			case 424 -> "Failed Dependency";
			case 425 -> "Too Early";
			case 426 -> "Upgrade Required";
			case 428 -> "Precondition Required";
			case 429 -> "Too Many Requests";
			case 431 -> "Request Header Fields Too Large";
			case 451 -> "Unavailable For Legal Reasons";
			case 500 -> "Internal Server Error";
			case 501 -> "Not Implemented";
			case 502 -> "Bad Gateway";
			case 503 -> "Service Unavailable";
			case 504 -> "Gateway Timeout";
			case 505 -> "HTTP Version Not Supported";
			case 506 -> "Variant Also Negotiates";
			case 507 -> "Insufficient Storage";
			case 508 -> "Loop Detected";
			case 510 -> "Not Extended";
			case 511 -> "Network Authentication Required";
			default -> "Unknown code";
		};
	}

	Map<String, String> headers()
	{
		return headers;
	}

	byte[] body()
	{
		return body;
	}
}