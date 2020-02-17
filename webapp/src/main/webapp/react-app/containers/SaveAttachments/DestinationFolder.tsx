import * as React from "react";
import { TextField, MaskedTextField } from "office-ui-fabric-react/lib/TextField";
import { IconButon } from "office-ui-fabric-react";

function DestinationFolder(props): React.ReactElement {
  return (
    <div>
      <div>Destination Folder</div>
      <TextField label="The target folder within the space in which the attachment placed" disabled />
    </div>
  );
}

export default DestinationFolder;
