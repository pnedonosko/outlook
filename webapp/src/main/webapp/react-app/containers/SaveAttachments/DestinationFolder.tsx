import * as React from "react";
import { TextField } from "office-ui-fabric-react/lib/TextField";
import { IconButton, IDropdownOption } from "office-ui-fabric-react";
import "./DestinationFolder.less";
import { IColumn, DetailsList, DetailsListLayoutMode } from "office-ui-fabric-react/lib/DetailsList";
import { useTranslation } from "react-i18next";

interface IDestinationFolderProps {
  onShowDialog: Function;
  space: IDropdownOption;
}

function DestinationFolder(props: IDestinationFolderProps): React.ReactElement<IDestinationFolderProps> {
  const [documents, setDocuments] = React.useState([]);
  const [showFolders, setShowFolders] = React.useState(false);
  const { t } = useTranslation();
  let listColumns: IColumn[];

  React.useEffect(() => {
    // getting spaces folders
    if (props.space) {
      setDocuments([]);
      listColumns = [].map(({ id, name }) => ({
        key: id,
        fieldName: name,
        name: name,
        minWidth: 200
      }));
    }
  }, [props.space]);

  // list with space's folders so that the user can choose which folder to save
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
      <div>{t("Outlook.selectFolder")}</div>
      <div className="target-folder">
        <TextField label={t("Outlook.pathInfoSaveDescription")} disabled />
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
