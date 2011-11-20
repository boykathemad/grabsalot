package net.grabsalot.business;

import net.grabsalot.business.Configuration;
import net.grabsalot.business.Rule;


/**
 * Specifies various rules on how the application works.
 *
 * @author madboyka
 *
 */
public class Rule {

	public static final int TEXT_RULE_TYPE = 1;
	public static final int OPTION_RULE_TYPE = 2;
	public static final int YESNO_RULE_TYPE = 3;

	public static final String ICON_DETAIL_SIMPLE = "simple";
	public static final String ICON_DETAIL_NICE = "nice";
	public static final String ICON_DETAIL_EXTENDED = "extended";
	public static final String ICON_DETAIL_EXTREME = "extreme";

	private String name;
	private String[] options;
	private int optionCount = 0;
	private int defaultOption = 0;
	private int type = 0;
	private Rule[] dependencies;
	private String value;
	private String defaultValue;
	private String option;

	/**
	 * Default constructor. Specifies the rule name and type.
	 *
	 * @param name
	 *            the name of the new rule
	 * @param type
	 *            an integer representing the type of the rule. Possible values
	 *            on {@link Rule}
	 */
	public Rule(String name, int type) {
		this.name = name;
		this.type = type;
		switch (this.type) {
		case Rule.TEXT_RULE_TYPE:
			this.value = "";
			this.defaultValue = "";
			break;
		case Rule.OPTION_RULE_TYPE:
			this.options = new String[10];
			break;
		case Rule.YESNO_RULE_TYPE:
			this.options = new String[] { "no", "yes" };
			this.optionCount = 2;
			break;
		}
	}

	/**
	 * Normalizes options if the rule is an option rule and loads the value
	 * stored in the {@link Configuration}.
	 */
	public void load() {
		if (this.type == Rule.OPTION_RULE_TYPE) {
			String[] realOptions = new String[this.optionCount];
			for (int i = 0; i < this.optionCount; ++i) {
				realOptions[i] = this.options[i];
			}
			this.options = realOptions;
		}
		String bla = Configuration.getInstance().getStringProperty("rules." + this.name, this.getDefault());
		this.setValue(bla);
	}

	/**
	 * Sets the default value for this rule. Unimplemented for option type rules.
	 * @param value the default value
	 */
	public void setDefault(String value) {
		switch (this.type) {
		case Rule.TEXT_RULE_TYPE:
			this.defaultValue = value;
			break;
		case Rule.OPTION_RULE_TYPE:
			// this.defaultOption = Integer.parseInt(value);
			break;
		case Rule.YESNO_RULE_TYPE:
			this.defaultOption = (value.equals("yes") ? 1 : 0);
			break;
		}
	}

	/** Sets the currend value of this rule
	 * @param value
	 */
	public void setValue(String value) {
		switch (this.type) {
		case Rule.TEXT_RULE_TYPE:
			this.value = value;
			break;
		case Rule.OPTION_RULE_TYPE:
		case Rule.YESNO_RULE_TYPE:
			this.option = value;
			break;
		}
	}

	/** Adds an option to the array of possible choices for values. Only works for option type rules.
	 * @param value the name of the option
	 * @param isDefault whether it is the default option or not
	 * @return true if this rule is an option type rule, false otherwise
	 */
	public boolean addOption(String value, boolean isDefault) {
		if (this.type == Rule.OPTION_RULE_TYPE) {
			this.options[optionCount++] = value;
			if (isDefault) {
				this.defaultOption = optionCount - 1;
			}
			return true;
		}
		return false;
	}

	/**
	 * To be implemented.
	 */
	public void addDependency() {

	}

	/** To be implemented
	 * @return
	 */
	public String getDescription() {
		return "TBD";
	}

	/** Returns the name of this rule.
	 * @return
	 */
	public String getName() {
		return name;
	}

	/** Returns the options for this rule or null if this rule is a text type rule
	 * @return
	 */
	public String[] getOptions() {
		return options;
	}

	/** Returns an integer representing the type of this rule. Possible values on {@link Rule}
	 * @return
	 */
	public int getType() {
		return type;
	}

	/** To be implemented
	 * @return
	 */
	public Rule[] getDependencies() {
		return dependencies;
	}

	/** Returns the current value of this rule. Returns empty string on error.
	 * @return
	 */
	public String getValue() {
		switch (this.type) {
		case Rule.TEXT_RULE_TYPE:
			return value;
		case Rule.OPTION_RULE_TYPE:
		case Rule.YESNO_RULE_TYPE:
			return option;
		}
		return "";
	}

	/** Returns the default value of this rule. Returns empty string on error.
	 * @return
	 */
	public String getDefault() {
		switch (this.type) {
		case Rule.TEXT_RULE_TYPE:
			return this.defaultValue;
		case Rule.OPTION_RULE_TYPE:
			return this.options[this.defaultOption];
		case Rule.YESNO_RULE_TYPE:
			return (this.defaultOption == 1 ? "yes" : "no");
		}
		return "";
	}

	/**
	 * Saves this rules value to the {@ling Configuration}.
	 */
	public void save() {
		Configuration.getInstance().setProperty("rules." + this.name, this.getValue());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name + " = " + this.getValue();
	}
}
