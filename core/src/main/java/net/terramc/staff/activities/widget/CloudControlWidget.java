package net.terramc.staff.activities.widget;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.terramc.staff.TerraStaffAddon;
import java.util.UUID;

public class CloudControlWidget extends SimpleWidget {

  private TerraStaffAddon addon;
  private UUID uuid;
  private String groupName;
  private String groupDisplay;
  private boolean maintenance;

  public CloudControlWidget(TerraStaffAddon addon, UUID uuid, String groupName, String groupDisplay, boolean maintenance) {
    this.addon = addon;
    this.uuid = uuid;
    this.groupName = groupName;
    this.groupDisplay = groupDisplay;
    this.maintenance = maintenance;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    DivWidget container = new DivWidget();
    container.addId("control-container");

    ComponentWidget title = ComponentWidget.text(this.groupDisplay);
    title.addId("title");

    container.addChild(title);

    ButtonWidget restartButton = ButtonWidget.component(Component.translatable("terramc_staff.ui.cloud.button.restart", NamedTextColor.RED));
    restartButton.addId("restartBtn");
    restartButton.setActionListener(() -> this.addon.apiUtil().sendCloudControl(this.uuid, "restart", this.groupName));
    container.addChild(restartButton);

    if(this.maintenance) {
      ButtonWidget maintenanceButton = ButtonWidget.component(Component.translatable("terramc_staff.ui.cloud.button.maintenance", NamedTextColor.GOLD));
      maintenanceButton.addId("maintenanceBtn");
      maintenanceButton.setActionListener(() -> this.addon.apiUtil().sendCloudControl(this.uuid, "maintenance", this.groupName));
      container.addChild(maintenanceButton);
    }

    addChild(container);

  }

}