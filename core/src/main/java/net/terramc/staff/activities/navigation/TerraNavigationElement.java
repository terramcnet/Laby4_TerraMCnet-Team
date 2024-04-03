package net.terramc.staff.activities.navigation;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.navigation.elements.ScreenNavigationElement;
import net.labymod.api.client.resources.ResourceLocation;
import net.terramc.staff.TerraStaffAddon;

public class TerraNavigationElement extends ScreenNavigationElement {

  private TerraStaffAddon addon;

  public TerraNavigationElement(TerraStaffAddon addon) {
    super(addon.terraMainActivity);
    this.addon = addon;
  }

  @Override
  public String getWidgetId() {
    return "terramc_staff_ui";
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("terramc_staff.ui.navigation.main_ui");
  }

  @Override
  public Icon getIcon() {
    return Icon.texture(ResourceLocation.create("terramc", "textures/icon.png"));
  }

  @Override
  public boolean isVisible() {
    return this.addon.rankUtil().isStaff() || this.addon.isConnected();
  }

}
