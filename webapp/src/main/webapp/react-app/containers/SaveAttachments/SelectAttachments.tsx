import * as React from "react";
import {
  IColumn,
  Selection,
  DetailsList,
  DetailsListLayoutMode
} from "office-ui-fabric-react/lib/DetailsList";

interface ISelectAttachmentProps {
  attachments: Office.AttachmentDetails[];
  onSelectItem: Function;
}

function SelectAttachments(props: ISelectAttachmentProps): React.ReactElement {
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
      <span>Select attachments</span>
      <DetailsList
        items={props.attachments}
        columns={columns}
        setKey="set"
        layoutMode={DetailsListLayoutMode.justified}
        selection={selection}
        selectionPreservedOnEmptyClick
        isHeaderVisible={false}
      />
      <div>Selected attachments will be saved inside the target space.</div>
    </div>
  );
}

export default SelectAttachments;
