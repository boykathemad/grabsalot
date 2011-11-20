package net.grabsalot.gui;

import net.grabsalot.business.Logger;
import net.grabsalot.business.Rule;
import net.grabsalot.business.RuleSet;

import net.grabsalot.gui.RulesFrame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The frame that handle editing Rules.
 * 
 * @author madboyka
 * 
 */
public class RulesFrame extends JFrame {

	private static final long serialVersionUID = 3663873394848982817L;

	private Rule[] rules;
	private JComponent[] ruleComponents;
	private int rcIndex = 0;
	private JButton btnSave;
	private JButton btnCancel;

	/**
	 * Default constructor, calls setupComponents to setup its ui. The size of
	 * the frame depends on the number of rules currently in existence.
	 */
	public RulesFrame() {
		this.setTitle("Edit rules");
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

		this.setupComponents();
		this.pack();
	}

	/**
	 * Sets up the ui to display the rules currently in existence.
	 */
	private void setupComponents() {
		this.rules = RuleSet.getRules();
		this.ruleComponents = new JComponent[this.rules.length];
		for (Rule i : this.rules) {
			this.addRulePanel(i);
		}
		JPanel actionsPanel = new JPanel();
		actionsPanel.setPreferredSize(new Dimension(600, 100));
		this.btnSave = new JButton("Save");
		this.btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RulesFrame.this.saveRules();
				RulesFrame.this.setVisible(false);
			}
		});
		this.btnCancel = new JButton("Cancel");
		this.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RulesFrame.this.setVisible(false);
			}
		});
		actionsPanel.add(this.btnSave);
		actionsPanel.add(this.btnCancel);
		this.add(actionsPanel);
	}

	/**
	 * Saves the modifications made to the rules on this frame.
	 */
	protected void saveRules() {
		for (int i = 0; i < rules.length; ++i) {
			String value = "";
			try {
				value = ((JComboBox) this.ruleComponents[i]).getSelectedItem().toString();
			} catch (ClassCastException ex) {
				try {
					value = ((JTextField) this.ruleComponents[i]).getText();
				} catch (Exception ex2) {
					Logger._().severe("RulesFrame:saveRules:component cast failed.");
				}
			}
			this.rules[i].setValue(value);
			this.rules[i].save();
		}
		RuleSet.resetRules();
	}

	/** Created a new rule-panel, that is the visual representation of rule.
	 * @param rule the rule to create the panel for
	 */
	private void addRulePanel(Rule rule) {
		JPanel pan = new JPanel();
		JLabel label = new JLabel(rule.getName());
		pan.add(label);
		this.ruleComponents[this.rcIndex] = null;
		switch (rule.getType()) {
		case Rule.TEXT_RULE_TYPE:
			this.ruleComponents[this.rcIndex] = new JTextField();
			((JTextField) this.ruleComponents[this.rcIndex]).setText(rule.getValue());
			break;
		case Rule.OPTION_RULE_TYPE:
		case Rule.YESNO_RULE_TYPE:
			this.ruleComponents[this.rcIndex] = new JComboBox(rule.getOptions());
			((JComboBox) this.ruleComponents[this.rcIndex]).setSelectedItem(rule.getValue());
			break;
		}
		this.ruleComponents[this.rcIndex].setPreferredSize(new Dimension(200, 20));
		pan.add(this.ruleComponents[this.rcIndex]);
		this.rcIndex++;
		this.add(pan);
	}
}
