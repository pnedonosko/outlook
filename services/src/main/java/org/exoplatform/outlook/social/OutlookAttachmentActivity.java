
/*
 * Copyright (C) 2003-2016 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.outlook.social;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.ecm.webui.utils.Utils;
import org.exoplatform.outlook.social.OutlookAttachmentActivity.ViewDocumentActionListener;
import org.exoplatform.services.cms.documents.TrashService;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.config.RepositoryConfigurationException;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.wcm.core.NodetypeConstant;
import org.exoplatform.services.wcm.friendly.FriendlyService;
import org.exoplatform.services.wcm.utils.WCMCoreUtils;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.webui.activity.BaseUIActivity;
import org.exoplatform.social.webui.activity.UIActivitiesContainer;
import org.exoplatform.social.webui.composer.PopupContainer;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.ComponentConfigs;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.ext.UIExtension;
import org.exoplatform.webui.ext.UIExtensionManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:pnedonosko@exoplatform.com">Peter Nedonosko</a>
 * @version $Id: OutlookMessageActivity.java 00000 Jul 12, 2016 pnedonosko $
 * 
 */
@ComponentConfigs({ @ComponentConfig(lifecycle = UIFormLifecycle.class,
                                     template = "classpath:groovy/templates/OutlookAttachmentActivity.gtmpl",
                                     events = { @EventConfig(listeners = ViewDocumentActionListener.class),
                                         @EventConfig(listeners = BaseUIActivity.LoadLikesActionListener.class),
                                         @EventConfig(listeners = BaseUIActivity.ToggleDisplayCommentFormActionListener.class),
                                         @EventConfig(listeners = BaseUIActivity.LikeActivityActionListener.class),
                                         @EventConfig(listeners = BaseUIActivity.SetCommentListStatusActionListener.class),
                                         @EventConfig(listeners = BaseUIActivity.PostCommentActionListener.class),
                                         @EventConfig(listeners = BaseUIActivity.DeleteActivityActionListener.class),
                                         @EventConfig(listeners = BaseUIActivity.DeleteCommentActionListener.class) }) })
public class OutlookAttachmentActivity extends BaseUIActivity {

  public static final String ACTIVITY_TYPE       = "outlook:attachment";

  public static final String COMMENT             = "comment";

  public static final String REPOSITORY          = "repository";

  public static final String WORKSPACE           = "workspace";

  public static final String FILES               = "files";

  public static final String AUTHOR              = "author";

  public static final String DATE_CREATED        = "dateCreated";

  public static final String DATE_LAST_MODIFIED  = "lastModified";

  public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

  public static final String DEFAULT_TIME_FORMAT = "HH:mm";

  protected static final Log LOG                 = ExoLogger.getLogger(OutlookAttachmentActivity.class);

  public static class ViewDocumentActionListener extends EventListener<OutlookAttachmentActivity> {
    @Override
    public void execute(Event<OutlookAttachmentActivity> event) throws Exception {
      // code adapted from FileUIActivity but using OutlookMessageDocumentPreview instead of UIDocumentPreview
      OutlookAttachmentActivity activity = event.getSource();
      String fileUUID = event.getRequestContext().getRequestParameter(OBJECTID);
      if (fileUUID != null && fileUUID.length() > 0) {
        UIActivitiesContainer uiActivitiesContainer = activity.getAncestorOfType(UIActivitiesContainer.class);
        PopupContainer uiPopupContainer = uiActivitiesContainer.getPopupContainer();

        RepositoryService repositoryService = WCMCoreUtils.getService(RepositoryService.class);
        ManageableRepository repository = repositoryService.getCurrentRepository();
        SessionProvider sessionProvider = WCMCoreUtils.getUserSessionProvider();
        Session session = sessionProvider.getSession(activity.getWorkspace(), repository);
        Node file = session.getNodeByUUID(fileUUID);

        OutlookMessageDocumentPreview preview = uiPopupContainer.createUIComponent(OutlookMessageDocumentPreview.class,
                                                                                   null,
                                                                                   "UIDocumentPreview");
        preview.setBaseUIActivity(activity);
        preview.setContentInfo(file.getPath(), repository.getConfiguration().getName(), activity.getWorkspace(), file);

        uiPopupContainer.activate(preview, 0, 0, true);
        event.getRequestContext().addUIComponentToUpdateByAjax(uiPopupContainer);
      } else {
        LOG.warn(OBJECTID + " doesn't contain file UUID for ViewDocument event in activity '" + activity + "'");
        event.getRequestContext().addUIComponentToUpdateByAjax(activity);
      }
    }
  }

