import { IConvertMessageConfig } from "./convert-message.types";

export const convertToWikiConfig: IConvertMessageConfig = {
  header: "Outlook.command.convertToWiki",
  description: "Outlook.convertToWikiDescription",
  fields: {
    title: {
      label: "Outlook.wikiPageTitle",
      description: "Outlook.wikiPageTitleDescription"
    },
    content: {
      label: "Outlook.wikiPageText",
      description: "Outlook.wikiPageTextDescription"
    },
    spaces: {
      description: "Outlook.wikiTargetSpaceDescription",
      isOptional: true
    }
  }
};

export const convertToForumConfig: IConvertMessageConfig = {
  header: "Outlook.command.convertToForum",
  description: "Outlook.convertToForumDescription.",
  fields: {
    title: {
      label: "Outlook.forumTopicName",
      description: "Outlook.forumTopicNameDescription"
    },
    content: {
      label: "Outlook.forumTopicText",
      description: "Outlook.forumTopicTextDescription"
    },
    spaces: {
      description: "Outlook.forumTargetSpaceDescription",
      isOptional: false
    }
  }
};

export const convertToActivityConfig: IConvertMessageConfig = {
  header: "Outlook.command.convertToStatus",
  description: "Outlook.convertToStatusDescription",
  fields: {
    content: {
      label: "Outlook.activityText",
      description: "Outlook.activityTextDescription",
      withHeader: true
    },
    spaces: {
      description: "Outlook.activityTargetSpaceDescription",
      isOptional: true
    }
  }
};
