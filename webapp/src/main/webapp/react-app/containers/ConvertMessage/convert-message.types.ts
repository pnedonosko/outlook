export enum ConvertMessageTypes {
  "Wiki" = "convertToWikiConfig",
  "Forum" = "convertToForumConfig",
  "Activity" = "convertToActivityConfig"
}

export interface IConvertField {
  label: string;
  description: string;
  withHeader?: boolean;
}

export interface IConvertMessageConfig {
  header: string;
  description: string;
  fields: {
    content: IConvertField;
    spaces: {
      description: string;
      isOptional: boolean;
    };
    title?: IConvertField;
  };
}
