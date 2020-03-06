import * as React from "react";
import { Spinner, SpinnerSize } from "office-ui-fabric-react/lib/Spinner";
import axios from "axios";
import { ConvertMessageTypes } from "./containers/ConvertMessage/convert-message.types";
import { IConvertMessageProps } from "./containers/ConvertMessage";

export interface IExoServices {
  self: { href: string };
  mailServices: { href: string };
  userServices: { href: string };
  spaceServices: { href: string };
  activityServices: { href: string };
  documentServices: { href: string };
}

export interface IContainerProps {
  services: IExoServices;
  userName: string;
}

interface IOutlookAppState {
  services: IExoServices;
  component?: React.ComponentType<IContainerProps | IConvertMessageProps>;
  error?: string;
  componentType?: ConvertMessageTypes;
}

interface IOutlookAppProps {
  componentName: string;
  baseUrl: string;
  userName: string;
}

class OutlookApp extends React.Component<IOutlookAppProps, IOutlookAppState> {
  state: IOutlookAppState = { services: undefined };

  constructor(props: IOutlookAppProps) {
    super(props);
  }

  async componentDidMount() {
    let componentName = this.props.componentName;
    if (componentName.includes("ConvertTo")) {
      switch (this.props.componentName) {
        case "ConvertToWiki":
          this.setState({ componentType: ConvertMessageTypes.Wiki });
          break;
        case "ConvertToForum":
          this.setState({ componentType: ConvertMessageTypes.Forum });
          break;
        case "ConvertToActivity":
          this.setState({ componentType: ConvertMessageTypes.Activity });
          break;
      }
      // for all converting component 'ConvertMessage' with defferent types should be rendered
      componentName = "ConvertMessage";
    }
    await this.addComponent(componentName);
    axios.get(this.props.baseUrl).then(({ data }) => {
      this.setState({ services: data._links });
    });
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
      this.state.component && this.state.services ? (
        <this.state.component
          services={this.state.services}
          type={this.state.componentType ? this.state.componentType : null}
          userName={this.props.userName}
        />
      ) : (
        <Spinner size={SpinnerSize.large} />
      );
    if (this.state.error) {
      displayedComponent = <div>{this.state.error}</div>;
    }
    return displayedComponent;
  }
}

export default OutlookApp;