  public class Attachment {
    final String      fileUUID;

    String            name;

    ThreadLocal<Node> node = new ThreadLocal<Node>();

    protected Attachment(String fileUUID, String name) {
      super();
      this.fileUUID = fileUUID;
      this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
      return name;
    }

    /**
     * @return the fileUUID
     */
    public String getFileUUID() {
      return fileUUID;
    }

    /**
     * Gets file title.
     * 
     * @return the title of Node.
     */
    public String getTitle() {
      Node node = null;
      try {
        node = node();
        if (node != null) {
          return org.exoplatform.ecm.webui.utils.Utils.getTitle(node);
        }
      } catch (Exception e) {
        LOG.error("Error reading node name " + node, e);
      }
      return getName();
    }

    /**
     * @return the mimeType
     */
    public String getMimeType() {
      Node node = null;
      try {
        node = node();
        if (node != null) {
          String mimeType = node.getNode("jcr:content").getProperty("jcr:mimeType").getString();
          if (mimeType.equals("application/rss+xml")) {
            // XXX it's a stuff copied from FileUIActivity.gtmpl in PLF 4.4
            mimeType = "text/html";
          }
          return mimeType;
        }
      } catch (RepositoryException e) {
        LOG.error("Error getting node mime type " + node, e);
      }
      return StringUtils.EMPTY;
    }

    public boolean isImage() throws Exception {
      String mimeType = getMimeType();
      return mimeType.startsWith("image") || mimeType.indexOf("icon") >= 0;
    }

    public String getSize() {
      Node node = null;
      try {
        node = node();
        if (node != null && node.hasNode(Utils.JCR_CONTENT)) {
          Node contentNode = node.getNode(Utils.JCR_CONTENT);
          double size = 0;
          if (contentNode.hasProperty(Utils.JCR_DATA)) {
            size = contentNode.getProperty(Utils.JCR_DATA).getLength();
          }
          return FileUtils.byteCountToDisplaySize((long) size);
        }
      } catch (RepositoryException e) {
        LOG.error("Error getting node mime type " + node, e);
      }
      return StringUtils.EMPTY;
    }

    public boolean isSupportPreview() throws Exception {
      Node node = null;
      try {
        node = node();
        if (node != null) {
          // code adapted from the super's method but with adding a node in to context
          UIExtensionManager manager = getApplicationComponent(UIExtensionManager.class);
          List<UIExtension> extensions = manager.getUIExtensions(Utils.FILE_VIEWER_EXTENSION_TYPE);

          Map<String, Object> context = new HashMap<String, Object>();
          context.put(Utils.MIME_TYPE, node.getNode(Utils.JCR_CONTENT).getProperty(Utils.JCR_MIMETYPE).getString());
          // add node in the context to help view filter recognize Outlook Message file
          context.put(Node.class.getName(), node);

          for (UIExtension extension : extensions) {
            if (manager.accept(Utils.FILE_VIEWER_EXTENSION_TYPE, extension.getName(), context)
                && !"Text".equals(extension.getName())) {
              return true;
            }
          }
        }
      } catch (Exception e) {
        LOG.error("Error getting node download link " + node, e);
      }
      return false;
    }

