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

class OutlookApp extends React.Component<IOutlookAppProps, IOutlookAppState> {
  async componentDidMount() {
    let componentName = this.props.componentName;
    if (componentName.includes("convertTo")) {
      switch (this.props.componentName) {
        case "convertToWiki":
          this.setState({ componentType: ConvertMessageTypes.Wiki });
          break;
        case "convertToForum":
          this.setState({ componentType: ConvertMessageTypes.Forum });
          break;
        case "convertToStatus":
          this.setState({ componentType: ConvertMessageTypes.Activity });
          break;
      }
      // for all converting component 'ConvertMessage' with defferent types should be rendered
      componentName = "ConvertMessage";
    }
    await this.addComponent(componentName);
  }

  addComponent = async componentName => {
    console.log(`Loading ${componentName} component...`);

    // download appropriate component selected by name
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
