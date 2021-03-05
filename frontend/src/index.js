import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';
import {BrowserRouter as Router, Switch, Route} from "react-router-dom";
import LandingPage from "./views/LandingPage/LandingPage";
import AddSong from "./views/AddSong/AddSong";

ReactDOM.render(
    <Router>
        <Switch>
            <Route path="/add-song">
                <AddSong/>
            </Route>
            <Route path="/">
                <LandingPage/>
            </Route>
        </Switch>
    </Router>,
    document.getElementById('root')
);

reportWebVitals();
