/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.bydzovsky.servlets;

import cz.zcu.kiv.bydzovsky.Pager;
import cz.zcu.kiv.bydzovsky.TweetResult;
import cz.zcu.kiv.bydzovsky.servlets.BaseServelet;
import cz.zcu.kiv.bydzovsky.dao.TweetDAO;
import cz.zcu.kiv.bydzovsky.User;
import cz.zcu.kiv.bydzovsky.dao.UserDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bydga
 */
public class UserServlet extends BaseServelet {

	public static int TWEETS_PER_PAGE = 10;

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

		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		request.setAttribute("loggedUser", loggedUser);
		User user;
		UserDAO userDAO = new UserDAO(this.getDBSettings());

		String unfollow = request.getParameter("unfollow");
		if (loggedUser != null && unfollow != null) {
			int unfollowId = Integer.parseInt(unfollow);
			userDAO.deleteRelation(loggedUser, userDAO.getUser(unfollowId));
			response.sendRedirect("user?id=" + unfollowId);
			return;
		}

		String follow = request.getParameter("follow");
		if (loggedUser != null && follow != null) {
			int followId = Integer.parseInt(follow);
			userDAO.addRelation(loggedUser, userDAO.getUser(followId));
			response.sendRedirect("user?id=" + followId);
			return;
		}

		String uid = request.getParameter("id");
		if (uid == null) {
			request.setAttribute("users", userDAO.getAllUsers());
			request.getRequestDispatcher("WEB-INF/jsp/allUsers.jsp").forward(request, response);
			return;
		}

		try {
			int userId = Integer.parseInt(uid);
			user = userDAO.getUser(userId);

		} catch (NumberFormatException ex) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid user id");
			return;
		}


		boolean friends = userDAO.areUsersInRelation(loggedUser, user);

		request.setAttribute("friends", friends);
		request.setAttribute("user", user);
		request.setAttribute("followers", userDAO.getFollowersOfUser(user));
		request.setAttribute("followings", userDAO.getUserFollowings(user));
		TweetDAO tweetDAO = new TweetDAO(this.getDBSettings());
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch (NumberFormatException ex) {
		}

		int offset = UserServlet.TWEETS_PER_PAGE * Math.abs(page - 1);
		TweetResult tweets = tweetDAO.getTweetsFromUser(user, offset, UserServlet.TWEETS_PER_PAGE);
		request.setAttribute("userTweets", tweets);
		Pager p = new Pager(page, tweets.getTotalCount(), UserServlet.TWEETS_PER_PAGE);
		request.setAttribute("tweetsPager", p);

		request.getRequestDispatcher("WEB-INF/jsp/user.jsp").forward(request, response);
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
	}
}
