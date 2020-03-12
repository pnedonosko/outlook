import * as React from "react";
import { Spinner, SpinnerSize } from "office-ui-fabric-react/lib/Spinner";
import { ConvertMessageTypes } from "./containers/ConvertMessage/convert-message.types";
import { IConvertMessageProps } from "./containers/ConvertMessage";

export interface IContainerProps {
  userUrl: string;
  userName: string;
}

interface IOutlookAppState {
  component?: React.ComponentType<IContainerProps | IConvertMessageProps>;
  error?: string;
  componentType?: ConvertMessageTypes;
}

interface IOutlookAppProps extends IContainerProps {
  componentName: string;
}

/* ContainerTypes contains relevant component names in app */
enum ContainerTypes {
  "PostStatus" = "PostActivity",
  "StartDiscussion" = "StartDiscussion",
  "AddAttachment" = "AttachDocument",
  "SaveAttachment" = "SaveAttachments"
}

class OutlookApp extends React.Component<IOutlookAppProps, IOutlookAppState> {
  async componentDidMount() {
    let componentName = this.props.componentName;
    /* Name in props matches command name from manifest */
    switch (this.props.componentName) {
      case "convertToWiki":
        this.setState({ componentType: ConvertMessageTypes.Wiki });
        componentName = "ConvertMessage";
        break;
      case "convertToForum":
        this.setState({ componentType: ConvertMessageTypes.Forum });
        componentName = "ConvertMessage";
        break;
      case "convertToStatus":
        this.setState({ componentType: ConvertMessageTypes.Activity });
        componentName = "ConvertMessage";
        break;
      case "postStatus":
        componentName = ContainerTypes.PostStatus;
        break;
      case "startDiscussion":
        componentName = ContainerTypes.StartDiscussion;
        break;
      case "addAttachment":
        componentName = ContainerTypes.AddAttachment;
        break;
      case "saveAttachment":
        componentName = ContainerTypes.SaveAttachment;
        break;
    }
    await this.addComponent(componentName);
  }

  addComponent = async componentName => {
    console.log(`Loading ${componentName} component...`);

    /* Download appropriate component selected by name */
    import(`./containers/${componentName}`)
      .then(module =>
        this.setState({
          component: module.default
        })
      )
      .catch(() =>
        this.setState({
          error: `Can't load ${componentName} component`
        })
      );
  };

  render(): JSX.Element {
    let displayedComponent =
      this.state && this.state.component ? (
        <this.state.component
          userUrl={this.props.userUrl}
          type={this.state.componentType ? this.state.componentType : null}
          userName={this.props.userName}
        />
      ) : (
        <Spinner size={SpinnerSize.large} />
      );
    if (this.state && this.state.error) {
      displayedComponent = <div>{this.state.error}</div>;
    }
    return displayedComponent;
  }
}

export default OutlookApp;
