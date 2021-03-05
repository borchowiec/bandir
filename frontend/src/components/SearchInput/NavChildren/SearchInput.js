import React from 'react';

import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {faPlus} from '@fortawesome/free-solid-svg-icons'

const SearchInput = props => {
    return (
        <input /*todo search*/
            placeholder="search..."
            style={{
                backgroundColor: "white",
                boxShadow: "0px 3px 6px #00000017",
                width: "218px",
                height: "45px",
                fontSize: "23px",
                fontFamily: "Segoe UI",
                color: "#E1E1E1",
                border: "1px solid #E1E1E1",
                borderRadius: "23px",
                padding: "0 18px",
                outline: "none"
            }}
        />
    )
};

export default SearchInput;
