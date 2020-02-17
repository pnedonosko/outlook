import * as React from "react";
import { TextField } from "office-ui-fabric-react/lib/TextField";

interface ITextFieldProps {
  label: string;
  description: string;
}

function TextMessage(props: ITextFieldProps): React.ReactElement {
  return (
    <>
      <TextField label={props.label} multiline rows={3} />
      <div>{props.description}</div>
    </>
  );
}

export default TextMessage;
