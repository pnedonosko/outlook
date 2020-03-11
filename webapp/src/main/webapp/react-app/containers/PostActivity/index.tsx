import * as React from "react";
import "./index.less";
import { IContainerProps } from "../../OutlookApp";
import TextMessage from "../../components/TextMessage";
import SpacesSelect from "../../components/SpacesSelect";
import { DefaultButton, PrimaryButton } from "office-ui-fabric-react/lib/Button";

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
      <div className="outlook-command-container outlook-save-attachments">
        <h4 className="outlook-title">Post a Status Update</h4>
        <div className="outlook-description">
          Post your status update on your personal activity stream or in the target space.
        </div>
        <TextMessage
          label="Status update"
          description="This will be the body of the activity stream post"
          placeholder="Type in your message here"
          onTextChange={this.getMessage}
        />
        <SpacesSelect
          isOptional
          description="If selected, your message will be posted on the space's activity stream."
          onSelectSpace={this.getSpace}
          user={this.props.userUrl}
          userName={this.props.userName}
        />
        <PrimaryButton text="Share" />
        <DefaultButton text="Cancel" />
      </div>
    );
  }
}

export default PostActivity;
