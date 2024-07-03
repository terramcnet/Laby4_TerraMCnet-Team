package net.terramc.staff.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent;
import net.labymod.serverapi.protocol.payload.exception.PayloadReaderException;
import net.labymod.serverapi.protocol.payload.io.PayloadReader;
import net.terramc.staff.TerraStaffAddon;
import net.terramc.staff.data.AddonData;
import net.terramc.staff.data.ReportData;
import net.terramc.staff.data.ServerData;
import java.util.concurrent.atomic.AtomicReference;

public class NetworkPayloadListener {

  private final TerraStaffAddon addon;

  public NetworkPayloadListener(TerraStaffAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onReceive(NetworkPayloadEvent event) {
    if(event.identifier().getNamespace().equals("labymod3") & event.identifier().getPath().equals("main")) {
      try {
        PayloadReader reader = new PayloadReader(event.getPayload());
        String messageKey = reader.readString();
        String messageContent = reader.readString();

        if(messageKey.equals("TerraMod") & this.addon.isConnected()) {
          JsonElement serverMessage = this.addon.gson().fromJson(messageContent, JsonElement.class);
          if(!serverMessage.isJsonObject()) return;
          JsonObject object = serverMessage.getAsJsonObject();

          /*if(object.has("LOBBY")) {
            if(object.get("LOBBY").getAsBoolean()) {
              AddonData.resetValues();
            }
          }*/

          if(object.has("playerRank")) {
            AddonData.setRank(object.get("playerRank").getAsString());
            //this.addon.terraMainActivity.updateStaffActivity();
          }

          // Staff

          if(object.has("vanish")) {
            AddonData.setVanish(object.get("vanish").getAsBoolean());
          }
          if(object.has("adminVanish")) {
            AddonData.setAdminVanish(object.get("adminVanish").getAsBoolean());
          }
          if(object.has("autoVanish")) {
            AddonData.setAutoVanish(object.get("autoVanish").getAsBoolean());
          }
          if(object.has("serverTPS")) {
            ServerData.setServerTps(object.get("serverTPS").getAsString());
          }
          if(object.has("serverCpuUsage")) {
            ServerData.setCpuUsage(object.get("serverCpuUsage").getAsString());
          }
          if(object.has("serverHeapUsage")) {
            ServerData.setRamUsage(object.get("serverHeapUsage").getAsString());
          }
          if(object.has("restartTime")) {
            ServerData.setRestartTime(object.get("restartTime").getAsString());
          }
          if(object.has("authKey")) {
            this.addon.apiUtil().authKey = object.get("authKey").getAsString();
          }

          if(object.has("toggleRank")) {
            AddonData.rankToggled(object.get("toggleRank").getAsBoolean());
          }

          if(object.has("reportAddonDataAdd")) {
            ReportData data = ReportData.fromString(object.get("reportAddonDataAdd").getAsString());
            if(data != null) {
              AddonData.reports().add(data);
            }
          }

          if(object.has("reportAddonDataDel")) {
            int id = object.get("reportAddonDataDel").getAsInt();
            AtomicReference<ReportData> remove = new AtomicReference<>(null);
            AddonData.reports().forEach(reportData -> {
              if(reportData.id() == id) {
                remove.set(reportData);
              }
            });
            if(remove.get() != null) {
              AddonData.reports().remove(remove.get());
            }
          }

          if(object.has("reportAddonUpdatePriority")) {
            String[] split = object.get("reportAddonUpdatePriority").getAsString().split(";");
            if(split.length != 2) return;
            try {
              int id = Integer.parseInt(split[0]);
              int priority = Integer.parseInt(split[1]);
              AddonData.reports().forEach(reportData -> {
                if(reportData.id() == id) {
                  reportData.priority(priority);
                }
              });
            } catch (NumberFormatException ignored) {}
          }

          if(object.has("reportAddonAccepted")) {

            String[] split = object.get("reportAddonAccepted").getAsString().split(";");
            if(split.length != 2) return;
            try {
              int id = Integer.parseInt(split[0]);
              String playerName = split[1];
              AddonData.reports().forEach(reportData -> {
                if(reportData.id() == id) {
                  if(!reportData.accepted()) {
                    reportData.accepted(true);

                    this.addon.pushNotification(
                        Component.text(TerraStaffAddon.doubleLine + "§4Report-System"),
                        Component.text("§7Der Report mit der ID §e#" + id + " §8(§c" + reportData.reported() + "§8) §7wird nun von §a" + playerName + " §7bearbeitet.")
                    );

                  }
                }
              });
            } catch (NumberFormatException ignored) {}

            /*int id = object.get("reportAddonAccepted").getAsInt();
            AddonData.reports().forEach(reportData -> {
              if(reportData.id() == id) {
                if(!reportData.accepted()) {
                  reportData.accepted(true);

                  this.addon.pushNotification(
                      Component.text(TerraStaffAddon.doubleLine + "§4Report-System"),
                      Component.text("§7Der Report mit der ID §e#" + id + " §8(§c" + reportData.reported() + "§8) §7wird nun von einem Teammitglied bearbeitet.")
                  );

                }
              }
            });*/
          }

          if(object.has("currentReportData")) {
            String message = object.get("currentReportData").getAsString();
            ReportData data = ReportData.fromString(message);
            if(data != null) {
              AddonData.setCurrentReport(data);
            } else {
              this.addon.pushNotification(
                  Component.translatable("terramc_staff.notification.error.title"),
                  Component.text("Unable to set current report data! Check log for more information.")
              );
              this.addon.logger().info("[Current Report Data] Unable to set current report data. Maybe wrong formatting of the message. Provided message: '" + message + "'");
            }
          }


        }
      } catch (PayloadReaderException ignored) {

      }
    }
  }

}
