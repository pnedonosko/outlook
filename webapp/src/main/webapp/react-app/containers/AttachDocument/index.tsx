import * as React from "react";
import "./index.less";
import { IContainerProps } from "../../OutlookApp";
import { Dropdown } from "office-ui-fabric-react/lib/Dropdown";
import { Pivot, PivotItem, PivotLinkFormat } from "office-ui-fabric-react/lib/Pivot";
import { SearchBox } from "office-ui-fabric-react/lib/SearchBox";

interface IAttachDocumentState {
  sources: { key: string; text: string }[];
  selectedSource: { key: string; text: string };
}

class AttachDocument extends React.Component<IContainerProps, IAttachDocumentState> {
  public state = { sources: [], selectedSource: undefined };

  componentDidMount() {
    // get spaces, add options 'All spaces' and 'My documents' to dropdown
    this.setState({
      sources: [
        { key: "allId", text: "All spaces" },
        { key: "mydocsId", text: "My documents" },
        { key: "space1", text: "Space 1" }
      ]
    });
  }

  private onSourceChange = () => {};

  public render(): JSX.Element {
    return (
      <div className="outlook-command-container outlook-save-attachments">
        <h4 className="outlook-title">Attach Document</h4>
        <div className="outlook-description">Attach documents from the intranet to this message.</div>
        <Dropdown
          label="Source"
          selectedKey={this.state.selectedSource ? this.state.selectedSource.key : undefined}
          onChange={this.onSourceChange}
          options={this.state.sources}
          styles={{ dropdown: { width: 300 } }}
        />
        <Pivot aria-label="Links to choose between exporer and search" linkFormat={PivotLinkFormat.tabs}>
          <PivotItem headerText="Search">
            <SearchBox
              placeholder="Search"
              onEscape={() => console.log("Custom onEscape Called")}
              onClear={() => console.log("Custom onClear Called")}
              onChange={(_, newValue) => console.log("SearchBox onChange fired: " + newValue)}
              onSearch={newValue => console.log("SearchBox onSearch fired: " + newValue)}
              onFocus={() => console.log("onFocus called")}
              onBlur={() => console.log("onBlur called")}
            />
          </PivotItem>
          <PivotItem headerText="Explore">Explorer</PivotItem>
        </Pivot>
      </div>
    );
  }
}

export default AttachDocument;
