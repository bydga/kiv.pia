/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.bydzovsky;

import java.awt.Image;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author bydga
 */
public class User {

	public enum Sex {

		Male, Female
	}
	private int userId;
	private String login;
	private String password;
	private String name;
	private String surname;
	private Sex sex;
	private Date birthdate;
	private String bio;
	private String image;

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getAge() {
		int age = 0;
		Calendar born = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		if (this.getBirthdate() != null) {
			now.setTime(new Date());
			born.setTime(this.getBirthdate());
			age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
			if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {
				age -= 1;
			}
			return "" + age;
		}
		return null;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String userImage) {
		this.image = userImage;
	}

	public int getId() {
		return this.userId;
	}

	public void setId(int id) {
		this.userId = id;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
