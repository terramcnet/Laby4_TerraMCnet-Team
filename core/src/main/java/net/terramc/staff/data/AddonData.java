package net.terramc.staff.data;

import java.util.ArrayList;
import java.util.List;

public class AddonData {

  private static List<ReportData> reports = new ArrayList<>();

  public static List<ReportData> reports() {
    return reports;
  }

  public enum CloudNotifyType {
    CHAT, NOTIFICATION, HIDE
  }

  private static String rank = "Spieler";


  private static boolean vanish = false;
  private static boolean adminVanish = false;
  private static boolean autoVanish = false;

  private static boolean rankToggled = false;

  private static ReportData currentReport = null;

  public static String getRank() {
    return rank;
  }

  public static void setRank(String rank) {
    AddonData.rank = rank;
  }

  public static boolean isVanish() {
    return vanish;
  }

  public static boolean isAdminVanish() {
    return adminVanish;
  }

  public static boolean isAutoVanish() {
    return autoVanish;
  }

  public static void setVanish(boolean vanish) {
    AddonData.vanish = vanish;
  }

  public static void setAdminVanish(boolean adminVanish) {
    AddonData.adminVanish = adminVanish;
  }

  public static void setAutoVanish(boolean autoVanish) {
    AddonData.autoVanish = autoVanish;
  }

  public static boolean rankToggled() {
    return rankToggled;
  }

  public static void rankToggled(boolean rankToggled) {
    AddonData.rankToggled = rankToggled;
  }

  public static ReportData getCurrentReport() {
    return currentReport;
  }

  public static void setCurrentReport(ReportData currentReport) {
    AddonData.currentReport = currentReport;
  }
}
