import React from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPlus } from '@fortawesome/free-solid-svg-icons'

const NavChildren = props => {
    return (
        <div style={{marginTop: "120px", paddingLeft: "38px"}}>
            <a href={"/add-song"} style={{fontSize: "23px", fontFamily: "Segoe UI", color: "#BCBAC2", fontWeight: 300}}>
                <FontAwesomeIcon icon={faPlus} style={{marginRight: "27px"}}/>
                <span>Add song</span>
            </a>
        </div>
    )
};

export default NavChildren;
