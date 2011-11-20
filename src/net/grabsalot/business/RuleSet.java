package net.grabsalot.business;

import java.util.HashMap;

import net.grabsalot.business.Rule;
import net.grabsalot.business.RuleSet;

/** The abstract class that holds and manager rules.
 * @author madboyka
 *
 */
public abstract class RuleSet {

	public static final String dataSourceRule = "DataSourceRule";
	public static final String saveServiceToLocalRule = "SaveServiceToLocalRule";
	public static final String preloadLevelRule = "PreloadLevelRule";
	public static final String localInfoRule = "LocalInfoRule";
	public static final String iconDetailLevelRule = "IconDetailLevelRule";
	public static final String coverArtNameRule = "CoverArtNameRule";
	public static final String coverArtExtensionRule = "CoverArtExtensionRule";
	public static final String autoSaveCoverArtRule = "AutoSaveCoverArtRule";
	public static final String autoOverwriteCoverArtRule = "AutoOverWriteCoverArtRule";
	public static final String artistPictureNameRule = "ArtistPictureNameRule";
	public static final String artistPictureExtensionRule = "ArtistPictureExtensionRule";
	public static final String trustTagsOverPaths = "TrustTagsOverPaths";
	private static Rule[] rules;
	private static int ruleCount = 12;
	private static HashMap<String, Object> ruleMap;

	/** Returns an array of all currently applied rules.
	 * @return and array of {@link Rule} objects
	 */
	public static Rule[] getRules() {
		if (rules == null) {
			ruleMap = new HashMap<String, Object>();
			rules = new Rule[RuleSet.ruleCount];
			int index = -1;
			rules[++index] = new Rule(RuleSet.dataSourceRule, Rule.OPTION_RULE_TYPE);
			rules[index].addOption("local over service", true);
			rules[index].addOption("service over local", false);
			
			rules[++index] = new Rule(RuleSet.saveServiceToLocalRule, Rule.YESNO_RULE_TYPE);
			
			rules[++index] = new Rule(RuleSet.preloadLevelRule, Rule.OPTION_RULE_TYPE);
			rules[index].addOption("artist", true);
			rules[index].addOption("album", false);
			rules[index].addOption("track", false);
			
			rules[++index] = new Rule(RuleSet.localInfoRule, Rule.OPTION_RULE_TYPE);
			rules[index].addOption("files over xml", false);
			rules[index].addOption("xml over files", true);
			
			rules[++index] = new Rule(RuleSet.iconDetailLevelRule, Rule.OPTION_RULE_TYPE);
			rules[index].addOption(Rule.ICON_DETAIL_SIMPLE, false);
			rules[index].addOption(Rule.ICON_DETAIL_NICE, true);
			rules[index].addOption(Rule.ICON_DETAIL_EXTENDED, false);
			rules[index].addOption(Rule.ICON_DETAIL_EXTREME, false);
			
			rules[++index] = new Rule(RuleSet.coverArtNameRule, Rule.TEXT_RULE_TYPE);
			rules[index].setDefault("cover");
			
			rules[++index] = new Rule(RuleSet.coverArtExtensionRule, Rule.TEXT_RULE_TYPE);
			rules[index].setDefault("png");
			
			rules[++index] = new Rule(RuleSet.autoSaveCoverArtRule, Rule.YESNO_RULE_TYPE);
			
			rules[++index] = new Rule(RuleSet.artistPictureNameRule, Rule.TEXT_RULE_TYPE);
			rules[index].setDefault("artist");
			
			rules[++index] = new Rule(RuleSet.artistPictureExtensionRule, Rule.TEXT_RULE_TYPE);
			rules[index].setDefault("png");
			
			rules[++index] = new Rule(RuleSet.autoOverwriteCoverArtRule, Rule.YESNO_RULE_TYPE);
			
			rules[++index] = new Rule(RuleSet.trustTagsOverPaths,Rule.YESNO_RULE_TYPE);
			
			for (int i = 0; i < RuleSet.ruleCount; ++i) {
				rules[i].load();
				ruleMap.put(rules[i].getName(), rules[i]);
			}
		}
		return rules;
	}
	
	/** Reloads all rules. Useful to call when rules have been changed.
	 * @return an array of the reloaded Rule objects
	 */
	public static Rule[] resetRules() {
		RuleSet.rules = null;
		RuleSet.ruleMap = null;
		return RuleSet.getRules();
	}

	/** Returns a single rule determined by name. May return null if the rule was not found.
	 * @param name the name of the Rule to load. Possible values on {@link RuleSet}.
	 * @return the rule matching name or null.
	 */
	public static Rule getRule(String name) {
		if (rules == null) {
			RuleSet.getRules();
		}
		return (Rule) ruleMap.get(name);
	}
}
