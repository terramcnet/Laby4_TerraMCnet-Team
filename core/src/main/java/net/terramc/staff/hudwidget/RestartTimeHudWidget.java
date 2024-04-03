package net.terramc.staff.hudwidget;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.terramc.staff.TerraStaffAddon;
import net.terramc.staff.data.ServerData;
import net.terramc.staff.util.TerraStaffTextures.Hud;

public class RestartTimeHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TerraStaffAddon addon;

  private TextLine textLine;

  public RestartTimeHudWidget(TerraStaffAddon addon) {
    super("terramc_restart_time");
    this.addon = addon;
    this.bindCategory(this.addon.widgetCategory);
    this.setIcon(Hud.RESTART_TIME);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.textLine = createLine(Component.translatable("terramc_staff.hudWidget.terramc_restart_time.name"), "0h:0m:00s");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    super.onTick(isEditorContext);

    this.textLine.updateAndFlush(Component.text(ServerData.getRestartTime()));
    this.textLine.setState(this.addon.isConnected() && this.addon.rankUtil().isAdmin() ? State.VISIBLE : State.HIDDEN);
  }

}
