package org.exoplatform.outlook.rest.controller;

import org.exoplatform.outlook.jcr.File;

/**
 * The type File info.
 */
public class FileInfo extends GeneralInfoBox {

  private String  name;

  private boolean isFolder;

  private String  fullPath;

  private String  pathLabel;

  private String  url;

  private String  webdavUrl;

  private String  title;

  /**
   * Instantiates a new Subfile info.
   *
   * @param file the file
   */
  public FileInfo(File file) {
    setName(file.getName());
    setIsFolder(file.isFolder());
    setFullPath(file.getFullPath());
    setPathLabel(file.getPathLabel());
    setUrl(file.getUrl());
    setWebdavUrl(file.getWebdavUrl());
    setTitle(file.getTitle());
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

  /**
   * Is folder boolean.
   *
   * @return the boolean
   */
  public boolean isFolder() {
    return isFolder;
  }

  /**
   * Sets folder.
   *
   * @param isFolder is the folder boolean
   */
  public void setIsFolder(boolean isFolder) {
    isFolder = isFolder;
  }

  /**
   * Gets full path.
   *
   * @return the full path
   */
  public String getFullPath() {
    return fullPath;
  }

  /**
   * Sets full path.
   *
   * @param fullPath the full path
   */
  public void setFullPath(String fullPath) {
    this.fullPath = fullPath;
  }

  /**
   * Gets path label.
   *
   * @return the path label
   */
  public String getPathLabel() {
    return pathLabel;
  }

  /**
   * Sets path label.
   *
   * @param pathLabel the path label
   */
  public void setPathLabel(String pathLabel) {
    this.pathLabel = pathLabel;
  }

  /**
   * Gets webdav url.
   *
   * @return the webdav url
   */
  public String getWebdavUrl() {
    return webdavUrl;
  }

  /**
   * Sets webdav url.
   *
   * @param webdavUrl the webdav url
   */
  public void setWebdavUrl(String webdavUrl) {
    this.webdavUrl = webdavUrl;
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
   * Sets title.
   *
   * @param title the title
   */
  public void setTitle(String title) {
    this.title = title;
  }
}
