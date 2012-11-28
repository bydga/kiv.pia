/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.bydzovsky.dao;

/**
 *
 * @author bydga
 */
import cz.zcu.kiv.bydzovsky.User;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

public class UserDAO {

	private final DBSettings dbSettings;

	public UserDAO(DBSettings dbSettings) {
		this.dbSettings = dbSettings;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public User insertUser(User u) throws SQLException {


		try {
			byte[] bytes = MessageDigest.getInstance("MD5").digest(u.getPassword().getBytes("UTF-8"));
			String pass = String.format("%0" + (bytes.length << 1) + "X", new BigInteger(1, bytes));
			u.setPassword(pass);
		} catch (Exception ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Connection connection = DriverManager.getConnection(this.dbSettings.getConnectionUrl(), this.dbSettings.getUser(), this.dbSettings.getPassword());

		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO bydga_user( login, password, name, surname, sex, birthdate, bio )"
				+ "VALUES (?,?,?,?,?,?,?)", ResultSet.FETCH_UNKNOWN, Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, u.getLogin());
		preparedStatement.setString(2, u.getPassword());
		preparedStatement.setString(3, u.getName());
		preparedStatement.setString(4, u.getSurname());
		preparedStatement.setString(5, u.getSex().toString());
		preparedStatement.setString(6, u.getBirthdate() != null ? formatter.format(u.getBirthdate()) : null);
		preparedStatement.setString(7, u.getBio());
		int res = preparedStatement.executeUpdate();
		if (res == 0) {
			throw new SQLException("Cannot insert user");
		}
		ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			u.setId(generatedKeys.getInt(1));
		} else {
			throw new SQLException("Cannot insert user");
		}

		return u;

	}

	public boolean areUsersInRelation(User u1, User u2) {
		if (u1 == null || u2 == null) {
			return false;
		}
		try {
			Connection connection = DriverManager.getConnection(this.dbSettings.getConnectionUrl(), this.dbSettings.getUser(), this.dbSettings.getPassword());
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM bydga_friend WHERE user_id=? AND friend_id=?");
			preparedStatement.setInt(1, u1.getId());
			preparedStatement.setInt(2, u2.getId());
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			}

		} catch (SQLException ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	public List<User> getAllUsers() {
		List<User> out = new ArrayList<User>();
		try {
			Connection connection = DriverManager.getConnection(this.dbSettings.getConnectionUrl(), this.dbSettings.getUser(), this.dbSettings.getPassword());
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM bydga_user ORDER BY login");
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				out.add(this.fillUserFromDB(resultSet));
			}

		} catch (SQLException ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
		}

		return out;
	}

	public User getUser(int userId) {
		try {
			Connection connection = DriverManager.getConnection(this.dbSettings.getConnectionUrl(), this.dbSettings.getUser(), this.dbSettings.getPassword());
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM bydga_user WHERE user_id=?");
			preparedStatement.setInt(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();

			return resultSet.next() ? this.fillUserFromDB(resultSet) : null;

		} catch (SQLException ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public List<User> getUserFollowings(User u) {
		return this.getUserConnections(u, "friend_id", "user_id");
	}

	public List<User> getFollowersOfUser(User u) {
		return this.getUserConnections(u, "user_id", "friend_id");
	}

	private List<User> getUserConnections(User u, String joinColumn, String whereColumn) {
		List<User> output = new ArrayList<User>();
		try {
			Connection connection = DriverManager.getConnection(this.dbSettings.getConnectionUrl(), this.dbSettings.getUser(), this.dbSettings.getPassword());
			PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT u.* FROM bydga_friend f INNER JOIN bydga_user u ON u.user_id=f." + joinColumn + " WHERE f." + whereColumn + "=?");
			preparedStatement.setInt(1, u.getId());
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				output.add(this.fillUserFromDB(resultSet));
			}

		} catch (SQLException ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}

		return output;
	}

	public User authenticateUser(String login, String pass) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = DriverManager.getConnection(this.dbSettings.getConnectionUrl(), this.dbSettings.getUser(), this.dbSettings.getPassword());

			try {
				byte[] bytes = MessageDigest.getInstance("MD5").digest(pass.getBytes("UTF-8"));
				pass = String.format("%0" + (bytes.length << 1) + "x", new BigInteger(1, bytes));
			} catch (NoSuchAlgorithmException ex) {
				Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
			} catch (UnsupportedEncodingException ex) {
				Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
			}

			preparedStatement = connection.prepareStatement("SELECT * FROM bydga_user WHERE login=? AND password=?");
			preparedStatement.setString(1, login);
			preparedStatement.setString(2, pass);
			resultSet = preparedStatement.executeQuery();

			return resultSet.next() ? this.fillUserFromDB(resultSet) : null;

		} catch (SQLException e) {
			return null;
		}

	}

	private User fillUserFromDB(ResultSet resultSet) {
		User user = new User();
		try {
			user.setId(resultSet.getInt("user_id"));
			user.setLogin(resultSet.getString("login"));
			user.setName(resultSet.getString("name"));
			user.setSurname(resultSet.getString("surname"));
			user.setPassword(resultSet.getString("password"));
			user.setBio(resultSet.getString("bio"));
			user.setSex(User.Sex.valueOf(resultSet.getString("sex")));
			user.setBirthdate(resultSet.getDate("birthdate"));
			String image = resultSet.getString("image") != null ? resultSet.getString("image") : user.getSex().toString().toLowerCase() + ".jpg";
			user.setImage(image);
		} catch (Exception ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}

		return user;
	}

	public void deleteRelation(User loggedUser, User user) {
		try {
			Connection connection = DriverManager.getConnection(this.dbSettings.getConnectionUrl(), this.dbSettings.getUser(), this.dbSettings.getPassword());
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM bydga_friend WHERE user_id=? AND friend_id=?");
			preparedStatement.setInt(1, loggedUser.getId());
			preparedStatement.setInt(2, user.getId());
			int res = preparedStatement.executeUpdate();

		} catch (SQLException ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void addRelation(User loggedUser, User user) {
		try {
			Connection connection = DriverManager.getConnection(this.dbSettings.getConnectionUrl(), this.dbSettings.getUser(), this.dbSettings.getPassword());
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO bydga_friend (user_id, friend_id) VALUES (?, ?)");
			preparedStatement.setInt(1, loggedUser.getId());
			preparedStatement.setInt(2, user.getId());
			int res = preparedStatement.executeUpdate();

		} catch (SQLException ex) {
			Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