    public String getDownloadLink() {
      Node node = null;
      try {
        node = node();
        if (node != null) {
          return org.exoplatform.wcm.webui.Utils.getDownloadLink(node);
        }
      } catch (Exception e) {
        LOG.error("Error getting node download link " + node, e);
      }
      return StringUtils.EMPTY;
    }

    public String getPdfThumbnailImageLink() throws RepositoryException, UnsupportedEncodingException {
      Node node = null;
      try {
        node = node();
        if (node != null) {
          String portalName = PortalContainer.getCurrentPortalContainerName();
          String restContextName = PortalContainer.getCurrentRestContextName();
          String repositoryName = repository.getConfiguration().getName();
          String workspaceName = getWorkspace();

          String encodedPath = URLEncoder.encode(node.getPath(), "utf-8");
          encodedPath = encodedPath.replaceAll("%2F", "/");

          StringBuilder link = new StringBuilder();
          link.append('/');
          link.append(portalName);
          link.append('/');
          link.append(restContextName);
          link.append("/thumbnailImage/big/");
          link.append(repositoryName);
          link.append('/');
          link.append(workspaceName);
          link.append(encodedPath);

          return link.toString();
        }
      } catch (Exception e) {
        LOG.error("Error getting node PDF thumbnail link " + node, e);
      }
      return StringUtils.EMPTY;
    }

    public String getThumbnailImageLink() throws RepositoryException, UnsupportedEncodingException {
      Node node = null;
      try {
        node = node();
        if (node != null) {
          String portalName = PortalContainer.getCurrentPortalContainerName();
          String restContextName = PortalContainer.getCurrentRestContextName();
          String repositoryName = repository.getConfiguration().getName();
          String workspaceName = getWorkspace();

          String mimeType = getMimeType();

          StringBuilder link = new StringBuilder();
          // we use relative URL
          link.append('/');
          link.append(portalName);
          link.append('/');
          link.append(restContextName);

          if (mimeType.indexOf("icon") >= 0) {
            // Icon will be rendered directly from JCR
            link.append("/jcr/");
            link.append(repositoryName);
            link.append('/');
            link.append(workspaceName);

            FriendlyService friendlyService = WCMCoreUtils.getService(FriendlyService.class);
            if (node.isNodeType("nt:frozenNode")) {
              String uuid = node.getProperty("jcr:frozenUuid").getString();
              Node originalNode = node.getSession().getNodeByUUID(uuid);
              link.append(originalNode.getPath());
              link.append("?version=");
              link.append(node.getParent().getName());
            } else {
              link.append(node.getPath());
            }

            return friendlyService.getFriendlyUri(link.toString());
          } else {
            String path = node.getPath();
            try {
              if (node.hasNode(NodetypeConstant.JCR_CONTENT)) {
                node = node.getNode(NodetypeConstant.JCR_CONTENT);
              }
              ImageReader reader = null;
              Iterator<ImageReader> readers = ImageIO.getImageReadersByMIMEType(mimeType);
              ImageInputStream iis = ImageIO.createImageInputStream(node.getProperty("jcr:data").getStream());
              try {
                reader = readers.next();
                reader.setInput(iis, true);
                int imageHeight = reader.getHeight(0);
                int imageWidth = reader.getWidth(0);

                // align sizes
                final int defaultDimension = 300;
                if (imageHeight > imageWidth && imageHeight > defaultDimension) {
                  imageWidth = (defaultDimension * imageWidth) / imageHeight;
                  imageHeight = defaultDimension;
                } else if (imageWidth > imageHeight && imageWidth > defaultDimension) {
                  imageHeight = (defaultDimension * imageHeight) / imageWidth;
                  imageWidth = defaultDimension;
                } else if (imageWidth == imageHeight && imageHeight > defaultDimension) {
                  imageWidth = imageHeight = 300;
                }
                
                link.append("/thumbnailImage/custom/");
                link.append(imageWidth);
                link.append('x');
                link.append(imageHeight);
                link.append('/');
              } catch (NoSuchElementException e) {
                if (LOG.isDebugEnabled()) {
                  LOG.debug("Cannot find image reader for node " + node, e);
                }
                link.append("/thumbnailImage/big/");
              } finally {
                try {
                  iis.close();
                } catch (IOException e) {
                  LOG.warn("Error closing image data stream of " + node, e);
                }
                if (reader != null) {
                  reader.dispose();
                }
              }
            } catch (Exception e) {
              LOG.warn("Cannot read image node " + node + ":" + e.getMessage());
              // large is 300x300 as documented in ThumbnailRESTService
              //link.append("/thumbnailImage/custom/300x300/");
              link.append("/thumbnailImage/large/");
            }

            link.append(repositoryName);
            link.append('/');
            link.append(workspaceName);

            String encodedPath = URLEncoder.encode(path, "utf-8");
            encodedPath = encodedPath.replaceAll("%2F", "/");
            link.append(encodedPath);
          }
          return link.toString();
        }
      } catch (Exception e) {
        LOG.error("Error getting node thumbnail link " + node, e);
      }
      return StringUtils.EMPTY;
    }

