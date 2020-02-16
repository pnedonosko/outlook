import * as React from "react";
import "./index.less";
import SpacesSelect from "../../components/SpacesSelect/SpacesSelect";
import { Panel } from "office-ui-fabric-react/lib/Panel";
import { DefaultButton } from "office-ui-fabric-react/lib/Button";
import SelectAttachments from "./SelectAttachments";

export interface IAttachment {
  name: string;
  size: string;
  id: string;
};

interface ISaveAttachmentsState {
  isPanelOpen: boolean;
  attachments?: IAttachment[];
};

class SaveAttachments extends React.Component {
  state: ISaveAttachmentsState = { isPanelOpen: false, attachments: [] };

  constructor(props) {
    super(props);
    console.log(props);
  }

  componentDidMount() {
    // getting letter attachment from Office namespace
    this.setState({
      attachments: [
        { name: 'first-picture.img', size: '20 KB', id: '1' },
        { name: 'second-picture.png', size: '14 KB', id: '2' }
      ]
    });
  }

  togglePanel = () => {
    this.setState((prevState: ISaveAttachmentsState) => ({ isPanelOpen: !prevState.isPanelOpen }));
  }

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
            <div className="outlook-description">Here you can save attachments as documents inside the intranet.</div>
            <SelectAttachments attachments={this.state.attachments} />
            <SpacesSelect />
          </div>
        </Panel>
      </>
    );
  }
}

export default SaveAttachments;
