package org.exoplatform.outlook.app.rest.info;

import org.exoplatform.outlook.OutlookException;
import org.exoplatform.outlook.jcr.File;
import org.exoplatform.outlook.jcr.Folder;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.wcm.core.NodetypeConstant;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import javax.jcr.RepositoryException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The Folder info.
 */
public class FolderInfo extends GeneralInfoBox {

  /** The Constant LOG. */
  private static final Log LOG    = ExoLogger.getLogger(FolderInfo.class);

  private final String     FOLDER = "folder";

  private String           name;

  private String           path;

  private String           title;

  private String           created;

  private String           modified;

  private String           author;

  private String           lastModifier;

  private String           explorerPath;

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
    setExplorerPath(folder.getPathLabel());
    setExplorerLink(folder.getUrl());

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    try {
      setCreated(formatter.format(folder.getNode().getProperty(NodetypeConstant.EXO_DATE_CREATED).getDate().getTime()));
    } catch (RepositoryException e) {
      LOG.error(e);
    }

    try {
      setModified(formatter.format(folder.getNode().getProperty(NodetypeConstant.EXO_LAST_MODIFIED_DATE).getDate().getTime()));
    } catch (RepositoryException e) {
      LOG.error(e);
    }

    try {
      setLastModifier(folder.getNode().getProperty(NodetypeConstant.EXO_LAST_MODIFIER).getString());
    } catch (RepositoryException e) {
      LOG.error(e);
    }

    try {
      setAuthor(folder.getNode().getProperty(NodetypeConstant.EXO_OWNER).getString());
    } catch (RepositoryException e) {
      LOG.error(e);
    }

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

      for (Folder fold : fullSubfolders) {
        subfolders.add(new SubfolderInfo(fold));
      }
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

      for (File f : fullFiles) {
        subfiles.add(new FileInfo(f));
      }
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
  public String getExplorerPath() {
    return explorerPath;
  }

  /**
   * Sets path label.
   *
   * @param explorerPath the path label
   */
  public void setExplorerPath(String explorerPath) {
    this.explorerPath = explorerPath;
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

      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      try {
        setLastModified(formatter.format(subfolder.getNode()
                                                  .getProperty(NodetypeConstant.EXO_LAST_MODIFIED_DATE)
                                                  .getDate()
                                                  .getTime()));
      } catch (RepositoryException e) {
        LOG.error(e);
      }
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

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      SubfolderInfo that = (SubfolderInfo) o;
      return Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getLastModified(), that.getLastModified());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getTitle(), getLastModified());
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
