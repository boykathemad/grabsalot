/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.grabsalot.gui;

/**
 *
 * @author madboyka
 */
public enum EDetailViewMode {
	Details,List,InfoPanels,ServicePage;

	@Override
	public String toString() {
		switch (this) {
			case Details:
			return "DetailViewMode.Details";
			case List:
			return "DetailViewMode.List";
			case InfoPanels:
			return "DetailViewMode.InfoPanels";
			case ServicePage:
			return "DetailViewMode.ServicePage";
			default:
				return null;
		}
	}
}
