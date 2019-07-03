package Models;

import java.util.HashMap;
import java.util.Map;

public class LoaderCategory {

    public Map<String, String> Radio = new HashMap<String, String>();

    public LoaderCategory() {
        Radio.put("3rd party etl form", "ETL's");
        Radio.put("eligibility_form", "ETL's");
        Radio.put("Digital Update", "Incentive Management");
        Radio.put("banner computation", "banner_computation");       //not using this
        Radio.put("outbound sso testing", "outbound_sso_testing");   //not using this
        Radio.put("outbound_sso_info", "Configurations");
        Radio.put("Reports", "Master Scheduler");
        Radio.put("Step 1: add new program", "Configure new banner");
        Radio.put("Step 2: add banner rules", "Configure new banner");
        Radio.put("Step 3: add client eligible programs", "Configure new banner");
        Radio.put("Step 4: add content for campaigns", "Configure new banner");
        Radio.put("Step 5: add vendor info", "Configure new banner");
        Radio.put("health_tips", "SMS text");
        Radio.put("Gameday Client Config", "Evive portal");
        Radio.put("campaign_content - Current Status", "Evive portal");
        Radio.put("campaign_content - Health Quest", "Evive portal");
        Radio.put("campaign_content - Plan choice", "Evive portal");
        Radio.put("campaign_content - Plan choice nearby", "Evive portal");
        Radio.put("campaign_content - Plan option content", "Evive portal");
        Radio.put("campaign_content- generic", "campaign_content_generic");
        Radio.put("Blacklist Data", "Best Match");
        Radio.put("Details Page Content", "Best Match");
        Radio.put("Dropdown Custom Tags Content", "Best Match");
        Radio.put("Help Center Content", "Best Match");
        Radio.put("Landing page", "Best Match");
        Radio.put("Onboarding page", "Best Match");
        Radio.put("Provider Card Content", "Best Match");
        Radio.put("Results page", "Best Match");
        Radio.put("Search Tabs Content", "Best Match");
        Radio.put("Sort Types", "Best Match");
        Radio.put("Step 1: add new client", "Configure the new client");
        Radio.put("Step 2: add roles for client", "Configure the new client");
        Radio.put("Step 3: add configurations for the client", "Configure the new client");
        Radio.put("Step 4: add products for particular roles", "Configure the new client");
        Radio.put("Step 5: add vendor data", "Configure the new client");
        Radio.put("Step 6: add new card", "Configure the new client");
        Radio.put("etl_starter", "File processing automation");
        Radio.put("report details", "File processing automation");
        Radio.put("hhr phenomenon detail", "Health hidden risk");
        Radio.put("onsite event location", "Health hidden risk");
        Radio.put("quest psc", "Health hidden risk");
        Radio.put("zip psc vicinity", "Health hidden risk");
        Radio.put("Game Center Config", "Gameday");
        Radio.put("client tags", "Quiz new dashboard");
        Radio.put("question", "Quiz new dashboard");
        Radio.put("quiz", "Quiz new dashboard");


    }
    public String getCategory(String s) {
        return Radio.get(s);
    }


}