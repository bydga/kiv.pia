/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.bydzovsky;

/**
 *
 * @author bydga
 */
public class PagerItem {

	private String text;
	private int number;
	private boolean isActive;

	public PagerItem(String text, int number, boolean isActive) {
		this.text = text;
		this.number = number;
		this.isActive = isActive;
	}

	public String getText() {
		return text;
	}

	public int getNumber() {
		return number;
	}

	public boolean isActive() {
		return isActive;
	}
}
