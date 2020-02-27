package org.exoplatform.outlook.model;

import java.text.DateFormat;
import java.util.Locale;

/**
 * The type Activity info.
 */
public class ActivityInfo {
  private String title;

  private String type;

  private String link;

  private String postedDate;

  /**
   * Instantiates a new Activity info.
   */
  public ActivityInfo() {
  }

  /**
   * Instantiates a new Activity info.
   *
   * @param title the title
   * @param type the type
   * @param link the link
   * @param postedDate the posted date
   */
  public ActivityInfo(String title, String type, String link, Long postedDate) {
    this.title = title;
    this.type = type;
    this.link = link;
    this.postedDate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(postedDate);
  }

  /**
   * Gets title.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Gets link.
   *
   * @return the link
   */
  public String getLink() {
    return link;
  }

  /**
   * Gets posted date.
   *
   * @return the posted date
   */
  public String getPostedDate() {
    return postedDate;
  }
}
