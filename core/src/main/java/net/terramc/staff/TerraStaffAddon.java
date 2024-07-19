package net.terramc.staff;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.google.gson.Gson;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.Type;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.GsonUtil;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.version.SemanticVersion;
import net.terramc.staff.activities.navigation.TerraStaffNavigation;
import net.terramc.staff.hudwidget.CurrentReportHudWidget;
import net.terramc.staff.hudwidget.RestartTimeHudWidget;
import net.terramc.staff.hudwidget.ServerStatusHudWidget;
import net.terramc.staff.hudwidget.VanishHudWidget;
import net.terramc.staff.interaction.BanBulletPoint;
import net.terramc.staff.interaction.MuteBulletPoint;
import net.terramc.staff.listener.ChatMessageListener;
import net.terramc.staff.listener.NetworkListener;
import net.terramc.staff.listener.NetworkPayloadListener;
import net.terramc.staff.listener.SessionListener;
import net.terramc.staff.util.ApiUtil;
import net.terramc.staff.util.BroadcastUtil;
import net.terramc.staff.util.RankUtil;

@AddonMain
public class TerraStaffAddon extends LabyAddon<TerraStaffConfiguration> {

  public final HudWidgetCategory widgetCategory = new HudWidgetCategory("terramc_staff");

  //public TerraMainActivity terraMainActivity;

  private ApiUtil apiUtil;
  private RankUtil rankUtil;
  private BroadcastUtil broadcastUtil;

  private boolean connected = false;
  public static String doubleLine = "§7§l§o▎§8§l§o▏ ";
  public static String doubleDots = "§7•§8●";

  private Gson gson;

  @Override
  protected void preConfigurationLoad() {
    Laby.references().revisionRegistry().register(new SimpleRevision("terramc_staff", new SemanticVersion("1.0.0"), "2024-03-31"));
  }

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.gson = GsonUtil.DEFAULT_GSON;

    UUID uuid = labyAPI().getUniqueId();
    this.apiUtil = new ApiUtil(this);
    this.apiUtil.loadRankData(uuid);
    this.apiUtil.loadServerData(uuid);

    //this.terraMainActivity = new TerraMainActivity(this);

    this.rankUtil = new RankUtil();
    this.broadcastUtil = new BroadcastUtil(this);

    this.registerListener(new ChatMessageListener(this));
    this.registerListener(new NetworkListener(this));
    this.registerListener(new SessionListener(this));
    this.registerListener(new NetworkPayloadListener(this));

    //labyAPI().navigationService().register("terramc_staff_ui", new TerraNavigationElement(this));

    labyAPI().hudWidgetRegistry().categoryRegistry().register(widgetCategory);
    labyAPI().hudWidgetRegistry().register(new RestartTimeHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new ServerStatusHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new VanishHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new CurrentReportHudWidget(this));

    labyAPI().interactionMenuRegistry().register("terramc_staff_ban", new BanBulletPoint(this));
    labyAPI().interactionMenuRegistry().register("terramc_staff_mute", new MuteBulletPoint(this));

    this.logger().info("[TerraMCnet Team] Addon enabled.");
    Task.builder(() -> TerraStaffNavigation.register(this)).delay(10, TimeUnit.SECONDS).build().execute();

    configuration().cloudNotifyType().addChangeListener(cloudNotifyType -> {
      if(!rankUtil.canControlCloud()) {
        if(cloudNotifyType != configuration().cloudNotifyType().defaultValue()) {
          pushNotification(
              Component.translatable("terramc_staff.notification.settings.title"),
              Component.translatable("terramc_staff.notification.settings.notPermitted")
          );
        }
        configuration().cloudNotifyType().set(configuration().cloudNotifyType().defaultValue());
      }
    });

    configuration().hideOwnTag().addChangeListener((type, oldValue, newValue) -> {
      if(!rankUtil.isStaff()) {
        if(newValue != configuration().hideOwnTag().defaultValue()) {
          pushNotification(
              Component.translatable("terramc_staff.notification.settings.title"),
              Component.translatable("terramc_staff.notification.settings.notPermitted")
          );
        }
        configuration().hideOwnTag().set(configuration().hideOwnTag().defaultValue());
        return;
      }
      broadcastUtil.sendStaffTag();
    });

    configuration().showTagAlways().addChangeListener((type, oldValue, newValue) -> {
      if(!rankUtil.isAdmin()) {
        if(newValue != configuration().showTagAlways().defaultValue()) {
          pushNotification(
              Component.translatable("terramc_staff.notification.settings.title"),
              Component.translatable("terramc_staff.notification.settings.notPermitted")
          );
        }
        configuration().showTagAlways().set(configuration().showTagAlways().defaultValue());
      }
    });

  }

  public void pushNotification(Component title, Component text) {
    Notification.Builder builder = Notification.builder()
        .title(title)
        .text(text)
        .icon(Icon.texture(ResourceLocation.create("terramc", "textures/icon.png")))
        .type(Type.SYSTEM);
    labyAPI().notificationController().push(builder.build());
  }

  public void pushNotificationIcon(Component title, Component text, Icon icon) {
    Notification.Builder builder = Notification.builder()
        .title(title)
        .text(text)
        .icon(icon)
        .type(Type.SYSTEM);
    labyAPI().notificationController().push(builder.build());
  }

  @Override
  protected Class<TerraStaffConfiguration> configurationClass() {
    return TerraStaffConfiguration.class;
  }

  public Gson gson() {
    return gson;
  }

  public boolean isConnected() {
    return connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }

  public ApiUtil apiUtil() {
    return apiUtil;
  }

  public RankUtil rankUtil() {
    return rankUtil;
  }

  public BroadcastUtil broadcastUtil() {
    return broadcastUtil;
  }

}
