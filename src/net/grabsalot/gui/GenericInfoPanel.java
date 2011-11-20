package net.grabsalot.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

/** A generic info panel that is to be extended with visual information about a collection element.
 * @author madboyka
 *
 */
public abstract class GenericInfoPanel extends JPanel {

	private static final long serialVersionUID = -5346386552806864912L;
	protected Dimension defaultSize;
	protected boolean minimalMode = false;
	protected String elementName;
	
	protected Color backgroundColor = Color.BLACK;
	protected Color foregroundColor = Color.LIGHT_GRAY;
	
	/**
	 * Default constructor that sets some default visual properties
	 */
	public GenericInfoPanel() {
		this.setBackground(this.backgroundColor);
		this.setForeground(foregroundColor);
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(this.defaultSize);
	}

	/**
	 * Align the components displayed on the panel according to the minimalMode setting.
	 */
	protected abstract void alignComponents();

	/**
	 * Create necessary component and add to this panel
	 */
	protected abstract void setupComponents();

	public String getElementName() {
		return this.elementName;
	}

	/** Set whether the component is in minimal mode or not
	 * @param minimal
	 */
	public void setMinimalMode(boolean minimal) {
		if (minimal == minimalMode) {
			return;
		}
		minimalMode = minimal;
		alignComponents();
	}

}
