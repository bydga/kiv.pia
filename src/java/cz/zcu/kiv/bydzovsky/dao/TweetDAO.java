/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.bydzovsky.dao;

import cz.zcu.kiv.bydzovsky.Tweet;
import cz.zcu.kiv.bydzovsky.User;
import cz.zcu.kiv.bydzovsky.TweetResult;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bydga
 */
public class TweetDAO {

	private final DBSettings dbSettings;

	public TweetDAO(DBSettings dbSettings) {
		this.dbSettings = dbSettings;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public TweetResult getUserStream(User u, int offset, int limit) {
		TweetResult tweets = new TweetResult();

		try {
			Connection connection = DriverManager.getConnection(this.dbSettings.getConnectionUrl(), this.dbSettings.getUser(), this.dbSettings.getPassword());
			PreparedStatement preparedStatement = connection.prepareStatement(""
					+ "SELECT COUNT(tweet_id) "
					+ "FROM bydga_tweet t "
					+ "WHERE t.user_id IN (SELECT friend_id FROM bydga_friend f WHERE f.user_id=?) OR t.user_id=?");
			preparedStatement.setInt(1, u.getId());
			preparedStatement.setInt(2, u.getId());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				tweets.setTotalCount(resultSet.getInt(1));
			}
		} catch (SQLException ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
		}


		try {
			Connection connection = DriverManager.getConnection(this.dbSettings.getConnectionUrl(), this.dbSettings.getUser(), this.dbSettings.getPassword());
			PreparedStatement preparedStatement = connection.prepareStatement(""
					+ "SELECT t.*, u.*, rt.tweet_id AS orig_tweet_id, "
					+ "rt.published AS orig_published, rt.user_id AS orig_user_id, rt.text AS orig_text, rt.retweet_id AS orig_retweet_id, "
					+ "ru.login AS orig_login, ru.password AS orig_password, ru.name AS orig_name, ru.surname AS orig_surname, ru.sex AS orig_sex, ru.birthdate AS orig_birthdate, ru.bio AS orig_bio, ru.image AS orig_image, "
					+ "COUNT(s.tweet_id) AS retweet_count, COUNT(so.tweet_id) AS orig_retweet_count "
					+ "FROM bydga_tweet t "
					+ "INNER JOIN bydga_user u ON u.user_id=t.user_id "
					+ "LEFT JOIN bydga_tweet rt ON rt.tweet_id=t.retweet_id "
					+ "LEFT JOIN bydga_user ru ON ru.user_id=rt.user_id "
					+ "LEFT JOIN bydga_tweet s ON s.retweet_id=t.tweet_id "
					+ "LEFT JOIN bydga_tweet so ON so.retweet_id=t.retweet_id "
					+ "WHERE t.user_id IN (SELECT friend_id FROM bydga_friend f WHERE f.user_id=?) OR t.user_id=? GROUP BY tweet_id ORDER BY t.published DESC LIMIT ? OFFSET ?");
			preparedStatement.setInt(1, u.getId());
			preparedStatement.setInt(2, u.getId());
			preparedStatement.setInt(3, limit);
			preparedStatement.setInt(4, offset);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Tweet t = this.fillTweetFromDB(resultSet, "");
				tweets.add(t);
			}
		} catch (SQLException ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
		}

