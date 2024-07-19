package net.terramc.staff.listener;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoAddEvent;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerLoginEvent;
import net.labymod.api.util.concurrent.task.Task;
import net.terramc.staff.TerraStaffAddon;
import net.terramc.staff.data.AddonData;

public class NetworkListener {

  private final TerraStaffAddon addon;

  public NetworkListener(TerraStaffAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onNetworkLogin(ServerLoginEvent event) {

    Task.builder(() -> this.addon.broadcastUtil().sendStaffTag()).delay(3, TimeUnit.SECONDS).build().execute();

    if(!this.addon.configuration().enabled().get()) return;
    if(event.serverData().actualAddress().matches("terramc.net", 25565, true)) {
      this.addon.setConnected(true);
      UUID uuid = Laby.references().gameUserService().clientGameUser().getUniqueId();
      this.addon.apiUtil().loadRankData(uuid);
      this.addon.apiUtil().loadServerData(uuid);
    }
  }

  @Subscribe
  public void onPlayerInfoAdd(PlayerInfoAddEvent event) {
    Task.builder(() -> this.addon.broadcastUtil().sendStaffTag()).delay(3, TimeUnit.SECONDS).build().execute();
  }

  @Subscribe
  public void onNetworkDisconnect(ServerDisconnectEvent event) {
    this.addon.setConnected(false);
    AddonData.reports().clear();
    AddonData.setCurrentReport(null);
  }

}
