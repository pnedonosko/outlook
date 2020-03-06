import * as React from "react";
import "./index.less";
import { IContainerProps } from "../../OutlookApp";
import SpacesSelect from "../../components/SpacesSelect";
import { PrimaryButton, DefaultButton, IconButton } from "office-ui-fabric-react/lib/Button";
import { TextField, ITextFieldProps } from "office-ui-fabric-react/lib/TextField";
import { getId } from "office-ui-fabric-react/lib/Utilities";
import { Stack } from "office-ui-fabric-react/lib/Stack";
import { ConvertMessageTypes, IConvertMessageConfig } from "./convert-message.types";
import { Spinner, SpinnerSize, IDropdownOption, MessageBar, MessageBarType } from "office-ui-fabric-react";
import TextMessage from "../../components/TextMessage";
import axios from "axios";
import ContentEditable from "react-contenteditable";

const qs = require("querystring");

export interface IConvertMessageProps extends IContainerProps {
  type: ConvertMessageTypes;
}

interface IConvertMessageState {
  config: IConvertMessageConfig;
  editMessage: boolean;
  messageContent?: string;
  messageTitle?: string;
  error?: string;
  selectedSpace?: IDropdownOption;
  activityMessage?: string;
  networkError?: string;
}

class ConvertMessage extends React.Component<IConvertMessageProps, IConvertMessageState> {
  state: IConvertMessageState = { config: null, editMessage: false };
  labelId: string = getId("label");
  iconButtonId: string = getId("iconButton");
  contentEditable = React.createRef<HTMLElement>();

  async componentDidMount() {
    await this.getComponentConfig(this.props.type);

    Office.onReady(() => {
      let specialCharacters = new RegExp(/[%=:@\/\\\|\^#;\[\]{}<>\*'"\+\?&]/g);
      // Not allowed:
      // % = : @ / \ | ^ # ; [ ] { } < > * ' " + ? &
      let subject = Office.context.mailbox.item.subject;
      this.setState({ messageTitle: subject.replace(specialCharacters, " ") });
      Office.context.mailbox.item.body.getAsync("html", async => {
        this.setState({ messageContent: async.value });
      });
    });
  }

  getComponentConfig = async (configType: string): Promise<void> => {
    // download component config in depending on type from props
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

  getSpace = (space: IDropdownOption) => {
    this.setState({ selectedSpace: space });
  };

  getMessage = (text: string) => {
    this.setState({ activityMessage: text });
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
        className="editButton"
      />
    </Stack>
  );

  handleContentEdit = (event: { target: { value: string } }) => {
    this.setState({ messageContent: event.target.value });
  };

  convertMessage = () => {
    const requestBody = {
      groupId: this.state.selectedSpace.key,
      messageId: Office.context.mailbox.item.itemId,
      title: this.state.activityMessage,
      Subject: this.state.messageTitle,
      body: this.state.messageContent,
      created: Office.context.mailbox.item.dateTimeCreated,
      modified: Office.context.mailbox.item.dateTimeModified,
      userName: Office.context.mailbox.userProfile.displayName,
      userEmail: Office.context.mailbox.userProfile.emailAddress,
      fromName: Office.context.mailbox.item.from.displayName,
      fromEmail: Office.context.mailbox.item.from.emailAddress
    };
    axios
      .post(
        `${this.props.services.userServices.href}${this.props.userName}/activity`,
        qs.stringify(requestBody),
        {
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          }
        }
      )
      .then(res => console.log(res))
      .catch(() => this.setState({ networkError: "Unable convert to activity" }));

    // Alternative post method using another http client
    //
    // const fetchBody = Object.keys(requestBody).map((key) => {
    //   return encodeURIComponent(key) + '=' + encodeURIComponent(requestBody[key]);
    // }).join('&');
    // return fetch(`${this.props.services.userServices.href}${this.props.userName}/activity`, {
    //   method: 'POST',
    //     headers: {
    //       'Content-Type': 'application/x-www-form-urlencoded'
    //     },
    //     body: fetchBody
    // }).then(res => {
    //   if (res && res.ok) {
    //     return res;
    //   } else {
    //     console.log('problem');
    //   }
    // }).then(data => console.log(data));
  };

  render(): JSX.Element {
    const displayedComponent = this.state.config ? (
      <div className="outlook-command-container">
        {this.state.networkError ? (
          <MessageBar
            messageBarType={MessageBarType.error}
            isMultiline={false}
            dismissButtonAriaLabel="Close"
          >
            {this.state.networkError}
          </MessageBar>
        ) : null}
        <h4 className="outlook-title">{this.state.config.header}</h4>
        <div className="outlook-description">{this.state.config.description}</div>
        {this.props.type === ConvertMessageTypes.Activity ? (
          <TextMessage
            label="Message (optional)"
            description="Leave a message upon sharing this email"
            onTextChange={this.getMessage}
          />
        ) : (
          <>
            <TextField label={this.state.config.fields.title.label} defaultValue={this.state.messageTitle} />
            <div>{this.state.config.fields.title.description}</div>
          </>
        )}
        {this.state.messageContent ? (
          <>
            {this.props.type === ConvertMessageTypes.Activity ? (
              <TextField
                aria-labelledby={this.labelId}
                label={this.state.config.fields.content.label}
                onRenderLabel={this.renderLabel}
                value={this.state.messageTitle}
                disabled={!this.state.editMessage}
                className="editableSubject"
                onChange={(_, newValue) => this.setState({ messageTitle: newValue })}
              />
            ) : null}
            <ContentEditable
              innerRef={this.contentEditable}
              html={this.state.messageContent}
              disabled={!this.state.editMessage}
              onChange={this.handleContentEdit}
              tagName="article"
              className="editableContent"
            />
            <div className="ms-TextField-description">{this.state.config.fields.content.description}</div>
          </>
        ) : null}
        <SpacesSelect
          isOptional={this.state.config.fields.spaces.isOptional}
          description={this.state.config.fields.spaces.description}
          onSelectSpace={this.getSpace}
          user={this.props.services.userServices.href}
          userName={this.props.userName}
        />
        <PrimaryButton text="Convert" onClick={this.convertMessage} />
        <DefaultButton text="Cancel" style={{ marginLeft: "10px" }} />
      </div>
    ) : (
      <Spinner size={SpinnerSize.large} />
    );

    return displayedComponent;
  }
}

export default ConvertMessage;
