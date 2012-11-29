/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.bydzovsky;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bydga
 */
public class Pager extends ArrayList<PagerItem> {

	public Pager(int curentPage, int totalRecords, int perPage) {
		super();

		PagerItem first = new PagerItem("&laquo; First", 1, curentPage != 1);
		PagerItem prev = new PagerItem("&lsaquo; Previous", curentPage - 1, curentPage > 1);
		this.add(first);
		this.add(prev);
		int pages = (int) Math.ceil((float)totalRecords / perPage);
		for (int i = 1; i <= pages; i++) {
			PagerItem item = new PagerItem("" + i, i, curentPage != i);
			this.add(item);
		}

		PagerItem next = new PagerItem("Next &rsaquo;", curentPage + 1, curentPage <= pages - 1);
		PagerItem last = new PagerItem("Last &raquo;", pages, curentPage != pages && pages != 0);
		this.add(next);
		this.add(last);
	}
}
