import * as React from "react";
import "./index.less";
import { IContainerProps } from "../../OutlookApp";
import SpacesSelect from "../../components/SpacesSelect";
import { PrimaryButton, DefaultButton, IconButton } from "office-ui-fabric-react/lib/Button";
import { TextField, ITextFieldProps } from "office-ui-fabric-react/lib/TextField";
import { getId } from "office-ui-fabric-react/lib/Utilities";
import { Stack } from "office-ui-fabric-react/lib/Stack";

interface IConvertToWikiState {
  wikiTitle: string;
  editMessage: boolean;
  message?: any;
}

class ConvertToWiki extends React.Component<IContainerProps> {
  state: IConvertToWikiState = { wikiTitle: "Title", editMessage: false };
  labelId: string = getId("label");
  iconButtonId: string = getId("iconButton");

  componentDidMount() {
    // get message title from Office.context.mailbox.item.subject
    let specialCharacters = new RegExp(/[%=:@\/\\\|\^#;\[\]{}<>\*'"\+\?&]/g);
    // Not allowed:
    // % = : @ / \ | ^ # ; [ ] { } < > * ' " + ? &
    this.setState({ wikiTitle: "Title of current message".replace(specialCharacters, " ") });
    // get message content
    this.setState({ message: "Message body" });
  }

  getSpace = (space: any) => {
    this.setState({ selectedSpace: space });
  };

  renderLabel = (props: ITextFieldProps): JSX.Element => (
    <Stack horizontal verticalAlign="center">
      <span id={this.labelId}>{props.label}</span>
      <IconButton
        id={this.iconButtonId}
        iconProps={{ iconName: "Label" }}
        title="Edit"
        ariaLabel="Edit"
        onClick={() => this.setState({ editMessage: !this.state.editMessage })}
        styles={{ root: { marginBottom: -3 } }}
      />
    </Stack>
  );

  render() {
    return (
      <div className="outlook-command-container outlook-save-attachments">
        <h4 className="outlook-title">Convert to Wiki Page</h4>
        <div className="outlook-description">
          This message will be converted to a page in the general intranet wiki or in the selected
          space&apos;s wiki.
        </div>
        <TextField label="Page title" defaultValue={this.state.wikiTitle} />
        <div>The title of the wiki page</div>
        {this.state.message ? (
          <TextField
            aria-labelledby={this.labelId}
            label="Page content"
            onRenderLabel={this.renderLabel}
            description="This will be the body of the wiki page"
            multiline
            rows={3}
            disabled={!this.state.editMessage}
            defaultValue={this.state.message ? this.state.message : "rgr"}
          />
        ) : null}
        <SpacesSelect
          isOptional
          description="If selected, your message will be converted to a page in this space's wiki."
          onSelectSpace={this.getSpace}
          user={this.props.user}
        />
        <PrimaryButton text="Convert" />
        <DefaultButton text="Cancel" />
      </div>
    );
  }
}

export default ConvertToWiki;
