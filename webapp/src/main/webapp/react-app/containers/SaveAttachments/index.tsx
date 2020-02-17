import * as React from "react";
import "./index.less";
import SpacesSelect from "../../components/SpacesSelect";
import { Panel } from "office-ui-fabric-react/lib/Panel";
import { DefaultButton } from "office-ui-fabric-react/lib/Button";
import SelectAttachments from "./SelectAttachments";
import TextMessage from "../../components/TextMessage";

interface ISaveAttachmentsState {
  isPanelOpen: boolean;
  selectionDetails: string;
  attachments?: Office.AttachmentDetails[];
}

class SaveAttachments extends React.Component {
  state: ISaveAttachmentsState = { isPanelOpen: false, selectionDetails: "No items selected" };

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
    this.setState({ selectionDetails: details });
  };

  render(): JSX.Element {
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
          <div className="outlook-command-container outlook-save-attachments">
            <h4 className="outlook-title">Save Attachments</h4>
            <div className="outlook-description">
              Here you can save attachments as documents inside the intranet.
            </div>
            <SelectAttachments attachments={this.state.attachments} onSelectItem={this.getSelection} />
            <TextMessage
              label="Include a message(optional)"
              description="This message will be included in the activity stream post about the saved attachment."
            />
            <SpacesSelect />
          </div>
        </Panel>
      </>
    );
  }
}

export default SaveAttachments;
