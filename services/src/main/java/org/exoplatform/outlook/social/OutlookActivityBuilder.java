
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

import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.webui.activity.BaseUIActivity;
import org.exoplatform.social.webui.activity.BaseUIActivityBuilder;

/**
 * Created by The eXo Platform SAS
 * 
 * @author <a href="mailto:pnedonosko@exoplatform.com">Peter Nedonosko</a>
 * @version $Id: OutlookActivityBuilder.java 00000 Jul 26, 2016 pnedonosko $
 * 
 */
public class OutlookActivityBuilder extends BaseUIActivityBuilder {

  @Override
  protected void extendUIActivity(BaseUIActivity uiActivity, ExoSocialActivity activity) {

    // TODO why we need something here?
    // OutlookAttachmentActivity attachmentActivity = (OutlookAttachmentActivity) uiActivity;
    //
    // // set data into the UI component of activity
    // if (activity.getTemplateParams() != null) {
    // attachmentActivity.setUIActivityData(activity.getTemplateParams());
    // }

    // get node data
    // RepositoryService repositoryService = WCMCoreUtils.getService(RepositoryService.class);
    // Node contentNode = null;
    // try {
    // ManageableRepository manageRepo = repositoryService.getCurrentRepository();
    // SessionProvider sessionProvider = WCMCoreUtils.getUserSessionProvider();
    // for (String ws : manageRepo.getWorkspaceNames()) {
    // try {
    // if (StringUtils.isEmpty(attachmentActivity.getNodeUUID())) {
    // String contentLink = attachmentActivity.getContentLink();
    // String _ws = contentLink.split("/")[0];
    // String _repo = contentLink.split("/")[1];
    // String nodePath = contentLink.replace(_ws + "/" + _repo, "");
    // contentNode = (Node) sessionProvider.getSession(ws, manageRepo).getItem(nodePath);
    // attachmentActivity.setContentNode(contentNode);
    // return;
    // }
    // contentNode = sessionProvider.getSession(ws,
    // manageRepo).getNodeByUUID(attachmentActivity.getNodeUUID());
    // attachmentActivity.docPath = contentNode.getPath();
    // attachmentActivity.workspace = ws;
    // attachmentActivity.repository = manageRepo.toString();
    // break;
    // } catch (RepositoryException e) {
    // continue;
    // }
    // }
    // } catch (RepositoryException re) {
    // LOG.error("Can not get the repository. ", re);
    // }
    //
    // attachmentActivity.setContentNode(contentNode);
  }

}