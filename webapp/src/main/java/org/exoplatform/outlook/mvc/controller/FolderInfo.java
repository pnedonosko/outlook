package org.exoplatform.outlook.mvc.controller;

import org.exoplatform.outlook.OutlookException;
import org.exoplatform.outlook.jcr.File;
import org.exoplatform.outlook.jcr.Folder;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import javax.jcr.RepositoryException;
import java.util.*;

/**
 * The Folder info.
 */
public class FolderInfo extends GeneralInfoBox {

  /** The Constant LOG. */
  private static final Log LOG  = ExoLogger.getLogger(FolderInfo.class);

  private final String     type = "folder";

  private String           name;

  private String           path;

  private String           title;

  private String           created;

  private String           modified;

  private String           author;

  private String           lastModifier;

  private String           pathLabel;

  private String           explorerLink;

  /**
   * Instantiates a new Folder info.
   *
   * @param folder the folder
   * @param metadata the metadata
   * @param links the links
   */
  public FolderInfo(Folder folder, PagedResources.PageMetadata metadata, List<Link> links) {

    set_metadata(new Metadata(metadata));
    add(links);

    setName(folder.getName());
    setPath(folder.getPath());
    setTitle(folder.getTitle());
    setModified(folder.getLastModified());
    setLastModifier(folder.getLastModifier());
    setPathLabel(folder.getPathLabel());
    setExplorerLink(folder.getUrl());

    Set<Folder> fullSubfolders = null;

    try {
      fullSubfolders = folder.getSubfolders();
    } catch (RepositoryException e) {
      LOG.error(e);
    } catch (OutlookException e) {
      LOG.error(e);
    }

    Set<SubfolderInfo> subfolders = null;

    if (fullSubfolders != null) {
      subfolders = new HashSet<>(fullSubfolders.size());

      Set<SubfolderInfo> finalSubfolders = subfolders;
      fullSubfolders.forEach((v) -> {
        finalSubfolders.add(new SubfolderInfo(v));
      });
    }

    Set<File> fullFiles = null;

    try {
      fullFiles = folder.getFiles();
    } catch (RepositoryException e) {
      LOG.error(e);
    } catch (OutlookException e) {
      LOG.error(e);
    }

    Set<FileInfo> subfiles = null;

    if (fullFiles != null) {
      subfiles = new HashSet<>(fullFiles.size());

      Set<FileInfo> finalSubfiles = subfiles;
      fullFiles.forEach((v) -> {
        finalSubfiles.add(new FileInfo(v));
      });
    }

    Children children = new Children(subfiles, subfolders);

    Collection<Children> contextParams = new ArrayList();
    contextParams.add(children);
    set_embedded(new EmbeddedContent(contextParams));
  }

  /**
   * Gets path.
   *
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * Sets path.
   *
   * @param path the path
   */
  public void setPath(String path) {
    this.path = path;
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
   * Gets explorer link.
   *
   * @return the explorer link
   */
  public String getExplorerLink() {
    return explorerLink;
  }

  /**
   * Sets explorer link.
   *
   * @param explorerLink the explorer link
   */
  public void setExplorerLink(String explorerLink) {
    this.explorerLink = explorerLink;
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

  /**
   * Gets created.
   *
   * @return the created
   */
  public String getCreated() {
    return created;
  }

  /**
   * Sets created.
   *
   * @param created the created
   */
  public void setCreated(String created) {
    this.created = created;
  }

  /**
   * Gets modified.
   *
   * @return the modified
   */
  public String getModified() {
    return modified;
  }

  /**
   * Sets modified.
   *
   * @param modified the modified
   */
  public void setModified(String modified) {
    this.modified = modified;
  }

  /**
   * Gets author.
   *
   * @return the author
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Sets author.
   *
   * @param author the author
   */
  public void setAuthor(String author) {
    this.author = author;
  }

  /**
   * Gets last modifier.
   *
   * @return the last modifier
   */
  public String getLastModifier() {
    return lastModifier;
  }

  /**
   * Sets last modifier.
   *
   * @param lastModifier the last modifier
   */
  public void setLastModifier(String lastModifier) {
    this.lastModifier = lastModifier;
  }

  private class SubfolderInfo {

    private String title;

    private String lastModified;

    /**
     * Instantiates a new Subfolder info.
     *
     * @param subfolder the subfolder
     */
    public SubfolderInfo(Folder subfolder) {
      setTitle(subfolder.getTitle());
      setLastModified(subfolder.getLastModified());
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

    /**
     * Gets last modified.
     *
     * @return the last modified
     */
    public String getLastModified() {
      return lastModified;
    }

    /**
     * Sets last modified.
     *
     * @param lastModified the last modified
     */
    public void setLastModified(String lastModified) {
      this.lastModified = lastModified;
    }
  }

  private class Children {

    private Set<FileInfo>      files;

    private Set<SubfolderInfo> folders;

    /**
     * Instantiates a new Children.
     *
     * @param files the files
     * @param folders the folders
     */
    public Children(Set<FileInfo> files, Set<SubfolderInfo> folders) {
      this.files = files;
      this.folders = folders;
    }

    /**
     * Gets files.
     *
     * @return the files
     */
    public Set<FileInfo> getFiles() {
      return files;
    }

    /**
     * Sets files.
     *
     * @param files the files
     */
    public void setFiles(Set<FileInfo> files) {
      this.files = files;
    }

    /**
     * Gets folders.
     *
     * @return the folders
     */
    public Set<SubfolderInfo> getFolders() {
      return folders;
    }

    /**
     * Sets folders.
     *
     * @param folders the folders
     */
    public void setFolders(Set<SubfolderInfo> folders) {
      this.folders = folders;
    }
  }
}
