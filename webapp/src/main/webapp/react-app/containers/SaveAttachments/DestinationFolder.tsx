import * as React from "react";
import { TextField } from "office-ui-fabric-react/lib/TextField";
import { IconButton } from "office-ui-fabric-react";
import "./DestinationFolder.less";
// import axios from "axios";
import { IColumn, DetailsList, DetailsListLayoutMode } from "office-ui-fabric-react/lib/DetailsList";

interface IDestinationFolderProps {
  onShowDialog: Function;
  space: any;
}

function DestinationFolder(props: IDestinationFolderProps): React.ReactElement<IDestinationFolderProps> {
  const [documents, setDocuments] = React.useState();
  const [showFolders, setShowFolders] = React.useState(false);
  let listColumns: IColumn[];

  React.useEffect(() => {
    if (props.space) {
      // axios.get(props.space._links.self.href).then(({ data }) => {
      //   axios.get(data._links.documents.href).then(res => {
      //     setDocuments(res.data.documents);
      //     listColumns = res.data.documents.map(({ id, name }) => ({ key: id, fieldName: name, name: name }));
      //   });
      // });
      setDocuments([{ id: "11", type: "folder", name: "doc1" }]);
      listColumns = [{ id: "11", type: "folder", name: "doc1" }].map(({ id, name }) => ({
        key: id,
        fieldName: name,
        name: name,
        minWidth: 200
      }));
    }
  }, [props.space]);

  const nodeFolders =
    documents && showFolders ? (
      <DetailsList
        items={documents}
        columns={listColumns}
        setKey="set"
        layoutMode={DetailsListLayoutMode.justified}
        isHeaderVisible={false}
      />
    ) : null;

  return (
    <div>
      <div>Destination Folder</div>
      <div className="target-folder">
        <TextField label="The target folder within the space in which the attachment placed" disabled />
        <div className="target-folder-actions">
          <IconButton
            iconProps={{ iconName: "Up" }}
            title="Show folders"
            ariaLabel="Show folders"
            onClick={() => setShowFolders(prevShowFolders => !prevShowFolders)}
          />
          <IconButton
            iconProps={{ iconName: "Add" }}
            title="Add folder"
            ariaLabel="Add folder"
            onClick={() => props.onShowDialog()}
          />
          <IconButton iconProps={{ iconName: "Go" }} title="Open in new tab" ariaLabel="Open in new tab" />
        </div>
      </div>
      {nodeFolders}
    </div>
  );
}

export default DestinationFolder;
