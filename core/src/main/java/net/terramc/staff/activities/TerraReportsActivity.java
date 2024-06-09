package net.terramc.staff.activities;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.TilesGridWidget;
import net.labymod.api.client.render.matrix.Stack;
import net.terramc.staff.TerraStaffAddon;
import net.terramc.staff.activities.widget.ReportWidget;
import net.terramc.staff.data.AddonData;
import net.terramc.staff.data.ReportData;
import net.terramc.staff.util.Util;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Link("reports.lss")
@AutoActivity
public class TerraReportsActivity extends Activity {

  private TerraStaffAddon addon;

  private Sorting sorting = Sorting.DEFAULT;

  public TerraReportsActivity(TerraStaffAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    if(!this.addon.rankUtil().isStaff()) {
      ComponentWidget noAccessWidget = ComponentWidget.i18n("terramc_staff.ui.general.noAccess");
      noAccessWidget.addId("no-access");
      this.document.addChild(noAccessWidget);
      return;
    }

    List<ReportData> reports = new ArrayList<>(AddonData.reports());
    if(this.sorting == Sorting.PRIORITY) {
      reports.sort(Comparator.comparing(ReportData::priority));
    }

    DivWidget container = new DivWidget().addId("container");

    DivWidget header = new DivWidget().addId("header");
    Component headerTextComponent = Component.translatable("terramc_staff.ui.reports.title");
    header.addChild(ComponentWidget.component(headerTextComponent).addId("header-text"));
    container.addChild(header);

    TilesGridWidget<ReportWidget> reportsGrid = new TilesGridWidget<>().addId("reports-grid");
    reports.forEach(report -> reportsGrid.addTile(new ReportWidget(this.addon, report)));

    container.addChild(reportsGrid);

    this.document.addChild(container);

  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float tickDelta) {
    super.render(stack, mouse, tickDelta);
    Util.drawCredits(this.addon, this.labyAPI, this.bounds(), stack);
  }

  public enum Sorting {
    DEFAULT, PRIORITY
  }

}
