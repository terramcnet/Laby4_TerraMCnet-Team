package net.terramc.staff.util;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class TerraStaffTextures {

  public static class Common {

    public static final Icon ICON;

    static {
      ICON = Icon.texture(ResourceLocation.create("terramc_staff", "textures/icon.png"));
    }

  }

  public static class Hud {

    private static final ResourceLocation TEXTURE = ResourceLocation.create("terramc_staff", "textures/common.png");

    public static final Icon RESTART_TIME;
    public static final Icon VANISH;
    public static final Icon SERVER_STATUS;

    static {
      RESTART_TIME = Icon.sprite16(TEXTURE, 0, 0);
      VANISH = Icon.sprite16(TEXTURE, 1, 0);
      SERVER_STATUS = Icon.sprite16(TEXTURE, 2, 0);
    }

  }

  @SuppressWarnings("unused")
  private static Icon sprite64(ResourceLocation resourceLocation, int slotX, int slotY) {
    return Icon.sprite(resourceLocation, slotX << 6, slotY << 6, 64, 64, 512, 512);
  }

}
