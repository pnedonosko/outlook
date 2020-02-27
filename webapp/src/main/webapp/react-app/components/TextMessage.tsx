import * as React from "react";
import { TextField } from "office-ui-fabric-react/lib/TextField";

interface ITextFieldProps {
  label: string;
  description: string;
  onTextChange: Function;
  placeholder?: string;
}

function TextMessage(props: ITextFieldProps): React.ReactElement<ITextFieldProps> {
  return (
    <>
      <TextField
        label={props.label}
        multiline
        rows={3}
        onChange={(_, newText: string) => props.onTextChange(newText)}
        placeholder={props.placeholder ? props.placeholder : ""}
      />
      <div>{props.description}</div>
    </>
  );
}

export default TextMessage;
