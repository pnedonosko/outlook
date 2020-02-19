package org.exoplatform.outlook.mvc.controller;

import org.exoplatform.outlook.jcr.File;

/**
 * The type File info.
 */
public class FileInfo {

  private String name;

  private String url;

  /**
   * Instantiates a new File info.
   *
   * @param file the file
   */
  public FileInfo(File file) {
    setName(file.getName());
    setUrl(file.getUrl());
  }

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets url.
   *
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets url.
   *
   * @param url the url
   */
  public void setUrl(String url) {
    this.url = url;
  }
}
