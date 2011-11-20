package net.grabsalot.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Window;

import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import net.grabsalot.core.Application;
import net.grabsalot.util.ImageUtil;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * The about(application information) dialogs class.
 *
 * @author madboyka
 *
 */
public class AboutDialog extends JDialog {

	private static final long serialVersionUID = -8873087995699252480L;
	private static final Map<String, String> libraries = new HashMap<String, String>();

	static {
		libraries.put("dom4j", "http://dom4j.sourceforge.net/");
		libraries.put("jaudiotagger", "http://www.jthink.net/jaudiotagger/");
		libraries.put("jflac", "http://jflac.sourceforge.net/");
		libraries.put("jmac", "http://jmac.sourceforge.net/");
		libraries.put("jlayer", "http://www.javazoom.net/javalayer/javalayer.html");
		libraries.put("tritonus", "http://www.tritonus.org/");
		libraries.put("mp3spi", "http://www.javazoom.net/mp3spi/mp3spi.html");
	}
	private JImageBox ibxLogo;
	private JTextPane txpCredits;
	private JScrollPane jspCredits;

	public AboutDialog(Window owner, ModalityType modalityType) {
		super(owner, modalityType);
		this.setTitle(Application.name);
		this.setupComponents();
		this.setPreferredSize(new Dimension(600, 600));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		// this.setModal(true);
		// this.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		// this.setModalityType(ModalityType.APPLICATION_MODAL);
	}

	private void setupComponents() {
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

		ibxLogo = new JImageBox(ImageUtil.loadFromResource("net/grabsalot/logo.png"));
		this.add(ibxLogo);

		txpCredits = new JTextPane();
		jspCredits = new JScrollPane(txpCredits);
		txpCredits.setContentType("text/html");
		txpCredits.setEditable(false);
		String creditText = "<a name=\"top\"></a>";
		creditText += "<div style='text-align:center'>";
		creditText += "<h1>" + Application.name + "</h1>";
		creditText += "<h3> version " + Application.version + " released on " + Application.released + "</h3>";
		creditText += "<h3> by " + Application.author + "</h3>";
		creditText += "</div>";

		creditText += "All used graphics are created by me (lame, but no licensing issues)\n"
				+ "Third party libraries used( some may be obsolete in the current version):\n";
		creditText += "<ul>";
		for (String name : libraries.keySet()) {
			String website = libraries.get(name);
			creditText += "<li><a href=\"" + website + "\">" + name + "(" + website.replace("http://", "") + ")" + "</a></li>\n";
		}
		creditText += "</ul>";

		creditText += "<span style='color:red; font-size:8px;'>If you're the owner of any of these libraries, and you think I'm not using your library fairly, please contact me on [gaspar.portik@gmail.com] </span>";
		creditText += new StringBuilder().append("<pre>Running on: ").
				append(System.getProperty("os.name")).append(" ").
				append(System.getProperty("os.version")).append(" ").
				append(System.getProperty("os.arch")).
				append("<br />Java runtime:").
				append(System.getProperty("java.vendor")).append(" ").
				append(System.getProperty("java.version")).append("</pre>").toString();

		txpCredits.setText(creditText);
		txpCredits.setPreferredSize(new Dimension(200, 100));
		this.add(jspCredits);

//		jspCredits.getVerticalScrollBar().setValue(0);
	}
}
