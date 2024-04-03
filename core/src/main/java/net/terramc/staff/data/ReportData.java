package net.terramc.staff.data;

import java.util.UUID;

public class ReportData {

  private int id;
  private String reported;
  private UUID reportedUUID;
  private String reporter;
  private UUID reporterUUID;
  private String reason;
  private int priority;

  public ReportData(int id, String reported, UUID reportedUUID, String reporter, UUID reporterUUID, String reason, int priority) {
    this.id = id;
    this.reported = reported;
    this.reportedUUID = reportedUUID;
    this.reporter = reporter;
    this.reporterUUID = reporterUUID;
    this.reason = reason;
    this.priority = priority;
  }

  public int id() {
    return id;
  }

  public String reported() {
    return reported;
  }

  public UUID reportedUUID() {
    return reportedUUID;
  }

  public String reporter() {
    return reporter;
  }

  public UUID reporterUUID() {
    return reporterUUID;
  }

  public String reason() {
    return reason;
  }

  public int priority() {
    return priority;
  }

  public void priority(int priority) {
    this.priority = priority;
  }

  public static ReportData fromString(String input) {
    String[] split = input.split(";");
    if(split.length != 7) return null;
    int id = Integer.parseInt(split[0]);
    String reported = split[1];
    UUID reportedUUID = UUID.fromString(split[2]);
    String reporter = split[3];
    UUID reporterUUID = UUID.fromString(split[4]);
    String reason = split[5];
    int priority = Integer.parseInt(split[6]);

    return new ReportData(id, reported, reportedUUID, reporter, reporterUUID, reason, priority);
  }

}
