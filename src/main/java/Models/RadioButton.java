package Models;

import java.util.HashMap;
import java.util.Map;

public class RadioButton {

    Map<String, String> Radio = new HashMap<String, String>();

    public RadioButton() {
        Radio.put("3rd party etl form", "third_party_etl_form");
        Radio.put("eligibility_form", "eligibility_form");
        Radio.put("Digital Update", "digital_update");
        Radio.put("banner computation", "banner_computation");
        Radio.put("outbound sso testing", "outbound_sso_testing");
        Radio.put("outbound_sso_info", "outbound_sso_info");
        Radio.put("Reports", "reports");
        Radio.put("add new program", "program");
        Radio.put("add banner rules", "banner_rules");
        Radio.put("add client eligible programs", "client_eligible_programs");
        Radio.put("add content for campaigns", "campaign_content_campaigns");
        Radio.put("add vendor info", "vendor_info");
        Radio.put("health_tips", "health_tips");
        Radio.put(" Gameday Client Config", "gameday_client_config");
        Radio.put("campaign_content - Current Status", "campaign_content_current_status");
        Radio.put("campaign_content - Health Quest", "campaign_content_health_quest");
        Radio.put("campaign_content - Plan choice", "campaign_content_plan_choice");
        Radio.put("campaign_content - Plan choice nearby", "plan_optional_content");
        Radio.put("campaign_content - Plan option content", "campaign_content_generic");
        Radio.put("campaign_content- generic", "gameday_client_config");

    }
    public String getRadio(String s) {
        return Radio.get(s);
    }


}