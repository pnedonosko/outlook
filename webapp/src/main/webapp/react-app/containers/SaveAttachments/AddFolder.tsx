import * as React from "react";
import { Dialog, DialogType, DialogFooter } from "office-ui-fabric-react/lib/Dialog";
import { PrimaryButton, DefaultButton } from "office-ui-fabric-react/lib/Button";
import { TextField } from "office-ui-fabric-react/lib/TextField";

interface IAddFolder {
  onAddFolder: Function;
  onCloseFolder: Function;
}

function AddFolder(props: IAddFolder): React.ReactElement {
  const [folderName, setFolderName] = React.useState("");

  return (
    <Dialog
      hidden={false}
      onDismiss={() => props.onCloseFolder()}
      dialogContentProps={{
        type: DialogType.normal,
        title: "Adding new folder",
        closeButtonAriaLabel: "Close",
        subText: "Provide a name for this new folder and then click 'Add'"
      }}
      modalProps={{
        isBlocking: false,
        styles: { main: { maxWidth: 450 } }
      }}
    >
      <TextField label="Folder name" onChange={(_, value) => setFolderName(value)} />
      <DialogFooter>
        <PrimaryButton onClick={() => props.onAddFolder(folderName)} text="Add" />
        <DefaultButton onClick={() => props.onCloseFolder()} text="Cancel" />
      </DialogFooter>
    </Dialog>
  );
}

export default AddFolder;
