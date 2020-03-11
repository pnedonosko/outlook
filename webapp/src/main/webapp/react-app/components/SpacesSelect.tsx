import * as React from "react";
import "./SpacesSelect.less";
import {
  Dropdown,
  IDropdownOption,
  ResponsiveMode,
  IDropdownProps
} from "office-ui-fabric-react/lib/Dropdown";
import axios from "axios";
import { Stack, IconButton } from "office-ui-fabric-react";

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
    fetchSpaces();
  }, []);

  const onRenderRefreshLabel = (props: IDropdownProps): JSX.Element => {
    return (
      <Stack horizontal verticalAlign="center">
        <label className="ms-Label">{props.label}</label>
        <IconButton
          iconProps={{ iconName: "Sync", styles: { root: { fontSize: "9px", fontWeight: 900 } } }}
          title="Refresh"
          ariaLabel="Refresh"
          styles={{ root: { marginBottom: -3 } }}
          className="labelIcon"
          onClick={() => fetchSpaces()}
        />
      </Stack>
    );
  };

  const fetchSpaces = (): void => {
    axios.get(props.user).then(({ data }) => {
      let spaces = data._links.spaces.href.replace("$UID", props.userName).split("?")[0];
      axios.get(spaces, { params: { offset: 0, limit: 100 } }).then(res => {
        const dropsownSpaces = res.data._embedded.children.map(({ groupId, title }) => ({
          key: groupId,
          text: title
        }));
        setSpaces(dropsownSpaces);
      });
    });
  };

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
              maxHeight: "112px",
              overflowY: "auto"
            }
          }}
          onRenderLabel={onRenderRefreshLabel}
        />
      ) : null}
      <div className="ms-TextField-description">{props.description}</div>
    </>
  );
}

export default SpacesSelect;
