import React, {Component} from 'react';
import {NavLink} from 'react-router-dom';

export default class Dashboard extends Component {
  render() {
    return (
      <div className="background">
        <div className="link">
          <NavLink to="/login">Log out</NavLink>
        </div>
      </div>
    );
  }
}
