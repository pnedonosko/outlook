import * as React from "react";
import "./index.less";
import SpacesSelect from "../../components/SpacesSelect";
import { Panel } from "office-ui-fabric-react/lib/Panel";
import { PrimaryButton, DefaultButton } from "office-ui-fabric-react/lib/Button";
import SelectAttachments from "./SelectAttachments";
import TextMessage from "../../components/TextMessage";
import { Spinner, SpinnerSize } from "office-ui-fabric-react/lib/Spinner";
import { MessageBar, MessageBarType } from "office-ui-fabric-react";
import DestinationFolder from "./DestinationFolder";
import AddFolder from "./AddFolder";

interface ISaveAttachmentsState {
  isPanelOpen: boolean;
  attachmentSelection: string;
  attachments?: Office.AttachmentDetails[];
  message?: string;
  selectedSpace?: any;
  showFolderDialog?: boolean;
}

class SaveAttachments extends React.Component {
  state: ISaveAttachmentsState = { isPanelOpen: false, attachmentSelection: "No items selected" };

  constructor(props) {
    super(props);
    console.log(props);
  }

  componentDidMount() {
    // getting letter attachment from Office namespace: Office.context.mailbox.item.attachments
    this.setState({
      attachments: [
        { name: "first-picture.img", size: 20, id: "1", attachmentType: "file" },
        { name: "second-picture.png", size: 14, id: "2", attachmentType: "file" }
      ]
    });
  }

  togglePanel = () => {
    this.setState((prevState: ISaveAttachmentsState) => ({ isPanelOpen: !prevState.isPanelOpen }));
  };

  getSelection = (details: string) => {
    this.setState({ attachmentSelection: details });
  };

  getMessage = (text: string) => {
    this.setState({ message: text });
  };

  getSpace = (space: any) => {
    console.log(space);
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
          <div className="outlook-command-container outlook-save-attachments">
            <h4 className="outlook-title">Save Attachments</h4>
            <div className="outlook-description">
              Here you can save attachments as documents inside the intranet.
            </div>
            <SelectAttachments attachments={this.state.attachments} onSelectItem={this.getSelection} />
            <TextMessage
              label="Include a message(optional)"
              description="This message will be included in the activity stream post about the saved attachment."
              onTextChange={this.getMessage}
            />
            <SpacesSelect
              isOptional
              description="Select the space where the attachments will be saved"
              onSelectSpace={this.getSpace}
            />
            <DestinationFolder onShowDialog={() => this.setState({ showFolderDialog: true })} />
            {this.state.showFolderDialog ? (
              <AddFolder
                onAddFolder={(name: string) => this.addNewFolder(name)}
                onCloseFolder={() => this.setState({ showFolderDialog: false })}
              />
            ) : null}
            <PrimaryButton onClick={this.saveAttachments} text="Save" />
            <DefaultButton text="Cancel" />
          </div>
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

    return (
      <>
        {/* Button and Panel components will be deleted in the future. For testing purposes. */}
        <DefaultButton text="Open panel" onClick={this.togglePanel} />
        <Panel
          headerText="Sample panel"
          isOpen={this.state.isPanelOpen}
          onDismiss={this.togglePanel}
          closeButtonAriaLabel="Close"
        >
          {saveAttachmentContent}
        </Panel>
      </>
    );
  }
}

export default SaveAttachments;
