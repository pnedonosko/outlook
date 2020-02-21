import { IConvertMessageConfig } from "./convert-message.types";

export const convertToWikiConfig: IConvertMessageConfig = {
  header: "Convert to Wiki Page",
  description:
    "This message will be converted to a page in the general intranet wiki or in the selected space's wiki.",
  fields: {
    title: {
      label: "Page title",
      description: "The title of the wiki page"
    },
    content: {
      label: "Page content",
      description: "This will be the body of the wiki page"
    },
    spaces: {
      description: "If selected, your message will be converted to a page in this space's wiki.",
      isOptional: true
    }
  }
};

export const convertToForumConfig: IConvertMessageConfig = {
  header: "Convert to Forum Topic",
  description: "This message will be converted to a new forum topic in the target space.",
  fields: {
    title: {
      label: "Topic title",
      description: "This will be the title of your forum topic"
    },
    content: {
      label: "Content",
      description: "This will be the body of the forum topic"
    },
    spaces: {
      description: "Your message will be converted to a forum topic in the space's forum.",
      isOptional: false
    }
  }
};

export const convertToActivityConfig: IConvertMessageConfig = {
  header: "Convert to Activity",
  description:
    "This message can be converted to a new post on your personal activity stream or on the stream of the selected space.",
  fields: {
    content: {
      label: "Activity content",
      description: "This will be the body of the activity stream post",
      withHeader: true
    },
    spaces: {
      description: "If selected, your message will be posted on the space's activity stream.",
      isOptional: true
    }
  }
};
