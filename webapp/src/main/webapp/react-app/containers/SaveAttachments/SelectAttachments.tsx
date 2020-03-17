import * as React from "react";
import { useTranslation } from "react-i18next";
import "./SelectAttachments.less";

interface ISelectAttachmentProps {
  attachments: Office.AttachmentDetails[];
  onSelectItem: Function;
}

function SelectAttachments(props: ISelectAttachmentProps): React.ReactElement<ISelectAttachmentProps> {
  const { t } = useTranslation();

  return (
    <div>
      <span className="ms-Label">{t("Outlook.selectAttachment")}</span>
      <ul>
        {props.attachments.map(item => (
          <li className="attachmentItem" key={item.id}>
            <span>{item.name}</span>
            <span>{`${item.size} B`}</span>
          </li>
        ))}
      </ul>
      <div className="ms-TextField-description">{t("Outlook.selectAttachmentDescription")}</div>
    </div>
  );
}

export default SelectAttachments;
