import * as React from "react";
import "./index.less";
import { IContainerProps } from "../../OutlookApp";
import { Dropdown, ResponsiveMode } from "office-ui-fabric-react/lib/Dropdown";
import { Pivot, PivotItem, PivotLinkFormat } from "office-ui-fabric-react/lib/Pivot";
import { SearchBox } from "office-ui-fabric-react/lib/SearchBox";
import {
  DetailsList,
  DetailsListLayoutMode,
  Selection,
  IColumn
} from "office-ui-fabric-react/lib/DetailsList";
import { MarqueeSelection } from "office-ui-fabric-react/lib/MarqueeSelection";
import {
  IconButton,
  TextField,
  PrimaryButton,
  DefaultButton,
  MessageBarType,
  MessageBar
} from "office-ui-fabric-react";
import axios from "axios";

interface IAttachDocumentState {
  sources: ISource[];
  selectedSource: ISource;
  sourceDocuments: ISourceDocument[];
  selectionDetails?: ISourceDocument[];
  resultNotification?: { type: MessageBarType; errors?: string[] };
}

interface ISource {
  key: string;
  text: string;
}

export interface ISourceDocument {
  name: string;
  type: string;
  path: string;
}

class AttachDocument extends React.Component<IContainerProps, IAttachDocumentState> {
  public state = {
    sources: [],
    selectedSource: undefined,
    sourceDocuments: [],
    selectionDetails: [],
    resultNotification: undefined
  };

  private selection = new Selection({
    onSelectionChanged: () => this.setState({ selectionDetails: this.getSelectionDetails() })
  });

  private columns: IColumn[] = [
    { key: "name", name: "Name", fieldName: "name", minWidth: 100, maxWidth: 200, isResizable: true },
    { key: "link", name: "Url", fieldName: "path", minWidth: 100, maxWidth: 200, isResizable: true }
  ];

  componentDidMount() {
    axios
      .get(this.props.services.userServices.href)
      .then(({ data }) => {
        let spaces = data._links.spaces.href.replace("$UID", this.props.userName).split("?")[0];
        axios.get(spaces, { params: { offset: 0, limit: 100 } }).then(res => {
          console.log(res.data._embedded.children);
          const dropdownSpaces = res.data._embedded.children.map(({ groupId, title }) => ({
            key: groupId,
            text: title
          }));
          this.setState({
            sources: [
              ...dropdownSpaces,
              { key: data._links.documents.href, text: "My documents" },
              { key: "all", text: "All spaces" }
            ]
          });
        });
      })
      .catch(err => console.log(err));
  }

  private onSourceChange = () => {
    // get documents from source
    this.setState({
      sourceDocuments: [
        { name: "file.jpg", type: "file", path: "tofile1" },
        { name: "file.png", type: "file", path: "tofile2" }
      ]
    });
  };

  private getSelectionDetails(): ISourceDocument[] {
    return this.selection.getSelection() as ISourceDocument[];
  }

  private attachDocuments = () => {
    // request save attachment with result
    const res = { status: "error", errors: ["An internal error has occured"] };
    if (res.status === "error") {
      this.setState({ resultNotification: { type: MessageBarType.error, errors: res.errors } });
    } else {
      this.setState({ resultNotification: { type: MessageBarType.success } });
    }
  };

  private renderItemColumn(item, _, column: IColumn) {
    const fieldContent = item[column.fieldName as keyof ISourceDocument] as string;
    return column.key === "link" ? (
      <IconButton iconProps={{ iconName: "Go" }} href={fieldContent} />
    ) : (
      <span>{fieldContent}</span>
    );
  }

  public render(): JSX.Element {
    const attachContent = this.state.resultNotification ? (
      <MessageBar
        messageBarType={this.state.resultNotification.type}
        isMultiline={false}
        dismissButtonAriaLabel="Close"
      >
        {this.state.resultNotification.errors ? this.state.resultNotification.errors[0] : "Successfull!"}
      </MessageBar>
    ) : (
      <>
        <div className="outlook-description">Attach documents from the intranet to this message.</div>
        <Dropdown
          label="Source"
          selectedKey={this.state.selectedSource ? this.state.selectedSource.key : undefined}
          onChange={this.onSourceChange}
          options={this.state.sources}
          responsiveMode={ResponsiveMode.large}
          styles={{
            dropdownItemsWrapper: {
              maxHeight: "186px",
              overflowY: "auto"
            }
          }}
        />
        <Pivot
          aria-label="Links to choose between exporer and search"
          linkFormat={PivotLinkFormat.tabs}
          className="tabsContainer"
        >
          <PivotItem headerText="Search">
            <SearchBox
              placeholder="Search by name"
              onEscape={() => console.log("Custom onEscape Called")}
              onClear={() => console.log("Custom onClear Called")}
              onChange={(_, newValue) => console.log("SearchBox onChange fired: " + newValue)}
              onSearch={newValue => console.log("SearchBox onSearch fired: " + newValue)}
              onFocus={() => console.log("onFocus called")}
              onBlur={() => console.log("onBlur called")}
            />
            <div className="documentSelector">
              {this.state.sourceDocuments.length > 0 ? (
                <MarqueeSelection selection={this.selection}>
                  <DetailsList
                    items={this.state.sourceDocuments}
                    columns={this.columns}
                    setKey="set"
                    layoutMode={DetailsListLayoutMode.justified}
                    selection={this.selection}
                    selectionPreservedOnEmptyClick
                    ariaLabelForSelectionColumn="Toggle selection"
                    ariaLabelForSelectAllCheckbox="Toggle selection for all items"
                    checkButtonAriaLabel="Row checkbox"
                    isHeaderVisible={false}
                    onRenderItemColumn={this.renderItemColumn}
                  />
                </MarqueeSelection>
              ) : null}
            </div>
          </PivotItem>
          <PivotItem headerText="Explore">
            <div className="target-folder">
              <TextField disabled />
              <div className="target-folder-actions">
                <IconButton iconProps={{ iconName: "Up" }} title="Show folders" ariaLabel="Show folders" />
                <IconButton
                  iconProps={{ iconName: "Go" }}
                  title="Open in new tab"
                  ariaLabel="Open in new tab"
                />
              </div>
            </div>
            <DetailsList
              items={this.state.sourceDocuments}
              columns={this.columns}
              setKey="set"
              layoutMode={DetailsListLayoutMode.justified}
              selection={this.selection}
              selectionPreservedOnEmptyClick
              ariaLabelForSelectionColumn="Toggle selection"
              ariaLabelForSelectAllCheckbox="Toggle selection for all items"
              checkButtonAriaLabel="Row checkbox"
              isHeaderVisible={false}
              onRenderItemColumn={this.renderItemColumn}
            />
          </PivotItem>
        </Pivot>
        <div>
          <span className="ms-Label">Selected documents</span>
          {this.state.selectionDetails.length > 0 ? (
            <DetailsList
              items={this.state.selectionDetails}
              columns={this.columns}
              isHeaderVisible={false}
              onRenderItemColumn={this.renderItemColumn}
            />
          ) : null}
          <div className="ms-TextField-description" style={{ maxWidth: "80%" }}>
            Selected documents will be attached to the message.
          </div>
        </div>
        <PrimaryButton text="Attach" onClick={this.attachDocuments} />
        <DefaultButton text="Cancel" />
      </>
    );

    return (
      <div className="outlook-command-container outlook-save-attachments">
        <h4 className="outlook-title">Attach Document</h4>
        {attachContent}
      </div>
    );
  }
}

export default AttachDocument;
