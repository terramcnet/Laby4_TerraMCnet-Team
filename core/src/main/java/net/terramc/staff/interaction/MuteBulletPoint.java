package net.terramc.staff.interaction;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;
import net.terramc.staff.TerraStaffAddon;
import net.terramc.staff.activities.popup.BanUserActivity;
import net.terramc.staff.activities.popup.MuteUserActivity;

public class MuteBulletPoint implements BulletPoint {

  private TerraStaffAddon addon;

  public MuteBulletPoint(TerraStaffAddon addon) {
    this.addon = addon;
  }

  @Override
  public Component getTitle() {
    return Component.translatable("terramc_staff.interaction.mute");
  }

  @Override
  public Icon getIcon() {
    return null;
  }

  @Override
  public void execute(Player player) {
    Laby.labyAPI().minecraft().executeNextTick(() ->
      Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new MuteUserActivity(this.addon, player.getUniqueId(), player.getName(), null))
    );
  }

  @Override
  public boolean isVisible(Player playerInfo) {
    return this.addon.rankUtil().isStaff();
  }

}