		return tweets;
	}

	public TweetResult getTweetsFromUser(User u, int offset, int limit) {
		TweetResult tweets = new TweetResult();
		try {
			Connection connection = DriverManager.getConnection(this.dbSettings.getConnectionUrl(), this.dbSettings.getUser(), this.dbSettings.getPassword());
			PreparedStatement preparedStatement = connection.prepareStatement(""
					+ "SELECT COUNT(tweet_id) "
					+ "FROM bydga_tweet t "
					+ "INNER JOIN bydga_user u ON u.user_id=t.user_id "
					+ "WHERE t.user_id=?");
			preparedStatement.setInt(1, u.getId());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				tweets.setTotalCount(resultSet.getInt(1));
			}
		} catch (SQLException ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
		}

		try {
			Connection connection = DriverManager.getConnection(this.dbSettings.getConnectionUrl(), this.dbSettings.getUser(), this.dbSettings.getPassword());
			PreparedStatement preparedStatement = connection.prepareStatement(""
					+ "SELECT t.*, u.*, rt.tweet_id AS orig_tweet_id, "
					+ "rt.published AS orig_published, rt.user_id AS orig_user_id, rt.text AS orig_text, rt.retweet_id AS orig_retweet_id, "
					+ "ru.login AS orig_login, ru.password AS orig_password, ru.name AS orig_name, ru.surname AS orig_surname, ru.sex AS orig_sex, ru.birthdate AS orig_birthdate, ru.bio AS orig_bio, ru.image AS orig_image, "
					+ "COUNT(s.tweet_id) AS retweet_count, COUNT(so.tweet_id) AS orig_retweet_count "
					+ "FROM bydga_tweet t "
					+ "INNER JOIN bydga_user u ON u.user_id=t.user_id "
					+ "LEFT JOIN bydga_tweet rt ON rt.tweet_id=t.retweet_id "
					+ "LEFT JOIN bydga_user ru ON ru.user_id=rt.user_id "
					+ "LEFT JOIN bydga_tweet s ON s.retweet_id=t.tweet_id "
					+ "LEFT JOIN bydga_tweet so ON so.retweet_id=t.retweet_id "
					+ "WHERE t.user_id=? GROUP BY tweet_id ORDER BY t.published DESC LIMIT ? OFFSET ?");
			preparedStatement.setInt(1, u.getId());
			preparedStatement.setInt(2, limit);
			preparedStatement.setInt(3, offset);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Tweet t = this.fillTweetFromDB(resultSet, "");
				tweets.add(t);
			}
		} catch (SQLException ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
		}

		return tweets;
	}

	public Tweet insertTweet(Tweet t) {

		try {
			Connection connection = DriverManager.getConnection(this.dbSettings.getConnectionUrl(), this.dbSettings.getUser(), this.dbSettings.getPassword());

			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO bydga_tweet( published, user_id, text, retweet_id )"
					+ "VALUES (?,?,?,?)", ResultSet.FETCH_UNKNOWN, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setTimestamp(1, new java.sql.Timestamp(t.getPublished().getTime()));
			preparedStatement.setInt(2, t.getAuthor().getId());
			preparedStatement.setString(3, t.getText());
			if (t.getRetweetedFrom() == null) {
				preparedStatement.setNull(4, java.sql.Types.NULL);
			} else {
				preparedStatement.setInt(4, t.getRetweetedFrom().getTweetId());
			}
			int res = preparedStatement.executeUpdate();
			if (res == 0) {
				throw new SQLException("Cannot insert tweet");
			}
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				t.setTweetId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Cannot insert tweet");
			}
		} catch (SQLException ex) {
		}

		return t;
	}

	private Tweet fillTweetFromDB(ResultSet resultSet, String prefix) {
		Tweet tweet = new Tweet();
		try {
			tweet.setTweetId(resultSet.getInt(prefix + "tweet_id"));
			tweet.setPublished(resultSet.getTimestamp(prefix + "published"));
			tweet.setText(resultSet.getString(prefix + "text").replaceAll("(\r\n|\r|\n)", "<br />"));
			try {
				tweet.setRwetweetCount(resultSet.getInt(prefix + "retweet_count"));
			} catch (SQLException ex) {
				tweet.setRwetweetCount(0);
			}

			User u = new User();
			u.setId(resultSet.getInt(prefix + "user_id"));
			u.setLogin(resultSet.getString(prefix + "login"));
			u.setName(resultSet.getString(prefix + "name"));
			u.setSurname(resultSet.getString(prefix + "surname"));
			u.setPassword(resultSet.getString(prefix + "password"));
			u.setBio(resultSet.getString(prefix + "bio"));
			u.setSex(User.Sex.valueOf(resultSet.getString(prefix + "sex")));
			u.setBirthdate(resultSet.getDate(prefix + "birthdate"));
			String image = resultSet.getString(prefix + "image") != null ? resultSet.getString(prefix + "image") : u.getSex().toString().toLowerCase() + ".jpg";
			u.setImage(image);
			tweet.setAuthor(u);

			if (resultSet.getInt(prefix + "retweet_id") > 0) {
				Tweet retweetedFrom = this.fillTweetFromDB(resultSet, "orig_");
				tweet.setRetweetedFrom(retweetedFrom);
				tweet.setRwetweetCount(retweetedFrom.getRetweetCount());

			} else {
				tweet.setRetweetedFrom(null);
			}

		} catch (Exception ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}

		return tweet;
	}

	public Tweet getTweetById(int tweetId) {
		try {
			Connection connection = DriverManager.getConnection(this.dbSettings.getConnectionUrl(), this.dbSettings.getUser(), this.dbSettings.getPassword());
			PreparedStatement preparedStatement = connection.prepareStatement(""
					+ "SELECT t.*, u.*, rt.tweet_id AS orig_tweet_id, "
					+ "rt.published AS orig_published, rt.user_id AS orig_user_id, rt.text AS orig_text, rt.retweet_id AS orig_retweet_id, "
					+ "ru.login AS orig_login, ru.password AS orig_password, ru.name AS orig_name, ru.surname AS orig_surname, ru.sex AS orig_sex, ru.birthdate AS orig_birthdate, ru.bio AS orig_bio, ru.image AS orig_image "
					+ "FROM bydga_tweet t "
					+ "INNER JOIN bydga_user u ON u.user_id=t.user_id "
					+ "LEFT JOIN bydga_tweet rt ON rt.tweet_id=t.retweet_id "
					+ "LEFT JOIN bydga_user ru ON ru.user_id=rt.user_id "
					+ "WHERE t.tweet_id=?");
			preparedStatement.setInt(1, tweetId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return this.fillTweetFromDB(resultSet, "");
			}
		} catch (SQLException ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}
