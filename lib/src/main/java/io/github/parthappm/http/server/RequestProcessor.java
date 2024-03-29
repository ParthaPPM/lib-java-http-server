package io.github.parthappm.http.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * The RequestProcessor class which contains the logic to process the request and create the response.
 * This class has to be extended while creating the custom RequestProcessor and passed to the Server object.
 */
public class RequestProcessor
{
	private final String ROOT_DIRECTORY;

	/**
	 * Create an instance of this class.
	 */
	public RequestProcessor()
	{
		this(null);
	}

	/**
	 * Create an instance of this class with specified file path as the root directory for html files.
	 * @param filePath File path of the root directory for the html files.
	 */
	public RequestProcessor(String filePath)
	{
		this.ROOT_DIRECTORY = absolutePath(filePath);
	}

	private String absolutePath(String path)
	{
		try
		{
			return new File(Objects.requireNonNull(path)).getCanonicalPath();
		}
		catch (IOException | NullPointerException ignored) {}
		return null;
	}

	private byte[] readFromResource(String fileName)
	{
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName))
		{
			return Objects.requireNonNull(is).readAllBytes();
		}
		catch (IOException | NullPointerException e)
		{
			Log.getInstance().debug("Resource not found: " + fileName);
		}
		return null;
	}

	private byte[] readFromFile(String fileName)
	{
		if (ROOT_DIRECTORY != null && fileName != null)
		{
			String absoluteFileName = absolutePath(ROOT_DIRECTORY + "/" + fileName);
			if (absoluteFileName != null && absoluteFileName.startsWith(ROOT_DIRECTORY))
			{
				try (InputStream is = new FileInputStream(absoluteFileName))
				{
					return Objects.requireNonNull(is).readAllBytes();
				}
				catch (IOException | NullPointerException e)
				{
					Log.getInstance().debug("File not found in http root directory: " + fileName);
				}
			}
		}
		return null;
	}

	/**
	 * This method return the mime type of the specified fileName based on the extension of the file.
	 * If the fileName is empty string or no extension is present then empty string is returned.
	 * @param fileName The file name
	 * @return The mime type of the file based on the file extension
	 */
	protected String getMimeType(String fileName)
	{
		int dotIndex = fileName.lastIndexOf('.');
		String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex+1).toLowerCase();
		return switch (extension) {
			case "aac" -> "audio/aac";
			case "abw" -> "application/x-abiword";
			case "arc" -> "application/x-freearc";
			case "avif" -> "image/avif";
			case "avi" -> "video/x-msvideo";
			case "azw" -> "application/vnd.amazon.ebook";
			case "bmp" -> "image/bmp";
			case "bz" -> "application/x-bzip";
			case "bz2" -> "application/x-bzip2";
			case "cda" -> "application/x-cdf";
			case "csh" -> "application/x-csh";
			case "css" -> "text/css";
			case "csv" -> "text/csv";
			case "doc" -> "application/msword";
			case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			case "eot" -> "application/vnd.ms-fontobject";
			case "epub" -> "application/epub+zip";
			case "gz" -> "application/gzip";
			case "gif" -> "image/gif";
			case "htm", "html" -> "text/html";
			case "ico" -> "image/vnd.microsoft.icon";
			case "ics" -> "text/calendar";
			case "jar" -> "application/java-archive";
			case "jpeg", "jpg" -> "image/jpeg";
			case "js", "mjs" -> "text/javascript";
			case "json" -> "application/json";
			case "jsonld" -> "application/ld+json";
			case "mid", "midi" -> "audio/midi";
			case "mkv" -> "video/x-matroska";
			case "mp3" -> "audio/mpeg";
			case "mp4" -> "video/mp4";
			case "mpeg" -> "video/mpeg";
			case "mpkg" -> "application/vnd.apple.installer+xml";
			case "odp" -> "application/vnd.oasis.opendocument.presentation";
			case "ods" -> "application/vnd.oasis.opendocument.spreadsheet";
			case "odt" -> "application/vnd.oasis.opendocument.text";
			case "oga" -> "audio/ogg";
			case "ogv" -> "video/ogg";
			case "ogx" -> "application/ogg";
			case "opus" -> "audio/opus";
			case "otf" -> "font/otf";
			case "png" -> "image/png";
			case "pdf" -> "application/pdf";
			case "php" -> "application/x-httpd-php";
			case "ppt" -> "application/vnd.ms-powerpoint";
			case "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation";
			case "rar" -> "application/vnd.rar";
			case "rtf" -> "application/rtf";
			case "sh" -> "application/x-sh";
			case "svg" -> "image/svg+xml";
			case "tar" -> "application/x-tar";
			case "tif", "tiff" -> "image/tiff";
			case "ts" -> "video/mp2t";
			case "ttf" -> "font/ttf";
			case "txt" -> "text/plain";
			case "vsd" -> "application/vnd.visio";
			case "wav" -> "audio/wav";
			case "weba" -> "audio/webm";
			case "webm" -> "video/webm";
			case "webp" -> "image/webp";
			case "woff" -> "font/woff";
			case "woff2" -> "font/woff2";
			case "xhtml" -> "application/xhtml+xml";
			case "xls" -> "application/vnd.ms-excel";
			case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			case "xml" -> "application/xml";
			case "xul" -> "application/vnd.mozilla.xul+xml";
			case "zip" -> "application/zip";
			case "3gp" -> "video/3gpp";
			case "3g2" -> "video/3gpp2";
			case "7z" -> "application/x-7z-compressed";
			default -> "application/octet-stream"; // "bin" is also considered as "application/octet-stream"
		};
	}

	/**
	 * This method creates the response object with a particular error code.
	 * @param status The http error response code.
	 * @return Returns an instance of Response class containing all the details.
	 */
	protected Response error(int status)
	{
		String errorFileName = "error/" + status + ".html";
		byte[] responseBodyFromFile = readFromFile(errorFileName);
		return new Response(status)
				.addHeader("Content-Type", getMimeType(errorFileName))
				.setBody((responseBodyFromFile == null) ? readFromResource(errorFileName) : responseBodyFromFile);
	}

	/**
	 * This method creates a response object with the content of the resource file as response body and required headers.
	 * @param fileName The file name for the response body.
	 * @return Returns an instance of Response class containing all the details.
	 */
	protected Response fromResource(String fileName)
	{
		byte[] responseBody = readFromResource(fileName);
		if (responseBody != null)
		{
			return new Response(responseBody).addHeader("Content-Type", getMimeType(fileName));
		}
		else
		{
			return error(404);
		}
	}

	/**
	 * This method creates a response object with the content of the file as response body and required headers.
	 * @param fileName The file name for the response body.
	 * @return Returns an instance of Response class containing all the details.
	 */
	protected Response fromFile(String fileName)
	{
		byte[] responseBody = readFromFile(fileName);
		if (responseBody != null)
		{
			return new Response(responseBody).addHeader("Content-Type", getMimeType(fileName));
		}
		else
		{
			return error(404);
		}
	}

	Response process(Request request)
	{
		try
		{
			return switch (request.method()) {
				case "GET" -> get(request);
				case "HEAD" -> head(request);
				case "POST" -> post(request);
				case "PUT" -> put(request);
				case "DELETE" -> delete(request);
				case "CONNECT" -> connect(request);
				case "OPTIONS" -> options(request);
				case "TRACE" -> trace(request);
				case "PATCH" -> patch(request);
				default -> none(request);
			};
		} catch (Exception e)
		{
			Log.getInstance().error(e);
			return new Response().setStatusCode(500);
		}
	}

	/**
	 * This method handles all the GET requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response get(Request request)
	{
		return fromFile(request.path());
	}

	/**
	 * This method handles all the HEAD requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response head(Request request)
	{
		return error(405);
	}

	/**
	 * This method handles all the POST requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response post(Request request)
	{
		return error(405);
	}

	/**
	 * This method handles all the PUT requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response put(Request request)
	{
		return error(405);
	}

	/**
	 * This method handles all the DELETE requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response delete(Request request)
	{
		return error(405);
	}

	/**
	 * This method handles all the CONNECT requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response connect(Request request)
	{
		return error(405);
	}

	/**
	 * This method handles all the OPTIONS requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response options(Request request)
	{
		return error(405);
	}

	/**
	 * This method handles all the TRACE requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response trace(Request request)
	{
		return error(405);
	}

	/**
	 * This method handles all the PATCH requests.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response patch(Request request)
	{
		return error(405);
	}

	/**
	 * This method handles any other type of HTTP request methods.
	 * @param request The request object created from the client request.
	 * @return Returns an instance of Response class containing all the details.
	 */
	public Response none(Request request)
	{
		return error(405);
	}
}
