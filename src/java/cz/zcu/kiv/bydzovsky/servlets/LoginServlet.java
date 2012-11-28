/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.bydzovsky.servlets;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import cz.zcu.kiv.bydzovsky.User;
import cz.zcu.kiv.bydzovsky.dao.UserDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bydga
 */
public class LoginServlet extends BaseServelet {

	/**
	 * Handles the HTTP
	 * <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("logout") != null) {
			request.getSession().removeAttribute("loggedUser");
		}

		if (request.getSession().getAttribute("loggedUser") != null) {
			response.sendRedirect("stream");
			return;
		}
		this.addCaptcha(request);
		request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request, response);
	}

	private void addCaptcha(HttpServletRequest req) {
		Random r = new Random();
		int a = r.nextInt(10);
		int b = r.nextInt(10);
		req.setAttribute("captchaA", a);
		req.setAttribute("captchaB", b);
	}

	/**
	 * Handles the HTTP
	 * <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String type = request.getParameter("form-type");
		if (type.equals("login")) {
			String name = request.getParameter("nickname");
			String pass = request.getParameter("password");
			UserDAO dao = new UserDAO(this.getDBSettings());
			User u = dao.authenticateUser(name, pass);
			if (u != null) {
				request.getSession().setAttribute("loggedUser", u);
				response.sendRedirect("stream");
			} else {
				request.setAttribute("nickname", name);
				request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request, response);
			}
		} else if (type.equals("register")) {
			User u = new User();

			//validate
			List<String> errors = new ArrayList<String>();
			if (request.getParameter("login") == null || request.getParameter("login").trim().isEmpty()) {
				errors.add("Login is required.");
			}

			if (request.getParameter("password") == null || request.getParameter("password").trim().isEmpty()) {
				errors.add("Password is required.");
			}

			if (!request.getParameter("password").equals(request.getParameter("password2"))) {
				errors.add("Passwords do not match");
			}

			if (request.getParameter("captcha") == null || !request.getParameter("captcha").equals(request.getParameter("captcha-result"))) {
				errors.add("Wrong captcha.");
			}


			if (request.getParameter("birthdate") == null || request.getParameter("birthdate").trim().isEmpty()) {
				u.setBirthdate(null);
			} else {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
					u.setBirthdate(sdf.parse(request.getParameter("birthdate")));
				} catch (ParseException ex) {
					errors.add("Incorrect date of birth - must be in format \"day.month.year\".");
				}
			}
			u.setLogin(request.getParameter("login"));
			u.setPassword(request.getParameter("password"));
			u.setBio(request.getParameter("bio"));
			u.setName(request.getParameter("name"));
			u.setSurname(request.getParameter("surname"));
			u.setSex(User.Sex.valueOf(request.getParameter("sex")));

			if (errors.isEmpty()) {
				UserDAO dao = new UserDAO(this.getDBSettings());

				try {
					dao.insertUser(u);
					request.getSession().setAttribute("loggedUser", u);
					response.sendRedirect("stream");
					return;
				} catch (MySQLIntegrityConstraintViolationException ex) {
					errors.add("Desired login name is already in use");
				} catch (SQLException ex) {
					errors.add("SQL error occured " + ex.getMessage());
				}
			}

			if (errors.size() > 0) {
				this.addCaptcha(request);
				request.setAttribute("errors", errors);
				request.setAttribute("userFilled", u);
				request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request, response);
			}
		}
	}
}
