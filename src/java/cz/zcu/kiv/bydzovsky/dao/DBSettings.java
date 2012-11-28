/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.bydzovsky.dao;

/**
 *
 * @author bydga
 */
import java.text.MessageFormat;

public class DBSettings {
	private static final String DB_ENCODING = "UTF-8";
	private String server;
	private int port;
	private String user;
	private String password;
	private String database;

	public String getConnectionUrl() {
		return MessageFormat.format("jdbc:mysql://{0}:{1,number,#}/{2}?characterEncoding={3}", this.server, this.port, this.database, DB_ENCODING);
	}

	public String getServer() {
		return this.server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDatabase() {
		return this.database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
}

