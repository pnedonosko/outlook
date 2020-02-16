import * as React from "react";
import { IAttachment } from ".";

const SelectAttachments: React.FC<{ attachments: IAttachment[]}> = ({ attachments }) => {
  return (
    <div>
      <span>Select attachments</span>
      <ul>{attachments.map(({ name, size, id }) => (<li key={id}><span>{name}</span><span>{size}</span></li>))}</ul>
      <div>Selected attachments will be saved inside the target space.</div>
    </div>
  );
}

export default SelectAttachments;
