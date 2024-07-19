package net.terramc.staff.activities.popup;

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
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.terramc.staff.TerraStaffAddon;
import java.util.UUID;

@Link("popup/ban.lss")
@AutoActivity
public class BanUserActivity extends SimpleActivity {

  private TerraStaffAddon addon;
  private UUID playerUUID;
  private String playerName;

  private ScreenInstance previousScreen;

  private BanReason banReason = BanReason.HACKING;

  private TextFieldWidget proofInputWidget;

  public BanUserActivity(TerraStaffAddon addon, UUID playerUUID, String playerName, ScreenInstance previousScreen) {
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
    ComponentWidget titleWidget = ComponentWidget.i18n("terramc_staff.popup.ban.title", this.playerName).addId("title");
    header.addEntry(headWidget);
    header.addEntry(titleWidget);

    ComponentWidget reasonTitleWidget = ComponentWidget.i18n("terramc_staff.popup.ban.reason.title").addId("reason-title");
    DropdownWidget<BanReason> reasonDropdownWidget = DropdownWidget.create(BanReason.HACKING, reason -> {
      this.banReason = reason;
    }).addId("reason-input");
    reasonDropdownWidget.setTranslationKeyPrefix("terramc_staff.popup.ban.reason.type");
    reasonDropdownWidget.addAll(BanReason.values());

    ComponentWidget messageTitleWidget = ComponentWidget.i18n("terramc_staff.popup.ban.proof").addId("proof-title");
    this.proofInputWidget = new TextFieldWidget().addId("proof-input");

    ButtonWidget sendButton = ButtonWidget.i18n("terramc_staff.popup.button.send").addId("send-button");
    sendButton.setPressable(() -> {
      if(this.sendForm()) {
        Laby.labyAPI().minecraft().minecraftWindow().displayScreen(this.previousScreen);
        this.addon.pushNotification(
            Component.translatable("terramc_staff.popup.success.title", NamedTextColor.DARK_GREEN),
            Component.translatable("terramc_staff.popup.success.ban", NamedTextColor.GRAY, Component.text(this.playerName, NamedTextColor.YELLOW))
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

    FlexibleContentWidget messageContainer = new FlexibleContentWidget().addId("proof-container");
    messageContainer.addContent(messageTitleWidget);
    messageContainer.addContent(this.proofInputWidget);
    content.addContent(messageContainer);

    FlexibleContentWidget buttonContainer = new FlexibleContentWidget().addId("button-container");
    buttonContainer.addContent(sendButton);
    buttonContainer.addContent(closeButton);
    content.addContent(buttonContainer);

    container.addContent(header);
    container.addContent(content);

    this.document.addChild(container);
  }

  private boolean sendForm() {
    String proof = this.proofInputWidget.getText();
    if(this.proofInputWidget.getText().isEmpty()) {
      if(!this.addon.rankUtil().isAdmin()) {
        this.addon.pushNotification(
            Component.translatable("terramc_staff.popup.failed.title", NamedTextColor.DARK_RED),
            Component.translatable("terramc_staff.popup.failed.empty", NamedTextColor.RED, Component.translatable("terramc_staff.popup.ban.proof", NamedTextColor.YELLOW))
        );
        return false;
      } else {
        proof = "X";
      }
    }

    this.addon.labyAPI().minecraft().chatExecutor().chat("/ban " + this.playerName + " " + this.banReason.id() + " " + proof, false);
    return true;
  }

  public enum BanReason {

    HACKING(7),
    UMGEHUNG(8),
    ABUSE(9),
    BUGUSING(10),
    TEAMING(11),
    GHOSTING(12),
    TRAPPING(13),
    SKIN(14);

    private final int id;

    BanReason(int id) {
      this.id = id;
    }

    public int id() {
      return id;
    }

  }

}
