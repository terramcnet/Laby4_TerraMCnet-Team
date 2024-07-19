package net.terramc.staff.activities.widget;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.terramc.staff.TerraStaffAddon;
import net.terramc.staff.data.ReportData;

public class ReportWidget extends SimpleWidget {

  private final TerraStaffAddon addon;
  private final ReportData reportData;

  public ReportWidget(TerraStaffAddon addon, ReportData reportData) {
    this.addon = addon;
    this.reportData = reportData;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    addId("report");
    if(reportData.accepted()) {
      addId("accepted");
    }

    Component idComponent = Component.text("ID", NamedTextColor.GRAY)
        .append(Component.text(": ", NamedTextColor.DARK_GRAY))
        .append(Component.text("#" + reportData.id(), NamedTextColor.RED));
    addChild(ComponentWidget.component(idComponent).addId("id"));

    addChild(new IconWidget(Icon.head(reportData.reportedUUID())).addId("reported-head"));
    addChild(new IconWidget(Icon.head(reportData.reporterUUID())).addId("reporter-head"));

    Component reportedComponent = Component.text("» ", NamedTextColor.DARK_GRAY)
        .append(Component.translatable("terramc_staff.ui.reports.reported", NamedTextColor.GRAY))
        .append(Component.text(": ", NamedTextColor.DARK_GRAY))
        .append(Component.text(reportData.reported(), NamedTextColor.RED));
    addChild(ComponentWidget.component(reportedComponent).addId("reported"));

    Component reporterComponent = Component.text("» ", NamedTextColor.DARK_GRAY)
        .append(Component.translatable("terramc_staff.ui.reports.reporter", NamedTextColor.GRAY))
        .append(Component.text(": ", NamedTextColor.DARK_GRAY))
        .append(Component.text(reportData.reporter(), NamedTextColor.RED));
    addChild(ComponentWidget.component(reporterComponent).addId("reporter"));

    Component reasonComponent = Component.text("» ", NamedTextColor.DARK_GRAY)
        .append(Component.translatable("terramc_staff.ui.reports.reason", NamedTextColor.GRAY))
        .append(Component.text(": ", NamedTextColor.DARK_GRAY))
        .append(Component.text(reportData.reason(), NamedTextColor.RED));
    addChild(ComponentWidget.component(reasonComponent).addId("reason"));

    Component priorityComponent = Component.text("» ", NamedTextColor.DARK_GRAY)
        .append(Component.translatable("terramc_staff.ui.reports.priority", NamedTextColor.GRAY))
        .append(Component.text(": ", NamedTextColor.DARK_GRAY))
        .append(Component.text(reportData.priority(), NamedTextColor.RED));
    addChild(ComponentWidget.component(priorityComponent).addId("priority"));


    if(this.addon.rankUtil().isStaff() && !reportData.accepted()) {
      ButtonWidget acceptButton = ButtonWidget.component(Component.translatable("terramc_staff.ui.reports.button.accept", NamedTextColor.GREEN)).addId("accept-button");
      acceptButton.setPressable(() -> {
        this.addon.labyAPI().minecraft().chatExecutor().chat("/reportjoin " + reportData.id());
      });
      addChild(acceptButton);
    }
    if(this.addon.rankUtil().isAdmin() && !reportData.accepted()) {
      ButtonWidget deleteButton = ButtonWidget.component(Component.translatable("terramc_staff.ui.reports.button.delete", NamedTextColor.RED)).addId("delete-button");
      deleteButton.setPressable(() -> {
        this.addon.labyAPI().minecraft().chatExecutor().chat("/reports del " + reportData.id());
      });
      addChild(deleteButton);
    }
    if(reportData.accepted()) {
      addChild(ComponentWidget.i18n("terramc_staff.ui.reports.accepted").addId("accepted-text"));
    }

  }

}
