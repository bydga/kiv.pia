/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.bydzovsky;

import java.util.ArrayList;

/**
 *
 * @author bydga
 */
public 	class TweetResult extends ArrayList<Tweet> {

		private int totalCount;

		public int getTotalCount() {
			return this.totalCount;
		}

		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}
	}
