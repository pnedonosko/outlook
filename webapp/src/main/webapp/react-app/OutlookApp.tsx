import * as React from "react";
import { Spinner, SpinnerSize } from "office-ui-fabric-react/lib/Spinner";
// import axios from "./axios-entry-point";
import { DefaultButton } from "office-ui-fabric-react/lib/Button";
import { Panel } from "office-ui-fabric-react/lib/Panel";

export interface IContainerProps {
  user: any;
}

interface IOutlookAppState {
  isPanelOpen: boolean;
  user?: any;
  component?: React.ComponentType<IContainerProps>;
  error?: string;
}

interface IOutlookAppProps {
  componentName: string;
}

class OutlookApp extends React.Component<IOutlookAppProps> {
  state: IOutlookAppState = { isPanelOpen: false };

  constructor(props: IOutlookAppProps) {
    super(props);
  }

  async componentDidMount() {
    await this.addComponent(this.props.componentName);
    // axios.get("/exo").then(({ data }) => {
    //   this.setState({ user: data });
    // });
    this.setState({ user: "data" });
  }

  addComponent = async componentName => {
    console.log(`Loading ${componentName} component...`);

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

  togglePanel = () => {
    this.setState((prevState: IOutlookAppState) => ({ isPanelOpen: !prevState.isPanelOpen }));
  };

  render(): JSX.Element {
    let displayedComponent = this.state.component ? (
      <this.state.component user={this.state.user} />
    ) : (
      <Spinner size={SpinnerSize.large} />
    );
    if (this.state.error) {
      displayedComponent = <div>{this.state.error}</div>;
    }
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
          {displayedComponent}
        </Panel>
      </>
    );
  }
}

export default OutlookApp;
