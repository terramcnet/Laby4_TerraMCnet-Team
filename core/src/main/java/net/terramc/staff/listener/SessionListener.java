package net.terramc.staff.listener;

import java.util.UUID;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.session.SessionUpdateEvent;
import net.terramc.staff.TerraStaffAddon;

public class SessionListener {

  private TerraStaffAddon addon;

  public SessionListener(TerraStaffAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onSessionUpdate(SessionUpdateEvent event) {
    UUID newUUID = event.newSession().getUniqueId();
    this.addon.apiUtil().loadRankData(newUUID);
    this.addon.apiUtil().loadServerData(newUUID);
    this.addon.apiUtil().authKey = "NA";
  }

}
