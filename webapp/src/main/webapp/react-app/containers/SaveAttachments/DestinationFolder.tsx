import * as React from "react";
import { TextField } from "office-ui-fabric-react/lib/TextField";
import { IconButton } from "office-ui-fabric-react";
import "./DestinationFolder.less";

interface IDestinationFolderProps {
  onShowDialog: Function;
}

function DestinationFolder(props: IDestinationFolderProps): React.ReactElement {
  return (
    <div>
      <div>Destination Folder</div>
      <div className="target-folder">
        <TextField label="The target folder within the space in which the attachment placed" disabled />
        <div className="target-folder-actions">
          <IconButton iconProps={{ iconName: "Up" }} title="Show folders" ariaLabel="Show folders" />
          <IconButton
            iconProps={{ iconName: "Add" }}
            title="Add folder"
            ariaLabel="Add folder"
            onClick={() => props.onShowDialog()}
          />
          <IconButton iconProps={{ iconName: "Go" }} title="Open in new tab" ariaLabel="Open in new tab" />
        </div>
      </div>
    </div>
  );
}

export default DestinationFolder;
