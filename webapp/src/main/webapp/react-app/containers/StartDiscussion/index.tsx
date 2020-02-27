import * as React from "react";
import "./index.less";
import { DefaultButton, PrimaryButton } from "office-ui-fabric-react/lib/Button";
import { TextField } from "office-ui-fabric-react/lib/TextField";
import TextMessage from "../../components/TextMessage";
import SpacesSelect from "../../components/SpacesSelect";
import { IContainerProps } from "../../OutlookApp";

interface IStartDiscussionState {
  message?: string;
  selectedSpace?: any;
}

class StartDiscussion extends React.Component<IContainerProps, IStartDiscussionState> {
  state: IStartDiscussionState = {};

  getMessage = (text: string) => {
    this.setState({ message: text });
  };

  getSpace = (space: any) => {
    this.setState({ selectedSpace: space });
  };

  render() {
    return (
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
          user={this.props.user}
        />
        <PrimaryButton text="Submit" />
        <DefaultButton text="Cancel" />
      </div>
    );
  }
}

export default StartDiscussion;
