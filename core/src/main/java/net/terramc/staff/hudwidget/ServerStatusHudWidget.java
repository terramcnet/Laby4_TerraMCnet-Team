package net.terramc.staff.hudwidget;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.terramc.staff.TerraStaffAddon;
import net.terramc.staff.data.ServerData;
import net.terramc.staff.util.TerraStaffTextures.Hud;

public class ServerStatusHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TerraStaffAddon addon;

  private TextLine tpsLine;
  private TextLine cpuLine;
  private TextLine ramLine;

  public ServerStatusHudWidget(TerraStaffAddon addon) {
    super("terramc_server_status");
    this.addon = addon;
    this.bindCategory(this.addon.widgetCategory);
    this.setIcon(Hud.SERVER_STATUS);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.tpsLine = createLine(Component.translatable("terramc_staff.hudWidget.terramc_server_status.tps"), "0.0");
    this.cpuLine = createLine(Component.translatable("terramc_staff.hudWidget.terramc_server_status.cpu"), "0.0");
    this.ramLine = createLine(Component.translatable("terramc_staff.hudWidget.terramc_server_status.ram"), "0.0");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    super.onTick(isEditorContext);

    this.tpsLine.updateAndFlush(Component.text(ServerData.getServerTps()));
    this.tpsLine.setState(this.addon.isConnected() && this.addon.rankUtil().isAdmin() ? State.VISIBLE : State.HIDDEN);

    this.cpuLine.updateAndFlush(Component.text(ServerData.getCpuUsage()));
    this.cpuLine.setState(this.addon.isConnected() && this.addon.rankUtil().isAdmin() ? State.VISIBLE : State.HIDDEN);

    this.ramLine.updateAndFlush(Component.text(ServerData.getRamUsage()));
    this.ramLine.setState(this.addon.isConnected() && this.addon.rankUtil().isAdmin() ? State.VISIBLE : State.HIDDEN);
  }

}
