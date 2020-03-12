import * as React from "react";
import {
  IColumn,
  Selection,
  DetailsList,
  DetailsListLayoutMode
} from "office-ui-fabric-react/lib/DetailsList";
import { useTranslation } from "react-i18next";

interface ISelectAttachmentProps {
  attachments: Office.AttachmentDetails[];
  onSelectItem: Function;
}

function SelectAttachments(props: ISelectAttachmentProps): React.ReactElement<ISelectAttachmentProps> {
  const { t } = useTranslation();
  let selection: Selection = new Selection({
    onSelectionChanged: () => props.onSelectItem(getSelectionDetails())
  });
  let columns: IColumn[];

  React.useEffect(() => {
    columns = [
      { key: "column1", name: "Name", fieldName: "name", minWidth: 100, maxWidth: 200, isResizable: true },
      { key: "column2", name: "Value", fieldName: "size", minWidth: 100, maxWidth: 200, isResizable: true }
    ];
  }, []);

  function getSelectionDetails(): string {
    const selectionCount = selection.getSelectedCount();

    switch (selectionCount) {
      case 0:
        return "No items selected";
      case 1:
        return "1 item selected: " + (selection.getSelection()[0] as Office.AttachmentDetails).name;
      default:
        return `${selectionCount} items selected`;
    }
  }

  return (
    <div>
      <span className="ms-Label">{t("Outlook.selectAttachment")}</span>
      <DetailsList
        items={props.attachments}
        columns={columns}
        setKey="set"
        layoutMode={DetailsListLayoutMode.justified}
        selection={selection}
        selectionPreservedOnEmptyClick
        isHeaderVisible={false}
      />
      <div>{t("Outlook.selectAttachmentDescription")}.</div>
    </div>
  );
}

export default SelectAttachments;
