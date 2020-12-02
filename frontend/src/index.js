import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';
import {BrowserRouter as Router, Switch, Route} from "react-router-dom";
import LandingPage from "./views/LandingPage/LandingPage";

ReactDOM.render(
  <Router>
    <Switch>
      <Route path="/">
        <LandingPage />
      </Route>
    </Switch>
  </Router>,
  document.getElementById('root')
);

reportWebVitals();
