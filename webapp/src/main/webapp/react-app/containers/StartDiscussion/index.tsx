import * as React from "react";
import "./index.less";
import { DefaultButton, PrimaryButton } from "office-ui-fabric-react/lib/Button";
import { Panel } from "office-ui-fabric-react/lib/Panel";
import { TextField } from "office-ui-fabric-react/lib/TextField";
import TextMessage from "../../components/TextMessage";
import SpacesSelect from "../../components/SpacesSelect";

interface IStartDiscussionState {
  isPanelOpen: boolean;
  message?: string;
  user?: any;
  selectedSpace?: any;
}

class StartDiscussion extends React.Component {
  state: IStartDiscussionState = { isPanelOpen: false };

  togglePanel = () => {
    this.setState((prevState: IStartDiscussionState) => ({ isPanelOpen: !prevState.isPanelOpen }));
  };

  getMessage = (text: string) => {
    this.setState({ message: text });
  };

  getSpace = (space: any) => {
    this.setState({ selectedSpace: space });
  };

  render() {
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
            <h4 className="outlook-title">Start a Discussion</h4>
            <div className="outlook-description">Start a new discussion in the forum of a space.</div>
            <TextField label="Topic title" />
            <div>This will be the title of your forum topic</div>
            <TextMessage
              label="Content"
              description="This will be the body of the forum post"
              placeholder="Type in your new forum post here"
              onTextChange={this.getMessage}
            />
             <SpacesSelect
              isOptional={false}
              description="Your new forum discussion will be posted in the space's forum"
              onSelectSpace={this.getSpace}
              user={this.state.user}
            />
            <PrimaryButton text="Submit" />
            <DefaultButton text="Cancel" />
          </div>
        </Panel>
      </>
    );
  }
}

export default StartDiscussion;
