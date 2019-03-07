package Models;

import java.util.HashMap;
import java.util.Map;

public class RadioButton {

    public Map<String, String> Radio = new HashMap<String, String>();

    public RadioButton() {
        Radio.put("3rd party etl form", "third_party_etl_form");
        Radio.put("eligibility_form", "eligibility_form");
        Radio.put("Digital Update", "digital_update");
        Radio.put("banner computation", "banner_computation");
        Radio.put("outbound sso testing", "outbound_sso_testing");
        Radio.put("outbound_sso_info", "outbound_sso_info");
        Radio.put("Reports", "reports");
        Radio.put("Step 1: add new program", "program");
        Radio.put("Step 2: add banner rules", "banner_rules");
        Radio.put("Step 3: add client eligible programs", "client_eligible_programs");
        Radio.put("Step 4: add content for campaigns", "campaign_content_campaigns");
        Radio.put("Step 5: add vendor info", "vendor_info");
        Radio.put("health_tips", "health_tips");
        Radio.put("Gameday Client Config", "gameday_client_config");
        Radio.put("campaign_content - Current Status", "campaign_content_current_status");
        Radio.put("campaign_content - Health Quest", "campaign_content_health_quest");
        Radio.put("campaign_content - Plan choice", "campaign_content_plan_choice");
        Radio.put("campaign_content - Plan choice nearby", "plan_optional_content");
        Radio.put("campaign_content - Plan option content", "campaign_content_generic");
        Radio.put("campaign_content- generic", "gameday_client_config");
        Radio.put("Blacklist Data", "bm_blacklisted_data");
        Radio.put("Details Page Content", "bm_details_page");
        Radio.put("Dropdown Custom Tags Content", "bm_dropdown_content");
        Radio.put("Help Center Content", "bm_help_center_content");
        Radio.put("Landing page", "bm_landing_page");
        Radio.put("Onboarding page", "bm_onboarding_page");
        Radio.put("Provider Card Content", "bm_provider_card_commons");
        Radio.put("Results page", "bm_results_page");
        Radio.put("Search Tabs Content", "bm_search_tabs");
        Radio.put("Sort Types", "bm_sort_types");
        Radio.put("Step 1: add new client", "client_profile");
        Radio.put("Step 2: add roles for client", "client_to_roles");
        Radio.put("Step 3: add configurations for the client", "client_config");
        Radio.put("Step 4: add products for particular roles", "role_product_map");
        Radio.put("Step 5: add vendor data", "vendor_data");
        Radio.put("Step 6: add new card", "card_content");
        Radio.put("etl_starter", "etl_starter");
        Radio.put("report details", "report_details");
        Radio.put("hhr phenomenon detail", "hhr_phenomenon_detail");
        Radio.put("onsite event location", "onsite_event_location");
        Radio.put("quest psc", "quest_psc");
        Radio.put("zip psc vicinity", "zip_psc_vicinity");
        Radio.put("Game Center Config", "game_center_config");
        Radio.put("client tags", "client_tag");
        Radio.put("question", "question");
        Radio.put("quiz", "quiz");


    }
    public String getRadio(String s) {
        return Radio.get(s);
    }


}