    public String getViewLink() {
      Node node = null;
      try {
        node = node();
        if (node != null) {
          if (isSupportPreview()) {
            return event("ViewDocument", getId(), node.getUUID());
          } else {
            // TODO do we want "edit" functionality here? what could be else?
            return org.exoplatform.wcm.webui.Utils.getEditLink(node, false, false);
          }
        }
      } catch (Exception e) {
        LOG.error("Error getting node view link " + node, e);
      }
      return StringUtils.EMPTY;
    }

    public String getLink() throws Exception {
      return isSupportPreview() ? getViewLink() : getDownloadLink();
    }

    public String getCssClassIcon() {
      Node node = null;
      try {
        node = node();
        if (node != null) {
          return org.exoplatform.ecm.webui.utils.Utils.getNodeTypeIcon(node, "uiIcon64x64");
        }
      } catch (Exception e) {
        return "uiIcon64x64Templatent_file uiIcon64x64nt_file";
      }
      return StringUtils.EMPTY;
    }

    public boolean isSupportThumbnailView() throws Exception {
      return org.exoplatform.services.cms.impl.Utils.isSupportThumbnailView(getMimeType());
    }

    /**
     * @return <code>true</code> when node exists, <code>false</code> otherwise
     * @throws RepositoryException when error
     */
    public boolean isExists() throws RepositoryException {
      try {
        return node() != null;
      } catch (AccessDeniedException e) {
        return false;
      }
    }

    /**
     * Gets the summary.
     * 
     * @return the summary of Node. Return empty string if catch an exception.
     */
    public String getSummary() {
      Node node = null;
      try {
        node = node();
        if (node != null) {
          return org.exoplatform.wcm.ext.component.activity.listener.Utils.getSummary(node);
        }
      } catch (Exception e) {
        LOG.error("Error getting node summary " + node, e);
      }
      return StringUtils.EMPTY;
    }

    /**
     * @return attachment file node or <code>null</code>
     * @throws RepositoryException when strorage error
     * @throws NoSuchWorkspaceException when default workspace not found
     * @throws LoginException when login error
     */
    public Node getNode() throws RepositoryException {
      return node();
    }

    protected Node node() throws RepositoryException {
      Node node = this.node.get();
      if (node != null) {
        try {
          // check node/session is valid
          node.getIndex();
        } catch (InvalidItemStateException | AccessDeniedException e) {
          node = null;
        }
      }
      if (node == null) {
        SessionProvider sessionProvider = WCMCoreUtils.getUserSessionProvider();
        Session session = sessionProvider.getSession(getWorkspace(), repository);
        try {
          node = session.getNodeByUUID(fileUUID);
          TrashService trashService = WCMCoreUtils.getService(TrashService.class);
          if (trashService.isInTrash(node)) {
            node = null;
          } else {
            this.node.set(node);
          }
        } catch (ItemNotFoundException | AccessDeniedException e) {
          // node was removed or not accessible
        }
      }
      return node;
    }

