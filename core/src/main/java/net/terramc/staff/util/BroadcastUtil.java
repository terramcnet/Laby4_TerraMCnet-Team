package net.terramc.staff.util;

import com.google.gson.JsonObject;
import net.labymod.api.Laby;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.terramc.staff.TerraStaffAddon;

public class BroadcastUtil {

  private TerraStaffAddon addon;

  public BroadcastUtil(TerraStaffAddon addon) {
    this.addon = addon;
  }

  public void sendStaffTag() {
    LabyConnectSession session = this.addon.labyAPI().labyConnect().getSession();
    if(session == null) return;

    if(this.addon.rankUtil().isStaff()) {
      JsonObject data = new JsonObject();
      data.addProperty("uuid", Laby.labyAPI().getUniqueId().toString());
      data.addProperty("userName", Laby.labyAPI().getName());

      JsonObject object = new JsonObject();
      object.add(this.addon.configuration().hideOwnTag().get() ? "hideTag" : "showTag", data);
      session.sendBroadcastPayload("terra-group-tag", object);
    }
  }

  @SuppressWarnings("unused")
  public void sendUserTag() {
    LabyConnectSession session = this.addon.labyAPI().labyConnect().getSession();
    if(session == null) return;
    JsonObject object = new JsonObject();
    object.addProperty("usingAddon", Laby.labyAPI().getUniqueId().toString());
    session.sendBroadcastPayload("terra-group-tag", object);
  }

}
