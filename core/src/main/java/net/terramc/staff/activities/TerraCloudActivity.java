package net.terramc.staff.activities;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.action.ListSession;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.TilesGridWidget;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.util.I18n;
import net.terramc.staff.TerraStaffAddon;
import net.terramc.staff.activities.widget.CloudControlWidget;
import net.terramc.staff.util.Util;
import java.util.UUID;

@AutoActivity
@Link("cloud.lss")
public class TerraCloudActivity extends Activity {

  private TerraStaffAddon addon;

  public TerraCloudActivity(TerraStaffAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    if(!this.addon.isConnected()) {
      ComponentWidget noInfoWidget = ComponentWidget.i18n("terramc.ui.general.noInfoAvailable");
      noInfoWidget.addId("not-connected-info");
      this.document.addChild(noInfoWidget);

      ComponentWidget connectTextWidget = ComponentWidget.i18n("terramc.ui.general.connectToServer");
      connectTextWidget.addId("connect-text");
      this.document.addChild(connectTextWidget);

      ButtonWidget connect = ButtonWidget.text(I18n.translate("terramc.ui.general.connect"),
          Icon.texture(ResourceLocation.create("terramc", "textures/ui/update.png")));
      connect.setActionListener(() -> this.labyAPI.serverController().joinServer("terramc.net"));
      connect.addId("connect-btn");

      this.document.addChild(connect);
      return;
    }

    if(!this.addon.rankUtil().canControlCloud()) {
      ComponentWidget noAccessWidget = ComponentWidget.i18n("terramc_staff.ui.general.noAccess");
      noAccessWidget.addId("no-access");
      this.document.addChild(noAccessWidget);
      return;
    }

    UUID uuid = Laby.references().gameUserService().clientGameUser().getUniqueId();

    DivWidget container = new DivWidget().addId("container");

    DivWidget header = new DivWidget().addId("header");
    Component headerTextComponent = Component.translatable("terramc_staff.ui.cloud.title");
    header.addChild(ComponentWidget.component(headerTextComponent).addId("header-text"));
    container.addChild(header);

    TilesGridWidget<CloudControlWidget> gridWidget = new TilesGridWidget<>();
    gridWidget.addId("grid-control");

    // General

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "Proxy", "§aProxy", false));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "DevLobby", "§bDevLobby", false));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "Lobby", "§aLobby", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "PremiumLobby", "§6PremiumLobby", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "SilentLobby", "§4SilentLobby", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "TerraUnity", "§aTerraUnity", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "TheLab", "§bTheLab", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "WaterFFA", "§9WaterFFA", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "KBFFA", "§6KnockBackFFA", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "ST", "§bSoupTrainer", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "FFA", "§cFFA", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "BuildFFA", "§eBuildFFA", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "CB", "§aCityBuild", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "GunGame", "§6GunGame", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "XP", "§eXP", true));

    // BedWars

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "BW-4x1", "§cBedWars-4x1", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "BW-4x2", "§cBedWars-4x2", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "BW-4x4", "§cBedWars-4x4", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "BW-8x1", "§cBedWars-8x1", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "BW-2x2", "§cBedWars-2x2", true));

    // TeamDeathMatch

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "TDM-2x1", "§4TeamDeathMatch-2x1", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "TDM-2x2", "§4TeamDeathMatch-2x2", true));

    gridWidget.addTile(new CloudControlWidget(this.addon, uuid, "TDM-2x4", "§4TeamDeathMatch-2x4", true));

    ScrollWidget scrollWidget = new ScrollWidget(gridWidget, new ListSession<>());

    container.addChild(scrollWidget);
    this.document.addChild(container);
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks) {
    super.render(stack, mouse, partialTicks);
    Util.drawCredits(this.addon, this.labyAPI, this.bounds(), stack);
  }
}
