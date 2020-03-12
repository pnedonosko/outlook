import * as React from "react";
import "./index.less";
import { IContainerProps } from "../../OutlookApp";
import TextMessage from "../../components/TextMessage";
import SpacesSelect from "../../components/SpacesSelect";
import { DefaultButton, PrimaryButton } from "office-ui-fabric-react/lib/Button";
import { Translation } from "react-i18next";

interface IPostActivityState {
  message: string;
  selectedSpace: any;
  // any will be replaced by space interface
}

class PostActivity extends React.Component<IContainerProps, IPostActivityState> {
  state: IPostActivityState = { message: "", selectedSpace: undefined };

  getMessage = (text: string) => {
    this.setState({ message: text });
  };

  getSpace = (space: any) => {
    this.setState({ selectedSpace: space });
  };

  render(): JSX.Element {
    return (
      <Translation>
        {t => (
          <div className="outlook-command-container outlook-save-attachments">
            <h4 className="outlook-title">{t("Outlook.command.postStatus")}</h4>
            <div className="outlook-description">{t("Outlook.postStatusDescription")}</div>
            <TextMessage
              label={t("Outlook.activityMessage")}
              description={t("Outlook.activityTextDescription")}
              placeholder={t("Outlook.activityMessagePlaceholder")}
              onTextChange={this.getMessage}
            />
            <SpacesSelect
              isOptional
              description={t("Outlook.activityTargetSpaceDescription")}
              onSelectSpace={this.getSpace}
              user={this.props.userUrl}
              userName={this.props.userName}
            />
            <PrimaryButton text={t("Outlook.post")} />
            <DefaultButton text={t("Outlook.cancel")} />
          </div>
        )}
      </Translation>
    );
  }
}

export default PostActivity;
