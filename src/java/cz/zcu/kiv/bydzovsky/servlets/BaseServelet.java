package cz.zcu.kiv.bydzovsky.servlets;

import cz.zcu.kiv.bydzovsky.dao.DBSettings;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bydga
 */
public abstract class BaseServelet extends HttpServlet {

	private DBSettings settings = null;

	protected DBSettings getDBSettings() {
		if (settings == null) {
			settings = new DBSettings();
			settings.setUser(this.getServletContext().getInitParameter("database.user"));
			settings.setDatabase(this.getServletContext().getInitParameter("database.database"));
			settings.setPort(Integer.parseInt(this.getServletContext().getInitParameter("database.port")));
			settings.setPassword(this.getServletContext().getInitParameter("database.password"));
			settings.setServer(this.getServletContext().getInitParameter("database.server"));
		}
		return this.settings;
	}
}
