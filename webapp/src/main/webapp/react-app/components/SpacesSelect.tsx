import * as React from "react";
import "./SpacesSelect.less";
import { Dropdown, IDropdownStyles, IDropdownOption } from "office-ui-fabric-react/lib/Dropdown";

interface ISpacesSelectProps {
  isOptional: boolean;
  description: string;
  onSelectSpace: Function;
}

const dropdownStyles: Partial<IDropdownStyles> = {
  dropdown: { width: 300 }
};

const SpacesSelect: React.FC<ISpacesSelectProps> = (props: ISpacesSelectProps) => {
  const [spaces, setSpaces] = React.useState<IDropdownOption[]>();

  React.useEffect(() => {
    // GET request for user spaces, now mocked data
    setSpaces([
      { key: "id1", text: "First space" },
      { key: "id2", text: "Second space" }
    ]);
  }, []);

  return (
    <>
      <Dropdown
        label={"Target space" + (props.isOptional ? " (optional)" : "")}
        options={spaces}
        styles={dropdownStyles}
        onChange={(_, selected) => props.onSelectSpace(selected)}
      />
      <div>{props.description}</div>
    </>
  );
};

export default SpacesSelect;
