import * as React from "react";
import "./index.less";
import { IContainerProps } from "../../OutlookApp";
import SpacesSelect from "../../components/SpacesSelect";
import { PrimaryButton, DefaultButton, IconButton } from "office-ui-fabric-react/lib/Button";
import { TextField, ITextFieldProps } from "office-ui-fabric-react/lib/TextField";
import { getId } from "office-ui-fabric-react/lib/Utilities";
import { Stack } from "office-ui-fabric-react/lib/Stack";
import { ConvertMessageTypes, IConvertMessageConfig } from "./convert-message.types";
import { Spinner, SpinnerSize } from "office-ui-fabric-react";

export interface IConvertMessageProps extends IContainerProps {
  type: ConvertMessageTypes;
}

interface IConvertMessageState {
  config: IConvertMessageConfig;
  editMessage: boolean;
  messageContent?: any;
  messageTitle?: string;
  error?: string;
  selectedSpace?: any;
}

class ConvertMessage extends React.Component<IConvertMessageProps, IConvertMessageState> {
  state: IConvertMessageState = { config: null, editMessage: false };
  labelId: string = getId("label");
  iconButtonId: string = getId("iconButton");

  async componentDidMount() {
    await this.getComponentConfig(this.props.type);

    // get message title from Office.context.mailbox.item.subject
    let specialCharacters = new RegExp(/[%=:@\/\\\|\^#;\[\]{}<>\*'"\+\?&]/g);
    // Not allowed:
    // % = : @ / \ | ^ # ; [ ] { } < > * ' " + ? &
    this.setState({ messageTitle: "Title of current message".replace(specialCharacters, " ") });
    // get message content
    this.setState({ messageContent: "Message body" });
  }

  getComponentConfig = async (configType: string): Promise<void> => {
    import(`./convert-message.config`)
      .then(configs =>
        this.setState({
          config: configs[configType]
        })
      )
      .catch(() =>
        this.setState({
          error: `Can't load config for component`
        })
      );
  };

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

  render(): JSX.Element {
    const displayedComponent = this.state.config ? (
      <div className="outlook-command-container outlook-save-attachments">
        <h4 className="outlook-title">{this.state.config.header}</h4>
        <div className="outlook-description">{this.state.config.description}</div>
        <TextField label={this.state.config.fields.title.label} defaultValue={this.state.messageTitle} />
        <div>{this.state.config.fields.title.description}</div>
        {this.state.messageContent ? (
          <TextField
            aria-labelledby={this.labelId}
            label={this.state.config.fields.content.label}
            onRenderLabel={this.renderLabel}
            description={this.state.config.fields.content.description}
            multiline
            rows={3}
            disabled={!this.state.editMessage}
            defaultValue={this.state.messageContent ? this.state.messageContent : "rgr"}
          />
        ) : null}
        <SpacesSelect
          isOptional={this.state.config.fields.spaces.isOptional}
          description={this.state.config.fields.spaces.description}
          onSelectSpace={this.getSpace}
          user={this.props.user}
        />
        <PrimaryButton text="Convert" />
        <DefaultButton text="Cancel" />
      </div>
    ) : (
      <Spinner size={SpinnerSize.large} />
    );

    return displayedComponent;
  }
}

export default ConvertMessage;
