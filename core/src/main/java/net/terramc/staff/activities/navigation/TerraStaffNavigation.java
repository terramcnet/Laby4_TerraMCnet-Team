package net.terramc.staff.activities.navigation;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.widgets.navigation.tab.DefaultComponentTab;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.activities.navigation.TerraMainActivity;
import net.terramc.staff.TerraStaffAddon;
import net.terramc.staff.activities.TerraCloudActivity;
import net.terramc.staff.activities.TerraReportsActivity;
import net.terramc.staff.activities.TerraServerActivity;

public class TerraStaffNavigation {

  public static void register(TerraStaffAddon staffAddon) {
    staffAddon.logger().info("Try invoke navigation into TerraMCnet addon");
    if(TerraAddon.instance() == null) {
      staffAddon.logger().info("Can't invoke navigation. Reason: TerraMCnet Addon instance is null.");
      return;
    }
    TerraAddon addon = TerraAddon.instance();
    TerraMainActivity activity = addon.terraMainActivity;
    if(activity == null) {
      staffAddon.logger().info("Can't invoke navigation. Reason: TerraMainActivity is null.");
      return;
    }

    String serverInfoId = "terra_server";
    String reportsId = "terra_staff_reports";
    String cloudId = "terra_cloud";

    Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
      if(addon.rankUtil().isAdmin()) {
        if(activity.getElementById(serverInfoId) == null) {
            activity.register(serverInfoId, new DefaultComponentTab(Component.translatable("terramc_staff.ui.activity.serverInfo"), new TerraServerActivity(staffAddon)));
            staffAddon.logger().info("Invoke ServerActivity into TerraMCnet Addon");
        }
      } else {
        activity.unregister(serverInfoId);
      }

      if(addon.rankUtil().isStaff()) {
        if(activity.getElementById(reportsId) == null) {
          activity.register(reportsId, new DefaultComponentTab(Component.translatable("terramc_staff.ui.activity.reports"), new TerraReportsActivity(staffAddon)));
        }
      } else {
        activity.unregister(reportsId);
      }

      if(addon.rankUtil().canControlCloud()) {
        if(activity.getElementById(cloudId) == null) {
          activity.register(cloudId, new DefaultComponentTab(Component.translatable("terramc_staff.ui.activity.cloud"), new TerraCloudActivity(staffAddon)));
          staffAddon.logger().info("Invoke CloudActivity into TerraMCnet Addon");
        }
      } else {
        activity.unregister(cloudId);
      }

        activity.reload();
      });
      staffAddon.logger().info("Invoked navigation into TerraMCnet addon");
  }

}
