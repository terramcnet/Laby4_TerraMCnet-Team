package net.terramc.staff.activities.navigation;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.types.TabbedActivity;
import net.labymod.api.client.gui.screen.widget.widgets.navigation.tab.DefaultComponentTab;
import net.terramc.staff.TerraStaffAddon;
import net.terramc.staff.activities.TerraCloudActivity;
import net.terramc.staff.activities.TerraReportsActivity;
import net.terramc.staff.activities.TerraServerActivity;

@AutoActivity
public class TerraMainActivity extends TabbedActivity {

  private TerraStaffAddon addon;

  public TerraMainActivity(TerraStaffAddon addon) {
    this.addon = addon;
  }

  public void updateStaffActivity() {

    String serverInfoId = "terra_staff_server";
    String reportsId = "terra_staff_reports";
    String cloudId = "terra_staff_cloud";

    if(this.addon.rankUtil().isAdmin()) {
      if(this.getElementById(serverInfoId) == null) {
        this.register(serverInfoId, new DefaultComponentTab(Component.translatable("terramc_staff.ui.activity.serverInfo"), new TerraServerActivity(this.addon)));
      }
    } else {
      this.unregister(serverInfoId);
    }

    if(this.addon.rankUtil().isStaff()) {
      if(this.getElementById(reportsId) == null) {
        this.register(reportsId, new DefaultComponentTab(Component.translatable("terramc_staff.ui.activity.reports"), new TerraReportsActivity(this.addon)));
      }
    } else {
      this.unregister(reportsId);
    }

    if(this.addon.rankUtil().canControlCloud()) {
      if(this.getElementById(cloudId) == null) {
        this.register(cloudId, new DefaultComponentTab(Component.translatable("terramc_staff.ui.activity.cloud"), new TerraCloudActivity(this.addon)));
      }
    } else {
      this.unregister(cloudId);
    }

    this.addon.labyAPI().minecraft().executeOnRenderThread(this::reload);
  }

}
