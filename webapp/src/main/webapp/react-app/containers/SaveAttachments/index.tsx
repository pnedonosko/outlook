import * as React from "react";
import "./index.less";
import SpacesSelect from "../../components/SpacesSelect";
import { PrimaryButton, DefaultButton } from "office-ui-fabric-react/lib/Button";
import SelectAttachments from "./SelectAttachments";
import TextMessage from "../../components/TextMessage";
import { Spinner, SpinnerSize } from "office-ui-fabric-react/lib/Spinner";
import { MessageBar, MessageBarType } from "office-ui-fabric-react";
import DestinationFolder from "./DestinationFolder";
import AddFolder from "./AddFolder";
import { IContainerProps } from "../../OutlookApp";
import { Translation } from "react-i18next";

interface ISaveAttachmentsState {
  attachmentSelection: string;
  // any should be replaced by Office.AttachmentDetails
  attachments?: any[];
  message?: string;
  selectedSpace?: any;
  showFolderDialog?: boolean;
}

class SaveAttachments extends React.Component<IContainerProps, ISaveAttachmentsState> {
  state: ISaveAttachmentsState = { attachmentSelection: "No items selected" };

  componentDidMount() {
    // getting letter attachment from Office namespace: Office.context.mailbox.item.attachments
    this.setState({
      attachments: [
        { name: "first-picture.img", size: 20, id: "1", attachmentType: "file" },
        { name: "second-picture.png", size: 14, id: "2", attachmentType: "file" }
      ]
    });
  }

  getSelection = (details: string) => {
    this.setState({ attachmentSelection: details });
  };

  getMessage = (text: string) => {
    this.setState({ message: text });
  };

  getSpace = (space: any) => {
    this.setState({ selectedSpace: space });
  };

  addNewFolder = (name: string) => {
    // POST new folder
    console.log(name);
    this.setState({ showFolderDialog: false });
  };

  saveAttachments = () => {
    // request to save attachments
  };

  render(): JSX.Element {
    let saveAttachmentContent = <Spinner size={SpinnerSize.large} />;
    if (this.state.attachments) {
      saveAttachmentContent =
        this.state.attachments.length > 0 ? (
          <Translation>
            {t => (
              <div className="outlook-command-container outlook-save-attachments">
                <h4 className="outlook-title">{t("Outlook.command.saveAttachment")}</h4>
                <div className="outlook-description">{t("Outlook.saveAttachmentDescription")}</div>
                <SelectAttachments attachments={this.state.attachments} onSelectItem={this.getSelection} />
                <TextMessage
                  label={t("Outlook.commentAttachment")}
                  description={t("Outlook.commentAttachmentDescription")}
                  onTextChange={this.getMessage}
                />
                <SpacesSelect
                  isOptional
                  description={t("Outlook.selectSaveSpaceDescription")}
                  onSelectSpace={this.getSpace}
                  user={this.props.userUrl}
                  userName={this.props.userName}
                />
                <DestinationFolder
                  onShowDialog={() => this.setState({ showFolderDialog: true })}
                  space={this.state.selectedSpace ? this.state.selectedSpace : null}
                />
                {this.state.showFolderDialog ? (
                  <AddFolder
                    onAddFolder={(name: string) => this.addNewFolder(name)}
                    onCloseFolder={() => this.setState({ showFolderDialog: false })}
                  />
                ) : null}
                <PrimaryButton onClick={this.saveAttachments} text={t("Outlook.save")} />
                <DefaultButton text={t("Outlook.cancel")} />
              </div>
            )}
          </Translation>
        ) : (
          <MessageBar
            messageBarType={MessageBarType.warning}
            isMultiline={false}
            dismissButtonAriaLabel="Close"
          >
            Message without attachments
          </MessageBar>
        );
    }

    return saveAttachmentContent;
  }
}

export default SaveAttachments;
