import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';

class TeamItem extends Component {
  render() {
    const { id, name, emails } = this.props.team;
    return (
      <tr>
        <td>
          <NavLink to={`teams/${id}`}>{name}</NavLink>
        </td>
        <td>test members</td>
        <td>{emails}</td>
      </tr>
    );
  }
}

export default TeamItem;
