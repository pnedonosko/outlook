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
    <TextField
      label={props.label}
      multiline
      rows={4}
      onChange={(_, newText: string) => props.onTextChange(newText)}
      placeholder={props.placeholder ? props.placeholder : ""}
      description={props.description}
      className="message"
      styles={{ field: { height: "64px", padding: "6px 10px 8px" } }}
    />
  );
}

export default TextMessage;
