import * as React from "react";
import { Dialog, DialogType, DialogFooter } from "office-ui-fabric-react/lib/Dialog";
import { PrimaryButton, DefaultButton } from "office-ui-fabric-react/lib/Button";
import { TextField } from "office-ui-fabric-react/lib/TextField";
import { useTranslation } from "react-i18next";

interface IAddFolder {
  onAddFolder: Function;
  onCloseFolder: Function;
}

function AddFolder(props: IAddFolder): React.ReactElement {
  const [folderName, setFolderName] = React.useState("");
  const { t } = useTranslation();

  // dialog with textfield for new folder creation on exo platform
  return (
    <Dialog
      hidden={false}
      onDismiss={() => props.onCloseFolder()}
      dialogContentProps={{
        type: DialogType.normal,
        title: t("Outlook.addingNewFolder"),
        closeButtonAriaLabel: "Close",
        subText: t("Outlook.enterFolderNameAndCreate")
      }}
      modalProps={{
        isBlocking: false,
        styles: { main: { maxWidth: 450 } }
      }}
    >
      <TextField label={t("Outlook.folderName")} onChange={(_, value) => setFolderName(value)} />
      <DialogFooter>
        <PrimaryButton onClick={() => props.onAddFolder(folderName)} text={t("Outlook.add")} />
        <DefaultButton onClick={() => props.onCloseFolder()} text={t("Outlook.cancel")} />
      </DialogFooter>
    </Dialog>
  );
}

export default AddFolder;
