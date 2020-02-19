import * as React from "react";
import "./SpacesSelect.less";
import { Dropdown, IDropdownStyles, IDropdownOption } from "office-ui-fabric-react/lib/Dropdown";
// import axios from "axios";

interface ISpacesSelectProps {
  isOptional: boolean;
  description: string;
  onSelectSpace: Function;
  user: any;
}

const dropdownStyles: Partial<IDropdownStyles> = {
  dropdown: { width: 300 }
};

const SpacesSelect: React.FC<ISpacesSelectProps> = (props: ISpacesSelectProps) => {
  const [spaces, setSpaces] = React.useState<IDropdownOption[]>();

  React.useEffect(() => {
    // axios.get(props.user._links.user.href).then(({ data }) => {
    //   axios.get(data._links.spaces.href).then(res => {
    //     const iterableSpaces = res.data._embedded.spaces.map(({ id, title, _links }) => ({
    //       key: id,
    //       text: title,
    //       _links: _links
    //     }));
    //     setSpaces(iterableSpaces);
    //   });
    // });
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
