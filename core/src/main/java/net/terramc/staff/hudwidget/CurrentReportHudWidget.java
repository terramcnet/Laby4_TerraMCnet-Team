package net.terramc.staff.hudwidget;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.terramc.staff.TerraStaffAddon;
import net.terramc.staff.data.AddonData;
import net.terramc.staff.data.ReportData;
import net.terramc.staff.util.TerraStaffTextures.Hud;

public class CurrentReportHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TerraStaffAddon addon;

  private TextLine reportedLine;
  private TextLine reporterLine;
  private TextLine reasonLine;
  private TextLine priorityLine;
  private TextLine idLine;
  private TextLine finishLine;

  public CurrentReportHudWidget(TerraStaffAddon addon) {
    super("terramc_current_report");
    this.addon = addon;
    this.bindCategory(this.addon.widgetCategory);
    this.setIcon(Hud.SERVER_STATUS);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    this.reportedLine = createLine(Component.translatable("terramc_staff.hudWidget.terramc_current_report.reported"), "N/A");
    this.reporterLine = createLine(Component.translatable("terramc_staff.hudWidget.terramc_current_report.reporter"), "N/A");
    this.reasonLine = createLine(Component.translatable("terramc_staff.hudWidget.terramc_current_report.reason"), "N/A");
    this.priorityLine = createLine(Component.translatable("terramc_staff.hudWidget.terramc_current_report.priority"), "0");
    this.idLine = createLine(Component.translatable("terramc_staff.hudWidget.terramc_current_report.id"), "N/A");
    this.finishLine = createLine(Component.translatable("terramc_staff.hudWidget.terramc_current_report.finish"), "§c/rpf");
  }

  @Override
  public void onTick(boolean isEditorContext) {
    super.onTick(isEditorContext);

    ReportData reportData = AddonData.getCurrentReport();

    this.reportedLine.updateAndFlush(Component.text(reportData != null ? reportData.reported() : ""));
    this.reportedLine.setState(this.addon.isConnected() && this.addon.rankUtil().isStaff() && reportData != null ? State.VISIBLE : State.HIDDEN);

    this.reporterLine.updateAndFlush(Component.text(reportData != null ? reportData.reporter() : ""));
    this.reporterLine.setState(this.addon.isConnected() && this.addon.rankUtil().isStaff() && reportData != null ? State.VISIBLE : State.HIDDEN);

    this.reasonLine.updateAndFlush(Component.text(reportData != null ? reportData.reason() : ""));
    this.reasonLine.setState(this.addon.isConnected() && this.addon.rankUtil().isStaff() && reportData != null ? State.VISIBLE : State.HIDDEN);

    this.priorityLine.updateAndFlush(Component.text(reportData != null ? reportData.priority() : ""));
    this.priorityLine.setState(this.addon.isConnected() && this.addon.rankUtil().isStaff() && reportData != null ? State.VISIBLE : State.HIDDEN);

    this.idLine.updateAndFlush(Component.text(reportData != null ? reportData.id() : ""));
    this.idLine.setState(this.addon.isConnected() && this.addon.rankUtil().isStaff() && reportData != null ? State.VISIBLE : State.HIDDEN);

    this.finishLine.updateAndFlush(Component.text("§c/rpf"));
    this.finishLine.setState(this.addon.isConnected() && this.addon.rankUtil().isStaff() && reportData != null ? State.VISIBLE : State.HIDDEN);

  }

}

