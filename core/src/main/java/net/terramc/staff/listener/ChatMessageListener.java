package net.terramc.staff.listener;

import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.terramc.staff.TerraStaffAddon;
import net.terramc.staff.data.AddonData;

public class ChatMessageListener {

  private final TerraStaffAddon addon;

  public ChatMessageListener(TerraStaffAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    if(!this.addon.configuration().enabled().get()) return;
    if(!this.addon.isConnected()) return;
    //String formatted = event.chatMessage().getOriginalFormattedText();
    String plain = event.chatMessage().getOriginalPlainText();

    // Cloud-Section

    String cloudPrefix = "▎▏ Cloud » ";

    if(plain.startsWith(cloudPrefix + "Der Service ") & plain.contains(" wird gestoppt...")) {
      String service = plain.replace(cloudPrefix + "Der Service ", "").replace(" wird gestoppt...", "");
      switch (this.addon.configuration().cloudNotifyType().get()) {
        case NOTIFICATION -> {
          this.addon.pushNotification(Component.text("§7§l§o▎§8§l§o▏ §aCloud"),
              Component.text("§7Cloud-Service §e" + service + " §7wird §cgestoppt§7..."));
          event.setCancelled(true);
        }
        case HIDE -> event.setCancelled(true);
      }
    }

    if(plain.startsWith(cloudPrefix + "Der Service ") & plain.contains(" wird gestartet...")) {
        String service = plain.replace(cloudPrefix + "Der Service ", "").replace(" wird gestartet...", "");
      switch (this.addon.configuration().cloudNotifyType().get()) {
        case NOTIFICATION -> {
          this.addon.pushNotification(Component.text("§7§l§o▎§8§l§o▏ §aCloud"),
              Component.text("§7Cloud-Service §e" + service + " §7wird §agestartet§7..."));
          event.setCancelled(true);
        }
        case HIDE -> event.setCancelled(true);
      }
    }

    String reportPrefix = "▎▏ Report » ";

    if(plain.equals(reportPrefix + "Der Report wurde erfolgreich abgeschlossen.")) {
      AddonData.setCurrentReport(null);
    }

  }

}
