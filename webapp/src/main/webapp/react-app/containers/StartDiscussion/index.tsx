import * as React from "react";
import "./index.less";
import { DefaultButton, PrimaryButton } from "office-ui-fabric-react/lib/Button";
import { TextField } from "office-ui-fabric-react/lib/TextField";
import TextMessage from "../../components/TextMessage";
import SpacesSelect from "../../components/SpacesSelect";
import { IContainerProps } from "../../OutlookApp";
import { Translation } from "react-i18next";
import { IDropdownOption, MessageBar, MessageBarType } from "office-ui-fabric-react";

interface IStartDiscussionState {
  message?: string;
  selectedSpace?: IDropdownOption;
  errorMessage?: string;
}

class StartDiscussion extends React.Component<IContainerProps, IStartDiscussionState> {
  state: IStartDiscussionState = {};

  getMessage = (text: string) => {
    this.setState({ message: text });
  };

  getSpace = (space: any) => {
    this.setState({ selectedSpace: space });
  };

  /* should be replaced by request to start discussion */
  startDiscussion = () => {
    this.setState({ errorMessage: "Unable to start discussion" });
  };

  render() {
    return (
      <Translation>
        {t => (
          <div className="outlook-command-container outlook-save-attachments">
            {this.state.errorMessage ? (
              <MessageBar
                messageBarType={MessageBarType.error}
                isMultiline={false}
                dismissButtonAriaLabel="Close"
              >
                {this.state.errorMessage}
              </MessageBar>
            ) : null}
            <h4 className="outlook-title">{t("Outlook.command.startDiscussion")}</h4>
            <div className="outlook-description">{t("Outlook.startDiscussionDescription")}</div>
            <TextField label="Topic title" description={t("Outlook.forumTopicNameDescription")} />
            <TextMessage
              label={t("Outlook.forumTopicText")}
              description={t("Outlook.forumTopicTextDescription")}
              placeholder={t("Outlook.forumTopicTextPlaceholder")}
              onTextChange={this.getMessage}
            />
            <SpacesSelect
              isOptional={false}
              description={t("Outlook.forumNewTopicTargetSpaceDescription")}
              onSelectSpace={this.getSpace}
              user={this.props.userUrl}
              userName={this.props.userName}
            />
            <PrimaryButton text={t("Outlook.start")} onClick={this.startDiscussion} />
            <DefaultButton text={t("Outlook.cancel")} />
          </div>
        )}
      </Translation>
    );
  }
}

export default StartDiscussion;
