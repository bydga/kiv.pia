/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.bydzovsky.servlets;

import cz.zcu.kiv.bydzovsky.Pager;
import cz.zcu.kiv.bydzovsky.Tweet;
import cz.zcu.kiv.bydzovsky.TweetResult;
import cz.zcu.kiv.bydzovsky.dao.TweetDAO;
import cz.zcu.kiv.bydzovsky.User;
import cz.zcu.kiv.bydzovsky.dao.UserDAO;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author bydga
 */
public class StreamServlet extends BaseServelet {

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

		User user = (User) request.getSession().getAttribute("loggedUser");
		if (user == null) {
			response.sendRedirect("login");
			return;
		}

		TweetDAO tweetDAO = new TweetDAO(this.getDBSettings());
		String retweet = request.getParameter("retweet");
		if (retweet != null) {
			try {
				int tweetId = Integer.parseInt(retweet);
				Tweet original = tweetDAO.getTweetById(tweetId);
				Tweet newTweet = new Tweet();
				newTweet.setAuthor(user);
				newTweet.setPublished(new Date());
				newTweet.setRetweetedFrom(original.getRetweetedFrom() != null ? original.getRetweetedFrom() : original);
				newTweet.setText(original.getText());
				tweetDAO.insertTweet(newTweet);
				response.sendRedirect("stream");
				return;

			} catch (NumberFormatException ex) {
				response.sendRedirect("stream");
				return;
			}
		}

		UserDAO dao = new UserDAO(this.getDBSettings());

		request.setAttribute("user", user);
		request.setAttribute("followers", dao.getFollowersOfUser(user));
		request.setAttribute("followings", dao.getUserFollowings(user));
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch (NumberFormatException ex) {
			//page is set to 1
		}

		int offset = StreamServlet.TWEETS_PER_PAGE * Math.abs(page - 1);
		TweetResult result = tweetDAO.getUserStream(user, offset, StreamServlet.TWEETS_PER_PAGE);
		request.setAttribute("tweets", result);

		Pager p = new Pager(page, result.getTotalCount(), StreamServlet.TWEETS_PER_PAGE);
		request.setAttribute("tweetsPager", p);

		request.getRequestDispatcher("WEB-INF/jsp/stream.jsp").forward(request, response);
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


		User user = (User) request.getSession().getAttribute("loggedUser");
		if (user != null) {

			String tweetText = request.getParameter("new-tweet-textarea");
			if (tweetText != null) {
				//new tweet
				request.setCharacterEncoding("UTF-8");
				Tweet t = new Tweet();
				t.setAuthor(user);
				t.setText(tweetText);
				t.setPublished(new Date());
				TweetDAO dao = new TweetDAO(this.getDBSettings());
				dao.insertTweet(t);
			} else {
				try {
					Random r = new Random();
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List<FileItem> items = upload.parseRequest(request);
					if (items.size() == 1) {
						FileItem item = items.get(0);
						BufferedImage img = ImageIO.read(item.getInputStream());
						String fileName = user.getId() + "-" + r.nextInt(100) + ".png";
						String fullPath = getServletContext().getRealPath("/") + "user-images" + File.separator + fileName;
						BufferedImage resized = this.createResizedCopy(img, 50, 50);
						boolean res = ImageIO.write(resized, "png", new File(fullPath));
						if (res) {
							UserDAO dao = new UserDAO(this.getDBSettings());
							user.setImage(fileName);
							dao.updateUserImage(user);
						}
					}

				} catch (FileUploadException ex) {
					Logger.getLogger(StreamServlet.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			response.sendRedirect("stream");
		}
	}

	private BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight) {
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = scaledBI.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		return scaledBI;
	}
}
