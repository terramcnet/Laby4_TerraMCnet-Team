package net.terramc.staff.util;

import com.google.gson.JsonObject;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.util.io.web.request.Request;
import net.terramc.staff.TerraStaffAddon;
import net.terramc.staff.data.AddonData;
import net.terramc.staff.data.ServerData;

public class ApiUtil {

  private TerraStaffAddon addon;
  public String authKey = "NA";

  public ApiUtil(TerraStaffAddon addon) {
    this.addon = addon;
  }

  private static String BASE_URL = "http://api.terramc.net/";

  // actions = [restart, maintenance]
  public void sendCloudControl(UUID uuid, String action, String group) {
    if(!this.addon.rankUtil().canControlCloud()) return;

    Request.ofString()
        .url("http://45.88.108.143:3610/cloud?uuid="+uuid+"&auth="+authKey+"&action="+action+"&group="+group)
        .async()
        .execute(response -> {
          if(response.getStatusCode() == 400) {
            this.addon.pushNotification(Component.translatable("terramc_staff.notification.cloud.error.title"),
                Component.translatable("terramc_staff.notification.cloud.error.parameter").color(
                    TextColor.color(255, 85, 85)));
            return;
          }
          if(response.getStatusCode() == 401) {
            this.addon.pushNotification(Component.translatable("terramc_staff.notification.cloud.error.title"),
                Component.translatable("terramc_staff.notification.cloud.error.notAuthorized").color(
                    TextColor.color(255, 85, 85)));
            return;
          }
          if(response.getStatusCode() == 404) {
            this.addon.pushNotification(Component.translatable("terramc_staff.notification.cloud.error.title"),
                Component.translatable("terramc_staff.notification.cloud.error.groupNotFound").color(
                    TextColor.color(255, 85, 85)));
            return;
          }
          if(response.getStatusCode() != 200) {
            this.addon.pushNotification(Component.translatable("terramc_staff.notification.cloud.error.title"),
                Component.translatable("terramc_staff.notification.cloud.error.unknown", Component.text(response.getStatusCode())).color(
                    TextColor.color(255, 85, 85)));
            return;
          }
          this.addon.pushNotification(Component.translatable("terramc_staff.notification.cloud.success.title"), Component.text("§e" + response.get()));
        });
  }

  public void loadServerData(UUID uuid) {
    Request.ofGson(JsonObject.class)
        .url(BASE_URL + "staff?req=serverData&uuid="+uuid)
        .async()
        .execute(response -> {
          if(response.getStatusCode() != 200) {
            if(response.getStatusCode() == 401) return;
            this.addon.pushNotification(Component.translatable("terramc_staff.notification.error.title"),
                Component.translatable("terramc_staff.notification.error.api.serverStats").color(
                    TextColor.color(255, 85, 85)));
            return;
          }
          JsonObject jsonObject = response.get();

          ServerData.Information.setRegisteredPlayers(jsonObject.get("registeredPlayers").getAsInt());

          if(jsonObject.has("serverStats")) {
            JsonObject serverStats = jsonObject.get("serverStats").getAsJsonObject();
            ServerData.Information.setMaxPlayers(serverStats.get("maxPlayers").getAsInt());
            ServerData.Information.setMaxPlayersToday(serverStats.get("maxPlayersToday").getAsInt());
            ServerData.Information.setVotes(serverStats.get("votes").getAsInt());
          }
          if(jsonObject.has("groupStats")) {
            ServerData.Information.setMaxPlayerData(jsonObject.get("groupStats").getAsString().replace("&", "§"));
          }

        });
  }

  public void loadRankData(UUID playerUuid) {
    Request.ofGson(JsonObject.class)
        .url(BASE_URL + "staff?req=staffData&uuid="+playerUuid+"&source=Addon_LM4")
        .async()
        .execute(response -> {
          if(response.getStatusCode() != 200) {
            this.addon.pushNotification(Component.translatable("terramc_staff.notification.error.title"),
                Component.translatable("terramc_staff.notification.error.api.data").color(
                    TextColor.color(255, 85, 851)));
            return;
          }
          JsonObject jsonObject = response.get();

          if(jsonObject.has("Global") && jsonObject.get("Global").isJsonObject()) {
            JsonObject global = jsonObject.get("Global").getAsJsonObject();
            AddonData.setRank(global.get("Rank").getAsString());
          }

          //Laby.labyAPI().minecraft().executeOnRenderThread(() -> addon.terraMainActivity.updateStaffActivity());
        });
  }

}
