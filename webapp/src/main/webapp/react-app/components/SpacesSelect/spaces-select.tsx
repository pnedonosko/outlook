import * as React from "react";
import "./spaces-select.less";
import { Dropdown, IDropdownStyles, IDropdownOption } from "office-ui-fabric-react/lib/Dropdown";

const dropdownStyles: Partial<IDropdownStyles> = {
  dropdown: { width: 300 }
};

const spacesSelect: React.SFC = () => {
  const [spaces, setSpaces] = React.useState<IDropdownOption[]>();

  React.useEffect(() => {
    // GET request for user spaces, now mocked data
    setSpaces([
      { key: "id1", text: "First space" },
      { key: "id2", text: "Second space" }
    ]);
  }, []);

  return (
    <Dropdown placeholder="Select to space" label="Save to space" options={spaces} styles={dropdownStyles} />
  );
};

export default spacesSelect;
