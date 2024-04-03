package net.terramc.staff.hudwidget;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.terramc.staff.TerraStaffAddon;
import net.terramc.staff.data.AddonData;
import net.terramc.staff.util.TerraStaffTextures.Hud;

public class VanishHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TerraStaffAddon addon;

  private TextLine vanishLine;
  private TextLine autoVanishLine;
  private TextLine adminVanish;

  public VanishHudWidget(TerraStaffAddon addon) {
    super("terramc_vanish");
    this.addon = addon;
    this.bindCategory(this.addon.widgetCategory);
    this.setIcon(Hud.VANISH);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.vanishLine = createLine(Component.translatable("terramc_staff.hudWidget.terramc_vanish.name"), "N/A");
    this.adminVanish = createLine(Component.translatable("terramc_staff.hudWidget.terramc_vanish.adminVanish"), "N/A");
    this.autoVanishLine = createLine(Component.translatable("terramc_staff.hudWidget.terramc_vanish.autoVanish"), "N/A");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    super.onTick(isEditorContext);

    this.vanishLine.updateAndFlush(Component.text(AddonData.isVanish() ? "§aAktiv" : "§cInaktiv"));
    this.vanishLine.setState(this.addon.isConnected() && this.addon.rankUtil().isStaff() ? State.VISIBLE : State.HIDDEN);

    this.adminVanish.updateAndFlush(Component.text(AddonData.isAdminVanish() ? "§aAktiv" : "§cInaktiv"));
    this.adminVanish.setState(this.addon.isConnected() && this.addon.rankUtil().isAdmin() ? State.VISIBLE : State.HIDDEN);

    this.autoVanishLine.updateAndFlush(Component.text(AddonData.isAutoVanish() ? "§aAktiv" : "§cInaktiv"));
    this.autoVanishLine.setState(this.addon.isConnected() && this.addon.rankUtil().isStaff() ? State.VISIBLE : State.HIDDEN);
  }

}
