package net.terramc.staff.util;

import net.terramc.staff.data.AddonData;

public class RankUtil {

  public boolean isAdmin() {
    String rank = AddonData.getRank();
    if(rank == null) return false;
    return rank.equals("Inhaber") || rank.equals("Admin");
  }

  public boolean isStaff() {
    String rank = AddonData.getRank();
    if(rank == null) return false;
    return rank.equals("Inhaber") || rank.equals("Admin") || rank.equals("SrDev") || rank.equals("Dev") ||
        rank.equals("Content") || rank.equals("SrMod") || rank.equals("Mod") || rank.equals("SrSup") ||
        rank.equals("Sup") || rank.equals("Designer") ||rank.equals("Builder");
  }

  public boolean canControlCloud() {
    String rank = AddonData.getRank();
    if(rank == null) return false;
    return isAdmin() || rank.equals("SrDev") || rank.equals("Dev") || rank.equals("SrMod");
  }

}