    public String getFullPath() throws RepositoryException {
      Node node = node();
      StringBuilder path = new StringBuilder();
      if (node != null) {
        path.append(node.getSession().getWorkspace().getName()).append(":").append(node.getPath()).toString();
      }
      return path.toString();
    }
  }

  protected List<Attachment>     files;

  protected ManageableRepository repository;

  public OutlookAttachmentActivity() throws Exception {
    RepositoryService repositoryService = WCMCoreUtils.getService(RepositoryService.class);
    this.repository = repositoryService.getCurrentRepository();
  }

  public List<Attachment> getFiles() throws RepositoryException, RepositoryConfigurationException {
    if (this.files == null) {
      synchronized (this) {
        if (this.files == null) {
          ExoSocialActivity activity = getActivity();
          if (activity != null) {
            String filesLine = activity.getTemplateParams().get(FILES);
            if (filesLine != null && filesLine.length() > 0) {
              List<Attachment> files = new ArrayList<Attachment>();
              for (String fline : filesLine.split(",")) {
                files.add(parseAttachment(fline));
              }
              this.files = files;
            } else {
              throw new IllegalArgumentException("Activity files empty");
            }
          } else {
            throw new IllegalArgumentException("Activity not set");
          }
        }
      }
    }
    return Collections.unmodifiableList(files);
  }

  public void renderAttachmentPresentation(Node node) throws Exception {
    OutlookMessagePresentation uicontentpresentation = addChild(OutlookMessagePresentation.class, null, null);
    uicontentpresentation.setNode(node);

    String mimeType = node.getNode("jcr:content").getProperty("jcr:mimeType").getString();

    UIComponent fileComponent = uicontentpresentation.getUIComponent(mimeType);
    uicontentpresentation.renderUIComponent(fileComponent);
  }

  public String[] getSystemCommentBundle(Map<String, String> activityParams) {
    return org.exoplatform.wcm.ext.component.activity.listener.Utils.getSystemCommentBundle(activityParams);
  }

  public String[] getSystemCommentTitle(Map<String, String> activityParams) {
    return org.exoplatform.wcm.ext.component.activity.listener.Utils.getSystemCommentTitle(activityParams);
  }

  public String getWorkspace() {
    ExoSocialActivity activity = getActivity();
    if (activity != null) {
      return activity.getTemplateParams().get(WORKSPACE);
    } else {
      throw new IllegalArgumentException("Activity not set");
    }
  }

  public String getComment() {
    ExoSocialActivity activity = getActivity();
    if (activity != null) {
      return activity.getTemplateParams().get(COMMENT);
    } else {
      throw new IllegalArgumentException("Activity not set");
    }
  }

  public String toString() {
    ExoSocialActivity activity = getActivity();
    if (activity != null) {
      String title = activity.getTitle();
      return new StringBuilder(title != null ? title : activity.getName()).append(" (")
                                                                          .append(activity.getId())
                                                                          .append(", ")
                                                                          .append(activity.getPermaLink())
                                                                          .append(' ')
                                                                          .append(")")
                                                                          .toString();
    } else {
      return super.toString();
    }
  }

  Attachment parseAttachment(String line) {
    int i = line.indexOf('=');
    String fileUUID, name;
    if (i > 0) {
      fileUUID = line.substring(0, i);
      name = line.substring(++i);
    } else {
      fileUUID = line;
      name = null;
    }
    return new Attachment(fileUUID, name);
  }

  public static String attachmentString(String fileUUID, String name) {
    StringBuilder line = new StringBuilder();
    line.append(fileUUID);
    line.append('=');
    line.append(name);
    return line.toString();
  }

}
