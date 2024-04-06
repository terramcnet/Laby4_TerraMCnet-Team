package net.terramc.staff.activities.popup;

import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.terramc.staff.TerraStaffAddon;

@Link("popup/mute.lss")
@AutoActivity
public class MuteUserActivity extends SimpleActivity {

  private TerraStaffAddon addon;
  private UUID playerUUID;
  private String playerName;

  private ScreenInstance previousScreen;

  private MuteReason banReason = MuteReason.WORTWAHL;

  public MuteUserActivity(TerraStaffAddon addon, UUID playerUUID, String playerName, ScreenInstance previousScreen) {
    this.addon = addon;
    this.playerUUID = playerUUID;
    this.playerName = playerName;
    this.previousScreen = previousScreen;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    FlexibleContentWidget container = new FlexibleContentWidget().addId("container");
    HorizontalListWidget header = new HorizontalListWidget().addId("header");

    IconWidget headWidget = new IconWidget(Icon.head(this.playerUUID)).addId("head");
    ComponentWidget titleWidget = ComponentWidget.i18n("terramc_staff.popup.mute.title", this.playerName).addId("title");
    header.addEntry(headWidget);
    header.addEntry(titleWidget);

    ComponentWidget reasonTitleWidget = ComponentWidget.i18n("terramc_staff.popup.mute.reason.title").addId("reason-title");
    DropdownWidget<MuteReason> reasonDropdownWidget = DropdownWidget.create(MuteReason.WORTWAHL, reason -> {
      this.banReason = reason;
    }).addId("reason-input");
    reasonDropdownWidget.setTranslationKeyPrefix("terramc_staff.popup.mute.reason.type");
    reasonDropdownWidget.addAll(MuteReason.values());

    ButtonWidget sendButton = ButtonWidget.i18n("terramc_staff.popup.button.send").addId("send-button");
    sendButton.setPressable(() -> {
      if(this.sendForm()) {
        Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.previousScreen);
        this.addon.pushNotification(
            Component.translatable("terramc_staff.popup.success.title", NamedTextColor.DARK_GREEN),
            Component.translatable("terramc_staff.popup.success.mute", NamedTextColor.GRAY, Component.text(this.playerName, NamedTextColor.YELLOW))
        );
      }
    });

    ButtonWidget closeButton = ButtonWidget.i18n("terramc_staff.popup.button.abort").addId("close-button");
    closeButton.setPressable(() -> {
      Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.previousScreen);
    });

    FlexibleContentWidget content = new FlexibleContentWidget().addId("content");

    FlexibleContentWidget reasonContainer = new FlexibleContentWidget().addId("reason-container");
    reasonContainer.addContent(reasonTitleWidget);
    reasonContainer.addContent(reasonDropdownWidget);
    content.addContent(reasonContainer);

    FlexibleContentWidget buttonContainer = new FlexibleContentWidget().addId("button-container");
    buttonContainer.addContent(sendButton);
    buttonContainer.addContent(closeButton);
    content.addContent(buttonContainer);

    container.addContent(header);
    container.addContent(content);

    this.document.addChild(container);
  }

  private boolean sendForm() {
    this.addon.labyAPI().minecraft().chatExecutor().chat("/mute " + this.playerName + " " + this.banReason.id(), false);
    return true;
  }

  public enum MuteReason {

    WORTWAHL(1),
    WERBUNG(2),
    VERHALTEN(3),
    SPAMMING(4),
    PROVOKATION(5),
    CAPSLOCK(6),
    OTHER(7);

    private final int id;

    MuteReason(int id) {
      this.id = id;
    }

    public int id() {
      return id;
    }

  }

}
