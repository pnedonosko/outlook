import * as React from "react";
import axios from "axios";
import ContentEditable from "react-contenteditable";
import { Translation } from "react-i18next";

import "./index.less";
import { IContainerProps } from "../../OutlookApp";
import { ConvertMessageTypes, IConvertMessageConfig } from "./convert-message.types";
import formatISODate from "../../utils/formatIsoDate";

import SpacesSelect from "../../components/SpacesSelect";
import TextMessage from "../../components/TextMessage";

import { PrimaryButton, DefaultButton, IconButton } from "office-ui-fabric-react/lib/Button";
import { TextField, ITextFieldProps } from "office-ui-fabric-react/lib/TextField";
import { getId } from "office-ui-fabric-react/lib/Utilities";
import { Stack } from "office-ui-fabric-react/lib/Stack";
import { Spinner, SpinnerSize, IDropdownOption, MessageBar, MessageBarType } from "office-ui-fabric-react";

export interface IConvertMessageProps extends IContainerProps {
  type: ConvertMessageTypes;
}

export interface IPostActivityDto {
  messageId: string;
  title: string;
  subject: string;
  body: string;
  created: string;
  modified: string;
  userName: string;
  userEmail: string;
  fromName: string;
  fromEmail: string;
}

interface IPostedActivity {
  link: string;
  space: boolean;
  targetName: string;
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
  successMessage?: string;
  postedActivity?: IPostedActivity;
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

  getSpace = (space: IDropdownOption): void => {
    this.setState({ selectedSpace: space });
  };

  getMessage = (text: string): void => {
    this.setState({ activityMessage: text });
  };

  renderLabel = (props: ITextFieldProps): JSX.Element => (
    <Stack horizontal verticalAlign="center" className="editLabel">
      <label id={this.labelId} className="ms-Label">
        {props.label}
      </label>
      <IconButton
        id={this.iconButtonId}
        iconProps={{ iconName: "Label", styles: { root: { fontSize: "12px" } } }}
        title="Edit"
        ariaLabel="Edit"
        onClick={() => this.setState({ editMessage: !this.state.editMessage })}
        className="labelIcon"
      />
    </Stack>
  );

  handleContentEdit = (event: { target: { value: string } }) => {
    this.setState({ messageContent: event.target.value });
  };

  convertMessage = () => {
    const requestBody: IPostActivityDto = {
      messageId: Office.context.mailbox.item.itemId,
      title: this.state.activityMessage ? this.state.activityMessage : "",
      subject: this.state.messageTitle,
      body: this.state.messageContent,
      created: formatISODate(Office.context.mailbox.item.dateTimeCreated),
      modified: formatISODate(Office.context.mailbox.item.dateTimeModified),
      userName: Office.context.mailbox.userProfile.displayName,
      userEmail: Office.context.mailbox.userProfile.emailAddress,
      fromName: Office.context.mailbox.item.from.displayName,
      fromEmail: Office.context.mailbox.item.from.emailAddress
    };
    const params = new URLSearchParams();
    for (let key in requestBody) {
      params.append(key, requestBody[key]);
    }
    this.state.selectedSpace
      ? axios
          .get(this.props.userUrl)
          .then(({ data }) => {
            axios.get(data._links.parent.href).then(res => {
              axios
                .post(
                  `${res.data._links.spaceServices.href}${
                    this.state.selectedSpace.key.toString().split("/")[2]
                  }/activity`,
                  params
                )
                .then(res =>
                  this.setState({
                    successMessage: "The activity was successfully posted in spaces stream.",
                    postedActivity: res.data.activityStatus
                  })
                )
                .catch(() => this.setState({ networkError: "Unable convert to activity" }));
            });
          })
          .catch(() => this.setState({ networkError: "Unable convert to activity" }))
      : axios
          .post(`${this.props.userUrl}/${this.props.userName}/activity`, params)
          .then(({ data }) =>
            this.setState({
              successMessage: "The activity was successfully posted in spaces stream.",
              postedActivity: data.activityStatus
            })
          )
          .catch(() => this.setState({ networkError: "Unable convert to activity" }));
  };

  render(): JSX.Element {
    const displayedComponent = this.state.config ? (
      <Translation>
        {t => (
          <div className="outlook-command-container">
            {this.state.networkError ? (
              <MessageBar
                messageBarType={MessageBarType.error}
                isMultiline={false}
                dismissButtonAriaLabel="Close"
              >
                {this.state.networkError}
              </MessageBar>
            ) : this.state.successMessage ? (
              <MessageBar
                messageBarType={MessageBarType.success}
                isMultiline={false}
                dismissButtonAriaLabel="Close"
              >
                {this.state.successMessage}
              </MessageBar>
            ) : null}
            <h4 className="outlook-title">{t(this.state.config.header)}</h4>
            <div className="outlook-description">{t(this.state.config.description)}</div>
            {this.props.type === ConvertMessageTypes.Activity ? (
              <TextMessage
                label={t("Outlook.commentAttachment")}
                description={t("Outlook.activityTitleDescription")}
                onTextChange={this.getMessage}
              />
            ) : (
              <>
                <TextField
                  label={t(this.state.config.fields.title.label)}
                  defaultValue={this.state.messageTitle}
                />
                <div>{t(this.state.config.fields.title.description)}</div>
              </>
            )}
            {this.state.messageContent ? (
              <>
                {this.props.type === ConvertMessageTypes.Activity ? (
                  <TextField
                    aria-labelledby={this.labelId}
                    label={t(this.state.config.fields.content.label)}
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
                <div className="ms-TextField-description">
                  {t(this.state.config.fields.content.description)}
                </div>
              </>
            ) : null}
            <SpacesSelect
              isOptional={this.state.config.fields.spaces.isOptional}
              description={t(this.state.config.fields.spaces.description)}
              onSelectSpace={this.getSpace}
              user={this.props.userUrl}
              userName={this.props.userName}
            />
            <PrimaryButton text={t("Outlook.convert")} onClick={this.convertMessage} />
            <DefaultButton text={t("Outlook.cancel")} style={{ marginLeft: "10px" }} />
          </div>
        )}
      </Translation>
    ) : (
      <Spinner size={SpinnerSize.large} />
    );

    return displayedComponent;
  }
}

export default ConvertMessage;
