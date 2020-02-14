import * as React from 'react';
import './save-attachments.less';
import SpacesSelect from '../../components/SpacesSelect/spaces-select';

class SaveAttachments extends React.Component {
    constructor(props) {
        super(props);
        console.log(props);
    }

    render() {
        return (
            <div className="the-app">
                <h1>Pane Content will be here</h1>
                <SpacesSelect />
            </div>
        );
    }
}

export default SaveAttachments;
