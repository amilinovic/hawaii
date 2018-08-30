import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';

class TeamItem extends Component {
  render() {
    const {
      team: { id, name, emails }
    } = this.props;
    return (
      <tr>
        <td>
          <NavLink to={`edit-team/${id}`}>{name}</NavLink>
        </td>
        <td>test members</td>
        <td>{emails}</td>
      </tr>
    );
  }
}

export default TeamItem;
