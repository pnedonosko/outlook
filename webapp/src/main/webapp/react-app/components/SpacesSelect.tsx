import * as React from "react";
import "./SpacesSelect.less";
import { Dropdown, IDropdownOption, ResponsiveMode } from "office-ui-fabric-react/lib/Dropdown";
import axios from "axios";

interface ISpacesSelectProps {
  isOptional: boolean;
  description: string;
  onSelectSpace: Function;
  user: string;
  userName: string;
}

function SpacesSelect(props: ISpacesSelectProps): React.ReactElement<ISpacesSelectProps> {
  const [spaces, setSpaces] = React.useState<IDropdownOption[]>();

  React.useEffect(() => {
    axios.get(props.user).then(({ data }) => {
      let spaces = data._links.spaces.href.replace("$UID", props.userName).split("?")[0];
      axios.get(spaces, { params: { offset: 0, limit: 10 } }).then(res => {
        const dropsownSpaces = res.data._embedded.children.map(({ groupId, title }) => ({
          key: groupId,
          text: title
        }));
        setSpaces(dropsownSpaces);
      });
    });
  }, []);

  return (
    <>
      {spaces ? (
        <Dropdown
          label={"Target space" + (props.isOptional ? " (optional)" : "")}
          options={spaces}
          onChange={(_, selected) => props.onSelectSpace(selected)}
          responsiveMode={ResponsiveMode.large}
          styles={{
            dropdownItemsWrapper: {
              maxHeight: "148px",
              overflowY: "auto"
            }
          }}
        />
      ) : null}
      <div className="ms-TextField-description">{props.description}</div>
    </>
  );
}

export default SpacesSelect;
